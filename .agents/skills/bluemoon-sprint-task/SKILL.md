---
name: bluemoon-sprint-task
description: Decompose BlueMoon Sprint stories into student tasks of 2–4 hours with traceability, prerequisites and acceptance criteria. Use for backlog refinement, not feature implementation or automatic GitHub issue creation.
---

# BlueMoon Sprint task decomposition

Find the repository root. Read `docs/PROJECT_CONVENTIONS.md`, the requested Sprint plan, relevant US/AC and backlog before adding tasks.

- Cover every AC, including owner profile/history for US-001. Make data, service, view, test and UAT dependencies explicit without expanding scope.
- Reuse parent task IDs. Split packages larger than four hours into independently reviewable 2–4 hour work units, one primary assignee each.
- Each issue needs action title `[Sprint X][Module] ...`, Traceability (US/RAW/NFR + parent), Layer Impact, Prerequisites, estimate, assignee and acceptance checklist.
- Criteria describe observable behavior. Apply DTO checks only where relevant; documentation/build tasks do not need invented DTOs. Keep business logic in services.
- Sơn leads/data/backend; Hoà security/architecture; Đức Anh UI; Huy QA; Khánh BA/UAT. Sơn and Hoà review each other, never self-approve.
- Separate preparation from execution dependencies: QA/BA can design from AC before UI exists.
- Sum hours by assignee/parent; show added demand. Do not equate story points to hours or promise feasibility from arithmetic.
- Local issue numbers are proposals until GitHub IDs exist. Creating issues, assignments or messages requires explicit authorization.
