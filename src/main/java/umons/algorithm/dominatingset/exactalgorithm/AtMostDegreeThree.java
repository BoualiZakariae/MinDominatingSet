package umons.algorithm.dominatingset.exactalgorithm;

import umons.algorithm.dominatingset.graph.Graph;
import umons.algorithm.dominatingset.graph.Node;
import umons.algorithm.dominatingset.graph.Result;
import umons.algorithm.dominatingset.util.DFS;
import umons.algorithm.dominatingset.toDelete.Stats;
import umons.algorithm.dominatingset.util.Util;

import java.util.*;
import java.util.stream.Collectors;


/**
 *
 *
 * This Class implements the part 5 "Graphs of maximum degree three" from the
 * paper : Exact (exponential) algorithms for the dominating set problem
 *
 */
public class AtMostDegreeThree implements mdsAlgorithm {


    /**
     *
     * The first vertex in a component means a vertex that has only one neighbour.
     * We deal with this concept when we manage paths or cycles.
     *
     * @param g         a {@link Graph} data structure
     * @param component a connected component
     * @return          the first vertex of this component
     *
     */
    private static int getFirstVertex( Graph g, List<Integer> component) {
        return component.stream()
                .filter(c->g.getAdj().get(c).size() == 1)
                .findFirst()
                .orElse(-1);
    }

    /**
     *
     *
     *
     * @param g   a {@link Graph} data structure
     * @param y   a vertex of {@code g}
     * @param y2  a vertex that is a neighbor of the y vertex
     * @param x   a vertex that is a neighbor of the y vertex
     *
     * @return  the third neighbor of the vertex y
     *          this neighbor should not be the vertex y2, nor the vertex x.
     */
    private static int getThirdNeighborOf( int y, Graph g, int y2, int x ) {
        return g.getAdj().get(y).stream()
                .filter(u->u != y2 && u != x)
                .findFirst().get();
    }

    /**
     *
     * @param y  a vertex of g
     * @param g  a {@link Graph} data structure
     * @param x  a vertex that is a neighbor of the y vertex
     *
     * @return   the second neighbor of the vertex y
     *           this neighbor should not be the vertex  x
     */
    private static int getSecondNeighborOf( int y, Graph g, int x ) {

        Set<Integer> neighborsSet = g.getAdj().get(y);
        Integer[] neighbors =  neighborsSet.toArray(new Integer[neighborsSet.size()]);
        int neighbor = neighbors[0];
        if (neighbor!=x)
            return  neighbor;
        return neighbors[1];
    }


    /**
     *
     *
     * @param w  a vertex in the graph g
     * @param v  a vertex in the graph g that is adjacent to w
     *
     * @return   the next element of v knowing that w is the predecessor
     *           of v in some path.
     */
    private static int getNextElement( int w, int v, Graph g ) {
        Set<Integer> neighborsSet = g.getAdj().get(v);
        Integer[] neighbors =  neighborsSet.toArray(new Integer[neighborsSet.size()]);
        int neighbour1 = neighbors[0];
        int neighbour2 = neighbors[1];
        if (neighbour1 != w)
            return neighbour1;
        else
            return neighbour2;
    }

    /**
     *
     * This method calculate the minimum dominating set from
     * the path {@code component} and inject the solution to the
     * {@code mds} variable.
     *
     * @param component  a connected component in the graph g
     * @param g          a graph data structure
     * @param mds        the set that will hold the  minimum dominating
     *                   set solution
     */
    private static void mdsFromPathComponent(List<Integer> component, Set<Integer> mds, Graph g) {
        int v = getFirstVertex(g, component);
        int mdsSize = Util.getMdsSize(component);
        int cnt = 1;
        int w = v;
        Set<Integer> neighborsSet = g.getAdj().get(v);
        Integer[] neighbors =  neighborsSet.toArray(new Integer[neighborsSet.size()]);
        v = neighbors[0] ;//second vertex in the path
        mds.add(v);
        while (cnt < mdsSize) {
            int temp = v;
            v = getNextElement(w, v, g);
            w = temp;
            temp = v;
            v = getNextElement(w, v, g);
            w = temp;
            if (g.getAdj().get(v).size() == 1) {
                mds.add(v);
                break;
            }
            temp = v;
            v = getNextElement(w, v, g);
            w = temp;
            mds.add(v);
            cnt++;
        }
    }

    /**
     * This method calculate the minimum dominating set from
     * the cycle {@code component} and inject the solution to the
     * {@code mds} variable.
     *
     *
     * @param component  a cycle component in the graph g
     * @param mds        the set that will hold the  minimum dominating
     *                   set solution
     * @param g          a graph data structure
     */
    private static void mdsFromCycle(List<Integer> component,Set<Integer> mds, Graph g) {
        int v = component.get(0);////second vertex in the cycle
        int mdsSize = Util.getMdsSize(component);
        int cnt = 1;
        int w = v;
        Set<Integer> neighborsSet = g.getAdj().get(v);
        Integer[] neighbors =  neighborsSet.toArray(new Integer[neighborsSet.size()]);
        v = neighbors[0] ;//second vertex in the cycle
        mds.add(v);
        int temp;
        while (cnt < mdsSize) {
            temp = v;
            v = getNextElement(w, v, g);
            w = temp;
            temp = v;
            v = getNextElement(w, v, g);
            w = temp;
            temp = v;
            v = getNextElement(w, v, g);
            w = temp;
            mds.add(v);
            cnt++;
        }
    }

    /**
     *
     * @param component
     * @param g  : a graph data structure
     * @param mds
     *
     * if the component is a graph of 3 vertex then add vertex that has two neighbors to the mds solution
     */
    private static void mdsFrom3vertexComponent(List<Integer> component, Graph g, Set<Integer> mds) {
        int v = component.stream()
                .filter(x->g.getAdj().get(x).size() == 2)
                .findFirst().get();
        mds.add(v);
    }

    /**
     * Calculate the mds from a list of components.
     *
     * Using a Depth First Search Algorithm, we calculate the connected components
     * and for each component and depending on the type of the component
     * we call a specified method to calculate the minimum dominating set
     *
     * @param g  a graph data structure
     * @return   the mds of g knowing that g may be composed by many connected components
     */
    private static Set<Integer> mdsFromConnectedComponents(Graph g) {
        Set<Integer> mds = new HashSet<>();
        DFS dfsAlgo = new DFS(g.size());
        List lists = g.getAdj().values().stream().collect(Collectors.toCollection(ArrayList::new));
        List<List<Integer>> listOfComponents = dfsAlgo.getTheListOfComponents(g.getAdj());
        for (List<Integer> compo: listOfComponents) {
            Set<Integer> mdsOfComponent = new HashSet<>();
            if (compo.size() == 1 || compo.size() == 2)
                mdsOfComponent.add(compo.get(0));
            else if (compo.size() == 3)
                mdsFrom3vertexComponent(compo, g, mdsOfComponent);
            else if (Util.isCycle(g, compo))
                mdsFromCycle(compo, mdsOfComponent, g);
            else
                mdsFromPathComponent(compo, mdsOfComponent, g);
            mds.addAll(mdsOfComponent);
        }
        return mds;
    }

    /**
     *
     *
     *
     * @param g  a graph data structure
     * @param v  a vertex
     * @return   the two neighbors of the vertex v
     */
    private static int[] getTwoNeighborsOf( Graph g, int v ) {
        Set<Integer> neighborsSet = g.getAdj().get(v);
        Integer[] neighbors =  neighborsSet.toArray(new Integer[neighborsSet.size()]);
        int y1 = neighbors[0];
        int y2 = neighbors[1];
        int[] result = new int[2];
        if (g.getAdj().get(y1).size()!=3){
            y1 = neighbors[1];
            y2 = neighbors[0];
        }
        result[0] = y1;
        result[1] = y2;
        return result;
    }


    /**
     * This method represents the main algorithm to calculate the mds for the graph with maximum degree <= 3
     *
     *
     * @param g  Graph with vertices at most degree 3
     * @return   the mds of this graph g
     */
    private static Set<Integer> mdsOfGraphMaxDegreeThree( Graph g ) {
        List<Node> listOfNeighbors = g.getNeighborsOf3DegreeVertices();
        List<Set<Integer>> listOfMds = new ArrayList<>();

        // base case
        if (listOfNeighbors.size() == 0) { // no vertex with degree 3
            return mdsFromConnectedComponents(g);
        }

        //minDegreeNeighbor
        Node x =listOfNeighbors.stream()
                                .min(Comparator.comparingInt(n-> n.getDegree()))
                                .get();
        if(x.getDegree()>2 ){
            //not presented in the paper, the case where every vertex in the graph is degree three
            return caseXisdegreeThree(g, x);

        }
        //there exists one or more vertex with degree three
        for (int i=0; i < listOfNeighbors.size(); i++){
            x = listOfNeighbors.get(i);
            Set<Integer> mds ;
            switch (x.getDegree()){
                case (1) :  mds = degreeOneVertexCase(g,x);/*case1.A and 1.B*/
                            listOfMds.add(mds);
                            break;

                case (2) :  mds = degreeTwoVertexCase(g,x);/*case 2*/
                            listOfMds.add(mds);
                            break;
            }
        }
        return listOfMds.stream()
                        .min(Comparator.comparing(Set::size))
                        .get();
    }

    private static Set<Integer> degreeTwoVertexCase( Graph g, Node x ) {

        int[] twoNeighborsOfx = getTwoNeighborsOf(g, x.getId());
        int y1 = twoNeighborsOfx[0];
        int y2 = twoNeighborsOfx[1];
        // case 2.1
        if (Util.areAdjacent(g, y1, y2)) {
            if (g.getDegreeOf(y2) == 2)
                return case2_1_1(g, y1, y2, x);
            if (g.getDegreeOf(y2) == 3)
                return case2_1_2(g, y1, x, y2);

        } // case 2.2
        // y1 and y2 not adjacent?
        if (g.getDegreeOf(y2) == 1) {//maybenotpresented in the paper todo
            Set<Integer> neighborsOfX = g.getClosedNeighbors(x.getId());
            Graph gPrime = g.removeVertices(neighborsOfX);
            Set<Integer> D0 = mdsOfGraphMaxDegreeThree(gPrime);
            D0.add(x.getId());
            return D0;
        }
        if (g.getDegreeOf(y2) == 2)
            return case2_2_1(g, y1, x, y2);
        if (g.getDegreeOf(y2) == 3)
            return case2_2_2(g, y1, x, y2);
        return null;//to be deleted todo
    }

    /**
     * TOdo BE REVIEWED because of the lemma condition
     * @param g  : a graph data structure
     * @param x
     * @return
     */
    private static Set<Integer> caseXisdegreeThree(Graph g, Node x) {
        // add x
        Set<Integer> neighborsOfX = g.getClosedNeighbors(x.getId());
        Graph gPrime = g.removeVertices(neighborsOfX);
        Set<Integer> firstMds = mdsOfGraphMaxDegreeThree(gPrime);
        firstMds.add(x.getId());

        // neighbors of x
        Set<Integer> neighborsSet = g.getAdj().get(x.getId());
        Integer[] neighborsOfx =  neighborsSet.toArray(new Integer[neighborsSet.size()]);
        int y1 = neighborsOfx[0];
        int y2 = neighborsOfx[1];
        int y3 = neighborsOfx[2];
        Set<Integer> mds = firstMds;

        // add y1
        Set<Integer> neighborsOfY1 = g.getClosedNeighbors(y1);
        Set<Integer> vertices = new HashSet<>(neighborsOfY1);
        gPrime = g.removeVertices(vertices);
        Set<Integer> currentMds = mdsOfGraphMaxDegreeThree(gPrime);
        currentMds.add(y1);
        mds = Util.minOf(mds, currentMds);

        // add x and y1
        neighborsOfX.stream()
                .filter(u->!neighborsOfY1.contains(u))
                .forEachOrdered(vertices::add);
        gPrime = g.removeVertices(vertices);
        currentMds = mdsOfGraphMaxDegreeThree(gPrime);
        currentMds.add(x.getId());
        currentMds.add(y1);
        mds = Util.minOf(mds, currentMds);

        // add y2 only
        Set<Integer> neighborsOfY2 = g.getClosedNeighbors(y2);
        vertices = new HashSet<>(neighborsOfY2);
        gPrime = g.removeVertices(vertices);
        currentMds = mdsOfGraphMaxDegreeThree(gPrime);
        currentMds.add(y2);
        mds = Util.minOf(mds, currentMds);

        // add x and y2

        neighborsOfX.stream()
                .filter(u->!neighborsOfY2.contains(u))
                .forEachOrdered(vertices::add);
        gPrime = g.removeVertices(vertices);
        currentMds = mdsOfGraphMaxDegreeThree(gPrime);
        currentMds.add(x.getId());
        currentMds.add(y2);
        mds = Util.minOf(mds, currentMds);

        // add y3 only
        Set<Integer> neighborsOfY3 = g.getClosedNeighbors(y3);
        vertices = new HashSet<>(neighborsOfY3);
        gPrime = g.removeVertices(vertices);
        currentMds = mdsOfGraphMaxDegreeThree(gPrime);
        currentMds.add(y3);
        mds = Util.minOf(mds, currentMds);

        // add x and y3
        neighborsOfX.stream()
                .filter(u->!neighborsOfY3.contains(u))
                .forEachOrdered(vertices::add);
        gPrime = g.removeVertices(vertices);
        currentMds = mdsOfGraphMaxDegreeThree(gPrime);
        currentMds.add(x.getId());
        currentMds.add(y3);
        mds = Util.minOf(mds, currentMds);
        return mds;
    }

    /**
     *
     * @param g  : a graph data structure
     * @param x
     * @param y1
     * @param y2
     * @return
     */
    private static Set<Integer> case2_2_2(Graph g, int y1, Node x, int y2) {
        int z11 = getSecondNeighborOf(y1, g, x.getId());
        int z12 = getThirdNeighborOf(y1, g, z11, x.getId());
        int z21 = getSecondNeighborOf(y2, g, x.getId());
        int z22 = getThirdNeighborOf(y2, g, z21, x.getId());

        // we add x in D
        Set<Integer> neighborsOfX = g.getClosedNeighbors(x.getId());
        Graph gPrime = g.removeVertices(neighborsOfX);
        Set<Integer> D1 = mdsOfGraphMaxDegreeThree(gPrime);
        D1.add(x.getId());
        // we add  y1 in D
        Set<Integer> neighborsOfY1 = g.getClosedNeighbors(y1);
        gPrime = g.removeVertices(neighborsOfY1);
        Set<Integer> D2 = mdsOfGraphMaxDegreeThree(gPrime);
        D2.add(y1);
        // we add  y2 in D
        Set<Integer> neighborsOfY2 = g.getClosedNeighbors(y2);
        gPrime = g.removeVertices(neighborsOfY2);
        Set<Integer> D3 = mdsOfGraphMaxDegreeThree(gPrime);
        D3.add(y2);
        Set<Integer> neighborsOfZ11 = g.getClosedNeighbors(z11);
        Set<Integer> neighborsOfZ12 = g.getClosedNeighbors(z12);
        Set<Integer> neighborsOfZ21 = g.getClosedNeighbors(z21);
        Set<Integer> neighborsOfZ22 = g.getClosedNeighbors(z22);
        Set<Integer> vertices;
        Set<Integer> D4=null, D5=null, D6=null, D7=null;
        // we add  y1 and z11 in D
        if (neighborsOfZ11.size()>3){
            vertices = neighborsOfZ11.stream()
                    .filter(v->!neighborsOfY1.contains(v))
                    .collect(Collectors.toCollection(HashSet::new));
            vertices.addAll(neighborsOfY1);
            gPrime = g.removeVertices(vertices);
            D4 = mdsOfGraphMaxDegreeThree(gPrime);
            D4.add(y1);
            D4.add(z11);
        }
        // we add  y1 and z12 in D
        if (neighborsOfZ12.size()>3){
            vertices =  neighborsOfZ12.stream()
                    .filter(v->!neighborsOfY1.contains(v))
                    .collect(Collectors.toCollection(HashSet::new));
            vertices.addAll(neighborsOfY1);
            gPrime = g.removeVertices(vertices);
            D5 = mdsOfGraphMaxDegreeThree(gPrime);
            D5.add(y1);
            D5.add(z12);

        }

        // we add  y2 and z21 in D
        if (neighborsOfZ21.size()>3){
            vertices =  neighborsOfZ21.stream()
                    .filter(v->!neighborsOfY2.contains(v))
                    .collect(Collectors.toCollection(HashSet::new));
            vertices.addAll(neighborsOfY2);
            gPrime = g.removeVertices(vertices);
            D6 = mdsOfGraphMaxDegreeThree(gPrime);
            D6.add(y2);
            D6.add(z21);

        }

        // we add  y2 and z22 in D
        if (neighborsOfZ22.size()>3){
            vertices =  neighborsOfZ22.stream()
                    .filter(v->!neighborsOfY2.contains(v))
                    .collect(Collectors.toCollection(HashSet::new));
            vertices.addAll(neighborsOfY2);
            gPrime = g.removeVertices(vertices);
            D7 = mdsOfGraphMaxDegreeThree(gPrime);
            D7.add(y2);
            D7.add(z22);

        }
        return Util.minOfTheSet(D1,D2,D3,D4,D5,D6,D7);
    }

    /**
     *
     * @param g  : a graph data structure
     * @param x
     * @param y1
     * @param y2
     * @return
     */
    private static Set<Integer> case2_2_1(Graph g, int y1, Node x, int y2) {
        int z11 = getSecondNeighborOf(y1, g, x.getId());
        int z12 = getThirdNeighborOf(y1, g, z11, x.getId());
        //2.2.1.A
        // x in D
        Set<Integer> neighborsOfX = g.getClosedNeighbors(x.getId());
        Graph gPrime = g.removeVertices(neighborsOfX);
        Set<Integer> D1 = mdsOfGraphMaxDegreeThree(gPrime);
        D1.add(x.getId());
        //2.2.1.B
        // y1 in D
        Set<Integer> neighborsOfY1 = g.getClosedNeighbors(y1);
        gPrime = g.removeVertices(neighborsOfY1);
        Set<Integer> D2 = mdsOfGraphMaxDegreeThree(gPrime);
        D2.add(y1);
        // y2 in D
        Set<Integer> neighborsOfY2 = g.getClosedNeighbors(y2);
        gPrime = g.removeVertices(neighborsOfY2);
        Set<Integer> D3 = mdsOfGraphMaxDegreeThree(gPrime);
        D3.add(y2);
        Set<Integer> neighborsOfZ11 = g.getClosedNeighbors(z11);
        Set<Integer> neighborsOfZ12 = g.getClosedNeighbors(z12);

        //2.2.1.C
        Set<Integer> vertices;
        Set<Integer> D4 = null,D5= null;
        // y1  and  z11 in D
        if(neighborsOfZ11.size()>3){
            vertices = neighborsOfZ11.stream()
                    .filter(v->!neighborsOfY1.contains(v))
                    .collect(Collectors.toCollection(HashSet::new));
            vertices.addAll(neighborsOfY1);
            gPrime = g.removeVertices(vertices);
            D4 = mdsOfGraphMaxDegreeThree(gPrime);
            D4.add(y1);
            D4.add(z11);
        }

        // y1 and z12 in D
        if(neighborsOfZ12.size()>3){
            vertices = neighborsOfZ12.stream()
                    .filter(v->!neighborsOfY1.contains(v))
                    .collect(Collectors.toCollection(HashSet::new));
            vertices.addAll(neighborsOfY1);
            gPrime = g.removeVertices(vertices);
            D5 = mdsOfGraphMaxDegreeThree(gPrime);
            D5.add(y1);
            D5.add(z12);

        }

        return Util.minOfTheSet(D1,D2,D3,D4,D5);
    }

    /**
     *
     * @param g  : a graph data structure
     * @param x
     * @param y1
     * @param y2
     * @return
     */
    private static Set<Integer> case2_1_2(Graph g, int y1, Node x, int y2) {
        int z1 = getThirdNeighborOf(y1, g, y2, x.getId());
        int z2 = getThirdNeighborOf(y2, g, y1, x.getId());

        // y1 in D
        Set<Integer> neighborsOfY1 = g.getClosedNeighbors(y1);
        Graph gPrime = g.removeVertices(neighborsOfY1);
        Set<Integer> D1 = mdsOfGraphMaxDegreeThree(gPrime);
        D1.add(y1);

        // y2 in D
        Set<Integer> neighborsOfY2 = g.getClosedNeighbors(y2);
        gPrime = g.removeVertices(neighborsOfY2);
        Set<Integer> D2 = mdsOfGraphMaxDegreeThree(gPrime);
        D2.add(y2);
        Set<Integer> neighborsOfZ1 = g.getClosedNeighbors(z1);
        Set<Integer> neighborsOfZ2 = g.getClosedNeighbors(z2);

        // y1 and z1 in D
        Set<Integer> vertices = neighborsOfZ1.stream()
                .filter(v->!neighborsOfY1.contains(v))
                .collect(Collectors.toCollection(HashSet::new));
        vertices.addAll(neighborsOfY1);
        gPrime = g.removeVertices(vertices);
        Set<Integer> D3 = mdsOfGraphMaxDegreeThree(gPrime);
        D3.add(y1);
        D3.add(z1);

        // y2 and z2 in D
        vertices = neighborsOfZ2.stream()
                .filter(v-> !neighborsOfY2.contains(v))
                .collect(Collectors.toCollection(HashSet::new));
        vertices.addAll(neighborsOfY2);
        gPrime = g.removeVertices(vertices);
        Set<Integer> D4 = mdsOfGraphMaxDegreeThree(gPrime);
        D4.add(y2);
        D4.add(z2);

        return Util.minOfTheSet(D1,D2,D3,D4);
    }

    /**
     *
     * @param g  : a graph data structure
     * @param x
     * @param y1
     * @param y2
     * @return
     */
    private static Set<Integer> case2_1_1(Graph g, int y1, int y2, Node x) {
        //only y1 in ArbitraryGraph
        Set<Integer> neighborsOfY1 = g.getClosedNeighbors(y1);
        Graph gPrime = g.removeVertices(neighborsOfY1);
        Set<Integer> D1 = mdsOfGraphMaxDegreeThree(gPrime);
        D1.add(y1);
        //y and z in mds
        //looking for z:the third neighbor of y1
        int z = getThirdNeighborOf(y1, g, y2, x.getId());
        Set<Integer> neighborsOfZ = g.getClosedNeighbors(z);
        Set<Integer> vertices = neighborsOfZ.stream()
                .filter(v->!neighborsOfY1.contains(v))
                .collect(Collectors.toCollection(HashSet::new));
        vertices.addAll(neighborsOfY1);
        gPrime = g.removeVertices(vertices);
        Set<Integer> D2 = mdsOfGraphMaxDegreeThree(gPrime);
        D2.add(y1);
        D2.add(z);
        if (D1.size() < D2.size()) {
            return D1;
        } else {
            return D2;
        }
    }

    /**
     * Case A,B.1 and B.2
     *
     *
     * @param g  : a graph data structure
     * @param x
     * @return
     */
    private static Set<Integer> degreeOneVertexCase(Graph g, Node x) {
        Set<Integer> neighborsSet = g.getAdj().get(x.getId());
        Integer[] neighbors =  neighborsSet.toArray(new Integer[neighborsSet.size()]);
        int y = neighbors[0];
        int z1 = g.getAdj().get(y).stream()
                .filter(u-> u != x.getId())
                .findFirst()
                .get();
        int z2 = getThirdNeighborOf(y, g, z1, x.getId());
        // case 1.A, add y to the ArbitraryGraph
        Set<Integer> neighborsOfY = g.getClosedNeighbors(y);
        Graph gPrime = g.removeVertices(neighborsOfY);
        Set<Integer> D1 = mdsOfGraphMaxDegreeThree(gPrime);
        D1.add(y);
        // case B

        // add y-z1 to the ArbitraryGraph
        Set<Integer> neighborsOfZ1 = g.getClosedNeighbors(z1);
        Set<Integer> vertices;
        Set<Integer> D2 = null,D3=null;
        if (neighborsOfZ1.size()>3){
            vertices =  neighborsOfZ1.stream()
                    .filter(v -> !neighborsOfY.contains(v))
                    .collect(Collectors.toCollection(HashSet::new));
            vertices.addAll(neighborsOfY);
            gPrime = g.removeVertices(vertices);
            D2 = mdsOfGraphMaxDegreeThree(gPrime);
            D2.add(y);
            D2.add(z1);

        }
        // add y-z2 to the ArbitraryGraph
        Set<Integer> neighborsOfZ2 = g.getClosedNeighbors(z2);
        if (neighborsOfZ2.size()>3){
            vertices =  neighborsOfZ2.stream()
                    .filter(v -> !neighborsOfY.contains(v))
                    .collect(Collectors.toCollection(HashSet::new));
            vertices.addAll(neighborsOfY);
            gPrime = g.removeVertices(vertices);
            D3 = mdsOfGraphMaxDegreeThree(gPrime);
            D3.add(y);
            D3.add(z2);
        }

        return Util.minOfTheSet(D1,D2,D3);

    }


    /**
     *
     * @param graph the graph instance
     */
    public Result run( Graph graph){
        double start = System.currentTimeMillis();
        if (graph.getMaxDegree() > 3) {
            return null;
        }
        Set<Integer> mds = mdsOfGraphMaxDegreeThree(graph);
        double end = System.currentTimeMillis();
        Stats.numberOfGraphs++;
        return new Result(mds,end-start);
    }

    /**
     * testing the full algorithm
     *
     * @param args
     */
    public static void main(String[] args) {


    }

}
