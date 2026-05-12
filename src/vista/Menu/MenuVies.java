package vista.Menu;

import controlador.ViaController;
import excepcions.Validacions;
import model.Via;
import vista.Vista;

import java.util.List;
import java.util.Scanner;

public class MenuVies {

    private final Scanner sc;
    private final ViaController controller;

    public MenuVies(Scanner sc, ViaController controller) {
        this.sc = sc;
        this.controller = controller;
    }

    public void actualitzarEstatsCaducats() {
        controller.actualitzarEstatsCaducats();
    }

    public void menu() {

        int op;

        do {
            Vista.menuVies();
            op = Validacions.llegirOpcio(sc, "Opcio: ", 0, 5);

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
        controller.crearVia(sc);
    }

    private void llistarUna() {

        Via v = controller.buscar(
                Validacions.llegirEnterNoNegatiu(sc, "ID: ")
        );

        if (v == null) {
            System.out.println("No trobada");
            return;
        }

        mostrar(v);
    }

    private void llistarTotes() {

        List<Via> list = controller.totes();

        if (list.isEmpty()) {
            System.out.println("Sense vies");
            return;
        }

        list.forEach(this::mostrar);
    }

    private void modificar() {
        controller.modificarVia(sc);
    }

    private void eliminar() {

        Via v = controller.buscar(
                Validacions.llegirEnterNoNegatiu(sc, "ID: ")
        );

        if (v == null) {
            System.out.println("No trobada");
            return;
        }

        controller.eliminar(v);
        System.out.println("Eliminada");
    }

    private void mostrar(Via v) {
        System.out.println(
                v.getIdVia() + " - " + v.getNom() +
                        " | " + v.getTipus() +
                        " | " + v.getGrau() +
                        " | " + v.getEstat()
        );
    }
}