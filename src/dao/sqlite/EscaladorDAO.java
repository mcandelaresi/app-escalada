package dao.sqlite;

import dao.ConnectionDB;
import dao.dao;
import model.Escalador;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EscaladorDAO implements dao<Escalador, Integer> {

    // Constructor sense paràmetres (usa ConnectionDB estàtic)
    public EscaladorDAO() {}

    @Override
    public void insert(Escalador e) {
        String sql = "INSERT INTO escalador (nom, alias, edat, nivell_max, estil_preferit, id_via_max) VALUES (?,?,?,?,?,?)";
        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, e.getNom());
            ps.setString(2, e.getAlias());
            ps.setInt(3, e.getEdat());
            ps.setString(4, e.getNivellMax());
            ps.setString(5, e.getEstilPreferit());
            if (e.getIdViaMax() > 0) ps.setInt(6, e.getIdViaMax());
            else ps.setNull(6, Types.INTEGER);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) e.setIdEscalador(rs.getInt(1));
            }
        } catch (SQLException ex) {
            System.err.println("Error inserint escalador: " + ex.getMessage());
        }
    }

    @Override
    public Escalador findById(Integer id) {
        String sql = "SELECT * FROM escalador WHERE id_escalador = ?";
        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return map(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error cercant escalador: " + e.getMessage());
        }
        return null;
    }

    public Escalador findByAlias(String alias) {
        String sql = "SELECT * FROM escalador WHERE alias = ?";
        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, alias);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return map(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error cercant escalador per alias: " + e.getMessage());
        }
        return null;
    }

    public Escalador findByNom(String nom) {
        String sql = "SELECT * FROM escalador WHERE LOWER(nom) = LOWER(?)";
        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nom);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return map(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error cercant escalador per nom: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Escalador> findAll() {
        List<Escalador> llista = new ArrayList<>();
        String sql = "SELECT * FROM escalador ORDER BY nom";
        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) llista.add(map(rs));
        } catch (SQLException e) {
            System.err.println("Error llistant escaladors: " + e.getMessage());
        }
        return llista;
    }

    @Override
    public void update(Escalador e) {
        String sql = "UPDATE escalador SET nom=?, alias=?, edat=?, nivell_max=?, estil_preferit=?, id_via_max=? WHERE id_escalador=?";
        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, e.getNom());
            ps.setString(2, e.getAlias());
            ps.setInt(3, e.getEdat());
            ps.setString(4, e.getNivellMax());
            ps.setString(5, e.getEstilPreferit());
            if (e.getIdViaMax() > 0) ps.setInt(6, e.getIdViaMax());
            else ps.setNull(6, Types.INTEGER);
            ps.setInt(7, e.getIdEscalador());
            ps.executeUpdate();
        } catch (SQLException ex) {
            System.err.println("Error actualitzant escalador: " + ex.getMessage());
        }
    }

    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM escalador WHERE id_escalador = ?";
        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error eliminant escalador: " + e.getMessage());
        }
    }

    private Escalador map(ResultSet rs) throws SQLException {
        int idViaMax = rs.getInt("id_via_max");
        if (rs.wasNull()) idViaMax = 0;
        return new Escalador(
                rs.getInt("id_escalador"),
                rs.getString("nom"),
                rs.getString("alias"),
                rs.getInt("edat"),
                rs.getString("nivell_max"),
                rs.getString("estil_preferit"),
                idViaMax
        );
    }
}
