package DijkstraAlgorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GraphGenerator {
    private final int vertices;
    private int[][] graph;
    private Random random = new Random();

    public GraphGenerator(int vertices) {
        this.vertices = vertices;
        fillGraph();
    }

    public GraphGenerator() {
        this.vertices = random.nextInt(5, 12);
        fillGraph();
        printGraph();
    }

    public int[][] getGraph() {
        return graph;
    }

    private void fillGraph() {
        graph = new int[vertices][vertices];
        List<Integer> verticesList = new ArrayList<>();
        for (int i = 0; i < vertices; i++) {
            verticesList.add(i);
        }

        for (int i = 0; i < vertices; i++) {
            int swapIndex = random.nextInt(vertices);
            int temp = verticesList.get(i);
            verticesList.set(i, verticesList.get(swapIndex));
            verticesList.set(swapIndex, temp);
        }

        for (int i = 0; i < vertices; i++) {
            int currentVertex = verticesList.get(i);
            int nextVertex = verticesList.get((i + 1) % vertices);
            int weight = random.nextInt(1, 20);
            graph[currentVertex][nextVertex] = weight;
        }

        int additionalConnections = random.nextInt(vertices * 3);
        for (int i = 0; i < additionalConnections; i++) {
            int vertex1 = random.nextInt(vertices);
            int vertex2 = random.nextInt(vertices);
            if (vertex1 != vertex2 && graph[vertex1][vertex2] == 0) {
                int weight = random.nextInt(1, 20);
                graph[vertex1][vertex2] = weight;
                graph[vertex2][vertex1] = weight;
            }
        }
    }

    private void printGraph() {
        for (int i = 0; i < vertices; i++) {
            for (int j = 0; j < vertices; j++) {
                System.out.print(graph[i][j] + " ");
            }
            System.out.println();
        }
    }
}
