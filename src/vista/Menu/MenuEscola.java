package vista.Menu;

import dao.sqlite.EscolaDAO;
import excepcions.Validacions;
import helpers.AuxEscola;
import model.Escola;
import model.enums.Popularitat;
import vista.Vista;

import java.util.List;
import java.util.Scanner;

public class MenuEscola {

    private final Scanner sc;
    private final EscolaDAO escolaDAO;

    public MenuEscola(Scanner sc, EscolaDAO escolaDAO) {
        this.sc = sc;
        this.escolaDAO = escolaDAO;
    }

    public void menu() {
        int op;

        do {
            Vista.menuEscoles();
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
        if (escolaDAO.findByNom(nom) != null) {
            System.out.println("Ja existeix una escola amb aquest nom.");
            return;
        }

        String aprox = Validacions.llegirTextNoBuit(sc, "Aproximació: ");
        String pop = llegirPopularitat();
        String rest = Validacions.llegirTextNoBuit(sc, "Restriccions: ");

        Escola e = new Escola(0, nom, aprox, pop, rest);
        escolaDAO.insert(e);
        System.out.println("Escola creada amb ID " + e.getIdEscola());
    }

    private void llistarUna() {
        Escola e = escolaDAO.findById(Validacions.llegirEnterNoNegatiu(sc, "ID: "));
        if (e == null) {
            System.out.println("No trobada");
            return;
        }

        System.out.println("ID: " + e.getIdEscola());
        System.out.println("Nom: " + e.getNom());
        System.out.println("Aproximació: " + e.getAproximacio());
        System.out.println("Popularitat: " + e.getPopularitat());
        System.out.println("Restriccions: " + e.getRestriccions());
        System.out.println("Número de vies: " + e.getNumVies());
    }

    private void llistarTotes() {
        List<Escola> llista = escolaDAO.findAll();
        if (llista.isEmpty()) {
            System.out.println("No hi ha escoles");
            return;
        }

        for (Escola e : llista) {
            System.out.println(e.getIdEscola() + " - " + e.getNom() + " | " + e.getPopularitat());
        }
    }

    private void modificar() {
        int id = Validacions.llegirEnterNoNegatiu(sc, "ID: ");
        Escola e = escolaDAO.findById(id);

        if (e == null) {
            System.out.println("No trobada");
            return;
        }

        String nom = Validacions.llegirTextOpcional(sc, "Nom (" + e.getNom() + "): ");
        if (!nom.isEmpty()) {
            Escola existent = escolaDAO.findByNom(nom);
            if (existent != null && existent.getIdEscola() != e.getIdEscola()) {
                System.out.println("Ja existeix una escola amb aquest nom.");
                return;
            }
            e.setNom(nom);
        }

        String aprox = Validacions.llegirTextOpcional(sc, "Aproximació (" + e.getAproximacio() + "): ");
        if (!aprox.isEmpty()) e.setAproximacio(aprox);

        String pop = Validacions.llegirTextOpcional(sc, "Popularitat (" + e.getPopularitat() + "): ");
        if (!pop.isEmpty()) e.setPopularitat(normalitzarPopularitat(pop));

        String rest = Validacions.llegirTextOpcional(sc, "Restriccions (" + e.getRestriccions() + "): ");
        if (!rest.isEmpty()) e.setRestriccions(rest);

        escolaDAO.update(e);
        System.out.println("Escola modificada");
    }

    private void eliminar() {
        escolaDAO.delete(Validacions.llegirEnterNoNegatiu(sc, "ID: "));
        System.out.println("Escola eliminada");
    }

    private String llegirPopularitat() {
        while (true) {
            String pop = Validacions.llegirTextNoBuit(sc, "Popularitat (Baixa/Mitjana/Alta): ");
            if (Popularitat.fromValor(pop) != null) {
                return normalitzarPopularitat(pop);
            }
            System.out.println("Popularitat no vàlida.");
        }
    }

    private String normalitzarPopularitat(String valor) {
        return AuxEscola.normalitzarPopularitat(valor);
    }
}