package ee.ut.oop.gof.gui;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

// java 8 code
public class DynamicTiles extends Application {
    int i = 0;
    
    //Class containing grid (see below)
    private GridDisplay gridDisplay;

    //Class responsible for displaying the grid containing the Rectangles
    public class GridDisplay {

        private static final double ELEMENT_SIZE = 100;
        private static final double GAP = ELEMENT_SIZE / 10;

        private TilePane tilePane = new TilePane();
        private Group display = new Group(tilePane);
        private int nRows;
        private int nCols;

        public GridDisplay(int nRows, int nCols) {
            tilePane.setStyle("-fx-background-color: rgba(255, 215, 0, 0.1);");
            tilePane.setHgap(GAP);
            tilePane.setVgap(GAP);
            setColumns(nCols);
            setRows(nRows);
        }

        public void setColumns(int newColumns) {
            nCols = newColumns;
            tilePane.setPrefColumns(nCols);
            createElements();
        }

        public void setRows(int newRows) {
            nRows = newRows;
            tilePane.setPrefRows(nRows);
            createElements();
        }

        public Group getDisplay() {
            return display;
        }

        private void createElements() {
            tilePane.getChildren().clear();
            for (int i = 0; i < nCols; i++) {
                for (int j = 0; j < nRows; j++) {
                    tilePane.getChildren().add(createElement());
                }
            }
        }

        private Rectangle createElement() {
            Rectangle rectangle = new Rectangle(ELEMENT_SIZE, ELEMENT_SIZE);
            //rectangle.setStroke(Color.ORANGE);
            rectangle.setFill(Color.STEELBLUE);

            return rectangle;
        }

    }

    @Override
    public void start(Stage primaryStage) {

        //Represents the grid with Rectangles
        gridDisplay = new GridDisplay(4, 4);

        BorderPane mainPanel = new BorderPane();
        mainPanel.setCenter(gridDisplay.getDisplay());
        //mainPanel.setTop(fields);
        
        EventHandler<ActionEvent> gameUpdate = event ->
        {
            // new Display(root);
            // dd.displayBoard(board);
            // if before
            // if first: create Rectangles; display them;
            
            
            if (i % 2 == 0) {
                TilePane t = (TilePane) gridDisplay.getDisplay().getChildren().get(0);
                ((Rectangle)t.getChildren().get(5)).setFill(Color.WHEAT);
            } else {
                TilePane t = (TilePane) gridDisplay.getDisplay().getChildren().get(0);
                ((Rectangle)t.getChildren().get(5)).setFill(Color.BLUE);
            }
            i++;
            System.out.println("testtt");
        };

        Timeline gameLoop = new Timeline(new KeyFrame(Duration.millis(1000), gameUpdate));

        gameLoop.setCycleCount(Animation.INDEFINITE);
        gameLoop.play();
        
        Scene scene = new Scene(mainPanel, 1000, 800);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}