This Java project contains some implementations of the Minimum Dominanting Set Algorithms

There are 4 packages:

1-exactalgorithm: contains the exacts solutions algorithms.

2-graph: for the data structure representation of the graph.

3-heuristics: contains the classes responsible for the heuristics solutions.

4-util: some utilities classes used in this project.

    /*
     * @param args
     *
     *
     *
     * To run this program, you should pass as parameters:
     *  + the algorithm type.
     *  + the path of the file that contains the graph(graphs if trinagleup file format).
     *  + the type of the passed file.
     * As a result, the program will print the adjacency list of the graph, the mds size found  and the time taken in seconds.
     *
     *
     * Four parameters can be given to the program: -algo, -path, -type, -mds.
     * three parameters are mandatory: -algo, -path, -type.
     * one parameter is optional -mds (used only when running the Genetic algorithm)
     *
     *      - algo
     * - algo 1 ------> to run the Arbitrary algorithm
     * - algo 2 ------> to run the atmostDegree3 algorithm
     * - algo 3 ------> to run the trivialSetCover algorithm
     * - algo 4 ------> to run the improvedSetCover algorithm
     * - algo 5 ------> to run the greedy algorithm
     * - algo 6 ------> to run the GreedyReverse algorithm
     * - algo 7 ------> to run the GreedyRandom algorithm
     * - algo 8 ------> to run the GeneticAlgo1 algorithm
     * - algo 9 ------> to run the GeneticAlgo2 algorithm
     *
     *
     *      - path
     * - path  "filepath/file.txt" ------> to pass the location of the file.txt as a path value to the program.
     *
     *
     *
     *      - type
     * - type 1 ------> means that the file passed to the program is of type triangleup.
     * - type 2 ------> means that the file passed to the program is of type dimacs.
     * - type 3 ------> means that the file passed to the program is of type HouseofGraphs.
     *
     * Only the triangleup file format could contains many graphs.
     * The two others files format should contains only one graph per file.
     *
     * Please refer to src\main\resources to see the 3 supported files type format(triangleup, dimacs, HousOfGraphs)
     * This program can run any implemented algorithms on any supported graphs file.
     *
     *
     *      - mds
     *  When you choose to run the genetic Algorithm on a (dimacs file format or a houseofgraphs file format)
     *  it is possible to pass the known minimum dominating set size as a parameter by :
     *
     * - mds s ------> where s is an int value representing the known minimum dominating set.
     *                 the Genetic algorithm can stop running after reaching this optimal value.
     *
     *
     *
     * given mds.jar as an executable file, you can run it with as the following examples:
     *
     * Example_1 :
     * java -jar mds.jar -algo 5 -path "c:Windows\Desktop\file.col" -type 2
     * this command line run the greedy algorithm on the dimacs file located in "c:Windows\Desktop\file.col"
     *
     * Example_2 :
     * java -jar mds.jar -algo 8 -path "c:Windows\Desktop\filename.lst" -type 3 -mds 4
     * this command line run the genetic algorithm on the houseofgraphs file format located in c:Windows\Desktop\filename.lst
     * the program will stop running whenever finding a dominating set of size 4.
     *
     * The mds.jar could be found in the same directory as this README file.
     */

