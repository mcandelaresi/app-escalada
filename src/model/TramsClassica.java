package model;

public class TramsClassica extends Tram {

    private int idViaClassica;

    public TramsClassica(int idTramClassica, int llarg, String grauDificultat, int idViaClassica) {
        super(idTramClassica, llarg, grauDificultat);
        this.idViaClassica = idViaClassica;
    }

    public int getIdViaClassica() {
        return idViaClassica;
    }

    public void setIdViaClassica(int idViaClassica) {
        this.idViaClassica = idViaClassica;
    }
}