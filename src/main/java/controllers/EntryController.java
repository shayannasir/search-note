package main.java.controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import main.java.beans.Entry;
import main.java.beans.Table;
import main.java.constants.EntryConstants;
import main.java.daos.EntryDAOImpl;

public class EntryController {

    @FXML
    public Text tableId;
    @FXML
    public Text tableName;
    @FXML
    public TextField entrySearch;
    @FXML
    public TableView<Entry> viewTable;
    @FXML
    public TableColumn<Entry, String> keyCol;
    @FXML
    public TableColumn<Entry, String> valCol;
    @FXML
    public TableColumn<Entry, String> descCol;

    EntryDAOImpl entryDAO = new EntryDAOImpl();
    private Table parentTable;
    private ObservableList<Entry> entryList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        keyCol.setCellValueFactory(new PropertyValueFactory<>(EntryConstants.KEY));
        valCol.setCellValueFactory(new PropertyValueFactory<>(EntryConstants.VALUE));
        descCol.setCellValueFactory(
                new PropertyValueFactory<>(EntryConstants.DESCRIPTION));
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
        if (keyEvent.getCode() == KeyCode.ESCAPE) {
            closeStage();
            keyEvent.consume();
        }
    }

    public void setParentTable(Table parentTable) {
        this.parentTable = parentTable;
        tableId.setText(parentTable.getId());
        tableName.setText(parentTable.getName());
        populateTable();
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
        Dialog<Entry> dialog = new Dialog<>();
        dialog.setTitle("Add New Entry under " + parentTable.getName());
        dialog.setHeaderText(null);
        dialog.setGraphic(null);
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPrefWidth(500);
        gridPane.setAlignment(Pos.CENTER);
        ButtonType addBtn = new ButtonType("ADD", ButtonBar.ButtonData.OK_DONE);
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
                .ifPresent(this::addNewEntry);
    }

    private void addNewEntry(Entry entry) {
        if (!entry.getKey().isBlank() && !entry.getKey().equalsIgnoreCase("null")) {
            entry.setTableId(parentTable.getId());
            entryDAO.addEntry(entry);
        }
        refreshScene();
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
