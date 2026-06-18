package org.example.studycarddemo.entity;

/**
 * Repräsentiert eine einzelne Lernkarte mit Frage und Antwort.
 * Eine Karte gehört immer zu genau einem StudySet (referenziert über
 * setId) und speichert zusätzlich, wie oft sie als bekannt bzw.
 * unbekannt markiert wurde.
 */
public class Card {
    private int id;
    private int setId;
    private String question;
    private String answer;
    private int timesKnown;
    private int timesUnknown;

    public Card() {
    }
    /** Erstellt eine Karte mit bekannter ID. Wird verwendet, wenn eine Karte
     * aus der Datenbank geladen wird (die ID stammt dann aus dem ResultSet).
     */
    public Card(int id, int setId, String question, String answer) {
        this.id = id;
        this.setId = setId;
        this.question = question;
        this.answer = answer;
    }
    public Card(int setId, String question, String answer) {
        this.id = id;
        this.setId = setId;
        this.question = question;
        this.answer = answer;
    }
    //getters setters
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getSetId() {
        return setId;
    }
    public void setSetId(int setId) {
        this.setId = setId;
    }
    public String getQuestion() {
        return question;
    }
    public void setQuestion(String question) {
        this.question = question;
    }
    public String getAnswer() {
        return answer;
    }
    public void setAnswer(String answer) {
        this.answer = answer;
    }
    public int getTimesKnown() {
        return timesKnown;
    }
    public void setTimesKnown(int timesKnown) {
        this.timesKnown = timesKnown;
    }
    public int getTimesUnknown() {
        return timesUnknown;
    }
    public void setTimesUnknown(int timesUnknown) {
        this.timesUnknown = timesUnknown;
    }
}
