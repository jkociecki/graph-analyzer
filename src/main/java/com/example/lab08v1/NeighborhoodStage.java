package com.example.lab08v1;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.DefaultGraph;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.fx_viewer.FxDefaultView;
import org.graphstream.ui.fx_viewer.FxViewer;
import rngGraph.*;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


public class NeighborhoodStage extends Stage {
    private Stage primaryStage;
    private int verticies;
    ArrayList<Vertex> verticiesList;
    ArrayList<Vertex> currentVerticiesList;
    RelativeNeighborhoodGraph relativeNeighborhoodGraph;
    Graph curentGraph;
    HashMap<ArrayList<Vertex> , Double> cyclesMap;
    List<Integer> previousVerticies;
    ArrayList<List<Integer>> toResetFromShortest;
    ArrayList<List<Integer>> toResetFromGabriel;

    public NeighborhoodStage(Stage primaryStage, Parent neighborhoodRoot, MyApp myApp) {
        curentGraph = new DefaultGraph("Neighborhood Graphs");
        currentVerticiesList = new ArrayList<>();

        setTitle("Neighborhood Graphs");
        this.primaryStage = primaryStage;

        BorderPane borderPane = (BorderPane) neighborhoodRoot.lookup("#glowny");

        primaryStage.setScene(new Scene(neighborhoodRoot, 800, 600));


        Button back = (Button) neighborhoodRoot.lookup("#back");
        Button rng = (Button) neighborhoodRoot.lookup("#rng");
        Button gabriel = (Button) neighborhoodRoot.lookup("#gabriel");
        Button shor = (Button) neighborhoodRoot.lookup("#shor");
        Button randomGraph = (Button) neighborhoodRoot.lookup("#randomGraph");
        Button rst = (Button) neighborhoodRoot.lookup("#rst");

        TextField verticiesText = (TextField) neighborhoodRoot.lookup("#verticies");
        TextField s = (TextField) neighborhoodRoot.lookup("#s");
        TextField t = (TextField) neighborhoodRoot.lookup("#t");
        TextField Shortest = (TextField) neighborhoodRoot.lookup("#Shortest");

        Button verOK = (Button) neighborhoodRoot.lookup("#verOK");
        CheckBox cycles = (CheckBox) neighborhoodRoot.lookup("#cykle");
        CheckBox labels = (CheckBox) neighborhoodRoot.lookup("#labels");
        CheckBox wages = (CheckBox) neighborhoodRoot.lookup("#wagi");


        back.setOnAction(event -> {
          myApp.start(primaryStage);
        });



        verOK.setOnAction(event -> {
            verticies = verticiesText.getText().isEmpty() ? 0 : Integer.parseInt(verticiesText.getText());
            verticiesText.clear();

            int paneWidth = (int) borderPane.getWidth();
            int paneHeight = (int) borderPane.getHeight();

            int minX = paneWidth / 4;
            int minY = paneHeight / 4;
            int maxX = paneWidth - minX;
            int maxY = paneHeight - minY;
            relativeNeighborhoodGraph = new RelativeNeighborhoodGraph(minX, maxX, minY, maxY, verticies);
            verticiesList = relativeNeighborhoodGraph.getVerticesWithoutEdges();


        });

        rst.setOnAction(event -> {
            s.clear();
            t.clear();
            Shortest.clear();
            curentGraph.edges().filter(e -> e.hasAttribute("ui.style")).forEach(e -> e.setAttribute("ui.style", "fill-color: rgb(0, 0, 0);"));
        });


        Pane con = new Pane();
        rng.setOnAction(event -> {
            borderPane.setRight(null);
            cycles.setSelected(false);
            labels.setSelected(false);
            wages.setSelected(false);
            currentVerticiesList = relativeNeighborhoodGraph.getVertices();
            for(Vertex v : currentVerticiesList){
                System.out.println(v.getId() + " " +v.getNeighbours().size());
            }
            ToGraphStream toGraphStream = new ToGraphStream(relativeNeighborhoodGraph, false);
            curentGraph = toGraphStream.getGraph();
            String nodeStyle = "node {";
            if (!labels.isSelected()) {
                nodeStyle += " text-mode: hidden;";
            } else {
                nodeStyle += " text-mode: normal;";
            }
            nodeStyle += "}";
            String edgeStyle = "edge {";
            if (!wages.isSelected()) {
                edgeStyle += " text-mode: hidden;";
            } else {
                edgeStyle += " text-mode: normal;";
            }
            edgeStyle += "}";
            curentGraph.setAttribute("ui.stylesheet", nodeStyle + " " + edgeStyle);
            FxViewer viewer = new FxViewer(curentGraph, FxViewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
            viewer.enableAutoLayout();
            FxDefaultView view = (FxDefaultView) viewer.addDefaultView(true);
            borderPane.setCenter(view);
        });

        shor.setOnAction(event -> {
            ShortestPathFinder shortestPathFinder = new ShortestPathFinder(currentVerticiesList);
            String source = s.getText();
            String target = t.getText();


            Vertex targetVertex = null;
            Vertex sourceVertex = null;

            for(Vertex v : currentVerticiesList){
                if(v.getId() == Integer.parseInt(source))
                    sourceVertex = v;
                if(v.getId() == Integer.parseInt(target))
                    targetVertex = v;
            }


        List<Vertex> ver = shortestPathFinder.findShortestPath(sourceVertex, targetVertex);

        List<Integer> vetInteger = new ArrayList<>();
            for (Vertex v : ver) {
                vetInteger.add(v.getId());
            }
            Double sum = calculateSumOfEdges(ver);
            Shortest.setText(Double.toString(sum));
            //Shortest.setText();
            changeEdgeColorsNoCycle(curentGraph, vetInteger, "rgb(255,0,0)");

        });

        randomGraph.setOnAction(event -> {
            borderPane.setRight(null);
            cycles.setSelected(false);
            labels.setSelected(false);
            wages.setSelected(false);
            for(Vertex v : verticiesList){
                v.setNeighbours(new LinkedList<>());
            }
            RandomGraphGenerator randomGraphGenerator = new RandomGraphGenerator(verticiesList, 0.25);
            ToGraphStream toGraphStream = new ToGraphStream(randomGraphGenerator, false);
            Graph graphStream = toGraphStream.getGraph();
            curentGraph = graphStream;
            currentVerticiesList = randomGraphGenerator.getVertices();

            for(Vertex v : currentVerticiesList){
                System.out.println(v.getId() + " " +v.getNeighbours().size());
            }

            String nodeStyle = "node {";
            if (!labels.isSelected()) {
                nodeStyle += " text-mode: hidden;";
            } else {
                nodeStyle += " text-mode: normal;";
            }
            nodeStyle += "}";

            String edgeStyle = "edge {";
            if (!wages.isSelected()) {
                edgeStyle += " text-mode: hidden;";
            } else {
                edgeStyle += " text-mode: normal;";
            }
            edgeStyle += "}";
            curentGraph.setAttribute("ui.stylesheet", nodeStyle + " " + edgeStyle);
            FxViewer viewer = new FxViewer(graphStream, FxViewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
            viewer.enableAutoLayout();
            FxDefaultView view = (FxDefaultView) viewer.addDefaultView(true);
            borderPane.setCenter(view);

        });

        gabriel.setOnAction(event -> {
            borderPane.setRight(null);
            cycles.setSelected(false);
            labels.setSelected(false);
            wages.setSelected(false);
            GabrielGraph graph = new GabrielGraph(verticiesList);
            ToGraphStream toGraphStream = new ToGraphStream(graph, false);
            Graph graphStream = toGraphStream.getGraph();
            curentGraph = graphStream;
            currentVerticiesList = graph.getVertices();

            for(Vertex v : currentVerticiesList){
                System.out.println(v.getId() + " " +v.getNeighbours().size());
            }

            String nodeStyle = "node {";
            if (!labels.isSelected()) {
                nodeStyle += " text-mode: hidden;";
            } else {
                nodeStyle += " text-mode: normal;";
            }
            nodeStyle += "}";

            String edgeStyle = "edge {";
            if (!wages.isSelected()) {
                edgeStyle += " text-mode: hidden;";
            } else {
                edgeStyle += " text-mode: normal;";
            }
            edgeStyle += "}";
            curentGraph.setAttribute("ui.stylesheet", nodeStyle + " " + edgeStyle);
            FxViewer viewer = new FxViewer(graphStream, FxViewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
            viewer.enableAutoLayout();
            FxDefaultView view = (FxDefaultView) viewer.addDefaultView(true);
            borderPane.setCenter(view);
        });

        curentGraph.setAttribute("ui.stylesheet", "node { text-mode: hidden; }");
        curentGraph.setAttribute("ui.stylesheet", "edge { text-mode: hidden; }");
        labels.setSelected(false);
        wages.setSelected(false);
        labels.setOnAction(actionEvent -> {
            if (labels.isSelected()) {
                curentGraph.setAttribute("ui.stylesheet", "node { text-mode: normal; }");
            } else {
                curentGraph.setAttribute("ui.stylesheet", "node { text-mode: hidden; }");
            }
        });

        wages.setOnAction(actionEvent -> {
            if (wages.isSelected()) {
                curentGraph.setAttribute("ui.stylesheet", "edge { text-mode: normal; }");
            } else {
                curentGraph.setAttribute("ui.stylesheet", "edge { text-mode: hidden; }");
            }
        });

        cycles.setOnAction(actionEvent -> {
            if (cycles.isSelected()) {
                CycleFinder cycleFinder = new CycleFinder(currentVerticiesList);
                cyclesMap = cycleFinder.getCyclesWithSumOfEdges();
                Parent cycleRoot;
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/cycle.fxml"));
                try {
                    cycleRoot = loader.load();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                Pane myCyclePane = (Pane) cycleRoot.lookup("#myCyclePane");
                TextField cyc = (TextField) cycleRoot.lookup("#cyc");
                TextArea cycleTEXT = (TextArea) cycleRoot.lookup("#cycleTEXT");
                int counter = 1;
                for (List<Vertex> cycle : cyclesMap.keySet()) {
                    String cycleString = "Cycle: " + counter + ": ";
                    for (int i = 0; i < cycle.size(); i++) {
                        cycleString += cycle.get(i).getId();
                        if (i < cycle.size() - 1) {
                            cycleString += "-";
                        }
                    }
                    cycleString += "-" + cycle.get(0).getId();
                    counter++;
                    cycleString += "  Sum: " + Math.round(cyclesMap.get(cycle)) + "\n";
                    cycleTEXT.setText(cycleTEXT.getText() + cycleString);
                }


                cyc.setOnKeyPressed(keyEvent -> {
                    if(keyEvent.getCode() == KeyCode.ENTER){
                        int cycleNumber = Integer.parseInt(cyc.getText());
                        List<Vertex> cycle = new ArrayList<>(cyclesMap.keySet()).get(cycleNumber - 1);
                        List<Integer> cycleInt = new ArrayList<>();
                        previousVerticies = cycleInt;
                        for (Vertex vertex : cycle) {
                            cycleInt.add(vertex.getId());
                        }
                        changeEdgeColors(curentGraph, cycleInt, "rgb(255,0,0)");
                    }
                });
                borderPane.setRight(myCyclePane);
            } else {
                borderPane.setRight(null);
            }
        });

    }
    private void changeEdgeColors(Graph graph, List<Integer> vertexList, String rgb) {
        vertexList.add(vertexList.get(0));
        for (int i = 0; i < vertexList.size() - 1; i++) {
            int sourceVertex = vertexList.get(i);
            int targetVertex = vertexList.get(i + 1);
            String sourceNode = Integer.toString(sourceVertex);
            String targetNode = Integer.toString(targetVertex);
            String edgeId = sourceNode + "-" + targetNode;
            String reverseEdgeId = targetNode + "-" + sourceNode;
            Edge edge = graph.getEdge(edgeId);
            Edge reverseEdge = graph.getEdge(reverseEdgeId);

            if (edge != null) {
                edge.setAttribute("ui.style", "fill-color: " + rgb + ";");
            }

            if (reverseEdge != null) {
                reverseEdge.setAttribute("ui.style", "fill-color: " + rgb + ";");
            }
        }
    }

    private void changeEdgeColorsNoCycle(Graph graph, List<Integer> vertexList, String rgb) {
        for (int i = 0; i < vertexList.size() - 1; i++) {
            int sourceVertex = vertexList.get(i);
            int targetVertex = vertexList.get(i + 1);
            String sourceNode = Integer.toString(sourceVertex);
            String targetNode = Integer.toString(targetVertex);
            String edgeId = sourceNode + "-" + targetNode;
            String reverseEdgeId = targetNode + "-" + sourceNode;
            Edge edge = graph.getEdge(edgeId);
            Edge reverseEdge = graph.getEdge(reverseEdgeId);

            if (edge != null) {
                edge.setAttribute("ui.style", "fill-color: " + rgb + ";");
            }

            if (reverseEdge != null) {
                reverseEdge.setAttribute("ui.style", "fill-color: " + rgb + ";");
            }
        }
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
        return Math.sqrt(Math.pow(p.getX() - q.getX(), 2) + Math.pow(p.getY() - q.getY(), 2));
    }


}
