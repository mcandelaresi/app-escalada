package dao.sqlite;

import dao.dao;
import model.Registre;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RegistreDAO implements dao<Registre, Integer> {

    private Connection connection;

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void insert(Registre r) {

        String sql = "INSERT INTO registres (id_registre, id_escalador, id_via, data_ascensio, estil) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, r.getIdRegistre());
            stmt.setInt(2, r.getIdEscalador());
            stmt.setInt(3, r.getIdVia());
            stmt.setString(4, r.getDataAscensio());
            stmt.setString(5, r.getEstil());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Registre findById(Integer id) {

        String sql = "SELECT * FROM registres WHERE id_registre = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Registre(
                        rs.getInt("id_registre"),
                        rs.getInt("id_escalador"),
                        rs.getInt("id_via"),
                        rs.getString("data_ascensio"),
                        rs.getString("estil")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<Registre> findAll() {

        List<Registre> lista = new ArrayList<>();

        String sql = "SELECT * FROM registres";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(new Registre(
                        rs.getInt("id_registre"),
                        rs.getInt("id_escalador"),
                        rs.getInt("id_via"),
                        rs.getString("data_ascensio"),
                        rs.getString("estil")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    @Override
    public void update(Registre r) {

        String sql = "UPDATE registres SET id_escalador=?, id_via=?, data_ascensio=?, estil=? WHERE id_registre=?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, r.getIdEscalador());
            stmt.setInt(2, r.getIdVia());
            stmt.setString(3, r.getDataAscensio());
            stmt.setString(4, r.getEstil());
            stmt.setInt(5, r.getIdRegistre());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Integer id) {

        String sql = "DELETE FROM registres WHERE id_registre = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}