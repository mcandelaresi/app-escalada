package helpers;

import model.Sector;
import model.enums.Popularitat;

import java.util.List;

//faig servir aquesta classe per validar sectors i tenir el menú més net.
public final class AuxSector {

    private AuxSector() {
    }

    public static String normalitzarPopularitat(String valor) {
        Popularitat popularitat = Popularitat.fromValor(valor);
        return popularitat == null ? valor.trim() : popularitat.getValor();
    }

    public static boolean existeixNomEnEscola(List<Sector> sectors, int idEscola, String nom) {
        if (sectors == null || nom == null) return false;
        for (Sector sector : sectors) {
            if (sector != null
                    && sector.getIdEscola() == idEscola
                    && sector.getNom() != null
                    && sector.getNom().equalsIgnoreCase(nom.trim())) {
                return true;
            }
        }
        return false;
    }
}



