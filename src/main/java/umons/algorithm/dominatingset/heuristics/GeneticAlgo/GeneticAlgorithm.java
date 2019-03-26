package umons.algorithm.dominatingset.heuristics.GeneticAlgo;

import umons.algorithm.dominatingset.graph.Graph;
import umons.algorithm.dominatingset.graph.Result;

public interface GeneticAlgorithm {

    /**
     *
     * @param graph
     * @param knowDominNumber
     * @return
     */
    Result run( Graph graph, int knowDominNumber );
}
