import java.util.Scanner;
import java.util.List;
import java.sql.Connection;
import dao.ConnectionDB;
import dao.sqlite.*;
import model.*;
public class Menu {
    private Scanner sc = new Scanner(System.in);
    private EscaladorDAO escaladorDAO;
    private EscolaDAO escolaDAO;
    private SectorDAO sectorDAO;
    public Menu() {
        this.escaladorDAO = new EscaladorDAO();
        this.escolaDAO = new EscolaDAO();
        this.sectorDAO = new SectorDAO();
        Connection conn = ConnectionDB.getConnection();
        if (conn != null) {
            this.escolaDAO.setConnection(conn);
            this.sectorDAO.setConnection(conn);
        }
    }
    public void menu() {
        int opcion = -1;
        while (opcion != 0) {
            System.out.println("\n====== PILLAM LTD ======");
            System.out.println("1. Escoles");
            System.out.println("2. Sectors");
            System.out.println("3. Vies");
            System.out.println("4. Escaladors");
            System.out.println("5. Búsquedas");
            System.out.println("0. Salir");
            System.out.print("Opció: ");
            opcion = sc.nextInt();
            sc.nextLine();
            switch (opcion) {
                case 1: menuEscoles(); break;
                case 2: menuSectors(); break;
                case 3: menuVies(); break;
                case 4: menuEscaladors(); break;
                case 5: menuBusquedas(); break;
                case 0: System.out.println("Adeu!"); break;
                default: System.out.println("Opció equivocada");
            }
        }
    }
    private void menuEscoles() {
        System.out.println("\n--- ESCOLES ---");
        System.out.println("1. Crear\n2. Llistar\n3. Modificar\n4. Eliminar");
        System.out.print("Opció: ");
        int op = sc.nextInt();
        sc.nextLine();
        switch (op) {
            case 1: crearEscola(); break;
            case 2: llistarEscoles(); break;
            case 3: modificarEscola(); break;
            case 4: eliminarEscola(); break;
        }
    }
    private void crearEscola() {
        System.out.print("Nom: "); String nom = sc.nextLine();
        System.out.print("Aproximació: "); String aprox = sc.nextLine();
        System.out.print("Popularitat: "); String pop = sc.nextLine();
        System.out.print("Restriccions: "); String rest = sc.nextLine();
        Escola e = new Escola(0, nom, aprox, pop, rest);
        escolaDAO.insert(e);
        System.out.println("✓ Escola creada!");
    }
    private void llistarEscoles() {
        List<Escola> llista = escolaDAO.findAll();
        if (llista.isEmpty()) { System.out.println("No hi ha escoles"); return; }
        System.out.println("\n--- ESCOLES ---");
        for (Escola e : llista)
            System.out.println("ID:" + e.getIdEscola() + " - " + e.getNom() + " (" + e.getPopularitat() + ")");
    }
    private void modificarEscola() {
        System.out.print("ID: "); int id = sc.nextInt(); sc.nextLine();
        Escola e = escolaDAO.findById(id);
        if (e == null) { System.out.println("No trobada"); return; }
        System.out.print("Nom (" + e.getNom() + "): "); String nom = sc.nextLine();
        if (!nom.isEmpty()) e.setNom(nom);
        escolaDAO.update(e);
        System.out.println("✓ Modificada!");
    }
    private void eliminarEscola() {
        System.out.print("ID: "); int id = sc.nextInt(); sc.nextLine();
        escolaDAO.delete(id);
        System.out.println("✓ Eliminada!");
    }
    private void menuSectors() {
        System.out.println("\n--- SECTORS ---");
        System.out.println("1. Crear\n2. Llistar\n3. Modificar\n4. Eliminar");
        System.out.print("Opció: ");
        int op = sc.nextInt();
        sc.nextLine();
        switch (op) {
            case 1: crearSector(); break;
            case 2: llistarSectors(); break;
            case 3: modificarSector(); break;
            case 4: eliminarSector(); break;
        }
    }
    private void crearSector() {
        System.out.print("Nom: "); String nom = sc.nextLine();
        System.out.print("Latitud: "); double lat = sc.nextDouble();
        System.out.print("Longitud: "); double lon = sc.nextDouble();
        sc.nextLine();
        System.out.print("Aproximació: "); String aprox = sc.nextLine();
        System.out.print("Popularitat: "); String pop = sc.nextLine();
        System.out.print("Restriccions: "); String rest = sc.nextLine();
        System.out.print("ID escola: "); int idEscola = sc.nextInt();
        Sector s = new Sector(0, nom, lat, lon, aprox, pop, rest, idEscola);
        sectorDAO.insert(s);
        System.out.println("✓ Sector creat!");
    }
    private void llistarSectors() {
        List<Sector> llista = sectorDAO.findAll();
        if (llista.isEmpty()) { System.out.println("No hi ha sectors"); return; }
        System.out.println("\n--- SECTORS ---");
        for (Sector s : llista)
            System.out.println("ID:" + s.getIdSector() + " - " + s.getNom() + " (" + s.getPopularitat() + ")");
    }
    private void modificarSector() {
        System.out.print("ID: "); int id = sc.nextInt(); sc.nextLine();
        Sector s = sectorDAO.findById(id);
        if (s == null) { System.out.println("No trobat"); return; }
        System.out.print("Nom (" + s.getNom() + "): "); String nom = sc.nextLine();
        if (!nom.isEmpty()) s.setNom(nom);
        sectorDAO.update(s);
        System.out.println("✓ Modificat!");
    }
    private void eliminarSector() {
        System.out.print("ID: "); int id = sc.nextInt(); sc.nextLine();
        sectorDAO.delete(id);
        System.out.println("✓ Eliminat!");
    }
    private void menuVies() {
        System.out.println("\n--- VIES ---");
        System.out.println("1. Crear\n2. Llistar\n3. Modificar\n4. Eliminar");
        System.out.print("Opció: ");
        int op = sc.nextInt();
        switch (op) {
            case 1: System.out.println("[TODO] Crear"); break;
            case 2: System.out.println("[TODO] Llistar"); break;
            case 3: System.out.println("[TODO] Modificar"); break;
            case 4: System.out.println("[TODO] Eliminar"); break;
        }
    }
    private void menuEscaladors() {
        System.out.println("\n--- ESCALADORS ---");
        System.out.println("1. Crear\n2. Llistar\n3. Modificar\n4. Eliminar");
        System.out.print("Opció: ");
        int op = sc.nextInt();
        sc.nextLine();
        switch (op) {
            case 1: crearEscalador(); break;
            case 2: llistarEscaladors(); break;
            case 3: modificarEscalador(); break;
            case 4: eliminarEscalador(); break;
        }
    }
    private void crearEscalador() {
        System.out.print("Nom: "); String nom = sc.nextLine();
        System.out.print("Alias: "); String alias = sc.nextLine();
        System.out.print("Edat: "); int edat = sc.nextInt();
        sc.nextLine();
        System.out.print("Nivell max: "); String nivell = sc.nextLine();
        System.out.print("Estil: "); String estil = sc.nextLine();
        Escalador e = new Escalador(0, nom, alias, edat, nivell, estil, 0);
        escaladorDAO.insert(e);
        System.out.println("✓ Escalador creat!");
    }
    private void llistarEscaladors() {
        List<Escalador> llista = escaladorDAO.findAll();
        if (llista.isEmpty()) { System.out.println("No hi ha escaladors"); return; }
        System.out.println("\n--- ESCALADORS ---");
        for (Escalador e : llista)
            System.out.println("ID:" + e.getIdEscalador() + " - " + e.getNom() + " (" + e.getAlias() + ") - " + e.getNivellMax());
    }
    private void modificarEscalador() {
        System.out.print("ID: "); int id = sc.nextInt(); sc.nextLine();
        Escalador e = escaladorDAO.findById(id);
        if (e == null) { System.out.println("No trobat"); return; }
        System.out.print("Nom (" + e.getNom() + "): "); String nom = sc.nextLine();
        if (!nom.isEmpty()) e.setNom(nom);
        escaladorDAO.update(e);
        System.out.println("✓ Modificat!");
    }
    private void eliminarEscalador() {
        System.out.print("ID: "); int id = sc.nextInt(); sc.nextLine();
        escaladorDAO.delete(id);
        System.out.println("✓ Eliminat!");
    }
    private void menuBusquedas() {
        System.out.println("\n--- BÚSQUEDAS ---");
        System.out.println("1. Vies per escola\n2. Vies per dificultat\n3. Vies per estat");
        System.out.println("4. Escoles amb restriccions\n5. Sectors amb X vies\n6. Escaladors per nivell");
        System.out.println("7. Vies recents\n8. Vies més llargues");
        System.out.print("Opció: ");
        int op = sc.nextInt();
        sc.nextLine();
        System.out.println("[TODO] Búsqueda " + op);
    }
}
