package dao.sqlite;

import dao.dao;
import model.Sector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SectorDAO implements dao<Sector, Integer> {

    private Connection connection;

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void insert(Sector s) {

        String sql = "INSERT INTO sectors (nom, latitud, longitud, aproximacio, popularitat, restriccions, id_escola) VALUES (?, ?, ?, ?, ?, ?, ?)";

        if (connection == null) {
            System.err.println("No hi ha connexió per inserir un sector.");
            return;
        }

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, s.getNom());
            stmt.setDouble(2, s.getLatitud());
            stmt.setDouble(3, s.getLongitud());
            stmt.setString(4, s.getAproximacio());
            stmt.setString(5, s.getPopularitat());
            stmt.setString(6, s.getRestriccions());
            stmt.setInt(7, s.getIdEscola());

            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    s.setIdSector(keys.getInt(1));
                }
            }

        } catch (SQLException e) {
            System.err.println("Error inserint sector: " + e.getMessage());
        }
    }

    @Override
    public Sector findById(Integer id) {

        String sql = "SELECT * FROM sectors WHERE id_sector=?";

        if (connection == null) {
            System.err.println("No hi ha connexió per cercar un sector.");
            return null;
        }

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
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

        } catch (SQLException e) {
            System.err.println("Error cercant sector: " + e.getMessage());
        }

        return null;
    }

    @Override
    public List<Sector> findAll() {

        List<Sector> llista = new ArrayList<>();
        String sql = "SELECT * FROM sectors";

        if (connection == null) {
            System.err.println("No hi ha connexió per llistar els sectors.");
            return llista;
        }

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                llista.add(new Sector(
                        rs.getInt("id_sector"),
                        rs.getString("nom"),
                        rs.getDouble("latitud"),
                        rs.getDouble("longitud"),
                        rs.getString("aproximacio"),
                        rs.getString("popularitat"),
                        rs.getString("restriccions"),
                        rs.getInt("id_escola")
                ));
            }

        } catch (SQLException e) {
            System.err.println("Error llistant sectors: " + e.getMessage());
        }

        return llista;
    }

    public Sector findByNomAndEscola(String nom, int idEscola) {

        String sql = "SELECT * FROM sectors WHERE LOWER(nom) = LOWER(?) AND id_escola = ?";

        if (connection == null) {
            System.err.println("No hi ha connexió per cercar un sector per nom i escola.");
            return null;
        }

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nom);
            stmt.setInt(2, idEscola);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
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
        } catch (SQLException e) {
            System.err.println("Error cercant sector per nom i escola: " + e.getMessage());
        }

        return null;
    }

    @Override
    public void update(Sector s) {

        String sql = "UPDATE sectors SET nom=?, latitud=?, longitud=?, aproximacio=?, popularitat=?, restriccions=?, id_escola=? WHERE id_sector=?";

        if (connection == null) {
            System.err.println("No hi ha connexió per actualitzar un sector.");
            return;
        }

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, s.getNom());
            stmt.setDouble(2, s.getLatitud());
            stmt.setDouble(3, s.getLongitud());
            stmt.setString(4, s.getAproximacio());
            stmt.setString(5, s.getPopularitat());
            stmt.setString(6, s.getRestriccions());
            stmt.setInt(7, s.getIdEscola());
            stmt.setInt(8, s.getIdSector());

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error actualitzant sector: " + e.getMessage());
        }
    }

    @Override
    public void delete(Integer id) {

        String sql = "DELETE FROM sectors WHERE id_sector=?";

        if (connection == null) {
            System.err.println("No hi ha connexió per eliminar un sector.");
            return;
        }

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error eliminant sector: " + e.getMessage());
        }
    }
}