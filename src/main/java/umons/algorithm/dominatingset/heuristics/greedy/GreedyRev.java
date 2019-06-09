package umons.algorithm.dominatingset.heuristics.greedy;

import umons.algorithm.dominatingset.exactalgorithm.MdsAlgorithm;
import umons.algorithm.dominatingset.graph.Graph;
import umons.algorithm.dominatingset.graph.Result;
import umons.algorithm.dominatingset.util.Stats;

import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * This class implements the GreedyReverse algorithm
 */
public class GreedyRev implements MdsAlgorithm {


    /**
     * each vertex in the graph has an integer value that
     * indicate how much it can be covered, and a boolean
     * value that indicate if it is uniquely covered
     */
    private HashMap<Integer, Integer> coveredBy ;
    private HashMap<Integer, Boolean> uniquely ;
    private int n;

    /**
     * Class constructor
     */
    public GreedyRev(){
        this.coveredBy = new HashMap<>();
        this.uniquely = new HashMap<>();
    }



    /**
     *
     * chooseVertex implementation for the greedy Reverse algorithm
     *
     * @return the vertex that will be deleted from the dominating set
     *
     * After removing this vertex from the dominating set D, D will still be a dominating set
     *
     **/
    private int chooseVertex( Graph g, Set<Integer> D, List<Integer> V, int start ) {
        while (start < n && (!D.contains(V.get(start)) ||  uniquely.get(V.get(start)))) {
            start++;
        }
        if (start >= n) {
            return -1;
        }
        int min =    g.getDegreeOf(V.get(start));
        List<Integer> set = g.getAdj().keySet()
                                      .stream()
                                      .filter(key->g.getDegreeOf(key)==min && !uniquely.get(key)&&D.contains(key))
                                      .collect(Collectors.toCollection(ArrayList::new));

        return set.get(new Random().nextInt(set.size()));

    }

    /**
     *
     * @param D
     * @param v
     * @param g
     *
     * adjust implementation for the greedy algorithm
     *
     */
    private void adjust( Set<Integer> D, int v, Graph g ) {
        int oldValue = coveredBy.get(v);
        coveredBy.replace(v, oldValue - 1);

        for (Integer vj : g.getAdj().get(v)) {
            if (coveredBy.get(v) == 1 && D.contains(vj))
                uniquely.replace(vj,true);
            oldValue = coveredBy.get(vj);
            coveredBy.replace(vj, oldValue - 1);
            if (coveredBy.get(vj) == 1) {
                if (D.contains(vj)) {
                    uniquely.replace(vj,true);
                } else {
                    for (Integer vk : g.getAdj().get(vj)) {
                        if (D.contains(vk)) {
                            uniquely.replace(vk,true);
                        }
                    }
                }
            }
        }

    }

    /**
     *
     * @param g
     * @return  mds of the graph instance
     */
    @Override
    public Result run( Graph g ){
        double startTime = System.currentTimeMillis();
        int start = 0;
        n=g.size();
        List<Integer> v1 = g.getAdj().keySet()
                .stream()
                .sorted(( arg0, arg1 ) -> g.getDegreeOf(arg0) - g.getDegreeOf(arg1))
                .collect(Collectors.toCollection(ArrayList::new));

        Set<Integer> D = new HashSet<>(v1);
        for (int key: g.getAdj().keySet() ) {
            coveredBy.put(key,1 + g.getDegreeOf(key));
            if (coveredBy.get(key) == 1)
                uniquely.put(key,true);
             else
                uniquely.put(key,false);
        }

        int v = chooseVertex(g,D, v1, start);

        while (v != -1) {
            D.remove(v);
            adjust(D, v, g);
            v = chooseVertex(g,D, v1, start);
        }
        double end = System.currentTimeMillis();
        Stats.numberOfGraphs++;
        return new Result(D,end-startTime);
    }

    /**
     * testing purpose
     * @param args
     */
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
        MdsAlgorithm heuristic = new GreedyRev();
        Result mds = heuristic.run(g);
        System.out.println(mds);

    }
}
