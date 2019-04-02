package umons.algorithm.dominatingset.heuristics.GeneticAlgo.population;

import com.google.common.collect.BiMap;

import java.util.*;

/**
 *  This Class represents an individual of the population.
 *
 *  The individuals of a population could be sorted by
 *  their fitness values.
 *  The individual with the min value is the fittest.
 *
 **/
public class Individual implements Comparable<Individual>{

    /**
     *  Each individual has a size,a fitness value and
     *  a byte array that represents the equivalent mds.
     */
    private byte[] genes;
    private int size;
    private int fitness;


    /**
     * Class constructor.
     *
     * Create a new Individual with the given size.
     * @param size  the size of the individual.
     */
    public Individual( int size ) {
        this.size = size;
        this.genes = new byte[size];
    }


    /**
     * Class constructor.
     *
     * Create a new Individual with the given bytes array.
     * @param _genes a bytes array that represent a mds.
     */
    public Individual(byte[] _genes ) {
        this.size = _genes.length;
        this.genes = Arrays.copyOf(_genes,_genes.length);
    }


    /**
     *  Class constructor.
     *  Create a new individual based on the given parameters.
     *
     *
     *  @param size the size of the individual
     *  @param prob the probability to add a vertex to the dominating set.
     *             In the context of the genomes of an individual,
     *             it is equivalent to make a cell equal to 1.
     *
     */
    public Individual( int size, double prob) {
        this.size = size;
        this.genes = new byte[size];
        initialisation(prob);
    }

    /**
     *  This method initialize this individual.
     *
     * @param prob
     */
    private void initialisation( double prob ) {
        boolean flag = false;
        while (flag == false) {/* this flag is used to be sure that at least one genome is different than 0*/
            int index = 0;
            Random random=new Random();
            double ran ;
            while (index < genes.length){
                ran = random.nextDouble();
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
     * @return the fitness value of this individual.
     */
    public int getFitness() {
        return fitness;
    }


    /**
     * @return the size of this individual.
     */
    public int getSize() { return size; }


    /**
     * @return the genes array of this individual.
     */
    public byte[] getGenes() { return genes; }


    /**
     * Edit the genes such that the value at the given index
     * is equal to the given passed value.
     *
     * @param index the index in the bytes array.
     * @param b the new value.
     */
    public void setAtIndex( int index, byte b ) {
        this.genes[index] = b;
    }


    /**
     * Edit the individual such that it represents
     * the dominatingSet passed as parameter
     *
     * @param DS the dominating set that should be represented by this individual.
     *
     */
    public void setDS( Set<Integer> DS) {
        int index =0;
        while (index < this.genes.length)
        {
            if (DS.contains(index))
                this.genes[index++] = 1;
            else
                this.genes[index++] = 0;
        }
    }

    /**
     *
     * @param o
     * @return true if the passed object is equal to this object.
     */
    @Override
    public boolean equals( Object o ) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Individual that = (Individual) o;
        return this.size == that.size &&
                Arrays.equals(this.genes, that.genes);
    }

    /**
     *
     * @return the computed hashCode value of this individual.
     */
    @Override
    public int hashCode() {
        int result = Objects.hash(size);
        result = 31 * result + Arrays.hashCode(genes);
        return result;
    }

    /**
     *
     * @return the String representation of this individual.
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
     *  This method compute the fitness value of this individual.
     *  This value is equal to the dominating set size represented by
     *  this individual.
     *
     * Technically this value is equivalent to the number of 1
     * in the genes array.
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
     * Given a biMap that stores the real values of the vertices,
     * this method return the dominating Set represented by this individual.
     *
     * A biMap was used to allow using graph that doesn't respect
     * the order of vertices.
     *
     * For example Graph g = new Graph(5,2,3),
     * the biMap will have the following data:
     *   keys->values
     *     0-->5
     *     1-->2
     *     2-->3
     *
     *  @param biMap the map that store the real vertices values
     *  @return the dominating Set represented by this individual.
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
     * This method compare this individual to the given individual.
     *
     * @param o the given individual to compare with
     * @return an int value indicating which is the fittest.
     */
    @Override
    public int compareTo( Individual o ) {
        return this.getFitness()-o.getFitness();
    }
}
