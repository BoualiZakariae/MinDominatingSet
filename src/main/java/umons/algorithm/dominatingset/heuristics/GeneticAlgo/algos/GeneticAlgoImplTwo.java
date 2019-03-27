package umons.algorithm.dominatingset.heuristics.GeneticAlgo.algos;

import com.google.common.collect.BiMap;
import umons.algorithm.dominatingset.graph.Graph;
import umons.algorithm.dominatingset.graph.Result;
import umons.algorithm.dominatingset.heuristics.GeneticAlgo.population.Individual;
import umons.algorithm.dominatingset.heuristics.GeneticAlgo.population.TreeSetPopulation;
import umons.algorithm.dominatingset.toDelete.Stats;

import java.util.*;

public class GeneticAlgoImplTwo extends GeneticAlgorithm {


    /**
     *
     * @param graph
     * @param knownDominatingNumber
     * @return
     */
    @Override
    public Result run( Graph graph, int knownDominatingNumber ) {
        double start = System.currentTimeMillis();
        BiMap<Integer, Integer> biMap = genesToVerticesMapping(graph);
        TreeSet<Individual> individuals = new TreeSet<>();
        popInitialisation(graph, biMap, individuals);
        TreeSetPopulation population = new TreeSetPopulation(individuals);
        int gen=0;
        int maxGen = 1000;
        do{
            evolve(graph, biMap,population );
            if (population.getIndividuals().first().getFitness() == knownDominatingNumber){
                System.out.println("we should break");
                break;
            }
        }while (gen++ < maxGen || knownDominatingNumber == -1 );
        double end = System.currentTimeMillis();
        Stats.numberOfGraphs++;
        return new Result(individuals.first().mdsFrom(biMap),end-start);
    }


    /**
     * whenever this method is called, a new individuals are created to replace
     * the half of the current population
     *
     * @param graph
     * @param map
     * @param treeSetPopulation
     */
    public void evolve( Graph graph, BiMap<Integer, Integer> map,TreeSetPopulation treeSetPopulation ) {
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
            currentDS = GeneticAlgoUtil.repairSolution(graph,child,map);
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
