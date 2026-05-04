package dao.sqlite;

import dao.dao;
import model.ViaClassica;

import java.sql.*;

public class ViaClassicaDAO implements dao<ViaClassica, Integer> {

    private Connection connection;

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void insert(ViaClassica v) {

        String sql = "INSERT INTO vies_classica (ancoratges_permesos, id_via) VALUES (?, ?)";

        if (connection == null) {
            System.err.println("No hi ha connexió per inserir una via clàssica.");
            return;
        }

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, v.getAncoratgesPermesos());
            stmt.setInt(2, v.getIdVia());

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error inserint via clàssica: " + e.getMessage());
        }
    }

    @Override
    public ViaClassica findById(Integer id) {

        String sql = "SELECT * FROM vies_classica WHERE id_via = ?";

        if (connection == null) {
            System.err.println("No hi ha connexió per cercar una via clàssica.");
            return null;
        }

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new ViaClassica(
                        rs.getInt("id_via"),
                        "", "", "", "", "",
                        "", "",
                        "", 0, 0, 0,
                        "", rs.getString("ancoratges_permesos")
                );
            }

        } catch (SQLException e) {
            System.err.println("Error cercant via clàssica: " + e.getMessage());
        }

        return null;
    }

    @Override
    public java.util.List<ViaClassica> findAll() {

        java.util.List<ViaClassica> lista = new java.util.ArrayList<>();

        String sql = "SELECT * FROM vies_classica";

        if (connection == null) {
            System.err.println("No hi ha connexió per llistar les vies clàssiques.");
            return lista;
        }

        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {

                lista.add(new ViaClassica(
                        rs.getInt("id_via"),
                        "", "", "", "", "",
                        "", "",
                        "", 0, 0, 0,
                        "", rs.getString("ancoratges_permesos")
                ));
            }

        } catch (SQLException e) {
            System.err.println("Error llistant vies clàssiques: " + e.getMessage());
        }

        return lista;
    }

    @Override
    public void update(ViaClassica v) {

        String sql = "UPDATE vies_classica SET ancoratges_permesos=? WHERE id_via=?";

        if (connection == null) {
            System.err.println("No hi ha connexió per actualitzar una via clàssica.");
            return;
        }

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, v.getAncoratgesPermesos());
            stmt.setInt(2, v.getIdVia());

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error actualitzant via clàssica: " + e.getMessage());
        }
    }

    @Override
    public void delete(Integer id) {

        String sql = "DELETE FROM vies_classica WHERE id_via=?";

        if (connection == null) {
            System.err.println("No hi ha connexió per eliminar una via clàssica.");
            return;
        }

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error eliminant via clàssica: " + e.getMessage());
        }
    }
}