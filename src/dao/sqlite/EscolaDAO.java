package dao.sqlite;

import dao.dao;
import model.Escola;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EscolaDAO implements dao<Escola, Integer> {

    private Connection connection;

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void insert(Escola escola) {

        String sql = "INSERT INTO escoles (nom, aproximacio, popularitat, restriccions) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, escola.getNom());
            stmt.setString(2, escola.getAproximacio());
            stmt.setString(3, escola.getPopularitat());
            stmt.setString(4, escola.getRestriccions());

            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    escola.setIdEscola(keys.getInt(1));
                }
            }

        } catch (SQLException e) {
            System.err.println("Error inserint escola: " + e.getMessage());
        }
    }

    @Override
    public Escola findById(Integer id) {

        String sql = "SELECT * FROM escoles WHERE id_escola = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Escola(
                            rs.getInt("id_escola"),
                            rs.getString("nom"),
                            rs.getString("aproximacio"),
                            rs.getString("popularitat"),
                            rs.getString("restriccions")
                    );
                }
            }

        } catch (SQLException e) {
            System.err.println("Error llegint escola: " + e.getMessage());
        }

        return null;
    }

    @Override
    public List<Escola> findAll() {

        List<Escola> llista = new ArrayList<>();

        String sql = "SELECT * FROM escoles";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                llista.add(new Escola(
                        rs.getInt("id_escola"),
                        rs.getString("nom"),
                        rs.getString("aproximacio"),
                        rs.getString("popularitat"),
                        rs.getString("restriccions")
                ));
            }

        } catch (SQLException e) {
            System.err.println("Error llistant escoles: " + e.getMessage());
        }

        return llista;
    }

    public Escola findByNom(String nom) {

        String sql = "SELECT * FROM escoles WHERE LOWER(nom) = LOWER(?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, nom);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Escola(
                            rs.getInt("id_escola"),
                            rs.getString("nom"),
                            rs.getString("aproximacio"),
                            rs.getString("popularitat"),
                            rs.getString("restriccions")
                    );
                }
            }

        } catch (SQLException e) {
            System.err.println("Error cercant escola: " + e.getMessage());
        }

        return null;
    }

    @Override
    public void update(Escola escola) {

        String sql = "UPDATE escoles SET nom=?, aproximacio=?, popularitat=?, restriccions=? WHERE id_escola=?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, escola.getNom());
            stmt.setString(2, escola.getAproximacio());
            stmt.setString(3, escola.getPopularitat());
            stmt.setString(4, escola.getRestriccions());
            stmt.setInt(5, escola.getIdEscola());

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error actualitzant escola: " + e.getMessage());
        }
    }

    @Override
    public void delete(Integer id) {

        String sql = "DELETE FROM escoles WHERE id_escola = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error eliminant escola: " + e.getMessage());
        }
    }
}