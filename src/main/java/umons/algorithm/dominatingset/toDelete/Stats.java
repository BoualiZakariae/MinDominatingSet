package umons.algorithm.dominatingset.toDelete;

import umons.algorithm.dominatingset.graph.Graph;

import java.util.HashMap;
import java.util.Iterator;

public class Stats {


    public static long numberOfGraphs = 0;
    public static long incorrectResult = 0;
    public static long correctResult = 0;
    private static double fullTime; //fullTime of the instances processing
    private static HashMap<Integer,double[]> statsMap = new HashMap<>();// ordre->total(mdsSize,time,numberOfgraphs)


    public static void addStatistic( Graph g, double mdsSize,double time ) {
        int ordre = g.size();
        if (statsMap.get(ordre)==null){
            statsMap.put(ordre,new double[3]);
        }
        double[] array = statsMap.get(ordre);
        array[0]+=mdsSize;
        array[1]+=time;
        array[2]++;
        fullTime+=time;
    }

    public static void prinFullStats() {
        System.out.println("number of  instance: " + Stats.numberOfGraphs + " with inorrect results: " + Stats.incorrectResult);
        System.out.println("full Time in second"+ fullTime/1000);

        Iterator it = statsMap.keySet().iterator();
        while (it.hasNext())
        {
            Integer key = (Integer) it.next();
            System.out.println(key + " with "+statsMap.get(key)[2]+" graphs and the  full mds size is "+ statsMap.get(key)[0]+ " in "+statsMap.get(key)[1]);

        }
    }

    public static double getFullTime() {
        return fullTime;
    }


}
