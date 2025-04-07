package model;

/**
 * Enum Metode: Representa els diferents mètodes algorísmics per calcular
 * la parella de punts més pròxima o més distant en un conjunt de punts.
 *
 * Cada mètode té una descripció associada que inclou la complexitat
 * computacional.
 *
 * Tipus de mètodes:
 * - FORCA_BRUTA: Algorisme clàssic amb complexitat O(n²).
 * - DIVIDEIX: Algorisme de Divideix i Venceràs amb complexitat O(n·log(n)).
 * - CONVEX_HULL: Algorisme basat en Convex Hull amb Rotating Calipers amb
 * complexitat O(n·log(n)).
 *
 * @author tonitorres
 */
public enum Metode {
    FORCA_BRUTA("Clàssic O(n²)"),
    DIVIDEIX("Divideix i Venceràs O(n·log(n))"),
    CONVEX_HULL("Convex Hull + Rotating Calipers O(n·log(n))");

    private final String description;

    /**
     * Constructor per inicialitzar la descripció del mètode.
     *
     * @param description La descripció textual del mètode i la seva
     *                    complexitat.
     */
    Metode(String d) {
        description = d;
    }

    /**
     * Retorna la descripció del mètode amb la seva complexitat.
     *
     * @return Una cadena amb la descripció del mètode.
     */
    @Override
    public String toString() {
        return description;
    }
}
