package vista.Menu;

import controlador.CercaController;
import excepcions.Validacions;
import model.*;
import model.enums.*;
import vista.Vista;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class MenuCerca {

    private final Scanner sc;
    private final CercaController controller;

    public MenuCerca(Scanner sc, CercaController controller) {
        this.sc = sc;
        this.controller = controller;
    }

    public void menu() {
        int op;

        do {
            Vista.menuBusquedas();

            op = Validacions.llegirOpcio(sc, "Opcio: ", 0, 10);

            switch (op) {
                case 1 -> viesDunaEscola();
                case 2 -> viesPerDificultat();
                case 3 -> viesPerEstat();
                case 4 -> escolesAmbRestriccions();
                case 5 -> sectorsAmbMesDeX();
                case 6 -> escaladorsMateixNivell();
                case 7 -> viesApteRecentment();
                case 8 -> viesMesLlarguesEscola();
                case 9 -> buscarEscaladorPerAlias();
                case 10 -> buscarEscaladorsPerNivell();
            }

        } while (op != 0);
    }

    private void viesDunaEscola() {
        int idEscola = Validacions.llegirEnterNoNegatiu(sc, "ID Escola: ");
        List<Via> vies = controller.viesDunaEscolaDisponibles(idEscola);
        if (vies.isEmpty()) {
            System.out.println("No hi ha vies disponibles");
        } else {
            vies.forEach(v -> System.out.println(v.getIdVia() + " - " + v.getNom() + " | " + v.getGrau()));
        }
    }

    private void viesPerDificultat() {
        String minStr = Validacions.llegirTextNoBuit(sc, "Grau mínim: ");
        String maxStr = Validacions.llegirTextNoBuit(sc, "Grau màxim: ");
        GrauDificultat min = GrauDificultat.fromValor(minStr);
        GrauDificultat max = GrauDificultat.fromValor(maxStr);
        if (min == null || max == null) {
            System.out.println("Graus no vàlids");
            return;
        }
        List<Via> vies = controller.viesPerDificultat(min, max);
        vies.forEach(v -> System.out.println(v.getNom() + " - " + v.getGrau()));
    }

    private void viesPerEstat() {
        String estatStr = Validacions.llegirTextNoBuit(sc, "Estat: ");
        EstatVia estat = EstatVia.fromValor(estatStr);
        if (estat == null) {
            System.out.println("Estat no vàlid");
            return;
        }
        List<Via> vies = controller.viesPerEstat(estat);
        vies.forEach(v -> System.out.println(v.getNom() + " - " + v.getEstat()));
    }

    private void escolesAmbRestriccions() {
        List<Escola> escoles = controller.escolesAmbRestriccions();
        if (escoles.isEmpty()) {
            System.out.println("No hi ha escoles amb restriccions");
        } else {
            escoles.forEach(e -> System.out.println(e.getNom() + " - " + e.getRestriccions()));
        }
    }

    private void sectorsAmbMesDeX() {
        int x = Validacions.llegirEnterNoNegatiu(sc, "Nombre mínim de vies: ");
        List<Sector> sectors = controller.sectorsAmbMesDeX(x);
        if (sectors.isEmpty()) {
            System.out.println("No hi ha sectors amb més de " + x + " vies");
        } else {
            sectors.forEach(s -> System.out.println(s.getNom()));
        }
    }

    private void escaladorsMateixNivell() {
        Map<String, List<Escalador>> map = controller.escaladorsMateixNivell();
        if (map.isEmpty()) {
            System.out.println("No hi ha escaladors agrupats per nivell");
        } else {
            map.forEach((nivell, list) -> {
                System.out.println("Nivell: " + nivell);
                list.forEach(e -> System.out.println("  " + e.getAlias()));
            });
        }
    }

    private void viesApteRecentment() {
        List<Via> vies = controller.viesApteRecentment();
        if (vies.isEmpty()) {
            System.out.println("No hi ha vies aptes recentment");
        } else {
            vies.forEach(v -> System.out.println(v.getNom() + " - Data: " + v.getDataEstat()));
        }
    }

    private void viesMesLlarguesEscola() {
        int idEscola = Validacions.llegirEnterNoNegatiu(sc, "ID Escola: ");
        List<Via> vies = controller.viesMesLlarguesEscola(idEscola);
        if (vies.isEmpty()) {
            System.out.println("No hi ha vies en aquesta escola");
        } else {
            vies.forEach(v -> System.out.println(v.getNom() + " - Grau: " + v.getGrau()));
        }
    }

    private void buscarEscaladorPerAlias() {
        String alias = Validacions.llegirTextNoBuit(sc, "Alias: ");
        Escalador e = controller.buscarEscaladorPerAlias(alias);
        if (e == null) {
            System.out.println("No trobat");
        } else {
            System.out.println(e.getAlias() + " - Nivell: " + e.getNivellMax());
        }
    }

    private void buscarEscaladorsPerNivell() {
        String nivell = Validacions.llegirTextNoBuit(sc, "Nivell: ");
        List<Escalador> list = controller.buscarEscaladorsPerNivell(nivell);
        if (list.isEmpty()) {
            System.out.println("No hi ha escaladors amb aquest nivell");
        } else {
            list.forEach(e -> System.out.println(e.getAlias()));
        }
    }
}