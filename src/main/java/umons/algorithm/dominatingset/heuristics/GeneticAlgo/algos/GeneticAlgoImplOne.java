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
        this.populationSize = populationSize;
        this.maxNumGeneration=maxNumGeneration;
        this.p_Ds= p_Ds;
        this.p_CrossOver=p_CrossOver;
        this.p_Mutation=p_Mutation;
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
        List<Individual> individuals = new ArrayList<>();
        popInitialisation(graph, biMap, individuals);
        ArrayListPopulation population = new ArrayListPopulation(individuals);
        Individual best = individuals.get(0),child;
        int gen=0,F = best.getFitness();
        double random ;
        do{
            random = new Random().nextDouble();
            if(random < p_CrossOver)
                child = evolve(population,random);
            else
                child = population.generateRandomIndividual(population.getIndividualSize(), p_Ds);
            Set<Integer> currentDS;
            if(random < p_Heuristic){
                currentDS = GeneticAlgoUtil.repairSolution(graph,child,biMap);
            }
            else{
                currentDS = GeneticAlgoUtil.randomRepair(graph,child,biMap);
            }
            GeneticAlgoUtil.minimizeSolution(graph, currentDS);
            child.setDS(GeneticAlgoUtil.getBackRealIndices(currentDS,biMap));
            child.computeFitness();
            if (population.isUnique(child)){
                population.replaceWorstBy(child);
                if(child.getFitness()<F){
                    System.out.println("best fitness found "+child.getFitness());
                    F = child.getFitness();
                    best = child;
                }
                gen++;
            }
        }while (gen < maxNumGeneration && (knownDominatingNumber == -1  ||  best.getFitness() != knownDominatingNumber));
        double end = System.currentTimeMillis();
        Stats.numberOfGraphs++;
        return new Result(best.mdsFrom(biMap),end-start);
    }


    /**
     * Whenever this method is called, a new child is created
     *
     * @param pop
     * @param random
     * @return
     */
    public Individual evolve( ArrayListPopulation pop , double random ) {
        Individual fittestOne = pop.getFittest(random, p_Better);
        Individual fittestTwo = pop.getSecondFittest(fittestOne, p_Better, random);
        Individual child = crossOver(fittestOne, fittestTwo);
        applyMutation(child, p_Mutation);
        return child;
    }

}
