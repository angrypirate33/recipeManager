package com.angrypirate;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * JavaFX MainApp
 */
public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/angrypirate/views/MainView.fxml"));
        Scene scene = new Scene(loader.load());

        primaryStage.setWidth(1200);
        primaryStage.setHeight(800);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Recipe Manager");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}