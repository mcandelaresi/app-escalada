package model;

import java.util.ArrayList;
import java.util.List;

public class ViaClassica extends Via {

    private String ancoratgesPermesos;
    private List<Tram> trams;

    public ViaClassica(int idVia, String nom, String grau, String orientacio,
                       String estat, String dataEstat, String tipus,
                       String ancoratges, String tipusDeRoca,
                       int idCreador, int idSector, int idEscola,
                       String restriccions, String ancoratgesPermesos) {

        super(idVia, nom, grau, orientacio, estat, dataEstat,
                tipus, ancoratges, tipusDeRoca,
                idCreador, idSector, idEscola, restriccions);

        this.ancoratgesPermesos = ancoratgesPermesos;
        this.trams = new ArrayList<>();
    }

    public int getLlargadaTotal() {
        int total = 0;
        for (Tram t : trams) {
            total += t.getLlargada();
        }
        return total;
    }

    public List<Tram> getTrams() {
        return trams;
    }

    public void afegirTram(Tram tram) {
        this.trams.add(tram);
    }

    public String getAncoratgesPermesos() {
        return ancoratgesPermesos;
    }

    public void setAncoratgesPermesos(String ancoratgesPermesos) {
        this.ancoratgesPermesos = ancoratgesPermesos;
    }
}