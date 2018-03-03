
/**
 * 
 * King.class
 * One of the six chess pieces.
 *
 * @author Zhuo (Cody) Ning
 * @version 2018
 */
public class King extends Chess {
    
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * Indicates whether this king is in the first round(have not moved).
     */
    private boolean firstRound;
    
    /**
     * 
     * Constructs an object of type King.
     * @param isRed
     *              color of this King.
     */
    public King(boolean isRed) {
        super(isRed);
        setText("♔ ");
        setImageAddress(isRed?"King-l.png":"King-d.png");
        firstRound = true;
    }
    
    /**
     * Return the firstRound for this King.
     * @return firstRound
     */
    public boolean isFirstRound() {
        return firstRound;
    }

    /**
     * Sets the firstRound for this King.
     * @param firstRound the firstRound to set
     */
    public void setFirstRound(boolean firstRound) {
        this.firstRound = firstRound;
    }

    
    

}
