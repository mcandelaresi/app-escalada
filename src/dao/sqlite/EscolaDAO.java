package dao.sqlite;

import dao.dao;
import model.Escola;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO SQLite de la classe Escola.
 * gestiono totes les operacions CRUD de la taula escoles.
 */
public class EscolaDAO implements dao<Escola, Integer> {

    private Connection connection;

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    /**
     * Inserto una nova escola a la base de dades.
     */
    @Override
    public void insert(Escola escola) {

        String sql = "INSERT INTO escoles (id_escola, nom, aproximacio, popularitat, restriccions) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, escola.getIdEscola());
            stmt.setString(2, escola.getNom());
            stmt.setString(3, escola.getAproximacio());
            stmt.setString(4, escola.getPopularitat());
            stmt.setString(5, escola.getRestriccions());

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error inserint escola: " + e.getMessage());
        }
    }

    /**
     * Busco una escola per ID.
     */
    @Override
    public Escola findById(Integer id) {

        String sql = "SELECT * FROM escoles WHERE id_escola = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Escola(
                        rs.getInt("id_escola"),
                        rs.getString("nom"),
                        rs.getString("aproximacio"),
                        rs.getString("popularitat"),
                        rs.getString("restriccions")
                );
            }

        } catch (SQLException e) {
            System.err.println("Error llegint escola: " + e.getMessage());
        }

        return null;
    }

    /**
     * Llisto totes les escoles.
     */
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

    /**
     * Actualitzo una escola.
     */
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

    /**
     * Elimino una escola.
     */
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