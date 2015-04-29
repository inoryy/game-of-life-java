package gof.core;

public class Board {
    private Cell[][] grid;
    private int height=3; //bottom right pos: grid[height-1][width-1]
    private int width=3;

    public Board(Cell[][] grid) {
        this.grid = grid;
        height = width = grid.length;
    }

    /**
     * @param height
     * @param width
     * @param p probability that Cell is alive at start
     */
    public Board(int height, int width, double p) {
        this.height=height;
        this.width = width;
        grid = new Cell[height][width];
        
        for (int h=0; h<grid.length; h++){
            for (int w=0; w<grid[h].length; w++){
                grid[h][w] = new Cell();
                if (Math.random()<=p){
                    grid[h][w].setNewState(true);
                    grid[h][w].updateState();
                }
            }
        }
    }

    public Cell[][] getGrid() {
        return grid;
    }

    public int neighboursCountAt(int row, int col) {
        int sum=0;
        // Positions numbered as phone dial
        if (row != 0 && col != 0){    //1
            if(isAlive(row-1,col-1)){
                sum++;
            }
        }
        
        if (row != 0){
            if(isAlive(row-1,col)){ //2
            sum++;
            }
        }
        
        if (row != 0 && col != width-1){//3
            if(isAlive(row-1,col+1)){
                sum++;
            }
        }
        if (col != 0){
            if(isAlive(row,col-1)){ //4
            sum++;
            }
        }
        //self
        if (col != width-1){
            if(isAlive(row,col+1)){ //6
                sum++;
            }
        }

        if (row != height-1 && col != 0){
            if(isAlive(row+1,col-1)){ //7
                sum++;
            }
        }

        if (row != height-1){
            if(isAlive(row+1,col)){ //8
            sum++;
            }
        }

        if (row != height-1 && col != width-1){
            if(isAlive(row+1,col+1)){ //9
                sum++;
            }
        }

        return sum;
    }

    public boolean isAlive(int row, int col) {
        return grid[row][col].getState();
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
        for (int h=0; h<grid.length; h++){
            for (int w=0; w<grid[h].length; w++){
                int nr = neighboursCountAt(h,w);                
                if (nr < 2) { grid[h][w].setNewState(false);}  //underpop
                else if (nr > 3) { grid[h][w].setNewState(false);} //overcrowd
                else if (nr == 3) { grid[h][w].setNewState(true);} //born
                else if (nr == 2) { grid[h][w].setNewState(grid[h][w].getState());} // stay same
            }
        }
    }

    /**
     * Updates Cell state based on newState
     */
    private void commit() {
        for (int h=0; h<grid.length; h++){
            for (int w=0; w<grid[h].length; w++){
                grid[h][w].updateState();
            }
        }
    }
    
    
}
