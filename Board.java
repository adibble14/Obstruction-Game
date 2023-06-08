import java.util.ArrayList;
import java.util.Arrays;

/**
 * Source code that models the game board
 */
public class Board {

    private char[] myBoard;
    private static int numCols;
    private static int numRows;

    public Board(String theSize){
        numCols = Integer.parseInt(String.valueOf(theSize.charAt(2)));
        numRows = Integer.parseInt(String.valueOf(theSize.charAt(0)));
        myBoard = initializeBoard();
        outputBoard(myBoard);
    }

    /**
     * @return an array that acts as the board, filled with all '-' to symbolize empty squares
     */
    public char[] initializeBoard(){
        char[] theBoard;
        if (numCols == 6){
            theBoard = new char[36];
        }else if(numCols == 7){
            theBoard = new char[42];
        }else{
            if(numRows == 7){
                theBoard = new char[56];
            }else{
                theBoard = new char[64];
            }
        }
        Arrays.fill(theBoard, '-');
        return theBoard;
    }

    /**
     *output the current state of the board
     */
    public void outputBoard(char[] theBoard){
        int col = 1;
        if(numCols == 6) {
            System.out.println("  1 2 3 4 5 6");
        }else if(numCols == 7){
            System.out.println("  1 2 3 4 5 6 7");
        }else{
            System.out.println("  1 2 3 4 5 6 7 8");
        }

        for(int i = 0; i < myBoard.length; i++){
            if(i % numCols == 0){
                System.out.print(col + " " + theBoard[i] + " ");
                col++;
            }else{
                System.out.print(theBoard[i] + " ");
                if(i % numCols == numCols-1){
                    System.out.println();
                }
            }
        }
        System.out.println();
    }

    /**
     * @param theCoordinates gets coordinates as comma seperated integers
     * @return the value in the square at those coordinates
     */
    public char getSquare(String theCoordinates){
        int row = Integer.parseInt(String.valueOf(theCoordinates.charAt(0)));
        int col = Integer.parseInt(String.valueOf(theCoordinates.charAt(2)));

        return myBoard[((row-1)*numCols)+(col-1)];
    }

    /**
     * @return if the game is over or not
     */
    public boolean gameOver(){
        boolean gameOver = true;
        for (char c : myBoard) {
            if (c == '-') {
                gameOver = false;
                break;
            }
        }
        return gameOver;
    }

    public char[] getMyBoard() {
        return myBoard;
    }

    public void setMyBoard(char[] theBoard){myBoard = theBoard;}

    @Override
    public String toString() {
        return Arrays.toString(myBoard);
    }

    public static int getNumCols(){
        return numCols;
    }

    /**
     *sets the specified coordinate with the specified symbol, also blocks out nearby squares
     */
    public static void setBoardCoordinate(char[] theBoard, String theCoordinates, char theSymbol){
        int row = Integer.parseInt(String.valueOf(theCoordinates.charAt(0)));
        int col = Integer.parseInt(String.valueOf(theCoordinates.charAt(2)));

        theBoard[((row-1)*numCols)+(col-1)] = theSymbol;

        if(row - 1 > 0){
            theBoard[((row-2)*numCols)+(col-1)] = '/';
            if(col + 1 < numCols+1)
                theBoard[((row-2)*numCols)+(col)] = '/';
            if(col - 1 > 0)
                theBoard[((row-2)*numCols)+(col-2)] = '/';
        }
        if(row < numRows){
            theBoard[((row)*numCols)+(col-1)] = '/';
            if(col + 1 < numCols+1)
                theBoard[((row)*numCols)+(col)] = '/';
            if(col - 1 > 0)
                theBoard[((row)*numCols)+(col-2)] = '/';
        }
        if(col - 1 > 0){
            theBoard[((row-1)*numCols)+(col-2)] = '/';
        }
        if(col + 1 < numCols+1){
            theBoard[((row-1)*numCols)+(col)] = '/';
        }
    }

}

/**
 * each node contains a Board object with other info about its heuristics and ancestors
 */
class Node{
    public Node myParent;
    public char[] myBoard;
    public int myUtilityValue;
    public int myDepth;
    public ArrayList<Node> myChildren;

    public Node(char[] theBoard, Node theParent, int theUtilityValue, int theDepth, ArrayList<Node> theChildren){
        myBoard = theBoard;
        myParent = theParent;
        myUtilityValue = theUtilityValue;
        myDepth = theDepth;
        myChildren = theChildren;
    }
}