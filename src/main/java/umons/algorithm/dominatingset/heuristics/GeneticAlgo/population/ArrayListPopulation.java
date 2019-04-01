package umons.algorithm.dominatingset.heuristics.GeneticAlgo.population;

import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

/**
 * This class represents a population for the genetic algorithm
 *
 * This  population use an ArrayList to hold the individuals
 * that evolve over each iteration
 *
 */
public class ArrayListPopulation extends Population {

    /*the list of individuals*/
    private List<Individual> individuals;

    /**
     * Class constructor
     * Create a population besed on the passed values
     * @param individuals the list of individuals
     */
    public ArrayListPopulation( List<Individual> individuals) {
        this.individuals = individuals;
    }

    @Override
    public int size() {
        return this.individuals.size();
    }

    @Override
    public int getIndividualSize() {
        return individuals.get(0).getSize();
    }

    /**
     *
     * @return
     */
    public Individual getFittest() {
        return this.individuals
                .stream()
                .parallel()
                .min(Comparator.comparingInt(indiv -> indiv.getFitness()))
                .get();
    }


    /**
     *
     * @return
     */
    public Individual getRandomIndividual(){
        int randomIndex = new Random().nextInt(individuals.size());
            return individuals.get(randomIndex);
    }



    /**
     *
     * @param fittest
     * @return
     */
    public Individual getSecondFittest( Individual fittest ) {
        return individuals.stream()
                .parallel()
                .filter(s -> s.getFitness() <= fittest.getFitness())
                .min(Comparator.comparingInt(object -> object.getFitness()))
                .get();

    }

    /**
     *
     * @param child
     * @return
     */
    public boolean isUnique( Individual child ) {
        return !this.individuals.contains(child);
    }

    /**
     *
     * @param child
     */
    public void replaceWorstBy( Individual child ) {
        int worstIndividualIndex = IntStream.range(0, individuals.size())
                .boxed()
                .parallel()
                .max(Comparator.comparingInt(i -> individuals.get(i).getFitness()))
                .get();
        this.individuals.set(worstIndividualIndex, child);

    }

    /**
     *
     * @return
     */
    public List<Individual> getIndividuals() {
        return individuals;
    }

    /**
     *
     * @param fittestOne
     * @return a random individual from the population that is not equal
     *         to the given individual
     */
    public Individual getRandomIndividual( Individual fittestOne ) {
        Individual secondFittest;
        do {
            int randomIndex = new Random().nextInt(individuals.size());
            secondFittest = individuals.get(randomIndex);
        }while(secondFittest.equals(fittestOne)==true);

        return secondFittest;
    }
}
