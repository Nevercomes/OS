package controller;

import global.Global;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main extends Application {

    private Stage stage;

    public void showHome() {
        try {
            HomeController homeController = (HomeController) replaceSceneContent("/view/home.fxml");
            homeController.setApp(this);
        } catch (Exception e) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    private Initializable replaceSceneContent(String fxml) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        InputStream in = Main.class.getResourceAsStream(fxml);
        loader.setBuilderFactory(new JavaFXBuilderFactory());
        loader.setLocation(Main.class.getResource(fxml));
        Parent root;
        try {
            root = loader.load(in);
        } finally {
            in.close();
        }
        Scene scene = new Scene(root, Global.SIZE_SCENE_WIDTH, Global.SIZE_SCENE_HEIGHT);
        stage.setScene(scene);
        stage.sizeToScene();
        return (Initializable) loader.getController();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        stage.setTitle("OS_Simulation");
//        Parent root = FXMLLoader.load(getClass().getResource("/View/welcome.fxml"));
//        scene = new Scene(root, GlobalConst.SIZE_SCENE_WIDTH, GlobalConst.SIZE_SCENE_HEIGHT);
//        stage.setScene(scene);
        showHome();
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
