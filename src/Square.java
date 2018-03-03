import java.io.Serializable;
import javafx.scene.control.Button;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

/**
 * 
 * Square.class
 * Uses javafx Button to simulate square of chess board.
 * It can be empty or contain a chess piece.
 * It can be set to highlight for interactive purpose.
 *
 * @author Zhuo (Cody) Ning
 * @version 2018
 */
public class Square extends Button implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * Chess piece this square contains.(Can be null)
     */
    private Chess chess;
    
    /**
     * Column of this square on chess board.
     */
    private int col;
    
    /**
     * Row of this square on chess board.
     */
    private int row;
    
    /**
     * Indicates whether this square is selectable.
     */
    boolean enable = false;
    
    /**
     * 
     * Constructs an object of type Square.(Empty)
     * @param col
     *              Column of this square on chess board.
     * @param row
     *              Row of this square on chess board.
     */
    public Square(int col, int row) {
        this.col = col;
        this.row = row;
        setUp();
    }
    
    public void setUp() {
        setBorder(new Border(new BorderStroke(Color.BLACK, 
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
          setPrefSize(100, 100);
          setChess(chess);
    }
    
    /**
     * 
     * Constructs an object of type Square.(Contains a chess piece)
     * @param chess
     *              Chess piece to be put on this Square.
     * @param col
     *              Column of this square on chess board.
     * @param row
     *              Row of this square on chess board.
     */
    public Square(Chess chess, int col, int row) {
        this.chess = chess;
        this.col = col;
        this.row = row;
        setBorder(new Border(new BorderStroke(Color.BLACK, 
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        setPrefSize(100, 100);
        setChess(chess);
    }
    
    /**
     * Return the enable for this Square.
     * @return enable
     */
    public boolean isEnable() {
        return enable;
    }

    /**
     * Sets the enable for this Square.
     * @param enable the enable to set
     */
    public void setEnable(boolean enable) {
        this.enable = enable;
    }
    
    /**
     * Return the chess for this Square.
     * @return Chess
     */
    public Chess getChess() {
        return chess;
    }
    
    /**
     * Sets the chess for this Square.
     * Sets this Square's feature depending on this square's condition and chess's property.
     * @param chess the enable to set
     */
    public void setChess(Chess chess) {
        if(chess != null) {
            this.chess = chess;               
            Loader.getIcon(this);
        }
        else {
            this.chess = null;
            setGraphic(null);
            setText("");
        }
    }
    
    /**
     * Highlight this square(Set background color to yellow).
     */
    public void hightLight() {
        setStyle("-fx-background-color: YELLOW; ");
    }

    /**
     * Return the col for this Square.
     * @return col
     */
    public int getCol() {
        return col;
    }

    /**
     * Sets the col for this Square.
     * @param col the col to set
     */
    public void setCol(int col) {
        this.col = col;
    }

    /**
     * Return the row for this Square.
     * @return row
     */
    public int getRow() {
        return row;
    }

    /**
     * Sets the row for this Square.
     * @param row the row to set
     */
    public void setRow(int row) {
        this.row = row;
    }

    /**
     * Dehighlight this square
     * (Set background color back depending on this square's position).
     */
    public void deHightLight() {
        setStyle(((col + row) % 2 == 0) ?
                "-fx-background-color: #2F4F4F; " : "-fx-background-color: BLACK;");
    }

    /**
     * Return toString description of this square's state(Testing purpose).
     * @see java.lang.Object#toString()
     * @return toString description
     */
    @Override
    public String toString() {
        return "Square [chess=" + chess + ", col=" + col + ", row=" + row + ", enable=" + enable + "]";
    }
}
