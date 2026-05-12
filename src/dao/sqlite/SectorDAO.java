package dao.sqlite;

import dao.ConnectionDB;
import dao.dao;
import model.Sector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SectorDAO implements dao<Sector, Integer> {

    public SectorDAO() {}

    @Override
    public void insert(Sector s) {
        String sql = "INSERT INTO sectors (nom, latitud, longitud, aproximacio, popularitat, restriccions, id_escola) VALUES (?,?,?,?,?,?,?)";
        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, s.getNom());
            ps.setDouble(2, s.getLatitud());
            ps.setDouble(3, s.getLongitud());
            ps.setString(4, s.getAproximacio());
            ps.setString(5, s.getPopularitat());
            ps.setString(6, s.getRestriccions());
            ps.setInt(7, s.getIdEscola());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) s.setIdSector(rs.getInt(1));
            }
        } catch (SQLException e) {
            System.err.println("Error inserint sector: " + e.getMessage());
        }
    }

    @Override
    public Sector findById(Integer id) {
        String sql = "SELECT * FROM sectors WHERE id_sector = ?";
        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return map(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error cercant sector: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Sector> findAll() {
        List<Sector> llista = new ArrayList<>();
        String sql = "SELECT * FROM sectors ORDER BY nom";
        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) llista.add(map(rs));
        } catch (SQLException e) {
            System.err.println("Error llistant sectors: " + e.getMessage());
        }
        return llista;
    }

    public List<Sector> findByEscola(int idEscola) {
        List<Sector> llista = new ArrayList<>();
        String sql = "SELECT * FROM sectors WHERE id_escola = ? ORDER BY nom";
        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idEscola);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) llista.add(map(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error cercant sectors per escola: " + e.getMessage());
        }
        return llista;
    }

    public Sector findByNomAndEscola(String nom, int idEscola) {
        String sql = "SELECT * FROM sectors WHERE LOWER(nom) = LOWER(?) AND id_escola = ?";
        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nom);
            ps.setInt(2, idEscola);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return map(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error cercant sector per nom i escola: " + e.getMessage());
        }
        return null;
    }

    @Override
    public void update(Sector s) {
        String sql = "UPDATE sectors SET nom=?, latitud=?, longitud=?, aproximacio=?, popularitat=?, restriccions=?, id_escola=? WHERE id_sector=?";
        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, s.getNom());
            ps.setDouble(2, s.getLatitud());
            ps.setDouble(3, s.getLongitud());
            ps.setString(4, s.getAproximacio());
            ps.setString(5, s.getPopularitat());
            ps.setString(6, s.getRestriccions());
            ps.setInt(7, s.getIdEscola());
            ps.setInt(8, s.getIdSector());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error actualitzant sector: " + e.getMessage());
        }
    }

    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM sectors WHERE id_sector = ?";
        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error eliminant sector: " + e.getMessage());
        }
    }

    private Sector map(ResultSet rs) throws SQLException {
        return new Sector(
                rs.getInt("id_sector"),
                rs.getString("nom"),
                rs.getDouble("latitud"),
                rs.getDouble("longitud"),
                rs.getString("aproximacio"),
                rs.getString("popularitat"),
                rs.getString("restriccions"),
                rs.getInt("id_escola")
        );
    }
}
