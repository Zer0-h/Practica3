package model.procesos;

import java.awt.geom.Point2D;
import java.util.Arrays;
import java.util.Comparator;
import java.util.concurrent.RecursiveTask;
import model.Model;

public class DivideAndConquerRecursiveTask extends RecursiveTask<Double> {
    private final Point2D.Double[] punts;
    private final int start, end;
    private final Model model;

    public DivideAndConquerRecursiveTask(Point2D.Double[] punts, int start, int end, Model model) {
        this.punts = punts;
        this.start = start;
        this.end = end;
        this.model = model;
    }

    @Override
    protected Double compute() {
        if (end - start <= 3) {
            return calcularDirectament(punts, start, end);
        }

        int mid = (start + end) / 2;

        // Dividir el problema en dues tasques paral·leles
        DivideAndConquerRecursiveTask leftTask = new DivideAndConquerRecursiveTask(punts, start, mid, model);
        DivideAndConquerRecursiveTask rightTask = new DivideAndConquerRecursiveTask(punts, mid + 1, end, model);

        // Llançar la tasca esquerra i calcular la dreta
        leftTask.fork();
        double distDreta = rightTask.compute();
        double distEsq = leftTask.join();

        double millorDistancia = getMillorDistancia(distEsq, distDreta);

        return combinarResultats(punts, start, end, mid, millorDistancia);
    }

    private double getMillorDistancia(double dist1, double dist2) {
        if (model.isMinimizar()) {
            return Math.min(dist1, dist2);
        } else {
            return Math.max(dist1, dist2);
        }
    }

    private double calcularDirectament(Point2D.Double[] punts, int start, int end) {
        double millorDistancia = model.isMinimizar() ? Double.MAX_VALUE : Double.MIN_VALUE;

        for (int i = start; i <= end; i++) {
            for (int j = i + 1; j <= end; j++) {
                double d = punts[i].distance(punts[j]);
                model.setSolucioSiEs(punts[i], punts[j]);
                millorDistancia = getMillorDistancia(millorDistancia, d);
            }
        }
        return millorDistancia;
    }

    private double combinarResultats(Point2D.Double[] punts, int start, int end, int mid, double millorDistancia) {
        double midX = punts[mid].getX();
        Point2D.Double[] franja = new Point2D.Double[end - start + 1];
        int idx = 0;

        for (int i = start; i <= end; i++) {
            if (Math.abs(punts[i].getX() - midX) < millorDistancia) {
                franja[idx++] = punts[i];
            }
        }

        Arrays.sort(franja, 0, idx, Comparator.comparingDouble(p -> p.getY()));

        for (int i = 0; i < idx; i++) {
            for (int j = i + 1; j < idx && (franja[j].getY() - franja[i].getY()) < millorDistancia; j++) {
                double dist = franja[i].distance(franja[j]);
                model.setSolucioSiEs(franja[i], franja[j]);
                millorDistancia = getMillorDistancia(millorDistancia, dist);
            }
        }

        return millorDistancia;
    }
}
