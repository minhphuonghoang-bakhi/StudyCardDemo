package org.example.studycarddemo.UI;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import org.example.studycarddemo.mainapp.MainApp;
import org.example.studycarddemo.dao.CardCRUD;
import org.example.studycarddemo.entity.Card;
import org.example.studycarddemo.entity.StudySet;

import java.sql.SQLException;

/**
 * Controller for the Card Editor screen.
 */
public class CardEditorController {

    @FXML private Label setNameLabel;
    @FXML private ListView<Card> cardListView;
    @FXML private TextArea questionArea;
    @FXML private TextArea answerArea;
    @FXML private Button saveButton;
    @FXML private Button deleteButton;
    @FXML private Button newButton;

    private final CardCRUD cardDAO = new CardCRUD();
    private StudySet currentSet;
    private Card editingCard;  // null = creating new card

    @FXML
    public void initialize() {
        cardListView.getSelectionModel().selectedItemProperty().addListener((obs, old, selected) -> {
            if (selected != null) {
                editingCard = selected;
                questionArea.setText(selected.getQuestion());
                answerArea.setText(selected.getAnswer());
                deleteButton.setDisable(false);
            }
        });
    }

    public void setStudySet(StudySet set) {
        this.currentSet = set;
        setNameLabel.setText("Editing: " + set.getName());
        loadCards();
    }

    private void loadCards() {
        try {
            cardListView.setItems(FXCollections.observableArrayList(
                    cardDAO.read(currentSet.getId())));
        } catch (SQLException e) {
            showError("Failed to load cards", e.getMessage());
        }
    }

    @FXML
    private void onNew() {
        editingCard = null;
        cardListView.getSelectionModel().clearSelection();
        questionArea.clear();
        answerArea.clear();
        deleteButton.setDisable(true);
        questionArea.requestFocus();
    }

    @FXML
    private void onSave() {
        String question = questionArea.getText().trim();
        String answer = answerArea.getText().trim();

        if (question.isEmpty() || answer.isEmpty()) {
            showError("Missing input", "Both question and answer are required.");
            return;
        }

        try {
            if (editingCard == null) {
                Card card = new Card(currentSet.getId(), question, answer);
                cardDAO.create(card);
            } else {
                editingCard.setQuestion(question);
                editingCard.setAnswer(answer);
                cardDAO.update(editingCard);
            }
            loadCards();
            onNew();
        } catch (SQLException e) {
            showError("Failed to save card", e.getMessage());
        }
    }

    @FXML
    private void onDelete() {
        if (editingCard == null) return;

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Delete Card");
        confirm.setHeaderText("Delete this card?");
        confirm.setContentText(editingCard.getQuestion());

        confirm.showAndWait().ifPresent(button -> {
            if (button == ButtonType.OK) {
                try {
                    cardDAO.delete(editingCard.getId());
                    loadCards();
                    onNew();
                } catch (SQLException e) {
                    showError("Failed to delete card", e.getMessage());
                }
            }
        });
    }

    @FXML
    private void onBack() {
        try {
            MainApp.showSetOverview();
        } catch (Exception e) {
            showError("Navigation error", e.getMessage());
        }
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}