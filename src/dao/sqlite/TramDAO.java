package dao.sqlite;

import dao.ConnectionDB;
import dao.dao;
import model.Tram;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TramDAO implements dao<Tram, Integer> {

    @Override
    public void insert(Tram tram) {
        String sql = "INSERT INTO trams (num_llarg, llarg, grau_dificultat, id_via) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, tram.getNumLlarg());
            ps.setInt(2, tram.getLlarg());
            ps.setString(3, tram.getGrauDificultat());
            ps.setInt(4, tram.getIdVia());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    tram.setIdTram(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error inserint tram: " + e.getMessage());
        }
    }

    @Override
    public Tram findById(Integer id) {
        String sql = "SELECT * FROM trams WHERE id_tram = ?";

        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Tram(
                    rs.getInt("id_tram"),
                    rs.getInt("num_llarg"),
                    rs.getInt("llarg"),
                    rs.getString("grau_dificultat"),
                    rs.getInt("id_via")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error cercant tram: " + e.getMessage());
        }

        return null;
    }

    @Override
    public List<Tram> findAll() {
        List<Tram> lista = new ArrayList<>();
        String sql = "SELECT * FROM trams";

        try (Connection conn = ConnectionDB.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(new Tram(
                    rs.getInt("id_tram"),
                    rs.getInt("num_llarg"),
                    rs.getInt("llarg"),
                    rs.getString("grau_dificultat"),
                    rs.getInt("id_via")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error llistant trams: " + e.getMessage());
        }

        return lista;
    }

    public List<Tram> findByIdVia(Integer idVia) {
        List<Tram> lista = new ArrayList<>();
        String sql = "SELECT * FROM trams WHERE id_via = ? ORDER BY num_llarg";

        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idVia);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                lista.add(new Tram(
                    rs.getInt("id_tram"),
                    rs.getInt("num_llarg"),
                    rs.getInt("llarg"),
                    rs.getString("grau_dificultat"),
                    rs.getInt("id_via")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error cercant trams per via: " + e.getMessage());
        }

        return lista;
    }

    @Override
    public void update(Tram tram) {
        String sql = "UPDATE trams SET num_llarg=?, llarg=?, grau_dificultat=? WHERE id_tram=?";

        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, tram.getNumLlarg());
            ps.setInt(2, tram.getLlarg());
            ps.setString(3, tram.getGrauDificultat());
            ps.setInt(4, tram.getIdTram());

            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error actualitzant tram: " + e.getMessage());
        }
    }

    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM trams WHERE id_tram=?";

        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error eliminant tram: " + e.getMessage());
        }
    }
}
