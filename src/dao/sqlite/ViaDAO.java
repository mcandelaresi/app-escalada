package dao.sqlite;

import dao.ConnectionDB;
import dao.dao;
import model.Via;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO de l'entitat Via.
 * Gestiona totes les operacions CRUD contra la base de dades SQLite.
 */
public class ViaDAO implements dao<Via, Integer> {

    /**
     * Inserta una nova via a la base de dades.
     */
    @Override
    public void insert(Via via) {

        String sql = "INSERT INTO via (nom, grau, orientacio, estat, data_estat, tipus, ancoratges, tipus_roca, id_creador, id_sector, id_escola, restriccions) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

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
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                via.setIdVia(rs.getInt(1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retorna una via pel seu ID.
     */
    @Override
    public Via findById(Integer id) {

        String sql = "SELECT * FROM via WHERE id_via = ?";

        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapResultSetToVia(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Retorna totes les vies.
     */
    @Override
    public List<Via> findAll() {

        List<Via> llista = new ArrayList<>();
        String sql = "SELECT * FROM via";

        try (Connection conn = ConnectionDB.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                llista.add(mapResultSetToVia(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return llista;
    }

    /**
     * Actualitza una via existent.
     */
    @Override
    public void update(Via via) {

        String sql = "UPDATE via SET nom=?, grau=?, orientacio=?, estat=?, data_estat=?, tipus=?, ancoratges=?, tipus_roca=?, id_creador=?, id_sector=?, id_escola=?, restriccions=? WHERE id_via=?";

        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

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

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Elimina una via pel seu ID.
     */
    @Override
    public void delete(Integer id) {

        String sql = "DELETE FROM via WHERE id_via=?";

        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
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