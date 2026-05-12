package controlador;

import dao.sqlite.*;
import excepcions.Validacions;
import model.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class ViaController {

    private final ViaDAO viaDAO;
    private final ViaEsportivaDAO viaEsportivaDAO;
    private final ViaClassicaDAO viaClassicaDAO;
    private final ViaGelDAO viaGelDAO;
    private final EscaladorDAO escaladorDAO;

    // Graus vàlids per Esportiva i Clàssica (fins 9c+)
    private static final String[] GRAUS_ESPORTIVA_CLASSICA = {
            "4","4+","5","5+","6a","6a+","6b","6b+","6c","6c+",
            "7a","7a+","7b","7b+","7c","7c+","8a","8a+","8b","8b+",
            "8c","8c+","9a","9a+","9b","9b+","9c","9c+"
    };

    // Graus vàlids per Gel (fins 8b)
    private static final String[] GRAUS_GEL = {
            "4","4+","5","5+","6a","6a+","6b","6b+","6c","6c+",
            "7a","7a+","7b","7b+","7c","7c+","8a","8a+","8b"
    };

    // Ancoratges vàlids per Esportiva
    private static final String[] ANCORATGES_ESPORTIVA = {"Spits","Parabolts","Quimics"};

    // Ancoratges vàlids per Clàssica (inclou spits/parabolts/químics)
    private static final String[] ANCORATGES_CLASSICA = {
            "Friends","Tascons","Bagues","Pitons","Tricams","BigBros","Spits","Parabolts","Quimics"
    };

    // Ancoratges vàlids per Gel (SENSE spits/parabolts/químics, segons enunciat)
    private static final String[] ANCORATGES_GEL = {
            "Friends","Tascons","Bagues","Pitons","Tricams","BigBros"
    };

    private static final String[] ORIENTACIONS = {"N","NE","NO","SE","SO","E","O","S"};

    public ViaController(ViaDAO viaDAO, ViaEsportivaDAO viaEsportivaDAO,
                         ViaClassicaDAO viaClassicaDAO, ViaGelDAO viaGelDAO,
                         EscaladorDAO escaladorDAO) {
        this.viaDAO = viaDAO;
        this.viaEsportivaDAO = viaEsportivaDAO;
        this.viaClassicaDAO = viaClassicaDAO;
        this.viaGelDAO = viaGelDAO;
        this.escaladorDAO = escaladorDAO;
    }

    // ===================== CRUD =====================

    public void crearVia(Scanner sc) {
        System.out.println("\n--- Tipus de Via ---");
        System.out.println("1. Esportiva");
        System.out.println("2. Clàssica");
        System.out.println("3. Gel");
        int tipus = Validacions.llegirOpcio(sc, "Tria tipus: ", 1, 3);
        String tipusStr = getTipusStr(tipus);

        String nom = Validacions.llegirTextNoBuit(sc, "Nom: ");
        String grau = llegirGrauValid(sc, tipusStr);
        String orientacio = llegirOrientacioValida(sc);

        int idEscola = Validacions.llegirEnterNoNegatiu(sc, "ID Escola: ");
        int idSector = Validacions.llegirEnterNoNegatiu(sc, "ID Sector: ");
        String ancoratges = llegirAncoratgesValids(sc, tipusStr);
        String tipusRoca = llegirTipusRoca(sc);
        int idCreador = llegirOCrearEscalador(sc);
        String restriccions = Validacions.llegirTextOpcional(sc, "Restriccions (opcional): ");
        if (restriccions.isEmpty()) restriccions = "Cap";

        String[] estatIData = llegirEstatIData(sc);
        String estat = estatIData[0];
        String dataEstat = estatIData[1];

        // Crear subtipus concret (Via és abstracta, NO s'instancia directament)
        Via via;
        if (tipus == 1) {
            int llargada = 0;
            boolean valid = false;
            while (!valid) {
                llargada = Validacions.llegirEnterNoNegatiu(sc, "Llargada (5-30m): ");
                if (llargada >= 5 && llargada <= 30) valid = true;
                else System.out.println("Error: la llargada ha d'estar entre 5 i 30 metres.");
            }
            via = new ViaEsportiva(0, nom, grau, orientacio, estat, dataEstat, tipusStr,
                    ancoratges, tipusRoca, idCreador, idSector, idEscola, restriccions, llargada);
        } else if (tipus == 2) {
            String ancoratgesPermesos = Validacions.llegirTextNoBuit(sc, "Ancoratges permesos (text lliure): ");
            via = new ViaClassica(0, nom, grau, orientacio, estat, dataEstat, tipusStr,
                    ancoratges, tipusRoca, idCreador, idSector, idEscola, restriccions, ancoratgesPermesos);
        } else {
            via = new ViaGel(0, nom, grau, orientacio, estat, dataEstat, tipusStr,
                    ancoratges, tipusRoca, idCreador, idSector, idEscola, restriccions);
        }

        // Insertar a vies (base) i obtenir l'id generat
        viaDAO.insert(via);
        // Recuperar la via insertada per obtenir l'id autogenerat
        Via inserida = viaDAO.findByNomAndEscola(nom, idEscola);
        if (inserida == null) {
            System.out.println("Error en crear la via.");
            return;
        }
        via.setIdVia(inserida.getIdVia());

        // Insertar a la taula del subtipus
        if (via instanceof ViaEsportiva ve) {
            ve.setIdVia(inserida.getIdVia());
            viaEsportivaDAO.insert(ve);
        } else if (via instanceof ViaClassica vc) {
            vc.setIdVia(inserida.getIdVia());
            viaClassicaDAO.insert(vc);
        } else if (via instanceof ViaGel vg) {
            vg.setIdVia(inserida.getIdVia());
            viaGelDAO.insert(vg);
        }

        System.out.println("Via '" + nom + "' creada correctament amb ID " + via.getIdVia() + ".");
    }

    public void modificarVia(Scanner sc) {
        int id = Validacions.llegirEnterNoNegatiu(sc, "ID Via a modificar: ");
        Via v = viaDAO.findById(id);
        if (v == null) {
            System.out.println("Via no trobada.");
            return;
        }
        mostrarVia(v);

        System.out.println("\nDeixa en blanc per mantenir el valor actual.");

        String nom = Validacions.llegirTextOpcional(sc, "Nou nom (" + v.getNom() + "): ");
        if (!nom.isEmpty()) v.setNom(nom);

        String grau = Validacions.llegirTextOpcional(sc, "Nou grau (" + v.getGrau() + "): ");
        if (!grau.isEmpty() && grauValid(grau, v.getTipus())) v.setGrau(grau);

        String restriccions = Validacions.llegirTextOpcional(sc, "Restriccions (" + v.getRestriccions() + "): ");
        if (!restriccions.isEmpty()) v.setRestriccions(restriccions);

        // Canvi d'estat
        System.out.println("Canviar estat? (actual: " + v.getEstat() + ")");
        System.out.println("1. Mantenir  2. Apte  3. Construccio  4. Tancada");
        int opcEstat = Validacions.llegirOpcio(sc, "Opció: ", 1, 4);
        if (opcEstat == 2) {
            v.setEstat("Apte");
            v.setDataEstat(null);
        } else if (opcEstat == 3 || opcEstat == 4) {
            String nouEstat = opcEstat == 3 ? "Construccio" : "Tancada";
            String dataFi = Validacions.llegirTextNoBuit(sc, "Data fi (" + nouEstat + ") [YYYY-MM-DD]: ");
            v.setEstat(nouEstat);
            v.setDataEstat(dataFi);
        }

        viaDAO.update(v);
        System.out.println("Via modificada correctament.");
    }

    public void llistarUna(Scanner sc) {
        int id = Validacions.llegirEnterNoNegatiu(sc, "ID Via: ");
        Via v = viaDAO.findById(id);
        if (v == null) {
            System.out.println("Via no trobada.");
            return;
        }
        mostrarVia(v);
    }

    public void llistarTotes() {
        List<Via> vies = viaDAO.findAll();
        if (vies.isEmpty()) {
            System.out.println("No hi ha vies registrades.");
            return;
        }
        System.out.println("\n=== TOTES LES VIES ===");
        for (Via v : vies) {
            mostrarViaResumida(v);
        }
        System.out.println("Total: " + vies.size() + " vies.");
    }

    public void eliminarVia(Scanner sc) {
        int id = Validacions.llegirEnterNoNegatiu(sc, "ID Via a eliminar: ");
        Via v = viaDAO.findById(id);
        if (v == null) {
            System.out.println("Via no trobada.");
            return;
        }
        System.out.println("Segur que vols eliminar '" + v.getNom() + "'? (s/n): ");
        String conf = sc.nextLine().trim();
        if (!conf.equalsIgnoreCase("s")) {
            System.out.println("Operació cancel·lada.");
            return;
        }
        // Esborrar subtipus primer (CASCADE ho fa a BD, però el DAO específic pot fer més neteja)
        switch (v.getTipus()) {
            case "Esportiva" -> viaEsportivaDAO.delete(v.getIdVia());
            case "Clàssica"  -> viaClassicaDAO.delete(v.getIdVia());
            case "Gel"       -> viaGelDAO.delete(v.getIdVia());
        }
        viaDAO.delete(v.getIdVia());
        System.out.println("Via eliminada.");
    }

    // ===================== LÒGICA DE NEGOCI =====================

    /**
     * Revisa totes les vies amb estat Construccio/Tancada.
     * Si data_estat < avui, torna a posar estat = Apte i esborra data_estat.
     * Nota: data_estat és la data FINS QUAN la via és no apta (no la data que es va posar).
     */
    public void actualitzarEstatsCaducats() {
        List<Via> vies = viaDAO.findAll();
        for (Via v : vies) {
            if (v.getEstat().equalsIgnoreCase("Apte")) continue;
            if (v.getDataEstat() != null && !v.getDataEstat().isBlank()) {
                try {
                    LocalDate dataFi = LocalDate.parse(v.getDataEstat());
                    if (!LocalDate.now().isBefore(dataFi)) {
                        // Ha passat la data límit -> tornar a Apte
                        v.setEstat("Apte");
                        v.setDataEstat(null);
                        viaDAO.update(v);
                        System.out.println("[Sistema] Via '" + v.getNom() + "' ha tornat a estat Apte.");
                    }
                } catch (Exception e) {
                    System.err.println("Error processant data de la via " + v.getIdVia() + ": " + e.getMessage());
                }
            }
        }
    }


    public Via buscar(int id) { return viaDAO.findById(id); }
    public List<Via> totes() { return viaDAO.findAll(); }
    public void update(Via v) { viaDAO.update(v); }


  // HELPER
    private String getTipusStr(int tipus) {
        return switch (tipus) {
            case 1 -> "Esportiva";
            case 2 -> "Clàssica";
            case 3 -> "Gel";
            default -> "Esportiva";
        };
    }

    private String llegirGrauValid(Scanner sc, String tipusVia) {
        String[] graus = tipusVia.equalsIgnoreCase("Gel") ? GRAUS_GEL : GRAUS_ESPORTIVA_CLASSICA;
        System.out.print("Graus vàlids: ");
        for (int i = 0; i < graus.length; i++) {
            System.out.print(graus[i] + (i < graus.length - 1 ? ", " : ""));
        }
        System.out.println();
        while (true) {
            String grau = Validacions.llegirTextNoBuit(sc, "Grau: ").trim();
            if (grauValid(grau, tipusVia)) return grau;
            System.out.println("Grau no vàlid per a via de tipus " + tipusVia + ".");
        }
    }

    private boolean grauValid(String grau, String tipusVia) {
        String[] graus = tipusVia.equalsIgnoreCase("Gel") ? GRAUS_GEL : GRAUS_ESPORTIVA_CLASSICA;
        for (String g : graus) {
            if (g.equalsIgnoreCase(grau)) return true;
        }
        return false;
    }

    private String llegirOrientacioValida(Scanner sc) {
        System.out.println("Orientacions: N, NE, NO, SE, SO, E, O, S");
        while (true) {
            String ori = Validacions.llegirTextNoBuit(sc, "Orientació: ").trim().toUpperCase();
            for (String o : ORIENTACIONS) {
                if (o.equals(ori)) return ori;
            }
            System.out.println("Orientació no vàlida.");
        }
    }

    private String llegirAncoratgesValids(Scanner sc, String tipusVia) {
        String[] opcions;
        if (tipusVia.equalsIgnoreCase("Esportiva")) opcions = ANCORATGES_ESPORTIVA;
        else if (tipusVia.equalsIgnoreCase("Gel"))  opcions = ANCORATGES_GEL;
        else opcions = ANCORATGES_CLASSICA;
        System.out.print("Ancoratges disponibles: ");
        for (int i = 0; i < opcions.length; i++) {
            System.out.print((i + 1) + "." + opcions[i] + " ");
        }
        System.out.println();
        int opc = Validacions.llegirOpcio(sc, "Tria ancoratge (número): ", 1, opcions.length);
        return opcions[opc - 1];
    }

    private String llegirTipusRoca(Scanner sc) {
        String[] tipus = {"Conglomerat","Granit","Calcaria","Arenisca","Altres"};
        System.out.println("Tipus de roca:");
        for (int i = 0; i < tipus.length; i++) {
            System.out.println("  " + (i + 1) + ". " + tipus[i]);
        }
        int opc = Validacions.llegirOpcio(sc, "Tria tipus de roca: ", 1, tipus.length);
        return tipus[opc - 1];
    }

    /**
     * Demana l'estat i, si no és Apte, demana la data fins quan serà no apta.
     * @return array [estat, dataFi] on dataFi pot ser null si estat = Apte
     */
    private String[] llegirEstatIData(Scanner sc) {
        System.out.println("Estat inicial:");
        System.out.println("  1. Apte");
        System.out.println("  2. Construccio");
        System.out.println("  3. Tancada");
        int opc = Validacions.llegirOpcio(sc, "Estat: ", 1, 3);
        if (opc == 1) return new String[]{"Apte", null};
        String estat = opc == 2 ? "Construccio" : "Tancada";
        String dataFi = "";
        while (dataFi.isEmpty()) {
            dataFi = Validacions.llegirTextNoBuit(sc, "Data fi de l'estat (YYYY-MM-DD), passada aquesta data tornarà a Apte: ");
            try {
                LocalDate.parse(dataFi);
            } catch (Exception e) {
                System.out.println("Format de data incorrecte. Utilitza YYYY-MM-DD.");
                dataFi = "";
            }
        }
        return new String[]{estat, dataFi};
    }

    /**
     * Busca un escalador per ID. Si no existeix, ofereix crear-lo.
     */
    private int llegirOCrearEscalador(Scanner sc) {
        while (true) {
            int id = Validacions.llegirEnterNoNegatiu(sc, "ID del creador (escalador): ");
            if (id == 0) {
                System.out.println("ID 0 no vàlid.");
                continue;
            }
            Escalador e = escaladorDAO.findById(id);
            if (e != null) {
                System.out.println("Creador: " + e.getNom() + " (" + e.getAlias() + ")");
                return id;
            }
            System.out.println("L'escalador amb ID " + id + " no existeix.");
            System.out.println("Vols donar-lo d'alta? (s/n): ");
            String resp = sc.nextLine().trim();
            if (resp.equalsIgnoreCase("s")) {
                String nom = Validacions.llegirTextNoBuit(sc, "Nom: ");
                String alias = Validacions.llegirTextNoBuit(sc, "Àlies: ");
                int edat = Validacions.llegirEnterNoNegatiu(sc, "Edat: ");
                Escalador nou = new Escalador(0, nom, alias, edat, "4", "Esportiva", 0);
                escaladorDAO.insert(nou);
                Escalador creat = escaladorDAO.findByAlias(alias);
                if (creat != null) {
                    System.out.println("Escalador creat amb ID " + creat.getIdEscalador() + ".");
                    return creat.getIdEscalador();
                }
            }
        }
    }

    private void mostrarVia(Via v) {
        System.out.println("\n--- Via #" + v.getIdVia() + " ---");
        System.out.println("Nom:        " + v.getNom());
        System.out.println("Tipus:      " + v.getTipus());
        System.out.println("Grau:       " + v.getGrau());
        System.out.println("Orientació: " + v.getOrientacio());
        System.out.println("Estat:      " + v.getEstat() +
                (v.getDataEstat() != null ? " (fins " + v.getDataEstat() + ")" : ""));
        System.out.println("Ancoratges: " + v.getAncoratges());
        System.out.println("Roca:       " + v.getTipusDeRoca());
        System.out.println("Restricc.:  " + v.getRestriccions());
        System.out.println("Escola ID:  " + v.getIdEscola() + " | Sector ID: " + v.getIdSector());

        if (v instanceof ViaEsportiva ve) {
            System.out.println("Llargada:   " + ve.getLlargada() + "m");
        } else if (v instanceof ViaClassica vc) {
            System.out.println("Ancoratges permesos: " + vc.getAncoratgesPermesos());
            System.out.println("Num. llargs: " + vc.getNumTrams() +
                    " | Llargada total: " + vc.getLlargadaTotal() + "m" +
                    (vc.esValida() ? " (>50m ✓)" : " (<50m, via no completa)"));
        } else if (v instanceof ViaGel vg) {
            System.out.println("Num. llargs: " + vg.getTrams().size() +
                    " | Llargada total: " + vg.getLlargadaTotal() + "m");
        }
    }

    private void mostrarViaResumida(Via v) {
        System.out.printf("  [%3d] %-25s %-10s %-12s %-12s%n",
                v.getIdVia(), v.getNom(), v.getTipus(), v.getGrau(), v.getEstat());
    }
}