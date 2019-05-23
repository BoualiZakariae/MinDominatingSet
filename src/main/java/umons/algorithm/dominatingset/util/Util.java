package umons.algorithm.dominatingset.util;

import umons.algorithm.dominatingset.graph.Graph;

import java.io.*;
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
                      .filter(Objects::nonNull)
                      .min(Comparator.comparingInt(Set::size))
                      .get();
    }



    /**
     *
     * @param arr
     * @param r
     * @return
     */
    public static ArrayList<int[]> getCombinations(int arr[], int r) {
        ArrayList<int[]> lists = new ArrayList<>();
        lists = combinations(arr, r, 0, new int[r],lists);
        return lists;

    }

    /**
     *
     * @param arr
     * @param len
     * @param startPosition
     * @param result
     * @param subsets
     * @return
     */
    private static ArrayList<int[]> combinations( int[] arr, int len, int startPosition, int[] result, ArrayList<int[]> subsets ){
        if (len == 0){
            subsets.add(Arrays.copyOf(result,result.length));
            return subsets;
        }
        for (int i = startPosition; i <= arr.length-len; i++){
            result[result.length - len] = arr[i];
            combinations(arr, len-1, i+1, result, subsets);
        }
        return subsets;
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
        if (C1 == null) {
            return C2;
        }
        if (C2 == null) {
            return C1;
        }
        if (C1.size() < C2.size()) {
            return C1;
        } else {
            return C2;
        }
    }


    /**
     * create a graph G
     * @param u the element of u will represents the vertices of the graph G
     * @param s every set S in in that have size 2 will be represented as an edge in G
     * @return
     */
    public static UndirectedGraph createTheGraph( List<Integer> u, List<List<Integer>> s) {
        UndirectedGraph G = new UndirectedGraph();
        u.forEach(G::addNode);//creating vertices
       // System.out.println("U "+u);
        for (List<Integer> S : s) {
            if (S.size() == 2)
                G.addEdge(S.get(0), S.get(1));
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
            List<Integer> newS = new ArrayList<>();
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




    /**
     * helper method to return n elements from the Set s.
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
            dominatedVertices.addAll(neighborsOfx);
        }
        return setMinus(X,dominatedVertices);
    }


}
