import java.util.Scanner;

public class GameOfLife {

    public static void main(String[] args) throws Exception {
        System.out.print("Please enter number of iterations to run: ");
        Scanner in = new Scanner(System.in);
        int iterations = in.nextInt();
        in.close();
        
        DisplayDriver dd = Display.getDriver();
        Board b = new Board(10, 10, 0.3);

        for (int i = 0; i <= iterations; i++) {
            dd.displayBoard(b);
            b.update();
            Thread.sleep(300);
        }
    }

}
