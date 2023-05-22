package com.zedination.diffwrappertool.controller;

import com.zedination.diffwrappertool.constant.EnumCommon;
import com.zedination.diffwrappertool.model.Commit;
import com.zedination.diffwrappertool.model.GlobalState;
import com.zedination.diffwrappertool.service.GitService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import org.eclipse.jgit.revwalk.RevCommit;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.TimeZone;

public class CommitController {

    @FXML
    private TableView<Commit> listCommitTable;

    @FXML
    private Label labelTitle;

    @FXML
    private TableColumn<Commit, String> shaColumn;

    @FXML
    private TableColumn<Commit, String> messageColumn;

    @FXML
    private TableColumn<Commit, String> authorColumn;

    @FXML
    private TableColumn<Commit, String> dateColumn;

    @FXML
    private TextField searchInput;

    private Commit selectedItem;

    @Getter
    @Setter
    private String refBranchName;

    @Getter
    @Setter
    private MainController mainController;

    @Getter
    @Setter
    private EnumCommon commitPosition;

    private ContextMenu contextMenu;

    private final List<Commit> fullCommitList = new ArrayList<>();

    private final List<Commit> commitList = new ArrayList<>();

    @FXML
    public void initialize() throws IOException {
        labelTitle.setText(labelTitle.getText() + GlobalState.selectedBranch);
        this.shaColumn.setCellValueFactory(new PropertyValueFactory<>("sha"));
        this.messageColumn.setCellValueFactory(new PropertyValueFactory<>("message"));
        this.authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        this.dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

        List<RevCommit> listCommit = GitService.getInstance().getListCommit();
        listCommit.forEach(x -> {
            this.commitList.add(new Commit(x.getId().getName(), x.getFullMessage(), x.getAuthorIdent().getName(), String.valueOf(LocalDateTime.ofInstant(Instant.ofEpochSecond(x.getCommitTime()),
                    TimeZone.getDefault().toZoneId()))));
            this.fullCommitList.add(new Commit(x.getId().getName(), x.getFullMessage(), x.getAuthorIdent().getName(), String.valueOf(LocalDateTime.ofInstant(Instant.ofEpochSecond(x.getCommitTime()),
                    TimeZone.getDefault().toZoneId()))));
        });
        listCommitTable.setItems(FXCollections.observableList(commitList));

        listCommitTable.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && Objects.nonNull(this.contextMenu) && this.contextMenu.isShowing()) {
                this.contextMenu.hide();
            }
        });

        listCommitTable.setRowFactory(tv -> {
            TableRow<Commit> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getButton() == MouseButton.SECONDARY && !row.isEmpty()) {
                    createContextMenu();
                    this.contextMenu.show(listCommitTable, event.getScreenX(), event.getScreenY());
                    this.selectedItem = listCommitTable.getSelectionModel().getSelectedItem();
                }
            });
            return row;
        });
    }

    @FXML
    private void onKeypressSearchInput(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            List<Commit> temp = fullCommitList.stream().filter(this::findAll).toList();
            listCommitTable.getItems().clear();
            listCommitTable.getItems().addAll(temp);
        }
    }

    private boolean findAll(Commit commit) {
        String keyword = searchInput.getText().trim().toLowerCase();
        return commit.getSha().toLowerCase().contains(keyword) || commit.getMessage().toLowerCase().contains(keyword)
                || commit.getAuthor().toLowerCase().contains(keyword) || commit.getDate().toLowerCase().contains(keyword);
    }

    private void createContextMenu() {
        if (Objects.nonNull(this.contextMenu)) {
            this.contextMenu.hide();
        }
        ContextMenu contextMenu = new ContextMenu();
        MenuItem menuItem1 = new MenuItem("Chọn commit này");
        MenuItem menuItem2 = new MenuItem("Compare with local");
        menuItem1.setOnAction(e -> {
            if (EnumCommon.LEFT_COMMIT.equals(this.commitPosition)) {
                mainController.getLeftCommitTextField().setText(selectedItem.getSha());
            } else {
                mainController.getRightCommitTextField().setText(selectedItem.getSha());
            }
            Stage stage = (Stage) listCommitTable.getScene().getWindow();
            stage.close();
        });
        menuItem2.setOnAction(e -> {
            try {
                if (!GlobalState.isBeyondCompare) {
                    GitService.getInstance().diffHtml(selectedItem.getSha(), "");
                } else {
                    GitService.getInstance().diffBeyondCompare(selectedItem.getSha(), "");
                }
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        contextMenu.getItems().addAll(menuItem1, menuItem2);
        this.contextMenu = contextMenu;
    }
}
