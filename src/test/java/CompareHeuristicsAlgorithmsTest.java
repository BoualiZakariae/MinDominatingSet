import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import umons.algorithm.dominatingset.exactalgorithm.MdsAlgorithm;
import umons.algorithm.dominatingset.graph.Graph;
import umons.algorithm.dominatingset.graph.Result;
import umons.algorithm.dominatingset.heuristics.geneticAlgo.algos.GeneticAlgoImplOne;
import umons.algorithm.dominatingset.heuristics.geneticAlgo.algos.GeneticAlgoImplTwo;
import umons.algorithm.dominatingset.heuristics.geneticAlgo.algos.GeneticAlgorithm;
import umons.algorithm.dominatingset.heuristics.greedy.Greedy;
import umons.algorithm.dominatingset.heuristics.greedy.GreedyRandom;
import umons.algorithm.dominatingset.heuristics.greedy.GreedyRev;
import umons.algorithm.dominatingset.util.FileParser;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
@Disabled
public class CompareHeuristicsAlgorithmsTest {



    public static final String directoryPathFile = "C:\\Users\\bouali\\Desktop\\Thesis2018-2019\\graphes\\graphes\\instances";
    private static List<Graph> listOfGraphs = new ArrayList();
    private MdsAlgorithm mdsAlgorithm;
    private Result result;
    /**
     *dimacs/hugeGraphs/gplus_200.col
     * @throws IOException
     * @throws URISyntaxException
     */
    @Test@BeforeAll@Disabled
    public static void compareHouseOfGraphInstance() throws IOException, URISyntaxException {

        Path exacts = Paths.get(CompareExactsAlgorithmsTest.class.getResource("/dimacs/hugeGraphs").toURI());
        Stream<Path> files = Files.list(exacts);
        files.forEach(
                p-> {
                        Graph hugeGraph = FileParser.createGraphFromDimacsFormat(new File(p.toString()));
                        listOfGraphs.add(hugeGraph);
                        System.out.println(hugeGraph.size());


                });
    }
    @Test@Disabled
    public void  testPerformanceGreedy(){
        for(Graph graph:listOfGraphs){
            System.out.println(graph.size()+" M "+graph.getNumberOfEdges());
            System.out.println("greedy");
            int min = Integer.MAX_VALUE;
            int counter=1;
            double start=0,end=0;
            MdsAlgorithm heuristic;
            Set<Integer> mds = null;
            Set<Integer> minMDS = null;
            while(counter++<10){
                start = System.currentTimeMillis();
                heuristic = new Greedy();
                mds = heuristic.run(graph).getMds();
                end = System.currentTimeMillis();
                if (min>mds.size())
                    minMDS=mds;
              //  System.out.println();
            }
            System.out.println(minMDS.size()+" in "+((end-start)/1000)+" sec");

            System.out.println();

            min = Integer.MAX_VALUE;
            counter=1;
            System.out.println("greedyReverse");
            while(counter++ < 10){
                start = System.currentTimeMillis();
                heuristic = new GreedyRev();
                mds = heuristic.run(graph).getMds();
                end = System.currentTimeMillis();
                if (min>mds.size())
                    minMDS=mds;
             //   System.out.println();

            }
            System.out.println(minMDS.size()+" in "+((end-start)/1000)+" sec");


            System.out.println();
            min = Integer.MAX_VALUE;
            counter=1;
            System.out.println("greedy Random");
            while(counter++<1000){
                start = System.currentTimeMillis();
                heuristic = new GreedyRandom();
                mds = heuristic.run(graph).getMds();
                end = System.currentTimeMillis();
                if (min>mds.size())
                    minMDS=mds;
              //  System.out.println();
            }
            System.out.println(minMDS.size()+" in "+((end-start)/1000)+" sec");
            System.out.println("\n\n");
            System.out.println();
        }
    }


    @Test@Disabled
    public void  testPerformanceGeneticAlgoOne(){
        System.out.println("GeneticOne");
        for (Graph graph: listOfGraphs){
            System.out.println(graph.size()+" M "+graph.getNumberOfEdges());
            double start = System.currentTimeMillis();
            GeneticAlgorithm genetic = new GeneticAlgoImplOne();
            Set<Integer> mds = genetic.run(graph,-1).getMds();
            double end = System.currentTimeMillis();
            System.out.println(mds.size()+" in "+((end-start)/1000)+" sec");
            System.out.println("\n\n");
            System.out.println();
        }
    }



    @Test@Disabled
    public void  testPerformanceGeneticAlgoTwo(){
        System.out.println("GeneticTwo");
        for (Graph graph: listOfGraphs){
            System.out.println(graph.size()+" M "+graph.getNumberOfEdges());
            double start = System.currentTimeMillis();
            GeneticAlgorithm genetic = new GeneticAlgoImplTwo();
            Set<Integer> mds = genetic.run(graph,-1).getMds();
            double end = System.currentTimeMillis();
            System.out.println(mds.size()+" in "+((end-start)/1000)+" ms");
            System.out.println("\n\n");
        }
    }




}
