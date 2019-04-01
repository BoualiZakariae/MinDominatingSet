package umons.algorithm.dominatingset.heuristics.GeneticAlgo.operations;

import umons.algorithm.dominatingset.heuristics.GeneticAlgo.population.Individual;

import java.util.Random;

/**
 *
 */
public class SwapMutation implements MutationStrategy {
    /**
     *
     * @param child
     * @param p_Mutation
     */
    @Override
    public void applyMutation( Individual child, double p_Mutation ) {
        int size = child.getSize();
        int numberOfSwap = (int) (p_Mutation*size);
        int count = 0;
        while (count++ < numberOfSwap) {
            swapBits(child);
        }
    }

    /**
     *
     * @param child
     */
    private void swapBits( Individual child ) {
        int randomOne = new Random().nextInt(child.getSize());
        int randomTwo;
        do {
            randomTwo = new Random().nextInt(child.getSize());
        }while (randomOne==randomTwo);
        byte valueOne  = child.getGenes()[randomOne];
        byte valueTwo = child.getGenes()[randomTwo];
        child.setAtIndex(randomOne, valueTwo);
        child.setAtIndex(randomTwo, valueOne);
    }
}
