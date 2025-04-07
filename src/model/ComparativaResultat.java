package model;

import java.awt.geom.Point2D;

/**
 * Classe ComparativaResultat: Representa el resultat d'un procés de
 * comparativa.
 */
public class ComparativaResultat {

    private final String metode;
    private final double temps;
    private final Point2D.Double[] solucio;

    public ComparativaResultat(String metode, double temps, Point2D.Double[] solucio) {
        this.metode = metode;
        this.temps = temps;
        this.solucio = solucio;
    }

    public String getMetode() {
        return metode;
    }

    public double getTemps() {
        return temps;
    }

    public Point2D.Double[] getSolucio() {
        return solucio;
    }

    public double getDistanciaSolucio() {
        if (solucio == null || solucio.length != 2) {
            return 0;
        }

        return solucio[0].distance(solucio[1]);
    }
}
