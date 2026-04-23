package model;

import java.util.ArrayList;
import java.util.List;

public class Sector {

    private int idSector;
    private String nom;
    private double latitud;
    private double longitud;
    private String aproximacio;
    private String popularitat;
    private String restriccions;
    private int idEscola;

    private List<Via> vies;

    public Sector(int idSector, String nom, double latitud, double longitud,
                  String aproximacio, String popularitat,
                  String restriccions, int idEscola) {

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

    public int getNumeroVies() {
        return vies.size();
    }

    public void afegirVia(Via via) {
        if (!vies.isEmpty()) {
            String tipusActual = vies.get(0).getTipus();
            if (!tipusActual.equals(via.getTipus())) {
                throw new IllegalArgumentException("No es poden barrejar tipus de via al sector");
            }
        }

        this.vies.add(via);
    }

    // Getters i setters

    public int getIdSector() { return idSector; }
    public void setIdSector(int idSector) { this.idSector = idSector; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public double getLatitud() { return latitud; }
    public void setLatitud(double latitud) { this.latitud = latitud; }

    public double getLongitud() { return longitud; }
    public void setLongitud(double longitud) { this.longitud = longitud; }

    public String getPopularitat() { return popularitat; }
    public void setPopularitat(String popularitat) { this.popularitat = popularitat; }

    public String getRestriccions() { return restriccions; }
    public void setRestriccions(String restriccions) { this.restriccions = restriccions; }

    public int getIdEscola() { return idEscola; }
    public void setIdEscola(int idEscola) { this.idEscola = idEscola; }

    public List<Via> getVies() { return vies; }
}