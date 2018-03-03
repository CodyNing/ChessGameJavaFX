
/**
 * 
 * Pawn.class
 * One of the six chess pieces.
 *
 * @author Zhuo (Cody) Ning
 * @version 2018
 */
public class Pawn extends Chess {
    
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * Indicates whether this pawn is in the first round(have not moved).
     */
    private boolean firstRound;
    
    /**
     * Indicates whether this pawn can present a En Passant capture.
     */
    private boolean enPassant;

    /**
     * 
     * Constructs an object of type Pawn.
     * @param isRed
     *              color of this Pawn.
     */
    public Pawn(boolean isRed) {
        super(isRed);
        setText("♙");
        setImageAddress(isRed?"Pawn-l.png":"Pawn-d.png");
        firstRound = true;
    }

    /**
     * Return the enPassant for this Pawn.
     * @return enPassant
     */
    public boolean isEnPassant() {
        return enPassant;
    }

    /**
     * Sets the enPassant for this Pawn.
     * @param enPassant the enPassant to set
     */
    public void setEnPassant(boolean enPassant) {
        this.enPassant = enPassant;
    }

    /**
     * Return the firstRound for this Pawn.
     * @return firstRound
     */
    public boolean isFirstRound() {
        return firstRound;
    }

    /**
     * Sets the firstRound for this Pawn.
     * @param firstRound the firstRound to set
     */
    public void setFirstRound(boolean firstRound) {
        this.firstRound = firstRound;
    }

}
