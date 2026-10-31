---
name: bluemoon-verification
description: Select and report verification for BlueMoon code, build, database or documentation changes. Use before handoff or Sprint acceptance; distinguish H2 smoke tests from business and PostgreSQL validation.
---

# BlueMoon verification

Find the repository root; read `docs/PROJECT_CONVENTIONS.md`. Inspect the diff, build configuration and affected US/AC before selecting checks.

- Java/build changes: Maven Wrapper `--batch-mode --no-transfer-progress clean verify` (Windows `.\mvnw.cmd`, Linux `sh ./mvnw`). Record failures; do not bypass download/approval restrictions.
- Smoke test with profile `test`/H2 proves startup and isolated connection only. It does not accept routes, RBAC, lockout or owner history.
- Business tests cover affected AC/boundaries/failures. Login includes fifth failure, disabled account, before/exactly/after expiry and reset. Derive CFG from reviewed code, not a fixed path count.
- Schema/migration changes need isolated PostgreSQL verification; H2 does not establish regex/partial index compatibility. Do not apply scripts to shared resident databases without authorization.
- JaCoCo HTML/XML: `target/site/jacoco/`. Sprint acceptance: `clean verify -Psprint1-quality`, >=85% aggregate Service/Controller line coverage plus business/UAT evidence. Missing coverage data is not success. Empty controller constructors do not prove workflows.
- Docs/skills: check canonical IDs, links, planned versus present outputs, estimates and frontmatter/references. Avoid tests merely matching wording.
- Repeat checks only after relevant edits or unresolved failures. Local verify does not prove remote Actions ran.
- Report behavior changes, commands, test counts, measured scope, results, pending checks and limits. Separate designed, implemented, executed and accepted.
