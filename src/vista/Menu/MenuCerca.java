package vista.Menu;

import controlador.CercaController;
import excepcions.Validacions;

import java.util.Scanner;

public class MenuCerca {

    private final Scanner sc;
    private final CercaController controller;

    public MenuCerca(Scanner sc, CercaController controller) {
        this.sc = sc;
        this.controller = controller;
    }

    public void menu() {
        boolean sortir = false;
        while (!sortir) {
            System.out.println("\n╔══════════════════════════════════════════════════════════╗");
            System.out.println("║                       CERQUES                            ║");
            System.out.println("╠══════════════════════════════════════════════════════════╣");
            System.out.println("║  1. Vies disponibles d'una escola                       ║");
            System.out.println("║  2. Cercar vies per rang de dificultat                  ║");
            System.out.println("║  3. Cercar vies per estat                               ║");
            System.out.println("║  4. Escoles amb restriccions actives                    ║");
            System.out.println("║  5. Sectors amb més de X vies disponibles               ║");
            System.out.println("║  6. Escaladors amb el mateix nivell màxim               ║");
            System.out.println("║  7. Vies que han tornat a Apte recentment               ║");
            System.out.println("║  8. Vies més llargues d'una escola                      ║");
            System.out.println("║  0. Tornar al menú principal                            ║");
            System.out.println("╚══════════════════════════════════════════════════════════╝");

            int opcio = Validacions.llegirOpcio(sc, "Opció: ", 0, 8);
            switch (opcio) {
                case 1 -> controller.viesDisponiblesEscola(sc);
                case 2 -> controller.viesPerDificultat(sc);
                case 3 -> controller.viesPerEstat(sc);
                case 4 -> controller.escolesAmbRestriccions(sc);
                case 5 -> controller.sectorsAmbMesDeXVies(sc);
                case 6 -> controller.escaladorsMateixNivell(sc);
                case 7 -> controller.viesAptesRecentment(sc);
                case 8 -> controller.viesMesLlarguesEscola(sc);
                case 0 -> sortir = true;
            }
        }
    }
}