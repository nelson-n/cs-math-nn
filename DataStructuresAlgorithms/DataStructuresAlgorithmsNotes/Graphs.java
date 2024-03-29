
//------------------------------------------------------------------------------
// Graphs
//------------------------------------------------------------------------------

// One of the most flexible data structures, many other data structures can be
// represented using a graph.
// - Can be used to represent a singly linked list, trees, etc.

// Graphs are made up of nodes (vertices) and edges.

// Applications:
// - Counting network hops: how many hops does it take to get from one node to
//  another.

// - Topological sorting: ordering of nodes in a graph such that for every edge
// u -> v, u comes before v in the ordering (linear ordering of vertices so that
// all edges go from left to right.)

// - Graph coloring: assign colors to nodes such that no two adjacent nodes have
// the same color. Interesting problem for determining the minimum number of
// colors needed to color a graph.

// - Hamiltonian path: a path that visits every node exactly once before returning
// to the original node. Also known as the traveling salesman problem.

// - Clique problems: region of the graph where every vertex is connected to every
// other vertex.

// - Conflict serializability: significant aspect of database optimization. Rather
// than executing instructions in a transaction one after another, the database
// tries to execute them in parallel. This can lead to conflicts, which must be
// resolved. The database must determine if two transactions can be executed in
// parallel or if they must be executed serially, and a graph is used to do this.

// Directed graph: edges go from one vertex to another in a specific direction. 
// - Edges are depicted as arrows. 

// Undirected graph: edges have no direction. Edges are depicted using lines.

// Formal graph definition:
// G = (V, E)
// In a directed graph, if an edge goes from vertex u to vertex v, E contains
// the ordered pair (u, v). In an undirected graph, E contains the unordered
// pair {u, v}.

// Example: V = {1, 2, 3, 4}, E = {(1, 2), (2, 3), (3, 4), (4, 1), (1, 3), (2, 4)}

// Adjacency = relation between two vertices.
// - If a graph contains edges (u, v), then u and v are adjacent.
// - i.e. u and v are adjacent if there is an edge between them.

// Complete graph = graph in which every vertex is adjacent to every other vertex.
// - i.e. every vertex is connected to every other vertex.

// Incidence = relation between a vertex and an edge.
// - For a directed graph:
//    - Edge (u, v) is incident from or leaves vertex u.
//    - Edge (u, v) is incident from or enters vertex v.
// - For an undirected graph:
//  - Edge {u, v} is incident on vertices u and v.

// Degree = count of incidence for a vertex.
// For a directed graph:
// - In-degree = number of edges incident on a vertex.
// - Out-degree = number of edges incident from a vertex.
// For an undirected graph:
// - Degree = number of edges incident on a vertex.

// Path
// - Sequence of vertices traversed by following edges between them.
// - A path is simple if it contains no repeated vertices.
// - A vertex is reachable from vertex u if a path exists from u to v.

// Cycle
// - Path that includes the same vertex two or more times. 
//  - You go back to a previous vertex.
// - An acyclic graph is one that has no cycles.

// Directed Acrylic Graph (DAG)
// - Special name gives to a directed graph that has no cycles.

// Connectivity
// - Represents how well a graph is connected.
// - An undirected graph is connected if every vertex is reachable from each 
// other by following some path.
// - A directed graph is strongly connected if every vertex is reachable from
// every other vertex.

// Articulation Point
// - A vertex that has special significance in keeping a graph connected, i.e.
// without the vertex, the graph would no longer be connected.
// - Any edge whose removal disconnects a graph is called a bridge.
// - A connected graph with no articulation points is bi-connected.

//------------------------------------------------------------------------------
// Graph Representation
//------------------------------------------------------------------------------

// Adjacency List Representation
// - Most common way to represent a graph in a computer.
// - Linked list of adjacency list structures.
// - Each structure in the linked list contains two members.
//    - Vertex.
//    - List of vertices adjacent to the vertex.
// - With this structure, edges logically exist from vertex to each adjacent vertex.

//------------------------------------------------------------------------------
// Graph Implementation
//------------------------------------------------------------------------------

// Graph.java
// package cse41321.containers;

import java.util.Iterator;

// Using google guava preconditions library:
// https://github.com/google/guava/wiki/PreconditionsExplained
// import static com.google.common.base.Preconditions.*;

/**
 * Adjacency list representation of a graph.
 * @param <V> Data type stored in each vertex.
 * @param <E> Data type stored in each edge.
 */
public class Graph<V, E> {
    public class Vertex {
        private V data;
        private HashSet<Edge> edgesIncidentFrom = new HashSet<Edge>();
        private HashSet<Edge> edgesIncidentTo = new HashSet<Edge>();

        private Vertex(V data) {
            this.data = data;
        }

        public V getData() {
            return data;
        }

        public Iterable<Edge> getEdgesIncidentFrom() {
            return new Iterable<Edge>() {
                public Iterator<Edge> iterator() {
                    return edgesIncidentFrom.iterator();
                }
            };
        }

        public Iterable<Edge> getEdgesIncidentTo() {
            return new Iterable<Edge>() {
                public Iterator<Edge> iterator() {
                    return edgesIncidentTo.iterator();
                }
            };
        }

        // Comparator.
        @Override
        @SuppressWarnings("unchecked")
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Vertex vertex = (Vertex) o;

            return data.equals(vertex.data);
        }

        @Override
        public int hashCode() {
            return data.hashCode();
        }
    }

    public class Edge {
        private Vertex from;
        private Vertex to;
        private E data;

        private Edge(Vertex from, Vertex to) {
            this(from, to, null);
        }

        private Edge(Vertex from, Vertex to, E data) {
            this.from = from;
            this.to = to;
            this.data = data;
        }

        public Vertex getFrom() {
            return from;
        }

        public Vertex getTo() {
            return to;
        }

        public E getData() {
            return data;
        }

        @Override
        @SuppressWarnings("unchecked")
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Edge edge = (Edge) o;

            return from.equals(edge.from) && to.equals(edge.to);
        }

        @Override
        public int hashCode() {
            int result = from.hashCode();
            result = 31 * result + to.hashCode();
            return result;
        }
    }

    private HashSet<Vertex> vertices = new HashSet<Vertex>();

    private HashSet<Edge> edges = new HashSet<Edge>();

    // ============================ Vertex methods ============================
    public boolean containsVertex(V data) {
        return vertices.isMember(new Vertex(data));
    }

    public int getNumVertices() {
        return vertices.getSize();
    }

    public Iterable<Vertex> getVertices() {
        return new Iterable<Vertex>() {
            public Iterator<Vertex> iterator() {
                return vertices.iterator();
            }
        };
    }

    public Vertex getVertex(V data) throws
            NullPointerException,
            IllegalStateException {
        // Preconditions
        checkVertexPreconditions(data);
        checkState(containsVertex(data), "Vertex does not exist");

        return vertices.getMember(new Vertex(data));
    }

    public void insertVertex(V data) throws
            NullPointerException,
            IllegalStateException {
        // Preconditions
        checkVertexPreconditions(data);
        checkState(!containsVertex(data), "Vertex already exists");

        vertices.insert(new Vertex(data));
    }

    public V removeVertex(V data) throws
            NullPointerException,
            IllegalStateException {
        // Preconditions
        checkVertexPreconditions(data);
        checkState(containsVertex(data), "Vertex does not exist");
        Vertex vertex = vertices.getMember(new Vertex(data));
        checkState(vertex.edgesIncidentFrom.isEmpty(),
                "Vertex has edges incident from it");
        checkState(vertex.edgesIncidentTo.isEmpty(),
                "Vertex has edges incident to it");

        return vertices.remove(new Vertex(data)).data;
    }

    private void checkVertexPreconditions(V data) throws
            NullPointerException {
        checkNotNull(data, "data must not be null");
    }

    // ============================= Edge methods =============================
    public boolean containsEdge(V from, V to) {
        return from != null
                && to != null
                && containsVertex(from)
                && containsVertex(to)
                && edges.isMember(new Edge(getVertex(from), getVertex(to)));
    }

    public int getNumEdges() {
        return edges.getSize();
    }

    public Iterable<Edge> getEdges() {
        return new Iterable<Edge>() {
            public Iterator<Edge> iterator() {
                return edges.iterator();
            }
        };
    }

    public Edge getEdge(V from, V to) throws
            NullPointerException,
            IllegalStateException {
        // Preconditions
        checkEdgePreconditions(from, to);
        checkState(containsEdge(from, to), "Edge does not exist");

        return edges.getMember(new Edge(getVertex(from), getVertex(to)));
    }

    public void insertEdge(V from, V to, E data) throws
            NullPointerException,
            IllegalStateException {
        // Preconditions
        checkEdgePreconditions(from, to);
        checkState(!containsEdge(from, to), "Edge already exists");

        // Add to graph's edge set
        Vertex fromVertex = getVertex(from);
        Vertex toVertex = getVertex(to);
        Edge edge = new Edge(fromVertex, toVertex, data);
        edges.insert(edge);

        // Add to "from" vertex's incidence set
        fromVertex.edgesIncidentFrom.insert(edge);

        // Add to "to" vertex's incidence set
        toVertex.edgesIncidentTo.insert(edge);
    }

    public E removeEdge(V from, V to) throws
            NullPointerException,
            IllegalStateException {
        // Preconditions
        checkEdgePreconditions(from, to);
        checkState(containsEdge(from, to), "Edge does not exist");

        // Remove edge from graph's edge set
        Vertex fromVertex = getVertex(from);
        Vertex toVertex = getVertex(to);
        Edge edge = new Edge(fromVertex, toVertex);
        E data = edges.remove(edge).data;

        // Remove edge from "from" vertex's incidence set
        fromVertex.edgesIncidentFrom.remove(edge);

        // Remove edge from "to" vertex's incidence set
        toVertex.edgesIncidentTo.remove(edge);

        return data;
    }

    private void checkEdgePreconditions(V from, V to) throws
            NullPointerException,
            IllegalStateException {
        checkNotNull(from, "from must not be null");
        checkNotNull(to, "to must not be null");
        checkState(containsVertex(from), "from vertex does not exist");
        checkState(containsVertex(to), "to vertex does not exist");
    }
}

//------------------------------------------------------------------------------
// Breadth-First Search
//------------------------------------------------------------------------------

// Exploration algorithm that starts at a given vertex and explores all the
// vertices reachable from it.

// Useful for finding the minimum spanning trees and shortest paths. 

// Algorithm:
// - Place start vertex in a queue.
// - For each vertex adjacent to the vertex at the front of the queue, check if
// it has been visited, and if not then visit it. 
// - Dequeue the vertex at the front of the queue once all adjacent vertices
// have been looked at. 
// - This algorithm is coded recursively.

// At the end of the algorithm, the visited set will contain all the vertices
// reachable from the start vertex.

// You can also store the number of vertices traversed before reaching each
// vertex in a map. This will allow you to find the shortest path from the
// start vertex to any other vertex.

// Example: use breadth-first search to count the number of hops between nodes
// in a network, i.e. find the best way to get from one node (computer) to another
// on the internet.
// - Breadth-first search is used to find the shortest path between two nodes.

// BreadthFirstSearch.java

// import static com.google.common.base.Preconditions.*;

public final class BreadthFirstSearch {
    public static final class Server {
        private String name;
        private VertexColor color;
        private int hops;

        public Server(String name) {
            checkNotNull(name, "name must not be null");

            this.name = name;
        }

        public String getName() {
            return name;
        }

        public int getHops() {
            return hops;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Server that = (Server) o;

            return name.equals(that.name);

        }

        @Override
        public int hashCode() {
            return name.hashCode();
        }
    }

    public static void countNetworkHops(Graph<Server, ?> graph, String start) {
        checkNotNull(graph, "graph must not be null");
        checkNotNull(start, "start must not be null");
        checkArgument(graph.containsVertex(new Server(start)),
                "start not found in graph");

        // Initialize all vertices in the graph
        for (Graph<Server, ?>.Vertex vertex : graph.getVertices()) {
            Server server = vertex.getData();
            if (start.equals(server.name)) {
                // Initialize the start vertex
                server.color = VertexColor.GRAY;
                server.hops = 0;
            } else {
                // Initialize vertices other than the start vertex
                server.color = VertexColor.WHITE;
                server.hops = -1;
            }
        }

        // Initialize queue with the start vertex
        Queue<Graph<Server, ?>.Vertex> queue =
                new Queue<Graph<Server, ?>.Vertex>();
        queue.enqueue(graph.getVertex(new Server(start)));

        // Perform breadth-first search setting each reachable vertex's hops
        while (!queue.isEmpty()) {
            Graph<Server, ?>.Vertex vertex = queue.dequeue();

            // Look at all vertices adjacent to current vertex
            for (Graph<Server, ?>.Edge edge
                    : vertex.getEdgesIncidentFrom()) {
                // If adjacent vertex is white, color it gray, update hops,
                // and add to queue
                Graph<Server, ?>.Vertex adjacentVertex = edge.getTo();
                if (adjacentVertex.getData().color == VertexColor.WHITE) {
                    adjacentVertex.getData().color = VertexColor.GRAY;
                    adjacentVertex.getData().hops = vertex.getData().hops + 1;
                    queue.enqueue(adjacentVertex);
                }
            }

            vertex.getData().color = VertexColor.BLACK;
        }
    }
}

//------------------------------------------------------------------------------
// Depth-First Search
//------------------------------------------------------------------------------

// Idea: Explore as far as possible in one direction, then backtrack and explore
// in another direction.

// Useful for detecting cycles in a graph.
// Also useful for topological sorting.

// Algorithm:
// - Select a starting vertex.
// - If any undiscovered vertices are adjacent to the selected vertex, then 
// visit that vertex and repeat the process. Once you cannot travel any further,
// return to the start vertex and label is as finished.
// - Then move to the next unvisited vertex and repeat the process recursively.

// Note* that this algorithm will only visit vertices reachable from the 
// starting vertex. To visit all vertices the process should be repeated 
// for all vertices in the graph.

// Example: Topological Sorting
// - Determine an order in which all courses can be taken with all pre-requisites
// satisfied along the way.

// We use a directed graph in which vertices represent classes and edges represent
// dependencies (pre-requisites). 

// Topological sort = sort in which tasks are sorted so that dependent classes
// appear before dependencies. 

// After performing a depth first search on all vertices, we are guaranteed to
// receive linked list that is topologically sorted.

// import static com.google.common.base.Preconditions.checkNotNull;

public final class DepthFirstSearch {
    public static final class Course {
        private String name;
        private VertexColor color;

        public Course(String name) {
            checkNotNull(name, "name must not be null");

            this.name = name;
        }

        public String getName() {
            return name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Course that = (Course) o;

            return name.equals(that.name);

        }

        @Override
        public int hashCode() {
            return name.hashCode();
        }
    }

    public static SinglyLinkedList<String> planCourses(
            Graph<Course, ?> graph) {
        checkNotNull(graph, "graph must not be null");

        // Initialize all vertices in the graph
        for (Graph<Course, ?>.Vertex vertex : graph.getVertices()) {
            vertex.getData().color = VertexColor.WHITE;
        }

        // Create list to hold planned courses
        SinglyLinkedList<String> plannedCourses =
                new SinglyLinkedList<String>();

        // Perform multiple depth first searches each starting at a different
        // vertex to ensure courses with no prerequisites (i.e. disconnected
        // parts of the graph) are included in the results
        for (Graph<Course, ?>.Vertex vertex : graph.getVertices()) {
            if (vertex.getData().color == VertexColor.WHITE) {
                planCoursesRecursive(vertex, plannedCourses);
            }
        }

        return plannedCourses;
    }

    private static void planCoursesRecursive(
            Graph<Course, ?>.Vertex vertex,
            SinglyLinkedList<String> plannedCourses) {
        // Color the vertex gray
        vertex.getData().color = VertexColor.GRAY;

        // Recursively traverse each adjacent white vertex
        for (Graph<Course, ?>.Edge edge : vertex.getEdgesIncidentFrom()) {
            Graph<Course, ?>.Vertex adjacentVertex = edge.getTo();

            if (adjacentVertex.getData().color == VertexColor.WHITE) {
                planCoursesRecursive(adjacentVertex, plannedCourses);
            }
        }

        // Color the vertex black and add it to the front of the list
        plannedCourses.insertHead(vertex.getData().name);
    }
}
