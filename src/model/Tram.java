package model;

public abstract class Tram {
    protected int idTram;
    protected int llarg;
    protected String grauDificultat;

    public Tram(int idTram, int llarg, String grauDificultat) {
        this.idTram = idTram;
        this.llarg = llarg;
        this.grauDificultat = grauDificultat;
    }

    public int getIdTram() {
        return idTram;
    }

    public int getLlarg() {
        return llarg;
    }

    public String getGrauDificultat() {
        return grauDificultat;
    }
}