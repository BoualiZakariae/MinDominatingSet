package umons.algorithm.dominatingset.heuristics.GeneticAlgo.algos;

import com.google.common.collect.BiMap;
import umons.algorithm.dominatingset.graph.Graph;
import umons.algorithm.dominatingset.graph.Result;
import umons.algorithm.dominatingset.heuristics.GeneticAlgo.population.Individual;
import umons.algorithm.dominatingset.heuristics.GeneticAlgo.population.ArrayListPopulation;
import umons.algorithm.dominatingset.toDelete.Stats;

import java.util.*;


/**
 *
 * A first genetic algorithm implementation for solving the minimum dominating set problem
 *
 * This Algo implement the algorithm from the paper
 *
 */
public class GeneticAlgoImplOne extends GeneticAlgorithm {

    private double  p_Heuristic = 0.8;//probability for using heuristic to repair a solution
    private double  p_Better = 0.8;//probability for picking the best individual to be selected as a parent

    public GeneticAlgoImplOne() {}

    /**
     *  Class constructor
     *  This constructor give the caller a possibility to choose
     *  the Genetic Algorithm parameters.
     *
     *  The parameters have default values, initialized
     *  whenever the default constructor is called.
     *
     *
     * @param populationSize
     * @param maxNumGeneration
     * @param p_Ds
     * @param p_CrossOver
     * @param p_Mutation
     * @param p_Heuristic
     * @param p_Better
     */
    public GeneticAlgoImplOne( int populationSize , int maxNumGeneration, double p_Ds,
                               double p_CrossOver, double p_Mutation, double p_Heuristic,
                               double  p_Better ) {
        super(populationSize,maxNumGeneration,p_Ds,p_CrossOver,p_Mutation);
        this.p_Heuristic=p_Heuristic;
        this.p_Better=p_Better;
    }



    /**
     * The main genetic algorithm method
     *
     * @param graph the given graph.
     * @param knownDominatingNumber
     *              the known minimum dominating Set size
     *              for the given graph.
     *              When this value equal to -1, it means that the mds
     *              number is unknown for this graph.
     *              Knowing the mds size help to stop the algorithm.
     *
     * @return      the dominating set and the time taken to compute it.
     */
    @Override
    public Result run( Graph graph , int knownDominatingNumber) {
        double start = System.currentTimeMillis();
        BiMap<Integer, Integer> biMap = genesToVerticesMapping(graph);
        ArrayListPopulation population = createPopulation(graph, biMap);
        Individual best = population.getFittest(),newChild;
        int gen=0,F = best.getFitness();
        Set<Integer> currentDS;
        do{
            newChild = generateChild(population);
            currentDS = repairSolution(graph, biMap, newChild);
            GeneticAlgoUtil.minimizeSolution(graph, currentDS);
            newChild.setDS(GeneticAlgoUtil.getBackRealIndices(currentDS,biMap));
            newChild.computeFitness();
            if (population.isUnique(newChild)){
                population.replaceWorstBy(newChild);
                if(newChild.getFitness() < F){
                    System.out.println("new best fitness found "+newChild.getFitness());
                    F = newChild.getFitness();
                    best = newChild;
                }
                gen++;
            }
        }while (gen < maxNumGeneration && (knownDominatingNumber == -1  ||  best.getFitness() != knownDominatingNumber));
        double end = System.currentTimeMillis();
        System.out.println("nombre d'iteration dans lalgo1: "+gen);
        Stats.numberOfGraphs++;
        return new Result(best.mdsFrom(biMap),end-start);
    }

    /**
     *
     * @param graph
     * @param biMap
     * @param newChild
     * @return
     */
    private Set<Integer> repairSolution( Graph graph, BiMap<Integer, Integer> biMap, Individual newChild) {
        Set<Integer> currentDS;
        Random random = new Random();
        if(random.nextDouble() < p_Heuristic)
            currentDS = GeneticAlgoUtil.heuristicRepair(graph, newChild, biMap);
        else
            currentDS = GeneticAlgoUtil.randomRepair(graph, newChild, biMap);
        return currentDS;
    }

    /**
     *
     * @param population
     * @return
     */
    private Individual generateChild( ArrayListPopulation population) {
        Random random = new Random();
        Individual newChild;
        if(random.nextDouble() < p_CrossOver)
            newChild = evolve(population);
        else
            newChild = population.generateRandomIndividual(p_Ds);
        return newChild;
    }

    @Override
    public Result run( Graph graph, int knowDominNumber, Collection<Individual> individuals ) {
        return null;
    }

    /**
     *
     * @param graph
     * @param biMap
     * @return
     */
    private ArrayListPopulation createPopulation( Graph graph, BiMap<Integer, Integer> biMap ) {
        List<Individual> individuals = new ArrayList<>();
        popInitialisation(graph, biMap, individuals);
        return new ArrayListPopulation(individuals);
    }


    /**
     * Whenever this method is called, a new child is created
     * and returned.
     *
     * @param pop
     * @return
     */
    public Individual evolve( ArrayListPopulation pop  ) {
        Individual fittestOne, fittestTwo;
        Random random = new Random();
        if (random.nextDouble() > p_Better)
            fittestOne = pop.getRandomIndividual();
        else
            fittestOne = pop.getFittest();
        if (random.nextDouble() > p_Better)
            fittestTwo = pop.getRandomIndividual(fittestOne);
        else
            fittestTwo = pop.getSecondFittest(fittestOne);
        Individual child = crossOver(fittestOne, fittestTwo);
        applyMutation(child, p_Mutation);
        return child;
    }

}
