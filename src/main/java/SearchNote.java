
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import constants.MessageConstants;

import java.io.IOException;

public class SearchNote extends Application {

    public static final String ENTRY_VIEW_PATH = "/views/entries.fxml";
    public static final String TABLE_VIEW_PATH = "/views/root.fxml";

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
        Parent root = FXMLLoader.load(getClass().getResource(TABLE_VIEW_PATH));
        return new Scene(root);
    }

    private Scene loadEntryScene() throws IOException {
        Parent entry = FXMLLoader.load(getClass().getResource(ENTRY_VIEW_PATH));
        return new Scene(entry);
    }
}
