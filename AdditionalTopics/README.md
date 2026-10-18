
# AdditionalTopics

## NetworkInternet

`/internet.pdf`

Notes that give a high-level overview of how the internet works. Covers:
* The TCP/IP protocol stack, how your computer converts incoming packets to a rendered webpage.
* How packets (such as a Google search) are routed between ISPs, NSPs, MAEs to the backbone of the internet in order to find the destination IP address and Port.
    * How routing tables are used to find the quickest route to a destination IP address.
* How domain names and DNS servers are used to store IP addresses and corresponding web addresses.
* How HTTP and HTTPS prococol is used to solicit information (such as the packets for a webpage) from another computer.
* How Transport Layer Security (TLS) is used to encrypt packets that are being passed between IP addresses.
* The differences between IPv4 and IPv6.

`/HTTPNotes.md`

Notes on the HTTP protocol. Covers:
* The request / response cycle, URLs, methods, status codes, and headers.
* Cookies, sessions, and caching.
* HTTPS / TLS and the differences between HTTP/1.1, HTTP/2, and HTTP/3.

## APIs

`/APINotes.md`

Notes on APIs in general and how to design them. Covers:
* What an API is and the kinds of APIs (library, OS, web).
* Design principles, web API styles (REST, GraphQL, gRPC, webhooks), and designing REST endpoints.
* Errors, authentication, versioning, pagination, rate limiting, documentation, and testing.

`/DemoAPI.py`

Minimal REST API for a "notes" resource using only the Python standard library (`http.server`). Covers:
* Routing requests by method and path.
* Reading JSON request bodies and returning JSON responses with appropriate status codes (200, 201, 204, 400, 404).
* Example `curl` commands for calling each endpoint.
