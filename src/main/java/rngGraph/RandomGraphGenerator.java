package rngGraph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

public class RandomGraphGenerator {
    private ArrayList<Vertex> vertices;

    public RandomGraphGenerator(ArrayList<Vertex> vertices) {
        this.vertices = vertices;
        generateEdges();
    }

    public RandomGraphGenerator(ArrayList<Vertex> vertices, double edgeProbability) {
        this.vertices = vertices;
        generateEdges(edgeProbability);
    }

    private void generateEdges() {
        generateEdges(0.5); // Domyślne prawdopodobieństwo istnienia krawędzi: 0.5
    }

    private void generateEdges(double edgeProbability) {
        Random random = new Random();
        HashSet<String> addedEdges = new HashSet<>(); // Zbiór krawędzi, aby uniknąć duplikatów
        for (int i = 0; i < vertices.size(); i++) {
            Vertex p = vertices.get(i);
            for (int j = i + 1; j < vertices.size(); j++) {
                Vertex q = vertices.get(j);
                boolean isNeighbour = random.nextDouble() < edgeProbability;
                if (isNeighbour) {
                    String edgeKey1 = generateEdgeKey(p, q);
                    String edgeKey2 = generateEdgeKey(q, p);
                    if (!addedEdges.contains(edgeKey1) && !addedEdges.contains(edgeKey2)) {
                        p.addNeighbour(q);
                        q.addNeighbour(p);
                        addedEdges.add(edgeKey1);
                        addedEdges.add(edgeKey2);
                    }
                }
            }
        }
    }

    public ArrayList<Vertex> getVertices() {
        return vertices;
    }

    private String generateEdgeKey(Vertex p, Vertex q) {
        // Tworzy unikalny klucz dla krawędzi między wierzchołkami p i q
        // Klucz jest oparty na identyfikatorach wierzchołków
        return Math.min(p.getId(), q.getId()) + "-" + Math.max(p.getId(), q.getId());
    }
}
