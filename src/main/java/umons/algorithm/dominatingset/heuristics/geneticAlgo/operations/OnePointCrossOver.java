package umons.algorithm.dominatingset.heuristics.geneticAlgo.operations;

import umons.algorithm.dominatingset.heuristics.geneticAlgo.population.Individual;

import java.util.Random;

/**
 * This class implements the crossover strategy interface.
 * This class defines a single point crossover operation.
 *
 */
public class OnePointCrossOver implements CrossOverStrategy {


    /**
     *  This method apply a single point crossover operation on the given parents,
     *  to produce a child individual.
     *
     * @param parentOne the first individual as a first parent.
     * @param parentTwo the second individual as a second parent.
     * @return  a child individual made by a OnePointCrossOver operation on the parents individuals.
     */
    public Individual crossOver( Individual parentOne, Individual parentTwo ){
        Individual child = new Individual(parentOne.getSize());
        int random = new Random().nextInt(child.getSize());
        int index =0;
        while (index < random){
            child.setAtIndex(index, parentOne.getGenes()[index++]);
        }
        while (index < parentTwo.getSize()){
            child.setAtIndex(index, parentTwo.getGenes()[index++]);
        }
        return child;
    }

}
