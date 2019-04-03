package umons.algorithm.dominatingset.heuristics.GeneticAlgo.operations;

import umons.algorithm.dominatingset.heuristics.GeneticAlgo.population.Individual;

import java.util.Random;

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
    public Individual crossOver( Individual parentOne, Individual parentTwo );
}



