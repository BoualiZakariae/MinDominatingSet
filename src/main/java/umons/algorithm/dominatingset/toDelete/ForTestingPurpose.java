package umons.algorithm.dominatingset.toDelete;

import umons.algorithm.dominatingset.graph.Graph;
import umons.algorithm.dominatingset.heuristics.HeuristicsMain;
import umons.algorithm.dominatingset.util.FileParser;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class ForTestingPurpose {

    public static StringBuffer pathToHugeFile   = new StringBuffer("C:\\Users\\bouali\\Desktop\\Thesis2018-2019\\graphes\\graphes\\g");
    public static StringBuffer pathTomax3degree = new StringBuffer("C:\\Users\\bouali\\Desktop\\Thesis2018-2019\\graphes\\graphes\\max3degree.txt");
    public static final String directoryPathFile = "C:\\Users\\bouali\\Desktop\\Thesis2018-2019\\graphes\\graphes\\instances";


    public static void main( String[] args ) throws Exception {


        try (Stream<Path> paths = Files.walk(Paths.get(directoryPathFile))) {
            paths.filter(Files::isRegularFile)
                    .forEach(path -> createGraph(path));
        }


    }

    /**
     * to print graph info
     * N and M
     * @param path
     */
    private static void createGraph( Path path ) {
       // System.out.println(path);
        BufferedReader reader;
        Graph g = null ;
        int numberOfLines=0;
        try {
            File file = new File(path.toString());
            reader = new BufferedReader(new FileReader(file));
            String line ;
            int numberOfVertices,numberOfEdegs;
            while ((line = reader.readLine()) != null) {
                numberOfLines++;
                //p edge 138 986
                //e 1 36
                if(line.charAt(0)=='p'){
                    String [] infos =  line.split(" ");
                    numberOfVertices = Integer.parseInt(infos[2]);
                    numberOfEdegs    = Integer.parseInt(infos[3]);
                    System.out.println(file.getName()+" "+numberOfVertices+" "+numberOfEdegs);
                    break;
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public static void readFile( ) throws IOException {
        List<Graph> listOfGraphs = new ArrayList<>();
        BufferedReader reader;
        BufferedWriter writer;
        writer = new BufferedWriter(new FileWriter(pathTomax3degree.toString()));
        for (int i=2;i<=13;i++){
            pathToHugeFile.append(i).append(".txt");
            System.out.println(pathToHugeFile);
            reader = new BufferedReader(new FileReader(pathToHugeFile.toString()));
            String line = reader.readLine();
            while (line != null) {
                writer.write(line);
                writer.write("\n");
                line = reader.readLine();
            }
            reader.close();
            //pathToHugeFile=new StringBuffer(path);
        }
        System.out.println("finish");

    }
}
