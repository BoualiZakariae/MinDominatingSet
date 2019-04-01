
package umons.algorithm.dominatingset.heuristics.GeneticAlgo.population;

import java.util.*;

/**
 * This class represents a population for the genetic algorithm
 *
 * a population is a list of individuals that evolve over each iteration
 *
 */
public class TreeSetPopulation extends Population {


    private TreeSet<Individual> individuals;

    /**
     * Class constructor
     * Create a population besed on the passed values
     * @param individuals the list of individuals
     */
    public TreeSetPopulation( TreeSet<Individual> individuals ) {
        this.individuals = individuals;
    }


    @Override
    public int size() {
        return this.individuals.size();
    }

    @Override
    public int getIndividualSize() {
        return individuals.first().getSize();
    }

    @Override
    public Individual getFittest() {
        return this.individuals.first();
    }

    /**
     *
     * @return
     */
    public TreeSet<Individual> getIndividuals() {
        return individuals;
    }

    /**
     *
     * @param individuals
     */
    public void setIndividuals( TreeSet<Individual> individuals ) {
        this.individuals = individuals;
    }
}

