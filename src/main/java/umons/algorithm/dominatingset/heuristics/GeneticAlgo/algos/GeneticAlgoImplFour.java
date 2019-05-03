package umons.algorithm.dominatingset.heuristics.GeneticAlgo.algos;

import com.google.common.collect.BiMap;
import umons.algorithm.dominatingset.exactalgorithm.mdsAlgorithm;
import umons.algorithm.dominatingset.graph.Graph;
import umons.algorithm.dominatingset.graph.Result;
import umons.algorithm.dominatingset.heuristics.GeneticAlgo.population.Individual;
import umons.algorithm.dominatingset.heuristics.GeneticAlgo.population.ListPopulation;
import umons.algorithm.dominatingset.heuristics.Greedy.Greedy;
import umons.algorithm.dominatingset.heuristics.Greedy.GreedyRandom;
import umons.algorithm.dominatingset.heuristics.Greedy.GreedyRev;
import umons.algorithm.dominatingset.toDelete.Stats;

import java.util.*;

public class GeneticAlgoImplFour extends GeneticAlgoImplOne {

    @Override
    public Result run( Graph graph, int knownDominatingNumber ) {
        double start = System.currentTimeMillis();
        BiMap<Integer, Integer> biMap = genesToVerticesMapping(graph);
        ListPopulation population = createPopulation(graph, biMap,getGreedyIndividuals(graph));
        Individual best = population.getFittest(),newChild;
        int gen=0,F = best.getFitness();
        Set<Integer> currentDS;
        do{
            newChild = generateChild(population);
            currentDS = repairSolution(graph, biMap, newChild);
            GeneticAlgoUtil.minimizeSolution(graph, currentDS);
            newChild.setDS(GeneticAlgoUtil.getBackRealIndices(currentDS,biMap));
            newChild.computeFitness();
            if (population.isUnique(newChild)){
                population.replaceWorstBy(newChild);
                if(newChild.getFitness() < F){
                    System.out.println("new best fitness found "+newChild.getFitness());
                    F = newChild.getFitness();
                    best = newChild;
                }
                gen++;
            }
        }while (gen < maxNumGeneration && (knownDominatingNumber == -1  ||  best.getFitness() != knownDominatingNumber));
        double end = System.currentTimeMillis();
        System.out.println("nombre d'iteration dans lalgo1: "+gen);
        Stats.numberOfGraphs++;
        return new Result(best.mdsFrom(biMap),end-start);
    }

    private static List<Individual> getGreedyIndividuals( Graph graph ) {
        Set<Individual> individuals = new HashSet<>();
        mdsAlgorithm greedy;
        Result res;
        Individual indiv;
        System.out.println("beginin list construction");
        while (individuals.size()<100){
            greedy = new Greedy();
            res = greedy.run(graph);
            indiv = new Individual(graph.size(), res.getMds());
            indiv.computeFitness();
            individuals.add(indiv);
            if (individuals.size()==100)
                break;
            greedy = new GreedyRandom();
            res = greedy.run(graph);
            indiv = new Individual(graph.size(), res.getMds());
            indiv.computeFitness();
            individuals.add(indiv);
            if (individuals.size()==100)
                break;
            greedy = new GreedyRev();
            res = greedy.run(graph);
            indiv = new Individual(graph.size(), res.getMds());
            indiv.computeFitness();
            individuals.add(indiv);
            System.out.println(individuals.size());

        }
        System.out.println("finishing the list construction");
        GeneticAlgoUtil.printIndividualsSize(new ArrayList<>(individuals));
        return new ArrayList<>(individuals);
    }

}
