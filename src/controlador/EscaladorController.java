package controlador;

import dao.sqlite.EscaladorDAO;
import dao.sqlite.ViaDAO;
import helpers.AuxEscalador;
import model.Escalador;

import java.util.List;

public class EscaladorController {

    private final EscaladorDAO dao;
    private final ViaDAO viaDAO;

    public EscaladorController(EscaladorDAO dao, ViaDAO viaDAO) {
        this.dao = dao;
        this.viaDAO = viaDAO;
    }

    public void crear(Escalador e) {
        dao.insert(e);
    }

    public Escalador buscarPorId(int id) {
        return dao.findById(id);
    }

    public List<Escalador> buscarTodos() {
        return dao.findAll();
    }

    public void actualizar(Escalador e) {
        dao.update(e);
    }

    public void eliminar(int id) {
        dao.delete(id);
    }

    // VALIDACIONS

    public boolean existeVia(int idVia) {
        return viaDAO.findById(idVia) != null;
    }

    public String normalizarGrau(String grau) {
        return AuxEscalador.normalitzarGrau(grau);
    }

    public String normalizarEstil(String estil) {
        return AuxEscalador.normalitzarEstil(estil);
    }

    public boolean estilValid(String estil) {
        return AuxEscalador.estilValid(estil);
    }
}