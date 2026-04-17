package model;

import java.util.ArrayList;
import java.util.List;

public class Sector {
    private int idSector;
    private String nom;
    private double latitud;
    private double longitud;
    private String aproximacio;    // Descripció de com arribar-hi
    private String popularitat;    // Baixa, Mitjana, Alta
    private String restriccions;
    private int idEscola;          // L'escola a la qual pertanyo

    // Aquí guardo totes les vies
    private List<Via> vies;

    public Sector(int idSector, String nom, double latitud, double longitud,
                  String aproximacio, String popularitat, String restriccions, int idEscola) {
        this.idSector = idSector;
        this.nom = nom;
        this.latitud = latitud;
        this.longitud = longitud;
        this.aproximacio = aproximacio;
        this.popularitat = popularitat;
        this.restriccions = restriccions;
        this.idEscola = idEscola;
        this.vies = new ArrayList<>();
    }


    /**
     * Compta quantes vies tinc en total.
     * Això em serveix per complir amb l'atribut "número de vies"
     */
    public int getNumeroVies() {
        return vies.size();
    }

    /**
     * Afegeixo una via al meu sector, aquí hauríem de validar que si ja tinc vies de Gel,
     */
    public void afegirVia(Via via) {
        this.vies.add(via);
    }

    // Getters i Setters

    public int getIdSector() { return idSector; }
    public void setIdSector(int idSector) { this.idSector = idSector; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public double getLatitud() { return latitud; }
    public void setLatitud(double latitud) { this.latitud = latitud; }

    public double getLongitud() { return longitud; }
    public void setLongitud(double longitud) { this.longitud = longitud; }

    public List<Via> getVies() { return vies; }

    public String getRestriccions() { return restriccions; }
    public void setRestriccions(String restriccions) { this.restriccions = restriccions; }
}