package umons.algorithm.dominatingset;


import org.apache.commons.cli.*;
import umons.algorithm.dominatingset.exactalgorithm.*;
import umons.algorithm.dominatingset.graph.Graph;
import umons.algorithm.dominatingset.graph.Result;
import umons.algorithm.dominatingset.heuristics.geneticAlgo.algos.GeneticAlgoImplOne;
import umons.algorithm.dominatingset.heuristics.geneticAlgo.algos.GeneticAlgoImplTwo;
import umons.algorithm.dominatingset.heuristics.geneticAlgo.algos.GeneticAlgorithm;
import umons.algorithm.dominatingset.heuristics.greedy.Greedy;
import umons.algorithm.dominatingset.heuristics.greedy.GreedyRandom;
import umons.algorithm.dominatingset.heuristics.greedy.GreedyRev;
import umons.algorithm.dominatingset.util.FileParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Set;

/**
 * This Class is responsible for running all the algorithms
 *
 *
 */
public class Run {

    /**
     *
     * @param args
     *
     * To run this program, you should pass as parameters:
     *  + the algorithm type.
     *  + the path of the file that contains the graph.
     *  + the type of the passed file.
     * As a result, the program will print the adjacency list of the graph, the mds size found  and the time taken in seconds.
     *
     *
     * Four parameters can be given to the program: -algo, -path, -type, -mds.
     * three parameters are mandatory: -algo, -path, -type.
     * one parameter is optional -mds (used only when running the Genetic algorithm)
     *
     *      - algo
     * - algo 1 ------> to run the Arbitrary algorithm
     * - algo 2 ------> to run the atmostDegree3 algorithm
     * - algo 3 ------> to run the trivialSetCover algorithm
     * - algo 4 ------> to run the improvedSetCover algorithm
     * - algo 5 ------> to run the greedy algorithm
     * - algo 6 ------> to run the GreedyReverse algorithm
     * - algo 7 ------> to run the GreedyRandom algorithm
     * - algo 8 ------> to run the GeneticAlgo1 algorithm
     * - algo 9 ------> to run the GeneticAlgo2 algorithm
     *
     *
     *      - path
     * - path  "filepath/file.txt" ------> to pass the location of the file.txt as a path value to the program.
     *
     *
     *
     *      - type
     * - type 1 ------> means that the file passed to the program is of type triangleup.
     * - type 2 ------> means that the file passed to the program is of type dimacs.
     * - type 3 ------> means that the file passed to the program is of type HouseofGraphs.
     *
     * Only the triangleup file format could contains many graphs.
     * The two others files format should contains only one graph per file.
     *
     * Please refer to src\main\resources to see the 3 supported files type format(triangleup, dimacs, HousOfGraphs)
     * This program can run any implemented algorithms on any supported graphs file.
     *
     *
     *      - mds
     *  When you choose to run the genetic Algorithm on a (dimacs file format or a houseofgraphs file format)
     *  it is possible to pass the known minimum dominating set size as a parameter by :
     *
     * - mds s ------> where s is an int value representing the known minimum dominating set.
     *                 the Genetic algorithm can stop running after reaching this optimal value.
     *
     *
     *
     * given mds.jar as an executable file, you can run it with as the following examples:
     *
     * Example_1 :
     * java -jar mds.jar -algo 5 -path "c:Windows\Desktop\file.col" -type 2
     * this command line run the greedy algorithm on the dimacs file located in "c:Windows\Desktop\file.col"
     *
     * Example_2 :
     * java -jar mds.jar -algo 8 -path "c:Windows\Desktop\filename.lst" -type 3 -mds 4
     * this command line run the genetic algorithm on the houseofgraphs file format located in c:Windows\Desktop\filename.lst
     * the program will stop running whenever finding a dominating set of size 4.
     *
     *
     */
    public static void main( String[] args ) {
        CommandLine cl = getCommandLineArguments(args);
        if (cl == null){
            System.out.println("no arguments passed to the program! please refer to the program instruction");
            return;
        }
        int algo;
        String path;
        int type;
        algo = Integer.parseInt(cl.getOptionValue("algo"));//the algorithm type
        path = cl.getOptionValue("path");//the path to the graph file
        type = Integer.parseInt(cl.getOptionValue("type"));//the type of the graph file passed to the program

        if (algo>9 || algo<1)
        {
            System.out.println("please review the command line program instruction");
            System.out.println("unknown algo type ");
            return;
        }

        if (type>3 || type<1)
        {
            System.out.println("please review the command line program instruction");
            System.out.println("unknown file format type ");
            return;
        }
        MdsAlgorithm mdsAlgo = null;
        GeneticAlgorithm geneticAlgo = null;
        System.out.println("The chosen algorithm : "+ Algos.values()[algo-1]);
        System.out.println("The chosen file path "+ path + " and of type "+FileType.values()[type-1]);
        switch(algo){/*algorithm instantiation based on the algo type*/
            case 1 :   mdsAlgo = new ArbitraryGraph();
                       System.out.println("ArbitraryGraph Algo");
                       break;
            case 2:    mdsAlgo = new AtMostDegreeThree();
                       System.out.println("AtMostDegreeThree  Algo");
                       break;
            case 3:    mdsAlgo = new TrivialSetCover();
                       System.out.println("TrivialSetCover  Algo");
                       break;
            case 4:    mdsAlgo = new ImprovedSetCover();
                       System.out.println("ImprovedSetCover  Algo");
                       break;
            case 5:    mdsAlgo = new Greedy();
                       System.out.println("Greedy  Algo");
                       break;
            case 6:    mdsAlgo = new GreedyRev();
                       System.out.println("GreedyRev  Algo");
                       break;
            case 7:    mdsAlgo = new GreedyRandom();
                       System.out.println("GreedyRandom  Algo");
                       break;
            case 8:    geneticAlgo = new GeneticAlgoImplOne();
                       System.out.println("GeneticAlgoImplOne  Algo");
                       break;
            case 9:    geneticAlgo = new GeneticAlgoImplTwo();
                       System.out.println("GeneticAlgoImplTwo  Algo");
                       break;
        }


        if(geneticAlgo != null)
        {
            System.out.println("geneticAlgo");
            int knowmdsSize=-1;
            if(cl.getOptionValue("mds")!=null)
                knowmdsSize =Integer.parseInt(cl.getOptionValue("mds"));


            switch (type){
                case 1 :  runGenticAlgoOnTriangleUp(geneticAlgo, path);//geneticAlgo+triangleUp file format
                          break;
                case 2 :  runGeneticAlgoOnDimacs(geneticAlgo,path,knowmdsSize);//geneticAlgo+dimacs file format
                          break;
                case 3 :  runGeneticAlgoOnHouseOfGraphs(geneticAlgo,path,knowmdsSize);//geneticAlgo+housofgraphs file format
                          break;
            }
        }
        else{//not a genetic algorithm
              switch(type){
                  case 1 : runAlgoOnTriangleUp(mdsAlgo, path);
                           break;
                  case 2 : runAlgoOnDimacs(mdsAlgo, path);
                           break;
                  case 3 : runAlgoOnHouseOfGraphs(mdsAlgo, path);
                           break;
              }
        }

    }

    /**
     * This method parse the command line arguments and return a CommandLine Object containing the argument passed to the program.
     *
     * @param args   the arguments passed to the program
     * @return       a CommandLine Object containing the argument
     */
    private static CommandLine getCommandLineArguments( String[] args ) {
        Options options = new Options();
        options.addOption("algo", true, "run the specific algo");
        options.addOption("path", true, "specific path");
        options.addOption("type", true, "file format type");
        options.addOption("mds", true, "minimum dominating set size");

        CommandLine cl = null;
        CommandLineParser commandLineParser = new DefaultParser();
        try {
             cl = commandLineParser.parse(options,args);
        } catch (ParseException e) {
            System.out.println("please review the command line program instruction");
            e.getCause();
        }
        if(!cl.hasOption("algo") || !cl.hasOption("path") || !cl.hasOption("type"))
        {
            System.out.println("the algorithm parameters type, algo and path are mandatory");
            System.out.println("please review the command line program instruction");
            return null;
        }
        return cl;
    }



    /**
     * This method run the given algorithm on the tringleup file format.
     * This method iterate over all graphs in the tringleup file and run
     * the given algorithm on the graph instance.
     *
     * @param algo
     * @param path
     */
    public static void runAlgoOnTriangleUp( MdsAlgorithm algo, String path){
        int incorrect=0;
        BufferedReader reader;
        Graph g=null;
        try{
            Result mds;
            reader = new BufferedReader(new FileReader(path));
            String line = reader.readLine();
            int count=0;
            while (line != null) {
                count++;
                String[] res = line.split("\\s");
                g = FileParser.createGraph(Integer.parseInt(res[0]),res[1]); //n 010101 mdssize
                mds = algo.run(g);
                if (mds.getMds().size() != Integer.parseInt(res[2])){
                    incorrect++;
                    System.out.println(g+" "+line+" AlgoFound "+mds.getMds().size());
                }
                System.out.println("Graph "+g+" mds size found "+ mds.getMds().size()+" in "+mds.getTime()/1000+" s" );
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println();
    }

    /**
     * This method run the GeneticAlgorithm algorithm on the tringleup file format.
     * This method iterate over all graphs in the tringleup file and run
     * the given algorithm on the graph instance.
     *
     * @param algo
     * @param path
     */
    public static void runGenticAlgoOnTriangleUp(GeneticAlgorithm algo, String path){
        int incorrect=0;
        BufferedReader reader;
        Graph g=null;
        try{
            Result mds;
            reader = new BufferedReader(new FileReader(path));
            String line = reader.readLine();
            int count=0;
            while (line != null) {
                count++;
                String[] res = line.split("\\s");
                g = FileParser.createGraph(Integer.parseInt(res[0]),res[1]); //n 010101 mdssize
                mds = algo.run(g,Integer.parseInt(res[2]) );
                if (mds.getMds().size() != Integer.parseInt(res[2])){
                    incorrect++;
                    System.out.println(g+" "+line+" AlgoFound "+mds.getMds().size());
                }
                System.out.println("Graph "+g+" mds size found "+ mds.getMds().size()+" in "+mds.getTime()/1000+" s" );
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println();
    }


    /**
     * Run the given algorithm on the given Dimacs file format.
     *
     * @param algo  the given algorithm
     * @param path  the path where the file is located.
     */
    public static void runAlgoOnDimacs( MdsAlgorithm algo, String path){
        Graph graph = getGraphFromDimacsFileFormat(path);
        Result result = algo.run(graph);
        Set<Integer> mds = result.getMds();
        System.out.println("mds size found "+ mds.size()+" in "+result.getTime()/1000+" s");
    }


    /**
     * Run the given algorithm on the given house of graph file format.
     *
     * @param algo  the given algorithm
     * @param path  the path where the file is located.
     */
    public static void runAlgoOnHouseOfGraphs( MdsAlgorithm algo, String path){
        Graph graph = getGraphFromHouseOfGraphsFileFormat(path);
        Result result = algo.run(graph);
        Set<Integer> mds = result.getMds();
        System.out.println("mds size found "+ mds.size()+" in "+result.getTime()/1000+" s");
    }
    /**
     *  Run the given genetic algorithm on the given dimacs file.
     *
     * @param algo  the given genetic algorithm
     * @param path  the path where the file is located.
     */
    public static void runGeneticAlgoOnDimacs(GeneticAlgorithm algo, String path, int mdsSize){
        Graph graph = getGraphFromDimacsFileFormat(path);
        Result result = algo.run(graph, mdsSize);
        Set<Integer> mds = result.getMds();
        System.out.println("mds size found "+ mds.size()+" in "+result.getTime()/1000+" s");
    }



    /**
     *  Run the given genetic algorithm on the given house of graphs file format.
     *
     * @param algo  the given genetic algorithm
     * @param path  the path where the file is located.
     */
    public static void runGeneticAlgoOnHouseOfGraphs(GeneticAlgorithm algo, String path, int mdsSize){
        Graph graph = getGraphFromHouseOfGraphsFileFormat(path);
        Result result = algo.run(graph, mdsSize);
        Set<Integer> mds = result.getMds();
        System.out.println("mds size found "+ mds.size()+" in "+result.getTime()/1000+" s");
    }


    /**
     * Create and return a graph data structure from a Dimacs file format.
     *
     * @param path  the path where the file is located.
     * @return  a graph data structure
     */
    private static Graph getGraphFromDimacsFileFormat( String path ) {
        File file = new File(path);
        return FileParser.createGraphFromDimacsFormat(file);

    }


    /**
     * Create and return a graph data structure from a house of graph file format.
     *
     * @param path  the path where the file is located.
     * @return  a graph data structure
     */
    private static Graph getGraphFromHouseOfGraphsFileFormat( String path ) {
        File file = new File(path);
        return FileParser.createGraphsFromHouseOfGraphs(file);
    }


}

enum Algos{ arbitrary ,
    atmostDegree3,
    trivialSetCover,
    improvedSetCover,
    Greedy,
    GreedyReverse,
    GreedyRandom,
    GeneticAlgo1,
    GeneticAlgo2;
}

enum FileType{ triangleup ,
    dimacs,
    houseofgraphs;
}
