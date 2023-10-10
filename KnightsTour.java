import java.util.Scanner;

public class KnightsTour {
    /**
     * Instance Variables
     */
    /*
    ChessBoardCoords shows the coordinates on a ChessBoard and ChessBoard is used to keep track
    of where the knight has been
    */
    private static int[][] ChessBoard = new int[8][8];
    private final static String[][] ChessBoardCoords = {
            {"a8", "b8", "c8", "d8", "e8", "f8", "g8", "h8"},
            {"a7", "b7", "c7", "d7", "e7", "f7", "g7", "h7"},
            {"a6", "b6", "c6", "d6", "e6", "f6", "g6", "h6"},
            {"a5", "b5", "c5", "d5", "e5", "f5", "g5", "h5"},
            {"a4", "b4", "c4", "d4", "e4", "f4", "g4", "h4"},
            {"a3", "b3", "c3", "d3", "e3", "f3", "g3", "h3"},
            {"a2", "b2", "c2", "d2", "e2", "f2", "g2", "h2"},
            {"a1", "b1", "c1", "d1", "e1", "f1", "g1", "h1"},
    };
    /*
    Initiated with the amount of moves a knight could make at that space
    and numberMoves is updated whenever a move is made
     */
    private final static int[][] finNumberMoves = {
            {2, 3, 4, 4, 4, 4, 3, 2},
            {3, 4, 6, 6, 6, 6, 4, 3},
            {4, 6, 8, 8, 8, 8, 6, 4},
            {4, 6, 8, 8, 8, 8, 6, 4},
            {4, 6, 8, 8, 8, 8, 6, 4},
            {4, 6, 8, 8, 8, 8, 6, 4},
            {3, 4, 6, 6, 6, 6, 4, 3},
            {2, 3, 4, 4, 4, 4, 3, 2}
    };
    private static int[][] numberMoves = {
            {2, 3, 4, 4, 4, 4, 3, 2},
            {3, 4, 6, 6, 6, 6, 4, 3},
            {4, 6, 8, 8, 8, 8, 6, 4},
            {4, 6, 8, 8, 8, 8, 6, 4},
            {4, 6, 8, 8, 8, 8, 6, 4},
            {4, 6, 8, 8, 8, 8, 6, 4},
            {3, 4, 6, 6, 6, 6, 4, 3},
            {2, 3, 4, 4, 4, 4, 3, 2}
    }; //private static int[][] numberMoves = finNumberMoves; doesn't work
    private static Scanner scan = new Scanner(System.in); //initiates the scanner object
    private static int rowIndex = -1; //keeps track of what row index the knight is on
    private static int columnIndex = -1; //keeps track of what column index the knight is on
    private static int rowSmallestMoves = -1; //keeps track of what row has the smallest number of moves in BestMove()
    private static int columnSmallestMoves = -1; //keeps track of what column has the smallest number of moves in BestMove()
    private static int prosRowIndex = -1; //keeps track of the prospective row index in checkValidity()
    private static int prosColumnIndex = -1; //keeps track of the prospective column index in checkValidity()
    private static String userInput;

    /**
     * Main Method
     */
    public static void main(String[] args) {
        do {
            printChessBoardCoords(); //prints the Chessboard to let the user choose a coordinate
            System.out.println();
            System.out.println("Please input a starting coordinate for the Knight:");
            userInput = scan.nextLine(); //takes in user input
            findLocation(userInput); //calls findLocation at user input and assigns a row and column index to the knight
            run(); //calls the run method
            System.out.println("If done, please type 'done'.");
        } while (!userInput.equalsIgnoreCase("Done"));
        endGame();
    }

    /**
     * Helper Methods
     */
    /*
    Called by the main method and actually runs the game. Used to make the main method simpler
     */
    private static void run() {
        clearChessBoard();
        resetNumberMoves();
        int moveNumber = 1;
        for (int i = 0; i < 64; i++) {
            markSpace(moveNumber);
            if (numberMoves[rowIndex][columnIndex] > 0) {
                updateAvailableSpaces();
                nextBestMove();
                moveNumber++;
            }
        }
        printChessBoard();
        System.out.println();
    }
    /*
    Prints the chess board by iterating through the 2D array and printing the strings in it
     */
    private static void printChessBoard() {
        for (int i = 0; i < ChessBoard.length; i++) {
            for (int j = 0; j < ChessBoard[i].length; j++){
                System.out.print(ChessBoard[i][j] + " ");
            }
            System.out.println();
        }
    }
    private static void printChessBoardCoords() {
        for (int i = 0; i < ChessBoardCoords.length; i++) {
            for (int j = 0; j < ChessBoardCoords[i].length; j++){
                System.out.print(ChessBoardCoords[i][j] + " ");
            }
            System.out.println();
        }
    }
    private static void resetNumberMoves() {
        for (int i = 0; i < numberMoves.length; i++) {
            for (int j = 0; j < numberMoves[i].length; j++){
                numberMoves[i][j] = finNumberMoves[i][j];
            }
        }
    }
    /*
    Called once by the main() method in order to initialize the knight at the row and column index
    If the coordinate is not on the chess board, it returns "invalid location" and ends the run
     */
    private static void findLocation(String location) {
        rowIndex = -1;
        columnIndex = -1;
        for (int i = 0; i < ChessBoardCoords.length; i++){
            for (int j = 0; j < ChessBoardCoords[i].length; j++) {
                if (location.equalsIgnoreCase(ChessBoardCoords[i][j])) {
                    rowIndex = i;
                    columnIndex = j;
                }
            }
        }
        if ((rowIndex == -1 || columnIndex == -1)){
            System.out.println("Invalid Location. Please input another coordinate: ");
            userInput = scan.nextLine();
            findLocation(userInput);
        }
    }
    /*
    Called by the run() method and used to assign "0" to every index in the chess board
    in order to indicate which spaces have not been landed on
     */
    private static void clearChessBoard() {
        for (int i = 0; i < ChessBoard.length; i++){
            for (int j = 0; j < ChessBoard[i].length; j++) {
                ChessBoard[i][j] = 0;
            }
        }
    }
    /*
    Marks the space where the knight is currently on with the current move number, which is the parameter, on
    the ChessBoard[][] array
     */
    private static void markSpace(Integer count) {
        int newNumber = count; //converts the move number into a string
        ChessBoard[rowIndex][columnIndex] = newNumber;
    }
    /*
    Checks which spaces are available to jump to and updates the current space to 0 and subtracts 1 from the surrounding spaces
    on numberMoves[][] if they are not already 0 and could actually have reached the space by calling updateSurroundingSpaces()
     */
    private static void updateAvailableSpaces() {
        numberMoves[rowIndex][columnIndex] = 0;
        updateSurroundingSpaces();
    }
    /*
    If the space was reachable by the knight at its current spot, it will decrease every
    space that could have reached it by one, since this space is now unavailable
    */
    private static void updateSurroundingSpaces() {
        for (int i = 1; i < 8; i++){
            if (checkValidity(i) && numberMoves[prosRowIndex][prosColumnIndex] != 0){
                numberMoves[prosRowIndex][prosColumnIndex]--;
            }
        }
    }
    /*
    Determines the best possible next move by first assigning a value to rowSmallestMoves and columnSmallestMoves
    depending on which spaces are available and then checking through every possible move to see which has the
    least number of available moves then updates the row and column indexes
     */
    private static void nextBestMove() {
        for (int i = 1; i <= 8; i++) {
            if (checkValidity(i) && numberMoves[prosRowIndex][prosColumnIndex] != 0) {
                rowSmallestMoves = prosRowIndex;
                columnSmallestMoves = prosColumnIndex;
                }
            }
        //checks which available space has the least number of available moves
        //goes through every
        for (int i = 1; i <= 8; i++) {
            if (checkValidity(i) && numberMoves[prosRowIndex][prosColumnIndex] != 0){
                if (numberMoves[prosRowIndex][prosColumnIndex] < numberMoves[rowSmallestMoves][columnSmallestMoves]) {
                    rowSmallestMoves = prosRowIndex;
                    columnSmallestMoves = prosColumnIndex;
                }
            }
        }
        rowIndex = rowSmallestMoves;
        columnIndex = columnSmallestMoves;
    }
    /*
    Checks whether or not the move is in bound based on parameter input which goes from 1 to 8
     */
    private static boolean checkValidity(int i){
        boolean checkLeftUp = false;
        boolean checkLeftDown = false;
        boolean checkRightUp = false;
        boolean checkRightDown = false;
        boolean checkUpRight = false;
        boolean checkUpLeft = false;
        boolean checkDownRight = false;
        boolean checkDownLeft = false;

        if (i==1 && columnIndex-2 >=0 && rowIndex+1 <= 7){
            checkLeftDown = true;
            prosRowIndex = rowIndex+1;
            prosColumnIndex = columnIndex-2;
            return checkLeftDown;
        }
        if (i==2 && columnIndex-2 >=0 && rowIndex-1 >= 0){
            checkLeftUp = true;
            prosRowIndex = rowIndex-1;
            prosColumnIndex = columnIndex-2;
            return checkLeftUp;
        }
        if (i==3 && columnIndex+2 <=7 && rowIndex+1 <= 7){
            checkRightDown = true;
            prosRowIndex = rowIndex+1;
            prosColumnIndex = columnIndex+2;
            return checkRightDown;
        }
        if (i==4 && columnIndex+2 <=7 && rowIndex-1 >= 0){
            checkRightUp = true;
            prosRowIndex = rowIndex-1;
            prosColumnIndex = columnIndex+2;
            return checkRightUp;
        }
        if (i == 5 && rowIndex-2 >= 0 && columnIndex+1 <=7){
            checkUpRight = true;
            prosRowIndex = rowIndex-2;
            prosColumnIndex = columnIndex+1;
            return checkUpRight;
        }
        if (i == 6 && rowIndex-2 >=0 && columnIndex-1 >= 0){
            checkUpLeft = true;
            prosRowIndex = rowIndex-2;
            prosColumnIndex = columnIndex-1;
            return checkUpLeft;
        }
        if (i == 7 && rowIndex+2 <=7 && columnIndex+1 <= 7){
            checkDownRight = true;
            prosRowIndex = rowIndex+2;
            prosColumnIndex = columnIndex+1;
            return checkDownRight;
        }
        if (i == 8 && rowIndex+2 <=7 && columnIndex-1 >= 0){
            checkDownLeft = true;
            prosRowIndex = rowIndex+2;
            prosColumnIndex = columnIndex-1;
            return checkDownLeft;
        }
        return false;
    }
    /*
    Prints the chess board and prints game over. Ends the program.
     */
    private static void endGame() {
        System.out.println("Game over.");
        scan.close();
        System.exit(1);
    }
}