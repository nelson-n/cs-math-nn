# Session log

Newest first. Each entry gives the date, what was done, and open threads.

## 2026-10-18: APIs lesson setup

- Created `AdditionalTopics/APIs/`. The owner wants general API notes (not only web APIs) plus a demo API script.
- `APINotes.md` is a skeleton (section headings only), to be filled in during the lesson.
- `DemoAPI.py` is a working stdlib-only (`http.server`) REST API for an in-memory notes resource (GET/POST/PATCH/DELETE, JSON, 200/201/204/400/404). Tested with curl. It responds as HTTP/1.0 because `BaseHTTPRequestHandler` defaults to that.
- Linked both from `AdditionalTopics/README.md` and the root README (new APIs section and TOC link). Updated `repo-overview.md`.
- Later filled in every section of `APINotes.md`.
- Open: nothing required. A possible follow-up is rewriting the demo in FastAPI to compare.

## 2026-10-13: HTTP / Chrome Network tab notes

- Owner is working through a lesson on the Chrome Network tab and HTTP.
- Added skeleton files `AdditionalTopics/NetworkInternet/HTTPNotes.md` (section headings only) and `ChromeNetworkTabCheatsheet.md` (group headings only). Linked both from `AdditionalTopics/README.md` and the root README's Networking / Internet section.
- Filled in the Status Codes section of `HTTPNotes.md` (1xx–5xx classes with common codes). Later filled in all other sections (request/response cycle, URLs, methods, headers, cookies/sessions, caching, HTTPS/TLS, HTTP versions). The owner removed the Chrome Network Tab section from `HTTPNotes.md`. The owner later deleted `ChromeNetworkTabCheatsheet.md`, and its README links were removed on 2026-10-18.
- Open: the owner fills in the content as the lesson goes. The README descriptions may need adjusting to match.

## 2026-09-30: Lean demo

- Added `Math/Lean/SimpleDemo.lean`, a minimal first test for the Lean install (`theorem two_plus_two : 2 + 2 = 4 := rfl`).
- Added `Math/Lean/LeanDemo.lean`, a core-only Lean 4 intro (no Mathlib, no lake project). It covers #eval/#check, recursive defs, rfl/decide, propositional logic, ∃, induction, and two `sorry` exercises. Linked it from `Math/README.md` and the root README.
- Lean was not installed on the machine, so neither file has been compile-checked yet.
- 2026-10-01: Owner installed Lean 4.34.1 and renamed the files to `Lean.lean` and `LeanDemos.lean`. Both check cleanly with `lean <file>`. A Mathlib Lake project was tried for derivative and integral examples, but the owner declined it because of the download size, so stay core-only. Real numbers, `deriv` and integrals are unavailable.

## 2026-09-30: agent-context bootstrap

- Read through the repo and created `agent-context/` (overview, conventions, this log) and a root `CLAUDE.md` pointing to it.
- No content or code was changed.
- Also wrote a `known-issues.md` listing bugs and hygiene problems. The owner said that isn't a concern for a learning repo, so it was removed and the "code doesn't have to run" rule was added to `repo-overview.md`.
