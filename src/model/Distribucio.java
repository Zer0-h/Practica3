package model;

/**
 * Enum Distribucio: Representa els diferents tipus de distribució
 * utilitzats per generar punts en el model.
 *
 * Cada distribució té una descripció associada per a la seva visualització.
 *
 * Tipus de distribució:
 * - GAUSSIAN: Distribució Gaussiana (Normal).
 * - EXPONENCIAL: Distribució Exponencial.
 * - UNIFORME: Distribució Uniforme.
 *
 * @author tonitorres
 */
public enum Distribucio {
    GAUSSIAN("Gaussiana"),
    EXPONENCIAL("Exponencial"),
    UNIFORME("Uniforme");

    private final String description;

    /**
     * Constructor per inicialitzar la descripció de la distribució.
     *
     * @param d La descripció textual del tipus de distribució.
     */
    Distribucio(String d) {
        description = d;
    }

    /**
     * Retorna la descripció del tipus de distribució.
     *
     * @return Una cadena amb la descripció de la distribució.
     */
    @Override
    public String toString() {
        return description;
    }
}
