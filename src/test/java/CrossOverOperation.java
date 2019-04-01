import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import umons.algorithm.dominatingset.heuristics.GeneticAlgo.operations.*;
import umons.algorithm.dominatingset.heuristics.GeneticAlgo.population.Individual;

/**
 *
 */
public class CrossOverOperation {


    private CrossOverStrategy crossOverStrategy;
    private MutationStrategy mutationStrategy;
    private Individual parentOne,parentTwo;


    @BeforeEach
    void init(){
        byte [] parentOneGenes = {1,1,0,0,1,0,1,0,1,1};
        byte [] parentTwoGenes = {0,0,1,0,1,0,0,1,1,0};
        parentOne = new Individual(parentOneGenes);
        parentTwo = new Individual(parentTwoGenes);
    }



    @Test@Disabled
    void onePointCrossOver(){
        System.out.println("parent One :"+parentOne);
        System.out.println("parent Two :"+parentTwo);
        crossOverStrategy= new OnePointCrossOver();
        Individual child = crossOverStrategy.crossOver(parentOne, parentTwo);
        System.out.println(child);
    }


    @Test@Disabled
    void twoPointsCrossOver(){
        System.out.println("parent One :"+parentOne);
        System.out.println("parent Two :"+parentTwo);
        crossOverStrategy= new TwoPointsCrossOver();
        Individual child = crossOverStrategy.crossOver(parentOne, parentTwo);
        System.out.println(child);
    }


    @Test
    void swapMutation(){
        System.out.println(parentOne);
        mutationStrategy = new SwapMutation();
        mutationStrategy.applyMutation(parentOne,0.2);
        System.out.println(parentOne);
    }




}
