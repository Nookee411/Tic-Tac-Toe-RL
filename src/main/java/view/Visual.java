package view;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;


public class Visual extends Application {


    @Override @FXML
    public void start(Stage stage) throws Exception {
        File file = new File("D:/Учеба/3 курс/1 семестр/Курсовой проект/Application/src/main/resources/fxml/GameVisuals.fxml");
        URL url = file.toURI().toURL();
        Parent root = FXMLLoader.load(url);

        stage.setScene(new Scene(root));
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
