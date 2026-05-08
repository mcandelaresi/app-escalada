package vista.Menu;

import controlador.SectorController;
import excepcions.Validacions;
import model.Sector;
import model.enums.Popularitat;
import vista.Vista;

import java.util.List;
import java.util.Scanner;

public class MenuSector {

    private final Scanner sc;
    private final SectorController controller;

    public MenuSector(Scanner sc, SectorController controller) {
        this.sc = sc;
        this.controller = controller;
    }

    public void menu() {

        int op;

        do {
            Vista.menuSectors();

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

        String nom = Validacions.llegirTextNoBuit(sc, "Nom: ");
        double lat = Validacions.llegirDouble(sc, "Latitud: ");
        double lon = Validacions.llegirDouble(sc, "Longitud: ");
        String aprox = Validacions.llegirTextNoBuit(sc, "Aproximació: ");
        String rest = Validacions.llegirTextNoBuit(sc, "Restriccions: ");
        int idEscola = Validacions.llegirEnterNoNegatiu(sc, "ID escola: ");

        String pop;
        while (true) {
            pop = Validacions.llegirTextNoBuit(sc, "Popularitat: ");
            if (Popularitat.fromValor(pop) != null) break;
            System.out.println("No vàlida");
        }

        Sector s = new Sector(0, nom, lat, lon, aprox, pop, rest, idEscola);

        if (!controller.crearSector(s)) {
            System.out.println("Ja existeix un sector amb aquest nom a l’escola.");
            return;
        }

        System.out.println("Sector creat");
    }

    private void un() {

        Sector s = controller.buscar(
                Validacions.llegirEnterNoNegatiu(sc, "ID: ")
        );

        if (s == null) {
            System.out.println("No trobat");
            return;
        }

        System.out.println("ID: " + s.getIdSector());
        System.out.println("Nom: " + s.getNom());
        System.out.println("Lat: " + s.getLatitud());
        System.out.println("Lon: " + s.getLongitud());
        System.out.println("Aprox: " + s.getAproximacio());
        System.out.println("Pop: " + s.getPopularitat());
        System.out.println("Restriccions: " + s.getRestriccions());
        System.out.println("Escola: " + s.getIdEscola());
        System.out.println("Vies: " + s.getNumeroVies());
    }

    private void tots() {

        List<Sector> list = controller.tots();

        if (list.isEmpty()) {
            System.out.println("No hi ha sectors");
            return;
        }

        list.forEach(s ->
                System.out.println(s.getIdSector() + " - " + s.getNom() +
                        " | escola=" + s.getIdEscola() +
                        " | " + s.getPopularitat())
        );
    }

    private void modificar() {

        int id = Validacions.llegirEnterNoNegatiu(sc, "ID: ");
        Sector s = controller.buscar(id);

        if (s == null) {
            System.out.println("No trobat");
            return;
        }

        String nom = Validacions.llegirTextOpcional(sc, "Nom (" + s.getNom() + "): ");
        String lat = Validacions.llegirTextOpcional(sc, "Lat (" + s.getLatitud() + "): ");
        String lon = Validacions.llegirTextOpcional(sc, "Lon (" + s.getLongitud() + "): ");
        String aprox = Validacions.llegirTextOpcional(sc, "Aprox (" + s.getAproximacio() + "): ");
        String pop = Validacions.llegirTextOpcional(sc, "Pop (" + s.getPopularitat() + "): ");
        String rest = Validacions.llegirTextOpcional(sc, "Rest (" + s.getRestriccions() + "): ");

        if (!nom.isEmpty()) {
            if (!controller.modificar(s, nom)) {
                System.out.println("Nom duplicat dins l’escola");
                return;
            }
        }

        if (!lat.isEmpty()) s.setLatitud(parseDouble(lat, s.getLatitud()));
        if (!lon.isEmpty()) s.setLongitud(parseDouble(lon, s.getLongitud()));
        if (!aprox.isEmpty()) s.setAproximacio(aprox);
        if (!pop.isEmpty()) s.setPopularitat(controller.normalitzarPopularitat(pop));
        if (!rest.isEmpty()) s.setRestriccions(rest);

        controller.modificar(s, s.getNom());

        System.out.println("Modificat");
    }

    private void eliminar() {

        controller.eliminar(
                Validacions.llegirEnterNoNegatiu(sc, "ID: ")
        );

        System.out.println("Eliminat");
    }

    private double parseDouble(String valor, double def) {
        try {
            return Double.parseDouble(valor.trim());
        } catch (Exception e) {
            return def;
        }
    }
}