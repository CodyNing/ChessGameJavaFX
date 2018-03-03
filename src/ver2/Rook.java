package ver2;

/**
 * 
 * Rook.class
 * One of the six chess pieces.
 *
 * @author Zhuo (Cody) Ning
 * @version 2018
 */
public class Rook extends Chess {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;
    /**
     * Indicates whether this rook is in the first round(have not moved).
     */
    private boolean firstRound;
    
    /**
     * 
     * Constructs an object of type Rook.
     * @param isRed
     *              color of this rook.
     */
    public Rook(boolean isRed) {
        super(isRed);
        int[][] movement = {
                {1, 0}, {0, 1}, {-1, 0}, {0, -1}
        };
        setMovement(movement);
        setStep(7);
        setText("♖");
        setImageAddress(isRed?"Rook-l.png":"Rook-d.png");
        firstRound = true;
    }
    
    /**
     * Return the firstRound for this Rook.
     * @return firstRound
     */
    public boolean isFirstRound() {
        return firstRound;
    }

    /**
     * Sets the firstRound for this Rook.
     * @param firstRound the firstRound to set
     */
    public void setFirstRound(boolean firstRound) {
        this.firstRound = firstRound;
    }


}
