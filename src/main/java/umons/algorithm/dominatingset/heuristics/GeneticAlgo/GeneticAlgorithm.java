package umons.algorithm.dominatingset.heuristics.GeneticAlgo;

import com.google.common.collect.BiMap;
import umons.algorithm.dominatingset.exactalgorithm.ArbitraryGraph;
import umons.algorithm.dominatingset.exactalgorithm.mdsAlgorithm;
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

public class GeneticAlgorithm  implements mdsAlgorithm {

    public static final int     POPULATION_SIZE = 100;//100
    public static final int     MAX_GEN = 10000;//10000;//number of iteration
    public static final double  P_DS = 0.3;
    public static final double  P_CROSSOVER = 0.7;//probability of cross over
    public static final double  P_MUTATION = 0.02;//probability for mutation
    public static final double  P_H = 0.8;
    public static final double  P_BETTER = 0.8;


    /**
     * not implemented
     * @param graph the graph instance
     * @return
     */
    public Result run( Graph graph) {
        double start = System.currentTimeMillis();
        BiMap<Integer, Integer> biMap = genes_verticesMap(graph);//mapping genes<--->verticies
        Population population = new Population(POPULATION_SIZE,popInitialisation(graph.size(),biMap,graph));
        double random ;
        Individual best = new Individual(),child;
        best.setFitness(Integer.MAX_VALUE);
        int gen=0,F = Integer.MAX_VALUE,uniqueness=0,theSame=0;
        while ( gen<MAX_GEN )
        {
            printIndividualsSize(population.getIndividuals());
            random = new Random().nextDouble();
            if(random < P_CROSSOVER){
                child = population.evolve(P_MUTATION,P_BETTER,random);
            }
            else{
                child = population.generateRandom(population.getIndividualSize(), P_DS);
            }
            Set<Integer> currentDS;
            if(random < P_H){
                currentDS = repairSolution(graph,child,biMap);
            }
            else{
                currentDS = randomRepair(graph,child,biMap);
            }
            minimizeSolution(graph,child,biMap,currentDS);//should be reviewed
            if (population.isUnique(child)){
                uniqueness++;
                population.replaceWorstBy(child);
                if(child.getFitness()<F){
                    System.out.println("best fitness found "+child.getFitness());
                    F = child.getFitness();
                    best = child;
                }
                gen++;
            }else{
                theSame++;
            }
        }
        //System.out.println("uniqueness "+ uniqueness);
        printIndividualsSize(population.getIndividuals());
        double end = System.currentTimeMillis();
        Stats.numberOfGraphs++;
        return new Result(mdsFrom(graph,best,biMap),end-start);    }

    /**
     *
     * @param graph
     * @param knownDominatingNumber
     * @return
     */
    @Override
    public Result run( Graph graph , int knownDominatingNumber) {
        double start = System.currentTimeMillis();
        BiMap<Integer, Integer> biMap = genes_verticesMap(graph);//mapping genes<--->verticies
        Population population = new Population(POPULATION_SIZE,popInitialisation(graph.size(),biMap,graph));
        double random ;
        Individual best = new Individual(),child;
        best.setFitness(Integer.MAX_VALUE);
        int gen=0,F = Integer.MAX_VALUE,uniqueness=0,theSame=0;
        while ( gen<MAX_GEN  && best.getFitness()!= knownDominatingNumber)
        {
            printIndividualsSize(population.getIndividuals());
            random = new Random().nextDouble();
            if(random < P_CROSSOVER){
                child = population.evolve(P_MUTATION,P_BETTER,random);
            }
            else{
                child = population.generateRandom(population.getIndividualSize(), P_DS);
            }
            Set<Integer> currentDS;
            if(random < P_H){
                currentDS = repairSolution(graph,child,biMap);
            }
            else{
                currentDS = randomRepair(graph,child,biMap);
            }
            minimizeSolution(graph,child,biMap,currentDS);//should be reviewed
            if (population.isUnique(child)){
                uniqueness++;
                population.replaceWorstBy(child);
                if(child.getFitness()<F){
                    System.out.println("best fitness found "+child.getFitness());
                    F = child.getFitness();
                    best = child;
                }
                gen++;
            }else{
                theSame++;
            }
        }
        //System.out.println("uniqueness "+ uniqueness);
        printIndividualsSize(population.getIndividuals());
        double end = System.currentTimeMillis();
        Stats.numberOfGraphs++;
        return new Result(mdsFrom(graph,best,biMap),end-start);

    }

    /**
     *
     * @param individuals
     */
    private void printIndividualsSize( List<Individual> individuals ) {
        for (Individual ind:individuals) {
           // System.out.print(ind.getFitness()+" ");
        }
        //System.out.println();
    }

    /**
     * mapping genes to verticies
     * @param graph
     * @return
     */
    private BiMap<Integer, Integer> genes_verticesMap( Graph graph ) {
        BiMap<Integer, Integer> biMap = HashBiMap.create();
        int index = 0;
        for (Integer key :graph.getAdj().keySet() ) {
            biMap.put(index,key);
            index++;
        }
        return biMap;
    }

    /**
     *
     *
     * @param best
     * @return
     */
    private Set<Integer> mdsFrom( Graph g, Individual best, BiMap<Integer, Integer> biMap) {
        Set<Integer> mdsFound = new HashSet<>();
        int index = 0;
        for (byte b :best.getGenes() ) {
            if (b==1)
                mdsFound.add(biMap.get(index));
            index++;
        }
       return  mdsFound;
    }

    /**
     *
     * @param child
     */
    private void minimizeSolution( Graph graph,Individual child, BiMap<Integer, Integer> map, Set<Integer> currentDS) {
        int index=0;
        int counter = 0;
        for (byte b :child.getGenes() ) {
            if (b==1)
            {
                Set<Integer> newDS = new HashSet<>(currentDS);
                newDS.remove(map.get(index));
                Set<Integer> neighbors = graph.getClosedNeighbors(map.get(index));
                if(ArbitraryGraph.isDominating(graph,newDS,neighbors)){
                    counter++;
                    currentDS.remove(map.get(index));
                    child.setAtIndex(index,(byte)0);
                }
            }
            index++;
        }
    }

    /**
     *
     * @param graph
     * @param child
     * @param map
     * @return
     */
    private Set<Integer> randomRepair( Graph graph,Individual child,BiMap<Integer, Integer> map  ) {
        Set<Integer> currentDS      = new HashSet<>();
        Set<Integer> setToDominate  = new HashSet<>();
        int index = 0;
        for (Integer key :graph.getAdj().keySet() ) {
            setToDominate.add(key);
            if(child.getGenes()[map.inverse().get(key)]==1)
                currentDS.add(key);
            index++;
        }
        boolean dominated = false;
        int ran ;
        while (!dominated){
            do{
                ran = new Random().nextInt(child.getSize());
              }
            while (currentDS.contains(map.get(ran)));
            currentDS.add(map.get(ran));
            Set<Integer> remainingSet =ArbitraryGraph.remainingSet(graph,currentDS,setToDominate);
            if (remainingSet.size()==0)
                dominated=true ;
        }
        child.setDS(getBackRealIndices(currentDS,map));
        return currentDS ;
    }

    /**
     *
     * @param child
     * @return
     *
     * Heuristic repair
     */
    private Set<Integer> repairSolution( Graph graph,Individual child,BiMap<Integer, Integer> map  ) {
        Set<Integer> currentDS      = new HashSet<>();
        Set<Integer> setToDominate  = new HashSet<>();
        int index = 0;
        for (Integer key :graph.getAdj().keySet() ) {
            setToDominate.add(key);
            if(child.getGenes()[map.inverse().get(key)]==1)
                currentDS.add(key);
            index++;
        }
        Set<Integer> remainingSet =ArbitraryGraph.remainingSet(graph,currentDS,setToDominate);
        if (remainingSet.size()==0)
            return currentDS ;
       // System.out.println("we should repair");
        Set<Integer> graphVertices = new HashSet<>(graph.getAdj().keySet());
        Set<Integer> verticesToremove = Util.setMinus(graphVertices,remainingSet);
        Graph nonDominatedVerticesgraph = graph.removeVertex(verticesToremove);
        Set<Integer> mdsOfRemainingSet = new Greedy().run(nonDominatedVerticesgraph).getMds();
        currentDS.addAll(mdsOfRemainingSet);
        child.setDS(getBackRealIndices(currentDS,map));
        return currentDS ;
    }

    /**
     *
     * @param DS
     * @param map
     * @return
     */
    private Set<Integer> getBackRealIndices( Set<Integer> DS, BiMap<Integer, Integer> map ) {
        Set<Integer> realIndices = new HashSet<>();
        for (Integer b: DS) {
            realIndices.add(map.inverse().get(b));
        }
        return realIndices;
    }

    /**
     * initialisation of the first population
     * @param individualSize
     * @return
     */
    private List<Individual> popInitialisation( int individualSize,BiMap<Integer, Integer> biMap ,Graph graph) {
        List<Individual> individuals = new ArrayList<>();
        Individual individual;
        for (int i=0; i<POPULATION_SIZE;i++){
           // System.out.print(i+" individu");
            individual = new Individual(individualSize, P_DS);
            Set<Integer> currentDS;
          //  System.out.print("repair  ");
            currentDS = repairSolution(graph,individual,biMap);
            //System.out.println(" minimise");
            minimizeSolution(graph,individual,biMap,currentDS);//should be reviewed
            individuals.add(individual);
        }
        // System.out.println("initialisation finished");
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
            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            File file = new File(classloader.getResource(grapheName).getFile());
            Graph hugeGraph = FileParser.createGraphFromDimacsFormat(file);

            double start = System.currentTimeMillis();
            mdsAlgorithm heuristic = new GeneticAlgorithm();
            Set<Integer> mds = heuristic.run(hugeGraph,knownMdsSize).getMds();
            double end = System.currentTimeMillis();
            System.out.println(mds.size()+" in "+((end-start)/1000)+" sec");
            System.out.println();
        }
    }
}
