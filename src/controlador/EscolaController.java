package controlador;

import dao.sqlite.EscolaDAO;
import helpers.AuxSector;
import model.Escola;

import java.util.List;

public class EscolaController {

    private final EscolaDAO dao = new EscolaDAO();

    public boolean crearEscola(Escola e) {
        if (dao.findByNom(e.getNom()) != null) {
            return false;
        }
        dao.insert(e);
        return true;
    }

    public Escola buscarPerId(int id) {
        return dao.findById(id);
    }

    public List<Escola> totes() {
        return dao.findAll();
    }

    public boolean modificar(Escola e, String nouNom) {
        if (nouNom != null && !nouNom.isEmpty()) {
            Escola existent = dao.findByNom(nouNom);
            if (existent != null && existent.getIdEscola() != e.getIdEscola()) {
                return false;
            }
            e.setNom(nouNom);
        }
        dao.update(e);
        return true;
    }

    public void eliminar(int id) {
        dao.delete(id);
    }

    public Escola buscarPerNom(String nom) {
        return dao.findByNom(nom);
    }

    public String normalitzarPopularitat(String pop) {
        return AuxSector.normalitzarPopularitat(pop);
    }
}