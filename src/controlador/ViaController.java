package controlador;

import dao.sqlite.*;
import excepcions.Validacions;
import model.*;
import model.enums.TipusVia;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

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

    public void modificarVia(Scanner sc) {

        int id = Validacions.llegirEnterNoNegatiu(sc, "ID: ");
        Via v = buscar(id);

        if (v == null) {
            System.out.println("No trobada");
            return;
        }

        String nom = Validacions.llegirTextOpcional(sc, "Nom (" + v.getNom() + "): ");
        if (!nom.isEmpty()) v.setNom(nom);

        String grau = Validacions.llegirTextOpcional(sc, "Grau (" + v.getGrau() + "): ");
        if (!grau.isEmpty()) v.setGrau(grau);

        String estat = Validacions.llegirTextOpcional(sc, "Estat (" + v.getEstat() + "): ");
        if (!estat.isEmpty()) v.setEstat(estat);

        viaDAO.update(v);

        System.out.println("Modificada");
    }

    public void crearVia(Scanner sc) {

        String nom = Validacions.llegirTextNoBuit(sc, "Nom: ");

        int idEscola = Validacions.llegirEnterNoNegatiu(sc, "ID escola: ");
        if (!escolaExisteix(idEscola)) {
            System.out.println("Escola no existeix");
            return;
        }

        int idSector = Validacions.llegirEnterNoNegatiu(sc, "ID sector: ");
        if (!sectorValido(idSector, idEscola)) {
            System.out.println("Sector no vàlid");
            return;
        }

        if (!viaNomDisponible(nom, idEscola)) {
            System.out.println("Ja existeix una via amb aquest nom");
            return;
        }

        String tipus = Validacions.llegirTextNoBuit(sc, "Tipus (Esportiva/Classica/Gel): ");

        String grau = Validacions.llegirTextNoBuit(sc, "Grau: ");
        String orientacio = Validacions.llegirTextNoBuit(sc, "Orientacio: ");

        String estat = "APTE";
        String dataEstat = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);

        String ancoratges = Validacions.llegirTextNoBuit(sc, "Ancoratges: ");
        String roca = Validacions.llegirTextNoBuit(sc, "Tipus roca: ");
        String restriccions = Validacions.llegirTextOpcional(sc, "Restriccions: ");

        Via v = new Via(
                0,
                nom,
                grau,
                orientacio,
                estat,
                dataEstat,
                tipus,
                ancoratges,
                roca,
                0,
                idSector,
                idEscola,
                restriccions
        ) {};

        viaDAO.insert(v);

        System.out.println("Via creada amb ID " + v.getIdVia());
    }

    // CRUD BASE


    public Via buscar(int id) {
        return viaDAO.findById(id);
    }

    public List<Via> totes() {
        return viaDAO.findAll();
    }

    public void update(Via v) {
        viaDAO.update(v);
    }

    public void eliminar(Via v) {
        if (v == null) return;

        TipusVia tipus = TipusVia.fromValor(v.getTipus());

        if (tipus == TipusVia.ESPORTIVA) {
            viaEsportivaDAO.delete(v.getIdVia());
        } else if (tipus == TipusVia.CLASSICA) {
            viaClassicaDAO.delete(v.getIdVia());
        } else {
            viaGelDAO.delete(v.getIdVia());
        }

        viaDAO.delete(v.getIdVia());
    }


    // VALIDACIONS

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


    // ESTATS

    public void actualitzarEstatsCaducats() {

        List<Via> vies = viaDAO.findAll();

        for (Via v : vies) {

            if (v.getDataEstat() == null || v.getDataEstat().isBlank()) continue;

            try {
                LocalDate data = LocalDate.parse(v.getDataEstat());

                if (data.isBefore(LocalDate.now()) &&
                        !"APTE".equalsIgnoreCase(v.getEstat())) {

                    v.setEstat("APTE");
                    v.setDataEstat(LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE));

                    viaDAO.update(v);
                }

            } catch (Exception ignored) {
            }
        }
    }


    // GETTER
    public EscaladorDAO getEscaladorDAO() {
        return escaladorDAO;
    }
}