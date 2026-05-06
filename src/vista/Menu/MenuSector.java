package vista.Menu;

import dao.sqlite.EscolaDAO;
import dao.sqlite.SectorDAO;
import excepcions.Validacions;
import helpers.AuxSector;
import model.Escola;
import model.Sector;
import model.enums.Popularitat;
import vista.Vista;

import java.util.List;
import java.util.Scanner;

public class MenuSector {

    private final Scanner sc;
    private final SectorDAO sectorDAO;
    private final EscolaDAO escolaDAO;

    public MenuSector(Scanner sc, SectorDAO sectorDAO, EscolaDAO escolaDAO) {
        this.sc = sc;
        this.sectorDAO = sectorDAO;
        this.escolaDAO = escolaDAO;
    }

    public void menu() {
        int op;

        do {
            Vista.menuSectors();
            op = Validacions.llegirOpcio(sc, "Opció: ", 0, 5);

            switch (op) {
                case 1 -> crear();
                case 2 -> llistarUn();
                case 3 -> llistarTots();
                case 4 -> modificar();
                case 5 -> eliminar();
            }

        } while (op != 0);
    }

    private void crear() {
        String nom = Validacions.llegirTextNoBuit(sc, "Nom: ");
        double lat = Validacions.llegirDouble(sc, "Latitud: ");
        double lon = Validacions.llegirDouble(sc, "Longitud: ");
        String aprox = Validacions.llegirTextNoBuit(sc, "Aproximació: ");
        String pop = llegirPopularitat();
        String rest = Validacions.llegirTextNoBuit(sc, "Restriccions: ");
        int idEscola = Validacions.llegirEnterNoNegatiu(sc, "ID escola: ");

        Escola escola = escolaDAO.findById(idEscola);
        if (escola == null) {
            System.out.println("L'escola no existeix.");
            return;
        }

        if (sectorDAO.findByNomAndEscola(nom, idEscola) != null) {
            System.out.println("Ja existeix un sector amb aquest nom dins d'aquesta escola.");
            return;
        }

        Sector s = new Sector(0, nom, lat, lon, aprox, pop, rest, idEscola);
        sectorDAO.insert(s);
        System.out.println("Sector creat amb ID " + s.getIdSector());
    }

    private void llistarUn() {
        Sector s = sectorDAO.findById(Validacions.llegirEnterNoNegatiu(sc, "ID: "));
        if (s == null) {
            System.out.println("No trobat");
            return;
        }

        System.out.println("ID: " + s.getIdSector());
        System.out.println("Nom: " + s.getNom());
        System.out.println("Coordenades: " + s.getLatitud() + ", " + s.getLongitud());
        System.out.println("Aproximació: " + s.getAproximacio());
        System.out.println("Popularitat: " + s.getPopularitat());
        System.out.println("Restriccions: " + s.getRestriccions());
        System.out.println("Escola: " + s.getIdEscola());
        System.out.println("Número de vies: " + s.getNumeroVies());
    }

    private void llistarTots() {
        List<Sector> llista = sectorDAO.findAll();
        if (llista.isEmpty()) {
            System.out.println("No hi ha sectors");
            return;
        }

        for (Sector s : llista) {
            System.out.println(s.getIdSector() + " - " + s.getNom() + " | escola = " + s.getIdEscola() + " | " + s.getPopularitat());
        }
    }

    private void modificar() {
        int id = Validacions.llegirEnterNoNegatiu(sc, "ID: ");
        Sector s = sectorDAO.findById(id);

        if (s == null) {
            System.out.println("No trobat");
            return;
        }

        String nom = Validacions.llegirTextOpcional(sc, "Nom (" + s.getNom() + "): ");
        if (!nom.isEmpty()) {
            Sector existent = sectorDAO.findByNomAndEscola(nom, s.getIdEscola());
            if (existent != null && existent.getIdSector() != s.getIdSector()) {
                System.out.println("Ja existeix un sector amb aquest nom dins d'aquesta escola.");
                return;
            }
            s.setNom(nom);
        }

        String lat = Validacions.llegirTextOpcional(sc, "Latitud (" + s.getLatitud() + "): ");
        if (!lat.isEmpty()) s.setLatitud(parseDouble(lat, s.getLatitud()));

        String lon = Validacions.llegirTextOpcional(sc, "Longitud (" + s.getLongitud() + "): ");
        if (!lon.isEmpty()) s.setLongitud(parseDouble(lon, s.getLongitud()));

        String aprox = Validacions.llegirTextOpcional(sc, "Aproximació (" + s.getAproximacio() + "): ");
        if (!aprox.isEmpty()) s.setAproximacio(aprox);

        String pop = Validacions.llegirTextOpcional(sc, "Popularitat (" + s.getPopularitat() + "): ");
        if (!pop.isEmpty()) s.setPopularitat(normalitzarPopularitat(pop));

        String rest = Validacions.llegirTextOpcional(sc, "Restriccions (" + s.getRestriccions() + "): ");
        if (!rest.isEmpty()) s.setRestriccions(rest);

        sectorDAO.update(s);
        System.out.println("Sector modificat");
    }

    private void eliminar() {
        sectorDAO.delete(Validacions.llegirEnterNoNegatiu(sc, "ID: "));
        System.out.println("Sector eliminat");
    }

    private double parseDouble(String valor, double perDefecte) {
        try {
            return Double.parseDouble(valor.trim());
        } catch (NumberFormatException e) {
            return perDefecte;
        }
    }

    private String llegirPopularitat() {
        while (true) {
            String pop = Validacions.llegirTextNoBuit(sc, "Popularitat (Baixa/Mitjana/Alta): ");
            if (Popularitat.fromValor(pop) != null) {
                return normalitzarPopularitat(pop);
            }
            System.out.println("Popularitat no vàlida.");
        }
    }

    private String normalitzarPopularitat(String valor) {
        return AuxSector.normalitzarPopularitat(valor);
    }
}