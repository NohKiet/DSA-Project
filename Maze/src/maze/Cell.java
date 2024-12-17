package maze;

public class Cell {
    int row, col;
    char value;
    boolean blocked = false;
    boolean visited = false;
    Cell previous = null;

    public Cell(int row, int col, char value) {
        this.row = row;
        this.col = col;
        this.value = value;
    }
    
    public void setBlock() {
        this.blocked = true;
    }

    public boolean canBeVisited() {
        return !blocked && !visited;
    }
    
    @Override
    public String toString() {
        return "(" + row + "," + col + ", " + value + ")";
    }
}