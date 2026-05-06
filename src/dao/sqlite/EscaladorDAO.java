package dao.sqlite;

import dao.ConnectionDB;
import dao.dao;
import model.Escalador;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * DAO de l'entitat Escalador.
 * En aquesta classe gestiono totes les operacions CRUD
 * relacionades amb la taula "escalador" de la base de dades.
 */
public class EscaladorDAO implements dao<Escalador, Integer> {

    private static final Logger LOGGER = Logger.getLogger(EscaladorDAO.class.getName());

    /**
     * Insereixo un nou escalador a la base de dades.
     */
    @Override
    public void insert(Escalador e) {

        // Defineixo la consulta SQL amb placeholders
        String sql = "INSERT INTO escalador (nom, alias, edat, nivell_max, estil_preferit, id_via_max) VALUES (?, ?, ?, ?, ?, ?)";

        // Obro connexió i preparo la consulta
        try (Connection conn = ConnectionDB.getConnection()) {
            if (conn == null) {
                LOGGER.severe("No s'ha pogut obtenir la connexió per inserir un escalador.");
                return;
            }

            try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                // Assigno els valors de l'objecte Escalador als paràmetres SQL
                ps.setString(1, e.getNom());
                ps.setString(2, e.getAlias());
                ps.setInt(3, e.getEdat());
                ps.setString(4, e.getNivellMax());
                ps.setString(5, e.getEstilPreferit());
                ps.setInt(6, e.getIdViaMax());

                // Executo la inserció
                ps.executeUpdate();

                // Recupero l'ID generat automàticament
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        e.setIdEscalador(rs.getInt(1));
                    }
                }
            }

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error inserint l'escalador", ex);
        }
    }

    /**
     * Busco un escalador pel seu ID.
     */
    @Override
    public Escalador findById(Integer id) {

        String sql = "SELECT * FROM escalador WHERE id_escalador=?";

        try (Connection conn = ConnectionDB.getConnection()) {
            if (conn == null) {
                LOGGER.severe("No s'ha pogut obtenir la connexió per cercar un escalador per ID.");
                return null;
            }

            try (PreparedStatement ps = conn.prepareStatement(sql)) {

                // Assigno l'ID a la consulta
                ps.setInt(1, id);

                // Executo la consulta
                ResultSet rs = ps.executeQuery();

                // Si trobo resultat, el transformo en objecte
                if (rs.next()) {
                    return map(rs);
                }
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error cercant l'escalador per ID", e);
        }

        // Si no existeix, retorno null
        return null;
    }

    /**
     * Retorno tots els escaladors de la base de dades.
     */
    @Override
    public List<Escalador> findAll() {

        List<Escalador> llista = new ArrayList<>();
        String sql = "SELECT * FROM escalador";

        try (Connection conn = ConnectionDB.getConnection()) {
            if (conn == null) {
                LOGGER.severe("No s'ha pogut obtenir la connexió per llistar els escaladors.");
                return llista;
            }

            try (Statement st = conn.createStatement();
                 ResultSet rs = st.executeQuery(sql)) {

                // Recorro totes les files del resultat
                while (rs.next()) {
                    // Converteixo cada fila en un objecte Escalador
                    llista.add(map(rs));
                }
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error llistant els escaladors", e);
        }

        return llista;
    }

    /**
     * Actualitzo un escalador existent.
     */
    @Override
    public void update(Escalador e) {

        String sql = "UPDATE escalador SET nom=?, alias=?, edat=?, nivell_max=?, estil_preferit=?, id_via_max=? WHERE id_escalador=?";

        try (Connection conn = ConnectionDB.getConnection()) {
            if (conn == null) {
                LOGGER.severe("No s'ha pogut obtenir la connexió per actualitzar un escalador.");
                return;
            }

            try (PreparedStatement ps = conn.prepareStatement(sql)) {

                // Assigno els nous valors
                ps.setString(1, e.getNom());
                ps.setString(2, e.getAlias());
                ps.setInt(3, e.getEdat());
                ps.setString(4, e.getNivellMax());
                ps.setString(5, e.getEstilPreferit());
                ps.setInt(6, e.getIdViaMax());
                ps.setInt(7, e.getIdEscalador());

                // Executo l'actualització
                ps.executeUpdate();
            }

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error actualitzant l'escalador", ex);
        }
    }

    /**
     * Elimino un escalador pel seu ID.
     */
    @Override
    public void delete(Integer id) {

        String sql = "DELETE FROM escalador WHERE id_escalador=?";

        try (Connection conn = ConnectionDB.getConnection()) {
            if (conn == null) {
                LOGGER.severe("No s'ha pogut obtenir la connexió per eliminar un escalador.");
                return;
            }

            try (PreparedStatement ps = conn.prepareStatement(sql)) {

                // Assigno l'ID a eliminar
                ps.setInt(1, id);

                // Executo l'eliminació
                ps.executeUpdate();
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error eliminant l'escalador", e);
        }
    }


    /**
     * Busca un escalador por alias.
     */
    public Escalador findByAlias(String alias) {

        String sql = "SELECT * FROM escalador WHERE LOWER(alias) = LOWER(?)";

        Connection conn = null;
        try {
            conn = ConnectionDB.getConnection();
            if (conn == null) {
                System.err.println("No s'ha pogut obtenir la connexió.");
                return null;
            }

            try (PreparedStatement ps = conn.prepareStatement(sql)) {

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
            }

        } catch (SQLException e) {
            System.err.println("Error cercant escalador per alias: " + e.getMessage());
        }

        return null;
    }

    /**
     * Busca escaladors per nivell màxim.
     */
    public List<Escalador> findByNivell(String nivell) {

        List<Escalador> llista = new ArrayList<>();
        String sql = "SELECT * FROM escalador WHERE LOWER(nivell_max) = LOWER(?)";

        Connection conn = null;
        try {
            conn = ConnectionDB.getConnection();
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
     * Transformo una fila del ResultSet en un objecte Escalador.
     */
    private Escalador map(ResultSet rs) throws SQLException {

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