package model;

public class Tram {

    private int idTram;
    private int llarg;
    private String grauDificultat;
    private int idVia;

    public Tram(int idTram, int llarg, String grauDificultat, int idVia) {
        this.idTram = idTram;
        this.llarg = llarg;
        this.grauDificultat = grauDificultat;
        this.idVia = idVia;
    }

    public int getLlarg() {
        return llarg;
    }
}