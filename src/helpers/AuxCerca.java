package helpers;

import model.Escalador;
import model.Escola;
import model.Sector;
import model.Via;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.ToIntFunction;

//  poso aquesta classe aquí per fer les cerques i no repetir filtres al menú.
public final class AuxCerca {

    private AuxCerca() {
    }

    public static List<Via> viesDunaEscola(List<Via> vies, int idEscola) {
        List<Via> resultat = new ArrayList<>();
        if (vies == null) return resultat;
        for (Via via : vies) {
            if (via != null && via.getIdEscola() == idEscola) {
                resultat.add(via);
            }
        }
        return resultat;
    }

    public static List<Via> viesPerEstat(List<Via> vies, String estat) {
        List<Via> resultat = new ArrayList<>();
        if (vies == null || estat == null) return resultat;
        for (Via via : vies) {
            if (via != null && via.getEstat() != null && via.getEstat().equalsIgnoreCase(estat.trim())) {
                resultat.add(via);
            }
        }
        return resultat;
    }

    public static List<Escola> escolesAmbRestriccionsActives(List<Escola> escoles) {
        List<Escola> resultat = new ArrayList<>();
        if (escoles == null) return resultat;
        for (Escola escola : escoles) {
            if (escola != null && escola.getRestriccions() != null && !escola.getRestriccions().trim().isEmpty()) {
                resultat.add(escola);
            }
        }
        return resultat;
    }

    public static Map<String, List<Escalador>> escaladorsMateixNivell(List<Escalador> escaladors) {
        Map<String, List<Escalador>> grups = new LinkedHashMap<>();
        if (escaladors == null) return grups;
        for (Escalador escalador : escaladors) {
            if (escalador == null || escalador.getNivellMax() == null) continue;
            grups.computeIfAbsent(escalador.getNivellMax(), k -> new ArrayList<>()).add(escalador);
        }
        return grups;
    }

    public static List<Sector> sectorsAmbMesDeX(List<Sector> sectors, List<Via> vies, int x) {
        List<Sector> resultat = new ArrayList<>();
        if (sectors == null || vies == null) return resultat;
        Map<Integer, Integer> comptador = new LinkedHashMap<>();
        for (Via via : vies) {
            if (AuxVia.esApte(via)) {
                comptador.put(via.getIdSector(), comptador.getOrDefault(via.getIdSector(), 0) + 1);
            }
        }
        for (Sector sector : sectors) {
            if (sector != null && comptador.getOrDefault(sector.getIdSector(), 0) > x) {
                resultat.add(sector);
            }
        }
        return resultat;
    }

    public static List<Via> viesMesLlarguesDunaEscola(List<Via> vies, int idEscola, ToIntFunction<Via> longitud) {
        List<Via> resultat = new ArrayList<>();
        if (vies == null || longitud == null) return resultat;
        int max = -1;
        for (Via via : vies) {
            if (via == null || via.getIdEscola() != idEscola) continue;
            int ll = longitud.applyAsInt(via);
            if (ll > max) {
                max = ll;
                resultat.clear();
                resultat.add(via);
            } else if (ll == max) {
                resultat.add(via);
            }
        }
        return resultat;
    }
}







