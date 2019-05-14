import org.junit.jupiter.api.*;
import umons.algorithm.dominatingset.exactalgorithm.*;
import umons.algorithm.dominatingset.graph.Graph;
import umons.algorithm.dominatingset.graph.Result;
import umons.algorithm.dominatingset.util.FileParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;


public class AtmostThreeDegreeVertexAlgoTest
{

    AtMostDegreeThree algo = new AtMostDegreeThree();
    private Graph graph;
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

    @Test
    @Disabled
    public void testGetNeighborsOf3DegreeVertices(){
        Graph graph = new Graph(8);
        graph.addVertices(0,1,2,3,4,5,6,7);
        graph.addEdge(0,5);
        graph.addEdge(5,7);
        graph.addEdge(5,6);
        graph.addEdge(6,1);
        graph.addEdge(3,6);
        graph.addEdge(3,4);
        graph.addEdge(4,7);
        graph.addEdge(2,7);
        List<Integer> listOfNeighbors = graph.getNeighborsOf3DegreeVertices()
                                            .stream()
                                            .map(node -> node.getId())
                                            .collect(Collectors.toCollection(ArrayList::new));;

        List<Integer> correctList = Stream.of(0, 1, 2).collect(Collectors.toCollection(ArrayList::new));

        Assertions.assertEquals(correctList, listOfNeighbors);
    }

    @Test
    @Disabled
    public void testingSetOfSetsDuplicate(){

        Set<Set<Integer>> setOfMds = new HashSet<>();
        Set<Integer> set1 = Stream.of(0,1).collect(Collectors.toCollection(HashSet::new));
        Set<Integer> set2 = Stream.of(0,2).collect(Collectors.toCollection(HashSet::new));
        Set<Integer> set3 = Stream.of(1,0).collect(Collectors.toCollection(HashSet::new));
        Set<Integer> set4 = Stream.of(1).collect(Collectors.toCollection(HashSet::new));
        setOfMds.add(set1);
        setOfMds.add(set2);
        setOfMds.add(set3);
        setOfMds.add(set1);
        setOfMds.add(set4);
        Assertions.assertEquals(3, setOfMds.size());

        System.out.println(setOfMds);
        System.out.println(setOfMds.stream().min(Comparator.comparing(Set::size)).get());

    }

    @Test@Disabled
    public void testPerformanceComparisonOne(){

        Graph graph = new Graph(32);
        IntStream.range(0,32)
                .forEach(i->graph.addVertex(i));
        Assertions.assertEquals(32,graph.size());

        graph.addEdge(0,1);
        graph.addEdge(0,2);
        graph.addEdge(0,3);
        graph.addEdge(1,3);
        graph.addEdge(1,4);
        graph.addEdge(2,3);
        graph.addEdge(2,4);
        graph.addEdge(4,5);



        graph.addEdge(10,7);
        graph.addEdge(10,9);
        graph.addEdge(10,8);
        graph.addEdge(7,8);
        graph.addEdge(7,6);
        graph.addEdge(9,8);
        graph.addEdge(9,6);
        graph.addEdge(6,5);

        graph.addEdge(5,11);
        graph.addEdge(11,12);
        graph.addEdge(12,13);
        graph.addEdge(12,14);
        graph.addEdge(13,15);
        graph.addEdge(13,16);
        graph.addEdge(14,17);
        graph.addEdge(14,18);
        graph.addEdge(15,16);
        graph.addEdge(16,17);
        graph.addEdge(17,18);

        graph.addEdge(11,19);
        graph.addEdge(19,20);
        graph.addEdge(19,21);
        graph.addEdge(20,22);
        graph.addEdge(20,23);
        graph.addEdge(21,24);
        graph.addEdge(21,25);
        graph.addEdge(18,22);
        graph.addEdge(22,23);
        graph.addEdge(23,24);
        graph.addEdge(24,25);

        graph.addEdge(15,26);
        graph.addEdge(26,25);
        graph.addEdge(26,27);
        graph.addEdge(27,28);
        graph.addEdge(27,29);

        graph.addEdge(28,30);
        graph.addEdge(28,31);

        graph.addEdge(29,30);
        graph.addEdge(29,31);

        graph.addEdge(30,31);

        mdsAlgorithm mdsAlgo = new AtMostDegreeThree();
        System.out.println("AtMostDegreeThree()");
        Result result = mdsAlgo.run(graph);
        System.out.println(result.getMds());
        System.out.println(result.getTime());

        mdsAlgo = new ImprovedSetCover();
        System.out.println("new improvedSetCover)");
        result = mdsAlgo.run(graph);
        System.out.println(result.getMds());
        System.out.println(result.getTime());

        mdsAlgo = new TrivialSetCover();
        System.out.println("new trivial)");
        result = mdsAlgo.run(graph);
        System.out.println(result.getMds());
        System.out.println(result.getTime());


        mdsAlgo = new ArbitraryGraph();
        System.out.println("new ArbitraryGraph()()");
        result = mdsAlgo.run(graph);
        System.out.println(result.getMds());
        System.out.println(result.getTime());



    }

    @Test@Disabled
    public void testPerformanceComparisonTwo(){

        Graph graph = new Graph(22);
        IntStream.range(0,22)
                .forEach(i->graph.addVertex(i));
        Assertions.assertEquals(22,graph.size());

        graph.addEdge(0,1);
        graph.addEdge(0,2);
        graph.addEdge(0,3);
        graph.addEdge(1,3);
        graph.addEdge(1,4);
        graph.addEdge(2,3);
        graph.addEdge(2,4);
        graph.addEdge(4,5);



        graph.addEdge(10,7);
        graph.addEdge(10,9);
        graph.addEdge(10,8);
        graph.addEdge(7,8);
        graph.addEdge(7,6);
        graph.addEdge(9,8);
        graph.addEdge(9,6);
        graph.addEdge(6,5);

        graph.addEdge(5,11);
        graph.addEdge(11,12);
        graph.addEdge(12,13);
        graph.addEdge(12,14);
        graph.addEdge(13,15);
        graph.addEdge(13,16);
        graph.addEdge(14,15);
        graph.addEdge(14,16);
        graph.addEdge(15,16);

        graph.addEdge(11,17);
        graph.addEdge(17,18);
        graph.addEdge(17,19);
        graph.addEdge(18,20);
        graph.addEdge(18,21);
        graph.addEdge(19,20);
        graph.addEdge(19,21);
        graph.addEdge(20,21);

        mdsAlgorithm mdsAlgo = new AtMostDegreeThree();
        System.out.println("AtMostDegreeThree()");
        Result result = mdsAlgo.run(graph);
        System.out.println(result.getMds());
        System.out.println(result.getTime());



        mdsAlgo = new TrivialSetCover();
        System.out.println("new trivial)");
        result = mdsAlgo.run(graph);
        System.out.println(result.getMds());
        System.out.println(result.getTime());

        mdsAlgo = new ArbitraryGraph();
        System.out.println("new ArbitraryGraph()()");
        result = mdsAlgo.run(graph);
        System.out.println(result.getMds());
        System.out.println(result.getTime());

        mdsAlgo = new ImprovedSetCover();
        System.out.println("new improvedSetCover)");
        result = mdsAlgo.run(graph);
        System.out.println(result.getMds());
        System.out.println(result.getTime());
    }

    @Test
    public void testPerformanceComparisonThree(){

        Graph graph = new Graph(46);
        IntStream.range(0,46)
                .forEach(i->graph.addVertex(i));
        Assertions.assertEquals(46,graph.size());

        graph.addEdge(0,1);
        graph.addEdge(0,2);
        graph.addEdge(0,3);
        graph.addEdge(1,3);
        graph.addEdge(1,4);
        graph.addEdge(2,3);
        graph.addEdge(2,4);
        graph.addEdge(4,5);



        graph.addEdge(10,7);
        graph.addEdge(10,9);
        graph.addEdge(10,8);
        graph.addEdge(7,8);
        graph.addEdge(7,6);
        graph.addEdge(9,8);
        graph.addEdge(9,6);
        graph.addEdge(6,5);

        graph.addEdge(5,11);
        graph.addEdge(11,12);
        graph.addEdge(12,13);
        graph.addEdge(12,14);
        graph.addEdge(13,15);
        graph.addEdge(13,16);
        graph.addEdge(14,17);
        graph.addEdge(14,18);
        graph.addEdge(15,16);
        graph.addEdge(16,17);
        graph.addEdge(17,18);

        graph.addEdge(11,19);
        graph.addEdge(19,20);
        graph.addEdge(19,21);
        graph.addEdge(20,22);
        graph.addEdge(20,23);
        graph.addEdge(21,24);
        graph.addEdge(21,25);
        graph.addEdge(18,22);
        graph.addEdge(22,23);
        graph.addEdge(23,24);
        graph.addEdge(24,25);

        graph.addEdge(15,26);
        graph.addEdge(25,27);

        graph.addEdge(28,26);
        graph.addEdge(29,26);

        graph.addEdge(30,27);
        graph.addEdge(31,27);


        graph.addEdge(28,32);
        graph.addEdge(28,33);
        graph.addEdge(29,34);
        graph.addEdge(29,35);

        graph.addEdge(30,36);
        graph.addEdge(30,37);
        graph.addEdge(31,38);
        graph.addEdge(31,39);

        graph.addEdge(32,33);
        graph.addEdge(33,34);
        graph.addEdge(34,35);
        graph.addEdge(35,36);
        graph.addEdge(36,37);
        graph.addEdge(37,38);
        graph.addEdge(38,39);
        graph.addEdge(39,40);

        graph.addEdge(32,40);


        graph.addEdge(40,41);
        graph.addEdge(41,42);
        graph.addEdge(41,43);
        graph.addEdge(42,44);
        graph.addEdge(42,45);
        graph.addEdge(43,44);
        graph.addEdge(43,45);
        graph.addEdge(44,45);



        mdsAlgorithm mdsAlgo = new AtMostDegreeThree();
        System.out.println("AtMostDegreeThree()");
        Result result = mdsAlgo.run(graph);
        System.out.println(result.getMds());
        System.out.println(result.getTime());



        mdsAlgo = new TrivialSetCover();
        System.out.println("new trivial)");
        result = mdsAlgo.run(graph);
        System.out.println(result.getMds());
        System.out.println(result.getTime());

        mdsAlgo = new ArbitraryGraph();
        System.out.println("new ArbitraryGraph()()");
        result = mdsAlgo.run(graph);
        System.out.println(result.getMds());
        System.out.println(result.getTime());

        mdsAlgo = new ImprovedSetCover();
        System.out.println("new improvedSetCover)");
        result = mdsAlgo.run(graph);
        System.out.println(result.getMds());
        System.out.println(result.getTime());



    }

    @Test@BeforeEach@Disabled
   public  void testParsingRegularsGraphs(){

       String pathToRegularGraphsFile = "C:\\Users\\bouali\\Desktop\\Thesis2018-2019\\graphes\\graphes\\HouseOfGraphes\\graph_3357_34_10.lst";
       FileParser fileParser = new FileParser();
       graph = fileParser.create3RegularGraphs(new File(pathToRegularGraphsFile));
       Assertions.assertNotNull(graph);

   }

    @Test
    public void testPerformanceComparisonFive(){

            mdsAlgorithm  mdsAlgo = new ImprovedSetCover();
            System.out.println("new improvedSetCover)");
            System.out.println(graph);
            Result result = mdsAlgo.run(graph);
            System.out.println("mds size "+result.getMds());
            System.out.println("mds time "+result.getTime());

            mdsAlgo = new ArbitraryGraph();
            System.out.println("new ArbitraryGraph()()");
            result = mdsAlgo.run(graph);
            System.out.println(result.getMds());
            System.out.println(result.getTime());

    }

    @Test
    public void testPerformanceComparisonRegularGraphs(){

        mdsAlgorithm mdsAlgo;
        Result result;
        double totalTimeAtmostThree=0, totalTimeGerneralGraph = 0;
        double totalMdsSzeAtmostThree=0, totalMdsSzeGerneralGraph = 0;


            mdsAlgo = new AtMostDegreeThree();
            //System.out.println("AtMostDegreeThree()");
            result = mdsAlgo.run(graph);
           // System.out.println(result.getMds());
           // System.out.println(result.getTime());
            totalTimeAtmostThree+=result.getTime();
            totalMdsSzeAtmostThree+=result.getMds().size();
            System.out.println(result.getMds().size());

        System.out.println("totalTimeAtmostThree "+totalTimeAtmostThree);
        System.out.println("totalMdsAtmostThree "+totalMdsSzeAtmostThree);

            mdsAlgo = new ArbitraryGraph();
            //System.out.println("new ArbitraryGraph()()");
            result = mdsAlgo.run(graph);
            //System.out.println(result.getMds());
           // System.out.println(result.getTime());
            totalTimeGerneralGraph+=result.getTime();
            totalMdsSzeGerneralGraph+=result.getMds().size();
            System.out.println(result.getMds().size());
        System.out.println("totalTimeGerneralGraph "+totalTimeGerneralGraph);
        System.out.println("totalMdsSizeGerneralGraph "+totalMdsSzeGerneralGraph);
    }

    @Test
    public void testPerformanceComparisonFour(){
        System.out.println("testPerformanceComparisonFour");
        Graph graph = new Graph(38);
        IntStream.range(0,38)
                .forEach(i->graph.addVertex(i));
        Assertions.assertEquals(38,graph.size());

        graph.addEdge(0,1);
        graph.addEdge(0,2);
        graph.addEdge(0,3);
        graph.addEdge(1,3);
        graph.addEdge(1,4);
        graph.addEdge(2,3);
        graph.addEdge(2,4);
        graph.addEdge(4,5);



        graph.addEdge(10,7);
        graph.addEdge(10,9);
        graph.addEdge(10,8);
        graph.addEdge(7,8);
        graph.addEdge(7,6);
        graph.addEdge(9,8);
        graph.addEdge(9,6);
        graph.addEdge(6,5);

        graph.addEdge(5,11);
        graph.addEdge(11,12);
        graph.addEdge(12,13);
        graph.addEdge(12,14);
        graph.addEdge(13,15);
        graph.addEdge(13,16);
        graph.addEdge(14,17);
        graph.addEdge(14,18);
        graph.addEdge(15,16);
        graph.addEdge(16,17);
        graph.addEdge(17,18);

        graph.addEdge(11,19);
        graph.addEdge(19,20);
        graph.addEdge(19,21);
        graph.addEdge(20,22);
        graph.addEdge(20,23);
        graph.addEdge(21,24);
        graph.addEdge(21,25);
        graph.addEdge(18,22);
        graph.addEdge(22,23);
        graph.addEdge(23,24);
        graph.addEdge(24,25);

        graph.addEdge(15,26);
        graph.addEdge(25,27);

        graph.addEdge(26,27);
        graph.addEdge(26,28);
        graph.addEdge(29,28);
        graph.addEdge(28,30);
        graph.addEdge(29,31);
        graph.addEdge(29,32);
        graph.addEdge(30,31);
        graph.addEdge(30,32);
        graph.addEdge(32,31);


        graph.addEdge(27,33);
        graph.addEdge(34,33);
        graph.addEdge(33,36);
        graph.addEdge(34,35);
        graph.addEdge(34,37);
        graph.addEdge(36,35);
        graph.addEdge(36,37);
        graph.addEdge(37,35);


        mdsAlgorithm mdsAlgo = new AtMostDegreeThree();
        System.out.println("AtMostDegreeThree()");
        Result result = mdsAlgo.run(graph);
        System.out.println(result.getMds());
        System.out.println(result.getTime());

        mdsAlgo = new ImprovedSetCover();
        System.out.println("new improvedSetCover)");
        result = mdsAlgo.run(graph);
        System.out.println(result.getMds());
        System.out.println(result.getTime());

        mdsAlgo = new TrivialSetCover();
        System.out.println("new trivial)");
        result = mdsAlgo.run(graph);
        System.out.println(result.getMds());
        System.out.println(result.getTime());

        mdsAlgo = new ArbitraryGraph();
        System.out.println("new ArbitraryGraph()()");
        result = mdsAlgo.run(graph);
        System.out.println(result.getMds());
        System.out.println(result.getTime());

    }

    @Test
    public void testSharingNeighbors(){
        Graph g = new Graph(6);
        g.addVertices(0,1,2,3,4,5);
        g.addEdge(0,1);
        g.addEdge(0,2);
        g.addEdge(0,3);
        g.addEdge(3,4);
        g.addEdge(3,5);
        System.out.println(g);
        AtMostDegreeThree atMostDegreeThree = new AtMostDegreeThree();
        boolean shareneighbors = atMostDegreeThree.openNeigborintersection(g.getClosedNeighbors(0),g.getClosedNeighbors(3));
        Assertions.assertEquals(false,shareneighbors);

        g.addEdge(1,2);
        g.addEdge(2,3);
        shareneighbors = atMostDegreeThree.openNeigborintersection(g.getClosedNeighbors(1),g.getClosedNeighbors(2));
        Assertions.assertEquals(true,shareneighbors);

    }
}
