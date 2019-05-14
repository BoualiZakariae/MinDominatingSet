package umons.algorithm.dominatingset.exactalgorithm;

import umons.algorithm.dominatingset.graph.Graph;
import umons.algorithm.dominatingset.graph.Result;


/**
 *
 * This Interface define the method that
 *
 * should be implemented by all the mds algorithms
 *
 */
public interface mdsAlgorithm {


    /**
     * run the exact algorithm on the specified graph instance
     * and return a Result object that contains the solution.
     *
     *
     * @param graph the graph instance
     * @return      a Result object that contains the solution
     *
     */
    Result run( Graph graph );




}
