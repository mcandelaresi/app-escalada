package controlador;

import dao.sqlite.SectorDAO;
import helpers.AuxSector;
import model.Sector;

import java.util.List;

public class SectorController {

    private final SectorDAO dao;

    public SectorController(SectorDAO dao) {
        this.dao = dao;
    }

    public boolean crearSector(Sector s) {

        if (dao.findByNomAndEscola(s.getNom(), s.getIdEscola()) != null) {
            return false;
        }

        dao.insert(s);
        return true;
    }

    public Sector buscar(int id) {
        return dao.findById(id);
    }

    public List<Sector> tots() {
        return dao.findAll();
    }

    public boolean modificar(Sector s, String nouNom) {

        if (nouNom != null && !nouNom.isEmpty()) {

            Sector existent = dao.findByNomAndEscola(nouNom, s.getIdEscola());

            if (existent != null && existent.getIdSector() != s.getIdSector()) {
                return false;
            }

            s.setNom(nouNom);
        }

        dao.update(s);
        return true;
    }

    public void eliminar(int id) {
        dao.delete(id);
    }

    public String normalitzarPopularitat(String pop) {
        return AuxSector.normalitzarPopularitat(pop);
    }
}