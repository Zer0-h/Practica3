package model;

import controlador.Controlador;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class BruteForceProcess extends AbstractCalculProcess {

    public BruteForceProcess(Controlador controlador) {
        super(controlador);
    }

    public BruteForceProcess(Controlador controlador, Point2D.Double[] punts) {
        super(controlador, punts);
    }

    @Override
    protected void calcular() {
        int numThreads = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);

        int numPunts = punts.length;
        int blockSize = numPunts / numThreads;

        List<Future<Void>> futures = new ArrayList<>();

        for (int t = 0; t < numThreads; t++) {
            int start = t * blockSize;
            int end = (t == numThreads - 1) ? numPunts : (t + 1) * blockSize;

            Callable<Void> task = () -> {
                for (int i = start; i < end; i++) {
                    for (int j = i + 1; j < numPunts; j++) {
                        model.setSolucioSiEs(punts[i], punts[j]);
                    }
                }
                return null;
            };
            futures.add(executor.submit(task));
        }

        // Esperem que tots els fils acabin
        for (Future<Void> future : futures) {
            try {
                future.get(); // Esperem el resultat del fil
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        // Tanquem l'executor
        executor.shutdown();
    }

    @Override
    protected Metode getMetode() {
        return Metode.FUERZA_BRUTA;
    }
}
