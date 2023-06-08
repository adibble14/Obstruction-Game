import java.util.*;

/**
 * Source code that implements minimax and minimax with
 * alpha-beta pruning
 */
public class Solver {
    private final Board myBoard;
    private final ArrayList<Node> myGameTree;
    private Node root;
    private Node myBestMove;
    private int firstDepth = 1;
    private int secondDepth = 2;
    private int thirdDepth = 3;
    private int fourthDepth = 4;
    private int numNodesExpanded = 0;
    private int largestDepth = 0;

    public Solver(Board theBoard) {
        myBoard = theBoard;
        myGameTree = new ArrayList<>();
    }

    /**
     * making the AI move based on what algorithm is specified
     */
    public void aiMove(int turnNum) {
        findBestMove(turnNum);
        myBoard.setMyBoard(correctDepthMove(turnNum)); //finding the best move and updating it
        myBoard.outputBoard(myBoard.getMyBoard()); //outputting

        if (turnNum % 4 != 0 && turnNum % 4 != 1) {
            Tester.setRoot(root);
            Tester.setMyDepth(root.myDepth);
            Tester.setMyRootChildren(root.myChildren);
            Tester.setRootUtility(root.myUtilityValue);
        }
    }

    /**
     * finding the best move for the AI to make
     */
    public void findBestMove(int theTurnNum) {
        if (theTurnNum % 4 != 0 && theTurnNum % 4 != 1) {
            findRoot();
        }
        //myQueue.add(root);
        Queue<Node> myQueue = new LinkedList<>(root.myChildren);
        int maxValue = 0;
        while (!myQueue.isEmpty()) {
            Node temp = myQueue.remove();
            numNodesExpanded++;
            if (temp.myDepth > largestDepth) {
                largestDepth = temp.myDepth;
            }
            if (temp.myUtilityValue > maxValue) {
                if (temp.myUtilityValue == Integer.MAX_VALUE) { //the AI can win
                    myBestMove = temp;
                    return;
                }
                maxValue = temp.myUtilityValue;
                myBestMove = temp;
            }
            myQueue.addAll(temp.myChildren);
        }
        if (myBestMove.myUtilityValue < theTurnNum) { //there is no scenario where the AI can win, so it just chooses the first option
            myBestMove = root.myChildren.get(0);
        }
    }

    /**
     * backtracking through the tree to get the next move
     */
    public char[] correctDepthMove(int theTurnNum) {
        while (true) {
            if (myBestMove.myDepth == theTurnNum) {
                root = myBestMove;
                return myBestMove.myBoard;
            } else {
                myBestMove = myBestMove.myParent;
            }
        }
    }

    /**
     * updates the value of root after the user has inputted coordinates
     */
    public void findRoot() {
        if (myBestMove == null) {
            return;
        }
        for (Node n : myBestMove.myChildren) {
            if (Objects.equals(convertBoardToString(n.myBoard), convertBoardToString(myBestMove.myBoard))) {
                root = n;
                return;
            }
        }
    }

    public static String convertBoardToString(char[] theBoard) {
        return Arrays.toString(theBoard);
    }


    /**
     * creates the game tree as a list of lists
     */
    public void generateTree(Node theNode) {
        root = theNode;
        if (myGameTree.size() == 0)
            myGameTree.add(root); //adding the first node

        for (int i = 0; i < theNode.myBoard.length; i++) { //AI first move
            char[] board = Arrays.copyOf(theNode.myBoard, theNode.myBoard.length);
            if (Objects.equals(board[i], '-')) {//empty square
                int row = (i / Board.getNumCols()) + 1;
                int col = (i % Board.getNumCols()) + 1;
                String coordinates = row + "," + col;
                Board.setBoardCoordinate(board, coordinates, Tester.getAiSymbol()); //updating the board with the correct symbols
                Node aiFirstPlay;
                if (getUtilityValue(board, false) == Integer.MAX_VALUE || getUtilityValue(board, false) == Integer.MIN_VALUE) { //if it's a leaf node
                    aiFirstPlay = new Node(Arrays.copyOf(board, board.length), root, getUtilityValue(board, false), firstDepth, new ArrayList<>());
                } else {
                    aiFirstPlay = new Node(Arrays.copyOf(board, board.length), root, -1, firstDepth, new ArrayList<>());
                }
                root.myChildren.add(aiFirstPlay);
                myGameTree.add(aiFirstPlay);

                for (int j = 0; j < board.length; j++) { //Human first move
                    if (Objects.equals(board[j], '-')) { //empty square
                        row = (j / Board.getNumCols()) + 1;
                        col = (j % Board.getNumCols()) + 1;
                        coordinates = row + "," + col;
                        Board.setBoardCoordinate(board, coordinates, Tester.getPlayersSymbol());
                        Node humanFirstPlay;
                        if (getUtilityValue(board, false) == Integer.MAX_VALUE || getUtilityValue(board, false) == Integer.MIN_VALUE) { //if it's a leaf node
                            humanFirstPlay = new Node(Arrays.copyOf(board, board.length), aiFirstPlay, getUtilityValue(board, false), secondDepth, new ArrayList<>());
                        } else {
                            humanFirstPlay = new Node(Arrays.copyOf(board, board.length), aiFirstPlay, -1, secondDepth, new ArrayList<>());
                        }
                        aiFirstPlay.myChildren.add(humanFirstPlay);
                        myGameTree.add(humanFirstPlay);

                        for (int k = 0; k < board.length; k++) { //AI second move
                            if (Objects.equals(board[k], '-')) { //empty square
                                row = (k / Board.getNumCols()) + 1;
                                col = (k % Board.getNumCols()) + 1;
                                coordinates = row + "," + col;
                                Board.setBoardCoordinate(board, coordinates, Tester.getAiSymbol());
                                Node aiSecondPlay;
                                if (getUtilityValue(board, false) == Integer.MAX_VALUE || getUtilityValue(board, false) == Integer.MIN_VALUE) { //if it's a leaf node
                                    aiSecondPlay = new Node(Arrays.copyOf(board, board.length), humanFirstPlay, getUtilityValue(board, false), thirdDepth, new ArrayList<>());
                                } else {
                                    aiSecondPlay = new Node(Arrays.copyOf(board, board.length), humanFirstPlay, -1, thirdDepth, new ArrayList<>());
                                }
                                humanFirstPlay.myChildren.add(aiSecondPlay);
                                myGameTree.add(aiSecondPlay);

                                for (int l = 0; l < board.length; l++) { //Human second move
                                    if (Objects.equals(board[l], '-')) { //empty square
                                        row = (l / Board.getNumCols()) + 1;
                                        col = (l % Board.getNumCols()) + 1;
                                        coordinates = row + "," + col;
                                        Board.setBoardCoordinate(board, coordinates, Tester.getPlayersSymbol());
                                        Node humanSecondPlay = new Node(Arrays.copyOf(board, board.length), aiSecondPlay, getUtilityValue(board, true), fourthDepth, new ArrayList<>());
                                        aiSecondPlay.myChildren.add(humanSecondPlay);
                                        myGameTree.add(humanSecondPlay);
                                        board = Arrays.copyOf(humanSecondPlay.myParent.myBoard, board.length);
                                    }
                                }
                                board = Arrays.copyOf(aiSecondPlay.myParent.myBoard, board.length);
                            }
                        }
                        board = Arrays.copyOf(humanFirstPlay.myParent.myBoard, board.length);
                    }
                }
            }
        }
        firstDepth += 4;
        secondDepth += 4;
        thirdDepth += 4;
        fourthDepth += 4;
    }

    public void generateTreeAB(Node theNode) {
        root = theNode;
        if (myGameTree.size() == 0)
            myGameTree.add(root); //adding the first node

        ArrayList<Node> aiFirst = new ArrayList<>();
        for (int i = 0; i < theNode.myBoard.length; i++) { //AI first move
            char[] board = Arrays.copyOf(theNode.myBoard, theNode.myBoard.length);
            if (Objects.equals(board[i], '-')) {//empty square
                int row = (i / Board.getNumCols()) + 1;
                int col = (i % Board.getNumCols()) + 1;
                String coordinates = row + "," + col;
                Board.setBoardCoordinate(board, coordinates, Tester.getAiSymbol()); //updating the board with the correct symbols
                Node aiFirstPlay = new Node(Arrays.copyOf(board, board.length), root, getUtilityValue(board, false), firstDepth, new ArrayList<>());
                aiFirst.add(aiFirstPlay);
            }
        }
        //adding the best utility value only
        int maxUtil = 0;
        Node aiFirstPlay = null;
        for (Node n : aiFirst) {
            if (n.myUtilityValue > maxUtil) {
                aiFirstPlay = n;
                maxUtil = n.myUtilityValue;
            }
        }
        assert aiFirstPlay != null;
        if(aiFirstPlay.myUtilityValue != Integer.MAX_VALUE){
            aiFirstPlay.myUtilityValue = -1;
        }
        root.myChildren.add(aiFirstPlay);
        myGameTree.add(aiFirstPlay);

        char[] board = Arrays.copyOf(aiFirstPlay.myBoard, aiFirstPlay.myBoard.length);
        for (int j = 0; j < board.length; j++) { //Human first move
            if (Objects.equals(board[j], '-')) { //empty square
                int row = (j / Board.getNumCols()) + 1;
                int col = (j % Board.getNumCols()) + 1;
                String coordinates = row + "," + col;
                Board.setBoardCoordinate(board, coordinates, Tester.getPlayersSymbol());
                Node humanFirstPlay;
                if (getUtilityValue(board, false) == Integer.MAX_VALUE || getUtilityValue(board, false) == Integer.MIN_VALUE) { //if it's a leaf node
                    humanFirstPlay = new Node(Arrays.copyOf(board, board.length), aiFirstPlay, getUtilityValue(board, false), secondDepth, new ArrayList<>());
                } else {
                    humanFirstPlay = new Node(Arrays.copyOf(board, board.length), aiFirstPlay, -1, secondDepth, new ArrayList<>());
                }
                aiFirstPlay.myChildren.add(humanFirstPlay);
                myGameTree.add(humanFirstPlay);

                ArrayList<Node> aiSecond = new ArrayList<>();
                for (int k = 0; k < humanFirstPlay.myBoard.length; k++) { //Human first move
                    board = Arrays.copyOf(humanFirstPlay.myBoard, humanFirstPlay.myBoard.length);
                    if (Objects.equals(board[k], '-')) { //empty square
                        row = (k / Board.getNumCols()) + 1;
                        col = (k % Board.getNumCols()) + 1;
                        coordinates = row + "," + col;
                        Board.setBoardCoordinate(board, coordinates, Tester.getAiSymbol());
                        Node aiSecondPlay = new Node(Arrays.copyOf(board, board.length), humanFirstPlay, getUtilityValue(board, false), thirdDepth, new ArrayList<>());
                        aiSecond.add(aiSecondPlay);
                    }
                }
                //adding the best utility value only
                maxUtil = 0;
                Node aiSecondPlay = null;
                for (Node n : aiSecond) {
                    if (n.myUtilityValue > maxUtil) {
                        aiSecondPlay = n;
                        maxUtil = n.myUtilityValue;
                    }
                }
                humanFirstPlay.myChildren.add(aiSecondPlay);
                myGameTree.add(aiSecondPlay);

                if (!humanFirstPlay.myChildren.isEmpty()) {
                    for (int l = 0; l < board.length; l++) { //Human second move
                        if (Objects.equals(board[l], '-')) { //empty square
                            row = (l / Board.getNumCols()) + 1;
                            col = (l % Board.getNumCols()) + 1;
                            coordinates = row + "," + col;
                            Board.setBoardCoordinate(board, coordinates, Tester.getPlayersSymbol());
                            Node humanSecondPlay = new Node(Arrays.copyOf(board, board.length), aiSecondPlay, getUtilityValue(board, true), fourthDepth, new ArrayList<>());
                            assert aiSecondPlay != null;
                            aiSecondPlay.myChildren.add(humanSecondPlay);
                            myGameTree.add(humanSecondPlay);
                            board = Arrays.copyOf(humanSecondPlay.myParent.myBoard, board.length);
                        }
                    }
                }

            }
            board = Arrays.copyOf(aiFirstPlay.myBoard, board.length);
        }
        firstDepth += 4;
        secondDepth += 4;
        thirdDepth += 4;
        fourthDepth += 4;
    }


    /**
     * @return the utility for the specified Board
     * utility values calculated by counting the number of empty squares
     */
    public int getUtilityValue(char[] theBoard, boolean playersTurn) {
        int count = 0;
        for (char c : theBoard) {
            if (Objects.equals(c, '-')) {
                count++;
            }
        }
        if (count == 0) {
            if (playersTurn)
                count = Integer.MIN_VALUE;  //if the AI can lose the game, will avoid this path
            else
                count = Integer.MAX_VALUE;  //if the AI can win the game, so it always chooses this path
        }
        return count;
    }

    public int getNumNodesExpanded() {
        return numNodesExpanded;
    }

    public int getLargestDepth() {
        return largestDepth;
    }
}
