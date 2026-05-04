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

        String sql = "INSERT INTO sectors (id_sector, nom, latitud, longitud, aproximacio, popularitat, restriccions, id_escola) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, s.getIdSector());
            stmt.setString(2, s.getNom());
            stmt.setDouble(3, s.getLatitud());
            stmt.setDouble(4, s.getLongitud());
            stmt.setString(5, s.getAproximacio());
            stmt.setString(6, s.getPopularitat());
            stmt.setString(7, s.getRestriccions());
            stmt.setInt(8, s.getIdEscola());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Sector findById(Integer id) {

        String sql = "SELECT * FROM sectors WHERE id_sector=?";

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
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<Sector> findAll() {

        List<Sector> llista = new ArrayList<>();
        String sql = "SELECT * FROM sectors";

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
            e.printStackTrace();
        }

        return llista;
    }

    @Override
    public void update(Sector s) {

        String sql = "UPDATE sectors SET nom=?, latitud=?, longitud=?, aproximacio=?, popularitat=?, restriccions=?, id_escola=? WHERE id_sector=?";

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
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Integer id) {

        String sql = "DELETE FROM sectors WHERE id_sector=?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}