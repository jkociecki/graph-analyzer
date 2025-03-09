package com.example.lab08v1;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;

public class MatrixScene {
    Button button;
    private int size;
    private TextField[][] textFields;
    private GridPane gridPane;

    public MatrixScene(int size) {
        this.size = size;
        this.textFields = new TextField[size][size];
    }


    public GridPane start() {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10));
        gridPane.setVgap(5);
        gridPane.setHgap(5);

        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {

                if(row==col){
                    TextField textField = new TextField("0");
                    textFields[row][col] = textField;
                    gridPane.add(textField, col, row);
                }else {
                    TextField textField = new TextField();
                    textFields[row][col] = textField;
                    gridPane.add(textField, col, row);
                }
            }
        }

        button = new Button("OK");
        button.setOnAction(event -> {
            int[][] matrix = readMatrixFromTextFields();
            GraphCreator graphCreator = new GraphCreator();
            Graph graph = graphCreator.createGraph(matrix);


        });

        HBox buttonBox = new HBox(button);
        buttonBox.setPadding(new Insets(10));

        gridPane.add(buttonBox, 0, size);
        this.gridPane= gridPane;
        return gridPane;
    }

    private int[][] readMatrixFromTextFields() {
        int[][] matrix = new int[size][size];
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                TextField textField = textFields[row][col];
                String text = textField.getText();
                try {
                    int value;
                    if (row == col) {
                        value = 0;  // Ustawienie przekątnej na 0
                    } else {
                        value = Integer.parseInt(text);
                    }
                    matrix[row][col] = value;
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        }
        return matrix;
    }


    private void printMatrix(int[][] matrix) {
        for (int[] row : matrix) {
            for (int value : row) {
                System.out.print(value + " ");
            }
            System.out.println();
        }
    }

    public int[][] getMatirx() {
        return readMatrixFromTextFields();
    }

    public int[][] getGraphMatrix() {
        return readMatrixFromTextFields();
    }
    public Button getButton(){
        return button;
    }

    public Node getMatrixPane() {
        return gridPane;
    }
}
