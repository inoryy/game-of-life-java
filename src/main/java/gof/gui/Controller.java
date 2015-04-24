package gof.gui;

import gof.console.ConsoleDriver;
import gof.core.Board;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.util.Duration;

public class Controller implements Initializable {
    
    @FXML
    private FlowPane base;
    
    @FXML
    private Button runButton;
    
    private Board board;
    
    private JavaFXDisplayDriver display;
    private ConsoleDriver console = null;
    
    private Timeline loop = null;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	createBoard();
    }

    @FXML
    private void onRandomize(Event evt) {
    	createBoard();
    }
    
    @FXML
    private void onRun(Event evt) {
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
    	loop.stop();
    }
    
    private void createBoard() {
        board = new Board(10, 10, 0.3);
        
        // for debugging
        // console = new ConsoleDriver();
        // console.displayBoard(board);

        display = new JavaFXDisplayDriver(10, 30, board);

        base.getChildren().clear();
        base.getChildren().add(new Group(display.getPane()));
    }
}