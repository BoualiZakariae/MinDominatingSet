package umons.algorithm.dominatingset.toDelete;

import umons.algorithm.dominatingset.graph.Graph;
import umons.algorithm.dominatingset.toDelete.HeuristicsMain;
import umons.algorithm.dominatingset.util.FileParser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class DimacFormatReader {


    public static final String results = "C:\\Users\\bouali\\Desktop\\Thesis2018-2019\\graphes\\graphes\\result.txt";
    private static final String directoryPathFile = "C:\\Users\\bouali\\Desktop\\Thesis2018-2019\\graphes\\graphes\\instances";

    private static Graph createGraph( Path path ) {
        System.out.println(path);
        BufferedReader reader;
        Graph g = null ;
        int numberOfLines=0;
        try {
            reader = new BufferedReader(new FileReader(path.toString()));
            String line ;
            int numberOfVertices,numberOfEdegs;
            //String [] edge;
            while ((line = reader.readLine()) != null) {
                numberOfLines++;
                //p edge 138 986
                //e 1 36
                if(line.charAt(0)=='p'){
                    String [] infos =  line.split(" ");
                    numberOfVertices = Integer.parseInt(infos[2]);
                    numberOfEdegs    = Integer.parseInt(infos[3]);
                    g = new Graph(numberOfVertices,numberOfEdegs);
                }
                else if(line.charAt(0)=='e'){
                    String [] edge =  line.split(" ");
                    FileParser.addEdge(g,edge);
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        int i=0;
        for (Integer v:g.getGraphVertices()) {
           // System.out.println(v);
            i++;
        }
        System.out.println("taille "+i);

        System.out.println("N: "+g.getGraphVertices().size()+" M: "+g.getNumberOfEdges());
        //System.out.println(g);
       return g;
    }


    public static void main( String[] args ) throws IOException{
        try (Stream<Path> paths = Files.walk(Paths.get(directoryPathFile))) {
            paths.filter(Files::isRegularFile)
                 .forEach(path -> {
                                  Graph g = createGraph(path);
                                  g.setNumberOfVertices(g.getGraphVertices().size());
                                  HeuristicsMain.heuristicsrun(g);
                                  }
                         );
        }
    }

}
