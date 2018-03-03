package ver2;

/**
 * 
 * Queen.class
 * One of the six chess pieces.
 *
 * @author Zhuo (Cody) Ning
 * @version 2018
 */
public class Queen extends Chess {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * 
     * Constructs an object of type Queen.
     * @param isRed
     *              color of this Queen.
     */
    public Queen(boolean isRed) {
        super(isRed);
        int[][] movement = {
                {1, 0}, {1, 1}, {0, 1}, {-1, 1}, {-1, 0}, {-1, -1}, {0, -1}, {1, -1}
        };
        setMovement(movement);
        setStep(7);
        setText("♕");
        setImageAddress(isRed?"Queen-l.png":"Queen-d.png");
    }

}
