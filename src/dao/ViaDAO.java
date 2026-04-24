package dao;

import model.Via;
import dao.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO de l'entitat Via.
 * S'encarrega de gestionar les operacions CRUD sobre la taula "via"
 * utilitzant JDBC.
 */
public class ViaDAO implements dao<Via, Integer> {

    /**
     * Insereix una nova Via a la base de dades.
     */
    @Override
    public void insert(Via via) {

        // Sentència SQL preparada per inserir una via
        String sql = "INSERT INTO via (nom, grau, orientacio, estat, data_estat, tipus, ancoratges, tipus_roca, id_creador, id_sector, id_escola, restriccions) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        // Try-with-resources: tanca automàticament la connexió i el PreparedStatement
        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // Mapeig objecte Java → columnes de la base de dades
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

            // Executa la inserció a la base de dades
            ps.executeUpdate();

        } catch (SQLException e) {
            // Gestió bàsica d’errors SQL
            e.printStackTrace();
        }
    }

    /**
     * Cerca una Via per ID.
     */
    @Override
    public Via findById(Integer id) {

        String sql = "SELECT * FROM via WHERE id_via = ?";

        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id); // assignem l'ID a la consulta
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                //  Aquí s’hauria de construir l’objecte Via a partir del ResultSet

                return null; // pendent d’implementació
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Retorna totes les Vies de la base de dades.
     */
    @Override
    public List<Via> findAll() {

        List<Via> llista = new ArrayList<>();

        String sql = "SELECT * FROM via";

        try (Connection conn = ConnectionDB.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                // Aquí s’ha de mapar cada fila a un objecte Via
                // i afegir-lo a la llista
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return llista;
    }

    /**
     * Actualitza una Via existent a la base de dades.
     */
    @Override
    public void update(Via via) {

        String sql = "UPDATE via SET nom=?, grau=?, orientacio=?, estat=? WHERE id_via=?";

        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // Assignació de valors a la consulta
            ps.setString(1, via.getNom());
            ps.setString(2, via.getGrau());
            ps.setString(3, via.getOrientacio());
            ps.setString(4, via.getEstat());
            ps.setInt(5, via.getIdVia());

            // Executa l’actualització
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Elimina una Via a partir del seu ID.
     */
    @Override
    public void delete(Integer id) {

        String sql = "DELETE FROM via WHERE id_via=?";

        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id); // assignem l'ID a eliminar

            // Executa el borrat
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}