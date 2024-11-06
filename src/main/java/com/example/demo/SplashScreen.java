package com.example.demo;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

import java.io.IOException;

public class SplashScreen extends Application {

    @FXML
    Label titleLabel;

    @Override
    public void start(Stage splashStage) throws IOException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("SplashScreen.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            splashStage.initStyle(StageStyle.UNDECORATED);
            splashStage.setScene(scene);
            splashStage.show();

            PauseTransition pause = new PauseTransition(Duration.seconds(3)); // 3 seconds delay
            pause.setOnFinished(event -> {
                // Close the splash screen and show the main scene
                splashStage.close();
                showMainStage();
            });
            pause.play();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void showMainStage() {
        try {
            Stage mainStage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("main.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            mainStage.setScene(scene);
            mainStage.setTitle("Harmony");
            Image icon=new Image("bully.jpg");
            mainStage.getIcons().add(icon);
            mainStage.show();
            mainStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent windowEvent) {
                    Platform.exit();
                    System.exit(0);
                }
            });
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
