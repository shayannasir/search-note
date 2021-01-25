package controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import beans.Entry;
import beans.Table;
import constants.EntryConstants;
import constants.MessageConstants;
import daos.EntryDAOImpl;
import utility.Utility;

import java.util.Objects;

public class EntryController {

    EntryDAOImpl entryDAO = new EntryDAOImpl();
    private Table parentTable;
    private ObservableList<Entry> entryList = FXCollections.observableArrayList();

    @FXML
    public AnchorPane anchorPane;
    @FXML
    public Text tableId;
    @FXML
    public Text tableName;
    @FXML
    public TextField entrySearch;
    @FXML
    public Text status;
    @FXML
    public TableView<Entry> viewTable;
    @FXML
    public TableColumn<Entry, String> keyCol;
    @FXML
    public TableColumn<Entry, String> valCol;
    @FXML
    public TableColumn<Entry, String> descCol;


    @FXML
    public void initialize() {
        initializeTableCols();
    }

    @FXML
    public void checkKeyCombination(KeyEvent keyEvent) {
        String keyword = entrySearch.getText();
        if (keyEvent.getCode() == KeyCode.ESCAPE) {
            closeStage();
            keyEvent.consume();
        } else if (new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN).match(keyEvent)
                && !keyword.isBlank()) {
            handleNewInsertion(keyword);
            keyEvent.consume();
        } else if (keyEvent.getCode() == KeyCode.DOWN) {
            Platform.runLater(() -> {
                viewTable.requestFocus();
                viewTable.getSelectionModel().select(0);
                viewTable.getFocusModel().focus(0);
            });
        }
    }

    @FXML
    public void searchEntryValChanged(KeyEvent keyEvent) {
        String keyword = entrySearch.getText();
        if (!keyword.isBlank()) {
            populateTableWithResult(keyword);
        } else {
            refreshScene();
        }
    }

    @FXML
    public void itemSelected(KeyEvent keyEvent) {
        Entry selected = viewTable.getSelectionModel().getSelectedItem();
        if (keyEvent.getCode() == KeyCode.ESCAPE) {
            closeStage();
            keyEvent.consume();
        } else if (keyEvent.getCode() == KeyCode.ENTER) {
            handleUpdate(selected);
        } else if (new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_DOWN).match(keyEvent)) {
            if (Utility.setToClipboard(selected.getValue())) {
                status.setText(selected.getValue());
                keyEvent.consume();
            }
        } else if (new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_DOWN, KeyCombination.SHIFT_DOWN).match(keyEvent)) {
            if (Utility.setToClipboard(selected.getKey())) {
                status.setText(selected.getKey());
                keyEvent.consume();
            }
        } else if (new KeyCodeCombination(KeyCode.DELETE, KeyCombination.CONTROL_DOWN).match(keyEvent)) {
            handleDeleteEntry(selected);
            keyEvent.consume();
        }
    }

    public void setParentTable(Table parentTable) {
        this.parentTable = parentTable;
        tableId.setText(parentTable.getId());
        tableName.setText(parentTable.getName());
        populateTable();
    }

    private void initializeTableCols() {
        keyCol.setCellValueFactory(new PropertyValueFactory<>(EntryConstants.KEY));
        valCol.setCellValueFactory(new PropertyValueFactory<>(EntryConstants.VALUE));
        descCol.setCellValueFactory(new PropertyValueFactory<>(EntryConstants.DESCRIPTION));
    }

    private void populateTable() {
        entryList.clear();
        entryList.addAll(entryDAO.findAllByTable(parentTable));
        viewTable.setItems(entryList);
    }

    private void populateTableWithResult(String keyword) {
        if (!keyword.isBlank()) {
            entryList.clear();
            entryList.addAll(entryDAO.findAllByTableAndKey(parentTable, keyword));
            viewTable.setItems(entryList);
        }
    }

    private void handleNewInsertion(String keyword) {
        configureDialog("Add New Entry under " + parentTable.getName(), "ADD", keyword, null, "ADD");
    }

    private void handleUpdate(Entry entry) {
        configureDialog("Update Entry", "UPDATE", "", entry, "UPDATE");
    }

    private void configureDialog(String title, String btnTitle, String keyword, Entry entry, String action) {
        title = title.isBlank() ? "Add New Entry" : title;
        btnTitle = btnTitle.isBlank() ? "ADD" : btnTitle;
        keyword = keyword.isBlank() ? entry.getKey() : keyword;
        Dialog<Entry> dialog = new Dialog<>();
        dialog.setTitle(title);
        dialog.setHeaderText(null);
        dialog.setGraphic(null);
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPrefWidth(500);
        gridPane.setAlignment(Pos.CENTER);
        ButtonType addBtn = new ButtonType(btnTitle, ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL, addBtn);
        Node addBtnControl = dialog.getDialogPane().lookupButton(addBtn);

        TextField key = new TextField(keyword);
        key.setPromptText(EntryConstants.KEY);
        key.textProperty().addListener((observable, oldVal, newVal) -> addBtnControl.setDisable(newVal.trim().isEmpty()));
        TextArea value = new TextArea();
        value.setPromptText(EntryConstants.VALUE);
        value.setPrefWidth(300);
        value.setPrefHeight(25);
        TextArea desc = new TextArea();
        desc.setPromptText(EntryConstants.DESCRIPTION);
        desc.setPrefWidth(300);
        desc.setPrefHeight(50);

        if (Objects.nonNull(entry)) {
            key.setText(entry.getKey());
            value.setText(entry.getValue());
            desc.setText(entry.getDescription());
        }

        gridPane.add(new Label(EntryConstants.KEY.toUpperCase()), 0, 0);
        gridPane.add(key, 1, 0);
        gridPane.add(new Label(EntryConstants.VALUE.toUpperCase()), 0, 1);
        gridPane.add(value, 1, 1);
        gridPane.add(new Label(EntryConstants.DESCRIPTION.toUpperCase()), 0, 2);
        gridPane.add(desc, 1, 2);
        dialog.getDialogPane().setContent(gridPane);
        Platform.runLater(key::requestFocus);
        dialog.setResultConverter(dialogBtn -> {
            if (dialogBtn == addBtn) {
                return new Entry(key.getText(), value.getText(), desc.getText());
            }
            return null;
        });
        dialog.showAndWait()
                .ifPresent(resultEntry -> {
                    if (action.equalsIgnoreCase("ADD"))
                        addNewEntry(resultEntry);
                    else
                        updateEntry(entry, resultEntry);
                });
    }

    private void addNewEntry(Entry entry) {
        if (!entry.getKey().isBlank() && !entry.getKey().equalsIgnoreCase("null")) {
            entry.setTableId(parentTable.getId());
            entryDAO.addEntry(entry);
        }
        refreshScene();
    }

    private void updateEntry(Entry oldEntry, Entry newEntry) {
        if (!oldEntry.getId().isBlank()) {
            newEntry.setTableId(oldEntry.getTableId());
            if (entryDAO.updateEntry(oldEntry, newEntry))
                refreshScene();
        }
    }

    private void deleteEntry(Entry entry) {
        if (entryDAO.deleteEntry(entry))
            refreshScene();
    }

    private void handleDeleteEntry(Entry entry) {
        configureAlert(MessageConstants.DELETE_TABLE_DIALOG_LABEL + entry.getKey() + " field?")
                .showAndWait()
                .filter(response -> response == ButtonType.YES)
                .ifPresent(response -> deleteEntry(entry));
    }

    private Alert configureAlert(String context) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "", ButtonType.YES, ButtonType.NO);
        alert.setContentText(context);
        alert.setHeaderText(null);
        alert.setGraphic(null);
        return alert;
    }

    private void refreshScene() {
        entrySearch.setText("");
        populateTable();
    }

    private void closeStage() {
        Stage activeStage = (Stage) entrySearch.getScene().getWindow();
        activeStage.close();
    }
}
