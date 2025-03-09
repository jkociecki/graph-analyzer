package rngGraph;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.DefaultGraph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class ToGraphStream {
    RelativeNeighborhoodGraph rng;
    Graph graph;
    ArrayList<Vertex> vertices;
    boolean isDirected;

    public ToGraphStream(RelativeNeighborhoodGraph rng, boolean isDirected) {
        this.rng = rng;
        graph = new DefaultGraph("RNG");
        vertices = rng.getVertices();
        this.isDirected = isDirected;
        addNodesAndEdges();
    }

    public ToGraphStream(GabrielGraph gabirel, boolean isDirected){
        graph = new DefaultGraph("Gabriel");
        vertices = gabirel.getVertices();
        this.isDirected = isDirected;
        addNodesAndEdges();
    }
    public ToGraphStream(RandomGraphGenerator randomGraphGenerator, boolean isDirected){
        graph = new DefaultGraph("Random");
        vertices = randomGraphGenerator.getVertices();
        this.isDirected = isDirected;
        addNodesAndEdges();
    }

    private void addNodesAndEdges() {
        Set<String> addedEdges = new HashSet<>();

        for (Vertex vertex : vertices) {
            Node node = graph.addNode(String.valueOf(vertex.getId()));
            node.setAttribute("xy", vertex.getX(), vertex.getY());
            node.setAttribute("ui.layout", "xy");
            node.setAttribute("layout.frozen", true);
            node.setAttribute("ui.label", String.valueOf(vertex.getId()));
            node.setAttribute("ui.style", "text-offset: 0, -20;");
            node.setAttribute("ui.size", 3);

        }

        for (Vertex vertex : vertices) {
            LinkedList<Vertex> neighbours = vertex.getNeighbours();
            for (Vertex neighbour : neighbours) {
                String edgeId = String.valueOf(vertex.getId()) + "-" + String.valueOf(neighbour.getId());
                String reversedId = String.valueOf(neighbour.getId()) + "-" + String.valueOf(vertex.getId());
                // Sprawdzenie, czy krawędź lub jej odwrócona wersja już istnieje w zbiorze
                if (!addedEdges.contains(edgeId) && !addedEdges.contains(reversedId)) {
                    double distance = Math.sqrt(Math.pow(vertex.getX() - neighbour.getX(), 2) + Math.pow(vertex.getY() - neighbour.getY(), 2));
                    Edge edge = graph.addEdge(
                            edgeId,
                            String.valueOf(vertex.getId()),
                            String.valueOf(neighbour.getId()), isDirected);

                    edge.setAttribute("ui.label", String.format("%.2f", distance));
                    edge.setAttribute("ui.style", "text-size: 10; text-offset: 0, -10; text-color: rgb(47,79,79);");

                    addedEdges.add(edgeId); // Dodanie krawędzi do zbioru


                }
            }
        }
    }




    public Graph getGraph() {
        return graph;
    }
}
