package model.enums;

public enum TipusVia {
    ESPORTIVA("Esportiva"),
    CLASSICA("Classica"),
    GEL("Gel");

    private final String valor;

    TipusVia(String valor) {
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }

    public static TipusVia fromValor(String valor) {
        if (valor == null) return null;
        for (TipusVia tipus : values()) {
            if (tipus.valor.equalsIgnoreCase(valor.trim()) || tipus.name().equalsIgnoreCase(valor.trim())) {
                return tipus;
            }
        }
        return null;
    }
}

