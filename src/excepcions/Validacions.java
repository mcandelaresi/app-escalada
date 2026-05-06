package excepcions;

import java.util.Scanner;

public class Validacions {

    public static int llegirOpcio(Scanner scanner, String missatge, int min, int max) {
        while (true) {
            try {
                System.out.print(missatge);
                String entrada = scanner.nextLine().trim();
                int valor = Integer.parseInt(entrada);
                if (valor < min || valor > max) {
                    throw new EntradaNoValidaException("L'opció ha d'estar entre " + min + " i " + max + ".");
                }
                return valor;
            } catch (NumberFormatException e) {
                System.out.println("Error: introdueix un número vàlid.");
            } catch (EntradaNoValidaException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    public static String llegirTextNoBuit(Scanner scanner, String missatge) {
        while (true) {
            try {
                System.out.print(missatge);
                String valor = scanner.nextLine().trim();
                if (valor.isEmpty()) {
                    throw new EntradaNoValidaException("El camp no pot estar buit.");
                }
                return valor;
            } catch (EntradaNoValidaException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    public static String llegirTextOpcional(Scanner scanner, String missatge) {
        System.out.print(missatge);
        return scanner.nextLine().trim();
    }

    public static int llegirEnterNoNegatiu(Scanner scanner, String missatge) {
        while (true) {
            try {
                System.out.print(missatge);
                String entrada = scanner.nextLine().trim();
                int valor = Integer.parseInt(entrada);
                if (valor < 0) {
                    throw new EntradaNoValidaException("El número no pot ser negatiu.");
                }
                return valor;
            } catch (NumberFormatException e) {
                System.out.println("Error: introdueix un número enter vàlid.");
            } catch (EntradaNoValidaException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    public static double llegirDouble(Scanner scanner, String missatge) {
        while (true) {
            try {
                System.out.print(missatge);
                String entrada = scanner.nextLine().trim();
                return Double.parseDouble(entrada);
            } catch (NumberFormatException e) {
                System.out.println("Error: introdueix un número decimal vàlid.");
            }
        }
    }

}

