package umons.algorithm.dominatingset.heuristics.GeneticAlgo.population;

import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

/**
 * This class represents a population for the genetic algorithm
 *
 * This population use a List to hold the individuals
 * that evolve over each iteration.
 *
 */
public class ListPopulation extends Population {

    /*the list of individuals*/
    private List<Individual> individuals;

    /**
     * Class constructor.
     * Create a population based on the passed values.
     * @param individuals the list of individuals.
     */
    public ListPopulation( List<Individual> individuals) {
        this.individuals = individuals;
    }


    /**
     *
     * @return the size of this population.
     */
    @Override
    public int size() {
        return this.individuals.size();
    }


    /**
     *
     * @return the size of the individuals in this population.
     */
    @Override
    public int getIndividualSize() {
        return individuals.get(0).getSize();
    }

    /**
     *
     * @return the fittest individual of this population.
     */
    @Override
    public Individual getFittest() {
        return this.individuals
                .stream()
                .parallel()
                .min(Comparator.comparingInt(ind -> ind.getFitness()))
                .get();
    }


    /**
     *
     * @return a random individual from this population.
     *
     */
    public Individual getRandomIndividual(){
         int randomIndex = new Random().nextInt(individuals.size());
         return individuals.get(randomIndex);
    }



    /**
     *  This method return a second fittest individual from this population,
     *  the fittest is given in the parameter.
     *
     * @param fittest the fittest individual of this population.
     *
     * @return the second fittest of this population.
     */
    public Individual getSecondFittest( Individual fittest ) {
        return individuals.stream()
                .parallel()
                .filter(s->!s.equals(fittest))
                .min(Comparator.comparingInt(ind -> ind.getFitness()))
                .get();

    }

    /**
     *
     * @param child
     * @return true whenever this population dosen't contains an individual equal to the given individual,
     *         otherwise return false.
     */
    public boolean isUnique( Individual child ) {
        return !this.individuals.contains(child);
    }

    /**
     *  This method replace the individual with the highest fitness
     *  with the given individual.
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
     * @return the list of individuals.
     */
    public List<Individual> getIndividuals() {
        return individuals;
    }


    /**
     *
     * @param fittestOne
     * @return a random individual from the population that is not equal
     *         to the given individual.
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
