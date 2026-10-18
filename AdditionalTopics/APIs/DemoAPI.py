#-------------------------------------------------------------------------------
# DemoAPI.py written by nelson-n 2026-10-18
#
# Minimal REST API for a "notes" resource using only the Python standard library.
#-------------------------------------------------------------------------------

# Run the server:
#   python3 DemoAPI.py
#
# Then call it from another terminal:
#   curl -i localhost:8000/notes
#   curl -i -X POST localhost:8000/notes -H "Content-Type: application/json" -d '{"text": "learn HTTP"}'
#   curl -i localhost:8000/notes/1
#   curl -i -X PATCH localhost:8000/notes/1 -H "Content-Type: application/json" -d '{"text": "learn APIs"}'
#   curl -i -X DELETE localhost:8000/notes/1
#
# The -i flag prints the status line and response headers along with the body.

## Sections
# - Data Store
# - Helpers
# - Request Handler
# - Run Server

import json
from http.server import BaseHTTPRequestHandler, ThreadingHTTPServer


#===============================================================================
# Data Store
#===============================================================================

# Notes are kept in memory, so they are lost when the server stops. A real API
# would store them in a database.
notes = {}
next_id = 1


#===============================================================================
# Helpers
#===============================================================================

def parse_note_id(path):
    """Return the id from a path like /notes/3, or None if it isn't one."""
    parts = path.strip("/").split("/")
    if len(parts) == 2 and parts[0] == "notes" and parts[1].isdigit():
        return int(parts[1])
    return None


#===============================================================================
# Request Handler
#===============================================================================

# http.server calls do_GET, do_POST, etc. based on the request's method. Each
# method checks the path (routing), does the work, and sends a status code,
# headers, and a JSON body.
class NotesHandler(BaseHTTPRequestHandler):

    # Functions for crafting HTTP responses and decoding
    def send_json(self, status, data=None):
        body = b"" if data is None else json.dumps(data).encode()
        self.send_response(status)
        self.send_header("Content-Type", "application/json")
        self.send_header("Content-Length", str(len(body)))
        self.end_headers()
        self.wfile.write(body)

    def send_error_json(self, status, message):
        # Errors are returned as JSON too, so clients can always parse the body.
        self.send_json(status, {"error": message})

    def read_json(self):
        """Read and parse the request body. Returns None if it isn't valid JSON."""
        length = int(self.headers.get("Content-Length", 0))
        try:
            return json.loads(self.rfile.read(length))
        except json.JSONDecodeError:
            return None

    # GET /notes      list all notes
    # GET /notes/{id} get one note
    def do_GET(self):
        if self.path == "/notes":
            return self.send_json(200, list(notes.values()))
        note_id = parse_note_id(self.path)
        if note_id is None:
            return self.send_error_json(404, "not found")
        if note_id not in notes:
            return self.send_error_json(404, f"note {note_id} not found")
        self.send_json(200, notes[note_id])

    # POST /notes     create a note. Returns 201 Created and the new note.
    def do_POST(self):
        global next_id
        if self.path != "/notes":
            return self.send_error_json(404, "not found")
        data = self.read_json()
        if not isinstance(data, dict) or not isinstance(data.get("text"), str):
            return self.send_error_json(400, 'body must be JSON like {"text": "..."}')
        note = {"id": next_id, "text": data["text"]}
        notes[next_id] = note
        next_id += 1
        self.send_json(201, note)

    # PATCH /notes/{id}   update a note's text
    def do_PATCH(self):
        note_id = parse_note_id(self.path)
        if note_id is None or note_id not in notes:
            return self.send_error_json(404, "not found")
        data = self.read_json()
        if not isinstance(data, dict) or not isinstance(data.get("text"), str):
            return self.send_error_json(400, 'body must be JSON like {"text": "..."}')
        notes[note_id]["text"] = data["text"]
        self.send_json(200, notes[note_id])

    # DELETE /notes/{id}  delete a note. Returns 204 No Content.
    def do_DELETE(self):
        note_id = parse_note_id(self.path)
        if note_id is None or note_id not in notes:
            return self.send_error_json(404, "not found")
        del notes[note_id]
        self.send_json(204)


#===============================================================================
# Run Server
#===============================================================================

if __name__ == "__main__":
    server = ThreadingHTTPServer(("localhost", 8000), NotesHandler)
    print("Serving on http://localhost:8000 (Ctrl+C to stop)")
    try:
        server.serve_forever()
    except KeyboardInterrupt:
        pass
