package gof.gui;

import gof.core.Board;
import gof.core.Cell;
import gof.core.DisplayDriver;
import javafx.scene.Group;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class JavaFXDisplayDriver implements DisplayDriver {
    private int sz;
    private TilePane tilePane = new TilePane(5,5);

    public JavaFXDisplayDriver(int boardSize, int cellSizePx, Board board) {
        sz = boardSize;
        tilePane.setPrefRows(boardSize);
        tilePane.setPrefColumns(boardSize);

        Cell[][] g = board.getGrid();
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                Color c = g[i][j].getState() ? Color.STEELBLUE : Color.WHITE;
                Rectangle r = new Rectangle(cellSizePx, cellSizePx, c);
                tilePane.getChildren().add(r);
                int ii = i;
                int jj = j;
                boolean newState = g[i][j].getState() ? false : true;
                r.setOnMousePressed(event -> {
                    r.setFill(Color.GRAY);

                });

                r.setOnMouseClicked(event -> {
                    r.setFill(g[ii][jj].getState() ? Color.WHITE : Color.STEELBLUE);
                    g[ii][jj].setNewState(newState);
                    g[ii][jj].updateState();

                });


            }
        }
    }

    @Override
    public void displayBoard(Board board) {
        Cell[][] g = board.getGrid();
        for (int i = 0; i < g.length; i++) {
            for (int j = 0; j < g[0].length; j++) {
                Rectangle r = (Rectangle) tilePane.getChildren().get(boardToPaneCoords(i, j));
                r.setFill(g[i][j].getState() ? Color.STEELBLUE : Color.WHITE);
            }
        }
    }

    public TilePane getPane() {
        return tilePane;
    }

    private int boardToPaneCoords(int i, int j) {
        return i * sz + j;
    }
}
