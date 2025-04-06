package model.procesos;

import controlador.Controlador;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import model.Metode;

public class ConvexHullProcess extends AbstractCalculProcess {

    public ConvexHullProcess(Controlador controlador) {
        super(controlador);
    }

    public ConvexHullProcess(Controlador controlador, Point2D.Double[] punts) {
        super(controlador, punts);
    }

    @Override
    protected void calcular() {
        List<Point2D.Double> hull = convexHull(punts);
        rotatingCalipers(hull);
    }

    private List<Point2D.Double> convexHull(Point2D.Double[] points) {
        List<Point2D.Double> hull = new ArrayList<>();

        // Ordenem els punts per X i després per Y
        List<Point2D.Double> sortedPoints = new ArrayList<>(List.of(points));
        sortedPoints.sort(Comparator.comparingDouble((Point2D.Double p) -> p.getX())
                                    .thenComparingDouble(p -> p.getY()));

        // Construcció de la part inferior del convex hull
        for (Point2D.Double p : sortedPoints) {
            while (hull.size() >= 2 && model.producteVectorial(hull.get(hull.size() - 2), hull.get(hull.size() - 1), p) <= 0) {
                hull.remove(hull.size() - 1);
            }
            hull.add(p);
        }

        // Construcció de la part superior del convex hull
        int t = hull.size() + 1;
        for (int i = sortedPoints.size() - 2; i >= 0; i--) {
            Point2D.Double p = sortedPoints.get(i);
            while (hull.size() >= t && model.producteVectorial(hull.get(hull.size() - 2), hull.get(hull.size() - 1), p) <= 0) {
                hull.remove(hull.size() - 1);
            }
            hull.add(p);
        }

        hull.remove(hull.size() - 1); // Eliminar el punt duplicat
        return hull;
    }

    private void rotatingCalipers(List<Point2D.Double> hull) {
        int hullSize = hull.size();
        int k = 1;

        for (int i = 0; i < hullSize; i++) {
            while (true) {
                double dist1 = hull.get(i).distance(hull.get((k + 1) % hullSize));
                double dist2 = hull.get(i).distance(hull.get(k % hullSize));

                if (dist1 > dist2) {
                    k = (k + 1) % hullSize;
                } else {
                    model.setSolucioSiEs(hull.get(i), hull.get(k % hullSize));
                    break;
                }
            }
        }
    }

    @Override
    protected Metode getMetode() {
        return Metode.CONVEX_HULL;
    }
}
