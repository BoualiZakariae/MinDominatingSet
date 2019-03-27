package umons.algorithm.dominatingset.util;

import umons.algorithm.dominatingset.graph.Graph;

import java.io.*;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class Util {

   // private static File FILE_Path = new File("C:\\Users\\bouali\\Desktop\\test\\XPRIME.txt");
     private static File FILE_Path = new File("C:\\Users\\bouali\\Desktop\\instances");

    /**
     *
     * @param sets
     * @return the list that has the min size
     */
    public static Set<Integer> minOfTheSet( Set<Integer>... sets) {
        return  Arrays.stream(sets)
                      .filter(set->set!=null)
                      .min(Comparator.comparingInt(Set::size))
                      .get();
    }



    /**
     *
     * @return the list that has the min size
     */
    public static Set<Integer> minOf(Set<Integer> set1, Set<Integer> set2) {
        if (set1.size()>set2.size())
            return set2;
        return set1;

    }


    /**
     *
     * https://www.geeksforgeeks.org/print-all-possible-combinations-of-r-elements-in-a-given-array-of-size-n/
     *   arr[]  ---> Input Array
     *   data[] ---> Temporary array to store current combination
     *   start & end ---> Staring and Ending indexes in arr[]
     *   index  ---> Current index in data[]
     *    r ---> Size of a combination to be printed *
     * @param arr
     * @param data
     * @param start
     * @param end
     * @param index
     * @param r
     */
    public static void combination(int arr[], int data[], int start,
            int end, int index, int r, ArrayList<int[]> al) {
        if (index == r) {
            int[] new_combo = new int[r];
            for (int j = 0; j < r; j++) {
                new_combo[j] = data[j];
            }
            al.add(new_combo);
            return;
        }
        for (int i = start; i <= end && end - i + 1 >= r - index; i++) {
            data[index] = arr[i];
            combination(arr, data, i + 1, end, index + 1, r, al);
        }
    }


    /**
     *  //arr from 0 to N
     *    //n equal to N
     *    //r subset size
     * @param arr
     * @param n
     * @param r
     * @return
     */
    public static ArrayList<int[]> getCombinations(int arr[], int n, int r) {
        int data[] = new int[r];
        ArrayList<int[]> al = new ArrayList<>();
        combination(arr, data, 0, n - 1, 0, r, al);
        return al;
    }



    /**
     *
     * @param g
     * @param y1
     * @param y2
     * @return  true if y1 and y2 are adjacent, false otherwise
     */
    public static boolean areAdjacent(Graph g, int y1, int y2) {
        return g.getAdj().get(y1).contains(y2);
    }

    /**
     *
     * @param g
     * @param c
     * @return true if the component is cycle component, false otherwise
     * knowing that c has no vertex with degree greater than 2
     */
    public static boolean isCycle(Graph g, List<Integer> c) {
        for (Integer v : c) {
            if (g.getAdj().get(v).size() < 2) {
                return false;
            }
        }
        return true;
    }


    /**
     *
     * @param component
     * @return the size of the ArbitraryGraph of the component
     */
    public static int getMdsSize(List<Integer> component) {
        int mdsSize;
        if (component.size() % 3 == 0) {
            mdsSize = component.size() / 3;
        } else {
            mdsSize = (component.size() / 3) + 1;
        }
        return mdsSize;
    }

    ///////////////////////////////////////////////////////////////
    ///// Helper Method for The MSCover Problem///////////////////
    //////////////////////////////////////////////////////////////

    /**
     * 17.22 min with reduce
     * 17.58              boxed
     * 12.85 min with indexOf
     * 13.89 min for loop
     * 9min using the three reduction rules
     * @param s
     * @return return the index of the subset that has the maximum cardinality
     */
        public static int getMaximumSet(List<List<Integer>> s) {
             return s.indexOf(Collections.max(s,Comparator.comparingInt(List::size)));
       }

    /**
     *
     * @param P
     * @param Q
     * @return true if Q Contains all element of P
     */
    public static boolean isSubset(Collection<Integer> P, Collection<Integer> Q) {
        return Q.containsAll(P);
    }


    /**
     *
     * @param U
     * @param S
     * @return  the list of elements that are in U and not in S
     */
    public static List<Integer> setMinus(List<Integer> U, List<Integer> S) {
        return U.stream()
                .filter(e->!S.contains(e))
                .collect(Collectors.toCollection(ArrayList::new));
    }



    /**
     *
     *
     * @return  the set of elements that are in s1 and not in S2
     */
    public static Set<Integer> setMinus(Set<Integer> s1, Set<Integer> s2) {
        return s1.stream()
                .filter(e->!s2.contains(e))
                .collect(Collectors.toCollection(HashSet::new));
    }





    /**
     *
     * @param C1
     * @param C2
     * @return the min size list
     */
    public static List<Integer> min(List<Integer> C1, List<Integer> C2) {
        if (C1 == null && C2 == null) {
            return null;
        }
        if (C1 == null && C2 != null) {
            return C2;
        }
        if (C2 == null && C1 != null) {
            return C1;
        }
        if (C1.size() < C2.size()) {
            return C1;
        } else {
            return C2;
        }
    }

    //Method to read The test File
    public static Scanner readFile() {
        File file = null;
        Scanner in = null;
        try {
            file = FILE_Path;

            in = new Scanner(new FileReader(file));
        } catch (FileNotFoundException e) {
            System.out.println(e.toString());
        }
        return in;

    }



    public static Graph createGraph(Path path){
        return null;
    }

    /**
     * create a graph G
     * @param u the element of u will represents the vertices of the graph G
     * @param s every set S in in that have size 2 will be represented as an edge in G
     * @return
     */
    public static UndirectedGraph createTheGraph( List<Integer> u, List<List<Integer>> s) {
        UndirectedGraph G = new UndirectedGraph();
        u.forEach(i->G.addNode(i));//creating vertices
       // System.out.println("U "+u);
        for (int i=0; i<s.size(); i++){
            List<Integer> S = s.get(i);
            if (S.size() == 2)
                G.addEdge(S.get(0),S.get(1));
        }
        return G;
    }

    /**
     *
     * @param MM
     * @param s
     * @return
     */
    public static List<Integer> getSetCover( UndirectedGraph MM , List<List<Integer>>  s) {
        List<Integer> setCover= new ArrayList<>();
        Iterator iterator = MM.iterator();
        while (iterator.hasNext()){
            Integer vertex = (Integer)iterator.next();
            Set<Integer> set = MM.edgesFrom(vertex);
            List<Integer> newS = new ArrayList<Integer>();
            if (!set.isEmpty()){
                Integer neighbour;
                neighbour= set.stream().findFirst().get();
                newS.add(neighbour);
            }
            newS.add(vertex);
            int index= s.indexOf(newS);
            if(index!=-1)
                setCover.add(s.indexOf(newS));
        }
        return setCover;
    }




    /*
     * Testing combination utility
     *
     * @param args
     **/
    public static void main(String[] args) throws IOException {
       /* ArrayList<int[]> ar = getCombinations(new int[]{1,2,3,4}, 4, 4);
        for (int i = 0; i < ar.size(); i++) {
            int[] list = ar.get(i);
            for (int l :list) {
                System.out.print(l+" ");
            }
            System.out.println();*/

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



    public static String pathToNewFileFormat = "C:\\Users\\bouali\\Desktop\\graphes.txt";

    public static void writeToFile( List<Graph> listOfGraphs ) throws IOException {

        BufferedWriter writer;
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

    /**
     * return @n elements from the Set s
     *
     *
     * @param s  set of vertices
     * @param n  an integer
     * @return an array containing n vertices from the Set s
     */
    public static int[] getNodes( Set<Integer> s, int n){
        int[] neighbors = new int[n];
        Iterator<Integer> it =s.iterator();
        int counter =0;
        while (counter<n){
            neighbors[counter] = it.next();
            counter++;
        }
        return neighbors;
    }

    /**
     * Return the set of vertices that are not dominated by the {@code dominatingSet}
     *
     * @param graph         a {@link Graph} data structure
     * @param dominatingSet the dominated set
     * @param X             set of vertices that should be dominated
     * @return              the elements in X that are not dominated by {@code dominatingSet}
     */
    public static Set<Integer> remainingSet(Graph graph, Set<Integer> dominatingSet, Set<Integer> X) {
        Set<Integer> dominatedVertices = new HashSet<>();
        for (int x : dominatingSet) {
            Set<Integer> neighborsOfx = graph.getClosedNeighbors(x);
            for (Integer e : neighborsOfx) {
                dominatedVertices.add(e);
            }
        }
        Set<Integer> remainingSet = setMinus(X,dominatedVertices);
        return remainingSet;
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
}
