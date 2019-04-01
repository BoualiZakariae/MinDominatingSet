package umons.algorithm.dominatingset.heuristics.GeneticAlgo.operations;

import umons.algorithm.dominatingset.heuristics.GeneticAlgo.population.Individual;

import java.util.Random;

/**
 *
 */
public class BitSwapMutation implements MutationStrategy {
    /**
     *
     * @param child
     * @param p_Mutation
     */
    @Override
    public void applyMutation( Individual child, double p_Mutation ) {
        int index = 0;
        Random random = new Random();
        double ran;
        while (index < child.getSize()) {
            ran = random.nextDouble();
            if (ran < p_Mutation)
                child.setAtIndex(index, (byte) (1 - child.getGenes()[index]));
            index++;
        }
    }
}
