package umons.algorithm.dominatingset.heuristics.GeneticAlgo.operations;

import umons.algorithm.dominatingset.heuristics.GeneticAlgo.population.Individual;

import java.util.Random;

/**
 *
 */
public interface MutationStrategy {
    /**
     *
     * @param child
     * @param p_Mutation
     */
    public void applyMutation( Individual child, double p_Mutation );
}


