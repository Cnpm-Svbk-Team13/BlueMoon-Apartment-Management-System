-- BlueMoon BFMS - Sprint 1 application privileges, PostgreSQL 16/17.
-- Run as the database/schema owner AFTER schema.sql.
-- The DBA must create bluemoon_app separately and supply its password outside Git.
-- psql --set=ON_ERROR_STOP=1 --file=database/permissions.sql.

BEGIN;

DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_roles WHERE rolname = 'bluemoon_app') THEN
        RAISE EXCEPTION 'Role bluemoon_app does not exist; create it outside Git first';
    END IF;
END;
$$;

REVOKE CREATE ON SCHEMA public FROM PUBLIC;
REVOKE CREATE ON SCHEMA public FROM bluemoon_app;
GRANT USAGE ON SCHEMA public TO bluemoon_app;

REVOKE ALL ON ALL TABLES IN SCHEMA public FROM bluemoon_app;
GRANT SELECT, INSERT, UPDATE
    ON persons, users, apartments, households, residents
    TO bluemoon_app;
GRANT SELECT, INSERT
    ON household_head_history, audit_logs
    TO bluemoon_app;

REVOKE ALL ON ALL SEQUENCES IN SCHEMA public FROM bluemoon_app;
GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA public TO bluemoon_app;

REVOKE EXECUTE ON ALL FUNCTIONS IN SCHEMA public FROM PUBLIC;
GRANT EXECUTE ON ALL FUNCTIONS IN SCHEMA public TO bluemoon_app;
ALTER DEFAULT PRIVILEGES IN SCHEMA public REVOKE EXECUTE ON FUNCTIONS FROM PUBLIC;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT EXECUTE ON FUNCTIONS TO bluemoon_app;

COMMIT;
