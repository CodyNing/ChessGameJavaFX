package ver2;
import javafx.scene.image.Image;

/**
 * ImageIcon.class
 * Image icon of chess game.
 *
 * @author Zhuo (Cody) Ning
 * @version 2018
 */
public class ImageIcon extends Image implements MyIcon {

    /**
     * Constructs an object of type ImageIcon.
     * @param address
     *              image's system path.
     */
    public ImageIcon(String address) {
        super(address);       
    }
    
}
