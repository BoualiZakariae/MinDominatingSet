package umons.algorithm.dominatingset.heuristics.GeneticAlgo.algos;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import umons.algorithm.dominatingset.graph.Graph;
import umons.algorithm.dominatingset.graph.Result;
import umons.algorithm.dominatingset.heuristics.GeneticAlgo.population.Individual;

import java.util.*;

/**
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





    /**
     *
     * @param graph
     * @param knowDominNumber
     * @return
     */
    public abstract Result run( Graph graph, int knowDominNumber );


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
            currentDS = GeneticAlgoUtil.repairSolution(graph, individual, biMap);
            GeneticAlgoUtil.minimizeSolution(graph, currentDS);//minimize the given solution
            individual.setDS(GeneticAlgoUtil.getBackRealIndices(currentDS, biMap));
            individual.computeFitness();
            individuals.add(individual);
        }

    }

    /**
     *
     * @param parentOne
     * @param parentTwo
     * @return
     */
    Individual crossOver( Individual parentOne, Individual parentTwo ){
        Individual child = new Individual(parentOne.getSize());
        double probParentOne = (double) parentOne.getFitness() / (parentOne.getFitness() + parentTwo.getFitness());
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
     * @param child
     * @param p_Mutation
     */
    void applyMutation( Individual child, double p_Mutation ) {
        int index = 0;
        double ran;
        while (index < child.getSize()) {
            ran = new Random().nextDouble();
            if (ran < p_Mutation)
                child.setAtIndex(index, (byte) (1 - child.getGenes()[index]));
            index++;
        }
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
    BiMap<Integer, Integer> genesToVerticesMapping( Graph graph ) {
        BiMap<Integer, Integer> biMap = HashBiMap.create();
        int index = 0;
        for (Integer key :graph.getAdj().keySet() ) {
            biMap.put(index, key);
            index++;
        }
        return biMap;
    }

}
