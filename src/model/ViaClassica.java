package model;

import java.util.ArrayList;
import java.util.List;

public class ViaClassica extends Via {
    private int idViaClassica;
    private String ancoratgesPermesos; // Friends, tascons, pitons...

    // He decidit guardar els meus trams en una llista.
    // Així puc complir amb el requeriment que diu que superem els 50 metres
    // sumant els diferents llargs (L1, L2, L3...).
    private List<Tram> trams;

    public ViaClassica(int idVia, String nom, String orientacio, String estat, String ancoratges,
                       String tipusDeRoca, int idCreador, int idSector, int diesNoApte,
                       String restriccions, int idViaClassica, String ancoratgesPermesos) {


        super(idVia, nom, orientacio, estat, ancoratges, tipusDeRoca, idCreador, idSector, diesNoApte, restriccions);

        this.idViaClassica = idViaClassica;
        this.ancoratgesPermesos = ancoratgesPermesos;
        this.trams = new ArrayList<>();
    }

    /**
     * Aquest mètode em permet calcular la meva llargada total sumant la de cada tram,
     * en lloc de tenir un número fix.
     */
    public int getLlargadaTotal() {
        int total = 0;
        for (Tram t : trams) {
            total += t.getLlargada();
        }
        return total;
    }

    /**
     * Per saber el meu grau de dificultat, normalment ens fixem en el tram més difícil.
     */
    public String getGrauMaxim() {
        // Aquí podria recórrer els trams i tornar el grau més alt.
        return "Càlcul pendent";
    }

    // Getters i Setters
    public List<Tram> getTrams() { return trams; }
    public void afegirTram(Tram tram) { this.trams.add(tram); }

    public String getAncoratgesPermesos() { return ancoratgesPermesos; }
    public void setAncoratgesPermesos(String ancoratgesPermesos) { this.ancoratgesPermesos = ancoratgesPermesos; }
}