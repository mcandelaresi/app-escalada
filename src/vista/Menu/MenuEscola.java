package vista.Menu;

import controlador.EscolaController;
import excepcions.Validacions;

import java.util.Scanner;

public class MenuEscola {

    private final Scanner sc;
    private final EscolaController controller;

    public MenuEscola(Scanner sc, EscolaController controller) {
        this.sc = sc;
        this.controller = controller;
    }

    public void menu() {
        boolean sortir = false;
        while (!sortir) {
            System.out.println("\n╔══════════════════════════════╗");
            System.out.println("║      GESTIÓ D'ESCOLES        ║");
            System.out.println("╠══════════════════════════════╣");
            System.out.println("║  1. Crear escola             ║");
            System.out.println("║  2. Modificar escola         ║");
            System.out.println("║  3. Veure escola (per ID)    ║");
            System.out.println("║  4. Llistar totes les escoles║");
            System.out.println("║  5. Eliminar escola          ║");
            System.out.println("║  0. Tornar                   ║");
            System.out.println("╚══════════════════════════════╝");

            int opcio = Validacions.llegirOpcio(sc, "Opció: ", 0, 5);
            switch (opcio) {
                case 1 -> controller.crearEscola(sc);
                case 2 -> controller.modificarEscola(sc);
                case 3 -> controller.llistarUna(sc);
                case 4 -> controller.llistarTotes();
                case 5 -> controller.eliminarEscola(sc);
                case 0 -> sortir = true;
            }
        }
    }
}