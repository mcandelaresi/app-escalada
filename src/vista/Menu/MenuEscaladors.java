package vista.Menu;

import controlador.EscaladorController;
import excepcions.Validacions;

import java.util.Scanner;

public class MenuEscaladors {

    private final Scanner sc;
    private final EscaladorController controller;

    public MenuEscaladors(Scanner sc, EscaladorController controller) {
        this.sc = sc;
        this.controller = controller;
    }

    public void menu() {
        boolean sortir = false;
        while (!sortir) {
            System.out.println("\n╔══════════════════════════════════╗");
            System.out.println("║      GESTIÓ D'ESCALADORS         ║");
            System.out.println("╠══════════════════════════════════╣");
            System.out.println("║  1. Crear escalador              ║");
            System.out.println("║  2. Modificar escalador          ║");
            System.out.println("║  3. Veure escalador (per ID)     ║");
            System.out.println("║  4. Llistar tots els escaladors  ║");
            System.out.println("║  5. Eliminar escalador           ║");
            System.out.println("║  6. Registrar ascensió           ║");
            System.out.println("║  0. Tornar                       ║");
            System.out.println("╚══════════════════════════════════╝");

            int opcio = Validacions.llegirOpcio(sc, "Opció: ", 0, 6);
            switch (opcio) {
                case 1 -> controller.crear(sc);
                case 2 -> controller.modificar(sc);
                case 3 -> controller.llistarUn(sc);
                case 4 -> controller.llistarTots();
                case 5 -> controller.eliminar(sc);
                case 6 -> controller.registrarAscensio(sc);
                case 0 -> sortir = true;
            }
        }
    }
}