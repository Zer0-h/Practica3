package procesos;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import model.Metode;

/**
 * Classe ConvexHullProcess: Implementa el càlcul de la parella de punts més
 * llunyana
 * utilitzant el mètode del convex hull amb Rotating Calipers (complexitat
 * O(n·log(n))).
 *
 * Utilitza el concepte de la geometria computacional per calcular l'envolupant
 * convexa
 * i després aplica l'algorisme de Rotating Calipers per trobar la distància
 * màxima.
 *
 * @author tonitorres
 */
public class ConvexHullProcess extends AbstractCalculProcess {

    /**
     * Constructor amb punts específics (utilitzat per a càlculs de constants).
     *
     * @param minimitzar Si el problema es per trobar la parella minima o
     *                   màxima.
     * @param punts      Conjunt de punts a utilitzar.
     */
    public ConvexHullProcess(boolean minimitzar, Point2D.Double[] punts) {
        super(minimitzar, punts);
    }

    /**
     * Mètode principal de càlcul que genera l'envolupant convexa i aplica el
     * mètode
     * de Rotating Calipers per trobar la parella de punts més llunyana.
     */
    @Override
    protected void calcular() {
        List<Point2D.Double> hull = convexHull(punts);
        rotatingCalipers(hull);
    }

    /**
     * Calcula l'envolupant convexa d'un conjunt de punts utilitzant el mètode
     * de Graham Scan.
     *
     * @param points Conjunt de punts d'entrada.
     *
     * @return Llista de punts que formen l'envolupant convexa.
     */
    private List<Point2D.Double> convexHull(Point2D.Double[] points) {
        List<Point2D.Double> hull = new ArrayList<>();

        // Ordenem els punts per X i després per Y per garantir l'ordre correcte
        List<Point2D.Double> sortedPoints = new ArrayList<>(List.of(points));
        sortedPoints.sort(Comparator.comparingDouble((Point2D.Double p) -> p.getX())
                .thenComparingDouble(p -> p.getY()));

        // Construcció de la part inferior del convex hull
        for (Point2D.Double p : sortedPoints) {
            while (hull.size() >= 2 && producteVectorial(hull.get(hull.size() - 2), hull.get(hull.size() - 1), p) <= 0) {
                hull.remove(hull.size() - 1);
            }
            hull.add(p);
        }

        // Construcció de la part superior del convex hull
        int t = hull.size() + 1;
        for (int i = sortedPoints.size() - 2; i >= 0; i--) {
            Point2D.Double p = sortedPoints.get(i);
            while (hull.size() >= t && producteVectorial(hull.get(hull.size() - 2), hull.get(hull.size() - 1), p) <= 0) {
                hull.remove(hull.size() - 1);
            }
            hull.add(p);
        }

        // Eliminar el punt duplicat resultant de la construcció
        hull.remove(hull.size() - 1);
        return hull;
    }

    /**
     * Algorisme de Rotating Calipers per trobar la màxima distància entre
     * parelles de punts
     * dins de l'envolupant convexa.
     *
     * @param hull Llista de punts que formen l'envolupant convexa.
     */
    private void rotatingCalipers(List<Point2D.Double> hull) {
        int hullSize = hull.size();
        int k = 1;

        // Recorregut de l'envolupant convexa per trobar la màxima distància
        for (int i = 0; i < hullSize; i++) {
            while (true) {
                double dist1 = hull.get(i).distance(hull.get((k + 1) % hullSize));
                double dist2 = hull.get(i).distance(hull.get(k % hullSize));

                // Si la distància augmenta, avancem el caliper
                if (dist1 > dist2) {
                    k = (k + 1) % hullSize;
                } else {
                    // Guardem la millor solució al model
                    setSolucio(hull.get(i), hull.get(k % hullSize));
                    break;
                }
            }
        }
    }

    /**
     * Calcula el producte vectorial entre tres punts.
     *
     * @param punt1
     * @param punt2
     * @param punt3
     *
     * @return Resultat del producte
     */
    public double producteVectorial(Point2D.Double punt1, Point2D.Double punt2, Point2D.Double punt3) {
        return (punt2.getX() - punt1.getX()) * (punt3.getY() - punt1.getY()) - (punt2.getY() - punt1.getY()) * (punt3.getX() - punt1.getX());
    }

    /**
     * Retorna el mètode utilitzat en aquesta classe (Convex Hull).
     *
     * @return El mètode CONVEX_HULL.
     */
    @Override
    public Metode getMetode() {
        return Metode.CONVEX_HULL;
    }
}
