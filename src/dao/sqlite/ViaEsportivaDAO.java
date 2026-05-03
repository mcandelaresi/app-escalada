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

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, v.getIdVia());
            stmt.setInt(2, v.getLlargada());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Busco una via esportiva per ID de via.
     */
    @Override
    public ViaEsportiva findById(Integer id) {

        String sql = "SELECT * FROM vies_esportiva WHERE id_via = ?";

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
            e.printStackTrace();
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
            e.printStackTrace();
        }

        return llista;
    }

    /**
     * Actualitzo la llargada de la via esportiva.
     */
    @Override
    public void update(ViaEsportiva v) {

        String sql = "UPDATE vies_esportiva SET llargada = ? WHERE id_via = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, v.getLlargada());
            stmt.setInt(2, v.getIdVia());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Elimino una via esportiva.
     */
    @Override
    public void delete(Integer id) {

        String sql = "DELETE FROM vies_esportiva WHERE id_via = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, id);

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}