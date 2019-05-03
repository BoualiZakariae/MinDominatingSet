package umons.algorithm.dominatingset.heuristics.GeneticAlgo.algos;

import com.google.common.collect.BiMap;
import umons.algorithm.dominatingset.exactalgorithm.mdsAlgorithm;
import umons.algorithm.dominatingset.graph.Graph;
import umons.algorithm.dominatingset.graph.Result;
import umons.algorithm.dominatingset.heuristics.GeneticAlgo.population.Individual;
import umons.algorithm.dominatingset.heuristics.GeneticAlgo.population.TreeSetPopulation;
import umons.algorithm.dominatingset.heuristics.Greedy.Greedy;
import umons.algorithm.dominatingset.heuristics.Greedy.GreedyRandom;
import umons.algorithm.dominatingset.heuristics.Greedy.GreedyRev;
import umons.algorithm.dominatingset.toDelete.Stats;

import java.util.ArrayList;
import java.util.TreeSet;

public class GeneticAlgoImplThree extends GeneticAlgoImplTwo {

    @Override
    public Result run( Graph graph, int knownDominatingNumber ) {
        double start = System.currentTimeMillis();

        TreeSet<Individual> individuals = getGreedyIndividuals(graph);

        BiMap<Integer, Integer> biMap = genesToVerticesMapping(graph);
        popInitialisation(graph, biMap, individuals);
        TreeSetPopulation population = new TreeSetPopulation(individuals);
        int gen=0;
        int maxGen = 10000;
        do{
            evolve(graph, biMap,population );
            if (population.getIndividuals().first().getFitness() == knownDominatingNumber){
                System.out.println("we should break");
                break;
            }
        }while (gen++ < maxGen || knownDominatingNumber == -1 );
        double end = System.currentTimeMillis();
        Stats.numberOfGraphs++;
        return new Result(population.getIndividuals().first().mdsFrom(biMap),end-start);

    }

     private static TreeSet<Individual> getGreedyIndividuals( Graph graph ) {
         mdsAlgorithm greedy;
         Result res;
         Individual indiv;
         TreeSet<Individual> individuals = new TreeSet<>();
         int index=0;
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
         System.out.println("finishing the greedy");
         GeneticAlgoUtil.printIndividualsSize(new ArrayList<>(individuals));

         return individuals;
    }
}
