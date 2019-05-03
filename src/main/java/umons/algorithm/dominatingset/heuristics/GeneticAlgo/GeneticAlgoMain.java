package umons.algorithm.dominatingset.heuristics.GeneticAlgo;

import umons.algorithm.dominatingset.graph.Graph;
import umons.algorithm.dominatingset.graph.Result;
import umons.algorithm.dominatingset.heuristics.GeneticAlgo.algos.*;
import umons.algorithm.dominatingset.heuristics.GeneticAlgo.operations.UniformCrossOver;
import umons.algorithm.dominatingset.util.FileParser;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class GeneticAlgoMain {


    private static Map<String,Integer> hugesGraphs = new HashMap<>();
    static {
        //  hugesGraphs.put("gplus_200.col",19);
        //  hugesGraphs.put("gplus_500.col",42);
       //   hugesGraphs.put("gplus_2000.col",170);
        //  hugesGraphs.put("pokec_500.col",16);
        //  hugesGraphs.put("pokec_2000.col",75);
            hugesGraphs.put("gplus_10000.col",861);
        //  hugesGraphs.put("gplus_20000.col",1716);
      //    hugesGraphs.put("pokec_10000.col",413);
        //  hugesGraphs.put("pokec_20000.col",921);
        //  hugesGraphs.put("pokec_50000.col",2706);
    }



    public static void main( String[] args ) throws IOException {
        System.out.println("geneticAlgo");
        Iterator<Map.Entry<String, Integer>> it = hugesGraphs.entrySet().iterator();
        while (it.hasNext())
        {
            Map.Entry<String, Integer> pair = it.next();
            String grapheName =  pair.getKey();
            int knownMdsSize  =  pair.getValue();
            System.out.println(grapheName+" "+knownMdsSize);
            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            File file = new File(classloader.getResource(grapheName).getFile());
            Graph hugeGraph = FileParser.createGraphFromDimacsFormat(file);
            GeneticAlgorithm heuristicTwo = new GeneticAlgoImplOne();
            heuristicTwo.setCrossOverStrategy(new UniformCrossOver());
            System.out.println("UniformCrossOver()+ New");
            Result mdsTwo = heuristicTwo.run(hugeGraph,knownMdsSize);
            System.out.println(mdsTwo.getMds().size()+" in "+(mdsTwo.getTime())+" sec");
            System.out.println();
        }
    }

}
