package model.enums;

public enum TipusRoca {
    CONGLOMERAT("Conglomerat"),
    GRANIT("Granit"),
    CALCARIA("Calcaria"),
    ARENISCA("Arenisca"),
    ALTRES("Altres");

    private final String valor;

    TipusRoca(String valor) {
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }

    public static TipusRoca fromValor(String valor) {
        if (valor == null) return null;
        for (TipusRoca tipus : values()) {
            if (tipus.valor.equalsIgnoreCase(valor.trim()) || tipus.name().equalsIgnoreCase(valor.trim())) {
                return tipus;
            }
        }
        return null;
    }
}

