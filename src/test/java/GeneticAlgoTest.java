import org.junit.jupiter.api.Test;
import umons.algorithm.dominatingset.graph.Graph;
import umons.algorithm.dominatingset.heuristics.GeneticAlgo.GeneticAlgoImpl;
import umons.algorithm.dominatingset.heuristics.GeneticAlgo.GeneticAlgorithm;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GeneticAlgoTest {


    @Test
    public void testDominatingSetMinimisation(){

        Graph graph = new Graph(6);
        graph.addVertices(0,1,2,3,4,5);
        graph.addEdge(0,1);
        graph.addEdge(2,1);
        graph.addEdge(3,1);
        graph.addEdge(3,4);
        graph.addEdge(4,5);
        graph.addEdge(4,6);

        /*GeneticAlgoImpl ga = new GeneticAlgoImpl();
        Set<Integer> mds = ga.run(graph,2).getMds();
        System.out.println(mds);*/
        GeneticAlgoImpl ga = new GeneticAlgoImpl();
        Set<Integer> currentMds = Stream.of(1,3,4,5)
                                        .collect(Collectors.toCollection(HashSet::new));
        System.out.println(currentMds);
        ga.minimizeSolution(graph,currentMds);
        System.out.println(currentMds);


    }

}
