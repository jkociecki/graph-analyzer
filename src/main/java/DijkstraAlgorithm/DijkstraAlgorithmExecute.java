package DijkstraAlgorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DijkstraAlgorithmExecute {
    private int[][] graph;
    private int numVertices;

    public DijkstraAlgorithmExecute(int[][] graph) {
        this.graph = graph;
        this.numVertices = graph.length;
    }

    public List<Integer> findShortestPath(int source, int destination) {
        int[] distances = new int[numVertices];
        boolean[] visited = new boolean[numVertices];
        int[] previous = new int[numVertices];

        Arrays.fill(distances, Integer.MAX_VALUE);
        Arrays.fill(previous, -1);

        distances[source] = 0;


        for (int count = 0; count < numVertices - 1; count++) {
            int u = findMinDistance(distances, visited);
            visited[u] = true;

            for (int v = 0; v < numVertices; v++) {
                if (!visited[v] && graph[u][v] != 0 && distances[u] != Integer.MAX_VALUE
                        && distances[u] + graph[u][v] < distances[v]) {
                    distances[v] = distances[u] + graph[u][v];
                    previous[v] = u;
                }
            }
        }
        return buildPath(previous, destination);
    }

    private int findMinDistance(int[] distances, boolean[] visited) {
        int minDistance = Integer.MAX_VALUE;
        int minIndex = -1;

        for (int v = 0; v < numVertices; v++) {
            if (!visited[v] && distances[v] <= minDistance) {
                minDistance = distances[v];
                minIndex = v;
            }

        }
        return minIndex;
    }

    private List<Integer> buildPath(int[] previous, int destination) {
        List<Integer> path = new ArrayList<>();
        int currentVertex = destination;

        while (currentVertex != -1) {
            path.add(0, currentVertex);
            currentVertex = previous[currentVertex];
        }
        return path;
    }
    public int calculateShortestDistance(int source, int destination) {
        List<Integer> shortestPath = findShortestPath(source, destination);
        int distance = 0;

        for (int i = 0; i < shortestPath.size() - 1; i++) {
            int u = shortestPath.get(i);
            int v = shortestPath.get(i + 1);
            distance += graph[u][v];
        }

        return distance;
    }
}
