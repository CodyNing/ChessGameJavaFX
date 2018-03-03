package ver2;
import java.io.Serializable;

/**
 * Chess.class 
 * Parent class of all chess pieces.
 *
 * @author Zhuo (Cody) Ning
 * @version 2018
 */
abstract class Chess implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * Color of this chess piece.(Red or blue)
     */
    final private boolean isRed;
    
    /**
     * Basic movements of this chess piece.
     */
    private int[][] movement;
    
    /**
     * Basic movement step of this chess piece.
     */
    private int step;
    
    /**
     * Text feature of this chess piece.
     */
    private String text;
    
    /**
     * Icon feature's address of this chess piece.
     */
    private String imageAddress;
    
    /**
     * 
     * Constructs an object of type Chess.
     * @param isRed
     *              color of this chess.
     */
    public Chess(boolean isRed) {
        this.isRed = isRed;
    }
    
    /**
     * Return the isRed for this Chess.
     * @return isRed
     */
    public boolean isRed() {
        return isRed;
    }

    /**
     * Return the text for this Chess.
     * @return text
     */
    public String getText() {
        return text;
    }

    /**
     * Sets the text for this Chess.
     * @param text the text to set
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Return the view for this Chess.
     * @return view
     */
    public String getImageAddress() {
        return imageAddress;
    }

    /**
     * Sets the imageAddress for this Chess.
     * @param imageAddress the imageAddress to set
     */
    public void setImageAddress(String imageAddress) {
        this.imageAddress = imageAddress;
    }

    /**
     * Return the movement for this Chess.
     * @return movement
     */
    public int[][] getMovement() {
        return movement;
    }

    /**
     * Sets the movement for this Chess.
     * @param movement the movement to set
     */
    public void setMovement(int[][] movement) {
        this.movement = movement;
    }

    /**
     * Return the step for this Chess.
     * @return step
     */
    public int getStep() {
        return step;
    }

    /**
     * Sets the step for this Chess.
     * @param step the step to set
     */
    public void setStep(int step) {
        this.step = step;
    }

}
