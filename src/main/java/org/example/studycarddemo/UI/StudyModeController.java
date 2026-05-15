package org.example.studycarddemo.UI;

import javafx.fxml.FXML;
import javafx.scene.control.*;

import org.example.studycarddemo.mainapp.MainApp;
import org.example.studycarddemo.dao.CardCRUD;
import org.example.studycarddemo.entity.Card;
import org.example.studycarddemo.entity.StudySet;
import org.example.studycarddemo.services.StudyService;

import java.sql.SQLException;

/**
 * Controller for the Study Mode screen.
 */
public class StudyModeController {

    @FXML private Label progressLabel;
    @FXML private Label setNameLabel;
    @FXML private Label questionLabel;
    @FXML private Label answerLabel;
    @FXML private Button revealButton;
    @FXML private Button knownButton;
    @FXML private Button unknownButton;
    @FXML private Label resultLabel;

    private final StudyService studyService = new StudyService();
    private final CardCRUD cardDAO = new CardCRUD();

    public void setStudySet(StudySet set) {
        setNameLabel.setText("Studying: " + set.getName());
        try {
            var cards = cardDAO.read(set.getId());
            if (cards.isEmpty()) {
                showInfo("No cards", "This set has no cards yet. Add some first!");
                onBack();
                return;
            }
            studyService.startSession(cards, true);
            showCurrentCard();
        } catch (SQLException e) {
            showError("Failed to load cards", e.getMessage());
        }
    }

    private void showCurrentCard() {
        if (studyService.isFinished()) {
            showFinalResult();
            return;
        }

        Card card = studyService.getCurrentCard();
        questionLabel.setText(card.getQuestion());
        answerLabel.setText("");
        answerLabel.setVisible(false);
        revealButton.setDisable(false);
        knownButton.setDisable(true);
        unknownButton.setDisable(true);

        progressLabel.setText(String.format("Card %d of %d",
                studyService.getCurrentPosition(), studyService.getTotalCount()));
    }

    @FXML
    private void onReveal() {
        Card card = studyService.getCurrentCard();
        if (card == null) return;
        studyService.revealAnswer();
        answerLabel.setText(card.getAnswer());
        answerLabel.setVisible(true);
        revealButton.setDisable(true);
        knownButton.setDisable(false);
        unknownButton.setDisable(false);
    }

    @FXML
    private void onKnown() {
        Card card = studyService.getCurrentCard();
        if (card != null) {
            try {
                cardDAO.incrementKnown(card.getId());
            } catch (SQLException e) {
                System.err.println("Failed to update card stats: " + e.getMessage());
            }
        }
        studyService.markKnown();
        showCurrentCard();
    }

    @FXML
    private void onUnknown() {
        Card card = studyService.getCurrentCard();
        if (card != null) {
            try {
                cardDAO.incrementUnknown(card.getId());
            } catch (SQLException e) {
                System.err.println("Failed to update card stats: " + e.getMessage());
            }
        }
        studyService.markUnknown();
        showCurrentCard();
    }

    private void showFinalResult() {
        questionLabel.setText("Session complete!");
        answerLabel.setVisible(false);
        revealButton.setDisable(true);
        knownButton.setDisable(true);
        unknownButton.setDisable(true);
        resultLabel.setText(String.format(
                "Known: %d  |  Not known: %d  |  Total: %d",
                studyService.getKnownCount(),
                studyService.getUnknownCount(),
                studyService.getTotalCount()));
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

    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}