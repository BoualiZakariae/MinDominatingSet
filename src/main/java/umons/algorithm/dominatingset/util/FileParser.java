package umons.algorithm.dominatingset.util;

import umons.algorithm.dominatingset.exactalgorithm.ArbitraryGraph;
import umons.algorithm.dominatingset.exactalgorithm.mdsAlgorithm;
import umons.algorithm.dominatingset.graph.Graph;
import umons.algorithm.dominatingset.graph.Result;

import java.io.*;

/**
 * This class is responsible for parsing the files to a graph data structure format
 *
 * This class can parse 3 text file format
 *
 * The first is the iso format
 * The third for heuristics algortithms
 *
 * and create as an output a Graph datastructure
 */
public class FileParser {


    /**
     *
     * @param path
     * @return
     * @throws IOException
     */
    public static Graph createGraphFromDimacsFormat( String path) throws IOException {
        return createGraphFromDimacsFormat(new File(path));
    }

    /**
     *
     * @param file
     * @return
     * @throws IOException
     */
    public static Graph createGraphFromDimacsFormat( File file) throws IOException {
        BufferedReader reader;
        Graph g = null ;
        try {
            reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();
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
     *
     * @param line1
     * @param line2
     * @return
     */
    public static Graph createGraph( String line1 , String line2) {
        String[] result1 = line1.split("\\s");
        int n = Integer.parseInt(result1[0]);
        int edgesNumber = Integer.parseInt(result1[1]);
        Graph graph = new Graph(n);
        for (int i=0;i<n;i++)
        {
            graph.addVertex(i);
        }
        if (line2.isEmpty() || line2==null)
            return graph;
        String[] result2 = line2.split("\\s\\s");
        int counter =0;
        for (String edge:result2 ) {
            String[] vertices = edge.split("\\s");
            int vertexOne = Integer.parseInt(vertices[0]);
            int vertexTwo = Integer.parseInt(vertices[1]);
            graph.addEdge(vertexOne,vertexTwo);
        }
        return graph;
    }

    /**
     * creating the edges of the Graph g
     *
     * @param g
     * @param matrix
     */
    public static void createEdges( Graph g, String matrix) {
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

    /**
     *
     * @param g
     * @return
     */
    public static String createTriangleUpFormat(Graph g){
        StringBuilder sb = new StringBuilder();
        int size = g.size();
        for (int i=0;i< size-1;i++)
        {
            for (int j=i+1;j<= size-1;j++)
            {
                if (g.getAdj().get(i).contains(j))
                    sb.append("1");
                else
                    sb.append("0");
            }
        }
        return  sb.toString();
    }


    public static String pathTomax3degreeFile = "C:\\Users\\bouali\\Desktop\\Thesis2018-2019\\graphes\\graphes\\max3degree.txt";
    public static String maxDegreeFile = "C:\\Users\\bouali\\Desktop\\Thesis2018-2019\\graphes\\graphes\\triangle.txt";


    public static void main( String[] args ) throws IOException {

        mdsAlgorithm atMostThreeDegreeAlgo = new ArbitraryGraph();
        BufferedReader reader = new BufferedReader(new FileReader(pathTomax3degreeFile));;
        BufferedWriter writer = new BufferedWriter(new FileWriter(maxDegreeFile));
        Graph g=null;
        String firstLine   =  reader.readLine();
        String secondtLine =  reader.readLine();
        while (firstLine != null) {
            g = FileParser.createGraph(firstLine,secondtLine);
            Result result = atMostThreeDegreeAlgo.run(g);
            double time = result.getTime();
            if (time == -1){
                System.out.println("Bad result");
                return;//if we hav bad mds size we stop
            }

            StringBuilder line = new StringBuilder("");
            line.append(g.size())
                .append(" ")
                .append(createTriangleUpFormat(g))
                .append(" ");
            int mdsSize = (int) result.getMds().size();
            line.append(mdsSize).append("\n");

            System.out.println(line);
            writer.write(line.toString());
            firstLine = reader.readLine();
            secondtLine = reader.readLine();
        }
        reader.close();
        System.out.println();

        System.out.println("fin");

    }


}
