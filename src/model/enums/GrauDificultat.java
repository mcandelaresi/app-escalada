package model.enums;

public enum GrauDificultat {

    G4("4"), G4P("4+"),
    G5("5"), G5P("5+"),
    G6A("6a"), G6AP("6a+"),
    G6B("6b"), G6BP("6b+"),
    G6C("6c"), G6CP("6c+"),
    G7A("7a"), G7AP("7a+"),
    G7B("7b"), G7BP("7b+"),
    G7C("7c"), G7CP("7c+"),
    G8A("8a"), G8AP("8a+"),
    G8B("8b"), G8BP("8b+"),
    G8C("8c"), G8CP("8c+"),
    G9A("9a"), G9AP("9a+"),
    G9B("9b"), G9BP("9b+"),
    G9C("9c"), G9CP("9c+");

    private final String valor;

    GrauDificultat(String valor) {
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }

    public static GrauDificultat fromValor(String valor) {
        if (valor == null) return null;
        for (GrauDificultat grau : values()) {
            if (grau.valor.equalsIgnoreCase(valor.trim())) {
                return grau;
            }
        }
        return null;
    }

    public static boolean esValidaPerEsportiva(String valor) {
        GrauDificultat grau = fromValor(valor);
        return grau != null;
    }

    public static boolean esValidaPerClassica(String valor) {
        GrauDificultat grau = fromValor(valor);
        return grau != null && grau.ordinal() <= G8B.ordinal();
    }

    public static boolean esValidaPerGel(String valor) {
        GrauDificultat grau = fromValor(valor);
        return grau != null && grau.ordinal() <= G8B.ordinal();
    }
}