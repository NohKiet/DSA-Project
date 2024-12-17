package maze;
import java.io.File;
import java.io.FileReader; // for reading characters
import java.io.BufferedReader;// for reading lines
import java.util.LinkedList; // for a stack
import java.util.ArrayList; // for storing adjacent cells

public class Maze {
    char entryChar = 'E', destChar ='M';
    char emptyChar ='0', blockedChar = '1';
    int rows=0, cols=0; // size of the maze
    Cell[][] cells = null; // map of the maze
    Cell entryCell = null;      // entry position of the maze
    Cell destCell = null;   // exit cell or destination cell
    boolean completed = false; // solving completed or not
    boolean succeeded = false; // solving succeeded or not
    
    // Constructor using default characters
    public Maze() {
    }
    
    // Constructor using specified characters
    public Maze(char entryChar, char destChar,
               char emptyChar, char blockedChar) {
        this.entryChar = entryChar;
        this.destChar = destChar;
        this.emptyChar = emptyChar;
        this.blockedChar = blockedChar;
    }

    public void print() {
        for (int i= 0; i<rows; i++) {
            for (int j= 0; j<cols; j++) {
                System.out.print(cells[i][j].value);
            }
            System.out.println();
        }
    }

    //Testing whether a position is valid or not
    private boolean isValid (int row, int col) {
        return row >=0 && row < rows && //it is in
               col >=0 && col < cols && // and the
               cells[row][col].canBeVisited(); // can be visited
    }

    private ArrayList<Cell> getAdjs (Cell curCell) {
        ArrayList<Cell> adjs = new ArrayList();
        int row = curCell.row, col = curCell.col;
        // Testing 4 adjacency cells of the current cell
        if (isValid(row-1,col)) { // UPPER adjacency cell
            cells[row-1][col].previous = curCell;
            adjs.add(cells[row-1][col]);
        }
        if (isValid(row+1,col)) { // LOWER adjacency cell
            cells[row+1][col].previous = curCell;
            adjs.add(cells[row+1][col]);
        }
        if (isValid(row,col-1)) { // LEFT adjacency cell
            cells[row][col-1].previous = curCell;
            adjs.add(cells[row][col-1]);
        }
        if (isValid(row,col+1)) { // RIGHT adjacency cell
            cells[row][col+1].previous = curCell;
            adjs.add(cells[row][col+1]);
        }
        return adjs;
    }

    // Load a maze from a text file
    public boolean loadFromFile (String filename) {
        File f = new File(filename);
        if (!f.exists()) { // checking existence of the file
            System.out.println("The file " + filename + " doesn't existed!");
            System.exit(0);
        }
        try {
            FileReader fr = new FileReader(f); //reading characters
            BufferedReader bf = new BufferedReader(fr);// reading lines
            ArrayList<String> list = new ArrayList(); // File --> string list
            String line;
            // Loading all lines in the file to list
            while ((line = bf.readLine())!=null) {
                line = line.trim(); // normalizing each line
                if (line.length()>0) list.add(line.toUpperCase());
            }
            // Closing the file
            bf.close();
            fr.close();
            
            // Creating the matrix from the list
            this.rows = list.size(); // Determining number of rows
            this.cols = list.get(0).length(); // Determining number of columns
            this.cells = new Cell[rows][cols];// Allocating memory of matrix
            for (int i= 0; i< list.size(); i++) { // Creating cells
                line = list.get(i); // Getting a line: 101M1010101
                for (int j= 0; j<cols; j++) { // Creating cells in the row i
                    char ch = line.charAt(j); //101M1010101
                    cells[i][j] = new Cell(i, j, ch);
                    if (ch==blockedChar) cells[i][j].setBlock();
                    // Determining entry and destination of the maze
                    else if (ch==entryChar) this.entryCell = cells[i][j];
                    else if (ch==destChar) this.destCell = cells[i][j];
                }
            }
        }
        catch(Exception e) {
            System.out.println(e);
            System.exit(0);
        }
        return true;
    }
 // Solving the maze
    public boolean solve() {
        LinkedList<Cell> stack = new LinkedList(); // Initialize the stack
        Cell curCell = this.entryCell; // start from the entry
        while (!completed) { // finding a solution
            curCell.visited = true; // marking curCell as visited
            if (curCell == this.destCell) { // succeeded
                completed = succeeded = true;
            }
            else {
                ArrayList<Cell> adjs = getAdjs(curCell);
                // if there are adjacency cells to move to
                if (adjs.size()>0) {
                    curCell = adjs.get(0); // move to the next cell
                    for (int i= 1; i< adjs.size(); i++)// save other cells
                        stack.addFirst(adjs.get(i));
                }
                // else if stack contains a cell which can be tried
                else if (!stack.isEmpty()) // if having a cell to try
                    curCell = stack.removeFirst();//Popping out a cell
                else { // stack is empty --> No solution, failed!
                    completed = true;
                    succeeded = false;
                }
            }
        }
        return completed;
    }

    public LinkedList<Cell> getPath() {
        if (!succeeded) return null;
        LinkedList<Cell> path = new LinkedList();
        // Reverse traversing to get the result path
        Cell cell = this.destCell;
        while (cell!=null) {
            path.addFirst(cell);
            cell = cell.previous;
        }
        return path;
    }
    } // Maze class