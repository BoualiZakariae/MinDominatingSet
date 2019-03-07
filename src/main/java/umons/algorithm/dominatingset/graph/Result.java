package umons.algorithm.dominatingset.graph;

import java.util.Set;

/**
 * A result object represents a solution
 *
 * that contains the minimum dominating set
 *
 * and the time taken to compute this mds.
 *
 */
public class Result {

    private Set<Integer> mds ;
    private double time;

    public Result(Set<Integer> mds, double time){
        this.mds  = mds;
        this.time = time;
    }

    /**
     * @return the time taken to compute the {@code mds}
     **/
    public double getTime() {
        return time;
    }

    /**
     *
     * @return the minimum dominating set
     */
    public Set<Integer> getMds() {
        return mds;
    }

    @Override
    public String toString() {
        return "the MDS size is : "+mds.size()+" found in : "+time/1000+" sec";
    }
}
