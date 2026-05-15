package vista.Menu;

import controlador.ViaController;
import excepcions.Validacions;

import java.util.Scanner;

public class MenuVies {

    private final Scanner sc;
    private final ViaController controller;

    public MenuVies(Scanner sc, ViaController controller) {
        this.sc = sc;
        this.controller = controller;
    }

    public void menu() {
        boolean sortir = false;
        while (!sortir) {
            System.out.println("╔══════════════════════════════╗");
            System.out.println("║       GESTIÓ DE VIES         ║");
            System.out.println("╠══════════════════════════════╣");
            System.out.println("║  1. Crear via                ║");
            System.out.println("║  2. Modificar via            ║");
            System.out.println("║  3. Veure via (per ID)       ║");
            System.out.println("║  4. Llistar totes les vies   ║");
            System.out.println("║  5. Eliminar via             ║");
            System.out.println("║  0. Tornar                   ║");
            System.out.println("╚══════════════════════════════╝");

            int opcio = Validacions.llegirOpcio(sc, "Opció: ", 0, 5);
            switch (opcio) {
                case 1 -> controller.crearVia(sc);
                case 2 -> controller.modificarVia(sc);
                case 3 -> controller.llistarUna(sc);
                case 4 -> controller.llistarTotes();
                case 5 -> controller.eliminarVia(sc);
                case 0 -> sortir = true;
            }
        }
    }
}