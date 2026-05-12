package dao.sqlite;

import dao.ConnectionDB;
import dao.dao;
import model.ViaEsportiva;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ViaEsportivaDAO implements dao<ViaEsportiva, Integer> {

    public ViaEsportivaDAO() {}

    @Override
    public void insert(ViaEsportiva v) {
        String sql = "INSERT INTO vies_esportiva (id_via, llargada) VALUES (?, ?)";
        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, v.getIdVia());
            stmt.setInt(2, v.getLlargada());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error inserint via esportiva: " + e.getMessage());
        }
    }

    @Override
    public ViaEsportiva findById(Integer id) {
        String sql = "SELECT ve.*, v.* FROM vies_esportiva ve JOIN vies v ON ve.id_via = v.id_via WHERE ve.id_via = ?";
        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new ViaEsportiva(
                            rs.getInt("id_via"),
                            rs.getString("nom"),
                            rs.getString("grau"),
                            rs.getString("orientacio"),
                            rs.getString("estat"),
                            rs.getString("data_estat"),
                            rs.getString("tipus"),
                            rs.getString("ancoratges"),
                            rs.getString("tipus_roca"),
                            rs.getInt("id_creador"),
                            rs.getInt("id_sector"),
                            rs.getInt("id_escola"),
                            rs.getString("restriccions"),
                            rs.getInt("llargada")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error cercant via esportiva: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<ViaEsportiva> findAll() {
        List<ViaEsportiva> llista = new ArrayList<>();
        String sql = "SELECT ve.*, v.* FROM vies_esportiva ve JOIN vies v ON ve.id_via = v.id_via";
        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                llista.add(new ViaEsportiva(
                        rs.getInt("id_via"),
                        rs.getString("nom"),
                        rs.getString("grau"),
                        rs.getString("orientacio"),
                        rs.getString("estat"),
                        rs.getString("data_estat"),
                        rs.getString("tipus"),
                        rs.getString("ancoratges"),
                        rs.getString("tipus_roca"),
                        rs.getInt("id_creador"),
                        rs.getInt("id_sector"),
                        rs.getInt("id_escola"),
                        rs.getString("restriccions"),
                        rs.getInt("llargada")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error llistant vies esportives: " + e.getMessage());
        }
        return llista;
    }

    @Override
    public void update(ViaEsportiva v) {
        String sql = "UPDATE vies_esportiva SET llargada = ? WHERE id_via = ?";
        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, v.getLlargada());
            stmt.setInt(2, v.getIdVia());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error actualitzant via esportiva: " + e.getMessage());
        }
    }

    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM vies_esportiva WHERE id_via = ?";
        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error eliminant via esportiva: " + e.getMessage());
        }
    }
}
