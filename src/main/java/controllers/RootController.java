package main.java.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.*;
import main.java.beans.Table;

import java.util.Objects;


public class RootController {

    @FXML
    public TextField search;
    @FXML
    public ListView<Table> viewList;
    @FXML
    public Button addBtn;

    ObservableList<Table> list = FXCollections.observableArrayList();
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "", ButtonType.YES, ButtonType.NO);

    @FXML
    public void itemSelected(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            Table selected = viewList.getSelectionModel().getSelectedItem();
            if (Objects.nonNull(selected))
                System.out.println(selected.toString());
        }
    }

    @FXML
    public void addTable(ActionEvent actionEvent) {
        if (Objects.isNull(actionEvent)) {
            list.add(new Table(search.getText()));
        } else {
            // Need to be dealt with
            list.add(new Table(search.getText()));
        }
        viewList.setItems(list);
    }

    @FXML
    public void searchValChanged(KeyEvent keyEvent) {
        String entry = search.getText();
        if (!entry.isBlank()) {
            if (new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN).match(keyEvent)) {
                Alert confirmationAlert = configureAlert("Add '" + entry + "' table?");
                confirmationAlert.showAndWait()
                        .filter(response -> response ==  ButtonType.YES)
                        .ifPresent(response -> {
                            System.out.println("Add new Table");
                            addTable(null);
                        });
            } else if (keyEvent.getCode() == KeyCode.ENTER) {
                System.out.println("Search");
            }
        }
    }

    private Alert configureAlert(String context) {
        alert.setContentText(context);
        alert.setHeaderText(null);
        alert.setGraphic(null);
        return alert;
    }
}
