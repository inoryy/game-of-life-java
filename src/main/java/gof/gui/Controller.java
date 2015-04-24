package gof.gui;

import gof.console.ConsoleDriver;
import gof.core.Board;
import gof.core.Cell;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class Controller implements Initializable {
    
    @FXML
    private FlowPane base;
    
    @FXML
    private Label countLabel;
    @FXML
    private Slider countSlider;
    @FXML
    private TextField rowsField;
    @FXML
    private TextField colsField;
    @FXML
    private Button setButton;
    @FXML
    private Button leftButton;
    @FXML
    private Button rightButton;
    @FXML
    private Pane presetBox = new Pane();
    @FXML
    private Button openButton;
    @FXML
    private Button saveButton;
    @FXML
    private Button runButton;
    @FXML
    private Button stopButton;
    @FXML
    private Button randomizeButton;
    
    private Board board;
    
    private JavaFXDisplayDriver display;
    private ConsoleDriver console = null;
    
    private Timeline loop = null;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	createBoard(10, 10, 0.3);
    }

    @FXML
    private void onRandomize(Event evt) {
    	createBoard(10, 10, 0.3);
    }
    
    @FXML
    private void onRun(Event evt) {
        /////////// DISABLE/ENABLE BLOCK ///////////
        countSlider.setDisable(true);
        rowsField.setDisable(true);
        colsField.setDisable(true);
        setButton.setDisable(true);
        leftButton.setDisable(true);
        rightButton.setDisable(true);
        presetBox.setDisable(true);
        openButton.setDisable(true);
        saveButton.setDisable(true);
        runButton.setDisable(true);
        randomizeButton.setDisable(true);
        stopButton.setDisable(false);
        /////////// END OF DISABLE/ENABLE BLOCK ///////////
        loop = new Timeline(new KeyFrame(Duration.millis(300), e -> {
        	board.update();
        	display.displayBoard(board);
        	if (console != null) {
        		console.displayBoard(board);
        	}
        }));
        loop.setCycleCount(100);
        loop.play();
    }
    
    @FXML
    private void onStop(Event evt) {
        /////////// DISABLE/ENABLE BLOCK ///////////
        countSlider.setDisable(false);
        rowsField.setDisable(false);
        colsField.setDisable(false);
        setButton.setDisable(false);
        leftButton.setDisable(false);
        rightButton.setDisable(false);
        presetBox.setDisable(false);
        openButton.setDisable(false);
        saveButton.setDisable(false);
        runButton.setDisable(false);
        randomizeButton.setDisable(false);
        stopButton.setDisable(true);
        /////////// END OF DISABLE/ENABLE BLOCK ///////////
    	loop.stop();
    	stopButton.setDisable(true);
    }
    
    @FXML
    private void onSet(Event evt) {
        System.out.println("action not set");
    }
    
    @FXML
    private void onOpen(Event evt) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Game of Life Board File");
        fileChooser.getExtensionFilters().add(new ExtensionFilter("GOFB files (*.gofb)", "*.gofb"));
        File selectedFile = fileChooser.showOpenDialog(new Stage());
        while (selectedFile == null) {
            selectedFile = fileChooser.showOpenDialog(new Stage());
        }
        
       try {
           Scanner s = new Scanner(selectedFile);
           int rows = 0;
           int cols = 0;
           String input = "";
           while(s.hasNextLine()){
               String line = s.nextLine();
               if (cols == 0){
                   cols = line.length();
               }
               line.replaceAll("\\s+","");
               input+=line;
               rows++;
           }
           
           int pos = 0;
           createBoard(rows,cols,0);
           Cell[][] g = board.getGrid();
           for (int i = 0; i < g.length; i++) {
               for (int j = 0; j < g[0].length; j++) {
                   char c = input.charAt(pos);
                   //boolean state = (int) c == 1 ? true : false;
                   boolean state;
                   if (c =='1'){
                       state = true;
                   } else {
                       state = false;
                   }
                   g[i][j].setNewState(state);
                   pos++;
               }
           }
           
           // UPDATE, COMMIT NEW STATE
           
           board = new Board(g);

           display = new JavaFXDisplayDriver(10, 30, board);

           base.getChildren().clear();
           base.getChildren().add(new Group(display.getPane()));
           //createBoard(rows,cols, 0);
       } catch (FileNotFoundException e) {
           e.printStackTrace();
       }
       
        //check if valid file (correct number of cells for rectangle shaped board)

        
    }
    
    @FXML
    private void onSave(Event evt) {
        String output = ""; // string of numbers from board
        
        Cell[][] g = board.getGrid();
        for (int i = 0; i < g.length; i++) {
            for (int j = 0; j < g[0].length; j++) {
                output+= g[i][j].getState() ? 1 : 0;
            }
            if (i != g.length-1){
                output+="\n";
            }
        }
        
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("GOFB files (*.gofb)", "*.gofb");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showSaveDialog(new Stage());
        
        if(file != null){
            try {
                FileWriter fileWriter = null;
                 
                fileWriter = new FileWriter(file);
                fileWriter.write(output);
                fileWriter.close();
            } catch (IOException ex) {
                System.out.println(ex);
            }
        }
    }
    
    
    @FXML
    private void onLeft(Event evt) {
        System.out.println("action not set");
    }
    
    @FXML
    private void onRight(Event evt) {
        System.out.println("action not set");
    }
    
    @FXML // REMOVE THIS METHOD AND USE LISTENER(?)
    private void onSlide(Event evt) {
        System.out.println("action not set");
    }
    
    /* BROKEN SLIDER LISTENER, NEEDS FIX
    countSlider.valueProperty().addListener(new ChangeListener<Number>() {
        @Override
        public void changed(ObservableValue<? extends Number> observable,
                Number oldValue, Number newValue) {
            countLabel.setText(newValue.intValue()+"%");
            createBoard(10, 10, newValue.intValue()/100);
        }
    });
    */
    private void createBoard(int rows, int cols, double prob) {
        //board = new Board(10, 10, 0.3);
        board = new Board(rows, cols, prob);
        // for debugging
        // console = new ConsoleDriver();
        // console.displayBoard(board);

        display = new JavaFXDisplayDriver(10, 30, board);

        base.getChildren().clear();
        base.getChildren().add(new Group(display.getPane()));
    }
}