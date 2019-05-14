package umons.algorithm.dominatingset.heuristics.Greedy;

import umons.algorithm.dominatingset.exactalgorithm.mdsAlgorithm;
import umons.algorithm.dominatingset.graph.Graph;
import umons.algorithm.dominatingset.graph.Result;

import java.util.HashMap;
import java.util.Random;
import java.util.Set;

/**
 * This class implements the GreedyRandom algorithm
 */
public class GreedyRandom extends Greedy implements mdsAlgorithm{

    /**
     *
     * chooseVertex implementation for the greedy Random algorithm
     *
     * @return the vertex that will be added to the dominating set
     *
     * this vertex is chosen with some amount of probabilities
     */
    @Override
    public int chooseVertex() {
        int sumOfWeights = weights.values()
                                 .stream()
                                 .reduce(0,Integer::sum);
        HashMap<Integer, Double> probabilities = new HashMap<>();
        for (Integer key:weights.keySet()) {
            probabilities.put(key, weights.get(key)* 1.0 / sumOfWeights);
        }
        double cumulativeSum = 0;
        double r = new Random().nextDouble();
        for (Integer key: probabilities.keySet()) {
            cumulativeSum += probabilities.get(key);
            if (cumulativeSum >= r) {
                return key;
            }
        }
        return -1;
    }

    /**
     *
     * @param ds
     * @return
     */
    private Set<Integer>  minimizeSolution(Set<Integer> ds){
        return null;
    }



    @Override
    public Result run( Graph graph ) {
        return super.run(graph);
    }

    public static void main( String[] args ) {
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
        mdsAlgorithm heuristic = new GreedyRandom();
        Result mds = heuristic.run(g);
        System.out.println(mds);
    }
}
