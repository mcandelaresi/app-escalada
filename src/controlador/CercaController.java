package controlador;

import dao.sqlite.EscaladorDAO;
import dao.sqlite.EscolaDAO;
import dao.sqlite.SectorDAO;
import dao.sqlite.ViaClassicaDAO;
import dao.sqlite.ViaDAO;
import dao.sqlite.ViaEsportivaDAO;
import dao.sqlite.ViaGelDAO;
import helpers.AuxCerca;
import helpers.AuxVia;
import model.Escalador;
import model.Escola;
import model.Sector;
import model.Via;
import model.ViaClassica;
import model.ViaEsportiva;
import model.ViaGel;
import model.enums.EstatVia;
import model.enums.GrauDificultat;
import model.enums.TipusVia;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CercaController {

    private final ViaDAO viaDAO;
    private final EscolaDAO escolaDAO;
    private final SectorDAO sectorDAO;
    private final EscaladorDAO escaladorDAO;

    private final ViaEsportivaDAO viaEsportivaDAO;
    private final ViaClassicaDAO viaClassicaDAO;
    private final ViaGelDAO viaGelDAO;

    public CercaController(
            ViaDAO viaDAO,
            EscolaDAO escolaDAO,
            SectorDAO sectorDAO,
            EscaladorDAO escaladorDAO,
            ViaEsportivaDAO viaEsportivaDAO,
            ViaClassicaDAO viaClassicaDAO,
            ViaGelDAO viaGelDAO
    ) {
        this.viaDAO = viaDAO;
        this.escolaDAO = escolaDAO;
        this.sectorDAO = sectorDAO;
        this.escaladorDAO = escaladorDAO;
        this.viaEsportivaDAO = viaEsportivaDAO;
        this.viaClassicaDAO = viaClassicaDAO;
        this.viaGelDAO = viaGelDAO;
    }


    public Escalador buscarEscaladorPerAlias(String alias) {
        return escaladorDAO.findByAlias(alias);
    }

    public List<Escalador> buscarEscaladorsPerNivell(String nivell) {
        return escaladorDAO.findByNivell(nivell);
    }

    public List<Via> viesDunaEscolaDisponibles(int idEscola) {

        if (escolaDAO.findById(idEscola) == null) {
            return new ArrayList<>();
        }

        List<Via> resultat = new ArrayList<>();

        List<Via> totes = AuxCerca.viesDunaEscola(viaDAO.findAll(), idEscola);

        for (Via via : totes) {
            if (AuxVia.esApte(via)) {
                resultat.add(via);
            }
        }

        return resultat;
    }


    public List<Via> viesPerDificultat(GrauDificultat min, GrauDificultat max) {

        if (min.ordinal() > max.ordinal()) {
            GrauDificultat aux = min;
            min = max;
            max = aux;
        }

        List<Via> resultat = new ArrayList<>();

        for (Via via : viaDAO.findAll()) {

            GrauDificultat grau = GrauDificultat.fromValor(via.getGrau());

            if (grau != null &&
                    grau.ordinal() >= min.ordinal() &&
                    grau.ordinal() <= max.ordinal()) {
                resultat.add(via);
            }
        }

        return resultat;
    }


    public List<Via> viesPerEstat(EstatVia estat) {
        return AuxCerca.viesPerEstat(viaDAO.findAll(), estat.getValor());
    }


    public List<Escola> escolesAmbRestriccions() {

        List<Escola> resultat = new ArrayList<>();

        for (Escola e : escolaDAO.findAll()) {
            if (e.getRestriccions() != null && !e.getRestriccions().isBlank()) {
                resultat.add(e);
            }
        }

        return resultat;
    }


    public List<Sector> sectorsAmbMesDeX(int x) {
        return AuxCerca.sectorsAmbMesDeX(sectorDAO.findAll(), viaDAO.findAll(), x);
    }


    public Map<String, List<Escalador>> escaladorsMateixNivell() {
        return AuxCerca.escaladorsMateixNivell(escaladorDAO.findAll());
    }


    public List<Via> viesApteRecentment() {

        List<Via> resultat = new ArrayList<>();
        LocalDate llindar = LocalDate.now().minusDays(30);

        for (Via v : viaDAO.findAll()) {

            if (!AuxVia.esApte(v)) continue;
            if (!dataValida(v.getDataEstat())) continue;

            LocalDate data = LocalDate.parse(
                    v.getDataEstat(),
                    DateTimeFormatter.ISO_LOCAL_DATE
            );

            if (!data.isBefore(llindar)) {
                resultat.add(v);
            }
        }

        return resultat;
    }


    public List<Via> viesMesLlarguesEscola(int idEscola) {

        List<Via> resultat = new ArrayList<>();
        int max = -1;

        for (Via v : viaDAO.findAll()) {

            if (v.getIdEscola() != idEscola) continue;

            int llarg = llargadaVia(v);

            if (llarg > max) {
                max = llarg;
                resultat.clear();
                resultat.add(v);
            } else if (llarg == max) {
                resultat.add(v);
            }
        }

        return resultat;
    }


    private int llargadaVia(Via via) {

        if (TipusVia.ESPORTIVA.getValor().equalsIgnoreCase(via.getTipus())) {
            ViaEsportiva v = viaEsportivaDAO.findById(via.getIdVia());
            return v != null ? v.getLlargada() : 0;
        }

        if (TipusVia.CLASSICA.getValor().equalsIgnoreCase(via.getTipus())) {
            ViaClassica v = viaClassicaDAO.findById(via.getIdVia());
            return v != null ? v.getLlargadaTotal() : 0;
        }

        if (TipusVia.GEL.getValor().equalsIgnoreCase(via.getTipus())) {
            ViaGel v = viaGelDAO.findById(via.getIdVia());
            return v != null ? v.getLlargadaTotal() : 0;
        }

        return 0;
    }

    private boolean dataValida(String data) {
        try {
            if (data == null || data.isBlank()) return false;

            LocalDate.parse(data, DateTimeFormatter.ISO_LOCAL_DATE);
            return true;

        } catch (DateTimeParseException e) {
            return false;
        }
    }
}