package main.java;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import main.java.constants.MessageConstants;
import main.java.controllers.RootController;

import java.io.IOException;


public class SearchNote extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setScene(loadTableScene());

        primaryStage.setTitle(MessageConstants.HOME);
        primaryStage.centerOnScreen();
        primaryStage.setHeight(650);
        primaryStage.setWidth(850);
        primaryStage.show();

    }

    @Override
    public void init() throws Exception {
        System.out.println(MessageConstants.APP_STARTING);
        Thread.sleep(100);
        System.out.println(MessageConstants.APP_STARTED);
    }

    @Override
    public void stop() throws Exception {
        System.out.println(MessageConstants.APP_STOPPING);
        Thread.sleep(100);
        System.out.println(MessageConstants.APP_STOPPED);
    }

    private Scene loadTableScene() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/main/resources/views/root.fxml"));
        Scene scene = new Scene(root);
//        scene.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
//            @Override
//            public void handle(KeyEvent event) {
//                if (new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN).match(event)) {
//                    System.out.println("bruh!!!");
//                    event.consume();
//                }
//            }
//        });
        return scene;
    }
}
