# Sprint 1 apartment and household backend handoff

Scope: DB-01, BE-01, BE-02, DB-02 and BE-03. Traceability: US-001, RAW-001/002, NFR-02.

## Database and authentication

- The SQL baseline is commit `e40c1cbeaf33fadd7d7be99a28a8da053e2758f1` for [schema, permissions and seed](../../../database/README.md). Prepare the scripts in that order on an empty, isolated database with synthetic seed data.
- Run bootstrap and seed as the database owner. Configure the application with the `bluemoon_app` role and supply `DB_URL`, `DB_USERNAME` and `DB_PASSWORD` through local environment settings. Hibernate uses schema validation.
- The apartment and household controllers resolve the authenticated principal username to an active `ROLE_ADMIN` or `ROLE_ACCOUNTANT` in `users`. Hoà's login integration must expose the canonical database username as the Spring Security principal name.
- Audit and head history persist the authenticated actor ID. History currently maps `changed_by_user_id` as a scalar ID while the shared Java `User` entity is pending. PostgreSQL enforces the foreign key.

## Web contract

| Method and route | Purpose |
| --- | --- |
| `GET /admin/apartments?floor=6` | List apartments, optionally filtered by floor |
| `GET /admin/apartments/new`, `POST /admin/apartments` | Show and submit the create form |
| `GET /admin/apartments/{id}/edit`, `POST /admin/apartments/{id}` | Show and submit the edit form |
| `GET /admin/apartments/{id}/household` | Show the active head, assignment form and snapshots |
| `POST /admin/apartments/{id}/household/lookup` | Look up an existing person by `headPerson.nationalId` |
| `POST /admin/apartments/{id}/household` | Assign or replace a head |
| `GET /admin/apartments/{id}/household?editProfile=true`, `POST /admin/apartments/{id}/household/profile` | Show and submit the current head profile |

The head form submits the nested `headPerson` fields, `expectedHeadPersonId` for stale-request detection, and `confirmedPersonId` after selecting an existing person. An initial assignment requires `movedInDate`. Replacement requires `previousHeadRelationship` when the outgoing head has a residence record in that household.

Đức Anh can use the current Thymeleaf views as a reference for FE-04/FE-05. Actor IDs come from the authenticated server context. Snapshots, current status and actor IDs must be read from the server when presenting history.

## Verification

Run the local suite and quality gate on Windows:

```powershell
.\mvnw.cmd --batch-mode --no-transfer-progress clean verify
.\mvnw.cmd --batch-mode --no-transfer-progress clean verify -Psprint1-quality
```

MockMvc and H2 exercise form binding, server-side access checks, database writes and rollback with synthetic users. The H2 head tests replace the PostgreSQL advisory lock with a mock.

Local result on 2026-11-07: `clean verify -Psprint1-quality` succeeded. 32 tests passed and the two opt-in PostgreSQL tests were skipped. JaCoCo measured 300 of 331 Service and Controller lines, or 90.6%. PostgreSQL execution, database-backed login and UAT remain pending for acceptance.

For the PostgreSQL scenarios, prepare a disposable local database named `bluemoon_sprint1_test` with the three SQL scripts above. The opt-in test accepts a loopback JDBC URL and uses `bluemoon_app`. It commits synthetic test records for independent connection checks, so use a disposable database.

```powershell
$env:BLUEMOON_PG_TEST_URL = "jdbc:postgresql://localhost:5432/bluemoon_sprint1_test"
$env:BLUEMOON_PG_TEST_PASSWORD = Read-Host "Enter the disposable database app password" -MaskInput
.\mvnw.cmd --batch-mode --no-transfer-progress "-Dtest=Sprint1PostgresTest" test
Remove-Item Env:BLUEMOON_PG_TEST_PASSWORD
Remove-Item Env:BLUEMOON_PG_TEST_URL
```

Check the report for two executed PostgreSQL cases covering concurrent apartment creation and concurrent head assignment/replacement. Run the existing isolated schema verification for constraints, grants and deferred trigger behavior. Docker engine connectivity was unavailable during this implementation.

Huy supplies PostgreSQL and business test evidence. Khánh compiles UAT results. Hoà reviews Sơn's PR, and Sơn reviews Hoà's PR and checks the acceptance evidence under PM-03. Logical deletion remains open for a team scope decision.
