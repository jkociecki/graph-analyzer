package com.example.lab08v1;

import DijkstraAlgorithm.GraphGenerator;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.DefaultGraph;
import org.graphstream.ui.fx_viewer.FxDefaultView;
import org.graphstream.ui.fx_viewer.FxViewPanel;
import org.graphstream.ui.fx_viewer.FxViewer;
import org.graphstream.ui.layout.Layout;
import org.graphstream.ui.layout.springbox.implementations.SpringBox;

import java.io.IOException;

public class MyApp extends Application {
    private GraphGenerator graphGenerator;
    private BorderPane root;
    private VBox leftPanel;
    private StackPane graphPane;
    private GraphStage graphStage;
    Graph graph;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Graph Display");
        Button createGraphButton = new Button("Create Graph");
        Button neighboorhoodButton = new Button("Neighborhood Graphs");
        Button fromMatrix = new Button("From Matrix");

        createGraphButton.setStyle("-fx-background-color:  rgb(157, 178, 191); -fx-border-color:  rgb(82, 109, 130)" );

        neighboorhoodButton.setStyle("-fx-background-color:  rgb(157, 178, 191); -fx-border-color: rgb(82, 109, 130) ");

        fromMatrix.setStyle("-fx-background-color:  rgb(157, 178, 191); -fx-border-color: rgb(82, 109, 130)");

        createGraphButton.setMaxWidth(Double.MAX_VALUE);
        neighboorhoodButton.setMaxWidth(Double.MAX_VALUE);
        fromMatrix.setMaxWidth(Double.MAX_VALUE);


        neighboorhoodButton.setOnAction(event -> {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/neib.fxml"));
            try {
                Parent neighborhoodRoot = fxmlLoader.load();
                NeighborhoodStage neighborhoodStage = new NeighborhoodStage(primaryStage, neighborhoodRoot, this);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });


        createGraphButton.setOnAction(event -> {
            graphGenerator = new GraphGenerator();
            int[][] graphMatrix = graphGenerator.getGraph();
            if (graphMatrix != null) {
                if (graphStage != null) {
                    graphPane.getChildren().clear();
                }
                graph = new GraphCreator().createGraph(graphMatrix);
                FxViewer viewer = new FxViewer(graph, FxViewer.ThreadingModel.GRAPH_IN_GUI_THREAD);
                viewer.enableAutoLayout();
                FxDefaultView view = (FxDefaultView) viewer.addDefaultView(false);

                graphPane.getChildren().add(view);
                graphStage = new GraphStage(graphMatrix, graph);
                root.setTop(graphStage.getTopPanel());
                root.setCenter(graphPane);
            } else {
                System.out.println("Graph matrix is null.");
            }
        });

        fromMatrix.setOnAction(event -> {
            Label NumberOfVertices = new Label("Number of vertices: ");
            TextField vertexCount = new TextField();
            Button createGraph = new Button("OK");
            HBox hBox = new HBox();
            hBox.getChildren().addAll(NumberOfVertices, vertexCount, createGraph);
            hBox.setSpacing(10);
            hBox.setAlignment(Pos.CENTER);
            root.setTop(hBox);
            graphPane.getChildren().clear();

            createGraph.setOnAction(e -> {
                MatrixScene matrixScene = new MatrixScene(Integer.parseInt(vertexCount.getText()));
                matrixScene.start();
                root.setCenter(matrixScene.getMatrixPane());
                matrixScene.getButton().setOnAction(ev -> {
                    int[][] graphMatrix = matrixScene.getGraphMatrix();
                    if (graphMatrix != null) {
                        if (graphStage != null) {
                            graphPane.getChildren().clear();
                        }
                        graph = new GraphCreator().createGraph(graphMatrix);
                        FxViewer viewer = new FxViewer(graph, FxViewer.ThreadingModel.GRAPH_IN_GUI_THREAD);
                        viewer.enableAutoLayout();
                        FxDefaultView view = (FxDefaultView) viewer.addDefaultView(false);
                        graphPane.getChildren().add(view);
                        graphStage = new GraphStage(graphMatrix, graph);
                        root.setTop(graphStage.getTopPanel());
                        root.setCenter(graphPane);
                    } else {
                        System.out.println("Graph matrix is null.");
                    }
                });
            });
        });

        root = new BorderPane();

        leftPanel = new VBox(10);
        leftPanel.setAlignment(Pos.TOP_LEFT);
        leftPanel.getChildren().addAll(createGraphButton, fromMatrix, neighboorhoodButton);

        root.setLeft(leftPanel);

        graphPane = new StackPane();
        graphPane.setStyle("-fx-background-color: lightgray;");
        graphPane.setAlignment(Pos.CENTER);
        root.setCenter(graphPane);
        root.getLeft().setStyle("-fx-background-color:  DDE6ED;");

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public Graph getGraph() {
        return graph;
    }
    public static void main(String[] args) {
        launch(args);
    }
}
