package ee.ut.oop.gof.gui;

import ee.ut.oop.gof.core.Board;
import ee.ut.oop.gof.core.Cell;
import ee.ut.oop.gof.core.DisplayDriver;
import javafx.scene.Group;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Creates Rectangles
 * 
 */
public class Display implements DisplayDriver {

    private static final double ELEMENT_SIZE = 50;
    private static final double GAP = ELEMENT_SIZE / 5;

    private TilePane tilePane = new TilePane();
    private Group display = new Group(tilePane);
    private int nRows; //number of Rows
    private int nCols; // number of columns

    public Display(int nRows, int nCols) {
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
        rectangle.setFill(Color.STEELBLUE);

        return rectangle;
    }

    @Override
    public void displayBoard(Board board) {
        // TODO Auto-generated method stub
        // TODO: Mihkel tee valmis
        // board.getGrid()
        Cell[][] g = board.getGrid();
        for (int i = 0; i < g.length; i++) {
            for (int j = 0; j < g[0].length; j++) {
                // i,j -> x:
                // c = Cell[i][j] -> tilePane[x]
                // tilePane[x].setFill(c.getState() ? Color.Black : Color.White);
            }
        }
    }

}
