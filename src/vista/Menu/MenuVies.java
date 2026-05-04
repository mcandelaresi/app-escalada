package vista.Menu;

import dao.sqlite.EscaladorDAO;
import dao.sqlite.EscolaDAO;
import dao.sqlite.SectorDAO;
import dao.sqlite.ViaClassicaDAO;
import dao.sqlite.ViaDAO;
import dao.sqlite.ViaEsportivaDAO;
import dao.sqlite.ViaGelDAO;
import excepcions.Validacions;
import helpers.AuxEscalador;
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
import model.enums.Orientacio;
import model.enums.TipusAncoratge;
import model.enums.TipusRoca;
import model.enums.TipusVia;
import vista.Vista;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class MenuVies {

    private final Scanner sc;
    private final ViaDAO viaDAO;
    private final ViaEsportivaDAO viaEsportivaDAO;
    private final ViaClassicaDAO viaClassicaDAO;
    private final ViaGelDAO viaGelDAO;
    private final EscolaDAO escolaDAO;
    private final SectorDAO sectorDAO;
    private final EscaladorDAO escaladorDAO;

    public MenuVies(Scanner sc, ViaDAO viaDAO, ViaEsportivaDAO viaEsportivaDAO,
                    ViaClassicaDAO viaClassicaDAO, ViaGelDAO viaGelDAO,
                    EscolaDAO escolaDAO, SectorDAO sectorDAO, EscaladorDAO escaladorDAO) {
        this.sc = sc;
        this.viaDAO = viaDAO;
        this.viaEsportivaDAO = viaEsportivaDAO;
        this.viaClassicaDAO = viaClassicaDAO;
        this.viaGelDAO = viaGelDAO;
        this.escolaDAO = escolaDAO;
        this.sectorDAO = sectorDAO;
        this.escaladorDAO = escaladorDAO;
    }

    public void menu() {

        int op;

        do {
            Vista.menuVies();
            op = Validacions.llegirOpcio(sc, "Opció: ", 0, 5);

            switch (op) {
                case 1 -> crear();
                case 2 -> llistarUna();
                case 3 -> llistarTotes();
                case 4 -> modificar();
                case 5 -> eliminar();
            }

        } while (op != 0);
    }

    private void crear() {
        String nom = Validacions.llegirTextNoBuit(sc, "Nom: ");
        int idEscola = Validacions.llegirEnterNoNegatiu(sc, "ID escola: ");
        Escola escola = escolaDAO.findById(idEscola);
        if (escola == null) {
            System.out.println("L'escola no existeix.");
            return;
        }

        int idSector = Validacions.llegirEnterNoNegatiu(sc, "ID sector: ");
        Sector sector = sectorDAO.findById(idSector);
        if (sector == null || sector.getIdEscola() != idEscola) {
            System.out.println("El sector no existeix o no pertany a aquesta escola.");
            return;
        }

        if (viaDAO.findByNomAndEscola(nom, idEscola) != null) {
            System.out.println("Ja existeix una via amb aquest nom dins d'aquesta escola.");
            return;
        }

        TipusVia tipus = llegirTipusVia();
        String grau = llegirGrau(tipus);
        String orientacio = llegirOrientacio();
        EstatVia estat = llegirEstatVia();
        String dataEstat = llegirDataEstat(estat);
        String ancoratge = llegirAncoratge(tipus);
        String roca = llegirTipusRoca();
        String restriccions = Validacions.llegirTextOpcional(sc, "Restriccions: ");
        int idCreador = obtenirOCrearEscalador();

        Via base = new Via(0, nom, grau, orientacio, estat.getValor(), dataEstat,
                tipus.getValor(), ancoratge, roca, idCreador, idSector, idEscola, restriccions) {};
        viaDAO.insert(base);

        if (tipus == TipusVia.ESPORTIVA) {
            int llargada = llegirLlargadaEsportiva();
            viaEsportivaDAO.insert(new ViaEsportiva(base.getIdVia(), nom, grau, orientacio,
                    estat.getValor(), dataEstat, tipus.getValor(), ancoratge, roca,
                    idCreador, idSector, idEscola, restriccions, llargada));
        } else if (tipus == TipusVia.CLASSICA) {
            String ancoratgesPermesos = Validacions.llegirTextNoBuit(sc, "Ancoratges permesos: ");
            viaClassicaDAO.insert(new ViaClassica(base.getIdVia(), nom, grau, orientacio,
                    estat.getValor(), dataEstat, tipus.getValor(), ancoratge, roca,
                    idCreador, idSector, idEscola, restriccions, ancoratgesPermesos));
        } else {
            viaGelDAO.insert(new ViaGel(base.getIdVia(), nom, grau, orientacio,
                    estat.getValor(), dataEstat, tipus.getValor(), ancoratge, roca,
                    idCreador, idSector, idEscola, restriccions));
        }

        System.out.println(" Via creada amb ID " + base.getIdVia());
    }

    private void llistarUna() {

        Via v = viaDAO.findById(Validacions.llegirEnterNoNegatiu(sc, "ID: "));

        if (v == null) {
            System.out.println("No trobada");
            return;
        }

        mostrar(v);
    }

    private void llistarTotes() {

        List<Via> list = viaDAO.findAll();

        if (list.isEmpty()) {
            System.out.println("Sense vies");
            return;
        }

        list.forEach(this::mostrar);
    }

    private void modificar() {

        Via v = viaDAO.findById(Validacions.llegirEnterNoNegatiu(sc, "ID: "));

        if (v == null) {
            System.out.println("No trobada");
            return;
        }

        String nom = Validacions.llegirTextOpcional(sc, "Nom (" + v.getNom() + "): ");
        int idEscola = v.getIdEscola();
        String escolaText = Validacions.llegirTextOpcional(sc, "ID escola (" + idEscola + "): ");
        if (!escolaText.isEmpty()) {
            int nouIdEscola = parseEnter(escolaText, idEscola);
            if (escolaDAO.findById(nouIdEscola) == null) {
                System.out.println("L'escola no existeix.");
                return;
            }
            idEscola = nouIdEscola;
        }

        int idSector = v.getIdSector();
        String sectorText = Validacions.llegirTextOpcional(sc, "ID sector (" + idSector + "): ");
        if (!sectorText.isEmpty()) {
            int nouIdSector = parseEnter(sectorText, idSector);
            Sector sector = sectorDAO.findById(nouIdSector);
            if (sector == null || sector.getIdEscola() != idEscola) {
                System.out.println("El sector no existeix o no pertany a aquesta escola.");
                return;
            }
            idSector = nouIdSector;
        }

        String nomFinal = nom.isEmpty() ? v.getNom() : nom;
        Via existent = viaDAO.findByNomAndEscola(nomFinal, idEscola);
        if (existent != null && existent.getIdVia() != v.getIdVia()) {
            System.out.println("Ja existeix una via amb aquest nom dins d'aquesta escola.");
            return;
        }

        if (!nom.isEmpty()) v.setNom(nom);
        v.setIdEscola(idEscola);
        v.setIdSector(idSector);

        String grau = Validacions.llegirTextOpcional(sc, "Grau (" + v.getGrau() + "): ");
        if (!grau.isEmpty()) {
            if (!AuxVia.grauValidPerTipus(grau, TipusVia.fromValor(v.getTipus()))) {
                System.out.println("Grau no vàlid.");
                return;
            }
            v.setGrau(AuxVia.normalitzarGrau(grau));
        }

        String orientacio = Validacions.llegirTextOpcional(sc, "Orientació (" + v.getOrientacio() + "): ");
        if (!orientacio.isEmpty()) {
            Orientacio o = Orientacio.fromValor(orientacio);
            if (o == null) {
                System.out.println("Orientació no vàlida.");
                return;
            }
            v.setOrientacio(o.name());
        }

        String estat = Validacions.llegirTextOpcional(sc, "Estat (" + v.getEstat() + "): ");
        if (!estat.isEmpty()) {
            EstatVia nouEstat = AuxVia.normalitzarEstat(estat);
            v.setEstat(nouEstat.getValor());
            v.setDataEstat(llegirDataEstat(nouEstat));
        }

        String ancoratges = Validacions.llegirTextOpcional(sc, "Ancoratges (" + v.getAncoratges() + "): ");
        if (!ancoratges.isEmpty()) v.setAncoratges(AuxVia.normalitzarTipusAncoratge(ancoratges, v.getTipus()));

        String roca = Validacions.llegirTextOpcional(sc, "Tipus de roca (" + v.getTipusDeRoca() + "): ");
        if (!roca.isEmpty()) v.setTipusDeRoca(AuxVia.normalitzarTipusRoca(roca));

        String rest = Validacions.llegirTextOpcional(sc, "Restriccions (" + v.getRestriccions() + "): ");
        if (!rest.isEmpty()) v.setRestriccions(rest);

        viaDAO.update(v);

        if (TipusVia.ESPORTIVA.getValor().equalsIgnoreCase(v.getTipus())) {
            ViaEsportiva ve = viaEsportivaDAO.findById(v.getIdVia());
            if (ve != null) {
                String llarg = Validacions.llegirTextOpcional(sc, "Llargada (" + ve.getLlargada() + "): ");
                if (!llarg.isEmpty()) {
                    int llargada = parseEnter(llarg, ve.getLlargada());
                    if (llargada < 5 || llargada > 30) {
                        System.out.println("La llargada ha d'estar entre 5 i 30.");
                        return;
                    }
                    ve.setLlargada(llargada);
                    viaEsportivaDAO.update(ve);
                }
            }
        } else if (TipusVia.CLASSICA.getValor().equalsIgnoreCase(v.getTipus())) {
            ViaClassica vc = viaClassicaDAO.findById(v.getIdVia());
            if (vc != null) {
                String anc = Validacions.llegirTextOpcional(sc, "Ancoratges permesos (" + vc.getAncoratgesPermesos() + "): ");
                if (!anc.isEmpty()) {
                    vc.setAncoratgesPermesos(anc);
                    viaClassicaDAO.update(vc);
                }
            }
        }

        System.out.println(" Modificada");
    }

    private void eliminar() {
        Via v = viaDAO.findById(Validacions.llegirEnterNoNegatiu(sc, "ID: "));
        if (v == null) {
            System.out.println("No trobada");
            return;
        }

        if (TipusVia.ESPORTIVA.getValor().equalsIgnoreCase(v.getTipus())) {
            viaEsportivaDAO.delete(v.getIdVia());
        } else if (TipusVia.CLASSICA.getValor().equalsIgnoreCase(v.getTipus())) {
            viaClassicaDAO.delete(v.getIdVia());
        } else {
            viaGelDAO.delete(v.getIdVia());
        }

        viaDAO.delete(v.getIdVia());
        System.out.println(" Eliminada");
    }

    private void mostrar(Via v) {
        System.out.println(v.getIdVia() + " - " + v.getNom() +
                " | " + v.getTipus() +
                " | " + v.getGrau() +
                " | " + v.getEstat() +
                " | escola=" + v.getIdEscola() +
                " | sector=" + v.getIdSector());
    }

    private TipusVia llegirTipusVia() {
        while (true) {
            TipusVia tipus = TipusVia.fromValor(Validacions.llegirTextNoBuit(sc, "Tipus (Esportiva/Classica/Gel): "));
            if (tipus != null) return tipus;
            System.out.println("Tipus no vàlid.");
        }
    }

    private String llegirGrau(TipusVia tipus) {
        while (true) {
            String valor = Validacions.llegirTextNoBuit(sc, "Grau: ");
            if (AuxVia.grauValidPerTipus(valor, tipus)) return AuxVia.normalitzarGrau(valor);
            System.out.println("Grau no vàlid.");
        }
    }

    private String llegirOrientacio() {
        while (true) {
            Orientacio o = Orientacio.fromValor(Validacions.llegirTextNoBuit(sc, "Orientació (N,NE,NO,SE,SO,E,O,S): "));
            if (o != null) return o.name();
            System.out.println("Orientació no vàlida.");
        }
    }

    private EstatVia llegirEstatVia() {
        while (true) {
            EstatVia estat = EstatVia.fromValor(Validacions.llegirTextNoBuit(sc, "Estat (Apte/Construccio/Tancada): "));
            if (estat != null) return estat;
            System.out.println("Estat no vàlid.");
        }
    }

    private String llegirDataEstat(EstatVia estat) {
        if (estat == EstatVia.APTE) {
            return LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
        }

        while (true) {
            int dies = Validacions.llegirEnterNoNegatiu(sc, "Dies fins a Apte: ");
            if (dies > 0) {
                return LocalDate.now().plusDays(dies).format(DateTimeFormatter.ISO_LOCAL_DATE);
            }
            System.out.println("Cal un nombre de dies superior a 0.");
        }
    }

    private String llegirAncoratge(TipusVia tipus) {
        while (true) {
            TipusAncoratge a = TipusAncoratge.fromValor(Validacions.llegirTextNoBuit(sc, "Ancoratge: "));
            if (a == null) {
                System.out.println("Ancoratge no vàlid.");
                continue;
            }
            try {
                return AuxVia.normalitzarTipusAncoratge(a.getValor(), tipus.getValor());
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private String llegirTipusRoca() {
        while (true) {
            TipusRoca roca = TipusRoca.fromValor(Validacions.llegirTextNoBuit(sc, "Tipus de roca (Conglomerat/Granit/Calcaria/Arenisca/Altres): "));
            if (roca != null) return roca.getValor();
            System.out.println("Tipus de roca no vàlid.");
        }
    }

    private int llegirLlargadaEsportiva() {
        while (true) {
            int llargada = Validacions.llegirEnterNoNegatiu(sc, "Llargada (5-30): ");
            if (llargada >= 5 && llargada <= 30) {
                return llargada;
            }
            System.out.println("El valor ha d'estar entre 5 i 30.");
        }
    }

    private String llegirEstilPreferit() {
        while (true) {
            String valor = Validacions.llegirTextNoBuit(sc, "Estil preferit (Esportiva/Classica/Gel): ");
            String net = AuxEscalador.normalitzarEstil(valor);
            if (AuxEscalador.estilValid(net)) return net;
            System.out.println("Estil no vàlid.");
        }
    }

    private String llegirGrauGeneral() {
        while (true) {
            String valor = Validacions.llegirTextNoBuit(sc, "Grau: ");
            if (GrauDificultat.fromValor(valor) != null) return AuxVia.normalitzarGrau(valor);
            System.out.println("Grau no vàlid.");
        }
    }

    private int obtenirOCrearEscalador() {
        int id = Validacions.llegirEnterNoNegatiu(sc, "ID creador (0 per donar-lo d'alta ara): ");
        if (id > 0 && escaladorDAO.findById(id) != null) return id;
        if (id == 0) {
            Escalador nou = nouEscalador();
            escaladorDAO.insert(nou);
            return nou.getIdEscalador();
        }

        System.out.println("L'escalador no existeix.");
        return obtenirOCrearEscalador();
    }

    private int parseEnter(String valor, int perDefecte) {
        try {
            return Integer.parseInt(valor.trim());
        } catch (NumberFormatException e) {
            return perDefecte;
        }
    }

    private Escalador nouEscalador() {
        return new Escalador(0,
                Validacions.llegirTextNoBuit(sc, "Nom: "),
                Validacions.llegirTextNoBuit(sc, "Àlies: "),
                Validacions.llegirEnterNoNegatiu(sc, "Edat: "),
                llegirGrauGeneral(),
                llegirEstilPreferit(),
                0);
    }
}