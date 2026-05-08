package helpers;


import model.enums.Estil;
import model.enums.GrauDificultat;



// tinc aquesta classe per validar escaladors i no barrejar-ho amb el menú.
public final class AuxEscalador {

    private AuxEscalador() {
    }

    public static String normalitzarEstil(String valor) {
        Estil estil = Estil.fromValor(valor);
        return estil == null ? valor.trim() : estil.getValor();
    }

    public static String normalitzarGrau(String valor) {
        GrauDificultat grau = GrauDificultat.fromValor(valor);
        return grau == null ? valor.trim() : grau.getValor();
    }

    public static boolean estilValid(String valor) {
        return Estil.fromValor(valor) != null;
    }



}



