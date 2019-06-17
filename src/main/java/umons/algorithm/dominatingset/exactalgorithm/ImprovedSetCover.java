package umons.algorithm.dominatingset.exactalgorithm;

import umons.algorithm.dominatingset.graph.Graph;
import umons.algorithm.dominatingset.graph.Result;
import umons.algorithm.dominatingset.util.Stats;
import umons.algorithm.dominatingset.util.EdmondsMatching;
import umons.algorithm.dominatingset.util.UndirectedGraph;
import umons.algorithm.dominatingset.util.Util;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 *
 * This Class implements the Algorithm : "The algorithm of Fomin, Grandoni, Kratsch"
 *                      from the paper : Exact algorithms for dominating set.
 *                                       Johan M.M. van Rooij, Hans L. Bodlaender
 *
 * This algorithm represent the 'Algorithme amélioré' in the thesis paper.
 *
 */
public class ImprovedSetCover extends TrivialSetCover {

    /**
     *  This map holds the frequency of each element
     */
    private static HashMap<Integer, Integer> frequencyMap;

    /**
     * Counting the frequency of the elements
     *
     * @param s the set of sets
     */
    private static void countElementsFrequency( List<List<Integer>> s ) {
        s.stream()
         .flatMap(List::stream)
         .forEach(i -> frequencyMap.replace(i, frequencyMap.get(i)+1));
    }

    /**
     *
     * @param s  the set of sets
     * @param U  the set of elements to cover
     * @return   the index of the set with an element that has frequencyMap one
     */
    private static int getTheSingletonSetIndex( List<List<Integer>> s, List<Integer> U ) {

        Optional<Integer> singleton = U.stream()
                                       .filter(i -> frequencyMap.get(i) == 1)
                                       .findFirst();
        if (!singleton.isPresent())
            return -1;

        return IntStream.range(0,s.size())
                        .filter(index->s.get(index)
                        .contains(singleton.get()))
                        .findFirst()
                        .getAsInt();
    }

    /**
     * This method apply the first reduction rule.
     *
     * @param s     the set of set
     * @param U     the set of elements to cover
     * @return      the Set Cover of U
     */
    private static List<Integer> reductionRuleOne( List<List<Integer>> s, List<Integer> U ) {
        countElementsFrequency(s);
        // if there exists an element e in U with frequency 1
        int singletonSetindex = getTheSingletonSetIndex(s, U);
        if (singletonSetindex == -1)
            return null;
        List<List<Integer>> sPrime = new ArrayList<>();
        List<Integer> UPrime = Util.setMinus(U, s.get(singletonSetindex));
        HashMap<Integer, Integer> indicesMap = new HashMap<>();
        takeS(s, singletonSetindex, sPrime, indicesMap);
        List<Integer> setCover = Algo3MSC(sPrime, UPrime);
        getBackRealSetIndices(setCover, indicesMap);
        setCover.add(singletonSetindex);
        return setCover;
    }


    /**
     * This method apply the second reduction rule.
     *
     * @param s     the set of sets
     * @param U     the set of elements to cover
     * @return      the Set Cover of U
     */
    private static List<Integer> reductionRuleTwo( List<List<Integer>> s, List<Integer> U ) {
        for (int i = 0; i < s.size(); i++) {
            for (int j = 0; j < s.size(); j++) {
                if (i != j) {
                    if (Util.isSubset(s.get(i), s.get(j))) {
                        List<List<Integer>> sPrime = new ArrayList<>();
                        HashMap<Integer, Integer> map = new HashMap<>();
                        discardS(s,i,sPrime,map);
                        List<Integer> setCover = Algo3MSC(sPrime, U);
                        getBackRealSetIndices(setCover, map);
                        return setCover;
                    }
                }
            }
        }
        return null;
    }

    /**
     * This method apply the third reduction rule (the 4 rule in the paper).
     * maximum matching to find set-cover
     *
     * This method is executed whenever there is no set that contains more
     * than 2 elements, to calculate the set cover of such instance
     * the Edmonds Matching Algorithm has been used to get a solution in a polynomial time.
     *
     * the Edmonds Matching implementation was get from:http://www.keithschwarz.com/interesting/code/edmonds-matching/
     *
     *
     *
     * @param s  the set of set
     * @param U  the set of elements to cover
     * @return   the set cover of U
     */
     private static List<Integer> reductionRuleThree( List<List<Integer>> s, List<Integer> U ) {
        int S = Util.getMaximumSet(s);
        if (s.get(S).size() <= 2) {
            UndirectedGraph graph = Util.createTheGraph(U,s);
            EdmondsMatching edmondsMatching = new EdmondsMatching();
            UndirectedGraph  MM = edmondsMatching.maximumMatching(graph);
            List<Integer> setCover = Util.getSetCover(MM,s);
            return setCover;
        }
        return null;
    }


    /**
     * This method presents the main method of the improved set cover algorithm.
     *
     * @param s  the set of sets
     * @param U  the set of elements to cover
     * @return   the Set Cover of U
     */
    private static List<Integer> Algo3MSC( List<List<Integer>> s, List<Integer> U ) {
        if (s.isEmpty()) {
            if (U.isEmpty()) {
                return new ArrayList<>();
            }
            return null;
        }

        // rule 1
        List<Integer> setCover = reductionRuleOne(s, U);
        if (setCover != null)
            return setCover;
        // rule 2
        setCover = reductionRuleTwo(s, U);
        if (setCover != null)
            return setCover;

        // rule 3
        setCover = reductionRuleThree(s, U);
        if (setCover != null)
            return setCover;

        return trivialAlgo(s, U);
    }

    /**
     * Run the improved set cover algorithm on the graph instance {@Code graph},
     * return a Result object that contains the minimum dominating set of {@Code graph}
     * and the time taken to compute it.
     *
     * @param graph the {@link Graph} instance
     * @return      the {@link Result} solution
     */
    @Override
    public Result run( Graph graph ) {
        double start = System.currentTimeMillis();
        subSetsInitialisation(graph);//s
        List<Integer> U = initialisationOfU(graph);
        frequencyMap =  initialiseFrequencyArray(graph);
        List<List<Integer>> s =  graph.getAdj().values().stream()
                .map(ArrayList::new)
                .collect(Collectors.toList());
        List<Integer> list = Algo3MSC(s, U);
        double end = System.currentTimeMillis();
        Stats.numberOfGraphs++;
        return new Result(new HashSet<>(list),end-start);
    }

    /**
     *
     * @param g  the {@link Graph} instance
     * @return   the frequencyMap
     */
    private HashMap<Integer,Integer> initialiseFrequencyArray(Graph g) {
        frequencyMap =new HashMap<>();
        for(int v:g.getAdj().keySet())
            frequencyMap.put(v,0);
        return frequencyMap;
        }
}
