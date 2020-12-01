package AutoShiftScheduler;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Undergraduate final year individual computing project - KV6003
 * Northumbria University - 2019/20
 * 
 * @author Syed Ali - w17023496
 * 
 * Main home screen class
 */
public class Homescreen extends Application {
        
    /**
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        
     
        Parent root = FXMLLoader.load(getClass().getResource("HomescreenFXML.fxml"));
               
        Scene scene = new Scene (root);
        primaryStage.setScene(scene);        
        primaryStage.setResizable(false);
        primaryStage.show();  
        

    }

    /**
     * @param args
     */
    public static void main(String[] args) {           
        launch(args);       
    }
    
}
