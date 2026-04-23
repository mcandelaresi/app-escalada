package model;

public class ViaEsportiva extends Via {

    private int llargada; // 5-30 metres

    public ViaEsportiva(int idVia, String nom, String grau, String orientacio,
                        String estat, String dataEstat, String tipus,
                        String ancoratges, String tipusDeRoca,
                        int idCreador, int idSector, int idEscola,
                        String restriccions, int llargada) {

        super(idVia, nom, grau, orientacio, estat, dataEstat,
                tipus, ancoratges, tipusDeRoca,
                idCreador, idSector, idEscola, restriccions);

        if (llargada < 5 || llargada > 30) {
            throw new IllegalArgumentException("Llargada fora de rang (5-30m)");
        }

        this.llargada = llargada;
    }

    public int getLlargada() {
        return llargada;
    }

    public void setLlargada(int llargada) {
        if (llargada < 5 || llargada > 30) {
            throw new IllegalArgumentException("Llargada fora de rang (5-30m)");
        }
        this.llargada = llargada;
    }
}