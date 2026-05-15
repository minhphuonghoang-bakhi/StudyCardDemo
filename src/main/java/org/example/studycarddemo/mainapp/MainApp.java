package org.example.studycarddemo.mainapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import org.example.studycarddemo.entity.StudySet;
import org.example.studycarddemo.UI.CardEditorController;
import org.example.studycarddemo.UI.StudyModeController;

import java.io.IOException;

public class MainApp extends Application {

    private static Stage primaryStage;
    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 600;

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;
        primaryStage.setTitle("Study Cards");
        showSetOverview();
        primaryStage.show();
    }

    public static void showSetOverview() throws IOException {
        FXMLLoader loader = new FXMLLoader(
                MainApp.class.getResource("/org/example/studycarddemo/fxml/SetOverview.fxml"));
        setRoot(loader.load());
    }

    public static void showCardEditor(StudySet set) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                MainApp.class.getResource("/org/example/studycarddemo/fxml/CardEditor.fxml"));
        Parent root = loader.load();
        CardEditorController controller = loader.getController();
        controller.setStudySet(set);
        setRoot(root);
    }

    public static void showStudyMode(StudySet set) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                MainApp.class.getResource("/org/example/studycarddemo/fxml/StudyMode.fxml"));
        Parent root = loader.load();
        StudyModeController controller = loader.getController();
        controller.setStudySet(set);
        setRoot(root);
    }

    private static void setRoot(Parent root) {
        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        scene.getStylesheets().add(
                MainApp.class.getResource("/org/example/studycarddemo/css/styles.css").toExternalForm());
        primaryStage.setScene(scene);
    }

    public static void main(String[] args) {
        launch(args);
    }
}