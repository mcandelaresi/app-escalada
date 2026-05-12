package dao.sqlite;

import dao.ConnectionDB;
import dao.dao;
import model.Registre;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RegistreDAO implements dao<Registre, Integer> {

    // Constructor sense paràmetres (usa ConnectionDB estàtic)
    public RegistreDAO() {}

    @Override
    public void insert(Registre r) {
        String sql = "INSERT INTO registres (id_escalador, id_via, data_ascensio, estil) VALUES (?,?,?,?)";
        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, r.getIdEscalador());
            ps.setInt(2, r.getIdVia());
            ps.setString(3, r.getDataAscensio());
            ps.setString(4, r.getEstil());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) r.setIdRegistre(rs.getInt(1));
            }
        } catch (SQLException e) {
            System.err.println("Error inserint registre: " + e.getMessage());
        }
    }

    @Override
    public Registre findById(Integer id) {
        String sql = "SELECT * FROM registres WHERE id_registre = ?";
        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return map(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error cercant registre: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Registre> findAll() {
        List<Registre> llista = new ArrayList<>();
        String sql = "SELECT * FROM registres ORDER BY data_ascensio DESC";
        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) llista.add(map(rs));
        } catch (SQLException e) {
            System.err.println("Error llistant registres: " + e.getMessage());
        }
        return llista;
    }

    public List<Registre> findByEscalador(int idEscalador) {
        List<Registre> llista = new ArrayList<>();
        String sql = "SELECT * FROM registres WHERE id_escalador = ? ORDER BY data_ascensio DESC";
        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idEscalador);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) llista.add(map(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error cercant registres per escalador: " + e.getMessage());
        }
        return llista;
    }

    public List<Registre> findByVia(int idVia) {
        List<Registre> llista = new ArrayList<>();
        String sql = "SELECT * FROM registres WHERE id_via = ? ORDER BY data_ascensio DESC";
        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idVia);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) llista.add(map(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error cercant registres per via: " + e.getMessage());
        }
        return llista;
    }

    @Override
    public void update(Registre r) {
        String sql = "UPDATE registres SET id_escalador=?, id_via=?, data_ascensio=?, estil=? WHERE id_registre=?";
        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, r.getIdEscalador());
            ps.setInt(2, r.getIdVia());
            ps.setString(3, r.getDataAscensio());
            ps.setString(4, r.getEstil());
            ps.setInt(5, r.getIdRegistre());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error actualitzant registre: " + e.getMessage());
        }
    }

    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM registres WHERE id_registre = ?";
        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error eliminant registre: " + e.getMessage());
        }
    }

    private Registre map(ResultSet rs) throws SQLException {
        return new Registre(
                rs.getInt("id_registre"),
                rs.getInt("id_escalador"),
                rs.getInt("id_via"),
                rs.getString("data_ascensio"),
                rs.getString("estil")
        );
    }
}
