package main.java.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.*;
import main.java.beans.Table;
import main.java.daos.TableDAOImpl;

import java.util.Objects;


public class RootController {

    ObservableList<Table> list = FXCollections.observableArrayList();
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "", ButtonType.YES, ButtonType.NO);
    TableDAOImpl tableDAO = new TableDAOImpl();

    @FXML
    public TextField search;
    @FXML
    public ListView<Table> viewList;
    @FXML
    public Button addBtn;

    @FXML
    public void initialize() {
        populateList();
    }

    @FXML
    public void itemSelected(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
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
            }
        }
    }

    private Alert configureAlert(String context) {
        alert.setContentText(context);
        alert.setHeaderText(null);
        alert.setGraphic(null);
        return alert;
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

    private void handleNewInsertion(String keyword) {
        Alert confirmationAlert = configureAlert("Add '" + keyword + "' table?");
        confirmationAlert.showAndWait()
                .filter(response -> response ==  ButtonType.YES)
                .ifPresent(response -> {
                    System.out.println("Add new Table");
                    addNewTable(new Table(keyword));
                });
    }

    private void refreshScene() {
        search.setText("");
        populateList();
    }

}
