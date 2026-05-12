package vista.Menu;

import controlador.SectorController;
import excepcions.Validacions;
import model.Sector;

import java.util.Scanner;

public class MenuSector {

    private final Scanner sc;
    private final SectorController controller;

    public MenuSector(Scanner sc, SectorController controller) {
        this.sc = sc;
        this.controller = controller;
    }

    public void menu() {
        boolean sortir = false;
        while (!sortir) {
            System.out.println("\n╔══════════════════════════════╗");
            System.out.println("║      GESTIÓ DE SECTORS       ║");
            System.out.println("╠══════════════════════════════╣");
            System.out.println("║  1. Crear sector             ║");
            System.out.println("║  2. Modificar sector         ║");
            System.out.println("║  3. Veure sector (per ID)    ║");
            System.out.println("║  4. Llistar tots els sectors ║");
            System.out.println("║  5. Eliminar sector          ║");
            System.out.println("║  0. Tornar                   ║");
            System.out.println("╚══════════════════════════════╝");

            int opcio = Validacions.llegirOpcio(sc, "Opció: ", 0, 5);
            switch (opcio) {
                case 1 -> crearSector();
                case 2 -> modificarSector();
                case 3 -> veureSector();
                case 4 -> controller.tots().forEach(s ->
                        System.out.printf("  [%3d] %-20s  Escola: %d  Popularitat: %s%n",
                                s.getIdSector(), s.getNom(), s.getIdEscola(), s.getPopularitat()));
                case 5 -> eliminarSector();
                case 0 -> sortir = true;
            }
        }
    }

    private void crearSector() {
        String nom = excepcions.Validacions.llegirTextNoBuit(sc, "Nom: ");
        double lat = excepcions.Validacions.llegirDouble(sc, "Latitud: ");
        double lon = excepcions.Validacions.llegirDouble(sc, "Longitud: ");
        String aprox = excepcions.Validacions.llegirTextNoBuit(sc, "Aproximació: ");
        System.out.println("Popularitat: 1.Baixa  2.Mitjana  3.Alta");
        int popOpc = excepcions.Validacions.llegirOpcio(sc, "Opció: ", 1, 3);
        String pop = switch (popOpc) { case 1 -> "Baixa"; case 3 -> "Alta"; default -> "Mitjana"; };
        String restr = excepcions.Validacions.llegirTextOpcional(sc, "Restriccions (opcional): ");
        if (restr.isEmpty()) restr = "Cap";
        int idEscola = excepcions.Validacions.llegirEnterNoNegatiu(sc, "ID Escola: ");

        Sector s = new Sector(0, nom, lat, lon, aprox, pop, restr, idEscola);
        if (controller.crearSector(s)) {
            System.out.println("Sector creat correctament.");
        } else {
            System.out.println("Ja existeix un sector amb aquest nom en aquesta escola.");
        }
    }

    private void modificarSector() {
        int id = excepcions.Validacions.llegirEnterNoNegatiu(sc, "ID Sector: ");
        Sector s = controller.buscar(id);
        if (s == null) { System.out.println("Sector no trobat."); return; }

        System.out.println("Sector: " + s.getNom() + " | Escola: " + s.getIdEscola());
        String nom = excepcions.Validacions.llegirTextOpcional(sc, "Nou nom (" + s.getNom() + "): ");
        String aprox = excepcions.Validacions.llegirTextOpcional(sc, "Aproximació (" + s.getAproximacio() + "): ");
        String restr = excepcions.Validacions.llegirTextOpcional(sc, "Restriccions (" + s.getRestriccions() + "): ");
        if (!aprox.isEmpty()) s.setAproximacio(aprox);
        if (!restr.isEmpty()) s.setRestriccions(restr);

        if (controller.modificar(s, nom.isEmpty() ? null : nom)) {
            System.out.println("Sector modificat.");
        } else {
            System.out.println("Ja existeix un sector amb aquest nom en aquesta escola.");
        }
    }

    private void veureSector() {
        int id = excepcions.Validacions.llegirEnterNoNegatiu(sc, "ID Sector: ");
        Sector s = controller.buscar(id);
        if (s == null) { System.out.println("Sector no trobat."); return; }
        System.out.println("\n--- Sector #" + s.getIdSector() + " ---");
        System.out.println("Nom:          " + s.getNom());
        System.out.println("Coordenades:  " + s.getLatitud() + ", " + s.getLongitud());
        System.out.println("Aproximació:  " + s.getAproximacio());
        System.out.println("Popularitat:  " + s.getPopularitat());
        System.out.println("Restriccions: " + s.getRestriccions());
        System.out.println("Escola ID:    " + s.getIdEscola());
    }

    private void eliminarSector() {
        int id = excepcions.Validacions.llegirEnterNoNegatiu(sc, "ID Sector a eliminar: ");
        Sector s = controller.buscar(id);
        if (s == null) { System.out.println("Sector no trobat."); return; }
        System.out.print("Segur? Eliminarà el sector i totes les seves vies. (s/n): ");
        if (!sc.nextLine().trim().equalsIgnoreCase("s")) { System.out.println("Cancel·lat."); return; }
        controller.eliminar(id);
        System.out.println("Sector eliminat.");
    }
}