package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe Poblacio.
 * Representa una població on hi ha escoles d'escalada.
 */
public class Poblacio {

    // Identificador únic de la població
    private int idPoblacio;

    // Nom de la població
    private String nom;

    // Relació: una població pot tenir múltiples escoles
    private final List<Escola> escoles;

    /**
     * Constructor principal.
     */
    public Poblacio(int idPoblacio, String nom) {
        this.idPoblacio = idPoblacio;
        this.nom = nom;
        this.escoles = new ArrayList<>();
    }

    /**
     * Afegeixo una escola a la població.
     */
    public void afegirEscola(Escola escola) {
        if (escola == null) return;
        escoles.add(escola);
    }

    /**
     * Retorno el nombre d'escoles de la població.
     */
    public int getNumeroEscoles() {
        return escoles.size();
    }

    // GETTERS I SETTERS

    public int getIdPoblacio() {
        return idPoblacio;
    }

    public void setIdPoblacio(int idPoblacio) {
        this.idPoblacio = idPoblacio;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public List<Escola> getEscoles() {
        return escoles;
    }
}