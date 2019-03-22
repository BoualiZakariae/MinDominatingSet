import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import umons.algorithm.dominatingset.exactalgorithm.AtMostDegreeThree;
import umons.algorithm.dominatingset.graph.Graph;
import umons.algorithm.dominatingset.graph.Result;

public class PathGraphInstanceTest
{


    AtMostDegreeThree algo = new AtMostDegreeThree();
    Graph graph;

    @Test
    public void firstPathInstance(){
        graph = new Graph(1);
        graph.addVertices(0);
        algo = new AtMostDegreeThree();
        Result mds = algo.run(graph);
        Assertions.assertEquals(1,mds.getMds().size());
    }

    @Test
    public void secondPathInstance(){
        graph = new Graph(2);
        graph.addVertices(0);
        graph.addVertices(1);
        graph.addEdge(0,1);
        Result mds = algo.run(graph);
        Assertions.assertEquals(1,mds.getMds().size());
    }

    @Test
    public void thirdPathInstance(){
        graph = new Graph(3);
        graph.addVertices(0);
        graph.addVertices(1);
        graph.addVertices(2);
        graph.addEdge(0,1);
        graph.addEdge(1,2);
        Result mds = algo.run(graph);
        Assertions.assertEquals(1,mds.getMds().size());
    }

    @Test
    public void fourthPathInstance(){
        graph = new Graph(4);
        graph.addVertices(0);
        graph.addVertices(1);
        graph.addVertices(2);
        graph.addVertices(3);
        graph.addEdge(0,1);
        graph.addEdge(1,2);
        graph.addEdge(2,3);
        Result mds = algo.run(graph);
        Assertions.assertEquals(2,mds.getMds().size());
    }

    @Test
    public void fifthPathInstance(){
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
        Result mds = algo.run(graph);
        Assertions.assertEquals(2,mds.getMds().size());
    }

    @Test
    public void sixsthPathInstance(){
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
        Result mds = algo.run(graph);
        Assertions.assertEquals(2,mds.getMds().size());
    }

    @Test
    public void seventhPathInstance(){
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
        Result mds = algo.run(graph);
        Assertions.assertEquals(3,mds.getMds().size());
    }

    @Test
    public void eighthPathInstance(){
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
        Result mds = algo.run(graph);
        Assertions.assertEquals(3,mds.getMds().size());
    }


    @Test
    public void ninethPathInstance(){
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
        Result mds = algo.run(graph);
        Assertions.assertEquals(3,mds.getMds().size());
    }
}
