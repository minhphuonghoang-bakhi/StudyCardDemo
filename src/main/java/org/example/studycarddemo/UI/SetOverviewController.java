package org.example.studycarddemo.UI;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import org.example.studycarddemo.mainapp.MainApp;
import org.example.studycarddemo.dao.StudySetCRUD;
import org.example.studycarddemo.entity.StudySet;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Controller for the Set Overview screen.
 */
public class SetOverviewController {

    @FXML private ListView<StudySet> setListView;
    @FXML private Label descriptionLabel;
    @FXML private Button editCardsButton;
    @FXML private Button studyButton;
    @FXML private Button editSetButton;
    @FXML private Button deleteSetButton;

    private final StudySetCRUD setDAO = new StudySetCRUD();

    @FXML
    public void initialize() {
        loadSets();
        setListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSet, newSet) -> {
            boolean hasSelection = newSet != null;
            editCardsButton.setDisable(!hasSelection);
            studyButton.setDisable(!hasSelection);
            editSetButton.setDisable(!hasSelection);
            deleteSetButton.setDisable(!hasSelection);
            descriptionLabel.setText(hasSelection ? newSet.getDescription() : "");
        });
    }

    private void loadSets() {
        try {
            List<StudySet> sets = setDAO.findAll();
            setListView.setItems(FXCollections.observableArrayList(sets));
        } catch (SQLException e) {
            showError("Failed to load study sets", e.getMessage());
        }
    }

    @FXML
    private void onCreateSet() {
        Optional<StudySet> result = showSetDialog(null);
        result.ifPresent(set -> {
            try {
                setDAO.create(set);
                loadSets();
            } catch (SQLException e) {
                showError("Failed to create set", e.getMessage());
            }
        });
    }

    @FXML
    private void onEditSet() {
        StudySet selected = setListView.getSelectionModel().getSelectedItem();
        if (selected == null) return;
        Optional<StudySet> result = showSetDialog(selected);
        result.ifPresent(set -> {
            try {
                setDAO.update(set);
                loadSets();
            } catch (SQLException e) {
                showError("Failed to update set", e.getMessage());
            }
        });
    }

    @FXML
    private void onDeleteSet() {
        StudySet selected = setListView.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Delete Set");
        confirm.setHeaderText("Delete \"" + selected.getName() + "\"?");
        confirm.setContentText("All cards in this set will also be deleted. Continue?");

        confirm.showAndWait().ifPresent(button -> {
            if (button == ButtonType.OK) {
                try {
                    setDAO.delete(selected.getId());
                    loadSets();
                } catch (SQLException e) {
                    showError("Failed to delete set", e.getMessage());
                }
            }
        });
    }

    @FXML
    private void onEditCards() {
        StudySet selected = setListView.getSelectionModel().getSelectedItem();
        if (selected == null) return;
        try {
            MainApp.showCardEditor(selected);
        } catch (Exception e) {
            showError("Failed to open card editor", e.getMessage());
        }
    }

    @FXML
    private void onStudy() {
        StudySet selected = setListView.getSelectionModel().getSelectedItem();
        if (selected == null) return;
        try {
            MainApp.showStudyMode(selected);
        } catch (Exception e) {
            showError("Failed to start study mode", e.getMessage());
        }
    }

    private Optional<StudySet> showSetDialog(StudySet existingSet) {
        Dialog<StudySet> dialog = new Dialog<>();
        dialog.setTitle(existingSet == null ? "New Study Set" : "Edit Study Set");
        dialog.setHeaderText(null);

        ButtonType saveType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveType, ButtonType.CANCEL);

        TextField nameField = new TextField();
        nameField.setPromptText("Set name");
        TextArea descField = new TextArea();
        descField.setPromptText("Description (optional)");
        descField.setPrefRowCount(3);

        if (existingSet != null) {
            nameField.setText(existingSet.getName());
            descField.setText(existingSet.getDescription());
        }

        javafx.scene.layout.VBox content = new javafx.scene.layout.VBox(10,
                new Label("Name:"), nameField,
                new Label("Description:"), descField);
        content.setPadding(new javafx.geometry.Insets(10));
        dialog.getDialogPane().setContent(content);

        dialog.setResultConverter(button -> {
            if (button == saveType && !nameField.getText().isBlank()) {
                if (existingSet != null) {
                    existingSet.setName(nameField.getText().trim());
                    existingSet.setDescription(descField.getText().trim());
                    return existingSet;
                } else {
                    return new StudySet(nameField.getText().trim(), descField.getText().trim());
                }
            }
            return null;
        });

        return dialog.showAndWait();
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
