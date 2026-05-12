package controlador;

import dao.sqlite.EscaladorDAO;
import dao.sqlite.RegistreDAO;
import dao.sqlite.ViaDAO;
import excepcions.Validacions;
import model.Escalador;
import model.Registre;
import model.Via;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class EscaladorController {

    private final EscaladorDAO dao;
    private final ViaDAO viaDAO;
    private final RegistreDAO registreDAO;

    // Graus vàlids (fins 9c+)
    private static final String[] GRAUS = {
            "4","4+","5","5+","6a","6a+","6b","6b+","6c","6c+",
            "7a","7a+","7b","7b+","7c","7c+","8a","8a+","8b","8b+",
            "8c","8c+","9a","9a+","9b","9b+","9c","9c+"
    };

    public EscaladorController(EscaladorDAO dao, ViaDAO viaDAO, RegistreDAO registreDAO) {
        this.dao = dao;
        this.viaDAO = viaDAO;
        this.registreDAO = registreDAO;
    }

    public void crear(Scanner sc) {
        String nom = Validacions.llegirTextNoBuit(sc, "Nom: ");
        String alias = llegirAliasUnic(sc);
        int edat = Validacions.llegirEnterNoNegatiu(sc, "Edat: ");
        String nivellMax = llegirGrauValid(sc, "Nivell màxim assolit: ");
        String estil = llegirEstil(sc);

        // Via on ha assolit el nivell màxim (opcional)
        int idViaMax = 0;
        System.out.print("ID de la via on ha assolit el nivell màxim (0 si no consta): ");
        try {
            idViaMax = Integer.parseInt(sc.nextLine().trim());
            if (idViaMax != 0 && viaDAO.findById(idViaMax) == null) {
                System.out.println("La via no existeix, es deixa buit.");
                idViaMax = 0;
            }
        } catch (NumberFormatException e) {
            idViaMax = 0;
        }

        Escalador e = new Escalador(0, nom, alias, edat, nivellMax, estil, idViaMax);
        dao.insert(e);
        System.out.println("Escalador '" + nom + "' creat correctament.");
    }

    public void modificar(Scanner sc) {
        int id = Validacions.llegirEnterNoNegatiu(sc, "ID Escalador: ");
        Escalador e = dao.findById(id);
        if (e == null) {
            System.out.println("Escalador no trobat.");
            return;
        }
        mostrarEscalador(e);
        System.out.println("Deixa en blanc per mantenir el valor actual.");

        String nom = Validacions.llegirTextOpcional(sc, "Nom (" + e.getNom() + "): ");
        if (!nom.isEmpty()) e.setNom(nom);

        String alias = Validacions.llegirTextOpcional(sc, "Àlies (" + e.getAlias() + "): ");
        if (!alias.isEmpty()) {
            Escalador amb = dao.findByAlias(alias);
            if (amb != null && amb.getIdEscalador() != e.getIdEscalador()) {
                System.out.println("Ja existeix un escalador amb aquest àlies.");
            } else {
                e.setAlias(alias);
            }
        }

        String edat = Validacions.llegirTextOpcional(sc, "Edat (" + e.getEdat() + "): ");
        if (!edat.isEmpty()) {
            try { e.setEdat(Integer.parseInt(edat)); } catch (NumberFormatException ignored) {}
        }

        String nivell = Validacions.llegirTextOpcional(sc, "Nivell màxim (" + e.getNivellMax() + "): ");
        if (!nivell.isEmpty() && grauValid(nivell)) e.setNivellMax(nivell);

        dao.update(e);
        System.out.println("Escalador modificat correctament.");
    }

    public void llistarUn(Scanner sc) {
        int id = Validacions.llegirEnterNoNegatiu(sc, "ID Escalador: ");
        Escalador e = dao.findById(id);
        if (e == null) {
            System.out.println("Escalador no trobat.");
            return;
        }
        mostrarEscalador(e);

        // Mostrar historial d'ascensions
        List<Registre> historial = registreDAO.findByEscalador(id);
        if (historial.isEmpty()) {
            System.out.println("  Historial: cap ascensió registrada.");
        } else {
            System.out.println("  Historial d'ascensions (" + historial.size() + "):");
            for (Registre r : historial) {
                Via v = viaDAO.findById(r.getIdVia());
                String nomVia = v != null ? v.getNom() : "ID:" + r.getIdVia();
                System.out.printf("    [%s] Via: %-20s  Estil: %s%n",
                        r.getDataAscensio(), nomVia, r.getEstil());
            }
        }
    }

    public void llistarTots() {
        List<Escalador> tots = dao.findAll();
        if (tots.isEmpty()) {
            System.out.println("No hi ha escaladors registrats.");
            return;
        }
        System.out.println("\n=== TOTS ELS ESCALADORS ===");
        for (Escalador e : tots) {
            String nomVia = "";
            if (e.getIdViaMax() > 0) {
                Via v = viaDAO.findById(e.getIdViaMax());
                nomVia = v != null ? " @ " + v.getNom() : " @ Via ID:" + e.getIdViaMax();
            }
            System.out.printf("  [%3d] %-25s  Àlies: %-15s  Nivell: %-6s%s%n",
                    e.getIdEscalador(), e.getNom(), e.getAlias(), e.getNivellMax(), nomVia);
        }
        System.out.println("Total: " + tots.size() + " escaladors.");
    }

    public void eliminar(Scanner sc) {
        int id = Validacions.llegirEnterNoNegatiu(sc, "ID Escalador a eliminar: ");
        Escalador e = dao.findById(id);
        if (e == null) {
            System.out.println("Escalador no trobat.");
            return;
        }
        System.out.print("Segur que vols eliminar '" + e.getNom() + "'? (s/n): ");
        if (!sc.nextLine().trim().equalsIgnoreCase("s")) {
            System.out.println("Operació cancel·lada.");
            return;
        }
        dao.delete(id);
        System.out.println("Escalador eliminat.");
    }

    public void registrarAscensio(Scanner sc) {
        int idEscalador = Validacions.llegirEnterNoNegatiu(sc, "ID Escalador: ");
        if (dao.findById(idEscalador) == null) {
            System.out.println("Escalador no trobat.");
            return;
        }
        int idVia = Validacions.llegirEnterNoNegatiu(sc, "ID Via: ");
        if (viaDAO.findById(idVia) == null) {
            System.out.println("Via no trobada.");
            return;
        }
        String data = LocalDate.now().toString();
        System.out.print("Data d'ascensió [YYYY-MM-DD] (" + data + "): ");
        String dataInput = sc.nextLine().trim();
        if (!dataInput.isEmpty()) data = dataInput;
        String estil = llegirEstil(sc);

        Registre r = new Registre(0, idEscalador, idVia, data, estil);
        registreDAO.insert(r);
        System.out.println("Ascensió registrada correctament.");
    }

    // Accessors
    public Escalador buscarPorId(int id) { return dao.findById(id); }
    public List<Escalador> buscarTodos() { return dao.findAll(); }
    public void actualizar(Escalador e) { dao.update(e); }

    // Helpers
    private String llegirAliasUnic(Scanner sc) {
        while (true) {
            String alias = Validacions.llegirTextNoBuit(sc, "Àlies (únic): ");
            if (dao.findByAlias(alias) == null) return alias;
            System.out.println("Aquest àlies ja existeix.");
        }
    }

    private String llegirGrauValid(Scanner sc, String missatge) {
        while (true) {
            String grau = Validacions.llegirTextNoBuit(sc, missatge);
            if (grauValid(grau)) return grau;
            System.out.println("Grau no vàlid. Rang: 4 a 9c+");
        }
    }

    private boolean grauValid(String grau) {
        for (String g : GRAUS) { if (g.equalsIgnoreCase(grau)) return true; }
        return false;
    }

    private String llegirEstil(Scanner sc) {
        System.out.println("Estil: 1.Esportiva  2.Classica  3.Gel");
        int opc = Validacions.llegirOpcio(sc, "Opció: ", 1, 3);
        return switch (opc) { case 1 -> "Esportiva"; case 2 -> "Classica"; default -> "Gel"; };
    }

    private void mostrarEscalador(Escalador e) {
        System.out.println("\n--- Escalador #" + e.getIdEscalador() + " ---");
        System.out.println("Nom:          " + e.getNom());
        System.out.println("Àlies:        " + e.getAlias());
        System.out.println("Edat:         " + e.getEdat());
        System.out.println("Nivell màx.:  " + e.getNivellMax());
        System.out.println("Estil:        " + e.getEstilPreferit());
        if (e.getIdViaMax() > 0) {
            Via v = viaDAO.findById(e.getIdViaMax());
            String nomVia = v != null ? v.getNom() + " (ID:" + v.getIdVia() + ")" : "ID:" + e.getIdViaMax();
            System.out.println("Via max.:     " + nomVia);
        }
    }
}