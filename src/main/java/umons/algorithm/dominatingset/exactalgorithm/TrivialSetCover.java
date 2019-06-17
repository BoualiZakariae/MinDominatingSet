package umons.algorithm.dominatingset.exactalgorithm;

import umons.algorithm.dominatingset.graph.Graph;
import umons.algorithm.dominatingset.graph.Result;
import umons.algorithm.dominatingset.util.Stats;
import umons.algorithm.dominatingset.util.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * This Class implements the Algorithm: "A trivial set cover algorithm"
 *                  from the paper : Exact algorithms for dominating set.
 *                  Johan M.M. van Rooij, Hans L. Bodlaender
 *
 *
 * This algorithm represent the 'Algorithme trivial' from the thesis.
 *
 * @author bouali
 */
public class TrivialSetCover implements MdsAlgorithm {

    /**
     * This utility method help to restore the real set indices
     *
     * of the minimum set cover.
     *
     * @param s     the minimum set cover indices
     * @param map   a HashMap that store the real indices values.
     */
    static void getBackRealSetIndices( List<Integer> s, HashMap<Integer, Integer> map) {
        for (int i = 0; i < s.size(); i++){
            int restoredIndex =s.get(i);
            s.set(i, map.get(restoredIndex));
        }

    }

    /**
     * This method run the trivial set cover algorithm on the instance (s,U).
     * the returned set cover is represented by a set of indices in s.
     *
     *
     * @param s the set of sets
     * @param U the set of elements to cover
     * @return  a list of indices that match the list of sets that covers U
     */
    static List<Integer> trivialAlgo( List<List<Integer>> s, List<Integer> U ) {
        // base case
        if (s.isEmpty()) {
            if (U.isEmpty()) {
                return new ArrayList<>();
            }
            return null;
        }
        //index of S where S is the maximum set in s
        int indexOfS = Util.getMaximumSet(s);

        // Branch 1: we take S
        List<Integer> UPrime = Util.setMinus(U, s.get(indexOfS));
        List<List<Integer>> sPrime = new ArrayList<>();
        HashMap<Integer, Integer> indicesMap = new HashMap<>();
        takeS(s, indexOfS, sPrime, indicesMap);
        List<Integer> C1= null;
        C1 = trivialAlgo(sPrime, UPrime);
        if (C1!=null){
            getBackRealSetIndices(C1, indicesMap);
            C1.add(indexOfS);
        }

        // Branch 2: we discard S
        List<Integer> C2=null;
        sPrime = new ArrayList<>();
        UPrime = new ArrayList<>(U);
        indicesMap = new HashMap<>();
        discardS(s, indexOfS, sPrime, indicesMap);
        C2 = trivialAlgo(sPrime, UPrime);
        if (C2!=null)
            getBackRealSetIndices(C2, indicesMap);

        //choosing between C1 and C2
        return Util.min(C1, C2);
    }



    /**
     * Taking S into the set cover
     *
     *
     * @param s          the set of sets
     * @param indexOfS   the index of the chosen S
     * @param sPrime     the next set of sets
     * @param indicesMap a map for helping to save the real indices
     *                   this map will be reused when returning from the recursive calls.
     *
     */
    static void takeS( List<List<Integer>> s, int indexOfS, List<List<Integer>> sPrime, HashMap<Integer, Integer> indicesMap ) {
        int j = 0;
        for (int i = 0; i < s.size(); i++) {
            if (i != indexOfS) {
                List<Integer> t = Util.setMinus(s.get(i), s.get(indexOfS));
                if (!t.isEmpty()) {
                    sPrime.add(t);
                    indicesMap.put(j, i);
                    j++;
                }
            }
        }
    }


    /**
     * Discarding S from the set cover
     *
     *
     * @param s          the set of sets
     * @param indexOfS   the index of the discarded S
     * @param sPrime     the new set of sets
     * @param indicesMap a map for helping to save the real indices
     *                   this map will be used when returning from the recursive call
     *
     */
    static void discardS( List<List<Integer>> s, int indexOfS, List<List<Integer>> sPrime, HashMap<Integer, Integer> indicesMap ) {
        int j = 0;
        for (int i = 0; i < s.size(); i++) {
            if (i != indexOfS) {
                sPrime.add(s.get(i));
                indicesMap.put(j, i);
                j++;
            }
        }
    }



    /**
     * This method initialise the set U.
     *
     * @param g the {@link Graph} data structure
     * @return  the list of elements to cover
     */
    static List<Integer> initialisationOfU( Graph g ) {

        return g.getAdj().keySet()
                         .stream()
                         .collect(Collectors.toCollection(ArrayList::new));
    }


    /**
     * This method initialise the set of set
     * @param g the {@link Graph} data structure
     */
    static void subSetsInitialisation( Graph g ) {
        for (int key: g.getAdj().keySet())
            g.getAdj().get(key).add(key);
    }




    /**
     * Run the trivial set cover algorithm on the graph instance {@Code graph},
     * return a Result object that contains the minimum dominating set of {@Code graph}
     * and the time taken to compute it.
     *
     * @param graph the {@link Graph} instance
     * @return      the {@link Result}
     */
    @Override
    public Result run( Graph graph ) {
        double start = System.currentTimeMillis();
        subSetsInitialisation(graph);//s
        List<Integer> U = initialisationOfU(graph);
        List<List<Integer>> s =  graph.getAdj().values()
                                               .stream()
                                               .map(ArrayList::new)
                                               .collect(Collectors.toList());
        List<Integer> list = trivialAlgo(s, U);
        double end = System.currentTimeMillis();
        return new Result(new HashSet<>(list),end-start);
    }

}
