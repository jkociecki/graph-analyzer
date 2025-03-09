package rngGraph;

import java.util.LinkedList;

public class Vertex implements Cloneable {
    private int x;
    private int y;
    private int id;
    private Vertex closestNeighbour;
    private LinkedList<Vertex> neighbours;


    public Vertex(int x, int y, int id) {
        this.x = x;
        this.y = y;
        this.id = id;
        neighbours = new LinkedList<>();
    }

    public int getX() {
        return x;
    }

    public void addNeighbour(Vertex neighbour) {
        neighbours.add(neighbour);
    }

    public LinkedList<Vertex> getNeighbours() {
        return neighbours;
    }

    public int getY() {
        return y;
    }

    public int getId() {
        return id;
    }

    public Vertex getClosestNeighbour() {
        return closestNeighbour;
    }

    public void setClosestNeighbour(Vertex closestNeighbour) {
        this.closestNeighbour = closestNeighbour;
    }
    public void setNeighbours(LinkedList<Vertex> neighbours) {
        this.neighbours = neighbours;
    }

    public boolean equals(Vertex v) {
        if (this.x == v.getX() && this.y == v.getY()) {
            return true;
        }
        return false;
    }

    public Vertex clone() {
        try {
            Vertex clone = (Vertex) super.clone();
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
