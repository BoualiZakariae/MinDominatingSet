package umons.algorithm.dominatingset.heuristics.GeneticAlgo.algos;

import com.google.common.collect.BiMap;
import umons.algorithm.dominatingset.exactalgorithm.ArbitraryGraph;
import umons.algorithm.dominatingset.graph.Graph;
import umons.algorithm.dominatingset.heuristics.GeneticAlgo.population.Individual;
import umons.algorithm.dominatingset.heuristics.Greedy.Greedy;
import umons.algorithm.dominatingset.util.Util;

import java.util.*;

/**
 *
 * This class contains helper methods for the genetic algorithm.
 *
 */
public class GeneticAlgoUtil {

    /**
     * Given a dominating set solution, this method try to minimize
     *  the ds size by removing vertices,
     *  without affecting the domination property.
     *
     *  todo: improving the algorithm complexity
     * @param graph
     * @param currentDS
     */
    public static void minimizeSolution( Graph graph, Set<Integer> currentDS ) {
        Iterator<Integer> it = currentDS.iterator();
        Set<Integer> newDS;
        while (it.hasNext()){
            int v = it.next();
            newDS = new HashSet<>(currentDS);
            newDS.remove(v);
            Set<Integer> neighbors = graph.getClosedNeighbors(v);
            if(ArbitraryGraph.isDominating(graph,newDS,neighbors)){
                it.remove();
            }
        }
    }

    /**
     *    *
     * @param individual
     * @return
     *
     * Heuristic repair
     *
     * @param graph
     * @param individual
     * @param map
     * @return
     */
    public static Set<Integer> heuristicRepair( Graph graph, Individual individual, BiMap<Integer, Integer> map ) {
        Set<Integer> currentDS      = new HashSet<>();
        Set<Integer> setToDominate  = new HashSet<>();
        for (Integer key :graph.getAdj().keySet()) {
            setToDominate.add(key);
            if(individual.getGenes()[map.inverse().get(key)]==1)
                currentDS.add(key);
        }
        Set<Integer> remainingSet = Util.remainingSet(graph,currentDS,setToDominate);
        if (remainingSet.size()==0)
            return currentDS ;
        Set<Integer> graphVertices = new HashSet<>(graph.getAdj().keySet());
        Set<Integer> dominatedVertices = Util.setMinus(graphVertices,remainingSet);
        Graph nonDominatedSubGraph = graph.removeVertices(dominatedVertices);
        Set<Integer> mdsOfRemainingSet = new Greedy().run(nonDominatedSubGraph).getMds();
        currentDS.addAll(mdsOfRemainingSet);
        return currentDS ;
    }


    /**
     *
     * @param graph
     * @param child
     * @param map
     * @return
     */
    static Set<Integer> randomRepair( Graph graph, Individual child, BiMap<Integer, Integer> map ) {
        Set<Integer> currentDS      = new HashSet<>();
        Set<Integer> setToDominate  = new HashSet<>();
        for (Integer key :graph.getAdj().keySet() ) {
            setToDominate.add(key);
            if(child.getGenes()[map.inverse().get(key)]==1)
                currentDS.add(key);
        }
        boolean dominated = false;
        Random random =new Random();
        int ran;
        while (!dominated){
            do{
                ran = random.nextInt(child.getSize());
            }
            while (currentDS.contains(map.get(ran)));
            currentDS.add(map.get(ran));
            Set<Integer> remainingSet = Util.remainingSet(graph,currentDS,setToDominate);
            if (remainingSet.size()==0)
                dominated=true ;
        }
        return currentDS ;
    }

    /**
     *
     * @param DS
     * @param map
     * @return
     */
    public static Set<Integer> getBackRealIndices( Set<Integer> DS, BiMap<Integer, Integer> map ) {
        Set<Integer> realIndices = new HashSet<>();
        for (Integer b: DS) {
            realIndices.add(map.inverse().get(b));
        }
        return realIndices;
    }

    /**
     *  helper method to print individuals fitness
     * @param individuals
     */
    public static void printIndividualsSize( List<Individual> individuals ) {
        for (Individual ind:individuals) {
            System.out.print(ind.getFitness()+" ");
        }
    }
}
