---
name: bluemoon-docs-audit
description: Audit BlueMoon requirements, traceability, Sprint documents and their agreement with code/configuration. Use for documentation consistency reviews; do not treat an audit as permission to implement business features.
---

# BlueMoon documentation audit

Find the repository root from Git. Read `docs/PROJECT_CONVENTIONS.md` first; resolve paths from the root, not this skill directory.

- Compare requested documents with canonical US/AC, traceability, Sprint plan/backlog, schema and actual source. Search before concluding something is absent.
- Keep US-001…011 for business; US-SEC-01/02 for accounts/authentication (NFR-02). Core Workflow 1 is authentication; Business Invariant 1 is voluntary funds.
- Check roles, dates, estimates, lock boundaries, claim status, links and technical versions. Keep TV codes; Sơn leads, Khánh remains BA/UAT.
- Missing planned UAT/test reports are future deliverables. Historical `.agents/teamwork` records are not current policy.
- Return findings with file/line, evidence, impact and correction. Separate observed mismatches, decisions needing owner input and unverified external facts.
- When corrections are authorized, update canonical definitions and consumers together; preserve useful comments and explain changes. Never rewrite RAW wishes merely to match code.
- Recheck links/stale references. Distinguish designed, implemented, executed and accepted; record commands and limits.

No database writes, GitHub mutations or new installations are implied.
