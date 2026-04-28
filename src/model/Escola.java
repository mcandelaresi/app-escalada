package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe Escola: conjunt de sectors d'escalada.
 */
public class Escola {

    private int idEscola;
    private String nom;
    private String aproximacio;
    private String popularitat;
    private String restriccions;
    private List<Sector> sectors;

    public Escola(int idEscola, String nom, String aproximacio,
                  String popularitat, String restriccions) {

        this.idEscola = idEscola;
        this.nom = nom;
        this.aproximacio = aproximacio;
        this.popularitat = popularitat;
        this.restriccions = restriccions;

        this.sectors = new ArrayList<>();
    }

    /**
     * El número de vies NO es guarda, es calcula dinàmicament.
     */
    public int getNumVies() {
        int total = 0;
        for (Sector s : sectors) {
            total += s.getNumeroVies();
        }
        return total;
    }

    /**
     * Afegim sectors a l'escola.
     */
    public void afegirSector(Sector sector) {
        if (sector == null) return;
        sectors.add(sector);
    }

    // GETTERS I SETTERS

    public int getIdEscola() {
        return idEscola;
    }

    public void setIdEscola(int idEscola) {
        this.idEscola = idEscola;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getAproximacio() {
        return aproximacio;
    }

    public void setAproximacio(String aproximacio) {
        this.aproximacio = aproximacio;
    }

    public String getPopularitat() {
        return popularitat;
    }

    public void setPopularitat(String popularitat) {
        this.popularitat = popularitat;
    }

    public String getRestriccions() {
        return restriccions;
    }

    public void setRestriccions(String restriccions) {
        this.restriccions = restriccions;
    }

    public List<Sector> getSectors() {
        return sectors;
    }
}