package vista.Menu;

import dao.sqlite.EscaladorDAO;
import dao.sqlite.ViaDAO;
import excepcions.Validacions;
import helpers.AuxEscalador;
import model.Escalador;
import model.enums.GrauDificultat;
import vista.Vista;

import java.util.List;
import java.util.Scanner;

public class MenuEscaladors {

    private final Scanner sc;
    private final EscaladorDAO dao;
    private final ViaDAO viaDAO;

    public MenuEscaladors(Scanner sc, EscaladorDAO dao, ViaDAO viaDAO) {
        this.sc = sc;
        this.dao = dao;
        this.viaDAO = viaDAO;
    }

    public void menu() {

        int op;

        do {
            Vista.menuEscaladors();

            op = Validacions.llegirOpcio(sc, "Opció: ", 0, 5);

            switch (op) {
                case 1 -> crear();
                case 2 -> un();
                case 3 -> tots();
                case 4 -> modificar();
                case 5 -> eliminar();
            }

        } while (op != 0);
    }

    private void crear() {

        Escalador e = new Escalador(
                0,
                Validacions.llegirTextNoBuit(sc, "Nom: "),
                Validacions.llegirTextNoBuit(sc, "Àlies: "),
                Validacions.llegirEnterNoNegatiu(sc, "Edat: "),
                llegirGrauMaxim(),
                llegirEstilPreferit(),
                llegirViaMax()
        );

        dao.insert(e);
        System.out.println(" Creat amb ID " + e.getIdEscalador());
    }

    private void un() {

        Escalador e = dao.findById(
                Validacions.llegirEnterNoNegatiu(sc, "ID: ")
        );

        if (e == null) {
            System.out.println("No trobat");
            return;
        }

        System.out.println(e);
    }

    private void tots() {

        List<Escalador> list = dao.findAll();

        if (list.isEmpty()) {
            System.out.println("Sense escaladors");
            return;
        }

        list.forEach(e ->
                System.out.println(e.getIdEscalador() + " - " + e.getNom() + " (" + e.getAlias() + ")")
        );
    }

    private void modificar() {

        Escalador e = dao.findById(
                Validacions.llegirEnterNoNegatiu(sc, "ID: ")
        );

        if (e == null) {
            System.out.println("No trobat");
            return;
        }

        String nom = Validacions.llegirTextOpcional(sc, "Nom: ");
        if (!nom.isEmpty()) e.setNom(nom);

        String alias = Validacions.llegirTextOpcional(sc, "Àlies: ");
        if (!alias.isEmpty()) e.setAlias(alias);

        String edat = Validacions.llegirTextOpcional(sc, "Edat: ");
        if (!edat.isEmpty()) e.setEdat(parseEnter(edat, e.getEdat()));

        String nivell = Validacions.llegirTextOpcional(sc, "Nivell màxim: ");
        if (!nivell.isEmpty()) e.setNivellMax(AuxEscalador.normalitzarGrau(nivell));

        String estil = Validacions.llegirTextOpcional(sc, "Estil preferit: ");
        if (!estil.isEmpty()) e.setEstilPreferit(AuxEscalador.normalitzarEstil(estil));

        String viaMax = Validacions.llegirTextOpcional(sc, "ID via màxima: ");
        if (!viaMax.isEmpty()) {
            int idVia = parseEnter(viaMax, e.getIdViaMax());
            if (idVia != 0 && viaDAO.findById(idVia) == null) {
                System.out.println("La via no existeix.");
                return;
            }
            e.setIdViaMax(idVia);
        }

        dao.update(e);
        System.out.println(" Modificat");
    }

    private void eliminar() {
        dao.delete(
                Validacions.llegirEnterNoNegatiu(sc, "ID: ")
        );
        System.out.println(" Eliminat");
    }

    private String llegirGrauMaxim() {
        while (true) {
            String grau = Validacions.llegirTextNoBuit(sc, "Nivell màxim: ");
            if (GrauDificultat.fromValor(grau) != null) {
                return AuxEscalador.normalitzarGrau(grau);
            }
            System.out.println("Grau no vàlid.");
        }
    }

    private String llegirEstilPreferit() {
        while (true) {
            String estil = Validacions.llegirTextNoBuit(sc, "Estil preferit: ");
            if (AuxEscalador.estilValid(estil)) {
                return AuxEscalador.normalitzarEstil(estil);
            }
            System.out.println("Estil no vàlid.");
        }
    }

    private int llegirViaMax() {
        while (true) {
            int id = Validacions.llegirEnterNoNegatiu(sc, "ID via màxima (0 si no n'hi ha): ");
            if (id == 0 || viaDAO.findById(id) != null) {
                return id;
            }
            System.out.println("La via no existeix.");
        }
    }

    private int parseEnter(String valor, int perDefecte) {
        try {
            return Integer.parseInt(valor.trim());
        } catch (NumberFormatException e) {
            return perDefecte;
        }
    }
}