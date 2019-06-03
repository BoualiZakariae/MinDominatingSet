import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import umons.algorithm.dominatingset.heuristics.geneticAlgo.population.Individual;
import umons.algorithm.dominatingset.heuristics.geneticAlgo.population.ListPopulation;
import umons.algorithm.dominatingset.heuristics.geneticAlgo.population.Population;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
@Disabled
public class ListPopulationTest {

    private Individual parentOne,parentTwo,parentThree,parentFour
                        ,parentFive, parentsix;

    List<Individual> individualsList ;

    private Population population;


    @BeforeEach@Disabled
    void init(){
        byte [] parent1Genes = {1,1,0,0,1,0,1,0,1,1};
        byte [] parent2Genes = {0,0,1,0,1,0,0,1,1,1};
        byte [] parent3Genes = {1,0,0,0,1,0,0,0,1,1};
        byte [] parent4Genes = {1,1,1,0,1,0,0,1,1,0};
        byte [] parent5Genes = {0,1,0,0,0,0,0,0,1,1};
        byte [] parent6Genes = {0,0,1,0,1,0,0,1,1,1};


        parentOne   = new Individual(parent1Genes);
        parentTwo   = new Individual(parent2Genes);
        parentThree = new Individual(parent3Genes);
        parentFour  = new Individual(parent4Genes);
        parentFive  = new Individual(parent5Genes);
        parentsix   = new Individual(parent6Genes);


        individualsList = Stream.of(parentOne,parentTwo,parentThree,parentFour
                ,parentFive, parentsix
        ) .collect(Collectors.toCollection(ArrayList::new));
        individualsList.stream().forEach(individual -> individual.computeFitness());
        population = new ListPopulation(individualsList);
    }


    @Test@Disabled
    public void test (){
        Individual fittest = population.getFittest();
        Assertions.assertEquals(fittest, parentFive);
        Assertions.assertEquals(((ListPopulation)population).getSecondFittest(fittest),parentThree);
    }

}
