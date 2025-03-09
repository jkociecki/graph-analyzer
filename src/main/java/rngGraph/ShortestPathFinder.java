package rngGraph;

import java.util.*;

public class ShortestPathFinder {
    private ArrayList<Vertex> vertices;

    public ShortestPathFinder(ArrayList<Vertex> vertices) {
        this.vertices = vertices;
    }
    public ShortestPathFinder(){
    }

    public List<Vertex> findShortestPath(Vertex start, Vertex end) {
        Map<Vertex, Double> distances = new HashMap<>();
        Map<Vertex, Vertex> previous = new HashMap<>();
        PriorityQueue<Vertex> queue = new PriorityQueue<>(Comparator.comparingDouble(distances::get));
        for (Vertex vertex : vertices) {
            if (vertex.equals(start)) {
                distances.put(vertex, 0.0);
            } else {
                distances.put(vertex, Double.POSITIVE_INFINITY);
            }
            queue.add(vertex);
        }

        while (!queue.isEmpty()) {
            Vertex current = queue.poll();

            if (current.equals(end)) {
                break;
            }
            for (Vertex neighbor : current.getNeighbours()) {
                double distance = distances.get(current) + calculateDistance(current, neighbor);

                if (distance < distances.get(neighbor)) {
                    distances.put(neighbor, distance);
                    previous.put(neighbor, current);
                    queue.remove(neighbor);
                    queue.add(neighbor);
                }
            }
        }

        List<Vertex> shortestPath = new ArrayList<>();
        Vertex current = end;
        while (current != null) {
            shortestPath.add(current);
            current = previous.get(current);
        }
        Collections.reverse(shortestPath);

        return shortestPath;
    }

    public double calculateSumOfEdges(List<Vertex> shortestPath) {
        double sum = 0.0;
        for (int i = 0; i < shortestPath.size() - 1; i++) {
            Vertex p = shortestPath.get(i);
            Vertex q = shortestPath.get(i + 1);
            double distance = calculateDistance(p, q);
            sum += distance;
        }
        return sum;
    }


    private double calculateDistance(Vertex p, Vertex q) {
        double dx = p.getX() - q.getX();
        double dy = p.getY() - q.getY();
        return Math.sqrt(dx * dx + dy * dy);
    }
}
