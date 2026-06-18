package org.example.studycarddemo.entity;
import java.util.*;

/**
 * Repräsentiert ein Lernset, also eine benannte Sammlung von Card-Objekten.
 * Ein Lernset besitzt einen Namen, eine optionale Beschreibung und eine Liste
 * zugehöriger Karten.
 */
public class StudySet {
    private int id;
    private String name;
    private String description;
    private List<Card> cards = new ArrayList<>();
    //no-arg constructor
    public StudySet() {
    }
    //arg-constructor with id: load from DB (for resultset)
    public StudySet(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }
    //arg-constructor without id: new create - no id
    public StudySet(String name, String description) {
        this.name = name;
        this.description = description;
    }
    // getters n setters
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }
    public List<Card> getCards() {
        return cards;
    }
    public void setId(int id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setCards(List<Card> cards) {
        this.cards = cards;
    }
    @Override
    public String toString() { return name; } //for good-looking ListView
}

