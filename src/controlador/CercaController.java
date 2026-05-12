package controlador;

import dao.sqlite.*;
import excepcions.Validacions;
import model.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CercaController {

    private final ViaDAO viaDAO;
    private final EscolaDAO escolaDAO;
    private final SectorDAO sectorDAO;
    private final EscaladorDAO escaladorDAO;
    private final TramDAO tramDAO;

    public CercaController(ViaDAO viaDAO, EscolaDAO escolaDAO, SectorDAO sectorDAO,
                           EscaladorDAO escaladorDAO, TramDAO tramDAO) {
        this.viaDAO = viaDAO;
        this.escolaDAO = escolaDAO;
        this.sectorDAO = sectorDAO;
        this.escaladorDAO = escaladorDAO;
        this.tramDAO = tramDAO;
    }


    // CERCA 1: Vies d'una escola disponibles (estat = Apte)

    public void viesDisponiblesEscola(Scanner sc) {
        System.out.println("\n=== CERCA 1: Vies disponibles d'una escola ===");
        llistarEscoles();
        int idEscola = Validacions.llegirEnterNoNegatiu(sc, "ID Escola: ");
        Escola escola = escolaDAO.findById(idEscola);
        if (escola == null) {
            System.out.println("Escola no trobada.");
            return;
        }

        List<Via> totes = viaDAO.findAll();
        List<Via> resultat = new ArrayList<>();
        for (Via v : totes) {
            if (v.getIdEscola() == idEscola && v.esPotEscalar()) {
                resultat.add(v);
            }
        }

        System.out.println("\nVies disponibles (Apte) de l'escola '" + escola.getNom() + "':");
        if (resultat.isEmpty()) {
            System.out.println("  Cap via disponible.");
        } else {
            for (Via v : resultat) {
                System.out.printf("  [%3d] %-25s %-10s  Grau: %-6s  Sector: %d%n",
                        v.getIdVia(), v.getNom(), v.getTipus(), v.getGrau(), v.getIdSector());
            }
            System.out.println("Total: " + resultat.size() + " vies disponibles.");
        }
    }


    // CERCA 2: Vies per rang de dificultat

    public void viesPerDificultat(Scanner sc) {
        System.out.println("\n=== CERCA 2: Vies per rang de dificultat ===");
        String[] graus = {"4","4+","5","5+","6a","6a+","6b","6b+","6c","6c+",
                "7a","7a+","7b","7b+","7c","7c+","8a","8a+","8b","8b+",
                "8c","8c+","9a","9a+","9b","9b+","9c","9c+"};
        System.out.println("Graus disponibles:");
        for (int i = 0; i < graus.length; i++) {
            System.out.print((i+1) + "." + graus[i] + "  ");
        }
        System.out.println();

        int des = Validacions.llegirOpcio(sc, "Grau mínim (número): ", 1, graus.length);
        int fins = Validacions.llegirOpcio(sc, "Grau màxim (número): ", des, graus.length);

        List<Via> totes = viaDAO.findAll();
        List<Via> resultat = new ArrayList<>();
        for (Via v : totes) {
            int idx = indexGrau(v.getGrau(), graus);
            if (idx >= des - 1 && idx <= fins - 1) {
                resultat.add(v);
            }
        }

        System.out.printf("%nVies entre %s i %s:%n", graus[des-1], graus[fins-1]);
        if (resultat.isEmpty()) {
            System.out.println("  Cap resultat.");
        } else {
            for (Via v : resultat) {
                Escola e = escolaDAO.findById(v.getIdEscola());
                String nomEscola = e != null ? e.getNom() : "ID:" + v.getIdEscola();
                System.out.printf("  [%3d] %-25s  Grau: %-6s  Sector: %d  Escola: %s%n",
                        v.getIdVia(), v.getNom(), v.getGrau(), v.getIdSector(), nomEscola);
            }
            System.out.println("Total: " + resultat.size() + " vies.");
        }
    }

    // CERCA 3: Vies per estat
    public void viesPerEstat(Scanner sc) {
        System.out.println("\n=== CERCA 3: Vies per estat ===");
        System.out.println("1. Apte  2. Construccio  3. Tancada");
        int opc = Validacions.llegirOpcio(sc, "Estat: ", 1, 3);
        String estat = switch (opc) { case 1 -> "Apte"; case 2 -> "Construccio"; default -> "Tancada"; };

        List<Via> totes = viaDAO.findAll();
        List<Via> resultat = new ArrayList<>();
        for (Via v : totes) {
            if (v.getEstat().equalsIgnoreCase(estat)) resultat.add(v);
        }

        System.out.println("\nVies amb estat '" + estat + "':");
        if (resultat.isEmpty()) {
            System.out.println("  Cap resultat.");
        } else {
            for (Via v : resultat) {
                String dataInfo = v.getDataEstat() != null ? " (fins " + v.getDataEstat() + ")" : "";
                System.out.printf("  [%3d] %-25s %-10s%s%n",
                        v.getIdVia(), v.getNom(), v.getTipus(), dataInfo);
            }
            System.out.println("Total: " + resultat.size() + " vies.");
        }
    }


    // CERCA 4: Escoles amb restriccions actives

    public void escolesAmbRestriccions(Scanner sc) {
        System.out.println("\n=== CERCA 4: Escoles amb restriccions actives ===");
        List<Escola> totes = escolaDAO.findAll();
        List<Escola> resultat = new ArrayList<>();
        for (Escola e : totes) {
            String r = e.getRestriccions();
            if (r != null && !r.isBlank() && !r.equalsIgnoreCase("Cap") && !r.equalsIgnoreCase("Ninguna")) {
                resultat.add(e);
            }
        }

        if (resultat.isEmpty()) {
            System.out.println("Cap escola amb restriccions actives.");
        } else {
            for (Escola e : resultat) {
                System.out.printf("  [%3d] %-20s  Restriccions: %s%n",
                        e.getIdEscola(), e.getNom(), e.getRestriccions());
            }
            System.out.println("Total: " + resultat.size() + " escoles.");
        }
    }

    // CERCA 5: Sectors amb més de X vies disponibles (Apte)

    public void sectorsAmbMesDeXVies(Scanner sc) {
        System.out.println("\n=== CERCA 5: Sectors amb més de X vies disponibles ===");
        int x = Validacions.llegirEnterNoNegatiu(sc, "Nombre mínim de vies: ");

        List<Sector> sectors = sectorDAO.findAll();
        List<Via> totes = viaDAO.findAll();

        System.out.println("\nSectors amb més de " + x + " vies disponibles:");
        boolean trovat = false;
        for (Sector s : sectors) {
            long count = totes.stream()
                    .filter(v -> v.getIdSector() == s.getIdSector() && v.esPotEscalar())
                    .count();
            if (count > x) {
                Escola e = escolaDAO.findById(s.getIdEscola());
                String nomEscola = e != null ? e.getNom() : "ID:" + s.getIdEscola();
                System.out.printf("  [%3d] %-20s  Escola: %-15s  Vies disponibles: %d%n",
                        s.getIdSector(), s.getNom(), nomEscola, count);
                trovat = true;
            }
        }
        if (!trovat) System.out.println("  Cap sector compleix el criteri.");
    }


    // CERCA 6: Escaladors amb el mateix nivell màxim assolit

    public void escaladorsMateixNivell(Scanner sc) {
        System.out.println("\n=== CERCA 6: Escaladors amb el mateix nivell màxim ===");
        List<Escalador> tots = escaladorDAO.findAll();
        if (tots.isEmpty()) {
            System.out.println("No hi ha escaladors registrats.");
            return;
        }

        // Mostrar escaladors disponibles
        System.out.println("Escaladors registrats:");
        for (Escalador e : tots) {
            System.out.printf("  [%3d] %-25s  Nivell: %s%n",
                    e.getIdEscalador(), e.getNom(), e.getNivellMax());
        }

        int id = Validacions.llegirEnterNoNegatiu(sc, "\nID escalador de referència: ");
        Escalador ref = escaladorDAO.findById(id);
        if (ref == null) {
            System.out.println("Escalador no trobat.");
            return;
        }

        System.out.println("\nEscaladors amb nivell màxim '" + ref.getNivellMax() + "' (com " + ref.getNom() + "):");
        boolean trovat = false;
        for (Escalador e : tots) {
            if (e.getIdEscalador() != ref.getIdEscalador() && e.tincMateixNivell(ref)) {
                System.out.printf("  [%3d] %-25s  Àlies: %-15s  Estil: %s%n",
                        e.getIdEscalador(), e.getNom(), e.getAlias(), e.getEstilPreferit());
                trovat = true;
            }
        }
        if (!trovat) System.out.println("  Cap altre escalador amb el mateix nivell.");
    }

    // CERCA 7: Vies que han passat a "Apte" recentment (últims 30 dies)

    public void viesAptesRecentment(Scanner sc) {
        System.out.println("\n=== CERCA 7: Vies que han tornat a Apte recentment (últims 30 dies) ===");
        LocalDate fa30Dies = LocalDate.now().minusDays(30);

        List<Via> totes = viaDAO.findAll();
        List<Via> resultat = new ArrayList<>();
        for (Via v : totes) {
            if (!v.esPotEscalar()) continue;
            // Si data_estat no és null, vol dir que tenia un estat temporal que ha caducat
            if (v.getDataEstat() != null && !v.getDataEstat().isBlank()) {
                try {
                    LocalDate dataFi = LocalDate.parse(v.getDataEstat());
                    // La data de recuperació de l'estat apte és la data_estat (quan va caducar la restricció)
                    if (!dataFi.isBefore(fa30Dies)) {
                        resultat.add(v);
                    }
                } catch (Exception ignored) {}
            }
        }

        if (resultat.isEmpty()) {
            System.out.println("Cap via ha tornat a Apte en els últims 30 dies.");
        } else {
            for (Via v : resultat) {
                System.out.printf("  [%3d] %-25s %-10s  Apte des de: %s%n",
                        v.getIdVia(), v.getNom(), v.getTipus(), v.getDataEstat());
            }
            System.out.println("Total: " + resultat.size() + " vies.");
        }
    }

    // CERCA 8: Vies més llargues d'una escola determinada
    public void viesMesLlarguesEscola(Scanner sc) {
        System.out.println("\n=== CERCA 8: Vies més llargues d'una escola ===");
        llistarEscoles();
        int idEscola = Validacions.llegirEnterNoNegatiu(sc, "ID Escola: ");
        Escola escola = escolaDAO.findById(idEscola);
        if (escola == null) {
            System.out.println("Escola no trobada.");
            return;
        }

        int topN = Validacions.llegirOpcio(sc, "Quantes vies mostrar (top N): ", 1, 50);

        // Recollir totes les vies de l'escola amb la seva llargada total
        List<Via> totes = viaDAO.findAll();
        List<int[]> viesLlargada = new ArrayList<>(); // [idVia, llargadaTotal]

        for (Via v : totes) {
            if (v.getIdEscola() != idEscola) continue;
            int llargada = 0;
            if (v instanceof ViaEsportiva ve) {
                llargada = ve.getLlargada();
            } else if (v instanceof ViaClassica vc) {
                llargada = vc.getLlargadaTotal();
                if (llargada == 0) {
                    // Carregar trams si no estan carregats
                    List<Tram> trams = tramDAO.findByVia(v.getIdVia());
                    for (Tram t : trams) llargada += t.getLlarg();
                }
            } else if (v instanceof ViaGel vg) {
                llargada = vg.getLlargadaTotal();
                if (llargada == 0) {
                    List<Tram> trams = tramDAO.findByVia(v.getIdVia());
                    for (Tram t : trams) llargada += t.getLlarg();
                }
            }
            viesLlargada.add(new int[]{v.getIdVia(), llargada});
        }

        // Ordenar per llargada descendent
        viesLlargada.sort((a, b) -> b[1] - a[1]);

        System.out.println("\nTop " + topN + " vies més llargues de '" + escola.getNom() + "':");
        int mostrades = 0;
        for (int[] entry : viesLlargada) {
            if (mostrades >= topN) break;
            Via v = viaDAO.findById(entry[0]);
            if (v == null) continue;
            System.out.printf("  %2d. [%3d] %-25s %-10s  Llargada total: %dm%n",
                    ++mostrades, v.getIdVia(), v.getNom(), v.getTipus(), entry[1]);
        }
        if (mostrades == 0) System.out.println("  Aquesta escola no té vies registrades.");
    }

    // Helpers
    private void llistarEscoles() {
        List<Escola> escoles = escolaDAO.findAll();
        System.out.println("Escoles disponibles:");
        for (Escola e : escoles) {
            System.out.printf("  [%3d] %s%n", e.getIdEscola(), e.getNom());
        }
    }

    private int indexGrau(String grau, String[] graus) {
        for (int i = 0; i < graus.length; i++) {
            if (graus[i].equalsIgnoreCase(grau)) return i;
        }
        return -1;
    }
}