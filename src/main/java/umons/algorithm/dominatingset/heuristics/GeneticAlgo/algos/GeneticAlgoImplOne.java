package umons.algorithm.dominatingset.heuristics.GeneticAlgo.algos;

import com.google.common.collect.BiMap;
import umons.algorithm.dominatingset.graph.Graph;
import umons.algorithm.dominatingset.graph.Result;
import umons.algorithm.dominatingset.heuristics.GeneticAlgo.population.Individual;
import umons.algorithm.dominatingset.heuristics.GeneticAlgo.population.ListPopulation;
import umons.algorithm.dominatingset.util.Stats;

import java.util.*;


/**
 *
 * A first genetic algorithm implementation for solving the minimum dominating set problem
 *
 * This Algo implement the algorithm from the paper
 *
 */
public class GeneticAlgoImplOne extends GeneticAlgorithm {

    private double  p_Heuristic = 0.6;//probability for using heuristic to repair a solution
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
        ListPopulation population = createPopulation(graph, biMap);
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
                   // System.out.println("new best fitness found "+newChild.getFitness());
                    F = newChild.getFitness();
                    best = newChild;
                }
                gen++;
            }
        }while (gen < maxNumGeneration && (knownDominatingNumber == -1  ||  best.getFitness() != knownDominatingNumber));
        double end = System.currentTimeMillis();
        Stats.numberOfGraphs++;
        return new Result(best.mdsFrom(biMap),end-start);
    }

    /**
     *  Repair a solution to be valid.
     *  depending on some probability, the method choose between
     *  a heuristic repair and a random repair.
     *
     *  Repairing an invalid solution means adding more vertices to the solution until
     *  that the mds dominate the graph.
     *
     *
     * @param graph     the graph to dominate.
     * @param biMap     a map that hold pairing values between indiviual indices -> Set values.
     * @param newChild  the child individual that represent an invalid solution.
     *
     * repair the solution found in the newChild individual.
     */
    Set<Integer> repairSolution( Graph graph, BiMap<Integer, Integer> biMap, Individual newChild) {
        Set<Integer> currentDS;
        Random random = new Random();
        if(random.nextDouble() < p_Heuristic)
            currentDS = GeneticAlgoUtil.heuristicRepair(graph, newChild, biMap);
        else
            currentDS = GeneticAlgoUtil.randomRepair(graph, newChild, biMap);
        return currentDS;
    }

    /**
     *  Generate and return a new individual to integrate the population.
     * depending on some probability, the method create a new individual by selection->mutation->crossover
     * or generate a randomly new individual.
     *
     * @param population the curent population
     * @return a new child
     */
    Individual generateChild( ListPopulation population) {
        Random random = new Random();
        Individual newChild;
        if(random.nextDouble() < p_CrossOver)
            newChild = evolve(population);
        else
            newChild = population.generateRandomIndividual(p_Ds);
        return newChild;
    }



    /**
     *  Create and initialise a new population.
     *
     * @param graph     the graph that should be dominated
     * @param biMap     a map that hold pairing values between individual indices -> Set values.
     * @return   a new population.
     */
    private ListPopulation createPopulation( Graph graph, BiMap<Integer, Integer> biMap ) {
        List<Individual> individuals = new ArrayList<>();
        popInitialisation(graph, biMap, individuals);
        return new ListPopulation(individuals);
    }


    /**
     * Whenever this method is called, a new child is created
     * and returned.
     *  2 pools of individuals are created.
     *  each pool contains 2 randomly chosen individuals.
     *
     *  the best individual from each pool is selected as a parent.
     *  the new child is created from those two parents.
     *
     *
     * @param pop   the current population.
     * @return a new child to integrate the population.
     */
    private Individual evolve( ListPopulation pop ) {
        Individual [] pool = new Individual[4];
        Random random = new Random();
        Set<Integer> intSet = new HashSet<>();
        while (intSet.size() < 4) {
            intSet.add(random.nextInt(populationSize));
        }

        Iterator<Integer> it = intSet.iterator();
        int index=0;
        while (it.hasNext()) {
            Integer randomIndex =it.next();
            pool[index++]=pop.getIndividuals().get(randomIndex);
        }
        Individual fittestInPoolOne;
        Individual notFittestInPoolOne;
        if (pool[0].getFitness() < pool[1].getFitness()){
            fittestInPoolOne=pool[0];
            notFittestInPoolOne=pool[1];
        }else{
            fittestInPoolOne=pool[1];
            notFittestInPoolOne=pool[0];
        }

        Individual fittestInPoolTwo;
        Individual notFittestInPoolTwo;
        if (pool[2].getFitness() < pool[3].getFitness()){
            fittestInPoolTwo=pool[2];
            notFittestInPoolTwo=pool[3];
        }else{
            fittestInPoolTwo=pool[3];
            notFittestInPoolTwo=pool[2];
        }
        Individual parentOne;
        Individual parentTwo;
        if (random.nextDouble() > p_Better)
            parentOne = notFittestInPoolOne;
        else
            parentOne = fittestInPoolOne;

        if (random.nextDouble() > p_Better)
            parentTwo = notFittestInPoolTwo;
        else
            parentTwo = fittestInPoolTwo;
        Individual child = crossOver(parentOne, parentTwo);
        applyMutation(child, p_Mutation);
        return child;
    }

}
