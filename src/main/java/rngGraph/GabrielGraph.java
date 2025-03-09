package rngGraph;

import java.util.ArrayList;
import java.util.LinkedList;

public class GabrielGraph {
    ArrayList<Vertex> vertices;
    ArrayList<MyCircut> circles;

    public GabrielGraph(ArrayList<Vertex> vertices) {
        for (Vertex vertex : vertices) {
            vertex.setNeighbours(new LinkedList<>());
        }
        this.vertices = vertices;
        circles = new ArrayList<>();
        generateEdges();
    }

    public void generateEdges() {
        int circutCounter = 0;
        double distancePQ = 0;
        double distancePR = 0;
        double distanceQR = 0;
        for (Vertex p : vertices) {
            for (Vertex q : vertices) {
                if (!p.equals(q)) {
                    distancePQ = calculateDistance(p, q);
                    boolean isNeighbour = true;
                    for (Vertex r : vertices) {
                        if (!r.equals(p) && !r.equals(q)) {
                            distancePR = calculateDistance(p, r);
                            distanceQR = calculateDistance(q, r);
                            if (distancePQ > Math.sqrt(distancePR * distancePR + distanceQR * distanceQR)) {
                                isNeighbour = false;
                                break;
                            }
                        }
                    }
                    if (isNeighbour) {
                        p.addNeighbour(q);
                        double centerX = (p.getX() + q.getX()) / 2.0; // Środek odcinka
                        double centerY = (p.getY() + q.getY()) / 2.0; // Środek odcinka
                        double radius = distancePQ / 2.0; // Połowa długości odcinka
                        circles.add(new MyCircut(centerX, centerY, radius, circutCounter));
                        circutCounter++;
                    }
                }
            }
        }
    }


    public ArrayList<MyCircut> getCircles() {
        return circles;
    }

    private double calculateDistance(Vertex p, Vertex q) {
        double dx = p.getX() - q.getX();
        double dy = p.getY() - q.getY();
        return Math.sqrt(dx * dx + dy * dy);
    }

    public ArrayList<Vertex> getVertices() {
        return vertices;
    }
    public class MyCircut{
        double x;
        double y;
        double radius;
        int id;
        public MyCircut(double x, double y, double radius, int id){
            this.x = x;
            this.y = y;
            this.radius = radius;
            this.id = id;
        }

        public double getRadius() {
            return radius;
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }

        public int getId() {
            return id;
        }
    }
}
