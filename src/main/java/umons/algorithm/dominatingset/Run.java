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


    /**
     *
     * @param args
     *
     * Four parameters can be given to the program: -algo, -path, -type, -mds.
     * three parameters are mandatory: -algo, -path, -type.
     * one parameter is optional -mds.(only when running the Genetic algo)
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
     * - path "filepath/file.txt" ------> to pass the location of the file.txt as a path value to the program.
     *
     *
     *
     *      - type
     * - type 1 ------> means that the file passed to the program is of type triangleup
     * - type 2 ------> means that the file passed to the program is of type dimacs
     * - type 3 ------> means that the file passed to the program is of type HouseofGraphs
     *
     * Only the triangleup format file could contains many graphs.
     * The two others file format should contains only one graph per file.
     *
     *
     * Please refer to /reseources/info.txt to understand the 3 type file type format(triangleup, dimacs, HousOfGraphs)
     * This program can run any implemented algorithms on any supported graphs file.
     *
     *
     *      - mds
     *  When you choose to run the genetic Algorithm on a (dimacs file format or a houseofgraphs),
     * it is possible to pass the known minimum dominating set size as a parametre by :
     *
     * - mds s where s is ant int value representing the known minimum dominating set.
     *
     * So the Genetic algorithm can stop running after reaching this optimal value.
     *
     *
     * Example :
     *
     * mvn run -algo 5 -path c:Windows\Desktop\file.txt -type 2
     * this command line run the greedy algorithm on the dimacs file
     *
     *
     */

    public static void main( String[] args ) {
        CommandLine cl = getCommandLineArguments(args);
        if (cl == null){
            System.out.println("no arguments passed to the program! please refer to the program instruction");
            return;
        }
        String algo = null, path, type = "";
        algo = cl.getOptionValue("algo");//the algorithm type
        path = cl.getOptionValue("path");//the path to the graph file
        type = cl.getOptionValue("type");//the type of the graph file passed to the program
        int mdsSize = -1;
        MdsAlgorithm mdsAlgo = null;
        GeneticAlgorithm geneticAlgo = null;
        System.out.println("The chosen algorithm : "+ Algos.values()[0]);
        System.out.println("The file path "+ path + " and of type "+type);
        switch(algo){/*algorithm instantiation based on the algo type*/
            case "1" :   mdsAlgo = new ArbitraryGraph();
                         break;
            case "2":    mdsAlgo = new AtMostDegreeThree();
                         break;
            case "3":    mdsAlgo = new TrivialSetCover();
                         break;
            case "4":    mdsAlgo = new ImprovedSetCover();
                         break;
            case "5":    mdsAlgo = new Greedy();
                         break;
            case "6":    mdsAlgo = new GreedyRev();
                         break;
            case "7":    mdsAlgo = new GreedyRandom();
                         break;
            case "8":    geneticAlgo = new GeneticAlgoImplOne();
                         break;
            case "9":    geneticAlgo = new GeneticAlgoImplTwo();
        }

        if(geneticAlgo != null)
        {
            System.out.println("geneticAlgo != null");
           if (type.equals("1"))//geneticAlgo+triangleUp file format
               runGenticAlgoOnTriangleUp(geneticAlgo, path);
           else if(type.equals("2"))//geneticAlgo+dimacs file format
               runGeneticAlgoOnDimacs(geneticAlgo,path,Integer.parseInt(cl.getOptionValue("mds")));
           else if (type.equals("3"))//geneticAlgo+housofgraphs file format
               runGeneticAlgoOnHouseOfGraphs(geneticAlgo,path,Integer.parseInt(cl.getOptionValue("mds")));

        }
        else{//not a genetic algorithm
              switch(type){
                  case "1" : runAlgoOnTriangleUp(mdsAlgo, path);
                             break;
                  case "2" : runAlgoOnDimacs(mdsAlgo, path);
                             break;
                  case "3" : runAlgoOnHouseOfGraphs(mdsAlgo, path);
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
        options.addOption("algo", true, "run the specic algo");
        options.addOption("path", true, "specific path");
        options.addOption("type", true, "file format type");
        options.addOption("mds", true, "minimum dominating set size");

        CommandLine cl = null;
        CommandLineParser commandLineParser = new DefaultParser();
        try {
             cl = commandLineParser.parse(options,args);
        } catch (ParseException e) {
            System.out.println("please revieuw the command line program instruction");
            e.getCause();
        }
        if(!cl.hasOption("algo") || !cl.hasOption("path") || !cl.hasOption("type"))
        {
            System.out.println("the algorithm parameter type and the path are mandatory");
            return null;
        }
        return cl;
    }

    /**
     * This method run the exact algorithm on the tringleup file format.
     *
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
              //  System.out.println(mds.getMds().size());
                line = reader.readLine();
            }
            System.out.println("incorrect result "+incorrect);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println();
    }

    /**
     *
     * @param algo
     * @param path
     */
    public static void runGenticAlgoOnTriangleUp(GeneticAlgorithm algo, String path){
        System.out.println("here");
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
                line = reader.readLine();
            }
            System.out.println("incorrect result "+incorrect);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println();
    }


    /**
     *
     * @param algo
     * @param path
     */
    public static void runAlgoOnDimacs( MdsAlgorithm algo, String path){
        Graph graph = getGraphFromDimacsFileFormat(path);
        Set<Integer> mds = algo.run(graph).getMds();
    }


    /**
     *
     * @param algo
     * @param path
     */
    public static void runAlgoOnHouseOfGraphs( MdsAlgorithm algo, String path){
        Graph graph = getGraphFromHouseOfGraphsFileFormat(path);
        Set<Integer> mds = algo.run(graph).getMds();
    }
    /**
     *
     * @param algo
     * @param path
     */
    public static void runGeneticAlgoOnDimacs(GeneticAlgorithm algo, String path, int mdsSize){
        Graph graph = getGraphFromDimacsFileFormat(path);
        Set<Integer> mds = algo.run(graph, mdsSize).getMds();
    }



    /**
     *
     * @param algo
     * @param path
     */
    public static void runGeneticAlgoOnHouseOfGraphs(GeneticAlgorithm algo, String path, int mdsSize){
        Graph graph = getGraphFromHouseOfGraphsFileFormat(path);
        Set<Integer> mds = algo.run(graph, -1).getMds();
    }


    /**
     *
     * @param path
     * @return
     */
    private static Graph getGraphFromDimacsFileFormat( String path ) {
        File file = new File(path);
        return FileParser.createGraphFromDimacsFormat(file);
    }


    /**
     *
     * @param path
     * @return
     */
    private static Graph getGraphFromHouseOfGraphsFileFormat( String path ) {
        File file = new File(path);
        return FileParser.createGraphsFromHouseOfGraphs(new File(path));
    }

}
