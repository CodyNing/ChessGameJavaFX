import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Loader.class pre-loads all icon to increase performance.
 *
 * @author Zhuo (Cody) Ning
 * @version 2018
 */
public class Loader {

    /**
     * All icon images of this chess game.
     */
    private static final MyIcon[] LOADEDIMAGES = new MyIcon[12];
    
    /**
     * Load image or icon in the class to increase performance.
     */
    public static void loadImage() {
        try {
            LOADEDIMAGES[0] = new ImageIcon("Pawn-l.png");
        } catch (IllegalArgumentException e) {
            System.out.println("Pawn-l.png IMAGE NOT FOUND");
            LOADEDIMAGES[0] = new UnicodeIcon("♙");
        }
        try {
            LOADEDIMAGES[1] = new ImageIcon("Rook-l.png");
        } catch (IllegalArgumentException e) {
            System.out.println("Rook-l.png IMAGE NOT FOUND");
            LOADEDIMAGES[1] = new UnicodeIcon("♖");
        }
        try {
            LOADEDIMAGES[2] = new ImageIcon("Knight-l.png");
        } catch (IllegalArgumentException e) {
            System.out.println("Knight-l.png IMAGE NOT FOUND");
            LOADEDIMAGES[2] = new UnicodeIcon("♘");
        }
        try {
            LOADEDIMAGES[3] = new ImageIcon("Bishop-l.png");
        } catch (IllegalArgumentException e) {
            System.out.println("Bishop-l.png IMAGE NOT FOUND");
            LOADEDIMAGES[3] = new UnicodeIcon("♗");
        }
        try {
            LOADEDIMAGES[4] = new ImageIcon("Queen-l.png");
        } catch (IllegalArgumentException e) {
            System.out.println("Queen-l.png IMAGE NOT FOUND");
            LOADEDIMAGES[4] = new UnicodeIcon("♕");
        }
        try {
            LOADEDIMAGES[5] = new ImageIcon("King-l.png");
        } catch (IllegalArgumentException e) {
            System.out.println("King-l.png IMAGE NOT FOUND");
            LOADEDIMAGES[5] = new UnicodeIcon("♔");
        }
        try {
            LOADEDIMAGES[6] = new ImageIcon("Pawn-d.png");
        } catch (IllegalArgumentException e) {
            System.out.println("Pawn-d.png IMAGE NOT FOUND");
            LOADEDIMAGES[6] = new UnicodeIcon("♟");
        }
        try {
            LOADEDIMAGES[7] = new ImageIcon("Rook-d.png");
        } catch (IllegalArgumentException e) {
            System.out.println("Rook-d.png IMAGE NOT FOUND");
            LOADEDIMAGES[7] = new UnicodeIcon("♜");
        }
        try {
            LOADEDIMAGES[8] = new ImageIcon("Knight-d.png");
        } catch (IllegalArgumentException e) {
            System.out.println("Knight-d.png IMAGE NOT FOUND");
            LOADEDIMAGES[8] = new UnicodeIcon("♞");
        }
        try {
            LOADEDIMAGES[9] = new ImageIcon("Bishop-d.png");
        } catch (IllegalArgumentException e) {
            System.out.println("Bishop-d.png IMAGE NOT FOUND");
            LOADEDIMAGES[9] = new UnicodeIcon("♝");
        }
        try {
            LOADEDIMAGES[10] = new ImageIcon("Queen-d.png");
        } catch (IllegalArgumentException e) {
            System.out.println("Queen-d.png IMAGE NOT FOUND");
            LOADEDIMAGES[10] = new UnicodeIcon("♛");
        }
        try {
            LOADEDIMAGES[11] = new ImageIcon("King-d.png");
        } catch (IllegalArgumentException e) {
            System.out.println("King-d.png IMAGE NOT FOUND");
            LOADEDIMAGES[11] = new UnicodeIcon("♚");
        }
     
    }
    
    /**
     * Set appropriate icon depending on chess type.
     * @param s Square to be set.
     */
    public static void getIcon(Square s) {
        Chess chess = s.getChess();
        if (chess instanceof Pawn)
            if (chess.isRed())
                setIcon(LOADEDIMAGES[0], s);
            else
                setIcon(LOADEDIMAGES[6], s);
        else if (chess instanceof Rook)
            if (chess.isRed())
                setIcon(LOADEDIMAGES[1], s);
            else
                setIcon(LOADEDIMAGES[7], s);
        else if (chess instanceof Knight)
            if (chess.isRed())
                setIcon(LOADEDIMAGES[2], s);
            else
                setIcon(LOADEDIMAGES[8], s);
        else if (chess instanceof Bishop)
            if (chess.isRed())
                setIcon(LOADEDIMAGES[3], s);
            else
                setIcon(LOADEDIMAGES[9], s);
        else if (chess instanceof Queen)
            if (chess.isRed())
                setIcon(LOADEDIMAGES[4], s);
            else
                setIcon(LOADEDIMAGES[10], s);
        else if (chess instanceof King)
            if (chess.isRed())
                setIcon(LOADEDIMAGES[5], s);
            else
                setIcon(LOADEDIMAGES[11], s);
    }
    
    /**
     * Set up icon for chess board square.
     * @param mi icon to set
     * @param s square to be set
     */
    public static void setIcon(MyIcon mi, Square s) {
        if(mi instanceof ImageIcon) {
            ImageView iv = new ImageView((ImageIcon) mi);
            iv.setFitHeight(90);
            iv.setFitWidth(45);
            s.setGraphic(iv);
        }
        else {
            s.setText(s.getChess().getText());
            s.setFont(Font.font(null, FontWeight.BOLD, 35));
            s.setTextFill(s.getChess().isRed() ? Color.RED : Color.BLUE);
        }
    }
}
