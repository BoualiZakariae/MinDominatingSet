package umons.algorithm.dominatingset.heuristics.geneticAlgo.operations;

import umons.algorithm.dominatingset.heuristics.geneticAlgo.population.Individual;

/**
 *  This interface defines the crossover operation.
 */
public interface CrossOverStrategy {


    /**
     * This method defines a crossover operation.
     * @param parentOne the first individual as a first parent.
     * @param parentTwo the second individual as a second parent.
     * @return a child individual made by a crossover operation to the parent individuals.
     */
    Individual crossOver( Individual parentOne, Individual parentTwo );
}



