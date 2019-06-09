import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import umons.algorithm.dominatingset.graph.Graph;
import umons.algorithm.dominatingset.heuristics.geneticAlgo.algos.GeneticAlgoImplOne;
import umons.algorithm.dominatingset.heuristics.geneticAlgo.algos.GeneticAlgoUtil;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
@Disabled
public class GeneticAlgoTest {


    @Test@Disabled

    public void testDominatingSetMinimisation(){

        Graph graph = new Graph(7);
        graph.addVertices(0,1,2,3,4,5,6);
        graph.addEdge(0,1);
        graph.addEdge(2,1);
        graph.addEdge(3,1);
        graph.addEdge(3,4);
        graph.addEdge(4,5);
        graph.addEdge(4,6);

        /*GeneticAlgoImplOne ga = new GeneticAlgoImplOne();
        Set<Integer> mds = ga.run(graph,2).getMds();
        System.out.println(mds);*/
        GeneticAlgoImplOne ga = new GeneticAlgoImplOne();
        Set<Integer> currentMds = Stream.of(1,3,4,5)
                                        .collect(Collectors.toCollection(HashSet::new));
        System.out.println(currentMds);
        GeneticAlgoUtil.minimizeSolution(graph,currentMds);
        System.out.println(currentMds);


    }

}
