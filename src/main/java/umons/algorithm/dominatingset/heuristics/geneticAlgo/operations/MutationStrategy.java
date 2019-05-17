package umons.algorithm.dominatingset.heuristics.geneticAlgo.operations;

import umons.algorithm.dominatingset.heuristics.geneticAlgo.population.Individual;

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
    void applyMutation( Individual child, double pMutation );
}


