package model;


public class ViaEsportiva extends Via {
    private int idViaEsportiva;
    private int llargada;       // Entre 5 i 30 metres segons en Jordi i en Miquel
    private String grauDificultat; // De 4 a 9c+

    public ViaEsportiva(int idVia, String nom, String orientacio, String estat, String ancoratges,
                        String tipusDeRoca, int idCreador, int idSector, int diesNoApte,
                        String restriccions, int idViaEsportiva, int llargada, String grauDificultat) {

        // Passo tota la informació comuna a la superclasse Via
        super(idVia, nom, orientacio, estat, ancoratges, tipusDeRoca, idCreador, idSector, diesNoApte, restriccions);

        this.idViaEsportiva = idViaEsportiva;
        this.llargada = llargada;
        this.grauDificultat = grauDificultat;
    }



    /**
     * Puc tornar el meu grau per mostrar-lo al menú de cerca per dificultat.
     */
    public String getGrauDificultat() {
        return grauDificultat;
    }

    // Getters i Setters

    public int getIdViaEsportiva() { return idViaEsportiva; }
    public void setIdViaEsportiva(int idViaEsportiva) { this.idViaEsportiva = idViaEsportiva; }

    public int getLlargada() { return llargada; }
    public void setLlargada(int llargada) { this.llargada = llargada; }

    public void setGrauDificultat(String grauDificultat) { this.grauDificultat = grauDificultat; }
}