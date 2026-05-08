package dao.sqlite;

import dao.dao;
import model.Tram;
import model.ViaClassica;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ViaClassicaDAO implements dao<ViaClassica, Integer> {

    private final Connection connection;

    public ViaClassicaDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void insert(ViaClassica v) {

        String sql = "INSERT INTO vies_classica (ancoratges_permesos, id_via) VALUES (?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, v.getAncoratgesPermesos());
            stmt.setInt(2, v.getIdVia());

            stmt.executeUpdate();

            // validació després d’insert
            if (v.getLlargadaTotal() <= 50) {
                throw new IllegalArgumentException("Llargada total ha de ser major a 50m");
            }

        } catch (SQLException e) {
            System.err.println("Error inserint via clàssica: " + e.getMessage());
        }
    }

    @Override
    public ViaClassica findById(Integer id) {

        String sql = """
            SELECT vc.*, v.*
            FROM vies_classica vc
            JOIN vies v ON vc.id_via = v.id_via
            WHERE vc.id_via = ?
        """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    ViaClassica vc = new ViaClassica(
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
                            rs.getString("restriccions"),
                            rs.getString("ancoratges_permesos")
                    );

                    TramDAO tramDAO = new TramDAO();
                    List<Tram> trams = tramDAO.findByIdVia(id);
                    vc.establirTrams(trams);

                    return vc;
                }
            }

        } catch (SQLException e) {
            System.err.println("Error cercant via clàssica: " + e.getMessage());
        }

        return null;
    }

    @Override
    public List<ViaClassica> findAll() {

        List<ViaClassica> lista = new ArrayList<>();

        String sql = "SELECT * FROM vies_classica";

        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {

                lista.add(new ViaClassica(
                        rs.getInt("id_via"),
                        "", "", "", "", "",
                        "", "",
                        "", 0, 0, 0,
                        "",
                        rs.getString("ancoratges_permesos")
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

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error eliminant via clàssica: " + e.getMessage());
        }
    }
}