package umons.algorithm.dominatingset.heuristics.GeneticAlgo.operations;

import umons.algorithm.dominatingset.heuristics.GeneticAlgo.population.Individual;

import java.util.Random;

/**
 * This interface defines the mutation operation.
 */
public interface MutationStrategy {


    /**
     * Apply the mutation operation on the given Individual
     * with the given mutation rate .
     *
     * @param child     the individual to which the mutation is applied.
     * @param pMutation the mutation rate.
     */
    public void applyMutation( Individual child, double pMutation );
}


