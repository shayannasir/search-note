package controllers;

import beans.User;
import constants.MessageConstants;
import daos.UserDAOImpl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import utility.BCrypt;
import utility.Context;

import java.util.Date;
import java.util.Objects;
import java.util.regex.Pattern;

public class RegisterController {

    UserDAOImpl userDAO = new UserDAOImpl();
    public static final String TABLE_VIEW_PATH = "/views/root.fxml";

    @FXML
    public TextField username;
    @FXML
    public PasswordField password;
    @FXML
    public Text errorText;
    @FXML
    public Button login;
    @FXML
    public Button register;

    @FXML
    public void registerUser(ActionEvent actionEvent) {
        if (isValidAuthForm()) {
            User user = userDAO.findUserByUsername(username.getText());
            if (Objects.isNull(user)) {
                user = new User();
                user.setUsername(username.getText());
                user.setEnabled(Boolean.TRUE);
                user.setCreatedDate(new Date());
                user.setPassword(BCrypt.hashpw(password.getText(), BCrypt.gensalt(12)));
                if (userDAO.createNewUser(user)) {
                    User savedUser = userDAO.findUserByUsername(user.getUsername());
                    if (Objects.nonNull(savedUser)) {
                        Context.getInstance().setUser(savedUser);
                        loadRootWindow();
                    } else
                        errorText.setText("Something went wrong, please try again");
                }
            } else
                errorText.setText("'"+ username.getText() + "' user already exists");
        }
    }

    @FXML
    public void loginUser(ActionEvent actionEvent) {
        if (isValidAuthForm()) {
            User user = userDAO.findUserByUsername(username.getText());
            if (Objects.nonNull(user)) {
                if (BCrypt.checkpw(password.getText(), user.getPassword())) {
                    Context.getInstance().setUser(user);
                    loadRootWindow();
                } else {
                    errorText.setText("Incorrect password");
                }
            } else
                errorText.setText("No user found with the username");
        }
    }

    private boolean isValidAuthForm() {
        if (
                username == null ||
                username.getText() == null ||
                username.getText().isBlank() ||
                username.getText().length() < 3 ||
                !Pattern.matches("^[0-9a-z]{3,20}$", username.getText())
        ){
            errorText.setText(MessageConstants.USERNAME_INVALID);
            return false;
        } else if (
                password == null ||
                password.getText() == null ||
                password.getText().isBlank() ||
                password.getText().length() < 8 ||
                password.getText().length() > 20
        ) {
            errorText.setText(MessageConstants.PASSWORD_INVALID);
            return false;
        }
        return true;
    }

    private void loadRootWindow() {
        Stage currentStage = (Stage) errorText.getScene().getWindow();
        Stage rootStage = new Stage();
        rootStage.initModality(Modality.WINDOW_MODAL);
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(TABLE_VIEW_PATH));
            Parent rootScene = loader.load();
            rootStage.setScene(new Scene(rootScene));
            rootStage.setTitle("Table View");
            rootStage.setHeight(650);
            rootStage.setWidth(850);
            rootStage.centerOnScreen();
            rootStage.show();
            currentStage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
