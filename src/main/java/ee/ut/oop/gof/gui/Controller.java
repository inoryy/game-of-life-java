package ee.ut.oop.gof.gui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class Controller implements Initializable {
    
    @FXML
    private FlowPane base;
    
    @FXML
    private Button button;
        
    private Rectangle r;
    private int i;
    private Group root;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("test");
        
        root = new Group();
        GridPane grid = new GridPane();

        r = new Rectangle(25,25);
        r.setFill(Color.AQUA);
        r.setX(0);
        grid.getChildren().add(r);
        for (int i = 1; i < 10; i++) {
            Rectangle rect = new Rectangle(25,25);
            rect.setFill(Color.BLUE);
            //rect.setX(25*i);
            grid.getChildren().add(rect);
        }
        
            
        // new Display(root);
        // 
        
        root.getChildren().add(grid);
        base.getChildren().add(root);
    }
    
    @FXML
    private void onDragDetected(Event evt) {
        
    }
    
    @FXML
    private void onRun(Event evt) {
        System.out.println("run");
        i = 0;
        EventHandler<ActionEvent> gameUpdate = event ->
        {
            // board.update();
            // d = new Display(root);
            // d.displayBoard(board); << kuvamine
            // if first: create Rectangles; display them;
            
            if (i % 2 == 0) {
                r.setFill(Color.WHEAT);
            } else {
                r.setFill(Color.BLACK);
            }
            i++;
            System.out.println("testtt");
        };

        Timeline gameLoop = new Timeline(new KeyFrame(Duration.millis(1000), gameUpdate));

        gameLoop.setCycleCount(Animation.INDEFINITE);
        gameLoop.play();

    }
}