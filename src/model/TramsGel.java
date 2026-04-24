package model;

public class TramsGel extends Tram {

    private int idViaGel;

    public TramsGel(int idTramGel, int llarg, String grauDificultat, int idViaGel) {
        super(idTramGel, llarg, grauDificultat);
        this.idViaGel = idViaGel;
    }

    public int getIdViaGel() {
        return idViaGel;
    }

    public void setIdViaGel(int idViaGel) {
        this.idViaGel = idViaGel;
    }
}