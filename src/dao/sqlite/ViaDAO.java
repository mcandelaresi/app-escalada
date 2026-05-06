package dao.sqlite;

import dao.ConnectionDB;
import dao.dao;
import model.Via;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * DAO de l'entitat Via.
 * Gestiona totes les operacions CRUD contra la base de dades SQLite.
 */
public class ViaDAO implements dao<Via, Integer> {

    private static final Logger LOGGER = Logger.getLogger(ViaDAO.class.getName());

    /**
     * Inserta una nova via a la base de dades.
     */
    @Override
    public void insert(Via via) {

        String sql = "INSERT INTO vies (nom, grau, orientacio, estat, data_estat, tipus, ancoratges, tipus_roca, id_creador, id_sector, id_escola, restriccions) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionDB.getConnection()) {
            if (conn == null) {
                LOGGER.severe("No s'ha pogut obtenir la connexió per inserir una via.");
                return;
            }

            try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                ps.setString(1, via.getNom());
                ps.setString(2, via.getGrau());
                ps.setString(3, via.getOrientacio());
                ps.setString(4, via.getEstat());
                ps.setString(5, via.getDataEstat());
                ps.setString(6, via.getTipus());
                ps.setString(7, via.getAncoratges());
                ps.setString(8, via.getTipusDeRoca());
                ps.setInt(9, via.getIdCreador());
                ps.setInt(10, via.getIdSector());
                ps.setInt(11, via.getIdEscola());
                ps.setString(12, via.getRestriccions());

                ps.executeUpdate();

                // Recuperar ID autogenerat
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        via.setIdVia(rs.getInt(1));
                    }
                }
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error inserint la via", e);
        }
    }

    /**
     * Retorna una via pel seu ID.
     */
    @Override
    public Via findById(Integer id) {

        String sql = "SELECT * FROM vies WHERE id_via = ?";

        try (Connection conn = ConnectionDB.getConnection()) {
            if (conn == null) {
                LOGGER.severe("No s'ha pogut obtenir la connexió per cercar una via per ID.");
                return null;
            }

            try (PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setInt(1, id);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    return mapResultSetToVia(rs);
                }
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error cercant la via per ID", e);
        }

        return null;
    }

    /**
     * Retorna totes les vies.
     */
    @Override
    public List<Via> findAll() {

        List<Via> llista = new ArrayList<>();
        String sql = "SELECT * FROM vies";

        try (Connection conn = ConnectionDB.getConnection()) {
            if (conn == null) {
                LOGGER.severe("No s'ha pogut obtenir la connexió per llistar les vies.");
                return llista;
            }

            try (Statement st = conn.createStatement();
                 ResultSet rs = st.executeQuery(sql)) {

                while (rs.next()) {
                    llista.add(mapResultSetToVia(rs));
                }
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error llistant les vies", e);
        }

        return llista;
    }

    public Via findByNomAndEscola(String nom, int idEscola) {

        String sql = "SELECT * FROM vies WHERE LOWER(nom) = LOWER(?) AND id_escola = ?";

        try (Connection conn = ConnectionDB.getConnection()) {
            if (conn == null) {
                LOGGER.severe("No s'ha pogut obtenir la connexió per cercar una via per nom i escola.");
                return null;
            }

            try (PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setString(1, nom);
                ps.setInt(2, idEscola);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return mapResultSetToVia(rs);
                    }
                }
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error cercant la via per nom i escola", e);
        }

        return null;
    }

    /**
     * Actualitza una via existent.
     */
    @Override
    public void update(Via via) {

        String sql = "UPDATE vies SET nom=?, grau=?, orientacio=?, estat=?, data_estat=?, tipus=?, ancoratges=?, tipus_roca=?, id_creador=?, id_sector=?, id_escola=?, restriccions=? WHERE id_via=?";

        try (Connection conn = ConnectionDB.getConnection()) {
            if (conn == null) {
                LOGGER.severe("No s'ha pogut obtenir la connexió per actualitzar una via.");
                return;
            }

            try (PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setString(1, via.getNom());
                ps.setString(2, via.getGrau());
                ps.setString(3, via.getOrientacio());
                ps.setString(4, via.getEstat());
                ps.setString(5, via.getDataEstat());
                ps.setString(6, via.getTipus());
                ps.setString(7, via.getAncoratges());
                ps.setString(8, via.getTipusDeRoca());
                ps.setInt(9, via.getIdCreador());
                ps.setInt(10, via.getIdSector());
                ps.setInt(11, via.getIdEscola());
                ps.setString(12, via.getRestriccions());
                ps.setInt(13, via.getIdVia());

                ps.executeUpdate();
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error actualitzant la via", e);
        }
    }

    /**
     * Elimina una via pel seu ID.
     */
    @Override
    public void delete(Integer id) {

        String sql = "DELETE FROM vies WHERE id_via=?";

        try (Connection conn = ConnectionDB.getConnection()) {
            if (conn == null) {
                LOGGER.severe("No s'ha pogut obtenir la connexió per eliminar una via.");
                return;
            }

            try (PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setInt(1, id);
                ps.executeUpdate();
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error eliminant la via", e);
        }
    }
    // Metode per cercar una via pel seu nom
    public Via findByNom(String nom) {

        String sql = "SELECT * FROM vies WHERE LOWER(nom) = LOWER(?)";

        try (Connection conn = ConnectionDB.getConnection()) {
            if (conn == null) {
                LOGGER.severe("No s'ha pogut obtenir la connexió per cercar una via per nom.");
                return null;
            }

            try (PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setString(1, nom);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return mapResultSetToVia(rs);
                    }
                }
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error cercant la via per nom", e);
        }

        return null;
    }

    //   Busca totes les vies d'un sector.
    public List<Via> findBySector(int idSector) {

        List<Via> llista = new ArrayList<>();
        String sql = "SELECT * FROM vies WHERE id_sector = ?";

        try (Connection conn = ConnectionDB.getConnection()) {
            if (conn == null) {
                LOGGER.severe("No s'ha pogut obtenir la connexió per cercar vies per sector.");
                return llista;
            }

            try (PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setInt(1, idSector);

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        llista.add(mapResultSetToVia(rs));
                    }
                }
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error cercant vies per sector", e);
        }

        return llista;
    }

    // Busca totes les vies d'una escola.
    public List<Via> findByEscola(int idEscola) {

        List<Via> llista = new ArrayList<>();
        String sql = "SELECT * FROM vies WHERE id_escola = ?";

        try (Connection conn = ConnectionDB.getConnection()) {
            if (conn == null) {
                LOGGER.severe("No s'ha pogut obtenir la connexió per cercar vies per escola.");
                return llista;
            }

            try (PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setInt(1, idEscola);

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        llista.add(mapResultSetToVia(rs));
                    }
                }
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error cercant vies per escola", e);
        }

        return llista;
    }

    /**
     * Mètode privat que transforma un ResultSet en un objecte Via.
     */
    private Via mapResultSetToVia(ResultSet rs) throws SQLException {

        return new Via(
                rs.getInt("id_via"),
                rs.getString("nom"),
                rs.getString("grau"),
                rs.getString("orientacio"),
                rs.getString("estat"),
                rs.getString("data_estat"),
                rs.getString("tipus"),
                rs.getString("ancoratges"),
                rs.getString("tipus_roca"),
                rs.getInt("id_creador"),
                rs.getInt("id_sector"),
                rs.getInt("id_escola"),
                rs.getString("restriccions")
        ) {};
    }
}