package vista.Menu;

import controlador.*;
import dao.sqlite.*;
import excepcions.Validacions;

import java.util.Scanner;

public class Menu {

    private final ViaController viaController;
    private final EscolaController escolaController;
    private final SectorController sectorController;
    private final EscaladorController escaladorController;
    private final CercaController cercaController;

    private final Scanner sc = new Scanner(System.in);

    public Menu() {

        // Els DAOs usen ConnectionDB.getConnection() de forma estàtica,
        // no necessiten rebre la connexió al constructor.
        ViaDAO viaDAO                 = new ViaDAO();
        ViaEsportivaDAO viaEspDAO     = new ViaEsportivaDAO();
        ViaClassicaDAO viaClasDAO     = new ViaClassicaDAO();
        ViaGelDAO viaGelDAO           = new ViaGelDAO();

        EscolaDAO escolaDAO           = new EscolaDAO();
        SectorDAO sectorDAO           = new SectorDAO();

        EscaladorDAO escaladorDAO     = new EscaladorDAO();
        RegistreDAO registreDAO       = new RegistreDAO();

        TramDAO tramDAO               = new TramDAO();

        // Controllers (DI correcte)
        this.viaController = new ViaController(
                viaDAO, viaEspDAO, viaClasDAO, viaGelDAO, escaladorDAO
        );

        this.escolaController = new EscolaController(
                escolaDAO, sectorDAO
        );

        this.sectorController = new SectorController(
                sectorDAO
        );

        this.escaladorController = new EscaladorController(
                escaladorDAO, viaDAO, registreDAO
        );

        this.cercaController = new CercaController(
                viaDAO, escolaDAO, sectorDAO, escaladorDAO, tramDAO
        );
    }

    public void menu() {

        boolean sortir = false;

        while (!sortir) {

            viaController.actualitzarEstatsCaducats();

            System.out.println("╔════════════════════════════════════════╗");
            System.out.println("║     GESTIÓ D'ESCALADA - PILLAM LTD     ║");
            System.out.println("╠════════════════════════════════════════╣");
            System.out.println("║  1. Gestió de Vies                     ║");
            System.out.println("║  2. Gestió d'Escoles                   ║");
            System.out.println("║  3. Gestió de Sectors                  ║");
            System.out.println("║  4. Gestió d'Escaladors                ║");
            System.out.println("║  5. Cerques                            ║");
            System.out.println("║  0. Sortir                             ║");
            System.out.println("╚════════════════════════════════════════╝");

            int opcio = Validacions.llegirOpcio(sc, "Opció: ", 0, 5);

            switch (opcio) {
                case 1 -> new MenuVies(sc, viaController).menu();
                case 2 -> new MenuEscola(sc, escolaController).menu();
                case 3 -> new MenuSector(sc, sectorController).menu();
                case 4 -> new MenuEscaladors(sc, escaladorController).menu();
                case 5 -> new MenuCerca(sc, cercaController).menu();
                case 0 -> sortir = true;
            }
        }

        System.out.println("Fins aviat!");
        sc.close();
    }
}
