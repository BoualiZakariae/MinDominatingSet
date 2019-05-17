package umons.algorithm.dominatingset.util;

import umons.algorithm.dominatingset.exactalgorithm.ArbitraryGraph;
import umons.algorithm.dominatingset.exactalgorithm.MdsAlgorithm;
import umons.algorithm.dominatingset.graph.Graph;
import umons.algorithm.dominatingset.graph.Result;

import java.io.*;
import java.util.stream.IntStream;

/**
 * This class is responsible for parsing the files to a graph data structure format
 *
 * This class can parse 3 file format
 *
 * The first is the triangleUp file format
 * The second is the dimacs file format
 * The third is the houseOfGraphs file format
 *
 * and create as an output a Graph datastructure
 */
public class FileParser {


    /**
     * this method create a graph data structure from a housofgraph file data format.
     * the vertices begin with 1 index
     * @param file
     * @return
     */
    public static Graph createGraphsFromHouseOfGraphs( File file) {
        String [] graphName = file.getName()
                                  .replaceFirst("[.][^.]+$", "")//remove extension
                                  .split("_");
        int graphId = Integer.parseInt(graphName[1]) ;
        int numberOfVertices= Integer.parseInt(graphName[2]) ;
        int mdsSize = Integer.parseInt(graphName[3]) ;
        System.out.println(graphId+" "+numberOfVertices+" "+mdsSize);
        BufferedReader reader;
        Graph graph = new Graph(26);
        IntStream.range(1,numberOfVertices+1)
                .forEach(graph::addVertex);
        try {
           // String filestr= file.getName();
           // System.out.println(filestr);
            reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine())!=null && !line.equals(""))
                {
                    String[] array = line.split(" ");
                    int verticesCounter = array.length;
                    int vertexOne   = Integer.parseInt(array[0].substring(0,array[0].length()-1));
                    for(int i=1; i<verticesCounter; i++)
                    {
                        graph.addEdge(vertexOne,Integer.parseInt(array[i]));
                    }
               }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return graph;
    }




    /**
     *
     * @param file
     * @return
     * @throws IOException
     */
    public static Graph createGraphFromDimacsFormat( File file) {
        BufferedReader reader;
        Graph g = null ;
        try {
            reader = new BufferedReader(new FileReader(file));
            String line;
            do{
                line = reader.readLine();
            }while (line.charAt(0)!='p');
            int numberOfVertices = getGraphSize(line);
            g = new Graph(numberOfVertices);
            while ((line = reader.readLine()) != null) {
                String [] edge =  line.split(" ");
                addEdge(g,edge);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return g;
    }

    /**
     *
     * @param line
     * @return
     */
    private static int getGraphSize( String line ) {
        String [] firstLine =  line.split(" ");
        return Integer.parseInt(firstLine[2]);
    }


    /**
     *
     * @param g
     * @param line
     */
    public static void  addEdge(Graph g,String [] line){
        int u = Integer.parseInt(line[1]);
        int v = Integer.parseInt(line[2]);
        g.addVertex(u);
        g.addVertex(v);
        g.addEdge(u, v);
    }
    /**
     *
     * @param n
     * @param matrix
     * @return
     */
    public static Graph createGraph( int n, String matrix ) {
       // System.out.println(n);
        Graph graph = new Graph(n);
        for (int i=0;i<n;i++)
        {
            graph.addVertex(i);
        }
        createEdges(graph, matrix);
        return graph;
    }



    /**
     * creating the edges of the Graph g
     *
     * @param g
     * @param matrix
     */
    private static void createEdges( Graph g, String matrix ) {
        char[] matrixArray = matrix.toCharArray();
        int u = 0, j = 1, k = 0;
        for (int len = g.size() - 1; len > 0; len--) {
            for (int i = 0; i < len; ++i) {
                int edge = matrixArray[i + k] - '0';
                if (edge == 1) {
                    g.addEdge(u, i + j);
                }
            }
            k += len;
            u++;
            j++;
        }
    }


}
