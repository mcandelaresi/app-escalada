package model;

/**
 * Classe abstracta Via.
 * Representa l'esquelet comú de qualsevol tipus de via d'escalada
 * (esportiva, clàssica o gel).
 */
public abstract class Via {

    // Identificador únic de la via
    private int idVia;

    // Nom de la via
    private String nom;

    // Grau de dificultat (ex: 6a, 7b+...)
    private String grau;

    // Orientació de la via (N, NE, S...)
    private String orientacio;

    // Estat actual de la via (Apte, Construccio, Tancada)
    private String estat;

    // Data en què es va canviar l'estat de la via
    private String dataEstat;

    // Tipus de via (esportiva, clàssica o gel)
    private String tipus;

    // Tipus d’ancoratges utilitzats a la via
    private String ancoratges;

    // Tipus de roca (granit, calcària, etc.)
    private String tipusDeRoca;

    // Escalador que ha creat la via (FK)
    private int idCreador;

    // Sector on es troba la via (FK)
    private int idSector;

    // Escola on es troba la via (FK)
    private int idEscola;

    // Restriccions (ex: prohibicions temporals per fauna, clima, etc.)
    private String restriccions;

    /**
     * Constructor principal de la classe Via.
     */
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
     * Comprova si la via es pot escalar actualment.
     * Només es pot escalar si l'estat és "Apte".
     */
    public boolean esPotEscalar() {
        return "apte".equalsIgnoreCase(this.estat);
    }

    // GETTERS I SETTERS

    public int getIdVia() {
        return idVia;
    }

    public void setIdVia(int idVia) {
        this.idVia = idVia;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getGrau() {
        return grau;
    }

    public void setGrau(String grau) {
        this.grau = grau;
    }

    public String getOrientacio() {
        return orientacio;
    }

    public void setOrientacio(String orientacio) {
        this.orientacio = orientacio;
    }

    public String getEstat() {
        return estat;
    }

    public void setEstat(String estat) {
        this.estat = estat;
    }

    public String getDataEstat() {
        return dataEstat;
    }

    public void setDataEstat(String dataEstat) {
        this.dataEstat = dataEstat;
    }

    public String getTipus() {
        return tipus;
    }

    public void setTipus(String tipus) {
        this.tipus = tipus;
    }

    public String getAncoratges() {
        return ancoratges;
    }

    public void setAncoratges(String ancoratges) {
        this.ancoratges = ancoratges;
    }

    public String getTipusDeRoca() {
        return tipusDeRoca;
    }

    public void setTipusDeRoca(String tipusDeRoca) {
        this.tipusDeRoca = tipusDeRoca;
    }

    public int getIdCreador() {
        return idCreador;
    }

    public void setIdCreador(int idCreador) {
        this.idCreador = idCreador;
    }

    public int getIdSector() {
        return idSector;
    }

    public void setIdSector(int idSector) {
        this.idSector = idSector;
    }

    public int getIdEscola() {
        return idEscola;
    }

    public void setIdEscola(int idEscola) {
        this.idEscola = idEscola;
    }

    public String getRestriccions() {
        return restriccions;
    }

    public void setRestriccions(String restriccions) {
        this.restriccions = restriccions;
    }
}