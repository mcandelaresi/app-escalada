package model.enums;

public enum EstatVia {
    APTE("Apte"),
    CONSTRUCCIO("Construccio"),
    TANCADA("Tancada");

    private final String valor;

    EstatVia(String valor) {
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }

    public static EstatVia fromValor(String valor) {
        if (valor == null) return null;
        for (EstatVia estat : values()) {
            if (estat.valor.equalsIgnoreCase(valor.trim()) || estat.name().equalsIgnoreCase(valor.trim())) {
                return estat;
            }
        }
        return null;
    }
}

