package com.zedination.diffwrappertool;

import com.zedination.diffwrappertool.controller.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.JMetroStyleClass;
import jfxtras.styles.jmetro.Style;

import java.io.IOException;
import java.util.Objects;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("main-view.fxml"));
        Parent root = fxmlLoader.load();
        root.getStyleClass().add(JMetroStyleClass.BACKGROUND);
        JMetro jMetro = new JMetro(Style.DARK);
//        JMetro jMetro = new JMetro(Style.LIGHT);
        Scene scene = new Scene(root);
        jMetro.setScene(scene);
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/app.png"))));
        stage.setTitle("Tool Export Git Diff Source!");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.sizeToScene();
        stage.show();
        MainController mainController = fxmlLoader.getController();
        if (getParameters().getRaw().size() > 0) {
            mainController.setLocalRepoTextFieldContent(getParameters().getRaw().get(0));
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}