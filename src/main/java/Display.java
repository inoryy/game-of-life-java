
public class Display {
    public static DisplayDriver getDriver() {
        return new NullDriver();
    }
    
    private Display() {}

    private static class NullDriver implements DisplayDriver {
        public void displayBoard(Board board) {
            
        }
    }
}