package rngGraph;

import java.util.*;

public class RelativeNeighborhoodGraph{
    private int xBoundLower;
    private int xBoundUpper;
    private int yBoundLower;
    private int yBoundUpper;
    private int numberOfVertices;
    private final Random random = new Random();
    private ArrayList<Vertex> vertices;
    private ArrayList<Vertex> verticesWithotEdges;

    public RelativeNeighborhoodGraph(int xBoundLower, int xBoundUpper, int yBoundLower, int yBoundUpper, int numberOfVertices) {
        this.xBoundLower = xBoundLower;
        this.yBoundUpper = yBoundUpper;
        this.xBoundUpper = xBoundUpper;
        this.yBoundLower = yBoundLower;
        this.numberOfVertices = numberOfVertices;
        generateVertices();
        generateRNG();
    }


    private void generateVertices() {
        vertices = new ArrayList<>();
        verticesWithotEdges = new ArrayList<>();
        for (int i = 0; i < numberOfVertices; i++) {
            Vertex vertex = new Vertex(random.nextInt(xBoundLower, xBoundUpper), random.nextInt(yBoundLower, yBoundUpper), i);
            vertices.add(vertex);
            verticesWithotEdges.add(new Vertex(vertex.getX(), vertex.getY(), vertex.getId()));
        }
    }

    private void generateRNG() {
        for(Vertex p : vertices){
            for(Vertex q : vertices){
                if(!p.equals(q)){
                    double distancePQ = calculateDistance(p, q);
                    boolean isNeighbour = true;
                    for(Vertex r : vertices){
                        if(!r.equals(p) && !r.equals(q)){
                            double distancePR = calculateDistance(p, r);
                            double distanceQR = calculateDistance(q, r);
                            if(distancePR < distancePQ && distanceQR < distancePQ){
                                isNeighbour = false;
                                break;
                            }
                        }
                    }
                    if(isNeighbour){
                        p.addNeighbour(q);
                    }
                }
            }
        }
    }

    private double calculateDistance(Vertex p, Vertex q) {
        double dx = p.getX() - q.getX();
        double dy = p.getY() - q.getY();
        return Math.sqrt(dx * dx + dy * dy);
    }

    public ArrayList<Vertex> getVertices() {
        return vertices;
    }
    public ArrayList<Vertex> getVerticesWithoutEdges() {
        return verticesWithotEdges;
    }
}
