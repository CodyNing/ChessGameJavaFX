package ver2;
import java.util.ArrayList;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 * 
 * Rule.class
 * 
 * Fully implemented chess game rule, core of the chess game.
 * 
 * Interact very frequently with chess board to tell it how
 * to perform. When special movement is triggered, Rule can
 * directly control the chess board's performance.
 * 
 * Implemented feature:
 * - Basic movement pattern
 * - Low level rules restricted movement
 * - Pawn's Promotion
 * - Check and Checkmate condition
 * - Pawn's En Passant special movement
 * - King and Rook's Castling special movement
 * 
 * TODO
 * - Separate functions
 * - Simplify logic
 * - Testing and Debugging
 * - Optimization
 *
 * @author Zhuo (Cody) Ning
 * @version 2018
 */
public class Rule {
    
    /**
     * Normal move pattern common to all chess pieces.
     */
    public static final int[][] NORMAL_MOVE = {
            {1, 0}, {1, 1}, {0, 1}, {-1, 1}, {-1, 0}, {-1, -1}, {0, -1}, {1, -1}
    };
    
    /**
     * Move pattern for Knight only.
     */
    public static final int[][] KNIGHT_MOVE = {
            {2, 1}, {1, 2}, {-1, 2}, {-2, 1}, {-2, -1}, {-1, -2}, {1, -2}, {2, -1}
    };

    /**
     * Indicates whether the current turn is red team's turn.
     */
    private static boolean isRedTurn;
    
    /**
     * Indicates whether red team can perform special move castling.
     */
    private static boolean redCastling;
    
    /**
     * Indicates whether blue team can perform special move castling.
     */
    private static boolean blueCastling;
    
    /**
     * The game board this Rule rely on.
     */
    private static Square[][] board;
    
    /* Core Function */

    /**
     * Searches for all enemy team chess pieces that is checking 
     * (allowed to move to) this square.
     * Note: Only can be used on Squares that contains chess piece.
     * @param square
     *              the square to be checked.
     * @return all square of enemy chess pieces that is checking
     *         this square.
     */
    private static ArrayList<Square> getChecker(Square square) {
        ArrayList<Square> checker = new ArrayList<Square>();
        for (Square[] col : board)
            for (Square row : col)
                if (row.getChess() != null && square.getChess() != null
                && row.getChess().isRed() != square.getChess().isRed()) {
                    ArrayList<Square> movelist = getBasicMove(row);
                    for (Square toMove : movelist)
                        if (toMove.equals(square))
                            checker.add(row);
                }
        return checker;
    }
    
    /**
     * Searches for all enemy team chess pieces that is checking 
     * (allowed to move to) this square.
     * Note: Can be use used on empty square but must provide team color.
     * @param square
     *              the square to be checked.
     * @param isRed
     *              which team this square would belong to
     * @return all square of enemy chess pieces that is checking
     *         this square.
     */
    private static ArrayList<Square> getChecker(Square square, boolean isRed) {
        ArrayList<Square> checker = new ArrayList<Square>();
        for (Square[] col : board)
            for (Square row : col)
                if (row.getChess() != null
                && row.getChess().isRed() != isRed) {
                    ArrayList<Square> movelist = getBasicMove(row);
                    for (Square toMove : movelist)
                        if (toMove.equals(square))
                            checker.add(row);
                }
        return checker;
    }
    
    /* Public Functions */
    
    /**
     * Sets the chess board this Rule rely on.
     * @param board
     *              the chess board this Rule rely on.
     */
    public static void setBoardRule(Square[][] board, boolean isRedTurn) {
        Rule.board = board;
        Rule.isRedTurn = isRedTurn;
    }

    /**
     * Restricts selecting square by game turn.
     * @param square
     *              the square to be selected.
     * @return whether the current turn is this square's team's turn
     */
    public static boolean canIselect(Square square) {
        return square.getChess() != null && square.getChess().isRed() == isRedTurn;
    }
    
    /**
     * Prepares the move for selecting square by getting movements
     * from movement functions and enables all the movable square.
     * @param selecting
     *                  the square contains chess to be moved.
     */
    public static void beforeMove(Square selecting) {
        Rule.setEnable(selecting);
        Rule.enableAll(getCheckFilteredMove(selecting));   //<- polymorphic call point 2, go to(Rule line 327)
    }

    /**
     * Does the aftermath after movement is performed by setting
     * moved chess's first round off and change game turn.
     * Then calls pawnPromotion function to take care of special case.
     * Finally checks whether one of the team has been checkmated
     * if yes, calls set winner function.
     * @param selecting
     *                  the square now contains the chess just moved.
     */
    public static void afterMove(Square selecting) {
        if(selecting.getChess() != null)
            setFirstRoundOff(selecting);
        for (Square[] col : board)
            for (Square row : col)
                if(row.getChess() != null 
                        && row.getChess() instanceof Pawn
                        && row != selecting)
                            ((Pawn)row.getChess()).setCoundBeEnPassant(false);
        Rule.setTurn();
        pawnPromotion(selecting);
        if(isCheckmate())
            win(!isRedTurn);
    }

    /**
     * Checks whether the passed square is enabled.
     * @param square
     *              square to be checked.
     * @return whether the passed square is enabled.
     */
    public static boolean isEnable(Square square) {
        return square.isEnable();
    }

    /**
     * If the current king and rook can perform castling,
     * sets them to proper position, and does the aftermath.
     * @param king
     *              the square contains the king piece to perform castling
     * @param rook
     *              the square contains the rook piece to perform castling
     * @return whether this castling has been successfully done.
     */
    public static boolean castling(Square king, Square rook) {
        if((king.getChess().isRed() && redCastling)
                || (!king.getChess().isRed() && blueCastling)) {
            setFirstRoundOff(king, rook);
            int distance = rook.getCol() - king.getCol();
            if(distance < 0) {
                board[king.getCol() - 2][king.getRow()].setChess(king.getChess());
                king.setChess(null);
                board[rook.getCol() + 
                      (Math.abs(distance) == 3 ? 2 : 3)]
                              [rook.getRow()].setChess(rook.getChess());
                rook.setChess(null); 
            } else {
                board[king.getCol() + 2][king.getRow()].setChess(king.getChess());
                king.setChess(null);
                board[rook.getCol() - 
                      (Math.abs(distance) == 3 ? 2 : 3)]
                              [rook.getRow()].setChess(rook.getChess());
                rook.setChess(null); 
            }
            redCastling = false;
            blueCastling = false;
            return true;
        }
        return false;
    }

    /**
     * Checks whether pawn has performed a En Passant move.
     * If yes, does the aftermath by taking off the captured pawn.
     * @param selectedSquare
     *                  the square contains the chess to be moved
     * @param selecting
     *                  the square the chess to be move to.
     */
    public static void enPassant(Square selectedSquare, Square selecting) {
        if(selectedSquare.getChess() != null 
                && selectedSquare.getChess() instanceof Pawn
                &&((Pawn)selectedSquare.getChess()).isEnPassant()
                && selecting.getChess() == null
                && selecting.getCol() != selectedSquare.getCol()) {
            board[selecting.getCol()][selectedSquare.getRow()].setChess(null);
            for (Square[] col : board)
                for (Square row : col)
                    if(row.getChess() != null 
                            && row.getChess() instanceof Pawn)
                        ((Pawn)row.getChess()).setEnPassant(false);
        }
        
    }

    /* Movement Functions */
    
    /**
     * Checks the chess piece in a square and select the proper
     * move pattern for them.
     * Then passes this square and its movements and steps to 
     * ruleFilterMove function to get the basic rule restricted
     * movements and returns it.
     * @param square
     *              the square contain the chess piece to be checked.
     * @return movable squares of the chess piece in this square.
     */
    static public ArrayList<Square> getBasicMove(Square square){
        /* polymorphic call for piece getMovement and getStep,
         * all pieces have unique move pattern and step
         * Rule class just filter the basic movement patterns
         * to perform as a chess game, will not influence anything.
         * This polymorphic call gives us flexibility to add 
         * new chess piece with different move pattern.
         */
        return getRuleFilteredMove(square, square.getChess().getMovement(), square.getChess().getStep());
    }
    
    /**
     * Takes a square and its chess piece's basic movement pattern and steps
     * and use basic chess rule to search for its movable places.
     * @param square
     *              the square contains the chess to be moved.
     * @param movement
     *              the basic movement pattern of the chess in this square
     * @param step
     *              the step of the chess in this square allowed to move.
     * @return movable squares of the chess piece in this square.
     */
    static private ArrayList<Square> getRuleFilteredMove(Square square, int[][] movement, int step) {
        int col;
        int row;
        Square temp;
        ArrayList<Square> ableMoves = new ArrayList<Square>(); 
        for(int i = 0; i < movement.length; i++) {
            for(int j = 1; j <= step; j++) {
                temp = null;
                col = movement[i][0] * j + square.getCol();
                row = movement[i][1] * j + square.getRow();
                if(col < 0 || col > 7 || row < 0 || row > 7)
                    break;
                temp = board[col][row];
                if ((temp.getChess() != null && temp.getChess().isRed() == square.getChess().isRed())
                        || (square.getChess().isRed() == isRedTurn 
                        && square.getChess() instanceof King 
                        && getChecker(temp, isRedTurn).size() > 0
                        || (i < 2 && temp.getChess() == null 
                        && square.getChess() instanceof Pawn))
                        || (i == 2 && temp.getChess() != null 
                        && square.getChess() instanceof Pawn)
                        || (i < 2 && j > 1 
                        && square.getChess() instanceof Pawn))
                    break;
                else {                    
                    ableMoves.add(temp);
                    if(temp.getChess() != null)
                        break;
                }
            }
        }
        return ableMoves;
    }
    
    /**
     * Takes a square and passes it to getBasicMove function
     * to gets its chess piece's movable squares.
     * Then filters these movable squares by chess game checking
     * conditions.
     * @param selecting
     *              the square contains the chess to be moved.
     * @return movable squares filtered by check conditions
     */
    static public ArrayList<Square> getCheckFilteredMove(Square selecting) {
        ArrayList<Square> movelist = Rule.getBasicMove(selecting); //<- polymorphic call point 3, go to(Rule line 263)
        ArrayList<Square> checkers = getChecker(getKing(isRedTurn));
        ArrayList<Square> filteredMove = new ArrayList<Square>();
        if(selecting.getChess() != null 
                && selecting.getChess() instanceof Pawn) {
            Square enPassantMove = getEnPassantMove(selecting);
            if(enPassantMove != null)
                movelist.add(enPassantMove);
        }
        if (checkers.size() > 0) {
            filteredMove.addAll(getCaptureMove(selecting, movelist, checkers));
            filteredMove.addAll(getBlockMove(selecting, movelist));
        } else {
            filteredMove.addAll(avoidSelfCheck(selecting, movelist));
            filteredMove.addAll(getCastlingMove(selecting));
        }
        return filteredMove;
    }

    /**
     * When king is being checking by enemy team, find out which legal move
     * of this piece could capture the enemy piece that is checking its king.
     * @param selecting
     *              the square contains the chess to be moved.
     * @param movelist
     *              movable squares of the chess piece in this square.
     * @param checkers
     *              the enemy piece that is checking our king
     * @return legal capture movement this piece could perform to save its king.
     */
    static public ArrayList<Square> getCaptureMove(
            Square selecting, ArrayList<Square> movelist, ArrayList<Square> checkers){
        ArrayList<Square> filteredMove = new ArrayList<Square>();
        for(Square checker : checkers)
            for(Square move : movelist)
                if (move == checker)
                    filteredMove.add(move);
        return filteredMove;
    }
    
    /**
     * When king is being checking by enemy team, find out which legal move
     * of this piece could block the enemy piece that is checking its king.
     * @param selecting
     *              the square contains the chess to be moved.
     * @param movelist
     *              movable squares of the chess piece in this square.
     * @return legal block movement this piece could perform to save its king.
     */
    static public ArrayList<Square> getBlockMove(
            Square selecting, ArrayList<Square> movelist){
        ArrayList<Square> filteredMove = new ArrayList<Square>();
        ArrayList<Square> checkers = null;
        Chess holding = selecting.getChess();
        Chess holding1 = null;
        selecting.setChess(null);
        for(int i = movelist.size() - 1; i >= 0; i--) {
            holding1 = null;
            if (movelist.get(i).getChess() != null)
                holding1 = movelist.get(i).getChess();
            movelist.get(i).setChess(holding);
            checkers = getChecker(getKing(isRedTurn));
            movelist.get(i).setChess(holding1);
            if (checkers.size() == 0)
                filteredMove.add(movelist.get(i)); 
        }
        selecting.setChess(holding);
        holding = null;
        return filteredMove;
    }
    
    /**
     * Filter out the potential movement of this piece could
     * lead to self check of its king.
     * @param selecting
     *              the square contains the chess to be moved.
     * @param movelist
     *              movable squares of the chess piece in this square.
     * @return legal movement which will not cause self check
     */
    static public ArrayList<Square> avoidSelfCheck(
            Square selecting, ArrayList<Square> movelist) {
        ArrayList<Square> checkers = null;
        Chess holding = selecting.getChess();
        Chess holding1 = null;
        selecting.setChess(null);
        for(int i = movelist.size() - 1; i >= 0; i--) {
            holding1 = null;
            if (movelist.get(i).getChess() != null)
                holding1 = movelist.get(i).getChess();
            movelist.get(i).setChess(holding);
            checkers = getChecker(getKing(isRedTurn));
            movelist.get(i).setChess(holding1);
            if (checkers.size() > 0)
                movelist.remove(i); 
                
        }
        selecting.setChess(holding);
        holding = null;
        return movelist;
    }
    
    /* Special Movement Functions*/

    /**
     * If the current selected square contains a king
     * and it can perform castling move, return the square of
     * rooks which can castling with it.
     * @param square
     *              the square contains the chess to be moved.
     * @return the square of rooks which can castling with it.
     */
    static public ArrayList<Square> getCastlingMove(Square square) {
        ArrayList<Square> rooks = new ArrayList<Square>();
        if(square.getChess() instanceof King //filter out other chess, previously moved or is being checked king
                && ((King)square.getChess()).isFirstRound()
                && getChecker(square).size() == 0) {
            rooks = getRook(isRedTurn); //get same color rooks
            for(int j = rooks.size() - 1; j >= 0; j--) {
                Square rook = rooks.get(j);
                if(((Rook)rook.getChess()).isFirstRound() //filter out previously moved or is being checked rook
                        && getChecker(rook).size() == 0) {
                    if(rook.getCol() < square.getCol()) { //left rook
                        for(int i = square.getCol() - 1; i > rook.getCol(); i--) {
                            Square temp = board[i][square.getRow()];
                            if (temp.getChess() != null || getChecker(temp, isRedTurn).size() > 0) { //check the path
                                rooks.remove(j);
                                break;
                            }
                        }
                    } else {
                        for(int i = square.getCol() + 1; i < rook.getCol(); i++) {
                            Square temp = board[i][square.getRow()];
                            if (temp.getChess() != null || getChecker(temp, isRedTurn).size() > 0) {
                                rooks.remove(j);
                                break;
                            }
                        }
                    }           
                } else {
                    rooks.remove(j);
                    continue;
                }
            }
        }
        if(rooks.size() > 0)
            if(isRedTurn)
                redCastling = true;
            else
                blueCastling = true;
        return rooks;
    }
    
    /**
     * If current selected square contain a pawn and it can perform
     * En Passant move, set its EnPassant to true.
     * @param square
     *              the square contains the chess to be moved.
     */
    static public void setEnPassant(Square square) {
        if(square.getChess() != null && square.getChess() instanceof Pawn
                && square.getChess().isRed() != isRedTurn
                && (square.getRow() == 4 || square.getRow() == 4))
            ((Pawn)square.getChess()).setEnPassant(true);
    }
    
    /**
     * If current selected square contains a pawn and its EnPassant indicator
     * is true, return the square it could En Passant move to.
     * @param square
     *              the square contains the chess to be moved.
     * @return the square this pawn could En Passant move to
     */
    static public Square getEnPassantMove(Square square) {
        Square toMove = null;
        if(((Pawn)square.getChess()).isEnPassant()) {
            Chess pawn = square.getChess();
            Square temp;
            if(pawn.isRed()) {
                temp = board[square.getCol() - 1][square.getRow()];
                if(temp.getChess() != null && temp.getChess() instanceof Pawn
                        && temp.getChess().isRed() != isRedTurn
                        && ((Pawn) temp.getChess()).isCoundBeEnPassant())
                    toMove = board[temp.getCol()][temp.getRow() + 1];
                temp = board[square.getCol() + 1][square.getRow()];
                if(temp.getChess() != null && temp.getChess() instanceof Pawn
                        && temp.getChess().isRed() != isRedTurn
                        && ((Pawn) temp.getChess()).isCoundBeEnPassant())
                    toMove = board[temp.getCol()][temp.getRow() + 1];
            } else {
                temp = board[square.getCol() - 1][square.getRow()];
                if(temp.getChess() != null && temp.getChess() instanceof Pawn
                        && temp.getChess().isRed() != isRedTurn
                        && ((Pawn) temp.getChess()).isCoundBeEnPassant())
                    toMove = board[temp.getCol()][temp.getRow() - 1];
                temp = board[square.getCol() + 1][square.getRow()];
                if(temp.getChess() != null && temp.getChess() instanceof Pawn
                        && temp.getChess().isRed() != isRedTurn
                        && ((Pawn) temp.getChess()).isCoundBeEnPassant())
                    toMove = board[temp.getCol()][temp.getRow() - 1];
            }
        }
        return toMove;
    }
    
    /**
     * If current selected square contains a pawn and it is at
     * the end of its path, pops up a window to let player choose
     * a piece it could promote to.
     * @param square
     *              the square contains the chess to be moved.
     */
    static public void pawnPromotion(Square square) {
        if (square.getChess() != null
                && square.getChess() instanceof Pawn) {
            Chess pawn = square.getChess();
            if(pawn.isRed() && square.getRow() == 7 
                    || !pawn.isRed() && square.getRow() == 0) {
                Text tips = new Text("You can promote your pawn, please choose from the following: ");
                tips.setFont(Font.font(null, FontWeight.NORMAL, 13));
                GridPane pawnPromotion = new GridPane();
                Button rook = new Button("♖");
                rook.setPrefSize(100, 100);
                rook.setFont(Font.font(null, FontWeight.BOLD, 35));
                rook.setOnAction((event)->{
                    square.setChess(new Rook(!isRedTurn));
                    ((Rook) square.getChess()).setFirstRound(false);
                    Launch.closePopWindow();
                });
                Button knight = new Button("♘");
                knight.setPrefSize(100, 100);
                knight.setFont(Font.font(null, FontWeight.BOLD, 35));
                knight.setOnAction((event)->{
                    square.setChess(new Knight(!isRedTurn));
                    Launch.closePopWindow();
                });
                Button bishop = new Button("♗");
                bishop.setPrefSize(100, 100);
                bishop.setFont(Font.font(null, FontWeight.BOLD, 35));
                bishop.setOnAction((event)->{
                    square.setChess(new Bishop(!isRedTurn));
                    Launch.closePopWindow();
                });
                Button queen = new Button("♕");
                queen.setPrefSize(100, 100);
                queen.setFont(Font.font(null, FontWeight.BOLD, 35));
                queen.setOnAction((event)->{
                    square.setChess(new Queen(!isRedTurn));
                    Launch.closePopWindow();
                });
                pawnPromotion.add(tips, 0, 0, 4, 1);
                pawnPromotion.add(rook, 0, 1);
                pawnPromotion.add(knight, 1, 1);
                pawnPromotion.add(bishop, 2, 1);
                pawnPromotion.add(queen, 3, 1);
                Launch.popWindow(pawnPromotion, "Pawn Promotion", 400, 120);
            }
        }
    }
    
    /* Referee Functions*/
    
    /**
     * If the king of the team on this turn is being checking
     * and it could not move, and none of its team pieces could
     * kill or block the checker, then sets the opposite team wins.
     * @return whether the team on this turn is being checkmated.
     */
    static public boolean isCheckmate() {
        ArrayList<Square> checkers = getChecker(getKing(isRedTurn));
        ArrayList<Square> basicMove = getCheckFilteredMove(getKing(isRedTurn));
        ArrayList<Square> protectors = getProtector(getKing(isRedTurn));
        ArrayList<Square> blockers = getBlocker(getKing(isRedTurn));
        return checkers.size() > 0 && basicMove.size() == 0 && protectors.size() == 0 && blockers.size() == 0;
    }
    
    /**
     * Sets the given team as winner.
     * @param isRed
     *              the color of team to set
     */
    private static void win(boolean isRed) {
        for (Square[] col : board)
            for (Square row : col)
                row.setDisable(true);
        if(isRed) {
            board[2][3].setText("R");
            board[2][3].setFont(Font.font(null, FontWeight.BOLD, 40));
            board[2][3].setTextFill(Color.RED);
            board[3][3].setText("E");
            board[3][3].setFont(Font.font(null, FontWeight.BOLD, 40));
            board[3][3].setTextFill(Color.RED);
            board[4][3].setText("D");
            board[4][3].setFont(Font.font(null, FontWeight.BOLD, 40));
            board[4][3].setTextFill(Color.RED);
        } else {
            board[2][3].setText("B");
            board[2][3].setFont(Font.font(null, FontWeight.BOLD, 40));
            board[2][3].setTextFill(Color.BLUE);
            board[3][3].setText("L");
            board[3][3].setFont(Font.font(null, FontWeight.BOLD, 40));
            board[3][3].setTextFill(Color.BLUE);
            board[4][3].setText("U");
            board[4][3].setFont(Font.font(null, FontWeight.BOLD, 40));
            board[4][3].setTextFill(Color.BLUE);
            board[5][3].setText("E");
            board[5][3].setFont(Font.font(null, FontWeight.BOLD, 40));
            board[5][3].setTextFill(Color.BLUE);
        }
        board[3][4].setText("W");
        board[3][4].setFont(Font.font(null, FontWeight.BOLD, 40));
        board[3][4].setTextFill(Color.YELLOW);
        board[4][4].setText("I");
        board[4][4].setFont(Font.font(null, FontWeight.BOLD, 40));
        board[4][4].setTextFill(Color.YELLOW);
        board[5][4].setText("N");
        board[5][4].setFont(Font.font(null, FontWeight.BOLD, 40));
        board[5][4].setTextFill(Color.YELLOW);
    }

    /* Helper Functions */
    
    /**
     * Sets the moved pieces' first round off.
     * @param chesses
     *              the squares contain chess pieces to be set
     */
    static public void setFirstRoundOff(Square...chesses) {
        for(Square chess : chesses)
            if (chess.getChess() instanceof Pawn
                    &&((Pawn)chess.getChess()).isFirstRound()) {
                ((Pawn)chess.getChess()).setFirstRound(false);
                Square temp;
                if(chess.getCol() + 1 <= 7) {
                    temp = board[chess.getCol() + 1][chess.getRow()];
                    ((Pawn)chess.getChess()).setCoundBeEnPassant(true);
                    setEnPassant(temp);
                }
                if(chess.getCol() - 1 >= 0) {
                    temp = board[chess.getCol() - 1][chess.getRow()];
                    ((Pawn)chess.getChess()).setCoundBeEnPassant(true);
                    setEnPassant(temp);
                }
            } else if(chess.getChess() instanceof Rook
                    && ((Rook)chess.getChess()).isFirstRound())
                ((Rook)chess.getChess()).setFirstRound(false);
            else if(chess.getChess() instanceof King
                    && ((King)chess.getChess()).isFirstRound())
                ((King)chess.getChess()).setFirstRound(false); 
    }
    
    /**
     * Searches for which team piece could capture the checker
     * for its king to save it from checkmate.
     * @param king
     *          the square contains king to be search
     * @return squares of pieces could capture checker and protect its king
     */
    static public ArrayList<Square> getProtector(Square king){
        ArrayList<Square> protectors = new ArrayList<Square>();
        ArrayList<Square> checkers = getChecker(king);
        for(Square checker : checkers) {
            protectors.addAll(getChecker(checker));
        }
        return protectors;
    }
    
    /**
     * Searches for which team piece could block the checker
     * for its king to save it from checkmate.
     * @param king
     *          the square contains king to be search
     * @return squares of pieces could block checker and protect its king
     */
    static public ArrayList<Square> getBlocker(Square king){
        ArrayList<Square> blockers = new ArrayList<Square>();
        for (Square[] col : board)
            for (Square row : col)
                if (row.getChess() != null 
                && row.getChess().isRed() == king.getChess().isRed()) {
                    ArrayList<Square> movelist = getBasicMove(row);
                    if (getBlockMove(row, movelist).size() > 0)
                        blockers.add(row);
                }
        return blockers;
    }
    
    /**
     * Sets the current square enable and highlight it.
     * @param square
     *              the square to be set
     */
    static public void setEnable(Square square) {
        square.hightLight();
        square.setEnable(true);
    }

    /**
     * Changes the game turn
     */
    public static void setTurn() {
        isRedTurn = !isRedTurn;  
    }

    /**
     * Gets the square contains king for given team.
     * @param isRed
     *          color of given team
     * @return the square contains king for given team.
     */
    public static Square getKing(boolean isRed) {
        Square king = null;
        for (Square[] col : board)
            for(Square row : col)
                if (row.getChess() != null 
                && row.getChess() instanceof King
                && row.getChess().isRed() == isRed)
                    king = row;
        return king;
    }
    
    /**
     * Gets the squares contains rooks for given team.
     * @param isRed
     *          color of given team
     * @return the squares contains rooks for given team.
     */
    public static ArrayList<Square> getRook(boolean isRed) {
        ArrayList<Square> rooks = new ArrayList<Square>();
        for (Square[] col : board)
            for(Square row : col)
                if (row.getChess() != null 
                && row.getChess() instanceof Rook
                && row.getChess().isRed() == isRed)
                    rooks.add(row);
        return rooks;
    }
    
    /**
     * Sets all the given squares enable.
     * @param ableMoves
     *              squares to be set
     */
    private static void enableAll(ArrayList<Square> ableMoves) {
        for(Square a : ableMoves)
            setEnable(a);
    }

    /**
     * Return the isRedTurn for this Rule.
     * @return isRedTurn
     */
    public static boolean isRedTurn() {
        return isRedTurn;
    }

    /**
     * Return the redCastling for this Rule.
     * @return redCastling
     */
    public static boolean isRedCastling() {
        return redCastling;
    }

    /**
     * Sets the redCastling for this Rule.
     * @param redCastling the redCastling to set
     */
    public static void setRedCastling(boolean redCastling) {
        Rule.redCastling = redCastling;
    }

    /**
     * Return the blueCastling for this Rule.
     * @return blueCastling
     */
    public static boolean isBlueCastling() {
        return blueCastling;
    }

    /**
     * Sets the blueCastling for this Rule.
     * @param blueCastling the blueCastling to set
     */
    public static void setBlueCastling(boolean blueCastling) {
        Rule.blueCastling = blueCastling;
    }

}
