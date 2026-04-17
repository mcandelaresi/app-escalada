package model;

/**
 *classe abstracta Via l'esquelet de qualsevol ruta d'escalada.
 */
public abstract class Via {
    private int idVia;
    private String nom;
    private String orientacio; // Recordem validar N, NE, NO...
    private String estat;      // Apte, Construcció, Tancada
    private String ancoratges;
    private String tipusDeRoca;
    private int idCreador;
    private int idSector;


    // L'enunciat diu que si l'estat és "Construcció" o "Tancada",
    // hem de definir un temps de bloqueig.
    private int diesNoApte;

    // L'enunciat diu que tant les Vies com els Sectors poden tenir restriccions
    private String restriccions;

    public Via(int idVia, String nom, String orientacio, String estat, String ancoratges,
               String tipusDeRoca, int idCreador, int idSector, int diesNoApte, String restriccions) {
        this.idVia = idVia;
        this.nom = nom;
        this.orientacio = orientacio;
        this.estat = estat;
        this.ancoratges = ancoratges;
        this.tipusDeRoca = tipusDeRoca;
        this.idCreador = idCreador;
        this.idSector = idSector;
        this.diesNoApte = diesNoApte;
        this.restriccions = restriccions;
    }

    // --- Els meus mètodes de consulta ---

    /**
     * Comprovo si estic disponible per ser escalada.
     */
    public boolean esPotEscalar() {
        return "Apte".equalsIgnoreCase(this.estat);
    }

    // Getters i Setters

    public int getDiesNoApte() { return diesNoApte; }
    public void setDiesNoApte(int diesNoApte) { this.diesNoApte = diesNoApte; }

    public String getRestriccions() { return restriccions; }
    public void setRestriccions(String restriccions) { this.restriccions = restriccions; }

}