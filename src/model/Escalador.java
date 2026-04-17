package model;

import java.util.ArrayList;
import java.util.List;

/**
 *classe Escalador s'encarrega de gestionar la informació dels usuaris
 */
public class Escalador {
    private int idEscalador;
    private String nom;
    private String alies;
    private int edat;
    private String nivellMax;      // He de vigilar que estigui entre el 4 i el 9c+
    private String estilPreferit;  // Aquí guardo si m'agrada més l'esportiva, clàssica o gel
    private int idViaMax;          // Guardo la referència a la via on he fet el meu màxim nivell

    // Com que l'enunciat diu que l'historial està pendent però el diagrama ja el preveu,
    // m'he preparat aquesta llista per guardar totes les meves ascensions.
    private List<Historial> historial;

    public Escalador(int idEscalador, String nom, String alies, int edat, String nivellMax, String estilPreferit, int idViaMax) {
        this.idEscalador = idEscalador;
        this.nom = nom;
        this.alies = alies;
        this.edat = edat;
        this.nivellMax = nivellMax;
        this.estilPreferit = estilPreferit;
        this.idViaMax = idViaMax;
        // Inicialitzo la meva llista d'ascensions buida per anar-la omplint més tard.
        this.historial = new ArrayList<>();
    }

    // --- Getters i Setters ---

    public int getIdEscalador() { return idEscalador; }
    public void setIdEscalador(int idEscalador) { this.idEscalador = idEscalador; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getAlies() { return alies; }
    public void setAlies(String alies) { this.alies = alies; }

    public int getEdat() { return edat; }
    public void setEdat(int edat) { this.edat = edat; }

    public String getNivellMax() { return nivellMax; }
    public void setNivellMax(String nivellMax) { this.nivellMax = nivellMax; }

    public String getEstilPreferit() { return estilPreferit; }
    public void setEstilPreferit(String estilPreferit) { this.estilPreferit = estilPreferit; }

    public int getIdViaMax() { return idViaMax; }
    public void setIdViaMax(int idViaMax) { this.idViaMax = idViaMax; }

    /**
     * Amb aquest mètode puc comprovar si un altre company té el mateix
     * nivell que jo, complint així amb un dels requeriments del menú.
     */
    public boolean tincMateixNivell(Escalador unAltreEscalador) {
        return this.nivellMax.equals(unAltreEscalador.getNivellMax());
    }

    /**
     * Afegeixo una nova ascensió al meu llistat personal de rutes completades.
     */
    public void afegirAscensio(Historial registre) {
        this.historial.add(registre);
    }
}