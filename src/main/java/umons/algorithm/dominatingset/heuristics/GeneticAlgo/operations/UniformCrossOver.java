package umons.algorithm.dominatingset.heuristics.GeneticAlgo.operations;

import umons.algorithm.dominatingset.heuristics.GeneticAlgo.population.Individual;

import java.util.Random;

/**
 *
 */
public class UniformCrossOver implements CrossOverStrategy {
    /**
     *
     * @param parentOne
     * @param parentTwo
     * @return
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
