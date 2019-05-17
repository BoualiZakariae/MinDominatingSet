package umons.algorithm.dominatingset.heuristics.geneticAlgo.algos;

import com.google.common.collect.BiMap;
import umons.algorithm.dominatingset.graph.Graph;
import umons.algorithm.dominatingset.graph.Result;
import umons.algorithm.dominatingset.heuristics.geneticAlgo.population.Individual;
import umons.algorithm.dominatingset.heuristics.geneticAlgo.population.TreeSetPopulation;
import umons.algorithm.dominatingset.util.Stats;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
/**
 *
 * A second genetic algorithm implementation for solving the minimum dominating set problem
 *
 * This Algo implementation is based on the GeneticAlgoImplOne.
 *
 */
public class GeneticAlgoImplTwo extends GeneticAlgorithm {

    /**
     * The main genetic algorithm method
     *
     * @param graph the given graph.
     * @param knownDominatingNumber
     *              the known minimum dominating Set size
     *              for the given graph.
     *              When this value equal to -1, it means that the mds
     *              number is unknown for this graph.
     *              Knowing the mds size help to stop the algorithm.
     *
     * @return      the dominating set and the time taken to compute it.
     */
    @Override
    public Result run( Graph graph, int knownDominatingNumber ) {
        double start = System.currentTimeMillis();
        BiMap<Integer, Integer> biMap = genesToVerticesMapping(graph);
        TreeSet<Individual> individuals = new TreeSet<>();
        popInitialisation(graph, biMap, individuals);
        TreeSetPopulation population = new TreeSetPopulation(individuals);
        int gen=0;
        int maxGen = 10000;//10000
        do{
           // System.out.println("gene "+gen);
            evolve(graph, biMap,population );
            if (population.getIndividuals().first().getFitness() == knownDominatingNumber){
                System.out.println("we should break");
                break;
            }
        }while (gen++ < maxGen || knownDominatingNumber != -1 );
        double end = System.currentTimeMillis();
        Stats.numberOfGraphs++;
        return new Result(population.getIndividuals().first().mdsFrom(biMap),end-start);
    }



    /**
     * whenever this method is called, new individuals are created to integrate the population
     * On every call 25% of the population are replaced by new individuals.
     *
     * the new individuals replace the worst individuals.
     *
     * @param graph
     * @param map
     * @param treeSetPopulation
     */
    private void evolve( Graph graph, BiMap<Integer, Integer> map, TreeSetPopulation treeSetPopulation ) {
        int index=0;
        Iterator<Individual> it = treeSetPopulation.getIndividuals().iterator();
        int half= treeSetPopulation.getIndividuals().size()/2;
        TreeSet<Individual> set2 = new TreeSet<>();
        Individual parentOne, parentTwo, child;
        Set<Integer> currentDS = null;
        while ( index < half) {
            if (index++ % 2 != 0){
                continue;
            }
            parentOne = it.next();
            parentTwo = it.next();
            child = crossOver(parentOne,parentTwo);
            applyMutation(child, p_Mutation);
            currentDS = GeneticAlgoUtil.heuristicRepair(graph,child,map);
            GeneticAlgoUtil.minimizeSolution(graph, currentDS);
            child.setDS(GeneticAlgoUtil.getBackRealIndices(currentDS,map));
            child.computeFitness();
            set2.add(child);
        }
        int sizeOf = treeSetPopulation.getIndividuals().size();
        while (set2.size() < sizeOf){
            set2.add(treeSetPopulation.getIndividuals().pollFirst());
        }
        treeSetPopulation.setIndividuals(set2);
        System.out.println(treeSetPopulation.getIndividuals().first().getFitness());
    }


}
