package helpers;

import model.Escola;
import model.enums.Popularitat;

import java.util.List;

//poso aquesta classe aquí per controlar escoles i no repetir codi al menú.
public final class AuxEscola {

    private AuxEscola() {
    }

    public static String normalitzarPopularitat(String valor) {
        Popularitat popularitat = Popularitat.fromValor(valor);
        return popularitat == null ? valor.trim() : popularitat.getValor();
    }

    public static boolean existeixNom(List<Escola> escoles, String nom) {
        if (escoles == null || nom == null) return false;
        for (Escola escola : escoles) {
            if (escola != null && escola.getNom() != null && escola.getNom().equalsIgnoreCase(nom.trim())) {
                return true;
            }
        }
        return false;
    }
}



