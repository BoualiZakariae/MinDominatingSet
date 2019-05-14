package umons.algorithm.dominatingset.heuristics.Greedy;

import umons.algorithm.dominatingset.exactalgorithm.mdsAlgorithm;
import umons.algorithm.dominatingset.graph.Graph;
import umons.algorithm.dominatingset.graph.Result;
import umons.algorithm.dominatingset.util.Stats;

import java.util.*;
import java.util.stream.Collectors;

/**
 * This class implements the Greedy algorithm
 *
 */
public class Greedy implements mdsAlgorithm {

    /**
     * each vertex in the graph has an integer value that
     * represents its weight, and a boolean value that indicate
     * if it is already covered
     */
    HashMap<Integer, Integer> weights ;
    private HashMap<Integer, Boolean> covered ;

    /**
     * Class constructor
     */
    public Greedy(){
        weights =new HashMap<>();
        covered =new HashMap<>();
    }

    /**
     * @return the the maximum weight of the graph
     * this value indicate which vertex can cover
     * the maximum number of vertices
     */
    private int getMax() {
        return this.weights.entrySet()
                .stream()
                .max(Comparator.comparingInt(Map.Entry::getValue))
                .map(Map.Entry::getValue)
                .get();
    }

    /**
     *
     * chooseVertex implementation of the greedy algorithm
     *
     * @return the vertex that will be added to the dominating set
     *
     * this vertex should have the maximum weight
     */
    int chooseVertex() {
     //   System.out.println("it should be never called");
        int M = getMax();
       // System.out.print("the maxim of uncovered  elements is "+ M);
        if (M == 0)
            return -1;
        List<Integer> set = this.weights.entrySet()
                .stream()
                .filter(entry -> entry.getValue() == M)
                .map(Map.Entry::getKey)
                .collect(Collectors.toCollection(ArrayList::new));
        return  set.get(new Random().nextInt(set.size()));
    }


    /**
     * adjustWeights implementation for the greedy algorithm
     * After adding a vertex to the dominating Set, an adjustment
     * of weight should be done on the vertices characteristics
     * @param g
     * @param v
     */
    private void adjustWeights( Graph g, int v ) {
        weights.replace(v, 0);
        for (Integer vj : g.getAdj().get(v)) {
            if (weights.get(vj) > 0) {
                if (!covered.get(v)) {
                    int oldValue = weights.get(vj);
                    weights.replace(vj, oldValue - 1);
                }
                if (!covered.get(vj)) {
                    covered.replace(vj, true);
                    int oldValue = weights.get(vj);
                    weights.replace(vj, oldValue - 1);
                    for (Integer vk : g.getAdj().get(vj)) {
                        if (weights.get(vk) > 0) {
                            oldValue = weights.get(vk);
                            weights.replace(vk, oldValue - 1);
                        }
                    }
                }
            }
        }
        covered.replace(v, true);
    }


    /**
     *
     * @param graph the graph instance
     *
     * @return mds of the graph instance
     */
    @Override
    public Result run( Graph graph ) {
        double start = System.currentTimeMillis();
        Set<Integer> D = new HashSet<>();
        //initialisation
        for (Integer key : graph.getAdj().keySet()) {
            weights.put(key, 1 + graph.getDegreeOf(key));
            covered.put(key, false);
        }
        int v = chooseVertex();
        while (v != -1) {
           // System.out.println("the choosen v is" +v);
            D.add(v);
            adjustWeights(graph, v);
            v = chooseVertex();
        }
        //System.out.println();
        double end = System.currentTimeMillis();
        Stats.numberOfGraphs++;
        return new Result(D,end-start);
        }


    /**
     * testing purpose
     * @param args
     */
    public static void main(String [] args){

        Graph g = new Graph(7);
        g.addVertices(1,2,3,5,1,6,9,7);
        g.addEdge(2, 3);
        g.addEdge(2, 5);
        g.addEdge(3, 1);
        g.addEdge(3, 5);
        g.addEdge(5, 6);
        g.addEdge(1, 6);
        g.addEdge(1, 9);
        g.addEdge(6, 9);
        g.addEdge(7, 9);
        mdsAlgorithm heuristic = new Greedy();
        Result mds = heuristic.run(g);
        System.out.println(mds);

    }
}
