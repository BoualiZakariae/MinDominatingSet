
package umons.algorithm.dominatingset.heuristics.GeneticAlgo.population;

import java.util.TreeSet;

/**
 * This class represents a population for the genetic algorithm
 *
 * This population use a treeSet data structure to store and retrieve the individuals.
 *
 */
public class TreeSetPopulation extends Population {

    /**
     * a Set of individuals.
     */
    private TreeSet<Individual> individuals;


    /**
     * Class constructor.
     * Create a population besad on the passed values.
     * @param individuals the set of individuals.
     */
    public TreeSetPopulation( TreeSet<Individual> individuals ) {
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
        return individuals.first().getSize();
    }


    /**
     *  This method should return the fittest individual of the population.
     *
     *  Technically, the best individual is the one that have the min fitness value.
     *
     * @return the best individual of this population.
     */
    @Override
    public Individual getFittest() {
        return this.individuals.first();
    }


    /**
     *
     * @return the individuals of this population.
     */
    public TreeSet<Individual> getIndividuals() {
        return individuals;
    }


    /**
     *  set the Set of the individuals.
     * @param individuals
     */
    public void setIndividuals( TreeSet<Individual> individuals ) {
        this.individuals = individuals;
    }
}

