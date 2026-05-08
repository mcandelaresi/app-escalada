package controlador;

import dao.sqlite.*;
import model.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ViaController {

    private final ViaDAO viaDAO;
    private final ViaEsportivaDAO viaEsportivaDAO;
    private final ViaClassicaDAO viaClassicaDAO;
    private final ViaGelDAO viaGelDAO;
    private final EscolaDAO escolaDAO;
    private final SectorDAO sectorDAO;
    private final EscaladorDAO escaladorDAO;

    public ViaController(ViaDAO viaDAO,
                         ViaEsportivaDAO viaEsportivaDAO,
                         ViaClassicaDAO viaClassicaDAO,
                         ViaGelDAO viaGelDAO,
                         EscolaDAO escolaDAO,
                         SectorDAO sectorDAO,
                         EscaladorDAO escaladorDAO) {

        this.viaDAO = viaDAO;
        this.viaEsportivaDAO = viaEsportivaDAO;
        this.viaClassicaDAO = viaClassicaDAO;
        this.viaGelDAO = viaGelDAO;
        this.escolaDAO = escolaDAO;
        this.sectorDAO = sectorDAO;
        this.escaladorDAO = escaladorDAO;
    }


    // CRUD
    public void crear(Via v, Object subtype) {
        viaDAO.insert(v);

        if (subtype instanceof ViaEsportiva ve) {
            viaEsportivaDAO.insert(ve);
        } else if (subtype instanceof ViaClassica vc) {
            viaClassicaDAO.insert(vc);
        } else if (subtype instanceof ViaGel vg) {
            viaGelDAO.insert(vg);
        }
    }

    public Via buscar(int id) {
        return viaDAO.findById(id);
    }

    public List<Via> totes() {
        return viaDAO.findAll();
    }

    public void eliminar(Via v) {
        if (v == null) return;

        if (v.getTipus().equalsIgnoreCase("Esportiva")) {
            viaEsportivaDAO.delete(v.getIdVia());
        } else if (v.getTipus().equalsIgnoreCase("Classica")) {
            viaClassicaDAO.delete(v.getIdVia());
        } else {
            viaGelDAO.delete(v.getIdVia());
        }

        viaDAO.delete(v.getIdVia());
    }

    public void update(Via v) {
        viaDAO.update(v);
    }

    // VALIDACIONS / AUX

    public boolean escolaExisteix(int id) {
        return escolaDAO.findById(id) != null;
    }

    public boolean sectorValido(int idSector, int idEscola) {
        Sector s = sectorDAO.findById(idSector);
        return s != null && s.getIdEscola() == idEscola;
    }

    public boolean viaNomDisponible(String nom, int idEscola) {
        return viaDAO.findByNomAndEscola(nom, idEscola) == null;
    }

    public String avui() {
        return LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    public void actualitzarEstatsCaducats() {
        List<Via> vies = viaDAO.findAll();
        for (Via v : vies) {
            if (v.getDataEstat() != null && LocalDate.parse(v.getDataEstat()).isBefore(LocalDate.now())) {
                v.setEstat("Caducada");
                viaDAO.update(v);
            }
        }
    }

    // getters
    public EscaladorDAO getEscaladorDAO() {
        return escaladorDAO;
    }
}