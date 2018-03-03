
/**
 * 
 * Knight.class
 * One of the six chess pieces.
 *
 * @author Zhuo (Cody) Ning
 * @version 2018
 */
public class Knight extends Chess {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * 
     * Constructs an object of type Knight.
     * @param isRed
     *              color of this Knight.
     */
    public Knight(boolean isRed) {
        super(isRed);
        setText("♘");
        setImageAddress(isRed?"Knight-l.png":"Knight-d.png");
    }

}
