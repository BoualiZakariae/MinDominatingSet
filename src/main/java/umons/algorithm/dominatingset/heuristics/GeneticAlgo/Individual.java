package umons.algorithm.dominatingset.heuristics.GeneticAlgo;

import java.util.Arrays;
import java.util.Objects;
import java.util.Random;
import java.util.Set;

/**
 *  This Class represents an individual of the population
 **/
public class Individual {
    /**
     *  Each individual has a size,a fitness value and
     *  a byte array that represent the equivalent mds
     */
    private byte[] genes;
    private int size;
    private int fitness;


    /**
     * Default class constructor
     */
    Individual(){}

    /**
     * Class constructor
     * Create a new individual based on the given parameters
     *
     *
     * @param size the size of the individual
     * @param prob the probability to add a vertex to the dominating Set
     *             in the context of the genomes of an individual,
     *             it is equivalent to making an index equal to 1
     *
     */
    public Individual( int size, double prob) {
        this.size = size;
        this.genes = new byte[size];
        initialisation(prob);
    }

    /**
     *  This method initialize the created individual
     *
     *
     * @param prob
     */
    private void initialisation( double prob ) {
        boolean flag = false;
        while (flag == false) {/* this flag is used to be sure that at least one genome is different than 0*/
            int index = 0;
            double ran;
            for (byte b : genes) {
                ran = new Random().nextDouble();
                if (ran < prob) {
                    flag = true;
                    genes[index] = 1;
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
    }



    /**
     * @return
     */
    public int getFitness() {
        return fitness;
    }

    /**
     * @return the size of this individual
     */
    public int getSize() { return size; }

    /**
     * @return the genes array of this individual
     */
    public byte[] getGenes() { return genes; }

    /**
     * @param index
     * @param b
     */
    public void setAtIndex( int index, byte b ) {
        this.genes[index] = b;
        //  if (this.genes[index] == 1 && b == 0)
        //  fitness--;
        //else if (this.genes[index] == 0 && b == 1)
        //  fitness++;
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
            //   if (this.genes[index] == 0)
            //  this.fitness++;
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
     * @param fitness
     */
    public void setFitness( int fitness ) {
        this.fitness = fitness;
    }

    public void calculateFitness() {
        this.fitness = 0;
        for (byte b : genes) {
            if (b == 1) {
                this.fitness++;
            }
        }
    }
}
