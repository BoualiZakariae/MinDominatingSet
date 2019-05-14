package umons.algorithm.dominatingset.heuristics.GeneticAlgo.algos;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import umons.algorithm.dominatingset.graph.Graph;
import umons.algorithm.dominatingset.graph.Result;
import umons.algorithm.dominatingset.heuristics.GeneticAlgo.operations.BitFlipMutation;
import umons.algorithm.dominatingset.heuristics.GeneticAlgo.operations.CrossOverStrategy;
import umons.algorithm.dominatingset.heuristics.GeneticAlgo.operations.MutationStrategy;
import umons.algorithm.dominatingset.heuristics.GeneticAlgo.operations.UniformCrossOver;
import umons.algorithm.dominatingset.heuristics.GeneticAlgo.population.Individual;

import java.util.Collection;
import java.util.Set;

/**
 *  This abstract class defines the defaults parameters of the genetic algorithm,
 *  and the methods that should be implemented by the concrete classes.
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
    private CrossOverStrategy crossOverStrategy = new UniformCrossOver();//default crossover strategy
    private MutationStrategy mutationStrategy = new BitFlipMutation();//default mutation strategy

    /**
     *  Default constructor, whenever called the instances
     *  variables get their default values.
     */
    GeneticAlgorithm(){}

    /**
     *  Parametrized constructor
     *
     *  This constructor give the caller the possibility to choose
     *  the desired parameters.
     *
     * @param populationSize    the population size.
     * @param maxNumGeneration  the number of iterations of the genetic algorithm.
     * @param p_ds              the probability rate to add a vertex to the dominating Set.
     * @param p_crossOver       the probability rate to do a crossover operation on a given iteration.
     * @param p_mutation        the probability rate to do a mutation operation on a given iteration.
     */
    GeneticAlgorithm( int populationSize, int maxNumGeneration, double p_ds, double p_crossOver, double p_mutation ) {
        this.populationSize = populationSize;
        this.maxNumGeneration = maxNumGeneration;
        this.p_Ds = p_ds;
        this.p_CrossOver = p_crossOver;
        this.p_Mutation = p_mutation;
    }


    /**
     *  Call the genetic algorithm on the given graph instance,
     *  stop the algorithm whenever the mds size is equal to the given
     *  knowDominNumber value.
     *
     * @param graph             the graph instance on which this genetic algorithm will be applied.
     * @param knowDominNumber   the minimum dominating set size of the given graph instance.
     * @return                  a Result object containing the mds set and the time taken by this algorithm to compute it.
     */
    public abstract Result run( Graph graph, int knowDominNumber );


    /**
     * todo : Given a collection of individual, run the genetic algorithm
     * to evolve those individuals for a better solutions.
     *
     *
     * @param graph
     * @param knowDominNumber
     * @param individuals
     * @return
     */
    public abstract Result run( Graph graph, int knowDominNumber, Collection<Individual> individuals );


    /**
     * Initialisation of the first population.
     * When the collection of individuals is empty, this method
     * create a population of individuals with size equal to the populationSize value defined in this class.
     * As the collection could be not empty, meaning that it has already some individuals, this method add more
     * individuals to reach the populationSize value .
     *
     * @return an initialized population where each individual is a valid dominating Set.
     */
    void popInitialisation( Graph graph, BiMap<Integer, Integer> biMap, Collection<Individual> individuals ) {
        int remainingIndividuals = getRemainingIndividualsSize(individuals);
        Individual individual;
        Set<Integer> currentDS;
        for (int i=0; i< remainingIndividuals; i++){
            individual = new Individual(graph.size(), p_Ds);
            currentDS = GeneticAlgoUtil.heuristicRepair(graph, individual, biMap);
            GeneticAlgoUtil.minimizeSolution(graph, currentDS);//minimize the given solution
            individual.setDS(GeneticAlgoUtil.getBackRealIndices(currentDS, biMap));
            individual.computeFitness();
            individuals.add(individual);
        }

    }

    /**
     *
     * @param individuals
     * @return
     */
    private int getRemainingIndividualsSize( Collection<Individual> individuals ) {
        int remainingIndividuals = this.populationSize;
        if(individuals.size()!=0)
        {
            remainingIndividuals -= individuals.size();
        }
        return remainingIndividuals;
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
     * @param pMutation
     */
    void applyMutation( Individual child, double pMutation ) {
        this.mutationStrategy.applyMutation(child,pMutation);
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
