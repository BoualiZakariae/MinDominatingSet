package umons.algorithm.dominatingset.graph;

import java.util.*;

/**
 *
 *
 *
 * This class represents a graph data structure.
 *
 */
public class Graph {

    /**
     * n:    represents the number of vertices of this Graph.
     * m:    represents the number of edges of this Graph.
     * adj:  represents the adjacency  list of this Graph.
     **/
    private int n;
    private int m;
    private Map<Integer,Set<Integer>> adj ;


    /**
     * Class constructor
     *
     * @param n the number of vertices.
     */
    public Graph(int n) {
        this.n = n;
        this.adj = new HashMap<>();
    }


    /**
     * Class constructor
     *
     * @param n the number of vertices.
     * @param m the number of edges.
     */
    public Graph(int n, int m) {
        this.n = n;
        this.m = m;
        this.adj = new HashMap<>();
    }

    /**
     *  Test if y1 and y2 are adjacent in this graph.
     *
     * @param y1    the first vetex
     * @param y2    the second vertex
     * @return      true if y1 and y2 are adjacent, false otherwise
     */
    public boolean areAdjacent(int y1, int y2) {
        return getAdj().get(y1).contains(y2);
    }


    /**
     *
     *
     * @return  an optional object that may or may not contains a node that is degree 1 or 2.
     *
     */
    public Optional<Node> getVertexOfDegreeOneOrTwo() {

        return  this.adj.keySet()
                        .stream()
                        .filter(key->adj.get(key).size()==1 || adj.get(key).size()==2)
                        .map(key->new Node(key, adj.get(key).size()))
                        .findFirst();
   }


    /**
     * Add the vertex {@code v} to this graph only if {@code v} is not already present.
     *
     * @param v  the vertex to be added.
     */
    public void addVertex(Integer v){
        this.adj.putIfAbsent(v,new HashSet<>());
    }


    /**
     *  Add the set {@code vertices} to this graph.
     *
     *  @param vertices    the set of vertices to be added to the graph.
     */
    public void addVertices( int... vertices ) {
        for (int v : vertices) {
            this.addVertex(v);
        }
    }



    /**
     *
     * @return the list of min degree neighbors of three degree vertices.
     */
    public Set<Node> getNeighborsOf3DegreeVertices() {
        Set<Node> nodes = new HashSet<>();
        Node minDegreeNode = null;
        for (Integer key:adj.keySet()) {
            if (getAdj().get(key).size() == 3){
                Set<Integer> neighborsSet = getAdj().get(key);
                Integer[] neighbors =  neighborsSet.toArray(new Integer[neighborsSet.size()]);
                int neighbor1 = neighbors[0];
                int neighbor2 = neighbors[1];
                int neighbor3 = neighbors[2];
                minDegreeNode = minDegreeVertex(neighbor1, neighbor2, neighbor3);
                nodes.add(minDegreeNode);
            }
        }
        return nodes;
    }

    /**
     *
     * @param vertex1  the first vertex.
     * @param vertex2  the second vertex.
     * @param vertex3  the third vertex.
     *
     * @return the min degree vertex between {@code vertex1}, {@code vertex2} and {@code vertex3}
     */
    private Node minDegreeVertex(int vertex1, int vertex2, int vertex3) {
        int minDegree = getAdj().get(vertex1).size();
        int minId = vertex1;

        if (getAdj().get(vertex2).size() < minDegree) {
            minDegree = getAdj().get(vertex2).size();
            minId = vertex2;
        }
        if (getAdj().get(vertex3).size() < minDegree) {
            minDegree = getAdj().get(vertex3).size();
            minId = vertex3;
        }
        return new Node(minId, minDegree);
    }


    /**
     * Remove the vertex {@code v} from the graph.
     * Removing {@code v} from the graph will result
     * on removing every edge that was linked to v.
     *
     * @param v   the vertex to remove.
     * @return    a new graph that does not contains {@code v}.
     */
    public Graph removeVertex(int v) {
        if (!getGraphVertices().contains(v))
            throw new NoSuchElementException();

        Set<Integer> vertices = new HashSet<>();
        
        vertices.add(v);
        return removeVertices(vertices);
    }

    /**
     * Remove the set {@code vertices} from the graph.
     *
     * @param vertices   the set of vertices to remove
     * @return           a new graph that does not contains the vertices set
     */
    public Graph removeVertices( Set<Integer> vertices) {
        Graph newGraph = new Graph(n - vertices.size());
        for(Map.Entry<Integer, Set<Integer>> pair : adj.entrySet()){
            int currentVertex =pair.getKey();
            if (!getGraphVertices().contains(currentVertex))
                throw new NoSuchElementException();
            if (!vertices.contains(currentVertex))
            {
                newGraph.addVertex(currentVertex);
                Set<Integer> neighbors = this.adj.get(currentVertex);
                Set<Integer> newNeighbors = new HashSet<>(neighbors);
                newNeighbors.removeAll(vertices);
                newGraph.getAdj().get(currentVertex).addAll(newNeighbors);
            }
        }
        return newGraph;
    }

    /**
     *
     * @param v  a vertex
     * @return   the openNeighbors of the vertex {@code v}
     */
    public Set<Integer> getOpenNeighbors(int v) {
        if (!getGraphVertices().contains(v))
            throw new NoSuchElementException();
        return new HashSet<>(getAdj().get(v));
    }

    /**
     *
     * @param v  a vertex
     * @return   the ClosedNeighbors of the vertex {@code v}
     */
    public Set<Integer> getClosedNeighbors(int v) {
        if (!getGraphVertices().contains(v))
            throw new NoSuchElementException();
        Set<Integer> closedNeighbors = getOpenNeighbors(v);
        closedNeighbors.add(v);
        return closedNeighbors;
    }

    /**
     * Add an edge between u and v.
     *
     * Adding an edge between two vertices requires that the two vertices
     * exists already in the graph. If one of the two vertices dos not
     * exist in the graph, this method will return without completing.
     *
     *
     * @param u   the first vertex of the edge
     * @param v   the second vertex of the edge
     */
    public void addEdge( int u, int v)  {
        if (this.getAdj().get(u)==null || this.getAdj().get(v)==null)
            return;
        this.getAdj().get(u).add(v);
        this.getAdj().get(v).add(u);
    }


    /**
     *
     * @param v  the vertex
     * @return   the degree of the vertex {@code v}
     */
    public int getDegreeOf(int v) {
        if (!getGraphVertices().contains(v))
            throw new NoSuchElementException();
        return this.getAdj().get(v).size();
    }

    /**
     *
     * @return the vertex with the maximum degree of this graph
     */
    public  int getMaxDegree() {
        return   Collections.max(this.adj.values(), Comparator.comparingInt(Set::size))
                            .size();

    }

    /**
     * Test if the vertex {@code v} is isolated.
     * a vertex v is isolated  means that d(v)=0.
     *
     * @param v  the vertex
     * @return   true if the vertex {@code v} is isolated, otherwise return false
     *
     */
    public boolean isIsolated(int v){
        if (!getGraphVertices().contains(v))
            throw new NoSuchElementException();

        return this.adj.get(v).size() == 0;

    }
    /**
     *
     *
     * @return true if all vertices in X are degree 0
     */
     public boolean areIsolated( Set<Integer> X) {
         return !X.stream().
                 anyMatch(v->this.adj.get(v).size()>0);
     }

    public int size() {
        return this.adj.size();
    }
    public Set<Integer> getGraphVertices(){
         return this.adj.keySet();
    }
    public int getNumberOfVertices() {
        return n;
    }
    public int getNumberOfEdges() { return m; }
    public void setNumberOfVertices( int n ) {
        this.n = n;
    }
    public Map<Integer, Set<Integer>> getAdj() {
        return this.adj;
    }
    @Override
    public String toString() {
        return this.adj.toString();
    }


    /**
     * testing purpose
     * @param args
     */
    public static void main ( String[] args ) {
        Graph g = new Graph(3);
        g.addVertex(1);
        g.addVertex(2);
        g.addVertex(3);
        g.addVertex(3);
        g.addEdge(1,2);
        g.addEdge(2,3);
        System.out.println(g);
    }


}
