package umons.algorithm.dominatingset.heuristics.GeneticAlgo.operations;

import umons.algorithm.dominatingset.heuristics.GeneticAlgo.population.Individual;

import java.util.Random;

/**
 *
 */
public class OnePointCrossOver implements CrossOverStrategy {
    /**
     *
     * @param parentOne
     * @param parentTwo
     * @return
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
