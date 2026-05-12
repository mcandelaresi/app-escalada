package dao.sqlite;

import dao.ConnectionDB;
import dao.dao;
import model.ViaGel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ViaGelDAO implements dao<ViaGel, Integer> {

    public ViaGelDAO() {}

    @Override
    public void insert(ViaGel v) {
        String sql = "INSERT INTO vies_gel (id_via) VALUES (?)";
        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, v.getIdVia());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error inserint via de gel: " + e.getMessage());
        }
    }

    @Override
    public ViaGel findById(Integer id) {
        String sql = """
            SELECT vg.*, v.*
            FROM vies_gel vg
            JOIN vies v ON vg.id_via = v.id_via
            WHERE vg.id_via = ?
        """;
        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    ViaGel vg = new ViaGel(
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
                            rs.getString("restriccions")
                    );
                    TramDAO tramDAO = new TramDAO();
                    vg.establirTrams(tramDAO.findByVia(id));
                    return vg;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error cercant via de gel: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<ViaGel> findAll() {
        List<ViaGel> llista = new ArrayList<>();
        String sql = """
            SELECT vg.*, v.*
            FROM vies_gel vg
            JOIN vies v ON vg.id_via = v.id_via
        """;
        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                ViaGel vg = new ViaGel(
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
                        rs.getString("restriccions")
                );
                TramDAO tramDAO = new TramDAO();
                vg.establirTrams(tramDAO.findByVia(rs.getInt("id_via")));
                llista.add(vg);
            }
        } catch (SQLException e) {
            System.err.println("Error llistant vies de gel: " + e.getMessage());
        }
        return llista;
    }

    @Override
    public void update(ViaGel v) {
        // vies_gel no té columnes pròpies, l'actualització és a la taula vies (via ViaDAO)
    }

    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM vies_gel WHERE id_via = ?";
        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error eliminant via de gel: " + e.getMessage());
        }
    }
}
