package vista;

public class Vista {

    public static void intro() {
        System.out.println("-----------------------------");
        System.out.println("----------BENVINGUT----------");
        System.out.println("-----------------------------");
    }

    public static void menuPrincipal() {
        System.out.println("\n====== PILLAM LTD ======");
        System.out.println("1. Escoles");
        System.out.println("2. Sectors");
        System.out.println("3. Vies");
        System.out.println("4. Escaladors");
        System.out.println("5. Cerques");
        System.out.println("0. Sortir");
    }

    public static void menuEscoles() {
        System.out.println("\n--- ESCOLES ---");
        System.out.println("1. Crear");
        System.out.println("2. Llistar un");
        System.out.println("3. Llistar tots");
        System.out.println("4. Modificar");
        System.out.println("5. Eliminar");
        System.out.println("0. Tornar enrere");
    }

    public static void menuSectors() {
        System.out.println("\n--- SECTORS ---");
        System.out.println("1. Crear");
        System.out.println("2. Llistar un");
        System.out.println("3. Llistar tots");
        System.out.println("4. Modificar");
        System.out.println("5. Eliminar");
        System.out.println("0. Tornar enrere");
    }

    public static void menuVies() {
        System.out.println("\n--- VIES ---");
        System.out.println("1. Crear");
        System.out.println("2. Llistar un");
        System.out.println("3. Llistar totes");
        System.out.println("4. Modificar");
        System.out.println("5. Eliminar");
        System.out.println("0. Tornar enrere");
    }

    public static void menuEscaladors() {
        System.out.println("\n--- ESCALADORS ---");
        System.out.println("1. Crear");
        System.out.println("2. Llistar un");
        System.out.println("3. Llistar tots");
        System.out.println("4. Modificar");
        System.out.println("5. Eliminar");
        System.out.println("0. Tornar enrere");
    }

    public static void menuBusquedas() {
        System.out.println("\n--- CERQUES ---");
        System.out.println("1. Vies per escola");
        System.out.println("2. Vies per dificultat");
        System.out.println("3. Vies per estat");
        System.out.println("4. Escoles amb restriccions");
        System.out.println("5. Sectors amb X vies");
        System.out.println("6. Escaladors per nivell");
        System.out.println("7. Vies recents");
        System.out.println("8. Vies més llargues");
        System.out.println("0. Tornar enrere");
    }
}

