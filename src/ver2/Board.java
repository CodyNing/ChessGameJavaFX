package ver2;
import java.io.Serializable;

import javafx.event.ActionEvent;
import javafx.scene.layout.GridPane;

/**
 * 
 * Board.class
 * - Uses javafx GridPane to simulate chess board.
 * - Uses 8x8 Square objects to simulate 8x8 squares on chess board.
 * - Gets report from Square objects' event handler and perform basic
 *   chess board function. 
 *   
 *   Note: Board doesn't know any chess rule, but it
 *   will interact very frequently with Rule.class to perform the
 *   proper chess game behaviors.
 *
 * @author Zhuo (Cody) Ning
 * @version 2018
 */
public class Board extends GridPane implements Serializable {
    
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * 8x8 Square objects to simulate 8x8 squares on chess board.
     */
    private Square[][] squares = new Square[8][8];
    
    /**
     * Currently user selected chess piece.
     */
    private Chess selectedChess;
    
    /**
     * Currently user selected Square.
     */
    private Square selectedSquare;
    
    /**
     * Indicates whether the current turn is red team's turn when game load and safe.
     */
    private boolean isRedTurn;
    
    /**
     * Indicates whether red team can perform special move castling when game load and safe.
     */
    private boolean redCastling;
    
    /**
     * Indicates whether blue team can perform special move castling when game load and safe.
     */
    private boolean blueCastling;
    
    /**
     * 
     * Constructs an object of type Board.
     * Pass all the squares(as a board) to Rule.class to setup the game rule.
     * Then puts all squares on GridPane to form the chess board.
     * Finally setup all chess pieces on the chess board.
     */
    public Board() {
        
        Rule.setBoardRule(squares, false);
        
        for(int i = 0; i < squares.length; i++) {
            
            for(int j = 0; j < squares[i].length; j++) {
                
                squares[i][j] = new Square(i, j);
                squares[i][j].setStyle(((i + j) % 2 == 0) ?
                        "-fx-background-color: #2F4F4F; " : "-fx-background-color: BLACK;");
                squares[i][j].setOnAction(this::selectSquare);
                add(squares[i][j], i, j);
            }
        }
        setBoard();
    }
    
    /**
     * Restore board feature when load from previous game.
     */
    public void restore() {

        Rule.setBoardRule(squares, isRedTurn);
        Rule.setRedCastling(redCastling);
        Rule.setBlueCastling(blueCastling);
        
        for(int i = 0; i < squares.length; i++) {
            
            for(int j = 0; j < squares[i].length; j++) {
                
                squares[i][j].setUp();
                squares[i][j].setStyle(((i + j) % 2 == 0) ?
                        "-fx-background-color: #2F4F4F; " : "-fx-background-color: BLACK;");
                squares[i][j].setOnAction(this::selectSquare);
                add(squares[i][j], i, j);
            }
        }
        
    }
    
    /**
     * Gets report from square objects' event handler and take care of the event(mouse click).
     * - If player have not selected any chess
     *      try to select the clicking square.
     * - If Player is clicking on the currently selected square
     *      unselect it.
     * - If player is clicking on chess piece on same team
     *      ask Rule if player is performing special move (castling).
     *      -- If yes, unselect all square and tell Rule to do aftermath.
     *      -- If no, unselect currently selected square and try to select 
     *         the clicking one.
     * - Otherwise, try to move the selected chess to clicking square.
     * 
     * @param event
     *              mouse clicking event of square objects.
     */
    private void selectSquare(ActionEvent event) {
        Square selecting = (Square) event.getSource();
        if (selectedChess == null)
            select(selecting);
        else if (selectedSquare.equals(selecting)) {
            deHightLightALl();
            selectedSquare = null;            
            selectedChess = null;
        } else if (selecting.getChess() != null &&
                selectedSquare.getChess().isRed() == selecting.getChess().isRed()) {
            if(Rule.castling(selectedSquare, selecting)) {
                deHightLightALl();
                selectedSquare = null;            
                selectedChess = null;
                Rule.afterMove(selecting);
            } else {
                deHightLightALl();
                selectedSquare = null;            
                selectedChess = null; 
                select(selecting);
            }
        } else
            move(selecting);     
    }
    
    /**
     * Ask Rule can I select this square.
     * If yes, tell Rule to prepare for movement
     * and then select this square.
     * @param selecting
     *                  player clicking square.
     */
    private void select(Square selecting) {
        if (Rule.canIselect(selecting)) {
            Rule.beforeMove(selecting); //<- polymorphic call starting here, go to(Rule line 153)
            selectedChess = selecting.getChess();
            selectedSquare = selecting;
        }
    }

    /**
     * Ask Rule can I move to this square.
     * If yes, tell Rule to take care of potential special movement (En Passant).
     * Then perform the movement and unselect all squares.
     * Finally tell Rule to do aftermath of the movement
     * @param selecting
     *                  player clicking square.
     */
    private void move(Square selecting) {
        if (Rule.isEnable(selecting)) {
            Rule.enPassant(selectedSquare, selecting);
            deHightLightALl();
            selectedSquare.setChess(null);            
            selecting.setChess(selectedChess);
            selectedChess = null;
            Rule.afterMove(selecting);
        }
    }

    /**
     * Unselect all squares.
     */
    private void deHightLightALl() {
        for(Square[] a : squares)
            for(Square b : a) {
                b.deHightLight();
                b.setEnable(false);
            }
        
    }

    /**
     * Save board states when saving.
     */
    public void save() {
        isRedTurn = Rule.isRedTurn();
        redCastling = Rule.isRedCastling();
        blueCastling = Rule.isBlueCastling();
    }

    /**
     * Set up chess pieces on the board.(Can be used for testing)
     */
    private void setBoard() {
        /* normal board setting*/
         
        squares[0][0].setChess(new Rook(true));
        squares[1][0].setChess(new Knight(true));
        squares[2][0].setChess(new Bishop(true));
        squares[3][0].setChess(new King(true));
        squares[4][0].setChess(new Queen(true));
        squares[5][0].setChess(new Bishop(true));
        squares[6][0].setChess(new Knight(true));
        squares[7][0].setChess(new Rook(true));
        
        for(int i = 0; i < 8; i++) {
            squares[i][1].setChess(new Pawn(true));
        }
        
        squares[0][7].setChess(new Rook(false));
        squares[1][7].setChess(new Knight(false));
        squares[2][7].setChess(new Bishop(false));
        squares[3][7].setChess(new Queen(false));
        squares[4][7].setChess(new King(false));
        squares[5][7].setChess(new Bishop(false));
        squares[6][7].setChess(new Knight(false));
        squares[7][7].setChess(new Rook(false));
        
        for(int i = 0; i < 8; i++) {
            squares[i][6].setChess(new Pawn(false));
        }
        
        
        /* checkmate test case.
        
        squares[4][7].setChess(new King(false));
        squares[0][0].setChess(new Queen(false));
        squares[7][0].setChess(new King(true));
        squares[5][1].setChess(new Pawn(true));
        squares[6][1].setChess(new Pawn(true));
        squares[7][1].setChess(new Pawn(true));
        */
        
        /* Castling test case.
        
        squares[3][0].setChess(new King(true));
        squares[1][5].setChess(new Queen(true));
        squares[4][7].setChess(new King(false));
        squares[0][7].setChess(new Rook(false));
        squares[7][7].setChess(new Rook(false));
        squares[7][1].setChess(new Pawn(false));
        */
        
        /* Pawn Promotion test case
        squares[3][0].setChess(new King(true));
        squares[4][7].setChess(new King(false));
        squares[5][1].setChess(new Pawn(false));
        squares[6][1].setChess(new Pawn(false));
        squares[7][1].setChess(new Pawn(false));
        */
        
        /* En Passant test case
        squares[3][0].setChess(new King(true));
        squares[4][7].setChess(new King(false));
        squares[3][4].setChess(new Pawn(true));
        ((Pawn)squares[3][4].getChess()).setFirstRound(false);
        squares[2][6].setChess(new Pawn(false));
        squares[4][6].setChess(new Pawn(false));
        */
        
    }

}
