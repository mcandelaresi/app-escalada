   package vista.Menu;

   import controlador.*;
   import dao.ConnectionDB;
   import dao.sqlite.*;
   import excepcions.Validacions;
   import vista.Vista;

   import java.sql.Connection;
   import java.util.Scanner;

   public class Menu {

       Connection conn = ConnectionDB.getConnection();
       private final Scanner sc = new Scanner(System.in);

       private final MenuEscola menuEscola;
       private final MenuSector menuSector;
       private final MenuVies menuVia;
       private final MenuEscaladors menuEscalador;
       private final MenuCerca menuCerca;

       public Menu() {
           // Instancia los DAOs (capa de acceso a datos)
           ViaDAO viaDAO = new ViaDAO();
           EscolaDAO escolaDAO = new EscolaDAO();
           SectorDAO sectorDAO = new SectorDAO();
           EscaladorDAO escaladorDAO = new EscaladorDAO();
           ViaEsportivaDAO viaEsportivaDAO = new ViaEsportivaDAO();
           ViaClassicaDAO viaClassicaDAO = new ViaClassicaDAO(conn);
           ViaGelDAO viaGelDAO = new ViaGelDAO();

           // Instancia controladores
           EscolaController escolaController = new EscolaController();  // Este no necesita DAO
           SectorController sectorController = new SectorController(sectorDAO);
           ViaController viaController = new ViaController(viaDAO, viaEsportivaDAO, viaClassicaDAO, viaGelDAO, escolaDAO, sectorDAO, escaladorDAO);
           EscaladorController escaladorController = new EscaladorController(escaladorDAO, viaDAO);
           CercaController cercaController = new CercaController(viaDAO, escolaDAO, sectorDAO, escaladorDAO, viaEsportivaDAO, viaClassicaDAO, viaGelDAO);

           // Instancia menús con controladores
           menuEscola = new MenuEscola(sc, escolaController);
           menuSector = new MenuSector(sc, sectorController);
           menuVia = new MenuVies(sc, viaController);
           menuEscalador = new MenuEscaladors(sc, escaladorController);
           menuCerca = new MenuCerca(sc, cercaController);
       }

       public void menu() {
           int op;

           Vista.intro();

           do {
               menuVia.actualitzarEstatsCaducats();

               Vista.menuPrincipal();

               op = Validacions.llegirOpcio(sc, "Opció: ", 0, 5);

               switch (op) {
                   case 1 -> menuEscola.menu();
                   case 2 -> menuSector.menu();
                   case 3 -> menuVia.menu();
                   case 4 -> menuEscalador.menu();
                   case 5 -> menuCerca.menu();
                   case 0 -> System.out.println("Adeu!");
               }

           } while (op != 0);
       }
   }