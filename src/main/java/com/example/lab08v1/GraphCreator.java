package com.example.lab08v1;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.layout.Layout;
import org.graphstream.ui.layout.springbox.implementations.SpringBox;

import java.awt.*;

public class GraphCreator {
    public Graph createGraph(int[][] matrix) {
        Graph graph = new SingleGraph("MyGraph");

        int nodeCount = matrix.length;
        for (int i = 0; i < nodeCount; i++) {
            String nodeName = Character.toString((char) ('A' + i));
            org.graphstream.graph.Node node = graph.addNode(nodeName);
            node.setAttribute("ui.label", nodeName); // Przypisanie nazwy wierzchołkowi
            node.setAttribute("ui.style", "shape:circle;fill-color: blue;size: 30px;");
            node.setAttribute("ui.style", "text-size: 20;");
            node.setAttribute("ui.style", "text-offset: 0, -20;");
        }

        for (int i = 0; i < nodeCount; i++) {
            for (int j = 0; j < nodeCount; j++) {
                if (matrix[i][j] > 0) {
                    String sourceNode = Character.toString((char) ('A' + i));
                    String targetNode = Character.toString((char) ('A' + j));
                    String edgeId = sourceNode + targetNode; // Zmieniono identyfikator krawędzi
                    Edge edge = graph.addEdge(edgeId, sourceNode, targetNode, true); // Usunięto flagę kierunku
                    edge.setAttribute("ui.label", Integer.toString(matrix[i][j]));
                    String color;
                    Color color1;

                    if (matrix[i][j] < 5) {
                        edge.setAttribute("ui.style", "fill-color: rgb(" + ColorsPalette.getP1c1().getRed() +
                                "," + ColorsPalette.getP1c1().getGreen() +
                                "," + ColorsPalette.getP1c1().getBlue() + ");");
                    } else if (matrix[i][j] < 10) {
                        edge.setAttribute("ui.style", "fill-color: rgb(" + ColorsPalette.getP1c2().getRed() +
                                "," + ColorsPalette.getP1c2().getGreen() +
                                "," + ColorsPalette.getP1c2().getBlue() + ");");
                    } else {
                        edge.setAttribute("ui.style", "fill-color: rgb(" + ColorsPalette.getP1c3().getRed() +
                                "," + ColorsPalette.getP1c3().getGreen() +
                                "," + ColorsPalette.getP1c3().getBlue() + ");");
                    }
                }
            }
        }

        Layout layout = new SpringBox(); // Zmieniono na ogólny interfejs Layout
        layout.setForce(0.2); // Zwiększenie siły oddziaływania między wierzchołkami
        layout.setQuality(0.2); // Zwiększenie jakości układu
        layout.addAttributeSink(graph);
        layout.setStabilizationLimit(0.0001);
        layout.compute();

        return graph;
    }
}
