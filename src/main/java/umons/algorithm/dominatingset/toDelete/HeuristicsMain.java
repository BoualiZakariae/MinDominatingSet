package umons.algorithm.dominatingset.toDelete;

import umons.algorithm.dominatingset.exactalgorithm.MdsAlgorithm;
import umons.algorithm.dominatingset.graph.Graph;
import umons.algorithm.dominatingset.heuristics.greedy.Greedy;
import umons.algorithm.dominatingset.heuristics.greedy.GreedyRandom;
import umons.algorithm.dominatingset.heuristics.greedy.GreedyRev;
import umons.algorithm.dominatingset.util.FileParser;

import java.io.File;
import java.util.Set;

/**
 *
 * This class permit to test the implemented heuristic solution
 *
 *
 *
 */
public class HeuristicsMain {


    static String [] mediumGraphs = {"fpsol2.col"};
    private static String [] googleGraphs = {"gplus_50000.col"};
    //"gplus_10000.col","gplus_20000.col",    "pokec_10000.col","pokec_20000.col" ,"pokec_2000.col"
    static String [] pokecGraphs = {"pokec_50000.col"};
    public static String pathToHugeFile = "C:\\Users\\bouali\\Desktop\\instances";



    public static void main( String[] args ) {
         ClassLoader classloader = Thread.currentThread().getContextClassLoader();
         for (String grapheName: googleGraphs) {
            System.out.println(grapheName);
            File file = new File(classloader.getResource(grapheName).getFile());
            Graph hugeGraph = FileParser.createGraphFromDimacsFormat(file);
            heuristicsrun(hugeGraph);
        }
}

    public static void heuristicsrun(Graph hugeGraph){
        System.out.println("greedy");
        double start = System.currentTimeMillis();
        MdsAlgorithm heuristic = new Greedy();
        Set<Integer> mds = heuristic.run(hugeGraph).getMds();
        double end = System.currentTimeMillis();
        System.out.println(mds.size()+" in "+((end-start)/1000)+" sec");
        System.out.println();
        System.out.println("greedyReverse");
        start = System.currentTimeMillis();
        heuristic = new GreedyRev();
        mds = heuristic.run(hugeGraph).getMds();
        end = System.currentTimeMillis();
        System.out.println(mds.size()+" in "+((end-start)/1000)+" sec");
        System.out.println();
        System.out.println("greedy Random");
        start = System.currentTimeMillis();
        heuristic = new GreedyRandom();
        mds = heuristic.run(hugeGraph).getMds();
        end = System.currentTimeMillis();
        System.out.println(mds.size()+" in "+((end-start)/1000)+" sec");
        System.out.println("\n\n");

    }


}
