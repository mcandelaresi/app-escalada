package vista.Menu;

import controlador.EscolaController;
import excepcions.Validacions;
import model.Escola;
import model.enums.Popularitat;
import vista.Vista;

import java.util.List;
import java.util.Scanner;

public class MenuEscola {

    private final Scanner sc;
    private final EscolaController controller;

    public MenuEscola(Scanner sc, EscolaController controller) {
        this.sc = sc;
        this.controller = controller;
    }

    public void menu() {

        int op;

        do {
            Vista.menuEscoles();

            op = Validacions.llegirOpcio(sc, "Opció: ", 0, 5);

            switch (op) {
                case 1 -> crear();
                case 2 -> una();
                case 3 -> totes();
                case 4 -> modificar();
                case 5 -> eliminar();
            }

        } while (op != 0);
    }

    private void crear() {

        String nom = Validacions.llegirTextNoBuit(sc, "Nom: ");
        String aprox = Validacions.llegirTextNoBuit(sc, "Aproximació: ");
        String rest = Validacions.llegirTextNoBuit(sc, "Restriccions: ");

        String pop;
        while (true) {
            pop = Validacions.llegirTextNoBuit(sc, "Popularitat: ");
            if (Popularitat.fromValor(pop) != null) break;
            System.out.println("No vàlida");
        }

        Escola e = new Escola(0, nom, aprox, pop, rest);

        if (!controller.crearEscola(e)) {
            System.out.println("Ja existeix una escola amb aquest nom.");
            return;
        }

        System.out.println("Escola creada");
    }

    private void una() {

        Escola e = controller.buscarPerId(
                Validacions.llegirEnterNoNegatiu(sc, "ID: ")
        );

        if (e == null) {
            System.out.println("No trobada");
            return;
        }

        System.out.println("ID: " + e.getIdEscola());
        System.out.println("Nom: " + e.getNom());
        System.out.println("Aproximació: " + e.getAproximacio());
        System.out.println("Popularitat: " + e.getPopularitat());
        System.out.println("Restriccions: " + e.getRestriccions());
        System.out.println("Vies: " + e.getNumVies());
    }

    private void totes() {

        List<Escola> list = controller.totes();

        if (list.isEmpty()) {
            System.out.println("No hi ha escoles");
            return;
        }

        list.forEach(e ->
                System.out.println(e.getIdEscola() + " - " + e.getNom() + " | " + e.getPopularitat())
        );
    }

    private void modificar() {

        int id = Validacions.llegirEnterNoNegatiu(sc, "ID: ");
        Escola e = controller.buscarPerId(id);

        if (e == null) {
            System.out.println("No trobada");
            return;
        }

        String nom = Validacions.llegirTextOpcional(sc, "Nom (" + e.getNom() + "): ");
        String aprox = Validacions.llegirTextOpcional(sc, "Aprox (" + e.getAproximacio() + "): ");
        String pop = Validacions.llegirTextOpcional(sc, "Pop (" + e.getPopularitat() + "): ");
        String rest = Validacions.llegirTextOpcional(sc, "Restriccions (" + e.getRestriccions() + "): ");

        if (!nom.isEmpty()) e.setNom(nom);
        if (!aprox.isEmpty()) e.setAproximacio(aprox);
        if (!pop.isEmpty()) e.setPopularitat(controller.normalitzarPopularitat(pop));
        if (!rest.isEmpty()) e.setRestriccions(rest);

        if (!controller.modificar(e, nom)) {
            System.out.println("Nom duplicat");
            return;
        }

        System.out.println("Modificada");
    }

    private void eliminar() {

        controller.eliminar(
                Validacions.llegirEnterNoNegatiu(sc, "ID: ")
        );

        System.out.println("Eliminada");
    }
}