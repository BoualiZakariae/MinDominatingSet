package umons.algorithm.dominatingset.heuristics.GeneticAlgo;

import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

/**
 *
 */
public class Population {

    private int size;
    private List<Individual> individuals;
    private int individualSize;


    /**
     * @param size
     * @param individuals
     */
    public Population( int size, List<Individual> individuals ) {
        this.size = size;
        this.individuals = individuals;
        this.individualSize = individuals.get(0).getSize();
    }

    public Population( int size ) {
        this.size = size;
    }

    public int size() {
        return this.size;
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
        return individuals.get(size - 1);
    }

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




    public Individual evolve( double pMutation, double prob, double random ) {
        Individual fittestOne = getFittest(prob, random);
        Individual fittestTwo = getNewSecondFittest(fittestOne, prob, random);
        Individual child = crossOver(fittestOne, fittestTwo);
        applyMutation(child, pMutation);
        // child.calculateFitness();
        return child;
    }

    private void applyMutation( Individual child, double pMutation ) {
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

    public Individual generateRandom( int size, double p_prob ) {
        Individual individual = new Individual(size, p_prob);
        return individual;
    }

    public boolean isUnique( Individual child ) {
        return !this.individuals.contains(child);
    }

    public void replaceWorstBy( Individual child ) {
        int worstIndividualIndex = IntStream.range(0, individuals.size())
                .boxed()
                .parallel()//to be deleted
                .max(Comparator.comparingInt(i -> individuals.get(i).getFitness()))
                .get();
        this.individuals.set(worstIndividualIndex, child);

    }

    /**
     * @param prob
     * @param random
     * @return
     */
    public Individual getFittest( double prob, double random ) {
        int randomIndex = new Random().nextInt(individuals.size());
        if (random > prob)
            return individuals.get(randomIndex);
        return individuals.stream()
                .parallel()//to be deleted
                .min(Comparator.comparingInt(indiv -> indiv.getFitness()))
                .get();
    }

    public int getIndividualSize() {
        return individualSize;
    }

    public List<Individual> getIndividuals() {
        return individuals;
    }
}
