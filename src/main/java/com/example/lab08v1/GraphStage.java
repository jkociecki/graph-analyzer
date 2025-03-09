package com.example.lab08v1;

import DijkstraAlgorithm.DijkstraAlgorithmExecute;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;

import java.awt.*;
import java.util.Iterator;
import java.util.List;

public class GraphStage extends Stage {
    List<Integer> shortestPath;
    Graph graph;
    int[][] graphMatrix;
    List<Integer> recentsList;
    public GraphStage(int[][] graphMatrix, Graph graph) {
        this.graph = graph;
        this.graphMatrix = graphMatrix;
        this.recentsList = null;



        setTitle("Graph Display");

        Button calculateButton = new Button("Calculate");
        Button reset = new Button("Reset");

        Label sourceLabel = new Label("Find shortest path between:");
        TextField sourceTextField = new TextField();


        Label targetLabel = new Label("and");

        Label pathLabel = new Label("Path:");
        pathLabel.setStyle("-fx-background-color: white;");

        ;


        TextField targetTextField = new TextField();
        calculateButton.setOnAction(event -> {
            String source = sourceTextField.getText();
            String target = targetTextField.getText();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Błąd");
            alert.setHeaderText(null);
            alert.setContentText("Wprowadź prawidłowe wartości");

            if (source == null || source.isEmpty() || target == null || target.isEmpty()) {
                alert.showAndWait();
            } else {
                Iterator<org.graphstream.graph.Node> iterator = graph.iterator();
                boolean sourceFound = false;
                boolean targetFound = false;

                while (iterator.hasNext()) {
                    org.graphstream.graph.Node node = iterator.next();
                    if (node.getId().equals(source)) {
                        sourceFound = true;
                    }
                    if (node.getId().equals(target)) {
                        targetFound = true;
                    }
                }
                if (!sourceFound || !targetFound) {
                    alert.showAndWait();
                } else {
                    DijkstraAlgorithmExecute dijkstraAlgorithmExecute = new DijkstraAlgorithmExecute(graphMatrix);
                    int sourceIndex = convertCoordinateToIndex(source);
                    int targetIndex = convertCoordinateToIndex(target);
                    shortestPath = dijkstraAlgorithmExecute.findShortestPath(sourceIndex, targetIndex);
                    recentsList = shortestPath;
                    int shortestPatch = dijkstraAlgorithmExecute.calculateShortestDistance(sourceIndex, targetIndex);
                    System.out.println(shortestPath);
                    pathLabel.setText(" Distance: " + shortestPatch);
                    changeEdgeColors(graph, shortestPath);
                }
            }
        });
        reset.setOnAction(event -> {
            if (recentsList != null) {
                sourceTextField.clear();
                targetTextField.clear();
                changeEdgeColorsToPrevious(graph, recentsList);
                pathLabel.setText("Path: ");
            }
        });


        // Tworzenie panelu górnego i dodawanie komponentów
        HBox topPanel = new HBox(10);
        topPanel.setAlignment(Pos.CENTER);
        topPanel.setPadding(new Insets(10));
        topPanel.getChildren().addAll(sourceLabel, sourceTextField, targetLabel, targetTextField, calculateButton, reset , pathLabel);

        // Tworzenie głównego kontenera BorderPane
        BorderPane root = new BorderPane();

        // Dodawanie panelu górnego do kontenera BorderPane
        root.setTop(topPanel);

        // Tworzenie sceny i przypisanie jej do GraphStage
        Scene scene = new Scene(root, 800, 600);
        setScene(scene);
    }

    private int convertCoordinateToIndex(String coordinate) {
        char c = coordinate.charAt(0);
        return c - 'A';
    }

    public Node getTopPanel() {
        return ((BorderPane) getScene().getRoot()).getTop();
    }

    private void changeEdgeColors(Graph graph, List<Integer> vertexList) {
        for (int i = 0; i < vertexList.size() - 1; i++) {
            int sourceVertex = vertexList.get(i);
            int targetVertex = vertexList.get(i + 1);
            String sourceNode = Character.toString((char) ('A' + sourceVertex));
            String targetNode = Character.toString((char) ('A' + targetVertex));
            String edgeId = sourceNode + targetNode;
            Edge edge = graph.getEdge(edgeId);
            if (edge != null) {
                edge.setAttribute("ui.style", "fill-color: rgb(" + ColorsPalette.getP1c4().getRed() +
                        "," + ColorsPalette.getP1c4().getGreen() +
                        "," + ColorsPalette.getP1c4().getBlue() + ");");
            }
        }
    }
    private void changeEdgeColorsToPrevious(Graph graph, List<Integer> vertexList) {
        for (int i = 0; i < vertexList.size() - 1; i++) {
            int sourceVertex = vertexList.get(i);
            int targetVertex = vertexList.get(i + 1);
            String sourceNode = Character.toString((char) ('A' + sourceVertex));
            String targetNode = Character.toString((char) ('A' + targetVertex));
            String edgeId = sourceNode + targetNode;
            Edge edge = graph.getEdge(edgeId);
            Color color;

            if (edge != null) {
                String colorToSet;
                int labelValule = Integer.parseInt(String.valueOf(edge.getAttribute("ui.label")));
                if (labelValule < 5) {
                    edge.setAttribute("ui.style", "fill-color: rgb(" + ColorsPalette.getP1c1().getRed() +
                            "," + ColorsPalette.getP1c1().getGreen() +
                            "," + ColorsPalette.getP1c1().getBlue() + ");");
                } else if (labelValule < 10) {
                    edge.setAttribute("ui.style", "fill-color: rgb(" + ColorsPalette.getP1c2().getRed() +
                            "," + ColorsPalette.getP1c2().getGreen() +
                            "," + ColorsPalette.getP1c2().getBlue() + ");");
                } else {
                    edge.setAttribute("ui.style", "fill-color: rgb(" + ColorsPalette.getP1c2().getRed() +
                            "," + ColorsPalette.getP1c3().getGreen() +
                            "," + ColorsPalette.getP1c3().getBlue() + ");");
                }
            }
        }
    }
}