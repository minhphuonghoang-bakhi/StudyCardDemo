package org.example.studycarddemo.dao;
import org.example.studycarddemo.entity.Card;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data-Access-Object (DAO) für Card-Objekte.
 * Kapselt alle Datenbankzugriffe rund um Karten (Erstellen, Lesen, Aktualisieren,
 * Löschen sowie das Hochzählen der Lernstatistik) über JDBC.
 */
public class CardCRUD {
    //helper method for clean code
    private Card Track_rs(ResultSet rs) throws SQLException {
        Card card = new Card(
                rs.getInt("id"),
                rs.getInt("set_id"),
                rs.getString("question"),
                rs.getString("answer"));
        card.setTimesKnown(rs.getInt("times_known"));
        card.setTimesUnknown(rs.getInt("times_unknown"));
        return card;
    }
    //Create cards
    public Card create(Card card) throws SQLException {
        String sql = "INSERT INTO card (set_id, question, answer) VALUES (?, ?, ?)";
        try (Connection c = DatabaseConnection.get();
        PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, card.getSetId());
            ps.setString(2, card.getQuestion());
            ps.setString(3, card.getAnswer());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                //the generated key stored in the resultset
                if (keys.next()) {
                    card.setId(keys.getInt(1));
                    //card has no id: set the id for the inserted card by the generated serial in DB
                }
            }
        }
        return card;
    }
    //READ - find by the set ID (one card must belong to one set)
    public List<Card> read(int setId) throws SQLException {
        List<Card> cards = new ArrayList<>();
        String sql = "SELECT id, set_id, question, answer, times_known, times_unknown " +
                "FROM card WHERE set_id = ? ORDER BY id";
        try (Connection c = DatabaseConnection.get();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, setId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                //while loop to add all into the arraylist until the cursor points to null
                    cards.add(Track_rs(rs));
                }
            }
        }
        return cards;
    }
    //Update
    public void update(Card card) throws SQLException {
        String sql = "UPDATE card SET question = ?, answer = ? WHERE id = ?";
        try (Connection c = DatabaseConnection.get();
        PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, card.getQuestion());
            ps.setString(2, card.getAnswer());
            ps.setInt(3, card.getId());
            ps.executeUpdate();
        }
    }
    //Delete - just need the id of the card, do not need delete(Card card)
    public void delete(int cardId) throws SQLException {
        String sql = "DELETE FROM card WHERE id = ?";
        try (Connection c = DatabaseConnection.get();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, cardId);
            ps.executeUpdate();
        }
    }
    public void incrementKnown(int cardId) throws SQLException {
        String sql = "UPDATE card SET times_known = times_known + 1 WHERE id = ?";
        try (Connection c = DatabaseConnection.get();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, cardId);
            ps.executeUpdate();
        }
    }
    public void incrementUnknown(int cardId) throws SQLException {
        String sql = "UPDATE card SET times_unknown = times_unknown + 1 WHERE id = ?";
        try (Connection c = DatabaseConnection.get();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, cardId);
            ps.executeUpdate();
        }
    }
}
