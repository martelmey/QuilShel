package gui.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    private static Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Main.primaryStage = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("/gui.main.fxml"));
        primaryStage.setTitle("QuilShell");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void init() {
//        try {
//            Deserializer.getInstance().loadPoem();
//        } catch (IOException e) {
//            System.out.println(e.getMessage());;
//        }
    }

    @Override
    public void stop() {
//        try {
//            Serializer.getInstance().savePoem();
//        } catch (IOException e) {
//            System.out.println(e.getMessage());
//        }
    }
}
