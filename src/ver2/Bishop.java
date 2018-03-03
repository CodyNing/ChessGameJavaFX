package ver2;

/**
 * 
 * Bishop.class
 * One of the six chess pieces.
 *
 * @author Zhuo (Cody) Ning
 * @version 2018
 */
public class Bishop extends Chess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * 
     * Constructs an object of type Bishop.
     * @param isRed
     *              color of this Bishop.
     */
    public Bishop(boolean isRed) {
        super(isRed);
        int[][] movement = {
                {1, 1}, {-1, 1}, {-1, -1}, {1, -1}
        };
        setMovement(movement);
        setStep(7);
        setText("♗");
        setImageAddress(isRed?"Bishop-l.png":"Bishop-d.png");
    }

}
