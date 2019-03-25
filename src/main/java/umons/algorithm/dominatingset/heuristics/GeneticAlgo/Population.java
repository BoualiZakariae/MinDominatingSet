package umons.algorithm.dominatingset.heuristics.GeneticAlgo;

import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

/**
 * This class represents a population for the genetic algorithm
 *
 * a population is a list of individuals that evolve over each iteration
 *
 */
public class Population {

    private List<Individual> individuals;/*the list of individuals*/
    private double pMutation;
    private double pBetter;


    /**
     * Class constructor
     * Create a population besed on the passed values
     * @param size the size of the population
     * @param individuals the list of individuals
     */
    public Population( int size, List<Individual> individuals ) {
       // this.size = size;
        this.individuals = individuals;
        //this.individualSize = individuals.get(0).getSize();
    }




    /**
     * Class constructor
     * Create a population besed on the passed values
     * @param individuals the list of individuals
     */
    public Population(List<Individual> individuals, double p_Mutation, double p_Better ) {
        this.individuals = individuals;
        this.pMutation = p_Mutation;
        this.pBetter = p_Better;
    }


    /**
     *
     * return the size of the population
     *
     */
    public int size() {
        return this.individuals.size();
    }

    /**
     * To review
     *
     * @param fittest
     * @return the second fittest from this population
     * <p>
     * This individual will be chosen with an amount of probabilitie
     * and should not be the @fittest
     */
    public Individual getSecondFittest( Individual fittest ) {
        double sumOfFitnesses = 0;
        for (Individual individual : individuals) {
            sumOfFitnesses += individual.getFitness();
        }
        // get a random value
        double value = new Random().nextDouble() * sumOfFitnesses;
        double cumulativeSum = 0;
        //double r = new Random().nextDouble();
        for (Individual individual : individuals) {
            value -= individual.getFitness();
            if (value < 0 && !individual.equals(fittest))
                return individual;
        }
        return individuals.get(getSize() - 1);
    }

    /**
     *
     * @param fittest
     * @param prob
     * @param random
     * @return
     */
    public Individual getNewSecondFittest( Individual fittest, double prob, double random ) {
        int randomIndex = new Random().nextInt(individuals.size());
        if (random > prob)
            return individuals.get(randomIndex);
        return individuals.stream()
                .parallel()
                .filter(s -> s.getFitness() <= fittest.getFitness())
                .min(Comparator.comparingInt(object -> object.getFitness()))
                .get();

    }



    /**
     * whenever this method is called, a new child is created
     *
     * @param random
     * @return
     */
    public Individual evolve(double random ) {
        Individual fittestOne = getFittest(random);
        Individual fittestTwo = getNewSecondFittest(fittestOne, pBetter, random);
        Individual child = crossOver(fittestOne, fittestTwo);
        applyMutation(child);
        return child;
    }

    /**
     *
     * @param child
     */
    private void applyMutation( Individual child ) {
        int index = 0;
        double ran;
        while (index < child.getSize()) {
            ran = new Random().nextDouble();
            if (ran < pMutation)
                child.setAtIndex(index, (byte) (1 - child.getGenes()[index]));
            index++;
        }


    }

    /**
     * TO BE REVIEWED
     *
     * @param parentOne
     * @param parentTwo
     * @return
     */
    private Individual crossOver( Individual parentOne, Individual parentTwo ) {
        Individual child = new Individual(parentOne.getSize());
        double probParentOne = (double) parentOne.getFitness() / (parentOne.getFitness() + parentTwo.getFitness());
        // double probParentTwo = parentTwo.getFitness()/(parentOne.getFitness()+parentTwo.getFitness());
        double p_parent;
        int index = 0;
        while (index < parentOne.getSize()) {
            p_parent = new Random().nextDouble();
            if (p_parent < probParentOne)
                child.setAtIndex(index, parentOne.getGenes()[index]);
            else
                child.setAtIndex(index, parentTwo.getGenes()[index]);
            index++;
        }
        return child;
    }

    /**
     *
     * @param size
     * @param p_prob
     * @return
     */
    public Individual generateRandom( int size, double p_prob ) {
        Individual individual = new Individual(size, p_prob);
        return individual;
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
     * @param random
     * @return
     */
    public Individual getFittest(double random ) {
        int randomIndex = new Random().nextInt(individuals.size());
        if (random > this.pBetter)
            return this.individuals.get(randomIndex);
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
    public int getIndividualSize() {
        return individuals.get(0).getSize();
    }

    /**
     *
     * @return
     */
    public List<Individual> getIndividuals() {
        return individuals;
    }


    public int getSize(){
        return this.individuals.size();
    }
}
