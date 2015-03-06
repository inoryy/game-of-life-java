public class Board {
    private Cell[][] grid;

    public Board(Cell[][] grid) {
        this.grid = grid;
    }

    /**
     * @param size
     * @param p probability that Cell is alive at start
     */
    public Board(int size, double p) {
        grid = new Cell[size][size];
        // TODO: initialize Cells with p alive probability.
    }

    public Cell[][] getGrid() {
        return grid;
    }

    public int neighboursCountAt(int i, int j) {
        // TODO: Implement this method
        
        return -1;
    }

    public boolean isAlive(int i, int j) {
        return grid[i][j].getState();
    }

    public void update() {
        prepare();
        commit();
    }

    /**
     * Assigns new state to individual Cells 
     * according to GoF rules
     */
    private void prepare() {
        
    }

    /**
     * Updates Cell state based on newState
     */
    private void commit() {
        
    }
}
