package model.enums;

public enum Popularitat {
    BAIXA("Baixa"),
    MITJANA("Mitjana"),
    ALTA("Alta");

    private final String valor;

    Popularitat(String valor) {
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }

    public static Popularitat fromValor(String valor) {
        if (valor == null) return null;
        for (Popularitat p : values()) {
            if (p.valor.equalsIgnoreCase(valor.trim()) || p.name().equalsIgnoreCase(valor.trim())) {
                return p;
            }
        }
        return null;
    }
}

