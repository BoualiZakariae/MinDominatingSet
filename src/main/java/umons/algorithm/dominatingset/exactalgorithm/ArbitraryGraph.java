package umons.algorithm.dominatingset.exactalgorithm;

import umons.algorithm.dominatingset.graph.Graph;
import umons.algorithm.dominatingset.graph.Node;
import umons.algorithm.dominatingset.graph.Result;
import umons.algorithm.dominatingset.util.Stats;
import umons.algorithm.dominatingset.util.Util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


/**
 *
 *
 *
 * This Class implements the part 3 :"An exact algorithm for the arbitrary graph"
 *
 * from the paper : Exact (exponential) algorithms for the dominating set problem
 *
 * This algorithm represent the 'Algorithme général' in the thesis.
 *
 *
 **/
public class ArbitraryGraph implements MdsAlgorithm {

    /**
     * Initialise and return the set X that should be dominated.
     *
     * In the beginning, the set X is equal to the set
     * of vertices of the graph {@code G}.
     *
     * @param G the {@link Graph} data structure
     * @return the set X
     */
    private static Set<Integer> initialisationOfX(Graph G) {
        return G.getAdj()
                .keySet()
                .stream()
                .collect(Collectors.toCollection(HashSet::new));
    }


    /**
     * Test if {@code dominatingSet} dominates {@code X}
     *
     *
     * @param graph         a {@link Graph} data structure
     * @param dominatingSet the dominating set
     * @param X             set of vertices that should be dominated
     * @return              true if {@code dominatingSet} dominates X, false otherwise
     */
    private static boolean isDominating( Graph graph, int[] dominatingSet, Set<Integer> X ) {
        Set<Integer> dominatedVertices = new HashSet<>();
       for (int x : dominatingSet) {
           Set<Integer> neighborsOfx = graph.getClosedNeighbors(x);
            for (Integer e : neighborsOfx) {
                dominatedVertices.add(e);
            }
        }
        return dominatedVertices.containsAll(X);
    }



    /**
     * Test if the {@code dominatingSet} dominates X
     * @param graph         a {@link Graph} data structure
     * @param dominatingSet the dominating set
     * @param X             set of vertices that should be dominated
     * @return              true if {@code dominatingSet} dominates X, false otherwise
     */
    public static boolean isDominating(Graph graph, Set<Integer> dominatingSet, Set<Integer> X) {
        Set<Integer> dominatedVertices = new HashSet<>();
        for (int x : dominatingSet) {
            Set<Integer> neighborsOfx = graph.getClosedNeighbors(x);
            for (Integer e : neighborsOfx) {
                dominatedVertices.add(e);
            }
        }
        return dominatedVertices.containsAll(X);
    }


    /**
     * Return the mds from a base case where every vertex is at least degree three.
     *
     * @param graph a {@link Graph} data structure
     * @param X
     * @return the mds for the base case where every vertex has at least 3 neighbors
     */
    private static Set<Integer> minDominatingSet( Graph graph, Set<Integer> X ) {
        if (X.isEmpty()) {
            return new HashSet<>();
        }
        int t = graph.size();
        int maxMdsSize = 3 *t / 8;
        int[] currentMds = null;
        int minMdsSize = Integer.MAX_VALUE;
        int[] V = new int[graph.size()];
        int index=0;
        for (Integer key : graph.getAdj().keySet()) {
            V[index] = key;
            index++;
        }
        //loop on all subset and get the min dominating set
       myloopToBreak: for (int currentSubsetSize = 1; currentSubsetSize <= maxMdsSize; currentSubsetSize++) {
                            ArrayList<int[]> combinations = Util.getCombinations(V, currentSubsetSize);
                            for (int[] combination : combinations) {
                                if (isDominating(graph, combination, X) && combination.length < minMdsSize) {
                                    currentMds = combination;
                                    break myloopToBreak;
                                }
                            }
                      }

        Set<Integer> al = new HashSet<>();
        if (currentMds != null) {
            for (int x : currentMds) {
                al.add(x);
            }
        }

        return al;
    }


    /**
     * Return the set of vertices with degree zero
     *
     * @param graph  a {@link Graph} data structure
     * @return       all the vertices v where d(v)= 0
     */
    private static Set<Integer> getZeroDegreeVertices( Graph graph, Set<Integer> X ) {
        return X.stream()
                .filter(i->graph.getAdj().get(i).isEmpty())
                .collect(Collectors.toCollection(HashSet::new));
    }


    /**
     *
     * The main method for the arbitrary graph algorithm
     *
     * @param graph  a {@link Graph} data structure {@link Graph}
     * @param X      the set X to dominate
     * @return       the minimum dominating set of the set X
     */
    private static Set<Integer> mdsOfrArbitraryGraph( Graph graph, Set<Integer> X ) {
        if (X.isEmpty())
            return new HashSet<>();
        Optional<Node> v = graph.getVertexOfDegreeOneOrTwo();
        if (!v.isPresent())//degree 0 or degree  >=3
            return baseCase(graph, X);
        if (v.get().getDegree() == 1)
            return mdsFromDegreeOneVertex(X, v.get(), graph);
         else
            return mdsFromDegreeTwoVertex(X, v.get(), graph);
    }


    /**
     *
     * This method deal with the base case where every vertices
     * in the graph is degree zero or at least 3.
     *
     * @param graph  a {@link Graph} data structure
     * @param X      the set X to dominate
     * @return       the minimum dominating set of the set X
     */
    private static Set<Integer> baseCase( Graph graph, Set<Integer> X ) {
        if (graph.areIsolated(X)) {
            return X;
        }
        /*
         * all node with 0 or >=3 degree
         *
         */
        Set<Integer> zeroDegreeVertices = getZeroDegreeVertices(graph, X);
        if (zeroDegreeVertices.size() > 0) {
            Graph gPrime = graph.removeVertices(zeroDegreeVertices);
            Set<Integer> newX = X.stream()  /*set of vertices that has at least 3 neighbours*/
                                 .filter(x -> !zeroDegreeVertices.contains(x))
                                 .collect(Collectors.toCollection(HashSet::new));
            Set<Integer> mds = minDominatingSet(gPrime, newX);
            zeroDegreeVertices.stream()
                    .filter(v->X.contains(v))
                    .forEach(v->mds.add(v));
            return mds;
        }
        /*
         * all node with degree >=3
         */
        return minDominatingSet(graph, X);/*looking for the mds in the subsets */
    }



    /**
     * This method represent the case A and B in the paper.
     *
     * Return the mds of X from a degree one vertex,
     * branching in the case A if v is not in X,
     * branching in the case B otherwise.
     *
     * @param X      the set X to dominate
     * @param v      the chosen vertex with degree one
     * @param graph  a {@link Graph} data structure
     * @return       the minimum dominating set of X from a one degree vertex
     */
    private static Set<Integer> mdsFromDegreeOneVertex( Set<Integer> X, Node v, Graph graph ) {
        Graph gPrime;
        Set<Integer> newX ;
        Set<Integer> dPrime;
        if (!X.contains(v.getId())){// v in V - X, case A
            gPrime = graph.removeVertex(v.getId());
            newX = X.stream()
                    .collect(Collectors.toCollection(HashSet::new));
            return mdsOfrArbitraryGraph(gPrime,newX);
        } else {// case B, v in X
            int[] neighbors = Util.getNodes(graph.getAdj().get(v.getId()),1);
            int w = neighbors[0];/*unique neighbour of v*/
            //looking for ArbitraryGraph of graph-{v,w} in X -N[w]
            Set<Integer> vertices = new HashSet<>();
            vertices.add(v.getId());
            vertices.add(w);
            gPrime = graph.removeVertices(vertices);
            Set<Integer> closedNeighborsOfw = graph.getClosedNeighbors(w);
            newX =  X.stream()
                    .filter(x->!closedNeighborsOfw.contains(x))
                    .collect(Collectors.toCollection(HashSet::new));
            dPrime = mdsOfrArbitraryGraph(gPrime, newX);
            dPrime.add(w);
            return dPrime;
        }
    }



    /**
     * This method represents the case C and D.
     *
     * Return the mds of X from a degree two vertex,
     * branching in case C or D whether v is in X or not.
     *
     * @param X      the set X to dominate
     * @param v      the chosen vertex with degree two
     * @param graph  a {@link Graph} data structure
     * @return       the minimum dominating set of X from a two degree vertex
     */
    private static Set<Integer> mdsFromDegreeTwoVertex( Set<Integer> X, Node v, Graph graph ) {
        int[] neighbors = Util.getNodes(graph.getAdj().get(v.getId()),2);
        int u1 = neighbors[0];
        int u2 = neighbors[1];
        if (!X.contains(v.getId()))
            return caseCFromDegreeTwoVertex(v, graph, X, u1, u2);
         else
            return caseDFromDegreeTwoVertex(v, graph, X, u1, u2);
    }



    /**
     * This method represents the case C, where
     * v is not in X.
     *
     * @param v      the chosen {@link Node} with degree two
     * @param graph  a {@link Graph} data structure
     * @param X      the set X to dominate
     * @param u1     the first  neighbor of v
     * @param u2     the second neighbor of v
     * @return       the minimum dominating set of X from a degree two vertex
     */
    private static Set<Integer> caseCFromDegreeTwoVertex( Node v, Graph graph, Set<Integer> X, int u1, int u2 ) {
        Set<Integer> vertices = new HashSet<>();
        //C1
        vertices.add(v.getId());
        vertices.add(u1);
        Graph gPrime = graph.removeVertices(vertices);
        Set<Integer> closedNeighborsOfu1 = graph.getClosedNeighbors(u1);
        Set<Integer> newX =  X.stream()
                              .filter(x->!closedNeighborsOfu1.contains(x))
                              .collect(Collectors.toCollection(HashSet::new));
        Set<Integer> d1 = mdsOfrArbitraryGraph(gPrime, newX);
        d1.add(u1);

        //C2        
        vertices.add(u2);
        gPrime = graph.removeVertices(vertices);
        newX =  X.stream()
                 .filter(x->x!=u1 && x!=u2)
                 .collect(Collectors.toCollection(HashSet::new));
        Set<Integer> d2 = mdsOfrArbitraryGraph(gPrime, newX);
        d2.add(v.getId());

        //C3
        vertices = new HashSet<>();
        vertices.add(v.getId());
        gPrime = graph.removeVertices(vertices);
        newX = new HashSet<>();
        newX.addAll(X);
        Set<Integer> d3 = mdsOfrArbitraryGraph(gPrime, newX);


        return Util.minOfTheSet(d1,d2,d3);
    }



    /**
     * This method represents the case D, where
     * v is in X.
     *
     * @param v      the chosen {@link Node} with degree two
     * @param graph  a {@link Graph} data structure
     * @param X      the set X to dominate
     * @param u1     the first  neighbor of v
     * @param u2     the second neighbor of v
     * @return       the minimum dominating set of X from a degree two vertex
     */
    private static Set<Integer> caseDFromDegreeTwoVertex( Node v, Graph graph, Set<Integer> X, int u1, int u2 ) {
        Set<Integer> vertices = new HashSet<>();
        // case D.1
        vertices.add(u1);
        vertices.add(v.getId());
        Graph gPrime = graph.removeVertices(vertices);
        Set<Integer> closedNeighborsOfu1 = graph.getClosedNeighbors(u1);
        Set<Integer>  newX =  X.stream()
                               .filter(x->!closedNeighborsOfu1.contains(x))
                               .collect(Collectors.toCollection(HashSet::new));
        Set<Integer> d1 = mdsOfrArbitraryGraph(gPrime, newX);
        d1.add(u1);

        //case D2
        vertices.add(u2);
        gPrime = graph.removeVertices(vertices);
        newX =  X.stream()
                 .filter(x->x!=u1 && x!=u2&& x!=v.getId())
                 .collect(Collectors.toCollection(HashSet::new));
        Set<Integer> d2 = mdsOfrArbitraryGraph(gPrime, newX);
        d2.add(v.getId());

        //case D3
        vertices = new HashSet<>();
        vertices.add(v.getId());
        vertices.add(u2);
        gPrime = graph.removeVertices(vertices);
        Set<Integer> closedNeighborsOfu2 = graph.getClosedNeighbors(u2);
        newX =  X.stream()
                 .filter(x->!closedNeighborsOfu2.contains(x))
                 .collect(Collectors.toCollection(HashSet::new));
        Set<Integer> d3 = mdsOfrArbitraryGraph(gPrime, newX);
        d3.add(u2);

        return Util.minOfTheSet(d1,d2,d3);

    }
    /**
     * Run he arbitrary graph algorithm on the graph instance {@Code g},
     * return a Result object that contains the minimum dominating set of {@Code g}
     * and the time taken to compute it.
     *
     * @param graph the {@link Graph} instance
     * @return      the {@link Result}
     */
    @Override
    public Result run( Graph graph ) {
        double start = System.currentTimeMillis();
        Set<Integer> X = initialisationOfX(graph);
        Set<Integer> mds = mdsOfrArbitraryGraph(graph, X);
        double end = System.currentTimeMillis();
        Stats.numberOfGraphs++;
        return new Result(mds,end-start);
    }
}
