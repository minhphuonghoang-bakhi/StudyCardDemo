package org.example.studycarddemo.dao;
import org.example.studycarddemo.entity.StudySet;
import java.util.*;
import java.sql.*;

public class StudySetCRUD {
    // CREATE study set
    public void create(StudySet set) throws SQLException {
        String sql = "INSERT INTO studyset(name, description) VALUES (?, ?)";
        try (Connection c = DatabaseConnection.get();
            PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, set.getName());
            ps.setString(2, set.getDescription());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    set.setId(keys.getInt(1));
                }
            }
        }
    }
    // UPDATE
    public void update(StudySet set) throws SQLException {
        String sql = "UPDATE studyset SET name = ?, description = ? WHERE id = ?";
        try (
                Connection c = DatabaseConnection.get();
                PreparedStatement ps = c.prepareStatement(sql);
                ) {
            ps.setString(1, set.getName());
            ps.setString(2, set.getDescription());
            ps.setInt(3, set.getId());
            ps.executeUpdate();
        }
    }
    // DELETE
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM studyset WHERE id = ?";
        try (Connection c = DatabaseConnection.get();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
    // READ
    // show all the sets
    public List<StudySet> findAll() throws SQLException {
        List<StudySet> result = new ArrayList<>();
        String sql = "SELECT id, name, description FROM studyset ORDER BY name";

        try (Connection c = DatabaseConnection.get();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                result.add(new StudySet(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description")
                ));
            }
        }
        return result;
    }
    // find by ID
    public StudySet findById(int id) throws SQLException {
        String sql = "SELECT id, name, description FROM study_set WHERE id = ?";
        try (Connection c = DatabaseConnection.get();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new StudySet(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("description")
                    );
                }
            }
        }
        return null;
    }
}

