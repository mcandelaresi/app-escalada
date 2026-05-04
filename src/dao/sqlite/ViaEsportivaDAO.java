package dao.sqlite;

import dao.dao;
import model.ViaEsportiva;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Jo gestiono la persistència de ViaEsportiva a SQLite.
 */
public class ViaEsportivaDAO implements dao<ViaEsportiva, Integer> {

    private Connection connection;

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    /**
     * Insereixo una via esportiva a la base de dades.
     */
    @Override
    public void insert(ViaEsportiva v) {

        String sql = "INSERT INTO vies_esportiva " +
                "(id_via, llargada) VALUES (?, ?)";

        if (connection == null) {
            System.err.println("No hi ha connexió per inserir una via esportiva.");
            return;
        }

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, v.getIdVia());
            stmt.setInt(2, v.getLlargada());

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error inserint via esportiva: " + e.getMessage());
        }
    }

    /**
     * Busco una via esportiva per ID de via.
     */
    @Override
    public ViaEsportiva findById(Integer id) {

        String sql = "SELECT * FROM vies_esportiva WHERE id_via = ?";

        if (connection == null) {
            System.err.println("No hi ha connexió per cercar una via esportiva.");
            return null;
        }

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new ViaEsportiva(
                        rs.getInt("id_via"),
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        0,
                        0,
                        0,
                        null,
                        rs.getInt("llargada")
                );
            }

        } catch (SQLException e) {
            System.err.println("Error cercant via esportiva: " + e.getMessage());
        }

        return null;
    }

    /**
     * Llisto totes les vies esportives.
     */
    @Override
    public List<ViaEsportiva> findAll() {

        List<ViaEsportiva> llista = new ArrayList<>();

        String sql = "SELECT * FROM vies_esportiva";

        if (connection == null) {
            System.err.println("No hi ha connexió per llistar les vies esportives.");
            return llista;
        }

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {

                llista.add(new ViaEsportiva(
                        rs.getInt("id_via"),
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        0,
                        0,
                        0,
                        null,
                        rs.getInt("llargada")
                ));
            }

        } catch (SQLException e) {
            System.err.println("Error llistant vies esportives: " + e.getMessage());
        }

        return llista;
    }

    /**
     * Actualitzo la llargada de la via esportiva.
     */
    @Override
    public void update(ViaEsportiva v) {

        String sql = "UPDATE vies_esportiva SET llargada = ? WHERE id_via = ?";

        if (connection == null) {
            System.err.println("No hi ha connexió per actualitzar una via esportiva.");
            return;
        }

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, v.getLlargada());
            stmt.setInt(2, v.getIdVia());

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error actualitzant via esportiva: " + e.getMessage());
        }
    }

    /**
     * Elimino una via esportiva.
     */
    @Override
    public void delete(Integer id) {

        String sql = "DELETE FROM vies_esportiva WHERE id_via = ?";

        if (connection == null) {
            System.err.println("No hi ha connexió per eliminar una via esportiva.");
            return;
        }

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, id);

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error eliminant via esportiva: " + e.getMessage());
        }
    }
}