package dao.sqlite;

import dao.ConnectionDB;
import dao.dao;
import model.Tram;
import model.ViaClassica;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ViaClassicaDAO implements dao<ViaClassica, Integer> {

    public ViaClassicaDAO() {}

    @Override
    public void insert(ViaClassica v) {
        String sql = "INSERT INTO vies_classica (id_via, ancoratges_permesos) VALUES (?, ?)";
        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, v.getIdVia());
            stmt.setString(2, v.getAncoratgesPermesos());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error inserint via clàssica: " + e.getMessage());
        }
    }

    @Override
    public ViaClassica findById(Integer id) {
        String sql = """
            SELECT vc.*, v.*
            FROM vies_classica vc
            JOIN vies v ON vc.id_via = v.id_via
            WHERE vc.id_via = ?
        """;
        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    ViaClassica vc = new ViaClassica(
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
                            rs.getString("ancoratges_permesos")
                    );
                    TramDAO tramDAO = new TramDAO();
                    List<Tram> trams = tramDAO.findByVia(id);
                    vc.establirTrams(trams);
                    return vc;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error cercant via clàssica: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<ViaClassica> findAll() {
        List<ViaClassica> lista = new ArrayList<>();
        String sql = """
            SELECT vc.*, v.*
            FROM vies_classica vc
            JOIN vies v ON vc.id_via = v.id_via
        """;
        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ViaClassica vc = new ViaClassica(
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
                        rs.getString("ancoratges_permesos")
                );
                TramDAO tramDAO = new TramDAO();
                vc.establirTrams(tramDAO.findByVia(rs.getInt("id_via")));
                lista.add(vc);
            }
        } catch (SQLException e) {
            System.err.println("Error llistant vies clàssiques: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public void update(ViaClassica v) {
        String sql = "UPDATE vies_classica SET ancoratges_permesos=? WHERE id_via=?";
        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, v.getAncoratgesPermesos());
            stmt.setInt(2, v.getIdVia());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error actualitzant via clàssica: " + e.getMessage());
        }
    }

    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM vies_classica WHERE id_via=?";
        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error eliminant via clàssica: " + e.getMessage());
        }
    }
}
