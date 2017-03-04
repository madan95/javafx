package ku.piii.madanapp;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    private Stage primaryStage;
    
    @Override
    public void start(Stage stage) throws Exception {
        this.primaryStage = stage;
        Parent parent = FXMLLoader.load(getClass().getResource("/fxml/start.fxml"));

        Scene scene = new Scene(parent);
        scene.getStylesheets().add("/styles/Styles.css");

        primaryStage.setTitle("JavaFX and Maven");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    
    
    public static void main(String[] args) {
        launch(args);
    }
    
    public Stage getPrimaryStage(){
        return primaryStage;
    }

}
