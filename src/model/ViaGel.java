package model;

import java.util.ArrayList;
import java.util.List;


public class ViaGel extends Via {
    private int idViaGel;

    // Al igual que la clàssica, tinc una llista de trams (llargs).
    // Cada llarg de gel tindrà la seva pròpia dificultat i llargada (15-30m).
    private List<Tram> trams;

    public ViaGel(int idVia, String nom, String orientacio, String estat, String ancoratges,
                  String tipusDeRoca, int idCreador, int idSector, int diesNoApte,
                  String String restriccions, int idViaGel) {

        // Crido a la superclasse Via.
        // aquí els ancoratges només poden ser: friends, tascons, bagues, pitons, Tricams, BigBros.
        super(idVia, nom, orientacio, estat, ancoratges, tipusDeRoca, idCreador, idSector, diesNoApte, restriccions);

        this.idViaGel = idViaGel;
        this.trams = new ArrayList<>();
    }

    /**
     * Com que el gel canvia segons la temperatura, aquest mètode em serà molt útil
     * per saber si estic en condicions d'ser escalada.
     */
    public int getLlargadaTotal() {
        int total = 0;
        for (Tram t : trams) {
            total += t.getLlargada();
        }
        return total;
    }

    // Getters i Setters

    public int getIdViaGel() { return idViaGel; }
    public void setIdViaGel(int idViaGel) { this.idViaGel = idViaGel; }

    public List<Tram> getTrams() { return trams; }
    public void afegirTram(Tram tram) { this.trams.add(tram); }
}