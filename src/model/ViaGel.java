package model;

import java.util.ArrayList;
import java.util.List;

public class ViaGel extends Via {

    private List<Tram> trams;

    public ViaGel(int idVia, String nom, String grau, String orientacio,
                  String estat, String dataEstat,
                  int idEscola, int idSector, int idCreador,
                  String restriccions) {

        super(idVia, nom, grau, orientacio, estat, dataEstat,
                "gel", idEscola, idSector, idCreador, restriccions);

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
}