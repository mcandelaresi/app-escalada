import java.util.Scanner;

public class Menu {
    private Scanner sc = new Scanner(System.in);

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
                case 1:
                    menuEscoles();
                    break;
                case 2:
                    menuSectors();
                    break;
                case 3:
                    menuVies();
                    break;
                case 4:
                    menuEscaladors();
                    break;
                case 5:
                    menuBusquedas();
                    break;
                case 0:
                    System.out.println("Adeu!");
                    break;
                default:
                    System.out.println("Opció equivocada");
            }
        }
    }

    private void menuEscoles() {
        System.out.println("\n--- ESCOLES ---");
        System.out.println("1. Crear");
        System.out.println("2. Llistar");
        System.out.println("3. Modificar");
        System.out.println("4. Eliminar");
        System.out.print("Opció: ");
        int op = sc.nextInt();
        sc.nextLine();
        
        switch (op) {
            case 1:
                System.out.println(" Crear escola");
                break;
            case 2:
                System.out.println(" Llistar escoles");
                break;
            case 3:
                System.out.println(" Modificar escola");
                break;
            case 4:
                System.out.println(" Eliminar escola");
                break;
        }
    }

    private void menuSectors() {
        System.out.println("\n--- SECTORS ---");
        System.out.println("1. Crear");
        System.out.println("2. Llistar");
        System.out.println("3. Modificar");
        System.out.println("4. Eliminar");
        System.out.print("Opció: ");
        int op = sc.nextInt();
        sc.nextLine();
        
        switch (op) {
            case 1:
                System.out.println(" Crear sector");
                break;
            case 2:
                System.out.println(" Llistar sectors");
                break;
            case 3:
                System.out.println(" Modificar sector");
                break;
            case 4:
                System.out.println(" Eliminar sector");
                break;
        }
    }

    private void menuVies() {
        System.out.println("\n--- VIES ---");
        System.out.println("1. Crear");
        System.out.println("2. Llistar");
        System.out.println("3. Modificar");
        System.out.println("4. Eliminar");
        System.out.print("Opció: ");
        int op = sc.nextInt();
        sc.nextLine();
        
        switch (op) {
            case 1:
                System.out.println(" Crear via");
                break;
            case 2:
                System.out.println(" Llistar vies");
                break;
            case 3:
                System.out.println(" Modificar via");
                break;
            case 4:
                System.out.println(" Eliminar via");
                break;
        }
    }

    private void menuEscaladors() {
        System.out.println("\n--- ESCALADORS ---");
        System.out.println("1. Crear");
        System.out.println("2. Llistar");
        System.out.println("3. Modificar");
        System.out.println("4. Eliminar");
        System.out.print("Opció: ");
        int op = sc.nextInt();
        sc.nextLine();
        
        switch (op) {
            case 1:
                System.out.println(" Crear escalador");
                break;
            case 2:
                System.out.println(" Llistar escaladors");
                break;
            case 3:
                System.out.println(" Modificar escalador");
                break;
            case 4:
                System.out.println(" Eliminar escalador");
                break;
        }
    }

    private void menuBusquedas() {
        System.out.println("\n--- BÚSQUEDAS ---");
        System.out.println("1. Vies d'una escola");
        System.out.println("2. Vies per dificultat");
        System.out.println("3. Vies per estat");
        System.out.println("4. Escoles amb restriccions");
        System.out.println("5. Sectors amb X vies");
        System.out.println("6. Escaladors per nivell");
        System.out.println("7. Vies recents Apte");
        System.out.println("8. Vies més llargues");
        System.out.print("Opció: ");
        int op = sc.nextInt();
        sc.nextLine();
        
        switch (op) {
            case 1:
                System.out.println(" Vies per escola");
                break;
            case 2:
                System.out.println(" Vies per dificultat");
                break;
            case 3:
                System.out.println(" Vies per estat");
                break;
            case 4:
                System.out.println(" Escoles amb restriccions");
                break;
            case 5:
                System.out.println(" Sectors amb X vies");
                break;
            case 6:
                System.out.println(" Escaladors per nivell");
                break;
            case 7:
                System.out.println(" Vies recents Apte");
                break;
            case 8:
                System.out.println(" Vies més llargues");
                break;
        }
    }
}


