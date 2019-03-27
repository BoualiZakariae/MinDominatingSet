package umons.algorithm.dominatingset.heuristics.GeneticAlgo.population;

import com.google.common.collect.BiMap;

import java.util.*;

/**
 *  This Class represents an individual of the population.
 *
 **/
public class Individual implements Comparable<Individual>{
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
    Individual(){this.genes = new byte[1];}

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
     * Class constructor
     * Create a new Individual with the given size
     * @param size
     */
    public Individual( int size ) {
        this.size = size;
        this.genes = new byte[size];
    }



    /**
     * @return the fitness value of this individual
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
     * Edit the genes such that the value at the given index
     * is equal to the given passed value.
     *
     * @param index
     * @param b
     */
    public void setAtIndex( int index, byte b ) {
        this.genes[index] = b;
    }

    /**
     * edit the individual such that it represent
     * the dominatingSet passed as parameter
     *
     * @param DS To be reviewed
     *
     */
    public void setDS( Set<Integer> DS) {
        this.genes = new byte[size];
        for (Integer index : DS) {
             this.genes[index] = 1;
        }
    }

    /**
     *
     * @param o
     * @return true if the passe object is equal to this object.
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
     * @return the computed hashCode value of this individual
     */
    @Override
    public int hashCode() {
        int result = Objects.hash(size);
        result = 31 * result + Arrays.hashCode(genes);
        return result;
    }

    /**
     *
     * @return the String representation of this individual
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
     *  This method compute the fitness value of this Individual.
     *  This value is equal to the dominating Set represented by
     *  this individual.
     *
     * Technically this vakue is equivalent to the number of 1
     * inn the genes array.
     *
     */
    public void computeFitness() {
        this.fitness = 0;
        for (byte b : genes) {
            if (b == 1) {
                this.fitness++;
            }
        }
    }

    /**
     * given a biMap that stores the real values of the vertices,
     * this method return the dominating Set represented by this individual.
     *
     * @param biMap
     *  @return the dominating Set represented by this individual
     */
    public Set<Integer> mdsFrom( BiMap<Integer, Integer> biMap ) {
        Set<Integer> mdsFound = new HashSet<>();
        int index = 0;
        for (byte b : this.genes ) {
            if (b==1)
                mdsFound.add(biMap.get(index));
            index++;
        }
       return  mdsFound;
    }


    /**
     *
     * @param o
     * @return
     */
    @Override
    public int compareTo( Individual o ) {
        return this.getFitness()-o.getFitness();
    }

    /**
     *
     * @param i
     * @return
     */
    public static Individual ofFitness( int i ) {
        Individual individual= new Individual();
        individual.seFitness(i);
        return  individual;
    }

    /**
     *
     * @param _fitness
     */
    private void seFitness( int _fitness ) {
        this.fitness = _fitness;
    }
}
