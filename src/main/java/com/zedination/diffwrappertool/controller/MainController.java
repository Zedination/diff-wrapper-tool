package com.zedination.diffwrappertool.controller;

import com.zedination.diffwrappertool.constant.EnumCommon;
import com.zedination.diffwrappertool.model.GlobalState;
import com.zedination.diffwrappertool.service.GitService;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.JMetroStyleClass;
import jfxtras.styles.jmetro.Style;
import lombok.Getter;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class MainController {
    @FXML
    private Button switchButton;

    @FXML
    @Getter
    private TextField leftCommitTextField;

    @FXML
    @Getter
    private TextField rightCommitTextField;

    @FXML
    @Getter
    private Button selectRepoButton;

    @FXML
    private TextField localRepoTextField;

    @FXML
    protected void showListCommitWindows() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/zedination/diffwrappertool/commit-view.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = new Stage();
        CommitController commitController = fxmlLoader.getController();
        commitController.setMainController(this);
        commitController.setCommitPosition(EnumCommon.LEFT_COMMIT);
        stage.initModality(Modality.APPLICATION_MODAL);
        root.getStyleClass().add(JMetroStyleClass.BACKGROUND);
        JMetro jMetro = new JMetro(Style.DARK);
        Scene scene = new Scene(root);
        jMetro.setScene(scene);
        stage.setTitle("Danh sách commit!");
        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();
    }

    @FXML
    protected void showListCommitWindowsRight() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/zedination/diffwrappertool/commit-view.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = new Stage();
        CommitController commitController = fxmlLoader.getController();
        commitController.setMainController(this);
        commitController.setCommitPosition(EnumCommon.RIGHT_COMMIT);
        stage.initModality(Modality.APPLICATION_MODAL);
        root.getStyleClass().add(JMetroStyleClass.BACKGROUND);
        JMetro jMetro = new JMetro(Style.DARK);
        Scene scene = new Scene(root);
        jMetro.setScene(scene);
        stage.setTitle("Danh sách commit!");
        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();
    }

    @FXML
    protected void onSwitchButton() {
        String temp = leftCommitTextField.getText();
        leftCommitTextField.setText(rightCommitTextField.getText());
        rightCommitTextField.setText(temp);
    }

    @FXML
    protected void selectLocalRepoButton (Event event) throws IOException {
        Node node = (Node) event.getSource();
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Chọn folder git bạn muốn diff source");
        File file = directoryChooser.showDialog(node.getScene().getWindow());
        if (Objects.nonNull(file)) {
            this.localRepoTextField.setText(file.getAbsolutePath());
            GlobalState.selectedLocalRepository = file.getAbsolutePath();
            GitService.getInstance().initGitState(GlobalState.selectedLocalRepository);
        }
    }

    @FXML
    protected void startDiffByCommit() throws IOException {
        String template = "diff2html -s side -f html -d word -i command -o preview -- -M %s %s";
        ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", String.format(template, leftCommitTextField.getText(), rightCommitTextField.getText()));
        builder.directory(new File(GlobalState.selectedLocalRepository));
        Process p = builder.start();
    }

    @FXML
    public void initialize() throws IOException {
        System.out.println("Application start!");
    }
}