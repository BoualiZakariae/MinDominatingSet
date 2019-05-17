package umons.algorithm.dominatingset.heuristics.geneticAlgo.operations;

import umons.algorithm.dominatingset.heuristics.geneticAlgo.population.Individual;

import java.util.Random;

/**
 * This class implements the crossover strategy interface.
 * This class defines a uniform crossover operation.
 *
 */
public class UniformCrossOver implements CrossOverStrategy {


    /**
     *  This method apply a uniform crossover operation on the given parents,
     *  to produce a child individual.
     *
     * @param parentOne the first individual as a first parent.
     * @param parentTwo the second individual as a second parent.
     * @return  a child individual made by a uniform crossover operation on the parents individuals.
     */
    public Individual crossOver( Individual parentOne, Individual parentTwo ){
        Individual child = new Individual(parentOne.getSize());
        double probParentOne = (double) parentOne.getFitness() / (parentOne.getFitness() + parentTwo.getFitness());
        double p_parent;
        int index = 0;
        while (index < parentOne.getSize()) {
            p_parent = new Random().nextDouble();
            if (p_parent < probParentOne)
                child.setAtIndex(index, parentOne.getGenes()[index]);
            else
                child.setAtIndex(index, parentTwo.getGenes()[index]);
            index++;
        }
        return child;
    }
}
