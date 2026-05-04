package model.enums;

public enum Orientacio {
    N, NE, NO, SE, SO, E, O, S;

    public static Orientacio fromValor(String valor) {
        if (valor == null) return null;
        try {
            return Orientacio.valueOf(valor.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}

