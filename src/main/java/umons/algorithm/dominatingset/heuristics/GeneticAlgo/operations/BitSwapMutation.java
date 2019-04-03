package umons.algorithm.dominatingset.heuristics.GeneticAlgo.operations;

import umons.algorithm.dominatingset.heuristics.GeneticAlgo.population.Individual;

import java.util.Random;

/**
 *  This class implements the mutation strategy interface.
 *
 *  The BitSwap mutation applied to an individual is just
 *  a bit swapping operation with a given rate.
 */
public class BitSwapMutation implements MutationStrategy {


    /**
     * Apply the bits swapping operations on the given Individual
     * with the given mutation rate.
     *
     * @param child     the individual to which the mutation is applied.
     * @param pMutation the mutation rate.
     */
    @Override
    public void applyMutation( Individual child, double pMutation ) {
        int size = child.getSize();
        int numberOfSwap = (int) (pMutation *size);
        int count = 0;
        while (count++ < numberOfSwap) {
            bitSwapping(child);
        }
    }


    /**
     *  Apply a bit swapping operation on the given individual.
     *  @param child  the individual to which the bits swapping is applied.
     */
    private void bitSwapping( Individual child ) {
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
