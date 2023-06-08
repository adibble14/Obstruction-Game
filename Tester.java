import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A tester file that connects your Board and Solver code
 */
public class Tester {
    private static String mySize; //board (grid) size options are 6(rows) x 6(columns) and 6 x 7
    private static String mySearchMethod; //can be MM (for minimax) or AB (for minimax with alpha-beta pruning)
    private static Board myBoard;
    private static char playersSymbol;
    private static char aiSymbol;
    private static Node root;
    private static int myDepth;
    private static ArrayList<Node> myRootChildren;
    private static int rootUtility;
    static boolean playersTurn; //tracking whose turn it is


    public static void main(String[] args) {

        //getting input ex: 1 MM 6*6
        String myPlayer = args[0];
        mySearchMethod = args[1];
        mySize = args[2];

        myBoard = new Board(mySize);

        if (Objects.equals(myPlayer, "1")) {
            System.out.println("Player 1: AI");
            System.out.println("Player 2: Human");
            playersSymbol = 'X';
            aiSymbol = 'O';
            playersTurn = false;
        } else {
            System.out.println("Player 1: Human");
            System.out.println("Player 2: AI");
            playersSymbol = 'O';
            aiSymbol = 'X';
            playersTurn = true;
        }

        Solver mySolver = new Solver(myBoard);
        gamePlay(mySolver);

        //seeing who won
        if (playersTurn) {
            System.out.println("You lose. Better luck next time!");
        } else {
            System.out.println("Congrats! You win!!");
        }

        output(mySolver);//outputting to read me file

    }


    /**
     * gets user's input and updates the board
     */
    public static void getInput() {
        System.out.println("It's your turn! Enter coordinates to place your symbol. Ex) 3,4");
        Scanner input = new Scanner(System.in);
        String coordinates;

        boolean validInput = false;
        while (!validInput) {
            coordinates = input.next();
            if (Objects.equals(mySize, "6*6"))
                validInput = patternMatcherHelper(coordinates, "[1-6],[1-6]");
            else if (Objects.equals(mySize, "6*7"))
                validInput = patternMatcherHelper(coordinates, "[1-6],[1-7]");
            else if (Objects.equals(mySize, "7*8"))
                validInput = patternMatcherHelper(coordinates, "[1-7],[1-8]");
            else
                validInput = patternMatcherHelper(coordinates, "[1-8],[1-8]");

            if (!validInput) {
                System.out.println("Please enter coordinates within the correct range and seperated by a comma. Try again.");
            } else if (myBoard.getSquare(coordinates) == '/' || myBoard.getSquare(coordinates) == 'X' || myBoard.getSquare(coordinates) == 'O') { //you can't play there
                System.out.println("You can't play at that coordinate. Enter new coordinates.");
                validInput = false;
            } else { //valid coordinate, placing symbol
                Board.setBoardCoordinate(myBoard.getMyBoard(), coordinates, playersSymbol);
            }

        }

    }

    /**
     *the method that handles the playing of the game
     */
    public static void gamePlay(Solver theSolver) {
        int turns = 1; //keeping track of number of turns
        myDepth = 0;
        myRootChildren = new ArrayList<>();
        rootUtility = -1;
        //while there are still other spots to play
        while (!myBoard.gameOver()) {
            if (playersTurn) {
                getInput(); //getting user input
                myBoard.outputBoard(myBoard.getMyBoard()); //outputting the updated board
                playersTurn = false;
                if (turns != 1) {
                    turns++;
                }
            } else {
                if (turns % 4 == 1 || turns % 4 == 0) { //every 4th turn, depending on who starts it could be equal to 0 or 1
                    if (root != null) {
                        for (Node n : root.myChildren) {
                            if (Objects.equals(Solver.convertBoardToString(n.myBoard), Solver.convertBoardToString(myBoard.getMyBoard()))) {
                                root = n;
                                rootUtility = root.myUtilityValue;
                                myDepth = root.myDepth;
                                myRootChildren = root.myChildren;
                                break;
                            }
                        }
                    }
                    if (Objects.equals(mySearchMethod, "MM")) { //minimax
                        theSolver.generateTree(new Node(myBoard.getMyBoard(), root, rootUtility, myDepth, myRootChildren));
                    } else { //alpha beta pruning
                        theSolver.generateTreeAB(new Node(myBoard.getMyBoard(), root, rootUtility, myDepth, myRootChildren));
                    }
                }
                //telling the AI to make a move
                theSolver.aiMove(turns); //telling the AI to make a move

                playersTurn = true;
                turns++;
            }
        }
    }

    /**
     * creates the solution path once a solution has been found, outputs data to a file called "Readme.txt"
     */
    public static void output(Solver theSolver) {
        try {
            FileWriter myWriter = new FileWriter("Readme.txt");
            BufferedWriter bw = new BufferedWriter(myWriter);
            if (Objects.equals(mySearchMethod, "MM")) {
                bw.write("Minimax Algorithm");
            } else {
                bw.write("Minimax Algorithm with Alpha-Beta Pruning");
            }
            bw.newLine();
            bw.write("Evaluation function: the number of empty squares left");
            bw.newLine();
            bw.write("Number nodes expanded: " + theSolver.getNumNodesExpanded());
            bw.newLine();
            bw.write("Largest depth level: " + theSolver.getLargestDepth());
            bw.newLine();
            bw.close();
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

    }

    public static char getAiSymbol() {
        return aiSymbol;
    }

    public static char getPlayersSymbol() {
        return playersSymbol;
    }

    public static void setRoot(Node root) {
        Tester.root = root;
    }

    public static void setMyDepth(int myDepth) {
        Tester.myDepth = myDepth;
    }

    public static void setRootUtility(int rootUtility) {
        Tester.rootUtility = rootUtility;
    }

    public static void setMyRootChildren(ArrayList<Node> myRootChildren) {
        Tester.myRootChildren = myRootChildren;
    }

    /**
     * checking if a string matches with a specified regex
     */
    public static boolean patternMatcherHelper(String str, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);

        boolean found = false;
        while (matcher.find()) {
            found = true;
        }

        return found;
    }
}

