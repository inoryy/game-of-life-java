import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;

import ee.ut.oop.gof.core.Board;
import ee.ut.oop.gof.core.Cell;
import ee.ut.oop.gof.core.DisplayDriver;

public class Display {
    public static DisplayDriver getDriver() {
        DisplayDriver driver;
        
        try {
            driver = new EclipseDriver();
        } catch (AWTException e) {
            // TODO: implement a better fall-back
            driver = new NullDriver();
        }
        
        return driver;
    }
    
    private Display() {}

    private static class EclipseDriver implements DisplayDriver {
        private Robot eclipse;
        
        public EclipseDriver() throws AWTException {
            eclipse = new Robot();
        }
        
        public void displayBoard(Board board) {
            cleanConsole();
            Cell[][] grid = board.getGrid();
            
            String border = String.format("+%0" + 2*grid.length + "d+", 0).replace("0","-");
            
            System.out.println(border);
            
            for (Cell[] row : grid) {
                String r = "|";
                for (Cell c : row) {
                    r += c.getState() ? "* " : "  ";
                }
                r += "|";
                System.out.println(r);
            }
            
            System.out.println(border);
        }
        
        private void cleanConsole() {
            eclipse.keyPress(KeyEvent.VK_SHIFT);
            eclipse.keyPress(KeyEvent.VK_F10);
            eclipse.keyRelease(KeyEvent.VK_SHIFT);
            eclipse.keyRelease(KeyEvent.VK_F10);
            eclipse.keyPress(KeyEvent.VK_R);
            eclipse.keyRelease(KeyEvent.VK_R);
        }
    }
    
    private static class NullDriver implements DisplayDriver {

        public void displayBoard(Board board) {
            System.out.println("Game of Life");
        }
        
    }
}