package umons.algorithm.dominatingset.heuristics.GeneticAlgo.operations;

import umons.algorithm.dominatingset.heuristics.GeneticAlgo.population.Individual;

import java.util.Random;

/**
 *
 */
public class TwoPointsCrossOver implements CrossOverStrategy {
    /**
     *
     * @param parentOne
     * @param parentTwo
     * @return
     */
    public Individual crossOver( Individual parentOne, Individual parentTwo ){
        Individual child = new Individual(parentOne.getSize());
        int randomOne = new Random().nextInt(child.getSize());
        int randomTwo;
        do {
            randomTwo = new Random().nextInt(child.getSize());
        }while (randomOne==randomTwo);
        int min,max,index=0;
        if (randomOne<randomTwo){
            min=randomOne;
            max=randomTwo;
        }else{
            min=randomTwo;
            max=randomOne;
        }
        while (index <min){
            child.setAtIndex(index, parentOne.getGenes()[index++]);
        }
        while (index < max){
            child.setAtIndex(index, parentTwo.getGenes()[index++]);
        }
        while (index < child.getSize()){
            child.setAtIndex(index, parentOne.getGenes()[index++]);
        }
        return child;
    }
}
