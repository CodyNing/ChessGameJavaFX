import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * 
 * Launch.class
 * Launches the chess game.
 *
 * @author Zhuo (Cody) Ning
 * @version 2018
 */
public class Launch extends Application {
    
    /**
     * Game stage of this chess game.
     */
    private static Stage game;
    
    /**
     * Chess board of this chess game.
     */
    private static Board chess;
    
    /**
     * Pop up window for chess game to communicate with player.
     */
    private static Stage popWindow;
    
    /**
     * MenuBar of this chess game.
     */
    private static MenuBar menuBar;

    /**
     * Drive the program
     * @param args
     *              command line arguments.
     */
    public static void main(String[] args) {  
        Loader.loadImage();
        launch(args);

    }

    @Override
    /**
     * Set up main stage for chess game.
     * @see javafx.application.Application#start(javafx.stage.Stage)
     * @param game 
     *              Main stage
     * @throws Exception
     */
    public void start(Stage game) throws Exception {
        VBox vbox = new VBox();
        Scene scene;
        Launch.game = game;
        chess = new Board();
        
        menuBar = new MenuBar();
        Menu menuFile = new Menu("File");
        MenuItem save = new MenuItem("save");
        save.setOnAction((event)->{
            savefile();
        });
        MenuItem load = new MenuItem("load");
        load.setOnAction((event)->{           
            loadfile(); 
        });
        menuFile.getItems().addAll(save, load);
        menuBar.getMenus().add(menuFile);
        
        vbox.getChildren().addAll(menuBar, chess);

        scene = new Scene(vbox, 800, 825, Color.BLACK);

        game.setTitle("Chess Game");
        game.setScene(scene);
        game.show();
        
    }
    
    /**
     * Load and return saved game.
     */
    private void loadfile() {
        Board load = null;
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(new ExtensionFilter(
                "I-am-a-chess-game-save-data-and-I-have-a-very-long-long-long-long-suffix",
                "*.I-am-a-chess-game-save-data-and-I-have-a-very-long-long-long-long-suffix"));
        File file = chooser.showOpenDialog(game);
        if (file != null) {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
                load = (Board) in.readObject();
                load.restore();
            } catch (Exception exc) {
                exc.printStackTrace();
            }
        }
        if(load != null) {
            VBox v = new VBox(menuBar, load);
            Scene s = new Scene(v, 800, 825, Color.BLACK);
            game.setScene(s);
        }
        
    }

    /**
     * Save current game board as a file.
     */
    private void savefile() {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(new ExtensionFilter(
                "I-am-a-chess-game-save-data-and-I-have-a-very-long-long-long-long-suffix", 
                "*.I-am-a-chess-game-save-data-and-I-have-a-very-long-long-long-long-suffix"));
        File file = chooser.showSaveDialog(game);
        if (file != null) {
            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
                chess.save();
                out.writeObject(chess);
            } catch (Exception exc) {
                exc.printStackTrace();
            }
}
    }

    /**
     * Pop up a window to interact with players.
     * @param n
     *              Layout scene to show on the pop up window.
     * @param title
     *              Title(purpose) of the window.
     * @param width
     *              Stage(window) width.
     * @param height
     *              Stage(window) height.
     */
    public static void popWindow(Parent n, String title, int width, int height) {
        popWindow = new Stage();
        Scene scene = new Scene(n, width, height);
        popWindow.setTitle(title);
        popWindow.setScene(scene);
        popWindow.initStyle(StageStyle.UNDECORATED);
        popWindow.initModality(Modality.APPLICATION_MODAL);
        popWindow.show();
    }
    
    /**
     * Close the pop'd up window.
     */
    public static void closePopWindow() {
        popWindow.close();
    }

}
