package umons.algorithm.dominatingset.heuristics.GeneticAlgo;

import com.google.common.collect.BiMap;
import umons.algorithm.dominatingset.exactalgorithm.ArbitraryGraph;
import umons.algorithm.dominatingset.graph.Graph;
import umons.algorithm.dominatingset.graph.Result;
import umons.algorithm.dominatingset.heuristics.Greedy.Greedy;
import com.google.common.collect.HashBiMap;
import umons.algorithm.dominatingset.toDelete.Stats;
import umons.algorithm.dominatingset.util.FileParser;
import umons.algorithm.dominatingset.util.Util;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 *
 * A first genetic algorithm implementation for solving the minimum dominating set problem
 *
 * This Algo implement the algorithm from the paper
 *
 */
public class GeneticAlgoImpl implements GeneticAlgorithm {

    /**
     * default values
     */
    private int     populationSize = 100;// the size of the population
    int     maxNumGeneration = 10000;//10000;//number of iteration
    double  p_Ds = 0.3;//probability of adding a vertex to a dominating set
    double  p_CrossOver = 0.7;//probability of cross over
    double  p_Mutation = 0.02;//probability for mutation
    double  p_Heuristic = 0.8;//probability for using heuristic to repair a solution
    double  p_Better = 0.8;//probability for picking the best individual to be selected as a parent

    public GeneticAlgoImpl() {}

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
    public GeneticAlgoImpl( int populationSize ,int maxNumGeneration, double p_Ds,
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
        List<Individual> individuals = popInitialisation(graph,biMap);
        Population population = new Population(individuals,p_Mutation, p_Better);
        Individual best = individuals.get(0),child;
        int gen=0,F = best.getFitness();
        double random ;
        do{
            random = new Random().nextDouble();
            if(random < p_CrossOver)
                child = population.evolve(random);
            else
                child = population.generateRandom(population.getIndividualSize(), p_Ds);
            Set<Integer> currentDS;
            if(random < p_Heuristic){
                currentDS = repairSolution(graph,child,biMap);
            }
            else{
                currentDS = randomRepair(graph,child,biMap);
            }
            minimizeSolution(graph, currentDS);
            child.setDS(getBackRealIndices(currentDS,biMap));
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
        // printIndividualsSize(population.getIndividuals());
        double end = System.currentTimeMillis();
        Stats.numberOfGraphs++;
        return new Result(best.mdsFrom(biMap),end-start);
    }

    /**
     *  helper method to print individuals fitness
     * @param individuals
     */
    private void printIndividualsSize( List<Individual> individuals ) {
        for (Individual ind:individuals) {
            System.out.print(ind.getFitness()+" ");
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


    /**
     *  Given an dominating Set Solution, try to minimize the ds size by removing
     *  vertices without affecting the domination property
     *
     *  todo: improving the algorithm complexity
     */
    public void minimizeSolution( Graph graph, Set<Integer> currentDS) {
        Iterator<Integer> it = currentDS.iterator();
        Set<Integer> newDS;
        while (it.hasNext()){
            int v = it.next();
            newDS = new HashSet<>(currentDS);
            newDS.remove(v);
            Set<Integer> neighbors = graph.getClosedNeighbors(v);
            if(ArbitraryGraph.isDominating(graph,newDS,neighbors)){
                it.remove();
            }
        }
    }


    /**
     *
     * @param graph
     * @param child
     * @param map
     * @return
     */
    Set<Integer> randomRepair( Graph graph, Individual child, BiMap<Integer, Integer> map ) {
        Set<Integer> currentDS      = new HashSet<>();
        Set<Integer> setToDominate  = new HashSet<>();
        for (Integer key :graph.getAdj().keySet() ) {
            setToDominate.add(key);
            if(child.getGenes()[map.inverse().get(key)]==1)
                currentDS.add(key);
        }
        boolean dominated = false;
        int ran ;
        while (!dominated){
            do{
                ran = new Random().nextInt(child.getSize());
              }
            while (currentDS.contains(map.get(ran)));
            currentDS.add(map.get(ran));
            Set<Integer> remainingSet = Util.remainingSet(graph,currentDS,setToDominate);
            if (remainingSet.size()==0)
                dominated=true ;
        }
        return currentDS ;
    }

    /**
     *
     * @param individual
     * @return
     *
     * Heuristic repair
     */
    Set<Integer> repairSolution( Graph graph, Individual individual, BiMap<Integer, Integer> map ) {
        Set<Integer> currentDS      = new HashSet<>();
        Set<Integer> setToDominate  = new HashSet<>();
        for (Integer key :graph.getAdj().keySet()) {
            setToDominate.add(key);
            if(individual.getGenes()[map.inverse().get(key)]==1)
                currentDS.add(key);
        }
        Set<Integer> remainingSet = Util.remainingSet(graph,currentDS,setToDominate);
        if (remainingSet.size()==0)
            return currentDS ;
        Set<Integer> graphVertices = new HashSet<>(graph.getAdj().keySet());
        Set<Integer> dominatedVertices = Util.setMinus(graphVertices,remainingSet);
        Graph nonDominatedSubGraph = graph.removeVertex(dominatedVertices);
        Set<Integer> mdsOfRemainingSet = new Greedy().run(nonDominatedSubGraph).getMds();
        currentDS.addAll(mdsOfRemainingSet);
        return currentDS ;
    }

    /**
     *
     * @param DS
     * @param map
     * @return
     */
    Set<Integer> getBackRealIndices( Set<Integer> DS, BiMap<Integer, Integer> map ) {
        Set<Integer> realIndices = new HashSet<>();
        for (Integer b: DS) {
            realIndices.add(map.inverse().get(b));
        }
        return realIndices;
    }

    /**
     * Initialisation of the first population
     *
     * @return an initialized population where each individual is a valid dominating Set.
     */
    List<Individual> popInitialisation( Graph graph, BiMap<Integer, Integer> biMap ) {
        List<Individual> individuals = new ArrayList<>();
        Individual individual;
        int individualSize = graph.size();
        Set<Integer> currentDS;
        for (int i=0; i< this.populationSize; i++){
            individual = new Individual(individualSize, p_Ds);
            currentDS = repairSolution(graph, individual, biMap);
            minimizeSolution(graph, currentDS);//minimize the given solution
            individual.setDS(getBackRealIndices(currentDS, biMap));
            individual.computeFitness();
            individuals.add(individual);
        }
        return individuals;
    }

    /**
     *
     */
    static Map<String,Integer> hugesGraphs = new HashMap<>();
    static {
           hugesGraphs.put("gplus_200.col",19);
           hugesGraphs.put("gplus_500.col",42);
           hugesGraphs.put("gplus_2000.col",170);
           hugesGraphs.put("pokec_500.col",16);
           hugesGraphs.put("pokec_2000.col",75);
    }

    /**
     * Testing the genetic algo for statistics results
     * @param args
     */
    public static void main( String[] args ) throws IOException {
        System.out.println("geneticAlgo");
        Iterator<Map.Entry<String, Integer>> it = hugesGraphs.entrySet().iterator();
        while (it.hasNext())
        {
            Map.Entry<String, Integer> pair = it.next();
            String grapheName =  pair.getKey();
            int knownMdsSize  =  pair.getValue();
            System.out.println(grapheName+" "+knownMdsSize);
            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            File file = new File(classloader.getResource(grapheName).getFile());
            Graph hugeGraph = FileParser.createGraphFromDimacsFormat(file);
            GeneticAlgorithm heuristic = new GeneticAlgoImpl();
            Result mds = heuristic.run(hugeGraph,knownMdsSize);

            System.out.println(mds.getMds().size()+" in "+(mds.getTime())+" sec");
            System.out.println();
        }
    }
}
