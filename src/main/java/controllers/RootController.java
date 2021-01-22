package main.java.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.*;
import main.java.beans.Table;
import main.java.constants.TableConstants;
import main.java.daos.TableDAOImpl;

import java.security.Key;
import java.util.Objects;


public class RootController {

    ObservableList<Table> list = FXCollections.observableArrayList();
    TableDAOImpl tableDAO = new TableDAOImpl();

    @FXML
    public TextField search;
    @FXML
    public ListView<Table> viewList;

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
            if (Objects.nonNull(selected))
                System.out.println(selected.toString());
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
                .filter(response -> response ==  ButtonType.YES)
                .ifPresent(response -> {
                    addNewTable(new Table(keyword));
                });
    }

    private void handleTableUpdate() {
        Table selectCell = viewList.getSelectionModel().getSelectedItem();
        TextInputDialog updateDialog = configureInputDialog(selectCell.getName(), TableConstants.UPDATE_DIALOG_LABEL);
        updateDialog.showAndWait()
                .ifPresent(response -> {
                    updateTable(selectCell, new Table(response));
                });
    }

    private void handleTableDelete() {
        Table selectCell = viewList.getSelectionModel().getSelectedItem();
        configureAlert(TableConstants.DELETE_DIALOG_LABEL + selectCell.getName() + "?")
                .showAndWait()
                .filter(response -> response == ButtonType.YES)
                .ifPresent(response -> {
                    deleteTable(selectCell);
                });

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

    private void refreshScene() {
        search.setText("");
        populateList();
    }

}
