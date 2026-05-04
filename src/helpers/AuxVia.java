package helpers;

import model.Via;
import model.enums.EstatVia;
import model.enums.GrauDificultat;
import model.enums.Orientacio;
import model.enums.TipusAncoratge;
import model.enums.TipusRoca;
import model.enums.TipusVia;

import java.util.List;

// deixo aquesta classe per validar les vies i no carregar el menú amb tanta lògica.
public final class AuxVia {

    private AuxVia() {
    }

    public static String normalitzarGrau(String valor) {
        GrauDificultat grau = GrauDificultat.fromValor(valor);
        return grau == null ? valor.trim() : grau.getValor();
    }

    public static String normalitzarOrientacio(String valor) {
        Orientacio orientacio = Orientacio.fromValor(valor);
        return orientacio == null ? valor.trim() : orientacio.name();
    }

    public static EstatVia normalitzarEstat(String valor) {
        EstatVia estat = EstatVia.fromValor(valor);
        if (estat == null) throw new IllegalArgumentException("Estat no vàlid");
        return estat;
    }

    public static String normalitzarTipusRoca(String valor) {
        TipusRoca roca = TipusRoca.fromValor(valor);
        if (roca == null) throw new IllegalArgumentException("Tipus de roca no vàlid");
        return roca.getValor();
    }

    public static String normalitzarTipusAncoratge(String valor, String tipusVia) {
        TipusAncoratge a = TipusAncoratge.fromValor(valor);
        if (a == null) throw new IllegalArgumentException("Ancoratge no vàlid");
        if (TipusVia.ESPORTIVA.getValor().equalsIgnoreCase(tipusVia) && (a == TipusAncoratge.SPITS || a == TipusAncoratge.PARABOLTS || a == TipusAncoratge.QUIMICS)) return a.getValor();
        if (TipusVia.CLASSICA.getValor().equalsIgnoreCase(tipusVia)) return a.getValor();
        if (TipusVia.GEL.getValor().equalsIgnoreCase(tipusVia) && (a == TipusAncoratge.FRIENDS || a == TipusAncoratge.TASCONS || a == TipusAncoratge.BAGUES || a == TipusAncoratge.PITONS || a == TipusAncoratge.TRICAMS || a == TipusAncoratge.BIGBROS)) return a.getValor();
        throw new IllegalArgumentException("Ancoratge no vàlid per aquest tipus de via");
    }

    public static boolean grauValidPerTipus(String valor, TipusVia tipus) {
        if (tipus == null) return false;
        if (tipus == TipusVia.ESPORTIVA) return GrauDificultat.esValidaPerEsportiva(valor);
        if (tipus == TipusVia.CLASSICA) return GrauDificultat.esValidaPerClassica(valor);
        if (tipus == TipusVia.GEL) return GrauDificultat.esValidaPerGel(valor);
        return false;
    }

    public static boolean esApte(Via via) {
        return via != null && via.getEstat() != null && EstatVia.APTE.getValor().equalsIgnoreCase(via.getEstat());
    }

    public static boolean nomDuplicatEnEscola(List<Via> vies, int idEscola, String nom) {
        if (vies == null || nom == null) return false;
        for (Via via : vies) {
            if (via != null && via.getIdEscola() == idEscola && via.getNom() != null && via.getNom().equalsIgnoreCase(nom.trim())) {
                return true;
            }
        }
        return false;
    }
}



