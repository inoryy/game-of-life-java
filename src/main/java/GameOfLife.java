
public class GameOfLife {

    public static void main(String[] args) throws Exception {
        int iterations = 1000;
        
        DisplayDriver dd = Display.getDriver();
        Board b = new Board(10, 10, 0.3);
        
        for (int i = 0; i <= iterations; i++) {
            dd.displayBoard(b);
            Thread.sleep(500);
        }
    }

}
