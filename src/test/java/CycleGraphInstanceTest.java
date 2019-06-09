import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import umons.algorithm.dominatingset.exactalgorithm.AtMostDegreeThree;
import umons.algorithm.dominatingset.graph.Graph;
import umons.algorithm.dominatingset.graph.Result;

@Disabled
public class CycleGraphInstanceTest
{


    private AtMostDegreeThree algo = new AtMostDegreeThree();
    private Graph graph;


    @Test@Disabled
    public void thirdCycleInstance(){
        graph = new Graph(3);
        graph.addVertices(0);
        graph.addVertices(1);
        graph.addVertices(2);
        graph.addEdge(0,1);
        graph.addEdge(1,2);
        graph.addEdge(0,2);
        Result mds = algo.run(graph);
        Assertions.assertEquals(1,mds.getMds().size());
    }

    @Test@Disabled
    public void fourthCycleInstance(){
        graph = new Graph(4);
        graph.addVertices(0);
        graph.addVertices(1);
        graph.addVertices(2);
        graph.addVertices(3);
        graph.addEdge(0,1);
        graph.addEdge(1,2);
        graph.addEdge(2,3);
        graph.addEdge(0,3);
        Result mds = algo.run(graph);
        Assertions.assertEquals(2,mds.getMds().size());
    }

    @Test@Disabled
    public void fifthCycleInstance(){
        graph = new Graph(5);
        graph.addVertices(0);
        graph.addVertices(1);
        graph.addVertices(2);
        graph.addVertices(3);
        graph.addVertices(4);
        graph.addEdge(0,1);
        graph.addEdge(1,2);
        graph.addEdge(2,3);
        graph.addEdge(3,4);
        graph.addEdge(0,4);
        Result mds = algo.run(graph);
        Assertions.assertEquals(2,mds.getMds().size());
    }

    @Test@Disabled
    public void sixsthCycleInstance(){
        graph = new Graph(6);
        graph.addVertices(0);
        graph.addVertices(1);
        graph.addVertices(2);
        graph.addVertices(3);
        graph.addVertices(4);
        graph.addVertices(5);

        graph.addEdge(0,1);
        graph.addEdge(1,2);
        graph.addEdge(2,3);
        graph.addEdge(3,4);
        graph.addEdge(4,5);
        graph.addEdge(0,5);
        Result mds = algo.run(graph);
        Assertions.assertEquals(2,mds.getMds().size());
    }

    @Test@Disabled
    public void seventhCycleInstance(){
        graph = new Graph(7);
        graph.addVertices(0);
        graph.addVertices(1);
        graph.addVertices(2);
        graph.addVertices(3);
        graph.addVertices(4);
        graph.addVertices(5);
        graph.addVertices(6);
        graph.addEdge(0,1);
        graph.addEdge(1,2);
        graph.addEdge(2,3);
        graph.addEdge(3,4);
        graph.addEdge(4,5);
        graph.addEdge(5,6);
        graph.addEdge(0,6);
        Result mds = algo.run(graph);
        Assertions.assertEquals(3,mds.getMds().size());
    }

    @Test@Disabled
    public void eighthCycleInstance(){
        graph = new Graph(8);
        graph.addVertices(0);
        graph.addVertices(1);
        graph.addVertices(2);
        graph.addVertices(3);
        graph.addVertices(4);
        graph.addVertices(5);
        graph.addVertices(6);
        graph.addVertices(7);
        graph.addEdge(0,1);
        graph.addEdge(1,2);
        graph.addEdge(2,3);
        graph.addEdge(3,4);
        graph.addEdge(4,5);
        graph.addEdge(5,6);
        graph.addEdge(6,7);
        graph.addEdge(0,7);
        Result mds = algo.run(graph);
        Assertions.assertEquals(3,mds.getMds().size());
    }


    @Test@Disabled
    public void ninethCycleInstance(){
        graph = new Graph(9);
        graph.addVertices(0);
        graph.addVertices(1);
        graph.addVertices(2);
        graph.addVertices(3);
        graph.addVertices(4);
        graph.addVertices(5);
        graph.addVertices(6);
        graph.addVertices(7);
        graph.addVertices(8);
        graph.addEdge(0,1);
        graph.addEdge(1,2);
        graph.addEdge(2,3);
        graph.addEdge(3,4);
        graph.addEdge(4,5);
        graph.addEdge(5,6);
        graph.addEdge(6,7);
        graph.addEdge(7,8);
        graph.addEdge(0,8);
        Result mds = algo.run(graph);
        Assertions.assertEquals(3,mds.getMds().size());
    }
}
