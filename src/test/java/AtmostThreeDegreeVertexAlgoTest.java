import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import umons.algorithm.dominatingset.exactalgorithm.AtMostDegreeThree;
import umons.algorithm.dominatingset.graph.Graph;
import umons.algorithm.dominatingset.graph.Result;


public class AtmostThreeDegreeVertexAlgoTest
{

    AtMostDegreeThree algo = new AtMostDegreeThree();


    @Test
    @Disabled
    public void firstGraphInstance(){
        Graph graph = new Graph(5);
        graph.addVertices(0,1,2,3,4);
        graph.addEdge(0,3);
        graph.addEdge(2,3);
        graph.addEdge(2,4);
        graph.addEdge(1,4);

        algo = new AtMostDegreeThree();
        Result mds = algo.run(graph);
        Assertions.assertEquals(2,mds.getMds().size());
    }


    @Test
    @Disabled
    public void secondGraphInstance(){
        Graph graph = new Graph(6);
        graph.addVertices(0,1,2,3,4,5);
        graph.addEdge(1,4);
        graph.addEdge(4,3);
        graph.addEdge(3,5);
        graph.addEdge(5,2);

        Result mds = algo.run(graph);
        Assertions.assertEquals(3,mds.getMds().size());
    }



    @Test
    @Disabled
    public void thirdGraphInstance(){
        Graph graph = new Graph(6);
        graph.addVertices(0,1,2,3,4,5);
        graph.addEdge(0,4);
        graph.addEdge(4,3);
        graph.addEdge(4,5);
        graph.addEdge(5,1);
        graph.addEdge(5,2);

        Result mds = algo.run(graph);
        Assertions.assertEquals(2,mds.getMds().size());
    }

    @Test
    @Disabled
    public void fourthGraphInstance(){
        Graph graph = new Graph(6);
        graph.addVertices(0,1,2,3,4,5);
        graph.addEdge(0,4);
        graph.addEdge(4,2);
        graph.addEdge(2,3);
        graph.addEdge(5,3);
        graph.addEdge(5,1);
        graph.addEdge(5,4);
        Result mds = algo.run(graph);
        Assertions.assertEquals(2,mds.getMds().size());
    }


    @Test
    @Disabled
    public void fifthGraphInstance(){
        Graph graph = new Graph(7);
        graph.addVertices(0,1,2,3,4,5,6);
        graph.addEdge(0,5);
        graph.addEdge(5,3);
        graph.addEdge(2,3);
        graph.addEdge(2,4);
        graph.addEdge(4,6);
        graph.addEdge(6,1);
        Result mds = algo.run(graph);
        Assertions.assertEquals(3,mds.getMds().size());
    }



}



