package model.enums;

public enum TipusAncoratge {
    SPITS("Spits"),
    PARABOLTS("Parabolts"),
    QUIMICS("Químics"),
    FRIENDS("Friends"),
    TASCONS("Tascons"),
    BAGUES("Bagues"),
    PITONS("Pitons"),
    TRICAMS("Tricams"),
    BIGBROS("BigBros");

    private final String valor;

    TipusAncoratge(String valor) {
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }

    public static TipusAncoratge fromValor(String valor) {
        if (valor == null) return null;
        for (TipusAncoratge t : values()) {
            if (t.valor.equalsIgnoreCase(valor.trim()) || t.name().equalsIgnoreCase(valor.trim())) {
                return t;
            }
        }
        return null;
    }
}

