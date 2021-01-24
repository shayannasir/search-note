package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import beans.Table;
import constants.MessageConstants;
import daos.TableDAOImpl;
import utility.Utility;

import java.util.Objects;


public class RootController {

    ObservableList<Table> list = FXCollections.observableArrayList();
    TableDAOImpl tableDAO = new TableDAOImpl();

    public static final String ENTRIES_VIEW_PATH = "/views/entries.fxml";

    @FXML
    public TextField search;
    @FXML
    public ListView<Table> viewList;
    @FXML
    public Text status;

    @FXML
    public void initialize() {
        populateList();
    }

    @FXML
    public void itemSelected(KeyEvent keyEvent) {
        if (new KeyCodeCombination(KeyCode.ENTER, KeyCombination.CONTROL_DOWN).match(keyEvent)) {
            handleTableUpdate();
            keyEvent.consume();
        } else if (new KeyCodeCombination(KeyCode.DELETE, KeyCombination.CONTROL_DOWN).match(keyEvent)) {
            handleTableDelete();
            keyEvent.consume();
        } else if (keyEvent.getCode() == KeyCode.ENTER) {
            Table selected = viewList.getSelectionModel().getSelectedItem();
//            if (Objects.nonNull(selected))
//                System.out.println(selected.toString());
            loadEntryWindow(selected);
        } else if (new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_DOWN).match(keyEvent)) {
            Table selected = viewList.getSelectionModel().getSelectedItem();
            if (Utility.setToClipboard(selected.getName()))
                status.setText(selected.getName());
        }else if (new KeyCodeCombination(KeyCode.W, KeyCombination.CONTROL_DOWN).match(keyEvent)) {
            closeStage();
        }
    }

    @FXML
    public void searchValChanged(KeyEvent keyEvent) {
        String entry = search.getText();
        if (!entry.isBlank()) {
            populateListWithResult(entry);
        } else {
            refreshScene();
        }
    }

    @FXML
    public void checkKeyCombination(KeyEvent keyEvent) {
        String entry = search.getText();
        if (!entry.isBlank()) {
            if (new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN).match(keyEvent)) {
                handleNewInsertion(entry);
                keyEvent.consume();
            } else if (new KeyCodeCombination(KeyCode.W, KeyCombination.CONTROL_DOWN).match(keyEvent)) {
                // not working rn
                closeStage();
                keyEvent.consume();
            }
        }
    }

    private void populateListWithResult(String keyword) {
        list.clear();
        list.addAll(tableDAO.findAllByKeyword(keyword));
        viewList.setItems(list);
    }

    private void populateList() {
        list.clear();
        list.addAll(tableDAO.findAll());
        viewList.setItems(list);
    }

    private void addNewTable(Table newTable) {
        if (tableDAO.addTable(newTable)) {
            refreshScene();
        }
    }

    private void updateTable(Table oldTable, Table newTable) {
        if (!oldTable.getName().isBlank()) {
            if (tableDAO.updateTable(oldTable, newTable)) {
                refreshScene();
            }
        }
    }

    private void deleteTable(Table table) {
        if (tableDAO.deleteTable(table)) {
            refreshScene();
        }
    }

    private void handleNewInsertion(String keyword) {
        Alert confirmationAlert = configureAlert("Add '" + keyword + "' table?");
        confirmationAlert.showAndWait()
                .filter(response -> response == ButtonType.YES)
                .ifPresent(response -> {
                    addNewTable(new Table(keyword));
                });
    }

    private void handleTableUpdate() {
        Table selectCell = viewList.getSelectionModel().getSelectedItem();
        TextInputDialog updateDialog = configureInputDialog(selectCell.getName(), MessageConstants.UPDATE_TABLE_DIALOG_LABEL);
        updateDialog.showAndWait()
                .ifPresent(response -> {
                    updateTable(selectCell, new Table(response));
                });
    }

    private void handleTableDelete() {
        Table selectCell = viewList.getSelectionModel().getSelectedItem();
        configureAlert(MessageConstants.DELETE_TABLE_DIALOG_LABEL + selectCell.getName() + "?")
                .showAndWait()
                .filter(response -> response == ButtonType.YES)
                .ifPresent(response -> deleteTable(selectCell));

    }

    private void loadEntryWindow(Table table) {
        Stage entryStage = new Stage();
        entryStage.initModality(Modality.APPLICATION_MODAL);
        entryStage.initOwner(viewList.getScene().getWindow());
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(ENTRIES_VIEW_PATH));
            Parent entryScene = loader.load();
            EntryController entryController = loader.getController();
            entryController.setParentTable(table);
            entryStage.setScene(new Scene(entryScene));
            entryStage.centerOnScreen();
            entryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private Alert configureAlert(String context) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "", ButtonType.YES, ButtonType.NO);
        alert.setContentText(context);
        alert.setHeaderText(null);
        alert.setGraphic(null);
        return alert;
    }

    private TextInputDialog configureInputDialog(String placeholder, String context) {
        TextInputDialog textInputDialog = new TextInputDialog(placeholder);
        textInputDialog.setHeaderText(null);
        textInputDialog.setGraphic(null);
        textInputDialog.setContentText(context);
        return textInputDialog;
    }

    private void closeStage() {
        Stage activeStage = (Stage) viewList.getScene().getWindow();
        activeStage.close();
    }

    private void refreshScene() {
        search.setText("");
        populateList();
    }

}
