"""Isolated PostgreSQL checks: US-001/US-SEC-01/02, DB-01/02, QA-05.
Uses only Python stdlib + Docker. Never connects to a shared database.
"""
import argparse
import json
from pathlib import Path
import subprocess
import time
import uuid

ROOT = Path(__file__).resolve().parents[2]
PARSER = argparse.ArgumentParser(description="Verify the current BlueMoon ERD on isolated PostgreSQL")
PARSER.add_argument("--image", default="postgres:17-alpine",
                    help="Docker image, for example postgres:16-alpine")
ARGS = PARSER.parse_args()
IMAGE = ARGS.image
NAME = "bluemoon-erd-" + uuid.uuid4().hex[:10]
RESULTS = []

def docker(*args, **kwargs):
    return subprocess.run(["docker", *args], text=True, encoding="utf-8",
                          capture_output=True, timeout=60, **kwargs)

def sql(statement, database="postgres"):
    return docker("exec", "-i", NAME, "psql", "-X", "-U", "postgres", "-d", database,
                  "-v", "ON_ERROR_STOP=1", "-v", "VERBOSITY=verbose", "-At", input=statement)

def check(name, statement, state=None, value=None):
    r = sql(statement)
    ok = (r.returncode == 0) if state is None else (r.returncode != 0 and state in r.stderr)
    if value is not None:
        ok = ok and value in r.stdout.splitlines()
    RESULTS.append({"name": name, "passed": ok})
    print(("PASS " if ok else "FAIL ") + name, flush=True)
    if not ok:
        raise AssertionError(r.stdout + r.stderr)

def reject(name, body, state="23514"):
    check(name, "BEGIN; " + body + "; SET CONSTRAINTS ALL IMMEDIATE; ROLLBACK;", state)

def accept(name, body):
    check(name, "BEGIN; " + body + "; SET CONSTRAINTS ALL IMMEDIATE; ROLLBACK;")

def pid(email):
    return "(SELECT id FROM persons WHERE email='" + email + "')"

def hid(code="HK-BM-00601"):
    return "(SELECT id FROM households WHERE registration_code='" + code + "')"

def account(username, email):
    return "INSERT INTO users(username,password_hash,person_id,role) VALUES ('" + username + "','test-hash'," + pid(email) + ",'ROLE_ACCOUNTANT')"

def race(name, first, second, expected="23514"):
    # A holds the invariant lock while sleeping. B must wait until A commits,
    # then its trigger must see A's committed change and reject the conflict.
    app = "race_" + uuid.uuid4().hex[:8]
    proc = subprocess.Popen(["docker", "exec", "-i", NAME, "psql", "-X", "-U", "postgres",
                             "-v", "ON_ERROR_STOP=1", "-At"], stdin=subprocess.PIPE,
                            stdout=subprocess.PIPE, stderr=subprocess.PIPE, text=True, encoding="utf-8")
    proc.stdin.write("SET application_name='" + app + "'; BEGIN; " + first + "; SELECT pg_sleep(4); COMMIT;\n")
    proc.stdin.close()
    for _ in range(60):
        r = sql("SELECT count(*) FROM pg_stat_activity WHERE application_name='" + app + "' AND wait_event='PgSleep';")
        if r.stdout.strip() == "1":
            break
        time.sleep(0.05)
    else:
        proc.kill()
        raise AssertionError("First connection never reached lock-holding wait")
    started = time.monotonic()
    r = sql("BEGIN; " + second + "; COMMIT;")
    elapsed = time.monotonic() - started
    proc.wait(timeout=15)
    out, err = proc.stdout.read(), proc.stderr.read()
    ok = proc.returncode == 0 and r.returncode != 0 and expected in r.stderr and elapsed > 1
    RESULTS.append({"name": name, "passed": ok, "blocked_seconds": round(elapsed, 2)})
    print(("PASS " if ok else "FAIL ") + name, flush=True)
    if not ok:
        raise AssertionError(out + err + r.stdout + r.stderr)

def seed_while_writer():
    """Seed must time out and rollback when an application writer is active."""
    app = "seed_writer_" + uuid.uuid4().hex[:8]
    proc = subprocess.Popen(["docker", "exec", "-i", NAME, "psql", "-X", "-U", "postgres",
                             "-v", "ON_ERROR_STOP=1", "-At"], stdin=subprocess.PIPE,
                            stdout=subprocess.PIPE, stderr=subprocess.PIPE, text=True, encoding="utf-8")
    proc.stdin.write("SET application_name='" + app + "'; BEGIN; "
                     "INSERT INTO persons(full_name) VALUES ('seed lock holder'); "
                     "SELECT pg_sleep(5); ROLLBACK;\n")
    proc.stdin.close()
    for _ in range(60):
        r = sql("SELECT count(*) FROM pg_stat_activity WHERE application_name='" + app
                + "' AND wait_event='PgSleep';")
        if r.stdout.strip() == "1":
            break
        time.sleep(0.05)
    else:
        proc.kill()
        raise AssertionError("Seed writer never reached lock-holding wait")
    started = time.monotonic()
    seeded = sql((ROOT / "database/seed_data.sql").read_text(encoding="utf-8"))
    elapsed = time.monotonic() - started
    proc.wait(timeout=15)
    out, err = proc.stdout.read(), proc.stderr.read()
    remaining = sql("SELECT count(*) FROM persons;")
    ok = (proc.returncode == 0 and seeded.returncode != 0 and "55P03" in seeded.stderr
          and 1 < elapsed < 4 and remaining.stdout.strip() == "0")
    RESULTS.append({"name": "seed maintenance lock timeout", "passed": ok,
                    "blocked_seconds": round(elapsed, 2)})
    print(("PASS " if ok else "FAIL ") + "seed maintenance lock timeout", flush=True)
    if not ok:
        raise AssertionError(out + err + seeded.stdout + seeded.stderr + remaining.stdout + remaining.stderr)

try:
    r = docker("run", "-d", "--name", NAME, "--network", "none", "-e",
               "POSTGRES_HOST_AUTH_METHOD=trust", IMAGE)
    if r.returncode:
        raise RuntimeError(r.stderr)
    for _ in range(60):
        if docker("exec", NAME, "pg_isready", "-U", "postgres").returncode == 0:
            break
        time.sleep(0.2)
    version = sql("SHOW server_version;").stdout.strip()
    check("bootstrap", (ROOT / "database/schema.sql").read_text(encoding="utf-8"))
    check("seven tables", "SELECT count(*) FROM pg_tables WHERE schemaname='public';", value="7")
    check("bootstrap repeated rejected", (ROOT / "database/schema.sql").read_text(encoding="utf-8"), "42P07")
    check("tables preserved after repeat", "SELECT count(*) FROM pg_tables WHERE schemaname='public';", value="7")
    check("create application role", "CREATE ROLE bluemoon_app NOLOGIN;")
    check("apply application privileges", (ROOT / "database/permissions.sql").read_text(encoding="utf-8"))
    # Check partial seed rejection before loading real fixtures.
    check("partial fixture", "INSERT INTO persons(full_name) VALUES ('partial');")
    check("partial seed rejected", (ROOT / "database/seed_data.sql").read_text(encoding="utf-8"), "P0001")
    check("partial seed unchanged", "SELECT count(*) FROM persons;", value="1")
    check("remove isolated partial fixture", "DELETE FROM persons WHERE full_name='partial';")
    seed_while_writer()
    check("seed", (ROOT / "database/seed_data.sql").read_text(encoding="utf-8"))
    check("seed repeat rejected", (ROOT / "database/seed_data.sql").read_text(encoding="utf-8"), "P0001")
    check("fixture counts", "SELECT (SELECT count(*) FROM users)||','||(SELECT count(*) FROM persons)||','||(SELECT count(*) FROM apartments)||','||(SELECT count(*) FROM households)||','||(SELECT count(*) FROM residents)||','||(SELECT count(*) FROM household_head_history)||','||(SELECT count(*) FROM audit_logs);", value="4,11,7,3,6,4,4")
    reject("account missing person", "INSERT INTO users(username,password_hash,role) VALUES ('bad','x','ROLE_ADMIN')", "23502")
    reject("account unknown person", "INSERT INTO users(username,password_hash,role,person_id) VALUES ('bad','x','ROLE_ADMIN',-1)", "23503")
    reject("account duplicate person", account("duplicate", "admin@example.test"), "23505")
    reject("account missing contact", "INSERT INTO persons(full_name,email) VALUES ('missing','missing@example.test'); " + account("missing", "missing@example.test"))
    reject("account invalid email", "UPDATE persons SET email='invalid' WHERE id=" + pid("admin@example.test"))
    reject("account phone removed", "UPDATE persons SET phone=NULL WHERE id=" + pid("admin@example.test"))
    reject("account email duplicate on edit", "UPDATE persons SET email='admin@example.test' WHERE id=" + pid("ketoan@example.test"), "23505")
    reject("account email duplicate on create", "INSERT INTO persons(full_name,email,phone) VALUES ('dup','admin@example.test','0900000999'); INSERT INTO users(username,password_hash,role,person_id) SELECT 'dup','x','ROLE_ADMIN',max(id) FROM persons", "23505")
    accept("non-account email may duplicate", "INSERT INTO persons(full_name,email) VALUES ('ordinary','admin@example.test')")
    reject("full name must be trimmed", "INSERT INTO persons(full_name) VALUES (' padded ')")
    reject("email must be lowercase", "INSERT INTO persons(full_name,email) VALUES ('email case','Case@Example.test')")
    reject("username must be lowercase", "INSERT INTO persons(full_name,email,phone) VALUES ('user case','user-case@example.test','0900000777'); INSERT INTO users(username,password_hash,person_id,role) SELECT 'UserCase','x',id,'ROLE_ACCOUNTANT' FROM persons WHERE email='user-case@example.test'")
    reject("room number must be uppercase", "INSERT INTO apartments(room_number,floor_number,area_sqm,apartment_type) VALUES ('p.999',9,50,'RESIDENTIAL')")
    reject("registration code must be uppercase", "INSERT INTO households(apartment_id,head_person_id,registration_code) SELECT apartment_id,head_person_id,'hk-lower' FROM households LIMIT 1")
    accept("canonical business keys accepted", "INSERT INTO apartments(room_number,floor_number,area_sqm,apartment_type) VALUES ('P.999',9,50,'RESIDENTIAL')")
    check("same floor different types", "SELECT count(DISTINCT apartment_type) FROM apartments WHERE floor_number=6;", value="2")
    for name, assignment in [("floor", "floor_number=31"), ("type", "apartment_type='BAD'"), ("area", "area_sqm=0"), ("renovation out of scope", "status='RENOVATING'")]:
        reject("invalid " + name, "UPDATE apartments SET " + assignment + " WHERE room_number='P.601'")
    reject("household apartment immutable", "UPDATE households SET apartment_id=(SELECT id FROM apartments WHERE room_number='P.701') WHERE id=" + hid())
    reject("one active household", "INSERT INTO households(apartment_id,head_person_id,registration_code) SELECT apartment_id,head_person_id,'DUPLICATE' FROM households WHERE id=" + hid(), "23505")
    accept("historical household unknown moved-out date permitted", "INSERT INTO households(apartment_id,head_person_id,registration_code,status) SELECT apartment_id,head_person_id,'OLD','MOVED_OUT' FROM households WHERE id=" + hid())
    reject("active household cannot have moved-out date", "UPDATE households SET moved_out_date=moved_in_date WHERE id=" + hid())
    reject("moved-out date cannot precede moved-in date", "UPDATE households SET status='MOVED_OUT',moved_out_date=moved_in_date-1 WHERE id=" + hid())
    accept("known valid moved-out date permitted", "UPDATE households SET status='TRANSFERRED',moved_out_date=moved_in_date WHERE id=" + hid())
    reject("HEAD cannot be other person", "UPDATE residents SET relationship_with_head='HEAD' WHERE relationship_with_head='CHILD'")
    reject("head resident must be HEAD", "UPDATE residents SET relationship_with_head='OTHER' WHERE household_id=" + hid() + " AND relationship_with_head='HEAD'")
    reject("unknown relationship", "UPDATE residents SET relationship_with_head='unknown'")
    reject("temporary dates required", "UPDATE residents SET residence_status='TAM_TRU',start_date=NULL,end_date=NULL")
    reject("permanent residence cannot carry dates", "UPDATE residents SET start_date='2026-01-01',end_date='2026-12-31' WHERE residence_status='THUONG_TRU'")
    accept("head and relationships change atomically", "UPDATE households SET head_person_id=" + pid("an@example.test") + " WHERE id=" + hid() + "; UPDATE residents SET relationship_with_head='OTHER' WHERE household_id=" + hid() + " AND relationship_with_head='HEAD'")
    reject("changing head without relationships fails", "UPDATE households SET head_person_id=" + pid("an@example.test") + " WHERE id=" + hid())
    reject("snapshot invalid", "UPDATE persons SET full_name='rollback marker' WHERE id=" + pid("admin@example.test") + "; INSERT INTO household_head_history(household_id,new_head_person_id,new_snapshot,changed_by_user_id) SELECT h.id,h.head_person_id,'{}',u.id FROM households h CROSS JOIN users u LIMIT 1")
    check("failed transaction rolled back", "SELECT count(*) FROM persons WHERE full_name='rollback marker';", value="0")
    reject("snapshot full name type", "INSERT INTO household_head_history(household_id,new_head_person_id,new_snapshot,changed_by_user_id) SELECT household_id,new_head_person_id,jsonb_set(new_snapshot,'{full_name}','123'::jsonb),changed_by_user_id FROM household_head_history LIMIT 1")
    reject("snapshot full name length", "INSERT INTO household_head_history(household_id,new_head_person_id,new_snapshot,changed_by_user_id) SELECT household_id,new_head_person_id,jsonb_set(new_snapshot,'{full_name}',to_jsonb(repeat('a',101))),changed_by_user_id FROM household_head_history LIMIT 1")
    reject("snapshot national ID format", "INSERT INTO household_head_history(household_id,new_head_person_id,new_snapshot,changed_by_user_id) SELECT household_id,new_head_person_id,jsonb_set(new_snapshot,'{national_id}','\"123\"'::jsonb),changed_by_user_id FROM household_head_history LIMIT 1")
    reject("snapshot phone format", "INSERT INTO household_head_history(household_id,new_head_person_id,new_snapshot,changed_by_user_id) SELECT household_id,new_head_person_id,jsonb_set(new_snapshot,'{phone}','\"123\"'::jsonb),changed_by_user_id FROM household_head_history LIMIT 1")
    reject("snapshot email format", "INSERT INTO household_head_history(household_id,new_head_person_id,new_snapshot,changed_by_user_id) SELECT household_id,new_head_person_id,jsonb_set(new_snapshot,'{email}','\"BAD\"'::jsonb),changed_by_user_id FROM household_head_history LIMIT 1")
    accept("snapshot extension key permitted", "INSERT INTO household_head_history(household_id,previous_head_person_id,new_head_person_id,previous_snapshot,new_snapshot,changed_by_user_id) SELECT household_id,new_head_person_id,new_head_person_id,new_snapshot,new_snapshot || '{\"source\":\"service\"}'::jsonb,changed_by_user_id FROM household_head_history LIMIT 1")
    accept("profile updated event", "INSERT INTO household_head_history(household_id,previous_head_person_id,new_head_person_id,previous_snapshot,new_snapshot,changed_by_user_id) SELECT household_id,new_head_person_id,new_head_person_id,new_snapshot,new_snapshot,changed_by_user_id FROM household_head_history LIMIT 1")
    for operation in ["UPDATE household_head_history SET changed_at=now()", "DELETE FROM household_head_history", "TRUNCATE household_head_history", "DELETE FROM audit_logs"]:
        reject("append only: " + operation, operation)
    check("history apartment join", "SELECT count(*) FROM household_head_history x JOIN households h ON h.id=x.household_id JOIN apartments a ON a.id=h.apartment_id;", value="4")
    reject("non READ COMMITTED rejected", "SET TRANSACTION ISOLATION LEVEL REPEATABLE READ; UPDATE persons SET full_name=full_name", "25000")
    reject("duplicate national ID", "INSERT INTO persons(full_name,national_id) VALUES ('duplicate','000000000001')", "23505")
    reject("malformed national ID", "INSERT INTO persons(full_name,national_id) VALUES ('invalid','123')")
    reject("malformed phone", "UPDATE persons SET phone='123' WHERE id=" + pid("admin@example.test"))
    reject("end before start", "UPDATE residents SET residence_status='TAM_VANG',start_date='2026-11-04',end_date='2026-11-03'")
    accept("snapshot survives profile edit", "UPDATE persons SET phone='0900000888' WHERE id=" + pid("an@example.test") + "; DO $$ BEGIN IF NOT EXISTS (SELECT 1 FROM household_head_history WHERE new_head_person_id=" + pid("an@example.test") + " AND new_snapshot->>'phone'='0900000001') THEN RAISE EXCEPTION 'snapshot changed'; END IF; END $$")
    reject("history actor required", "INSERT INTO household_head_history(household_id,new_head_person_id,new_snapshot) SELECT household_id,new_head_person_id,new_snapshot FROM household_head_history LIMIT 1", "23502")
    reject("history previous pair required", "INSERT INTO household_head_history(household_id,previous_head_person_id,new_head_person_id,new_snapshot,changed_by_user_id) SELECT household_id,new_head_person_id,new_head_person_id,new_snapshot,changed_by_user_id FROM household_head_history LIMIT 1")
    accept("application role can update business table", "SET ROLE bluemoon_app; UPDATE persons SET full_name=full_name WHERE id=" + pid("admin@example.test"))
    accept("application role can append history", "SET ROLE bluemoon_app; INSERT INTO household_head_history(household_id,previous_head_person_id,new_head_person_id,previous_snapshot,new_snapshot,changed_by_user_id) SELECT household_id,new_head_person_id,new_head_person_id,new_snapshot,new_snapshot,changed_by_user_id FROM household_head_history LIMIT 1")
    reject("application role cannot delete business rows", "SET ROLE bluemoon_app; DELETE FROM persons WHERE full_name='nobody'", "42501")
    reject("application role cannot update history", "SET ROLE bluemoon_app; UPDATE household_head_history SET changed_at=changed_at", "42501")
    reject("application role cannot truncate history", "SET ROLE bluemoon_app; TRUNCATE household_head_history", "42501")
    reject("application role cannot alter tables", "SET ROLE bluemoon_app; ALTER TABLE persons DISABLE TRIGGER trg_persons_updated_at", "42501")
    check("race people setup", "INSERT INTO persons(full_name,email,phone) VALUES ('race-a','race@example.test','0900000801'),('race-b','race@example.test','0900000802'),('race-c','race-c@example.test','0900000803');")
    def by_name(username, name):
        return "INSERT INTO users(username,password_hash,person_id,role) SELECT '" + username + "','x',id,'ROLE_ACCOUNTANT' FROM persons WHERE full_name='" + name + "'"
    race("concurrent account creation same email", by_name("race-a", "race-a"), by_name("race-b", "race-b"), "23505")
    race("email edit versus account creation", "UPDATE persons SET email='race-c@example.test' WHERE full_name='race-a'", by_name("race-c", "race-c"), "23505")
    check("reverse race setup", "INSERT INTO persons(full_name,email,phone) VALUES ('race-d','race-d@example.test','0900000804'),('race-e','race-e@example.test','0900000805');")
    race("account creation versus email edit", by_name("race-d", "race-d"), "UPDATE persons SET email='race-d@example.test' WHERE full_name='race-a'", "23505")
    race("account creation versus phone removal", by_name("race-e", "race-e"), "UPDATE persons SET phone=NULL WHERE full_name='race-e'")
    race("concurrent account email edits", "UPDATE persons SET email='race-final@example.test' WHERE full_name='race-a'", "UPDATE persons SET email='race-final@example.test' WHERE id=" + pid("ketoan@example.test"), "23505")
    race("head change versus resident update", "UPDATE households SET head_person_id=" + pid("an@example.test") + " WHERE id=" + hid() + "; UPDATE residents SET relationship_with_head='OTHER' WHERE household_id=" + hid() + " AND relationship_with_head='HEAD'", "UPDATE residents SET relationship_with_head='HEAD' WHERE person_id=" + pid("lan@example.test"))
    check("final HEAD invariant", "SELECT count(*) FROM residents r JOIN households h ON h.id=r.household_id WHERE (r.relationship_with_head='HEAD')<>(r.person_id=h.head_person_id);", value="0")
    check("final email invariant", "SELECT count(*) FROM (SELECT p.email FROM users u JOIN persons p ON p.id=u.person_id GROUP BY p.email HAVING count(*)>1) duplicates;", value="0")
    print(json.dumps({"image": IMAGE, "postgres": version, "passed": len(RESULTS),
                      "results": RESULTS}, ensure_ascii=False), flush=True)
finally:
    # Only the uniquely named container created above; no shared DB or volumes.
    docker("rm", "-f", NAME)
