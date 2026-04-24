package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe Escalador: gestiona la informació dels escaladors.
 */
public class Escalador {

    private int idEscalador;
    private String nom;
    private String alias;
    private int edat;
    private String nivellMax;
    private String estilPreferit;
    private int idViaMax;

    private List<Registre> historial;

    public Escalador(int idEscalador, String nom, String alias, int edat,
                     String nivellMax, String estilPreferit, int idViaMax) {

        this.idEscalador = idEscalador;
        this.nom = nom;
        this.alias = alias;
        this.edat = edat;
        this.nivellMax = nivellMax;
        this.estilPreferit = estilPreferit;
        this.idViaMax = idViaMax;
        this.historial = new ArrayList<>();
    }

    // Getters i setters

    public int getIdEscalador() {
        return idEscalador;
    }

    public void setIdEscalador(int idEscalador) {
        this.idEscalador = idEscalador;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public int getEdat() {
        return edat;
    }

    public void setEdat(int edat) {
        this.edat = edat;
    }

    public String getNivellMax() {
        return nivellMax;
    }

    public void setNivellMax(String nivellMax) {
        this.nivellMax = nivellMax;
    }

    public String getEstilPreferit() {
        return estilPreferit;
    }

    public void setEstilPreferit(String estilPreferit) {
        this.estilPreferit = estilPreferit;
    }

    public int getIdViaMax() {
        return idViaMax;
    }

    public void setIdViaMax(int idViaMax) {
        this.idViaMax = idViaMax;
    }

    public List<Registre> getHistorial() {
        return historial;
    }

    public void setHistorial(List<Registre> historial) {
        this.historial = historial;
    }

    /**
     * Comprova si dos escaladors tenen el mateix nivell màxim.
     */
    public boolean tincMateixNivell(Escalador altre) {
        return this.nivellMax != null && this.nivellMax.equals(altre.getNivellMax());
    }

    /**
     * Afegeix una nova ascensió a l'historial.
     */
    public void afegirAscensio(Registre registre) {
        this.historial.add(registre);
    }

    @Override
    public String toString() {
        return "Escalador{" +
                "idEscalador=" + idEscalador +
                ", nom='" + nom + '\'' +
                ", alias='" + alias + '\'' +
                ", edat=" + edat +
                ", nivellMax='" + nivellMax + '\'' +
                ", estilPreferit='" + estilPreferit + '\'' +
                ", idViaMax=" + idViaMax +
                '}';
    }
}