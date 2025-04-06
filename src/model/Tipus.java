package model;

/**
 * Enum Tipus: Representa els tipus de càlcul que es poden realitzar
 * amb els punts generats: parella de punts més propera o més llunyana.
 *
 * Tipus disponibles:
 * - PROPER: Trobar la parella de punts més propera.
 * - LLUNY: Trobar la parella de punts més llunyana.
 *
 * @author tonitorres
 */
public enum Tipus {
    PROPER("Parella més propera"),
    LLUNY("Parella més llunyana");

    private final String description;

    /**
     * Constructor per inicialitzar la descripció del tipus.
     *
     * @param description La descripció textual del tipus de càlcul.
     */
    Tipus(String d) {
        description = d;
    }

    /**
     * Retorna la descripció del tipus de càlcul.
     *
     * @return Una cadena amb la descripció del tipus.
     */
    @Override
    public String toString() {
        return description;
    }
}
