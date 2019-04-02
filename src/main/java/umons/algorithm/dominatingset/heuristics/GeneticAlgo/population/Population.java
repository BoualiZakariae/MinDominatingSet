package umons.algorithm.dominatingset.heuristics.GeneticAlgo.population;

/**
 *  This abstract class represents the population used
 *  in the genetic algorithm.
 *
 *  This class exposes the methods that should be implemented by the subclasses.
 *  every subclasses can choose the fittest data structure.
 */
public abstract class Population {

    /**
     *
     * @return the size of this population.
     */
    public abstract int size();


    /**
     *
     * @return the size of the individuals in this population.
     */
    public abstract int getIndividualSize();


    /**
     *  This method should return the fittest individual of the population.
     *
     *  Technically, the best individual is the one that have the min fitness value.
     *
     * @return the best individual of this population.
     */
    public abstract Individual getFittest();


    /**
     *
     * Generate a random individual with the given probability.
     *
     *
     * @param p_prob the given probability
     * @return a random individual
     */
    public Individual generateRandomIndividual(double p_prob ) {
        Individual individual = new Individual(getIndividualSize(), p_prob);
        return individual;
    }


}
