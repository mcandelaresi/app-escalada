package dao.sqlite;

import dao.dao;
import model.ViaGel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Jo gestiono la persistència de ViaGel.
 * IMPORTANT: només persisteixo la relació amb Via.
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

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, v.getIdVia());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Busco una ViaGel per id de Via.
     */
    @Override
    public ViaGel findById(Integer id) {

        String sql = "SELECT * FROM vies_gel WHERE id_via = ?";

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
            e.printStackTrace();
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
            e.printStackTrace();
        }

        return llista;
    }

    @Override
    public void update(ViaGel v) {

        String sql = "UPDATE vies_gel SET id_via = ? WHERE id_via = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, v.getIdVia());
            stmt.setInt(2, v.getIdVia());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Elimino relació Via-Gel.
     */
    @Override
    public void delete(Integer id) {

        String sql = "DELETE FROM vies_gel WHERE id_via = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}