package vista.Menu;

import dao.ConnectionDB;
import dao.sqlite.EscaladorDAO;
import dao.sqlite.EscolaDAO;
import dao.sqlite.SectorDAO;
import dao.sqlite.ViaClassicaDAO;
import dao.sqlite.ViaDAO;
import dao.sqlite.ViaEsportivaDAO;
import dao.sqlite.ViaGelDAO;
import excepcions.Validacions;
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
import vista.Vista;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class MenuCerca {

    private final Scanner sc;
    private final ViaDAO viaDAO;
    private final EscolaDAO escolaDAO;
    private final SectorDAO sectorDAO;
    private final EscaladorDAO escaladorDAO;
    private final ViaEsportivaDAO viaEsportivaDAO;
    private final ViaClassicaDAO viaClassicaDAO;
    private final ViaGelDAO viaGelDAO;

    public MenuCerca(Scanner sc, ViaDAO viaDAO) {
        this(sc, viaDAO,
                crearEscolaDAO(),
                crearSectorDAO(),
                new EscaladorDAO(),
                new ViaEsportivaDAO(),
                new ViaClassicaDAO(),
                new ViaGelDAO());
    }

    public MenuCerca(Scanner sc, ViaDAO viaDAO, EscolaDAO escolaDAO, SectorDAO sectorDAO,
                     EscaladorDAO escaladorDAO, ViaEsportivaDAO viaEsportivaDAO,
                     ViaClassicaDAO viaClassicaDAO, ViaGelDAO viaGelDAO) {
        this.sc = sc;
        this.viaDAO = viaDAO;
        this.escolaDAO = escolaDAO;
        this.sectorDAO = sectorDAO;
        this.escaladorDAO = escaladorDAO;
        this.viaEsportivaDAO = viaEsportivaDAO;
        this.viaClassicaDAO = viaClassicaDAO;
        this.viaGelDAO = viaGelDAO;
    }

    public void menu() {

        int op;

        do {
            Vista.menuBusquedas();

            op = Validacions.llegirOpcio(sc, "Opció: ", 0, 8);

            switch (op) {
                case 1 -> viesDunaEscolaDisponibles();
                case 2 -> viesPerDificultat();
                case 3 -> viesPerEstat();
                case 4 -> escolesAmbRestriccions();
                case 5 -> sectorsAmbMesDeX();
                case 6 -> escaladorsMateixNivell();
                case 7 -> viesApteRecentment();
                case 8 -> viesMesLlarguesEscola();
            }

        } while (op != 0);
    }

    private void viesDunaEscolaDisponibles() {
        int id = Validacions.llegirEnterNoNegatiu(sc, "ID escola: ");
        Escola escola = escolaDAO.findById(id);
        if (escola == null) {
            System.out.println("L'escola no existeix.");
            return;
        }

        List<Via> resultat = new java.util.ArrayList<>();
        List<Via> totes = AuxCerca.viesDunaEscola(viaDAO.findAll(), id);
        for (Via via : totes) {
            if (AuxVia.esApte(via)) {
                resultat.add(via);
            }
        }
        mostrarVies(resultat);
    }

    private void viesPerDificultat() {
        GrauDificultat min = llegirGrau("Grau mínim: ");
        GrauDificultat max = llegirGrau("Grau màxim: ");

        if (min.ordinal() > max.ordinal()) {
            GrauDificultat aux = min;
            min = max;
            max = aux;
        }

        List<Via> resultat = new java.util.ArrayList<>();
        List<Via> totes = viaDAO.findAll();
        for (Via via : totes) {
            GrauDificultat grau = GrauDificultat.fromValor(via.getGrau());
            if (grau != null && grau.ordinal() >= min.ordinal() && grau.ordinal() <= max.ordinal()) {
                resultat.add(via);
            }
        }

        mostrarVies(resultat);
    }

    private void viesPerEstat() {
        EstatVia estat = llegirEstat();
        mostrarVies(AuxCerca.viesPerEstat(viaDAO.findAll(), estat.getValor()));
    }

    private void escolesAmbRestriccions() {
        List<Escola> resultat = AuxCerca.escolesAmbRestriccionsActives(escolaDAO.findAll());
        if (resultat.isEmpty()) {
            System.out.println("Sense resultats");
            return;
        }

        for (Escola escola : resultat) {
            System.out.println(escola.getIdEscola() + " - " + escola.getNom() + " | " + escola.getRestriccions());
        }
    }

    private void sectorsAmbMesDeX() {
        int x = Validacions.llegirEnterNoNegatiu(sc, "X: ");
        List<Sector> sectors = AuxCerca.sectorsAmbMesDeX(sectorDAO.findAll(), viaDAO.findAll(), x);
        if (sectors.isEmpty()) {
            System.out.println("Sense resultats");
            return;
        }

        for (Sector sector : sectors) {
            long count = 0;
            List<Via> totes = viaDAO.findAll();
            for (Via via : totes) {
                if (via.getIdSector() == sector.getIdSector() && AuxVia.esApte(via)) {
                    count++;
                }
            }
            System.out.println(sector.getIdSector() + " - " + sector.getNom() + " | " + count + " vies disponibles");
        }
    }

    private void escaladorsMateixNivell() {
        Map<String, List<Escalador>> grups = AuxCerca.escaladorsMateixNivell(escaladorDAO.findAll());
        boolean trobat = false;

        for (Map.Entry<String, List<Escalador>> entry : grups.entrySet()) {
            if (entry.getValue().size() > 1) {
                trobat = true;
                System.out.println("Nivell " + entry.getKey() + ":");
                for (Escalador e : entry.getValue()) {
                    System.out.println("  - " + e.getIdEscalador() + " - " + e.getNom() + " (" + e.getAlias() + ")");
                }
            }
        }

        if (!trobat) {
            System.out.println("Sense resultats");
        }
    }

    private void viesApteRecentment() {
        LocalDate llindar = LocalDate.now().minusDays(30);
        List<Via> resultat = new java.util.ArrayList<>();
        List<Via> totes = viaDAO.findAll();
        for (Via via : totes) {
            if (AuxVia.esApte(via) && dataValida(via.getDataEstat())) {
                LocalDate data = LocalDate.parse(via.getDataEstat(), DateTimeFormatter.ISO_LOCAL_DATE);
                if (!data.isBefore(llindar)) {
                    resultat.add(via);
                }
            }
        }
        mostrarVies(resultat);
    }

    private void viesMesLlarguesEscola() {
        int idEscola = Validacions.llegirEnterNoNegatiu(sc, "ID escola: ");
        Escola escola = escolaDAO.findById(idEscola);
        if (escola == null) {
            System.out.println("L'escola no existeix.");
            return;
        }

        List<Via> resultat = new java.util.ArrayList<>();
        List<Via> totes = viaDAO.findAll();
        int max = -1;

        for (Via via : totes) {
            if (via.getIdEscola() != idEscola) {
                continue;
            }

            int llargada = llargadaVia(via);
            if (llargada > max) {
                max = llargada;
                resultat.clear();
                resultat.add(via);
            } else if (llargada == max) {
                resultat.add(via);
            }
        }

        mostrarVies(resultat);
    }

    private void mostrarVies(List<Via> list) {

        if (list.isEmpty()) {
            System.out.println("Sense resultats");
            return;
        }

        for (Via via : list) {
            mostrarVia(via);
        }
    }

    private void mostrarVia(Via via) {
        Escola escola = escolaDAO.findById(via.getIdEscola());
        Sector sector = sectorDAO.findById(via.getIdSector());

        System.out.println(via.getIdVia() + " - " + via.getNom() +
                " | " + via.getTipus() +
                " | grau=" + via.getGrau() +
                " | estat=" + via.getEstat() +
                " | escola=" + (escola == null ? via.getIdEscola() : escola.getNom()) +
                " | sector=" + (sector == null ? via.getIdSector() : sector.getNom()));
    }

    private GrauDificultat llegirGrau(String missatge) {
        while (true) {
            String valor = Validacions.llegirTextNoBuit(sc, missatge);
            GrauDificultat grau = GrauDificultat.fromValor(valor);
            if (grau != null) return grau;
            System.out.println("Grau no vàlid.");
        }
    }

    private EstatVia llegirEstat() {
        while (true) {
            EstatVia estat = EstatVia.fromValor(Validacions.llegirTextNoBuit(sc, "Estat: "));
            if (estat != null) return estat;
            System.out.println("Estat no vàlid.");
        }
    }

    private boolean dataValida(String data) {
        try {
            if (data == null || data.isBlank()) {
                return false;
            }
            LocalDate.parse(data, DateTimeFormatter.ISO_LOCAL_DATE);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    private static EscolaDAO crearEscolaDAO() {
        EscolaDAO dao = new EscolaDAO();
        Connection conn = ConnectionDB.getConnection();
        if (conn != null) {
            dao.setConnection(conn);
        }
        return dao;
    }

    private static SectorDAO crearSectorDAO() {
        SectorDAO dao = new SectorDAO();
        Connection conn = ConnectionDB.getConnection();
        if (conn != null) {
            dao.setConnection(conn);
        }
        return dao;
    }

    private int llargadaVia(Via via) {
        if (TipusVia.ESPORTIVA.getValor().equalsIgnoreCase(via.getTipus())) {
            ViaEsportiva ve = viaEsportivaDAO.findById(via.getIdVia());
            return ve == null ? 0 : ve.getLlargada();
        }
        if (TipusVia.CLASSICA.getValor().equalsIgnoreCase(via.getTipus())) {
            ViaClassica vc = viaClassicaDAO.findById(via.getIdVia());
            return vc == null ? 0 : vc.getLlargadaTotal();
        }
        if (TipusVia.GEL.getValor().equalsIgnoreCase(via.getTipus())) {
            ViaGel vg = viaGelDAO.findById(via.getIdVia());
            return vg == null ? 0 : vg.getLlargadaTotal();
        }
        return 0;
    }
}