
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

// Directed Acrylic Graph (DAG)
// - Special name gives to a directed graph that has no cycles.






