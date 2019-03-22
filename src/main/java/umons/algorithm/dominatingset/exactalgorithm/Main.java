package umons.algorithm.dominatingset.exactalgorithm;


import umons.algorithm.dominatingset.graph.Graph;
import umons.algorithm.dominatingset.graph.Result;
import umons.algorithm.dominatingset.heuristics.GeneticAlgo.GeneticAlgorithm;
import umons.algorithm.dominatingset.heuristics.Greedy.Greedy;
import umons.algorithm.dominatingset.toDelete.Stats;
import umons.algorithm.dominatingset.util.FileParser;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 *
 *
 */
public class Main {

    //  public static String pathToHugeFile = "C:\\Users\\bouali\\Desktop\\XPRIME.txt";
    //  public static String pathToHugeFile = "C:\\Users\\bouali\\Desktop\\instances";
    // public static String pathToHugeFile = "C:\\Users\\bouali\\Desktop\\Thesis2018-2019\\graphes\\graphes\\3degreeVertexbug.txt";
     public static String pathToHugeFile = "C:\\Users\\bouali\\Desktop\\Thesis2018-2019\\graphes\\graphes\\maxDegreeThree.txt";
    //  public static String pathToHugeFile = "C:\\Users\\bouali\\Desktop\\Thesis2018-2019\\graphes\\graphes\\g24.txt";
    //  public static String pathToHugeFile = "C:\\Users\\bouali\\Desktop\\Thesis2018-2019\\graphes\\graphes\\badResult.txt";
    //  public static String pathToHugeFile = "C:\\Users\\bouali\\Desktop\\Thesis2018-2019\\graphes\\graphes\\two.txt";
    //public static String pathToHugeFile = "C:\\Users\\bouali\\Desktop\\Thesis2018-2019\\graphes\\graphes\\triangle.txt";

    /**
     *
     * @param args
     */
    public static void main( String[] args ) {
        int arg =2;
        mdsAlgorithm exactAlgo ;
        if (arg == 1){
            exactAlgo = new ArbitraryGraph();
            runExactAlgo(exactAlgo,pathToHugeFile);
        }else if (arg == 2){
            exactAlgo = new AtMostDegreeThree();
            runExactAlgo(exactAlgo,pathToHugeFile);
        }else if (arg == 3){
            exactAlgo = new TrivialSetCover();
            runExactAlgo(exactAlgo,pathToHugeFile);
        }else if (arg == 4){
            exactAlgo = new ImprovedSetCover();
            runExactAlgo(exactAlgo,pathToHugeFile);
        }
        else{
            runExactAlgo(new GeneticAlgorithm(),pathToHugeFile);
        }
    }
    /**
     * As the hugeFile contains 12.... graph instance
     * This method read the file line by line, for each line it create a graph instance and it call
     * the run method of the exact algo  to calculate the mds size.
     *
     * the arbitraryGraphAlgo.run(g) method return the time taken and the size found
     * the mds size is checked if it is the correct mds size of the graph instance
     */
    public static void runExactAlgo( mdsAlgorithm algo, String pathToHugeFile){
        int incorrect=0;
        BufferedReader reader;
        Graph g=null;
        try{
            Result mds;
            reader = new BufferedReader(new FileReader(pathToHugeFile));
            String line = reader.readLine();
            int count=0;
            while (line != null) {
                count++;
                String[] res = line.split("\\s");
                g = FileParser.createGraph(Integer.parseInt(res[0]),res[1]); //n 010101 mdssize
                mds = algo.run(g,Integer.parseInt(res[2]));
                if (mds.getMds().size() != Integer.parseInt(res[2])){
                    incorrect++;
                    System.out.println(g+" "+line+" AlgoFound"+mds.getMds().size());
                }
                Stats.addStatistic(g,mds.getMds().size(),mds.getTime());
                line = reader.readLine();
            }
            System.out.println("incorrect result "+incorrect);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stats.prinFullStats();
        System.out.println();
    }
}
