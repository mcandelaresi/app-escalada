package controlador;

import dao.sqlite.EscolaDAO;
import dao.sqlite.SectorDAO;
import excepcions.Validacions;
import model.Escola;
import model.Sector;

import java.util.List;
import java.util.Scanner;

public class EscolaController {

    private final EscolaDAO dao;
    private final SectorDAO sectorDAO;


    public EscolaController(EscolaDAO dao, SectorDAO sectorDAO) {
        this.dao = dao;
        this.sectorDAO = sectorDAO;
    }

    public void crearEscola(Scanner sc) {
        String nom = Validacions.llegirTextNoBuit(sc, "Nom: ");
        if (dao.findByNom(nom) != null) {
            System.out.println("Ja existeix una escola amb el nom '" + nom + "'.");
            return;
        }
        String aproximacio = Validacions.llegirTextNoBuit(sc, "Aproximació: ");
        String popularitat = llegirPopularitat(sc);
        String restriccions = Validacions.llegirTextOpcional(sc, "Restriccions (opcional): ");
        if (restriccions.isEmpty()) restriccions = "Cap";

        Escola e = new Escola(0, nom, aproximacio, popularitat, restriccions);
        dao.insert(e);
        System.out.println("Escola '" + nom + "' creada correctament.");
    }

    public void modificarEscola(Scanner sc) {
        int id = Validacions.llegirEnterNoNegatiu(sc, "ID Escola: ");
        Escola e = dao.findById(id);
        if (e == null) {
            System.out.println("Escola no trobada.");
            return;
        }
        mostrarEscola(e);
        System.out.println("Deixa en blanc per mantenir el valor actual.");

        String nom = Validacions.llegirTextOpcional(sc, "Nou nom (" + e.getNom() + "): ");
        if (!nom.isEmpty()) {
            Escola existent = dao.findByNom(nom);
            if (existent != null && existent.getIdEscola() != e.getIdEscola()) {
                System.out.println("Ja existeix una escola amb aquest nom.");
                return;
            }
            e.setNom(nom);
        }

        String aprox = Validacions.llegirTextOpcional(sc, "Aproximació (" + e.getAproximacio() + "): ");
        if (!aprox.isEmpty()) e.setAproximacio(aprox);

        String restriccions = Validacions.llegirTextOpcional(sc, "Restriccions (" + e.getRestriccions() + "): ");
        if (!restriccions.isEmpty()) e.setRestriccions(restriccions);

        dao.update(e);
        System.out.println("Escola modificada correctament.");
    }

    public void llistarUna(Scanner sc) {
        int id = Validacions.llegirEnterNoNegatiu(sc, "ID Escola: ");
        Escola e = dao.findById(id);
        if (e == null) {
            System.out.println("Escola no trobada.");
            return;
        }
        mostrarEscola(e);
        // Mostrar sectors associats
        List<Sector> sectors = sectorDAO.findByEscola(id);
        if (sectors.isEmpty()) {
            System.out.println("  Sectors: cap");
        } else {
            System.out.println("  Sectors (" + sectors.size() + "):");
            for (Sector s : sectors) {
                System.out.printf("    [%d] %s - %d vies%n", s.getIdSector(), s.getNom(), s.getNumeroVies());
            }
        }
    }

    public void llistarTotes() {
        List<Escola> escoles = dao.findAll();
        if (escoles.isEmpty()) {
            System.out.println("No hi ha escoles registrades.");
            return;
        }
        System.out.println("\n=== TOTES LES ESCOLES ===");
        for (Escola e : escoles) {
            System.out.printf("  [%3d] %-20s  Popularitat: %-8s  Restricc.: %s%n",
                    e.getIdEscola(), e.getNom(), e.getPopularitat(), e.getRestriccions());
        }
        System.out.println("Total: " + escoles.size() + " escoles.");
    }

    public void eliminarEscola(Scanner sc) {
        int id = Validacions.llegirEnterNoNegatiu(sc, "ID Escola a eliminar: ");
        Escola e = dao.findById(id);
        if (e == null) {
            System.out.println("Escola no trobada.");
            return;
        }
        System.out.print("Segur que vols eliminar '" + e.getNom() + "' i tots els seus sectors i vies? (s/n): ");
        String conf = sc.nextLine().trim();
        if (!conf.equalsIgnoreCase("s")) {
            System.out.println("Operació cancel·lada.");
            return;
        }
        dao.delete(id);
        System.out.println("Escola eliminada (CASCADE als sectors i vies).");
    }


    public Escola buscarPerId(int id) { return dao.findById(id); }
    public Escola buscarPerNom(String nom) { return dao.findByNom(nom); }
    public List<Escola> totes() { return dao.findAll(); }

    // Helpers
    private String llegirPopularitat(Scanner sc) {
        System.out.println("Popularitat: 1.Baixa  2.Mitjana  3.Alta");
        int opc = Validacions.llegirOpcio(sc, "Opció: ", 1, 3);
        return switch (opc) { case 1 -> "Baixa"; case 3 -> "Alta"; default -> "Mitjana"; };
    }

    private void mostrarEscola(Escola e) {
        System.out.println("\n--- Escola #" + e.getIdEscola() + " ---");
        System.out.println("Nom:          " + e.getNom());
        System.out.println("Aproximació:  " + e.getAproximacio());
        System.out.println("Popularitat:  " + e.getPopularitat());
        System.out.println("Restriccions: " + e.getRestriccions());
    }
}