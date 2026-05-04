package dao.sqlite;

import dao.dao;
import model.ViaGel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Jo gestiono la persistència de ViaGel.
 *
 */
public class ViaGelDAO implements dao<ViaGel, Integer> {

    private Connection connection;

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    /**
     * Inserto una via de gel.
     * Només guardo id_via perquè la resta es calcula.
     */
    @Override
    public void insert(ViaGel v) {

        String sql = "INSERT INTO vies_gel (id_via) VALUES (?)";

        if (connection == null) {
            System.err.println("No hi ha connexió per inserir una via de gel.");
            return;
        }

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, v.getIdVia());

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error inserint via de gel: " + e.getMessage());
        }
    }

    /**
     * Busco una ViaGel per id de Via.
     */
    @Override
    public ViaGel findById(Integer id) {

        String sql = "SELECT * FROM vies_gel WHERE id_via = ?";

        if (connection == null) {
            System.err.println("No hi ha connexió per cercar una via de gel.");
            return null;
        }

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new ViaGel(
                        rs.getInt("id_via"),
                        null, null, null,
                        null, null,
                        null, null, null,
                        0, 0, 0,
                        null
                );
            }

        } catch (SQLException e) {
            System.err.println("Error cercant via de gel: " + e.getMessage());
        }

        return null;
    }

    /**
     * Llisto totes les vies de gel.
     */
    @Override
    public List<ViaGel> findAll() {

        List<ViaGel> llista = new ArrayList<>();
        String sql = "SELECT * FROM vies_gel";

        if (connection == null) {
            System.err.println("No hi ha connexió per llistar les vies de gel.");
            return llista;
        }

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {

                llista.add(new ViaGel(
                        rs.getInt("id_via"),
                        null, null, null,
                        null, null,
                        null, null, null,
                        0, 0, 0,
                        null
                ));
            }

        } catch (SQLException e) {
            System.err.println("Error llistant vies de gel: " + e.getMessage());
        }

        return llista;
    }

    @Override
    public void update(ViaGel v) {

        String sql = "UPDATE vies_gel SET id_via = ? WHERE id_via = ?";

        if (connection == null) {
            System.err.println("No hi ha connexió per actualitzar una via de gel.");
            return;
        }

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, v.getIdVia());
            stmt.setInt(2, v.getIdVia());

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error actualitzant via de gel: " + e.getMessage());
        }
    }

    /**
     * Elimino relació Via-Gel.
     */
    @Override
    public void delete(Integer id) {

        String sql = "DELETE FROM vies_gel WHERE id_via = ?";

        if (connection == null) {
            System.err.println("No hi ha connexió per eliminar una via de gel.");
            return;
        }

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error eliminant via de gel: " + e.getMessage());
        }
    }
}