package umons.algorithm.dominatingset.heuristics.GeneticAlgo;

import umons.algorithm.dominatingset.util.Util;

import java.util.Arrays;
import java.util.Objects;
import java.util.Random;
import java.util.Set;

/**
 *  This Class represents an individual of the population
 **/
public class Individual {
    /**
     *  each individual has a size,a fitness value and
     *  a byte array that represent the mds
     */
    private byte[] genes;
    private int size;
    private int fitness;


    /**
     * Class constructor
     */
    Individual(){

    }

    /**
     * Class constructor
     * @param size
     * @param prob
     */
    public Individual( int size, double prob ) {
        this.size = size;
        this.genes = new byte[size];
        double ran;
        boolean flag = false;
        while (flag == false) {
            int index = 0;
            this.fitness = 0;
            for (byte b : genes) {
                ran = new Random().nextDouble();
                if (ran < prob) {
                    flag = true;
                    genes[index] = 1;
                    this.fitness++;
                } else
                    genes[index] = 0;
                index++;
            }
        }

    }


    /**
     * @param size
     */
    public Individual( int size ) {
        this.size = size;
        this.genes = new byte[size];
        this.fitness = 0;
    }

    /**
     * @return
     */
    public int getFitness() {
        if (genes==null)
            return this.fitness;
        int fitness0 = 0;
        for (byte b : genes) {
            if (b == 1) {
                fitness0++;
            }
        }
        return fitness0;
    }

    /**
     * @return
     */
    public int getSize() {
        return size;
    }

    /**
     * @return
     */
    public byte[] getGenes() {
        return genes;
    }

    /**
     * @param index
     * @param b
     */
    public void setAtIndex( int index, byte b ) {
        this.genes[index] = b;
        if (this.genes[index] == 1 && b == 0)
            fitness--;
        else if (this.genes[index] == 0 && b == 1)
            fitness++;
    }

    /**
     * edit the individual such that it represent
     * the dominatingSet passed as parameter
     *
     * @param DS To be reviewed
     *
     */
    public void setDS( Set<Integer> DS ) {
        this.fitness = 0;
        for (Integer index : DS) {
            if (this.genes[index] == 0)
                this.fitness++;
            this.genes[index] = 1;
        }
    }

    /**
     *
     * @param o
     * @return
     */
    @Override
    public boolean equals( Object o ) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Individual that = (Individual) o;
        return size == that.size &&
                Arrays.equals(genes, that.genes);
    }

    /**
     *
     * @return
     */
    @Override
    public int hashCode() {

        int result = Objects.hash(size);
        result = 31 * result + Arrays.hashCode(genes);
        return result;
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append(this.getGenes()[0]);
        for (int i = 1; i < this.getGenes().length; i++)
            str.append(",").append(this.getGenes()[i]);
        return str.toString();
    }

    /**
     *
     */
    public void calculateFitness() {
        this.fitness = 0;
        for (byte b : genes) {
            if (b == 1)
                this.fitness++;
        }
    }

    /**
     *
     * @param fitness
     */
    public void setFitness( int fitness ) {
        this.fitness = fitness;
    }
}
