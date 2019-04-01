package umons.algorithm.dominatingset.heuristics.GeneticAlgo.algos;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import umons.algorithm.dominatingset.graph.Graph;
import umons.algorithm.dominatingset.graph.Result;
import umons.algorithm.dominatingset.heuristics.GeneticAlgo.operations.BitSwapMutation;
import umons.algorithm.dominatingset.heuristics.GeneticAlgo.operations.CrossOverStrategy;
import umons.algorithm.dominatingset.heuristics.GeneticAlgo.operations.MutationStrategy;
import umons.algorithm.dominatingset.heuristics.GeneticAlgo.operations.UniformCrossOver;
import umons.algorithm.dominatingset.heuristics.GeneticAlgo.population.Individual;

import java.util.*;

/**
 *  This abstract class represents the main variables, methods used
 *  by the child class.
 *
 *
 *
 */
public abstract class GeneticAlgorithm {

    /**
     * default values
     */
    int     populationSize = 100;// the size of the population
    int     maxNumGeneration = 10000;//10000;//number of iteration
    double  p_Ds = 0.3;//probability of adding a vertex to a dominating set
    double  p_CrossOver = 0.7;//probability of cross over
    double  p_Mutation = 0.02;//probability for mutation
    CrossOverStrategy crossOverStrategy = new UniformCrossOver();//strategie of crossover
    MutationStrategy mutationStrategy = new BitSwapMutation();

    /**
     *  Default constructor,whenever called the instances
     *  variables get their default values.
     */
    GeneticAlgorithm(){}

    /**
     *  Parametrized constructor
     *
     *  This constructor give the caller the possibility to choose the
     *  the desired parameters.
     *
     * @param populationSize
     * @param maxNumGeneration
     * @param p_ds
     * @param p_crossOver
     * @param p_mutation
     */
    public GeneticAlgorithm( int populationSize, int maxNumGeneration, double p_ds, double p_crossOver, double p_mutation ) {
        this.populationSize = populationSize;
        this.maxNumGeneration = maxNumGeneration;
        this.p_Ds = p_ds;
        this.p_CrossOver = p_crossOver;
        this.p_Mutation = p_mutation;
    }


    /**
     *  Call the genetic algorithm on the given graph instance,
     *  Stop the algorithm whenever the mds size is equal to the given
     *  knowDominNumber.
     *
     * @param graph
     * @param knowDominNumber
     * @return
     */
    public abstract Result run( Graph graph, int knowDominNumber );

    /**
     * Given a collection of individual, run the genetic algorithm
     * to evolve those individuals to a better solution.
     *
     *
     * @param graph
     * @param knowDominNumber
     * @param individuals
     * @return
     */
    public abstract Result run( Graph graph, int knowDominNumber, Collection<Individual> individuals );




    /**
     * Initialisation of the first population
     *
     * @return an initialized population where each individual is a valid dominating Set.
     */
    void popInitialisation( Graph graph, BiMap<Integer, Integer> biMap, Collection<Individual> individuals ) {
        Individual individual;
        int individualSize = graph.size();
        Set<Integer> currentDS;
        for (int i=0; i< this.populationSize; i++){
            individual = new Individual(individualSize, p_Ds);
            currentDS = GeneticAlgoUtil.heuristicRepair(graph, individual, biMap);
            GeneticAlgoUtil.minimizeSolution(graph, currentDS);//minimize the given solution
            individual.setDS(GeneticAlgoUtil.getBackRealIndices(currentDS, biMap));
            individual.computeFitness();
            individuals.add(individual);
        }

    }

    /**
     *  This method do a crossover operation between two child.
     *  the class that implements this abstract class can choose to use another strategy.
     *
     * @param parentOne
     * @param parentTwo
     * @return
     */
    Individual crossOver( Individual parentOne, Individual parentTwo ){
       return this.crossOverStrategy.crossOver(parentOne,parentTwo);
    }

    /**
     *  This method do a mutation operation on the specific individual.
     *  the class that implements this abstract class can override this method.
     *
     *
     * @param child
     * @param p_Mutation
     */
    void applyMutation( Individual child, double p_Mutation ) {
        this.mutationStrategy.applyMutation(child,p_Mutation);
    }


    /**
     * This method define a mapping from genes to the graph vertices.
     * The returned BiMap help later when translating an individual
     * to a dominating set.
     *
     *
     * @param graph the given graph
     * @return the Mapping where each key(gene) point to a value(vertex)
     */
    final BiMap<Integer, Integer> genesToVerticesMapping( Graph graph ) {
        BiMap<Integer, Integer> biMap = HashBiMap.create();
        int index = 0;
        for (Integer key :graph.getAdj().keySet() ) {
            biMap.put(index, key);
            index++;
        }
        return biMap;
    }

    /**
     *
     * @param crossOverStrategy
     */
    public void setCrossOverStrategy( CrossOverStrategy crossOverStrategy ) {
        this.crossOverStrategy = crossOverStrategy;
    }

    /**
     *
     * @return
     */
    public CrossOverStrategy getCrossOverStrategy() {
        return crossOverStrategy;
    }


    public void setMutationStrategy( MutationStrategy mutationStrategy ) {
        this.mutationStrategy = mutationStrategy;
    }

    public MutationStrategy getMutationStrategy() {
        return mutationStrategy;
    }
}
