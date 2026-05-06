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

    private final List<Via> vies;

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

    if (via == null) {
        return;
    }

    if (!potAfegirVia(via)) {
        throw new IllegalArgumentException(
                "No es poden barrejar vies de GEL amb altres tipus");
    }

    vies.add(via);
}
    public boolean potAfegirVia(Via novaVia) {
    if (novaVia == null) {
        return false;
    }

    if (vies.isEmpty()) {
        return true;
    }

    boolean sectorTeGel = false;
    boolean sectorTeNoGel = false;

    for (Via via : vies) {
        if ("GEL".equalsIgnoreCase(via.getTipus())) {
            sectorTeGel = true;
        } else {
            sectorTeNoGel = true;
        }
    }

    boolean novaEsGel = "GEL".equalsIgnoreCase(novaVia.getTipus());

    if (sectorTeGel && !novaEsGel) {
        return false;
    }

    if (sectorTeNoGel && novaEsGel) {
        return false;
    }

    return true;
}

    // GETTERS I SETTERS

    public int getIdSector() { return idSector; }
    public void setIdSector(int idSector) { this.idSector = idSector; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public double getLatitud() { return latitud; }
    public void setLatitud(double latitud) { this.latitud = latitud; }

    public String getAproximacio() { return aproximacio; }
    public void setAproximacio(String aproximacio) { this.aproximacio = aproximacio; }

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