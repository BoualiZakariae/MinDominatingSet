package umons.algorithm.dominatingset.graph;


import java.util.Objects;

/**
 *
 * This Class represents a vertex in the Graph.
 *
 * Every node is identified by an integer id
 * and an integer degree.
 *
 *
 * @author bouali
 */
public class Node {

    private int id;
    private int degree;

    public Node() {}


    /**
     * Class constructor
     *
     * @param id      the identifier of thi vertex
     * @param degree  the degree of this vertex
     */
    public Node(int id, int degree) {
        this.id = id;
        this.degree = degree;
    }

    @Override
    public String toString() {
        return "id : " + id + "with Degree: " + degree;
    }
    public int getDegree() {
        return degree;
    }
    public int getId() {
        return id;
    }


    @Override
    public boolean equals( Object obj ) {
        if (!(obj instanceof Node))
            return false;
        Node theOtherNode = (Node)obj;
        return this.id == theOtherNode.id && this.degree == theOtherNode.degree;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id,this.degree);
    }
}
