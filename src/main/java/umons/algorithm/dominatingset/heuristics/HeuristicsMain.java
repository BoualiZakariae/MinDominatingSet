package umons.algorithm.dominatingset.heuristics;

import umons.algorithm.dominatingset.exactalgorithm.mdsAlgorithm;
import umons.algorithm.dominatingset.graph.Graph;
import umons.algorithm.dominatingset.heuristics.GeneticAlgo.GeneticAlgorithm;
import umons.algorithm.dominatingset.heuristics.Greedy.Greedy;
import umons.algorithm.dominatingset.heuristics.Greedy.GreedyRandom;
import umons.algorithm.dominatingset.heuristics.Greedy.GreedyRev;
import umons.algorithm.dominatingset.util.FileParser;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 *
 * This class permit to test the implemented heuristic solution
 *
 *
 *
 */
public class HeuristicsMain {



    static Map<String,Integer> hugesGraphs = new HashMap<>();
    static {

        hugesGraphs.put("gplus_200.col",19);
        hugesGraphs.put("gplus_500.col",42);
        hugesGraphs.put("gplus_2000.col",170);
        hugesGraphs.put("pokec_500.col",16);
        hugesGraphs.put("pokec_2000.col",75);
    }


    static String [] mediumGraphs = {"fpsol2.col"};
    static String [] googleGraphs = {"gplus_50000.col"};
    //"gplus_10000.col","gplus_20000.col",    "pokec_10000.col","pokec_20000.col" ,"pokec_2000.col"
    static String [] pokecGraphs = {"pokec_50000.col"};
    public static String pathToHugeFile = "C:\\Users\\bouali\\Desktop\\instances";



    public static void main( String[] args ) throws IOException {
        /*Iterator iterator = hugesGraphs.entrySet().iterator();
        while (iterator.hasNext())
        {
            Map.Entry pair = (Map.Entry) iterator.next();
            String grapheName = (String) pair.getKey();
            int knownMDSSize = (int) pair.getValue();
        }*/
        //geneticRun();
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
         for (String grapheName: googleGraphs) {
            System.out.println(grapheName);
            File file = new File(classloader.getResource(grapheName).getFile());
            Graph hugeGraph = FileParser.createGraphFromDimacsFormat(file);
            heuristicsrun(hugeGraph);
        }




    }

    public static void heuristicsrun(Graph hugeGraph){
       /* System.out.println("greedy");
        double start = System.currentTimeMillis();
        mdsAlgorithm heuristic = new Greedy();
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
        System.out.println("\n\n");*/

        System.out.println("geneticAlgo");
        double start = System.currentTimeMillis();
        mdsAlgorithm heuristic = new GeneticAlgorithm();
        Set<Integer> mds = heuristic.run(hugeGraph).getMds();
        double end = System.currentTimeMillis();
        System.out.println(mds.size()+" in "+((end-start)/1000)+" sec");
        System.out.println();

    }




    public static void geneticRun() throws IOException {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        System.out.println("geneticAlgo");
        double start = System.currentTimeMillis();
        String grapheName = "gplus_200.col";
        File file = new File(classloader.getResource(grapheName).getFile());
        Graph hugeGraph = FileParser.createGraphFromDimacsFormat(file);
        GeneticAlgorithm heuristic = new GeneticAlgorithm();
        System.out.println("    public static final int     POPULATION_SIZE = 100;\n" +
                "    public static final int     MAX_GEN = 20000;//number of iteration\n" +
                "    public static final double  P_DS = 0.3;\n" +
                "    public static final double  P_CROSSOVER = 0.7;//probability of cross over\n" +
                "    public static final double  P_MUTATION = 0.1;//probability for mutation\n" +
                "    public static final double  P_H = 0.8;\n" +
                "    public static final double  P_BETTER = 0.8;");
        Set<Integer> mds = heuristic.run(hugeGraph).getMds();
        double end = System.currentTimeMillis();
        System.out.println(mds.size()+" in "+((end-start)/1000)+" sec");
        System.out.println();
    }

}
