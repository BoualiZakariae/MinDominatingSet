package umons.algorithm.dominatingset.heuristics.geneticAlgo.operations;

import umons.algorithm.dominatingset.heuristics.geneticAlgo.population.Individual;

import java.util.Random;

/**
 *  This class implements the mutation strategy interface.
 *
 *  The BitFlip mutation applied to an individual is just
 *  a bit flip operation with a given rate.
 */
public class BitFlipMutation implements MutationStrategy {


    /**
     * Apply the bit flip operation on the given Individual
     * with the given mutation rate.
     *
     * @param child the individual to which the mutation is applied.
     * @param pMutation the mutation rate.
     */
    @Override
    public void applyMutation( Individual child, double pMutation ) {
        int index = 0;
        Random random = new Random();
        double ran;
        while (index < child.getSize()) {
            ran = random.nextDouble();
            if (ran < pMutation)
                child.setAtIndex(index, (byte) (1 - child.getGenes()[index]));
            index++;
        }
    }
}
