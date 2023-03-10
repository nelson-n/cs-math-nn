
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
// parallel or if they must be executed serially.

// Directed graph: edges go from one vertex to another in a specific direction. 
// - Edges are depicted as arrows. 

// Undirected graph: edges have no direction. Edges are depicted using lines.




