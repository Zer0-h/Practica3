package model;

import controlador.Controlador;
import java.awt.geom.Point2D;
import java.util.Arrays;
import java.util.Comparator;
import java.util.concurrent.*;

public class DivideAndConquerProcess extends AbstractCalculProcess {

    // Pool de fils limitat segons el nombre de nuclis disponibles
    private static final int MAX_THREADS = Runtime.getRuntime().availableProcessors();
    private static final ExecutorService executor = Executors.newFixedThreadPool(MAX_THREADS);

    public DivideAndConquerProcess(Controlador controlador) {
        super(controlador);
    }

    public DivideAndConquerProcess(Controlador controlador, Point2D.Double[] punts) {
        super(controlador, punts);
    }

    @Override
    protected void calcular() {
        try {
            Arrays.sort(punts, Comparator.comparingDouble(Point2D.Double::getX));
            divideAndConquer(punts, 0, punts.length - 1);
        } finally {
            executor.shutdown();
        }
    }

    private double divideAndConquer(Point2D.Double[] punts, int start, int end) {
        if (end - start <= 3) {
            return calcularDirectament(punts, start, end);
        }

        int mid = (start + end) / 2;

        // Comprovació de la disponibilitat de fils
        if (executor.isShutdown() || executor instanceof ThreadPoolExecutor
                && ((ThreadPoolExecutor) executor).getActiveCount() >= MAX_THREADS) {
            // Si el pool està saturat, fem el càlcul seqüencial
            double distEsq = divideAndConquer(punts, start, mid);
            double distDreta = divideAndConquer(punts, mid + 1, end);
            return combinarResultats(punts, start, end, mid, Math.min(distEsq, distDreta));
        }

        // Si hi ha recursos, executem les dues parts en paral·lel
        Future<Double> leftFuture = executor.submit(() -> divideAndConquer(punts, start, mid));
        Future<Double> rightFuture = executor.submit(() -> divideAndConquer(punts, mid + 1, end));

        try {
            double distanciaEsq = leftFuture.get();
            double distanciaDreta = rightFuture.get();
            double millorDistancia = model.isMinimizar() ? Math.min(distanciaEsq, distanciaDreta) : Math.max(distanciaEsq, distanciaDreta);
            return combinarResultats(punts, start, end, mid, millorDistancia);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return model.isMinimizar() ? Double.MAX_VALUE : Double.MIN_VALUE;
        }
    }

    private double calcularDirectament(Point2D.Double[] punts, int start, int end) {
        double millorDistancia = model.isMinimizar() ? Double.MAX_VALUE : Double.MIN_VALUE;
        for (int i = start; i <= end; i++) {
            for (int j = i + 1; j <= end; j++) {
                double d = punts[i].distance(punts[j]);
                model.setSolucioSiEs(punts[i], punts[j]);
                if (model.isMinimizar()) {
                    millorDistancia = Math.min(millorDistancia, d);
                } else {
                    millorDistancia = Math.max(millorDistancia, d);
                }
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
                if (model.isMinimizar()) {
                    millorDistancia = Math.min(millorDistancia, dist);
                } else {
                    millorDistancia = Math.max(millorDistancia, dist);
                }
            }
        }

        return millorDistancia;
    }

    @Override
    protected Metode getMetode() {
        return Metode.DIVIDE_Y_VENCERAS;
    }
}
