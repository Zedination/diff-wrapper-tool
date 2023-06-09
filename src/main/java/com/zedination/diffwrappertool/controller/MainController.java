package com.zedination.diffwrappertool.controller;

import com.zedination.diffwrappertool.component.AutoCompleteTextField;
import com.zedination.diffwrappertool.constant.Constant;
import com.zedination.diffwrappertool.constant.EnumCommon;
import com.zedination.diffwrappertool.model.GlobalState;
import com.zedination.diffwrappertool.service.ConfigService;
import com.zedination.diffwrappertool.service.GitService;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.JMetroStyleClass;
import jfxtras.styles.jmetro.Style;
import lombok.Getter;
import org.controlsfx.control.ToggleSwitch;
import org.controlsfx.control.textfield.TextFields;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;

public class MainController {

    @FXML
    private ToggleSwitch bcSwitch;

    @FXML
    @Getter
    private TextField leftCommitTextField;

    @Getter
    @FXML
    private TextField beyondComparePathInput;

    @FXML
    @Getter
    private TextField rightCommitTextField;

    @FXML
    @Getter
    private Button selectRepoButton;

    @FXML
    private TextField localRepoTextField;

    @FXML
    private AutoCompleteTextField branch1Input;

    @FXML
    private AutoCompleteTextField branch2Input;

    @FXML
    private Button diffCommitButton;

    @FXML
    private Button diffBranchButton;

    @FXML
    private ProgressBar diffProgressBar;

    public void setLocalRepoTextFieldContent(String text) {
        this.localRepoTextField.setText(text);
        try {
            GlobalState.selectedLocalRepository = text;
            GitService.getInstance().initGitState(GlobalState.selectedLocalRepository);
            List<String> branchList = GitService.getInstance().getListBranch();
            branch1Input.getEntries().clear();
            branch2Input.getEntries().clear();
            branch2Input.getEntries().addAll(branchList);
            branch1Input.getEntries().addAll(branchList);
        } catch (Exception e) {
            alertFail("Folder bạn chọn không phải là một Git repository, vui lòng chọn lại!");
        }
    }

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
    protected void onSwitchBranchButton() {
        String temp = this.branch1Input.getText();
        this.branch1Input.setText(this.branch2Input.getText());
        this.branch2Input.setText(temp);
        this.branch1Input.getEntriesPopup().hide();
        this.branch2Input.getEntriesPopup().hide();
    }

    @FXML
    protected void selectLocalRepoButton (Event event){
        Node node = (Node) event.getSource();
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Chọn folder git bạn muốn diff source");
        File file = directoryChooser.showDialog(node.getScene().getWindow());
        if (Objects.nonNull(file)) {
            this.localRepoTextField.setText(file.getAbsolutePath());
            try {
                GlobalState.selectedLocalRepository = file.getAbsolutePath();
                GitService.getInstance().initGitState(GlobalState.selectedLocalRepository);
                List<String> branchList = GitService.getInstance().getListBranch();
                branch1Input.getEntries().clear();
                branch2Input.getEntries().clear();
                branch2Input.getEntries().addAll(branchList);
                branch1Input.getEntries().addAll(branchList);
            } catch (Exception e) {
                alertFail("Folder bạn chọn không phải là một Git repository, vui lòng chọn lại!");
            }
        }
    }

    @FXML
    protected void selectBeyondComparePath(Event event) throws IOException {
        Node node = (Node) event.getSource();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Chọn file Bcomp cần mở");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(".exe file", "*.exe"));
        File file = fileChooser.showOpenDialog(node.getScene().getWindow());
        if (Objects.nonNull(file)) {
            beyondComparePathInput.setText(file.getAbsolutePath());
            ConfigService.getInstance().saveConfig(Constant.BEYOND_COMPARE_PATH, this.beyondComparePathInput.getText());
            GitService.getInstance().changeConfigForBeyondCompare(this.beyondComparePathInput.getText());
        }
    }

    @FXML
    protected void handleTextChangedBeyondPath() throws IOException {
        if (!this.beyondComparePathInput.getText().isEmpty()) {
            ConfigService.getInstance().saveConfig(Constant.BEYOND_COMPARE_PATH, this.beyondComparePathInput.getText());
            GitService.getInstance().changeConfigForBeyondCompare(this.beyondComparePathInput.getText());
        }
    }

    @FXML
    protected void handleTextChangedGitPath() {
        if (!this.localRepoTextField.getText().isEmpty()) {
            try {
                GlobalState.selectedLocalRepository = this.localRepoTextField.getText();
                GitService.getInstance().initGitState(GlobalState.selectedLocalRepository);
                List<String> branchList = GitService.getInstance().getListBranch();
                branch1Input.getEntries().clear();
                branch2Input.getEntries().clear();
                branch2Input.getEntries().addAll(branchList);
                branch1Input.getEntries().addAll(branchList);
            } catch (Exception e) {
                alertFail("Folder bạn chọn không phải là một Git repository, vui lòng chọn lại!");
            }
        }
    }

    @FXML
    protected void startDiffByCommit() {
        Service<Void> diffService = new Service<Void>() {

            protected void succeeded() {
                super.succeeded();
                diffCommitButton.setDisable(false);
                diffProgressBar.progressProperty().unbind();
                diffProgressBar.setVisible(false);
            }

            @Override
            public void start() {
                super.start();
                diffCommitButton.setDisable(true);
                diffProgressBar.setVisible(true);
            }

            @Override
            protected void failed() {
                super.failed();
                diffCommitButton.setDisable(false);
                diffProgressBar.progressProperty().unbind();
                diffProgressBar.setVisible(false);
            }

            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() {
                        try {
                            if (!GlobalState.isBeyondCompare) {
                                GitService.getInstance().diffHtml(leftCommitTextField.getText(), rightCommitTextField.getText());
                            } else {
                                GitService.getInstance().diffBeyondCompare(leftCommitTextField.getText(), rightCommitTextField.getText());
                            }
                        } catch (Exception e) {
                            alertFail(e.getMessage());
                        }
                        return null;
                    }
                };
            }
        };
        this.diffProgressBar.progressProperty().bind(diffService.progressProperty());
        diffService.start();
    }

    @FXML
    protected void enableBeyondCompare() throws IOException {
        if (this.bcSwitch.isSelected()) {
            GitService.getInstance().changeConfigForBeyondCompare(this.beyondComparePathInput.getText());
            GlobalState.isBeyondCompare = true;
        } else {
            GlobalState.isBeyondCompare = false;
        }
    }

    @FXML
    protected void onClickDiffBranch() {
        Service<Void> diffService = new Service<Void>() {

            protected void succeeded() {
                super.succeeded();
                diffBranchButton.setDisable(false);
                diffProgressBar.progressProperty().unbind();
                diffProgressBar.setVisible(false);
            }

            @Override
            public void start() {
                super.start();
                diffCommitButton.setDisable(true);
                diffProgressBar.setVisible(true);
            }

            @Override
            protected void failed() {
                super.failed();
                diffBranchButton.setDisable(false);
                diffProgressBar.progressProperty().unbind();
                diffProgressBar.setVisible(false);
            }

            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() {
                        try {
                            if (!GlobalState.isBeyondCompare) {
                                GitService.getInstance().diffHtml(branch1Input.getText(), branch2Input.getText());
                            } else {
                                GitService.getInstance().diffBeyondCompare(branch1Input.getText(), branch2Input.getText());
                            }
                        } catch (Exception e) {
                            alertFail(e.getMessage());
                        }
                        return null;
                    }
                };
            }
        };
        this.diffProgressBar.progressProperty().bind(diffService.progressProperty());
        diffService.start();
    }

    @FXML
    public void initialize() throws IOException {
        String userPath = System.getProperty("user.home");
        Files.createDirectories(Paths.get(userPath + "/AppData/Local/Diff Wrapper"));
        File icoIcon = new File(userPath + "/AppData/Local/Diff Wrapper/" + Constant.icon);
        if (!icoIcon.exists()) {
            Files.copy(Objects.requireNonNull(getClass().getResourceAsStream("/app.ico")), Paths.get(userPath + "/AppData/Local/Diff Wrapper/"+Constant.icon), StandardCopyOption.REPLACE_EXISTING);
        }
        ConfigService.getInstance().registerWindowsContextMenu();
        beyondComparePathInput.setText(ConfigService.getInstance().readConfig(Constant.BEYOND_COMPARE_PATH));
        ConfigService.getInstance().copyBundleHtmlDiff();
        System.out.println("Application start!");
    }

    private void alertFail(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Lỗi!");
        alert.setContentText(message);
        alert.getDialogPane().getStyleClass().add(JMetroStyleClass.BACKGROUND);
        alert.setHeaderText("Có lỗi xảy ra!");
        alert.initModality(Modality.APPLICATION_MODAL);
        new JMetro(Style.DARK).setParent(alert.getDialogPane());
        alert.show();
    }
}