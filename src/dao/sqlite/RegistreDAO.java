package dao.sqlite;

import dao.dao;
import dao.ConnectionDB;
import model.Escalador;
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
     * Busca un escalador por alias.
     */
    public Escalador findByAlias(String alias) {

        String sql = "SELECT * FROM escalador WHERE LOWER(alias) = LOWER(?)";

        if (connection == null) {
            System.err.println("No hi ha connexió.");
            return null;
        }

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, alias);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Escalador(
                            rs.getInt("id_escalador"),
                            rs.getString("nom"),
                            rs.getString("alias"),
                            rs.getInt("edat"),
                            rs.getString("nivell_max"),
                            rs.getString("estil_preferit"),
                            rs.getInt("id_via_max")
                    );
                }
            }

        } catch (SQLException e) {
            System.err.println("Error cercant escalador: " + e.getMessage());
        }

        return null;
    }

    /**
     * Busca escaladors per nivell màxim.
     */
    public List<Escalador> findByNivell(String nivell) {

        List<Escalador> llista = new ArrayList<>();
        String sql = "SELECT * FROM escalador WHERE LOWER(nivell_max) = LOWER(?)";


        try {
            Connection conn = ConnectionDB.getConnection();
            if (conn == null) {
                System.err.println("No s'ha pogut obtenir la connexió.");
                return llista;
            }

            try (PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setString(1, nivell);

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        llista.add(new Escalador(
                                rs.getInt("id_escalador"),
                                rs.getString("nom"),
                                rs.getString("alias"),
                                rs.getInt("edat"),
                                rs.getString("nivell_max"),
                                rs.getString("estil_preferit"),
                                rs.getInt("id_via_max")
                        ));
                    }
                }
            }

        } catch (SQLException e) {
            System.err.println("Error cercant escaladors per nivell: " + e.getMessage());
        }

        return llista;
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