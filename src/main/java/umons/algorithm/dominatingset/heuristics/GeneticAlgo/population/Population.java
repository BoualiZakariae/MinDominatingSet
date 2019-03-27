package umons.algorithm.dominatingset.heuristics.GeneticAlgo.population;

/**
 *
 */
public abstract class Population {

    public abstract int size();

    public abstract int getIndividualSize();

    public Individual generateRandomIndividual( int size, double p_prob ) {
        Individual individual = new Individual(size, p_prob);
        return individual;
    }


}
