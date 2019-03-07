package umons.algorithm.dominatingset.util;

import umons.algorithm.dominatingset.graph.Graph;

import java.util.*;

/**
 * This class implement the Depth first search algorithm.
 *
 * This algorithm allow to compute the connected components of a Graph instance.
 */
public class DFS {

    /**
     *
     */
    private List<List<Integer>> listOfCompenents = new ArrayList<>();
    private Map<Integer,Boolean> visited ;
    private int n;
    Map<Integer, Set<Integer>> adj;

    /**
     *
     * @param n
     */
    public DFS(int n){
        this.n = n;
        visited = new HashMap<>();
    }


    /**
     *
     * @param adjMap
     * @return
     */
    public List<List<Integer>> getTheListOfComponents(Map<Integer, Set<Integer>> adjMap){
        adj= adjMap;
        for (int key:adjMap.keySet() ) {
            visited.putIfAbsent(key,false);
        }
        List<Integer> component ;
        for (Integer key:adjMap.keySet()) {
            if (visited.get(key)==false){
                component = new ArrayList<>();
                DFS(key,component);
                listOfCompenents.add(component);
            }

        }

        return listOfCompenents;

    }

    /**
     *
     * @param v
     * @param component
     */
    public void DFS( int v ,List<Integer> component) {
        visited.put(v,true);
        component.add(v);
        for (int i: adj.get(v)) {
            if(!visited.get(i)){
                DFS(i,component);
            }
        }
    }


    /**
     * test the dfs algorithm
     * @param args
     */
    public static void main( String[] args ) {
        Graph g = new Graph(6);
        g.addVertex(0);
        g.addVertex(1);
        g.addVertex(2);
        g.addVertex(3);
        g.addVertex(4);
        g.addVertex(5);
        g.addEdge(0,1);
        g.addEdge(0,2);
        g.addEdge(1,2);
        g.addEdge(3,4);
        System.out.println(g);
        DFS dfs = new DFS(g.size());

        List<List<Integer>> listofComponants = dfs.getTheListOfComponents(g.getAdj());
        System.out.println(listofComponants);
    }
}
