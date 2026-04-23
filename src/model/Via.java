package model;

/**
 * Classe abstracta Via: esquelet de qualsevol ruta d'escalada.
 */
public abstract class Via {

    private int idVia;
    private String nom;
    private String grau;
    private String orientacio;
    private String estat; // Apte, Construccio, Tancada
    private String dataEstat;

    private String tipus; // esportiva, classica, gel

    private String ancoratges;
    private String tipusDeRoca;

    private int idCreador;
    private int idSector;
    private int idEscola;

    private String restriccions;

    public Via(int idVia, String nom, String grau, String orientacio,
               String estat, String dataEstat, String tipus,
               String ancoratges, String tipusDeRoca,
               int idCreador, int idSector, int idEscola,
               String restriccions) {

        this.idVia = idVia;
        this.nom = nom;
        this.grau = grau;
        this.orientacio = orientacio;
        this.estat = estat;
        this.dataEstat = dataEstat;
        this.tipus = tipus;
        this.ancoratges = ancoratges;
        this.tipusDeRoca = tipusDeRoca;
        this.idCreador = idCreador;
        this.idSector = idSector;
        this.idEscola = idEscola;
        this.restriccions = restriccions;
    }

    /**
     * Comprova si la via està disponible.
     */
    public boolean esPotEscalar() {
        return "apte".equalsIgnoreCase(this.estat);
    }

    // getters i setters
    // ID
    public int getIdVia() { return idVia; }
    public void setIdVia(int idVia) { this.idVia = idVia; }

    // Nom
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    // Grau
    public String getGrau() { return grau; }
    public void setGrau(String grau) { this.grau = grau; }

    // Orientació
    public String getOrientacio() { return orientacio; }
    public void setOrientacio(String orientacio) { this.orientacio = orientacio; }

    // Estat
    public String getEstat() { return estat; }
    public void setEstat(String estat) { this.estat = estat; }

    // Data estat
    public String getDataEstat() { return dataEstat; }
    public void setDataEstat(String dataEstat) { this.dataEstat = dataEstat; }

    // Tipus
    public String getTipus() { return tipus; }
    public void setTipus(String tipus) { this.tipus = tipus; }

    // Ancoratges
    public String getAncoratges() { return ancoratges; }
    public void setAncoratges(String ancoratges) { this.ancoratges = ancoratges; }

    // Tipus de roca
    public String getTipusDeRoca() { return tipusDeRoca; }
    public void setTipusDeRoca(String tipusDeRoca) { this.tipusDeRoca = tipusDeRoca; }

    // Creador
    public int getIdCreador() { return idCreador; }
    public void setIdCreador(int idCreador) { this.idCreador = idCreador; }

    // Sector
    public int getIdSector() { return idSector; }
    public void setIdSector(int idSector) { this.idSector = idSector; }

    // Escola
    public int getIdEscola() { return idEscola; }
    public void setIdEscola(int idEscola) { this.idEscola = idEscola; }

    // Restriccions
    public String getRestriccions() { return restriccions; }
    public void setRestriccions(String restriccions) { this.restriccions = restriccions; }
}