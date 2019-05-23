package umons.algorithm.dominatingset.toDelete;

import umons.algorithm.dominatingset.exactalgorithm.ArbitraryGraph;
import umons.algorithm.dominatingset.exactalgorithm.MdsAlgorithm;
import umons.algorithm.dominatingset.graph.Graph;
import umons.algorithm.dominatingset.graph.Result;
import umons.algorithm.dominatingset.util.FileParser;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestUtilities {

    public static void main( String[] args ) {

        String [] words ={"0","00","000","0000","00000","000000","0000000"} ;
        for (String str :words) {
            System.out.println(transform(str));
        }
    }
    private static String  transform( String word ) {
        int min =  getMinNumber(word);
        StringBuilder sb = new StringBuilder(word);
        //starting from char at index 1 replace each third '0' with '1'
        //and substract 1 for each replaced char from min
        for(int i = 1; i< word.length(); i = i+3){
            sb.setCharAt(i, '1');
            min--;
        }
        //if minimum replacement count not yet met replace last char
        if(min >0){
            sb.setCharAt(word.length()-1, '1');
        }
        return sb.toString();
    }

    private static int getMinNumber( String word ) {
        int min;
        if (word.length() % 3 == 0) {
            min = word.length() / 3;
        } else {
            min = (word.length() / 3) + 1;
        }


        return min;
    }



    private static String createTriangleUpFormat( Graph g ){
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


    public void createtringleupFilefromMAxDegreeThreefile() throws IOException {
        MdsAlgorithm atMostThreeDegreeAlgo = new ArbitraryGraph();
        String pathTomax3degreeFile = "C:\\Users\\bouali\\Desktop\\Thesis2018-2019\\graphes\\graphes\\max3degree.txt";
        BufferedReader reader = new BufferedReader(new FileReader(pathTomax3degreeFile));
        String maxDegreeFile = "C:\\Users\\bouali\\Desktop\\Thesis2018-2019\\graphes\\graphes\\triangle.txt";
        BufferedWriter writer = new BufferedWriter(new FileWriter(maxDegreeFile));
        Graph g=null;
        String firstLine   =  reader.readLine();
        String secondtLine =  reader.readLine();
        while (firstLine != null) {
            g = createGraph(firstLine,secondtLine);
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
            int mdsSize = result.getMds().size();
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

    /**
     *
     * @param line1
     * @param line2
     * @return
     */
    private static Graph createGraph( String line1, String line2 ) {
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


    static class Edge {
        Integer v;
        Integer u;

        Edge(Integer v,Integer u){
            this.v=v;
            this.u=u;
        }

        @Override
        public boolean equals( Object o ) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Edge edge = (Edge) o;
            return (Objects.equals(v, edge.v) && Objects.equals(u, edge.u))  ||
                    (Objects.equals(v, edge.u) && Objects.equals(u, edge.v))    ;
        }

        @Override
        public int hashCode() {
            return Objects.hash(v, u);
        }

        @Override
        public String toString() {
            return v+" "+u;
        }
    }

    private static void writeToFile( List<Graph> listOfGraphs ) throws IOException {

        BufferedWriter writer;
        String pathToNewFileFormat = "C:\\Users\\bouali\\Desktop\\graphes.txt";
        writer = new BufferedWriter(new FileWriter(pathToNewFileFormat));
        for (Graph g : listOfGraphs){
            storeGraph(writer, g);
        }
        writer.close();
        System.out.println("finish");
    }

    private static void storeGraph( BufferedWriter writer, Graph g ) throws IOException {
        Set<Edge> edges = new HashSet<>();
        writer.write(g.size()+" ");
        for (Map.Entry<Integer,Set<Integer>> pair:g.getAdj().entrySet() ) {
            int vertex = pair.getKey();


            for (Integer neighbor : pair.getValue()) {
                edges.add(new Edge(vertex,neighbor));
            }
        }
        writer.write(edges.size()+"\n");
        if (edges.size()>0)
            for (Edge edge:edges ) {
                writer.write(edge.toString()+"  ");
            }
        writer.write("\n");
        edges=null;
        System.gc();
    }



    /*
     * Testing combination utility
     *
     * @param args
     **/
    public static void main2(String[] args) throws IOException {

        Graph g = new Graph(3);
        g.addVertex(1);
        g.addVertex(2);
        g.addVertex(3);
        g.addVertex(3);
        g.addEdge(1,2);
        g.addEdge(2,3);


        Graph g2 = new Graph(3);
        g2.addVertex(1);
        g2.addVertex(2);
        g2.addVertex(3);
        g2.addVertex(3);
        g2.addEdge(1,2);
        g2.addEdge(2,3);

        writeToFile(Stream.of(g,g2).collect(Collectors.toCollection(ArrayList::new)));




    }



}
