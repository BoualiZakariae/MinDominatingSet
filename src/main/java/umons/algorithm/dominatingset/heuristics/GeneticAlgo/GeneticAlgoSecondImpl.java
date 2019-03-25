package umons.algorithm.dominatingset.heuristics.GeneticAlgo;

import com.google.common.collect.BiMap;
import umons.algorithm.dominatingset.graph.Graph;
import umons.algorithm.dominatingset.graph.Result;
import umons.algorithm.dominatingset.toDelete.Stats;

import java.util.List;
import java.util.Random;
import java.util.Set;

public class GeneticAlgoSecondImpl extends GeneticAlgoImpl {


    @Override
    public Result run( Graph graph, int knownDominatingNumber ) {
        double start = System.currentTimeMillis();
        BiMap<Integer, Integer> biMap = genesToVerticesMapping(graph);
        List<Individual> individuals = popInitialisation(graph,biMap);
        Population population = new Population(individuals,p_Mutation, p_Better);
        Individual best = individuals.get(0),child;
        int gen=0,F = best.getFitness();
        double random ;
        do{
            random = new Random().nextDouble();
            child = population.evolve(random);
            Set<Integer> currentDS;
            currentDS = repairSolution(graph,child,biMap);
            minimizeSolution(graph, currentDS);
            child.setDS(getBackRealIndices(currentDS,biMap));
            child.computeFitness();
            if (population.isUnique(child)){
                population.replaceWorstBy(child);
                if(child.getFitness()<F){
                    System.out.println("best fitness found "+child.getFitness());
                    F = child.getFitness();
                    best = child;
                }
                gen++;
            }
        }while (gen < maxNumGeneration && (knownDominatingNumber == -1  ||  best.getFitness() != knownDominatingNumber));
        // printIndividualsSize(population.getIndividuals());
        double end = System.currentTimeMillis();
        Stats.numberOfGraphs++;
        return new Result(best.mdsFrom(biMap),end-start);
    }
}
