package model.enums;

public enum Estil {
    ESPORTIVA("Esportiva"),
    CLASSICA("Clàssica"),
    GEL("Gel");

    private final String valor;

    Estil(String valor) {
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }

    public static Estil fromValor(String valor) {
        if (valor == null) return null;
        for (Estil e : values()) {
            if (e.valor.equalsIgnoreCase(valor.trim()) || e.name().equalsIgnoreCase(valor.trim())) {
                return e;
            }
        }
        return null;
    }
}

