import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import umons.algorithm.dominatingset.exactalgorithm.ArbitraryGraph;
import umons.algorithm.dominatingset.exactalgorithm.ImprovedSetCover;
import umons.algorithm.dominatingset.exactalgorithm.TrivialSetCover;
import umons.algorithm.dominatingset.exactalgorithm.MdsAlgorithm;
import umons.algorithm.dominatingset.graph.Graph;
import umons.algorithm.dominatingset.graph.Result;
import umons.algorithm.dominatingset.util.FileParser;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * This Test Class compare the exact implemented algorithms on some specifics graphes.
 * The graphs instances located in the 'resources\exacts' folder were downloaded from the house of graph database.
 *
 */
@Disabled
public class CompareExactsAlgorithmsTest {

    private static List<Graph> listOfGraphs = new ArrayList();
    private MdsAlgorithm mdsAlgorithm;
    private Result result;


    /**
     *
     * @throws IOException
     * @throws URISyntaxException
     */
    @Test@BeforeAll@Disabled
    public static void compareHouseOfGraphInstance() throws IOException, URISyntaxException {

        Path exacts = Paths.get(CompareExactsAlgorithmsTest.class.getResource("/exacts").toURI());
        Stream<Path> files = Files.list(exacts);
        FileParser fileParser = new FileParser();
        files.forEach(
                p-> {
                    Graph graph = fileParser.createGraphsFromHouseOfGraphs(new File(String.valueOf(p)));
                    listOfGraphs.add(graph);
                    });
    }



    @Test@Disabled
    public void testPerformanceArbitraryGraph(){

       System.out.println("new ArbitraryGraph() Test");
       for (Graph graph: listOfGraphs){
           if (graph.size()==10)
               continue;
           mdsAlgorithm = new ArbitraryGraph();
           result = mdsAlgorithm.run(graph);
           System.out.println(result.getMds());
           System.out.println(result.getTime());
       }
    }

    @Test@Disabled
    public void testPerformanceTrivialAlgo(){

        System.out.println("new TrivialAlgo Test");
        for (Graph graph: listOfGraphs){
            mdsAlgorithm = new TrivialSetCover();
            result = mdsAlgorithm.run(graph);
            System.out.println(result.getMds());
            System.out.println(result.getTime());
        }
    }


    @Test@Disabled
    public void testPerformanceImprovedSetCover(){

        System.out.println("new ImprovedSetCover() Test");
        for (Graph graph: listOfGraphs){
            mdsAlgorithm = new ImprovedSetCover();
            result = mdsAlgorithm.run(graph);
            System.out.println(graph.size());
            System.out.println(result.getMds());
            System.out.println(result.getTime());
        }
    }



}
