package org.example.studycarddemo.services;
import org.example.studycarddemo.entity.Card;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//features of the app and logic for GUI
public class StudyService {
    private List<Card> cards = new ArrayList<>();
    private int currentIdx = 0;
    private int knownCount = 0;
    private int unknownCount = 0;
    private boolean answerRevealed = false;

    public void startSession(List<Card> cards, boolean shuffle) {
        this.cards = new ArrayList<>(cards);
        //shallow copy the cards from the parameter-cards into the cards in data field
        //avoid sharing reference with the caller => accidentially adjust the original card lists
        if (shuffle) {
            Collections.shuffle(this.cards);
        }
        //reset for new session
        this.currentIdx = 0;
        this.knownCount = 0;
        this.unknownCount = 0;
        this.answerRevealed = false;
    }
    //avoid OutOfBounds Error
    /*for ex: after learning the last card - markknown - nextCard(CurrentIdx++)
    - CurrentIdx >= cards.size = finished - getCurrentCard = null
    */
    public boolean isFinished() {
        if (currentIdx >= cards.size()) {
            return true;
        }
        else { return false;}
    }
    public Card getCurrentCard() {
        return isFinished() ? null : cards.get(currentIdx);
    }
    public void revealAnswer() { //user has clicked on "show Ans"
        answerRevealed = true;
    }
    //getter for answerRevealed to check if answer is shown or not yet
    public boolean isAnswerRevealed() {
        return answerRevealed;
    }
    //go to the next card in one set of cards
    // when user has shown the answer of card 1, then answerRevealed = true
    // then go to the card 2, the answerRevealed is still true - nope, need to reset
    private void nextCard() {
        currentIdx++;
        answerRevealed = false;
    }
    public void markKnown() {
        knownCount++;
        nextCard();
    }
    public void markUnknown() {
        unknownCount++;
        nextCard();
    }
    public int getKnownCount() { return knownCount; }
    public int getUnknownCount() { return unknownCount; }
    public int getTotalCount() { return cards.size(); }
    public int getCurrentPosition() { return Math.min(currentIdx + 1, cards.size()); }
    //Math.min no needed just to make sure always displaying correctly´ß
}
