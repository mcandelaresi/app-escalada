package excepcions;

public class EntradaNoValidaException extends RuntimeException {
    public EntradaNoValidaException(String missatge) {
        super(missatge);
    }
}

