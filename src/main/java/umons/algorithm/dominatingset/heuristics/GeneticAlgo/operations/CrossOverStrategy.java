package umons.algorithm.dominatingset.heuristics.GeneticAlgo.operations;

import umons.algorithm.dominatingset.heuristics.GeneticAlgo.population.Individual;

import java.util.Random;

/**
 *
 */
public interface CrossOverStrategy {
    /**
     *
     * @param parentOne
     * @param parentTwo
     * @return
     */
    public Individual crossOver( Individual parentOne, Individual parentTwo );
}



