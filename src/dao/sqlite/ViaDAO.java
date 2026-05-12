package dao.sqlite;

import dao.ConnectionDB;
import dao.dao;
import model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ViaDAO implements dao<Via, Integer> {

    public ViaDAO() {}

    @Override
    public void insert(Via v) {
        String sql = "INSERT INTO vies (nom, grau, orientacio, estat, data_estat, tipus, ancoratges, tipus_roca, id_creador, id_sector, id_escola, restriccions) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, v.getNom());
            ps.setString(2, v.getGrau());
            ps.setString(3, v.getOrientacio());
            ps.setString(4, v.getEstat());
            if (v.getDataEstat() != null) ps.setString(5, v.getDataEstat());
            else ps.setNull(5, Types.VARCHAR);
            ps.setString(6, v.getTipus());
            ps.setString(7, v.getAncoratges());
            ps.setString(8, v.getTipusDeRoca());
            if (v.getIdCreador() > 0) ps.setInt(9, v.getIdCreador());
            else ps.setNull(9, Types.INTEGER);
            ps.setInt(10, v.getIdSector());
            ps.setInt(11, v.getIdEscola());
            ps.setString(12, v.getRestriccions());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) v.setIdVia(rs.getInt(1));
            }
        } catch (SQLException e) {
            System.err.println("Error inserint via: " + e.getMessage());
        }
    }

    @Override
    public Via findById(Integer id) {
        String sql = "SELECT * FROM vies WHERE id_via = ?";
        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapAmbSubtipus(rs, conn);
            }
        } catch (SQLException e) {
            System.err.println("Error cercant via: " + e.getMessage());
        }
        return null;
    }

    public Via findByNomAndEscola(String nom, int idEscola) {
        String sql = "SELECT * FROM vies WHERE nom = ? AND id_escola = ?";
        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nom);
            ps.setInt(2, idEscola);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapAmbSubtipus(rs, conn);
            }
        } catch (SQLException e) {
            System.err.println("Error cercant via per nom i escola: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Via> findAll() {
        List<Via> llista = new ArrayList<>();
        String sql = "SELECT * FROM vies ORDER BY nom";
        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Via v = mapAmbSubtipus(rs, conn);
                if (v != null) llista.add(v);
            }
        } catch (SQLException e) {
            System.err.println("Error llistant vies: " + e.getMessage());
        }
        return llista;
    }

    @Override
    public void update(Via v) {
        String sql = "UPDATE vies SET nom=?, grau=?, orientacio=?, estat=?, data_estat=?, tipus=?, ancoratges=?, tipus_roca=?, id_creador=?, id_sector=?, id_escola=?, restriccions=? WHERE id_via=?";
        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, v.getNom());
            ps.setString(2, v.getGrau());
            ps.setString(3, v.getOrientacio());
            ps.setString(4, v.getEstat());
            if (v.getDataEstat() != null) ps.setString(5, v.getDataEstat());
            else ps.setNull(5, Types.VARCHAR);
            ps.setString(6, v.getTipus());
            ps.setString(7, v.getAncoratges());
            ps.setString(8, v.getTipusDeRoca());
            if (v.getIdCreador() > 0) ps.setInt(9, v.getIdCreador());
            else ps.setNull(9, Types.INTEGER);
            ps.setInt(10, v.getIdSector());
            ps.setInt(11, v.getIdEscola());
            ps.setString(12, v.getRestriccions());
            ps.setInt(13, v.getIdVia());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error actualitzant via: " + e.getMessage());
        }
    }

    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM vies WHERE id_via = ?";
        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error eliminant via: " + e.getMessage());
        }
    }

    // =====================================================================
    // Mapeig: crea el subtipus correcte (ViaEsportiva / ViaClassica / ViaGel)
    // IMPORTANT: Via és abstracta, mai s'instancia directament
    // =====================================================================
    private Via mapAmbSubtipus(ResultSet rs, Connection conn) throws SQLException {
        int id = rs.getInt("id_via");
        String nom = rs.getString("nom");
        String grau = rs.getString("grau");
        String orientacio = rs.getString("orientacio");
        String estat = rs.getString("estat");
        String dataEstat = rs.getString("data_estat");
        String tipus = rs.getString("tipus");
        String ancoratges = rs.getString("ancoratges");
        String tipusRoca = rs.getString("tipus_roca");
        int idCreador = rs.getInt("id_creador");
        if (rs.wasNull()) idCreador = 0;
        int idSector = rs.getInt("id_sector");
        int idEscola = rs.getInt("id_escola");
        String restriccions = rs.getString("restriccions");

        // IMPORTANT: "Clàssica" (amb accent) és el valor guardat a la BD
        return switch (tipus != null ? tipus : "") {
            case "Esportiva" -> {
                int llargada = getLlargadaEsportiva(conn, id);
                if (llargada < 5)  llargada = 5;
                if (llargada > 30) llargada = 30;
                yield new ViaEsportiva(id, nom, grau, orientacio, estat, dataEstat, tipus,
                        ancoratges, tipusRoca, idCreador, idSector, idEscola, restriccions, llargada);
            }
            case "Clàssica", "Classica" -> {
                String ancoratgesPermesos = getAncoratgesPermesosClassica(conn, id);
                ViaClassica vc = new ViaClassica(id, nom, grau, orientacio, estat, dataEstat, tipus,
                        ancoratges, tipusRoca, idCreador, idSector, idEscola, restriccions, ancoratgesPermesos);
                vc.establirTrams(getTrams(conn, id));
                yield vc;
            }
            case "Gel" -> {
                ViaGel vg = new ViaGel(id, nom, grau, orientacio, estat, dataEstat, tipus,
                        ancoratges, tipusRoca, idCreador, idSector, idEscola, restriccions);
                vg.establirTrams(getTrams(conn, id));
                yield vg;
            }
            default -> {
                System.err.println("Tipus de via desconegut: '" + tipus + "' (id=" + id + ")");
                yield new ViaEsportiva(id, nom, grau, orientacio, estat, dataEstat, tipus,
                        ancoratges, tipusRoca, idCreador, idSector, idEscola, restriccions, 5);
            }
        };
    }

    private int getLlargadaEsportiva(Connection conn, int idVia) throws SQLException {
        String sql = "SELECT llargada FROM vies_esportiva WHERE id_via = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idVia);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("llargada");
            }
        }
        return 10;
    }

    private String getAncoratgesPermesosClassica(Connection conn, int idVia) throws SQLException {
        String sql = "SELECT ancoratges_permesos FROM vies_classica WHERE id_via = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idVia);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getString("ancoratges_permesos");
            }
        }
        return "";
    }

    private List<model.Tram> getTrams(Connection conn, int idVia) throws SQLException {
        List<model.Tram> trams = new ArrayList<>();
        String sql = "SELECT * FROM trams WHERE id_via = ? ORDER BY num_llarg";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idVia);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int llarg = rs.getInt("llarg");
                    if (llarg < 15) llarg = 15;
                    if (llarg > 30) llarg = 30;
                    trams.add(new model.Tram(
                            rs.getInt("id_tram"),
                            rs.getInt("num_llarg"),
                            llarg,
                            rs.getString("grau_dificultat"),
                            idVia
                    ));
                }
            }
        }
        return trams;
    }
}
