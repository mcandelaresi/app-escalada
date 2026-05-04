package dao.sqlite;

import dao.dao;
import model.Poblacio;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO SQLite de la classe Poblacio.
 *  gestiona totes les operacions CRUD de la taula poblacions.
 */
public class PoblacioDAO implements dao<Poblacio, Integer> {

    private Connection connection;


    /**
     * Assigno la connexió des de fora.
     */
    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    /**
     * Inserto una població.
     */
    @Override
    public void insert(Poblacio poblacio) {

        String sql = "INSERT INTO poblacions (nom) VALUES (?)";

        if (connection == null) {
            System.err.println("No hi ha connexió per inserir una població.");
            return;
        }

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, poblacio.getNom());

            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    poblacio.setIdPoblacio(keys.getInt(1));
                }
            }

        } catch (SQLException e) {
            System.err.println("Error inserint població: " + e.getMessage());
        }
    }

    /**
     * Busco una població per ID.
     */
    @Override
    public Poblacio findById(Integer id) {

        String sql = "SELECT * FROM poblacions WHERE id_poblacio = ?";

        if (connection == null) {
            System.err.println("No hi ha connexió per cercar una població.");
            return null;
        }

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Poblacio(
                        rs.getInt("id_poblacio"),
                        rs.getString("nom")
                );
            }

        } catch (SQLException e) {
            System.err.println("Error llegint població: " + e.getMessage());
        }

        return null;
    }

    /**
     * Retorno totes les poblacions.
     */
    @Override
    public List<Poblacio> findAll() {

        List<Poblacio> llista = new ArrayList<>();
        String sql = "SELECT * FROM poblacions";

        if (connection == null) {
            System.err.println("No hi ha connexió per llistar les poblacions.");
            return llista;
        }

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                llista.add(new Poblacio(
                        rs.getInt("id_poblacio"),
                        rs.getString("nom")
                ));
            }

        } catch (SQLException e) {
            System.err.println("Error llistant poblacions: " + e.getMessage());
        }

        return llista;
    }

    /**
     * Actualitzo una població.
     */
    @Override
    public void update(Poblacio poblacio) {

        String sql = "UPDATE poblacions SET nom=? WHERE id_poblacio=?";

        if (connection == null) {
            System.err.println("No hi ha connexió per actualitzar una població.");
            return;
        }

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, poblacio.getNom());
            stmt.setInt(2, poblacio.getIdPoblacio());

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error actualitzant població: " + e.getMessage());
        }
    }

    /**
     * Elimino una població.
     */
    @Override
    public void delete(Integer id) {

        String sql = "DELETE FROM poblacions WHERE id_poblacio=?";

        if (connection == null) {
            System.err.println("No hi ha connexió per eliminar una població.");
            return;
        }

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error eliminant població: " + e.getMessage());
        }
    }
}