package rngGraph;

import java.util.*;

public class CycleFinder {
    private ArrayList<Vertex> vertices;
    private LinkedHashMap<ArrayList<Vertex>, Double> cyclesWithSumOfEdges;

    public CycleFinder(ArrayList<Vertex> vertices) {
        this.vertices = vertices;
        this.cyclesWithSumOfEdges = new LinkedHashMap<>();
        findCycles();
        sortBySumOfEdgesDescending();
    }

    public CycleFinder(){
    }

    public void findCycles() {
        ArrayList<ArrayList<Vertex>> cycles = new ArrayList<>();
        for (Vertex vertex : vertices) {
            Set<Vertex> visited = new HashSet<>();
            ArrayList<Vertex> currentPath = new ArrayList<>();
            dfs(vertex, vertex, visited, currentPath, cycles);
        }
        ArrayList<ArrayList<Vertex>> uniqueCycles = getUniqueCycles(cycles);
        if (!uniqueCycles.isEmpty()) {
            this.cyclesWithSumOfEdges = calculateSumOfEdges(uniqueCycles);
        }
    }

    private void dfs(Vertex startVertex, Vertex currentVertex, Set<Vertex> visited, ArrayList<Vertex> currentPath, ArrayList<ArrayList<Vertex>> cycles) {
        visited.add(currentVertex);
        currentPath.add(currentVertex);
        for (Vertex neighbour : currentVertex.getNeighbours()) {
            if (neighbour == startVertex && currentPath.size() > 2) {
                cycles.add(new ArrayList<>(currentPath));
            } else if (!visited.contains(neighbour)) {
                dfs(startVertex, neighbour, visited, currentPath, cycles);
            }
        }
        visited.remove(currentVertex);
        currentPath.remove(currentPath.size() - 1);
    }


    private ArrayList<ArrayList<Vertex>> getUniqueCycles(ArrayList<ArrayList<Vertex>> cycles) {
        ArrayList<ArrayList<Vertex>> uniqueCycles = new ArrayList<>();
        Set<List<Vertex>> cycleSet = new HashSet<>();
        for (ArrayList<Vertex> cycle : cycles) {
            List<Vertex> sortedCycle = new ArrayList<>(cycle);
            sortedCycle.sort(Comparator.comparingInt(Vertex::getId));
            if (cycleSet.add(sortedCycle)) {
                uniqueCycles.add(cycle);
            }
        }
        return uniqueCycles;
    }

    private LinkedHashMap<ArrayList<Vertex>, Double> calculateSumOfEdges(ArrayList<ArrayList<Vertex>> cycles) {
        LinkedHashMap<ArrayList<Vertex>, Double> cyclesWithSumOfEdges = new LinkedHashMap<>();
        for (ArrayList<Vertex> cycle : cycles) {
            double sumOfEdges = calculateCycleLength(cycle);
            cyclesWithSumOfEdges.put(cycle, sumOfEdges);
        }
        return cyclesWithSumOfEdges;
    }

    public double calculateCycleLength(ArrayList<Vertex> cycle) {
        double sum = 0;
        int size = cycle.size();
        for (int i = 0; i < size - 1; i++) {
            Vertex currentVertex = cycle.get(i);
            Vertex nextVertex = cycle.get((i + 1));
            double edgeWeight = getEdgeWeight(currentVertex, nextVertex);
            sum += edgeWeight;
        }
        sum += getEdgeWeight(cycle.get(size - 1), cycle.get(0));
        return sum;
    }

    private double getEdgeWeight(Vertex vertex1, Vertex vertex2) {
        return Math.sqrt(Math.pow(vertex1.getX() - vertex2.getX(), 2) + Math.pow(vertex1.getY() - vertex2.getY(), 2));
    }

    private void sortBySumOfEdgesDescending() {
        ArrayList<Map.Entry<ArrayList<Vertex>, Double>> entries = new ArrayList<>(cyclesWithSumOfEdges.entrySet());
        entries.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));
        cyclesWithSumOfEdges.clear();
        for (Map.Entry<ArrayList<Vertex>, Double> entry : entries) {
            cyclesWithSumOfEdges.put(entry.getKey(), entry.getValue());
        }
    }

    public LinkedHashMap<ArrayList<Vertex>, Double> getCyclesWithSumOfEdges() {
        return cyclesWithSumOfEdges;
    }
}
