package dao.sqlite;

import dao.ConnectionDB;
import dao.dao;
import model.Tram;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TramDAO implements dao<Tram, Integer> {

    public TramDAO() {}

    @Override
    public void insert(Tram t) {
        String sql = "INSERT INTO trams (num_llarg, llarg, grau_dificultat, id_via) VALUES (?,?,?,?)";
        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, t.getNumLlarg());
            ps.setInt(2, t.getLlarg());
            ps.setString(3, t.getGrauDificultat());
            ps.setInt(4, t.getIdVia());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) t.setIdTram(rs.getInt(1));
            }
        } catch (SQLException e) {
            System.err.println("Error inserint tram: " + e.getMessage());
        }
    }

    @Override
    public Tram findById(Integer id) {
        String sql = "SELECT * FROM trams WHERE id_tram = ?";
        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return map(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error cercant tram: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Tram> findAll() {
        List<Tram> llista = new ArrayList<>();
        String sql = "SELECT * FROM trams ORDER BY id_via, num_llarg";
        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) llista.add(map(rs));
        } catch (SQLException e) {
            System.err.println("Error llistant trams: " + e.getMessage());
        }
        return llista;
    }

    public List<Tram> findByVia(int idVia) {
        List<Tram> llista = new ArrayList<>();
        String sql = "SELECT * FROM trams WHERE id_via = ? ORDER BY num_llarg";
        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idVia);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) llista.add(map(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error cercant trams per via: " + e.getMessage());
        }
        return llista;
    }

    @Override
    public void update(Tram t) {
        String sql = "UPDATE trams SET num_llarg=?, llarg=?, grau_dificultat=?, id_via=? WHERE id_tram=?";
        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, t.getNumLlarg());
            ps.setInt(2, t.getLlarg());
            ps.setString(3, t.getGrauDificultat());
            ps.setInt(4, t.getIdVia());
            ps.setInt(5, t.getIdTram());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error actualitzant tram: " + e.getMessage());
        }
    }

    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM trams WHERE id_tram = ?";
        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error eliminant tram: " + e.getMessage());
        }
    }

    public void deleteByVia(int idVia) {
        String sql = "DELETE FROM trams WHERE id_via = ?";
        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idVia);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error eliminant trams per via: " + e.getMessage());
        }
    }

    private Tram map(ResultSet rs) throws SQLException {
        int llarg = rs.getInt("llarg");
        if (llarg < 15) llarg = 15;
        if (llarg > 30) llarg = 30;
        return new Tram(
                rs.getInt("id_tram"),
                rs.getInt("num_llarg"),
                llarg,
                rs.getString("grau_dificultat"),
                rs.getInt("id_via")
        );
    }
}
