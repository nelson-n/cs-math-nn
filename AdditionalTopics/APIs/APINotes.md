# API Notes

## Sections
* What is an API
* Kinds of APIs (Library, OS, Web)
* API Design Principles
* Web API Styles (REST, GraphQL, gRPC, Webhooks)
* Designing REST Endpoints
* Request and Response Formats
* Errors
* Authentication and Authorization
* Versioning
* Pagination, Filtering, and Rate Limiting
* Documentation (OpenAPI)
* Testing an API

---

### What is an API
* **API** = Application Programming Interface: the defined way one piece of software lets other software use it.
* It is a *contract*: which operations exist, what inputs they take, what they return, and what errors can happen. The implementation behind it is hidden.
* The caller depends on the contract, not the internals, so the internals can change freely as long as the contract holds.
* Anywhere code talks to other code across a boundary, there is an API.

### Kinds of APIs (Library, OS, Web)
* **Library / language API**: normal function calls inside one program, e.g. `np.dot(a, b)`, `list.append()`, `torch.nn.Linear`.
* **Operating system API**: system calls such as `open()`, `read()`, `fork()` (POSIX) or Win32 on Windows. The program asks the kernel to do something.
* **Hardware / driver API**: CUDA, OpenGL, Vulkan. Code talks to the GPU through a driver.
* **Database API**: SQL itself, or drivers like JDBC and Python's DB-API.
* **Web API**: a service reached over the network with HTTP requests, usually returning JSON (Stripe, GitHub, OpenAI / Anthropic).
* When people say "our API", "API key", or "call the API", they almost always mean a web API. If there is an endpoint, a URL, or an API key, it is a web API.
* See `HTTPNotes.md` in `NetworkInternet/` for the HTTP concepts web APIs are built on.

### API Design Principles
* **Hide the implementation**: expose what callers need, not how it works.
* **Be consistent**: the same naming, structure, and error shape everywhere. Surprises are bugs in the design.
* **Be predictable**: a caller should be able to guess how an unseen endpoint behaves from the ones they have seen.
* **Make errors explicit and useful**: say what went wrong and how to fix it.
* **Keep it stable**: once people depend on an API, breaking changes are costly. Adding is safe, removing or renaming is not.
* **Keep it small**: everything exposed must be supported forever. Easy to add, hard to remove.
* **Idempotency**: design so that retrying a request is safe where possible (see Methods in `HTTPNotes.md`).
* Web APIs add network concerns: latency, partial failure, authentication, rate limits, and serialization.

### Web API Styles (REST, GraphQL, gRPC, Webhooks)

#### REST
* Resources are named by URLs (nouns), and HTTP methods are the actions (verbs).
* Uses HTTP status codes, headers, and caching directly. Usually JSON.
* **Stateless**: each request carries everything the server needs, so any server can handle it.
* The most common style, and the one `DemoAPI.py` uses.
* Weaknesses: **over-fetching** (the response has fields you do not need), **under-fetching** (you need several requests to assemble one screen).

#### GraphQL
* One endpoint (usually `POST /graphql`). The client sends a query that names exactly the fields it wants, and gets back JSON with exactly that shape.
* Solves over- and under-fetching, and is strongly typed through a schema.
* Costs: harder to cache with plain HTTP, and queries can be expensive so servers need to limit them.

```
query { user(id: 42) { name, posts { title } } }
```

#### gRPC
* Uses HTTP/2 and **Protocol Buffers** (a compact binary format) defined in `.proto` files, with client and server code generated from them.
* Fast and strongly typed, supports streaming. Common for service-to-service calls inside a company.
* Not browser-friendly and not human-readable like JSON.

#### Webhooks
* The reverse direction: instead of you calling the service, the **service calls your URL** when an event happens (e.g. Stripe sends your server a request when a payment succeeds).
* Avoids polling. You expose an endpoint, and should verify that the request really came from the service (usually a signature header).

#### Choosing
* REST is the default for public APIs. GraphQL fits clients with varied data needs (mobile and web apps). gRPC fits internal, high-performance services. Webhooks fit event notifications.

### Designing REST Endpoints
* Use **plural nouns** for collections and put the id in the path. Do not put verbs in URLs.

```
GET    /notes          list notes
POST   /notes          create a note
GET    /notes/42       get note 42
PATCH  /notes/42       update part of note 42
PUT    /notes/42       replace note 42
DELETE /notes/42       delete note 42
```

* Not `/getNote` or `/deleteNote?id=42`. The method already says the action.
* **Nesting** shows relationships: `GET /users/7/notes` lists notes of user 7. Keep it to one or two levels.
* Use the **query string** for optional filtering, sorting, and paging: `GET /notes?author=7&sort=-created`.
* Pick the right method for the semantics: GET and HEAD are safe, GET / PUT / DELETE are idempotent, POST is neither.
* Return the right status code: `201 Created` for POST (with a `Location` header), `204 No Content` for DELETE, `404` for unknown resources.
* Actions that do not fit CRUD can be modeled as a sub-resource, e.g. `POST /orders/42/cancel`.

### Request and Response Formats
* **JSON** is the standard. Set `Content-Type: application/json` on bodies you send and `Accept: application/json` on requests that want JSON back.
* The server must know where a request body ends, which is why `Content-Length` is needed (`read_json` in `DemoAPI.py` reads exactly that many bytes).
* Other formats: form data (`application/x-www-form-urlencoded`, `multipart/form-data` for file uploads), XML (older APIs), Protocol Buffers (gRPC).
* Conventions:
    * Pick one naming style for fields (`snake_case` or `camelCase`) and keep to it.
    * Return objects, not bare arrays or values, at the top level of responses, so fields can be added later without breaking clients (e.g. `{"notes": [...]}` is more extensible than `[...]`).
    * Use ISO 8601 for timestamps (`2026-10-18T12:51:11Z`) and store them in UTC.
    * Do not reuse a field for a different meaning or type.
* JSON has no date, binary, or large-integer types, so these need conventions (strings, base64).

```
POST /notes
Content-Type: application/json

{"text": "learn HTTP"}

HTTP/1.1 201 Created
Content-Type: application/json

{"id": 1, "text": "learn HTTP"}
```

### Errors
* Use the HTTP **status code** to say what kind of failure it was (see Status Codes in `HTTPNotes.md`), and a **JSON body** to say details.
* 4xx = the client's fault (fix the request), 5xx = the server's fault (retry later may work).
* Common choices: `400` malformed, `401` not authenticated, `403` not allowed, `404` not found, `409` conflict, `422` validation failed, `429` rate limited.
* Use the same error shape everywhere so clients can parse it in one place:

```
{"error": {"code": "note_not_found", "message": "note 99 not found"}}
```

* Include a machine-readable `code` (stable, for programs) and a human-readable `message` (may change, for people). For validation errors, list which fields failed.
* Never leak internals (stack traces, SQL, file paths) in error bodies.
* Do not return `200 OK` with an error in the body. Clients and tools rely on the status code.
* `send_error_json` in `DemoAPI.py` enforces one shape for all errors. Without it, `http.server` returns HTML error pages.

### Authentication and Authorization
* **Authentication (authn)**: who are you? **Authorization (authz)**: what are you allowed to do? `401` means authentication failed, `403` means authenticated but not allowed.
* **API keys**: a long random string identifying the calling app, sent in a header (`Authorization: Bearer sk-...` or `X-API-Key`). Simple, but anyone holding the key has full access, so keep it secret and never put it in client-side code or git.
* **Bearer tokens / JWT**: a token sent as `Authorization: Bearer <token>`. A JWT is a signed JSON token holding the user's identity and expiry, which the server verifies without a database lookup.
* **Session cookies**: for browser apps, as in the Cookies and Sessions section of `HTTPNotes.md`.
* **OAuth 2.0**: lets a user grant an app limited access to their account on another service without sharing their password ("Sign in with Google"). The app receives a short-lived **access token** with limited **scopes**, plus a **refresh token** to get new ones.
* Always use **HTTPS**, because keys and tokens sent over plain HTTP can be read by anyone on the network.
* Give keys and tokens the least privilege needed (scopes), allow expiry and revocation, and store only hashes of keys on the server.
* Authorization also means checking *ownership* on every request: user A must not be able to `GET /notes/42` if note 42 belongs to user B, even with a valid login.

### Versioning
* Clients depend on the contract, so changes that break it need a new version. Old clients keep working on the old one.
* **Breaking**: removing or renaming a field or endpoint, changing a type, making an optional parameter required, changing the meaning of a value.
* **Non-breaking**: adding a new endpoint, a new optional field, or a new optional parameter. Clients should ignore fields they do not know.
* Ways to version:
    * **URL path**: `/v1/notes`, `/v2/notes`. Simplest and most common.
    * **Header**: `Accept: application/vnd.myapp.v2+json`, or a custom header. Cleaner URLs, harder to test in a browser.
    * **Date-based**: the client pins a date (e.g. `Anthropic-Version: 2023-06-01`).
* Announce **deprecations** ahead of time, and give a sunset date. Avoid versioning at all if additive changes are enough.

### Pagination, Filtering, and Rate Limiting

#### Pagination
* Never return an unbounded list. Return a page and a way to get the next one.
* **Offset / limit**: `GET /notes?limit=20&offset=40`. Simple, but slow for large offsets and rows can be skipped or repeated if data changes between pages.
* **Cursor-based**: `GET /notes?limit=20&after=abc123`. The response includes an opaque cursor for the next page. Stable and fast, and the preferred approach for large or changing data.

```
{"notes": [...], "next_cursor": "abc123"}
```

#### Filtering and Sorting
* Use query parameters: `?status=open&author=7&sort=-created` (a leading `-` for descending is a common convention).

#### Rate Limiting
* Servers cap how many requests a client can make per time window, to protect themselves from overload and abuse.
* Exceeding it returns `429 Too Many Requests`, usually with a `Retry-After` header saying how many seconds to wait.
* Often advertised in headers such as `X-RateLimit-Limit`, `X-RateLimit-Remaining`, `X-RateLimit-Reset`.
* Clients should **retry with exponential backoff**: wait 1s, 2s, 4s, ... (plus random jitter) before retrying.
* Common algorithms: fixed window, sliding window, token bucket.

### Documentation (OpenAPI)
* An API is only as usable as its docs. Docs should include every endpoint, parameters, example requests and responses, error codes, and authentication.
* **OpenAPI** (formerly Swagger) is the standard machine-readable description of a REST API, written in YAML or JSON. Tools generate interactive docs (Swagger UI), client libraries, and test cases from it.
* Frameworks like **FastAPI** generate the OpenAPI spec automatically from your code and serve interactive docs at `/docs`.

```yaml
paths:
  /notes/{id}:
    get:
      summary: Get one note
      parameters:
        - name: id
          in: path
          required: true
          schema: {type: integer}
      responses:
        "200": {description: The note}
        "404": {description: Note not found}
```

### Testing an API
* See `DemoAPI.py` for a minimal working web API to test against with `curl`.
* **curl**: quick manual checks. `-i` shows status line and headers, `-v` shows the full request and response, `-X` sets the method, `-H` adds a header, `-d` sends a body.
* **Browser**: GET requests can be opened directly, and the Chrome Network tab shows the request, headers, and response for any call the page makes.
* **API clients**: Postman, Insomnia, or the VS Code REST Client, for saving and replaying requests.
* **Automated tests**: call the API from code and assert on the status code and body (e.g. Python `requests` with `pytest`). Test the error cases as well as the happy path: bad JSON, missing fields, unknown ids.
* Checks worth making on every endpoint: correct status code, correct `Content-Type`, response shape, idempotency of PUT / DELETE, and behavior without or with the wrong credentials.
