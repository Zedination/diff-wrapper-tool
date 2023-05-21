package com.zedination.diffwrappertool.controller;

import com.zedination.diffwrappertool.constant.EnumCommon;
import com.zedination.diffwrappertool.model.Commit;
import com.zedination.diffwrappertool.service.GitService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
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
    private TableColumn<Commit, String> shaColumn;

    @FXML
    private TableColumn<Commit, String> messageColumn;

    @FXML
    private TableColumn<Commit, String> authorColumn;

    @FXML
    private TableColumn<Commit, String> dateColumn;

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

    @FXML
    public void initialize() throws IOException {
        this.shaColumn.setCellValueFactory(new PropertyValueFactory<>("sha"));
        this.messageColumn.setCellValueFactory(new PropertyValueFactory<>("message"));
        this.authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        this.dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

        List<RevCommit> listCommit = GitService.getInstance().getListCommit();
        List<Commit> commitList = new ArrayList<>();
        listCommit.forEach(x -> commitList.add(new Commit(x.getId().getName(), x.getFullMessage(), x.getAuthorIdent().getName(), String.valueOf(LocalDateTime.ofInstant(Instant.ofEpochSecond(x.getCommitTime()),
                        TimeZone.getDefault().toZoneId())))));
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

    private void createContextMenu() {
        if (Objects.nonNull(this.contextMenu)) {
            this.contextMenu.hide();
        }
        ContextMenu contextMenu = new ContextMenu();
        MenuItem menuItem1 = new MenuItem("Chọn commit này");
        menuItem1.setOnAction(e -> {
            if (EnumCommon.LEFT_COMMIT.equals(this.commitPosition)) {
                mainController.getLeftCommitTextField().setText(selectedItem.getSha());
            } else {
                mainController.getRightCommitTextField().setText(selectedItem.getSha());
            }
            Stage stage = (Stage) listCommitTable.getScene().getWindow();
            stage.close();
        });
        contextMenu.getItems().addAll(menuItem1);
        this.contextMenu = contextMenu;
    }
}
