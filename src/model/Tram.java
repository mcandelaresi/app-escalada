package model;

public class Tram {

    private int idTram;
    private int numLlarg;
    private int llarg;
    private String grauDificultat;
    private int idVia;

    public Tram(int idTram, int numLlarg, int llarg, String grauDificultat, int idVia) {
        if (llarg < 15 || llarg > 30) {
            throw new IllegalArgumentException("La llargada del tram ha d'estar entre 15 i 30 metres");
        }
        this.idTram = idTram;
        this.numLlarg = numLlarg;
        this.llarg = llarg;
        this.grauDificultat = grauDificultat;
        this.idVia = idVia;
    }

    public int getIdTram() {
        return idTram;
    }

    public int getNumLlarg() {
        return numLlarg;
    }

    public void setNumLlarg(int numLlarg) {
        this.numLlarg = numLlarg;
    }

    public int getLlarg() {
        return llarg;
    }

    public void setLlarg(int llarg) {
        if (llarg < 15 || llarg > 30) {
            throw new IllegalArgumentException("La llargada del tram ha d'estar entre 15 i 30 metres");
        }
        this.llarg = llarg;
    }

    public String getGrauDificultat() {
        return grauDificultat;
    }

    public void setGrauDificultat(String grauDificultat) {
        this.grauDificultat = grauDificultat;
    }

    public int getIdVia() {
        return idVia;
    }

    public void setIdVia(int idVia) {
        this.idVia = idVia;
    }
}