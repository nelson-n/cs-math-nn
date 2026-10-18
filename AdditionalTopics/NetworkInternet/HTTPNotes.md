# HTTP Notes

## Sections
* Request / Response Cycle
* URLs
* Methods
* Status Codes
* Headers
* Cookies and Sessions
* Caching
* HTTPS / TLS
* HTTP Versions (1.1, 2, 3)

---

### Request / Response Cycle
* HTTP (HyperText Transfer Protocol) is an application-layer protocol. It sits on top of TCP (or QUIC for HTTP/3), which sits on top of IP.
* HTTP is **client-server**: the client (usually a browser) sends a **request**, and the server sends back exactly one **response**. The server never speaks first.
* HTTP is **stateless**: each request is independent and the server doesn't remember previous requests. Anything that needs memory (logins, carts) is built on top with cookies or tokens.
* What happens when you type a URL and press enter:
    1. **DNS lookup**: the browser resolves the domain name (`example.com`) to an IP address.
    2. **TCP connection**: the browser opens a connection to that IP on port 443 (HTTPS) or 80 (HTTP) with the TCP three-way handshake (SYN, SYN-ACK, ACK).
    3. **TLS handshake** (HTTPS only): client and server agree on encryption keys and the server proves its identity with a certificate.
    4. **Request**: the browser sends the HTTP request.
    5. **Response**: the server sends back a status code, headers, and a body (usually HTML for the first request).
    6. **Sub-resources**: the browser parses the HTML and makes more requests for the CSS, JavaScript, images, fonts, and data it references. One page load is often dozens to hundreds of requests.
    7. **Render**: the browser builds the DOM, applies CSS, runs JavaScript, and paints the page.
* Connections are reused (**keep-alive**), so later requests to the same server skip steps 1–3.

#### Anatomy of a Request
* A request has a **request line** (method, path, version), **headers**, a blank line, and an optional **body**.

```
POST /api/login HTTP/1.1
Host: example.com
Content-Type: application/json
Content-Length: 41

{"username": "nelson", "password": "..."}
```

#### Anatomy of a Response
* A response has a **status line** (version, status code, reason phrase), **headers**, a blank line, and an optional **body**.

```
HTTP/1.1 200 OK
Content-Type: text/html; charset=utf-8
Content-Length: 1256
Cache-Control: max-age=3600

<!DOCTYPE html>
<html>...</html>
```

* In HTTP/1.1 these messages are literally plain text sent over the connection. HTTP/2 and HTTP/3 encode the same information in binary frames, but the concepts (method, path, headers, body, status) are identical.
* You can see raw requests and responses with `curl -v https://example.com`.

### URLs
* A URL (Uniform Resource Locator) tells the client where a resource is and how to get it.

```
https://user@www.example.com:443/products/shoes?color=red&size=10#reviews
└─┬─┘   └┬─┘ └──────┬──────┘ └┬┘└──────┬──────┘ └───────┬───────┘ └──┬──┘
scheme user       host      port     path             query      fragment
```

* **Scheme**: the protocol to use, usually `http` or `https`.
* **Host**: the domain name (or IP address) of the server. Resolved to an IP with DNS.
* **Port**: optional. Defaults to 80 for `http` and 443 for `https`, so it's usually omitted.
* **Path**: which resource on the server. It looks like a file path, but the server can map it to anything (a file, a database lookup, a function).
* **Query string**: `?key=value` pairs separated by `&`. Used for search terms, filters, pagination, and tracking parameters.
* **Fragment**: `#section`. Never sent to the server. The browser uses it to scroll to an element, and client-side apps sometimes use it for routing.
* **Percent-encoding**: characters that aren't allowed in a URL are encoded as `%` plus their hex byte value, for example a space becomes `%20` and `&` inside a value becomes `%26`.
* **Origin**: the combination of scheme + host + port (`https://www.example.com:443`). Browsers use the origin as a security boundary: JavaScript from one origin can't freely read responses from another (the **same-origin policy**). Servers can relax this with **CORS** headers.

### Methods
* The method says what the client wants to do with the resource.
* **GET**: retrieve a resource. Has no body. Should not change anything on the server. This is what the browser uses when you visit a page.
* **POST**: send data to the server to create something or trigger an action (submit a form, log in, place an order). Data goes in the body.
* **PUT**: replace a resource entirely with the body sent.
* **PATCH**: partially update a resource (only the fields sent).
* **DELETE**: delete a resource.
* **HEAD**: same as GET but the server returns only the headers, no body. Used to check if something exists or has changed.
* **OPTIONS**: ask what methods/headers the server allows. Browsers send this automatically as a CORS **preflight** before certain cross-origin requests.

#### Safe and Idempotent Methods
* **Safe**: the method doesn't change server state. GET, HEAD, OPTIONS are safe.
* **Idempotent**: sending the same request many times has the same effect as sending it once. GET, HEAD, OPTIONS, PUT, DELETE are idempotent. POST is not (submitting an order twice creates two orders), and PATCH isn't guaranteed to be.
* This matters because browsers, proxies, and retry logic assume safe/idempotent requests can be repeated. This is why the browser warns you before re-submitting a form (POST) on refresh.

#### REST
* Most JSON APIs follow **REST** conventions: URLs name resources (nouns) and methods say the action (verbs).

```
GET    /api/users        list users
POST   /api/users        create a user
GET    /api/users/42     get user 42
PATCH  /api/users/42     update some fields of user 42
DELETE /api/users/42     delete user 42
```

### Status Codes
* Every response carries a three-digit status code. The first digit gives the class of response.
* Rule of thumb: 2xx = it worked, 3xx = go somewhere else, 4xx = the client made a mistake, 5xx = the server made a mistake.
* In the Chrome Network tab the Status column shows each request's code, and failed requests (4xx/5xx) are shown in red.

#### 1xx: Informational
* The request was received and processing is still going on. These are rarely seen in the Network tab.
* **100 Continue**: the client can go ahead and send the request body.
* **101 Switching Protocols**: the server is switching protocols, for example upgrading the connection to a WebSocket.

#### 2xx: Success
* **200 OK**: the standard success response.
* **201 Created**: a new resource was created, usually after a POST.
* **204 No Content**: the request succeeded and there is no body to return. Common for DELETE.
* **206 Partial Content**: only part of the resource was sent because the client asked for a byte range. Seen when streaming video or audio.

#### 3xx: Redirection
* The client has to make another request to finish. The new URL is given in the `Location` header.
* **301 Moved Permanently**: the resource has a new permanent URL. Browsers cache this redirect.
* **302 Found**: a temporary redirect.
* **304 Not Modified**: the client's cached copy is still valid, so the server sends no body. Very common in the Network tab.
* **307 Temporary Redirect** / **308 Permanent Redirect**: the same as 302 / 301, except the method and body must stay the same when the redirect is followed.

#### 4xx: Client Error
* The request itself has a problem.
* **400 Bad Request**: the request is malformed, for example invalid JSON.
* **401 Unauthorized**: the client isn't authenticated (not logged in or no valid token).
* **403 Forbidden**: the client is authenticated but isn't allowed to access this resource.
* **404 Not Found**: there is no resource at this URL.
* **405 Method Not Allowed**: the endpoint doesn't support this method, for example a POST sent to a GET-only endpoint.
* **409 Conflict**: the request conflicts with the resource's current state.
* **422 Unprocessable Content**: the request is well-formed but fails validation.
* **429 Too Many Requests**: the client has been rate limited.

#### 5xx: Server Error
* The request was valid, but the server failed to handle it.
* **500 Internal Server Error**: a general server-side error, often an unhandled exception.
* **502 Bad Gateway**: a proxy or load balancer got an invalid response from the server behind it.
* **503 Service Unavailable**: the server is overloaded or down for maintenance.
* **504 Gateway Timeout**: a proxy or load balancer didn't get a response from the server behind it in time.

### Headers
* Headers are `Name: value` pairs of metadata sent with every request and response. Names are case-insensitive.
* Headers control nearly everything that isn't the body: what format the data is in, caching, authentication, cookies, compression, and security policies.

#### Common Request Headers
* **Host**: the domain being requested. Required in HTTP/1.1, because one server IP can host many sites.
* **User-Agent**: identifies the browser/client and OS.
* **Accept**: which content types the client can handle, for example `text/html` or `application/json`.
* **Accept-Encoding**: which compression formats the client supports (`gzip, br, zstd`).
* **Accept-Language**: preferred languages (`en-US,en;q=0.9`).
* **Authorization**: credentials, usually a token: `Authorization: Bearer <token>`.
* **Cookie**: cookies the browser has stored for this site.
* **Referer** (misspelled in the spec): the URL of the page that made the request.
* **Origin**: the origin that made the request. Sent on cross-origin requests and POSTs, and used for CORS checks.
* **If-None-Match** / **If-Modified-Since**: conditional request headers used for caching (see Caching).

#### Common Response Headers
* **Content-Type**: what the body is. This is how the browser knows whether it received a page, an image, or data.
    * `text/html`, `text/css`, `text/javascript`, `application/json`, `image/png`, `application/pdf`, …
    * These are called **MIME types** (or media types).
* **Content-Length**: size of the body in bytes.
* **Content-Encoding**: how the body is compressed (`gzip`, `br`). The browser decompresses it automatically.
* **Set-Cookie**: tells the browser to store a cookie.
* **Location**: the URL to go to for redirects (3xx) or the URL of a newly created resource (201).
* **Cache-Control**, **ETag**, **Last-Modified**: caching instructions (see Caching).
* **Access-Control-Allow-Origin**: CORS. Which other origins are allowed to read this response.
* **Strict-Transport-Security** (HSTS): tells the browser to only ever use HTTPS for this site.
* **Content-Security-Policy**: restricts which sources scripts, styles, images, etc. can load from. Main defense against cross-site scripting (XSS).
* **Server** / **X-Powered-By**: sometimes reveal the server software or framework (`nginx`, `cloudflare`, `Next.js`).

### Cookies and Sessions
* HTTP is stateless, so the server needs a way to recognize that two requests come from the same user. **Cookies** are the main mechanism.
* A cookie is a small `name=value` string that the server asks the browser to store. The browser then automatically sends it back on every later request to that site.

```
Response:  Set-Cookie: session_id=abc123; Path=/; HttpOnly; Secure; SameSite=Lax; Max-Age=86400
Later request:  Cookie: session_id=abc123
```

#### Cookie Attributes
* **Expires** / **Max-Age**: when the cookie should be deleted. Without one it's a **session cookie**, deleted when the browser closes.
* **Domain** / **Path**: which hosts and paths the cookie is sent to.
* **Secure**: only send over HTTPS.
* **HttpOnly**: JavaScript can't read the cookie (`document.cookie`). Protects session cookies from being stolen by XSS.
* **SameSite**: controls whether the cookie is sent on requests coming from other sites. Protects against cross-site request forgery (CSRF).
    * `Strict`: never sent on cross-site requests.
    * `Lax` (browser default): sent when you navigate to the site from a link, but not on cross-site POSTs or background requests.
    * `None`: always sent (requires `Secure`). Needed for third-party cookies.

#### Sessions
* **Server-side sessions**: on login, the server creates a session record (user ID, expiry) in a database or cache, and sends the browser a random **session ID** in a cookie. Each request sends the cookie back, and the server looks up the session. Logging out deletes the record.
* **Token-based auth**: the server issues a signed token (often a **JWT**, JSON Web Token) containing the user's info. The server verifies the signature instead of looking anything up. Tokens are sent in a cookie or in the `Authorization: Bearer` header. Common for APIs and mobile apps.
* **First-party vs third-party cookies**: first-party cookies belong to the site in the address bar. Third-party cookies are set by other domains embedded in the page (ads, trackers), and browsers increasingly block or restrict them.

### Caching
* Caching stores copies of responses so they don't have to be downloaded again. It makes pages load faster and reduces server load.
* Caches exist at several levels: the **browser cache**, **CDNs** (Cloudflare, CloudFront) that keep copies close to users, and reverse proxies in front of servers.

#### Cache-Control
* The `Cache-Control` response header tells caches what they may do with a response.
* **max-age=N**: the response is **fresh** for N seconds. During that time the browser reuses it without contacting the server at all.
* **no-cache**: the response may be stored, but must be revalidated with the server before every use. (Confusingly, this does not mean "don't cache".)
* **no-store**: don't store the response anywhere. Used for sensitive data.
* **public** / **private**: whether shared caches (CDNs) may store it, or only the user's browser.
* **immutable**: the content will never change, so don't even revalidate.

#### Revalidation (Conditional Requests)
* Once a cached response is stale, the browser asks the server if it has changed instead of re-downloading it.
* **ETag**: the server sends an identifier for the version of the resource (often a hash). The browser later sends `If-None-Match: <etag>`.
* **Last-Modified**: the server sends a timestamp. The browser later sends `If-Modified-Since: <timestamp>`.
* If nothing changed, the server responds **304 Not Modified** with no body, and the browser uses its cached copy. Otherwise it sends **200** with the new content.

#### Cache Busting
* Common strategy for static files: put a hash of the file's contents in its name (`app.3f9a2c.js`) and cache it for a year with `max-age=31536000, immutable`. When the file changes, its name changes, so browsers fetch the new one.
* The HTML page itself is usually served with `no-cache` or a short max-age so it always points to the latest file names.

### HTTPS / TLS
* HTTPS is HTTP sent over a **TLS** (Transport Layer Security) connection. The HTTP messages are the same, they're just encrypted. TLS replaced the older SSL, but the name "SSL" is still used loosely.
* TLS provides three guarantees:
    * **Confidentiality**: nobody between client and server (Wi-Fi owner, ISP) can read the traffic.
    * **Integrity**: nobody can modify the traffic without it being detected.
    * **Authentication**: you are really talking to `example.com` and not an impostor.
* What's still visible to someone watching the network: the server's IP address, the domain name (usually), and the timing and size of traffic. The path, query, headers, cookies, and body are all encrypted.

#### Certificates
* The server presents a **certificate**: a document containing its domain name and **public key**, signed by a **Certificate Authority** (CA) such as Let's Encrypt or DigiCert.
* Browsers and operating systems ship with a list of trusted root CAs. The browser checks that the certificate's signature chains back to a trusted root, the certificate hasn't expired, and the domain matches the URL.
* If any check fails, the browser shows a full-page security warning.
* Click the icon left of the address bar to view a site's certificate.

#### TLS Handshake (TLS 1.3, Simplified)
1. **ClientHello**: the client sends supported cipher suites and its half of a key exchange (Diffie-Hellman).
2. **ServerHello**: the server picks a cipher, sends its half of the key exchange, its certificate, and a signature proving it owns the certificate's private key.
3. Both sides combine the key exchange halves to derive the same **symmetric session key** without ever sending it over the network.
4. All further traffic is encrypted with fast symmetric encryption (e.g. AES-GCM) using that key.
* **Asymmetric** (public-key) crypto is used only for the handshake, to authenticate and agree on a key. **Symmetric** crypto does the bulk encryption because it's much faster.
* TLS 1.3 takes one round trip. TLS 1.2 took two.

### HTTP Versions (1.1, 2, 3)

#### HTTP/1.1 (1997)
* Text-based messages over TCP.
* Persistent (keep-alive) connections, so one TCP connection can carry many requests.
* But requests on a connection are handled one at a time: a slow response blocks everything behind it (**head-of-line blocking**).
* Browsers work around this by opening ~6 parallel connections per domain. Developers worked around it with tricks like bundling files and image sprites.

#### HTTP/2 (2015)
* Same methods, headers, and status codes, but messages are encoded as **binary frames**.
* **Multiplexing**: many requests and responses are interleaved over a single TCP connection as independent **streams**, so one slow response doesn't block the others at the HTTP level.
* **Header compression** (HPACK): headers like cookies and user agent repeat on every request, so compressing them saves a lot of bandwidth.
* Still runs on TCP, so a single lost packet stalls all streams until it's retransmitted (head-of-line blocking at the TCP level).
* In practice only used over HTTPS.

#### HTTP/3 (2022)
* Runs on **QUIC** instead of TCP. QUIC is a transport protocol built on UDP that handles reliability, ordering, and encryption itself.
* Streams are independent at the transport level, so a lost packet only stalls the stream it belongs to.
* TLS 1.3 is built into QUIC, so the connection and encryption handshakes happen together: a new connection takes one round trip, and a repeat connection can send data immediately (**0-RTT**).
* **Connection migration**: connections are identified by an ID rather than IP + port, so they survive switching networks (Wi-Fi to cellular).
* Header compression uses QPACK.
* Browsers usually start with HTTP/2 and switch to HTTP/3 when the server advertises it with the `Alt-Svc` header.

#### Seeing the Version
* In the Chrome Network tab, right-click the column headers and enable **Protocol**. It shows `http/1.1`, `h2`, or `h3` for each request.

