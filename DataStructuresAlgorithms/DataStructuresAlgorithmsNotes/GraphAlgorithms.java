
//------------------------------------------------------------------------------
// Graphs Algorithms
//------------------------------------------------------------------------------

// Covers:
// - Minimum Spanning Tree
// - Shortest Paths
// - Traveling Salesperson 

// - Builds on top of depth-first search and breadth-first search.
// - These extension algorithms take advantage of data stored on the edges.
// - These algorithms work with weighted graphs where each edge is assigned a 
// weight.
// - Weights can represent anything, but they generally represent the cost 
// associated with traversing an edge. 

//------------------------------------------------------------------------------
// Minimum Spanning Tree
//------------------------------------------------------------------------------

// Minimum Spanning Tree = tree that covers all vertices in an undirected, 
// weighted graph at minimum cost.

// Applications:
// - Efficient pipelines, find the cheapest way to interconnect all distribution points.
// - Optimal communication, find the cheapest way to interconnect all network gateways.
// - Optimal wiring of circuit boards.

// Formally: 
// - Given an undirected graph G = (V, E)
// - A minimum spanning tree is the set of T edges in E that connects all 
// vertices V at minimum cost. 
// - The edges of T form a tree that covers all vertices in V. Each vertex 
// ends up with exactly one parent.
//    - This tree is the minimum spanning tree.

// Prim's Algorithm:
// - Method for generating a minimum spanning tree. 
// - Works by adding one edge at a time based on what looks best at the moment.
// - This makes it a greedy algorithm. Though greedy algorithms usually generate
// approximations for the optimal solution, Prim's algorithm always generates
// the optimal solution.
// - Works similar to breadth-first search, but instead of using a queue, it
// uses a priority queue.
// - Starts with a vertex, explores all edges incident to that vertex, and
// then adds the cheapest edge to the tree.

// Implementation:

// import static com.google.common.base.Preconditions.*;

public final class MinimumSpanningTree {
    /**
     * A facility that produces or distributes water.
     */
    public static final class WaterFacility {
        private String name;

        private WaterFacility parent;
        private VertexColor color;
        private double costToConnectToParent;

        public WaterFacility(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public WaterFacility getParent() {
            return parent;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            WaterFacility that = (WaterFacility) o;

            return name.equals(that.name);
        }

        @Override
        public int hashCode() {
            return name.hashCode();
        }
    }

    /**
     * A pipeline connecting two water facilities.
     */
    public static final class Pipeline {
        private double cost;

        public Pipeline(double cost) {
            this.cost = cost;
        }
    }

    /**
     * Generates a minimum spanning tree connecting all water facilities.
     * The "parent" field in each facility is set to represent the tree.
     */
    public static void computeOptimumDistributionSystem(
            Graph<WaterFacility, Pipeline> graph,
            WaterFacility start) {
        checkNotNull(graph, "graph must not be null");
        checkNotNull(start, "start must not be null");
        checkArgument(graph.containsVertex(start), "start not found in graph");

        // Initialize all of the vertices in the graph
        for (Graph<WaterFacility, Pipeline>.Vertex vertex
                : graph.getVertices()) {
            WaterFacility facility = vertex.getData();
            facility.color = VertexColor.WHITE;
            facility.parent = null;
            facility.costToConnectToParent =
                    facility.equals(start) ? 0 : Double.MAX_VALUE;
        }

        // Use Prim's algorithm to compute a minimum spanning tree
        for (int i = 0; i < graph.getNumVertices(); ++i) {
            // Select the white facility with the smallest cost to connect
            // to its parent
            Graph<WaterFacility, Pipeline>.Vertex selectedVertex = null;
            WaterFacility selectedFacility = null;
            double minCost = Double.MAX_VALUE;
            for (Graph<WaterFacility, Pipeline>.Vertex vertex
                    : graph.getVertices()) {
                if (vertex.getData().color == VertexColor.WHITE
                        && vertex.getData().costToConnectToParent < minCost) {
                    selectedVertex = vertex;
                    selectedFacility = vertex.getData();
                    minCost = vertex.getData().costToConnectToParent;
                }
            }

            // If no facility found, graph has disconnected components
            checkState(selectedFacility != null,
                    "graph has disconnected components");

            // Color the selected facility black
            selectedFacility.color = VertexColor.BLACK;

            // Traverse each pipeline that leaves the selected facility
            for (Graph<WaterFacility, Pipeline>.Edge edge
                    : selectedVertex.getEdgesIncidentFrom()) {
                // If adjacent facility is white and it would be cheaper to
                // connect to that facility with this pipeline
                Pipeline pipeline = edge.getData();
                WaterFacility adjacentFacility = edge.getTo().getData();
                if (adjacentFacility.color == VertexColor.WHITE
                        && pipeline.cost
                                < adjacentFacility.costToConnectToParent) {
                    // Use this pipeline (i.e. set adjacent facility's parent
                    // to the selected facility)
                    adjacentFacility.parent = selectedFacility;
                    adjacentFacility.costToConnectToParent = pipeline.cost;
                }
            }
        }
    }
}

// Multiple minimum spanning trees may exist for a single graph. 
// - However, they will all have the same cost.


//------------------------------------------------------------------------------
// Shortest Paths
//------------------------------------------------------------------------------

// Shortest path = path that connects one vertex to another in a directed,
// weighted graph at minimum cost.
// - Solving this problem requires finding the shortest path not just to the 
// ideal vertex, but to all vertices in the graph. This is necessary because
// it is the only way to know if you have actually found the shortest path.

// This gives the solution to optimizing routing.

// Also known as Dijkstra's algorithm.
// - No known algorithm that solves this problem faster. 
// - For a graph G = (V, E) runs in O(E lg V) time when implemented correctly.
// - Only works with positive edge weights, for negative edge weights one must
// use the Bellman-Ford algorithm which runs in O(VE) time.
// - Dijkstra's works by growing a shortest paths tree. The root is the start
// vertex and branches are the shortest path to all other vertices. 
//     - Works by adding edges to the shortest path tree based on what looks
// best at the moment (greedy). 
// - Djisktra's algorithm is a greedy algorithm, but generates the optimal 
// solution.
// - Resembles breadth-first search, repeatedly selects vertex and explores
// edges incident to it.

// Each vertex maintains two values, 1) whether is has been visited and 2) the
// cost of the shortest path to that vertex. 2) is used to determine whether
// the algorithm can be improved.

// At the end of the algorithm, if you traverse the tree you have the guaranteed
// shortest path to each of the other vertices.

// Applications:
// - Optimal routing, find the cheapest way to connect two network gateways.
// - Routing planes / trucks, determine the best route between A and B. 
// - Traffic monitoring / route planning.

// Implementation:

// import static com.google.common.base.Preconditions.*;

public final class ShortestPaths {
    public static final class City {
        private String name;
        private City parent;
        private VertexColor color;
        private double shortestPathCost;

        public City(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public City getParent() {
            return parent;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            City that = (City) o;

            return name.equals(that.name);
        }

        @Override
        public int hashCode() {
            return name.hashCode();
        }
    }

    public static final class Connection {
        private double cost;

        public Connection(double cost) {
            this.cost = cost;
        }
    }

    /**
     * Generates shortest paths tree from start to all other cities.
     * The "parent" field in each city is set to represent the tree.
     */
    public static void computeShortestPaths(
            Graph<City, Connection> graph,
            City start) {
        checkNotNull(graph, "graph must not be null");
        checkNotNull(start, "start must not be null");
        checkArgument(graph.containsVertex(start), "start not found in graph");

        // Initialize all of the vertices in the graph
        for (Graph<City, Connection>.Vertex vertex
                : graph.getVertices()) {
            City city = vertex.getData();
            city.color = VertexColor.WHITE;
            city.parent = null;
            city.shortestPathCost = city.equals(start) ? 0 : Double.MAX_VALUE;
        }

        // Use Dijkstra's algorithm to compute a shortest paths tree
        for (int i = 0; i < graph.getNumVertices(); ++i) {
            // Select the white city with the smallest shortest path cost
            Graph<City, Connection>.Vertex selectedVertex = null;
            City selectedCity = null;
            double minCost = Double.MAX_VALUE;
            for (Graph<City, Connection>.Vertex vertex
                    : graph.getVertices()) {
                if (vertex.getData().color == VertexColor.WHITE
                        && vertex.getData().shortestPathCost < minCost) {
                    selectedVertex = vertex;
                    selectedCity = vertex.getData();
                    minCost = vertex.getData().shortestPathCost;
                }
            }

            // If no city found, graph has disconnected components
            checkState(selectedCity != null,
                    "graph has disconnected components");

            // Color the selected city black
            selectedCity.color = VertexColor.BLACK;

            // Traverse each connection that leaves the selected city
            for (Graph<City, Connection>.Edge edge
                    : selectedVertex.getEdgesIncidentFrom()) {
                // If adjacent city is white
                Connection connection = edge.getData();
                City adjacentCity = edge.getTo().getData();

                // Relax edge from selected city to adjacent city
                relax(selectedCity, adjacentCity, connection);
            }
        }
    }

    private static void relax(City from, City to, Connection connection) {
        // If path to "to" through "from" is shorter than previously
        // identified path
        if (to.shortestPathCost > from.shortestPathCost + connection.cost) {
            to.parent = from;
            to.shortestPathCost = from.shortestPathCost + connection.cost;
        }
    }
}

// There can be multiple shortest paths trees, but they will all have the same
// cost.


//------------------------------------------------------------------------------
// Traveling Salesperson
//------------------------------------------------------------------------------

// Generates complete shortest tour.
// - Minimum path in complete, undirected, weighted graph from start vertex 
// back to start vertex in which every vertex is visited exactly once.

// We are looking for a Hamiltonian cycle, a tour in which we visit each vertex
// exactly once before returning to the start vertex.

// Solution to optimizing delivery / transportation problem.

// Because the graph is undirected and complete, for G = (V, E) 
// E contains V(V-1) / 2 edges.

// Naive Algorithm:
// - Explore all permutations of the vertices of G. Each permutations represents
// one possible tour. However, this approach results in O(V!) time complexity
// which explodes and is not possible to solve.

// Nearest Neighbor Heuristic:
// - Approximate solution to the traveling salesperson problem.
// - At any given vertex, travel to the next closest vertex. Thus it is greedy
// and can result in suboptimal solutions.
// - Edges are implied since the graph is complete and all vertices are linked.
// - The weight of each edge is the distance between the two vertices.
// - Resembled breadth-first search, repeatedly selects vertex and explores
// edges incident to it before exploring further.
// - Greedy algorithm and does not guarantee optimal solution.
// - Worst case performance: results in a tour whose length is within a factor
// of 2 of the optimal tour length. 

// import static com.google.common.base.Preconditions.*;

public final class TravellingSalesPerson {
    public static final class City {
        private String name;
        private int x, y;
        private VertexColor color;

        public City(String name, int x, int y) {
            this.name = name;
            this.x = x;
            this.y = y;
        }

        public String getName() {
            return name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            City city = (City) o;

            return name.equals(city.name);
        }

        @Override
        public int hashCode() {
            return name.hashCode();
        }
    }

    /**
     * Returns tour of cities within factor of 2 of optimal tour.
     */
    public static SinglyLinkedList<City> computeShortestTour(
            City[] graph,
            City start) {
        checkNotNull(graph, "graph must not be null");
        checkNotNull(start, "start must not be null");
        for (City city : graph) {
            checkNotNull(city, "graph contains one or more null cities");
        }

        SinglyLinkedList<City> tour = new SinglyLinkedList<City>();

        // Initialize all of the vertices in the graph
        City startCity = null;
        City currentCity = null;
        for (City city : graph) {
            if (city.equals(start)) {
                startCity = city;
                startCity.color = VertexColor.BLACK;
                tour.insertTail(startCity);
                currentCity = startCity;
            } else {
                city.color = VertexColor.WHITE;
            }
        }

        // Verify start found in graph
        checkArgument(startCity != null, "start not found in graph");

        // Use nearest-neighbor heuristic to compute the tour
        for (int i = 0; i < graph.length - 1; ++i) {
            // Select the white city closest to the previous vertex in the tour
            City closestCity = null;
            double closestDistance = Double.MAX_VALUE;
            for (City city : graph) {
                if (city.color == VertexColor.WHITE) {
                    double distance = Math.sqrt(
                            Math.pow(city.x - currentCity.x, 2.0)
                            + Math.pow(city.y - currentCity.y, 2.0));
                    if (distance < closestDistance) {
                        closestCity = city;
                        closestDistance = distance;
                    }
                }
            }

            // Add the closest city to the tour
            tour.insertTail(closestCity);

            // Color the closest city black
            closestCity.color = VertexColor.BLACK;

            // Make the closest city the current city
            currentCity = closestCity;
        }

        // Insert the start city again to complete the tour
        tour.insertTail(startCity);

        return tour;
    }
}

// Traveling Salesperson Problem is NP-hard, meaning that there is no known
// polynomial time algorithm to solve it. In this sense it is intractable.
