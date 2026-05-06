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

        String sql = "INSERT INTO registres (id_escalador, id_via, data_ascensio, estil) VALUES (?, ?, ?, ?)";

        if (connection == null) {
            System.err.println("No hi ha connexió per inserir un registre.");
            return;
        }

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, r.getIdEscalador());
            stmt.setInt(2, r.getIdVia());
            stmt.setString(3, r.getDataAscensio());
            stmt.setString(4, r.getEstil());

            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    r.setIdRegistre(keys.getInt(1));
                }
            }

        } catch (SQLException e) {
            System.err.println("Error inserint registre: " + e.getMessage());
        }
    }

    @Override
    public Registre findById(Integer id) {

        String sql = "SELECT * FROM registres WHERE id_registre = ?";

        if (connection == null) {
            System.err.println("No hi ha connexió per cercar un registre.");
            return null;
        }

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
            System.err.println("Error cercant registre: " + e.getMessage());
        }

        return null;
    }

    @Override
    public List<Registre> findAll() {

        List<Registre> lista = new ArrayList<>();

        String sql = "SELECT * FROM registres";

        if (connection == null) {
            System.err.println("No hi ha connexió per llistar els registres.");
            return lista;
        }

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
            System.err.println("Error llistant registres: " + e.getMessage());
        }

        return lista;
    }

    @Override
    public void update(Registre r) {

        String sql = "UPDATE registres SET id_escalador=?, id_via=?, data_ascensio=?, estil=? WHERE id_registre=?";

        if (connection == null) {
            System.err.println("No hi ha connexió per actualitzar un registre.");
            return;
        }

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, r.getIdEscalador());
            stmt.setInt(2, r.getIdVia());
            stmt.setString(3, r.getDataAscensio());
            stmt.setString(4, r.getEstil());
            stmt.setInt(5, r.getIdRegistre());

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error actualitzant registre: " + e.getMessage());
        }
    }

    @Override
    public void delete(Integer id) {

        String sql = "DELETE FROM registres WHERE id_registre = ?";

        if (connection == null) {
            System.err.println("No hi ha connexió per eliminar un registre.");
            return;
        }

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error eliminant registre: " + e.getMessage());
        }
    }

    /**
     * Buscar tots els registres d'un escalador específic, ordenats per data d'ascensió de més recent a més antiga.
     */
    public List<Registre> findByEscalador(int idEscalador) {

        List<Registre> llista = new ArrayList<>();
        String sql = "SELECT * FROM registres WHERE id_escalador = ? ORDER BY data_ascensio DESC";

        if (connection == null) {
            System.err.println("No hi ha connexió per cercar registres per escalador.");
            return llista;
        }

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, idEscalador);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    llista.add(new Registre(
                            rs.getInt("id_registre"),
                            rs.getInt("id_escalador"),
                            rs.getInt("id_via"),
                            rs.getString("data_ascensio"),
                            rs.getString("estil")
                    ));
                }
            }

        } catch (SQLException e) {
            System.err.println("Error cercant registres per escalador: " + e.getMessage());
        }

        return llista;
    }

    /**
     * Busca tots els registres d'una via específica, ordenats per data d'ascensió de més recent a més antiga.
     */
    public List<Registre> findByVia(int idVia) {

        List<Registre> llista = new ArrayList<>();
        String sql = "SELECT * FROM registres WHERE id_via = ? ORDER BY data_ascensio DESC";

        if (connection == null) {
            System.err.println("No hi ha connexió per cercar registres per via.");
            return llista;
        }

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, idVia);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    llista.add(new Registre(
                            rs.getInt("id_registre"),
                            rs.getInt("id_escalador"),
                            rs.getInt("id_via"),
                            rs.getString("data_ascensio"),
                            rs.getString("estil")
                    ));
                }
            }

        } catch (SQLException e) {
            System.err.println("Error cercant registres per via: " + e.getMessage());
        }

        return llista;
    }
}