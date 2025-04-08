package procesos;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import model.Metode;

/**
 * Classe BruteForceProcess: Implementa el càlcul de la parella de punts més
 * propera o més llunyana
 * utilitzant el mètode de força bruta (complexitat O(n²)).
 *
 * Utilitza paral·lelisme amb un ExecutorService per optimitzar el càlcul.
 * Cada fil calcula distàncies en un bloc diferent de punts.
 *
 * @autor tonitorres
 */
public class BruteForceProcess extends AbstractCalculProcess {

    /**
     * Constructor amb punts específics (utilitzat per a càlculs de constants).
     *
     * @param minimitzar Si el problema es per trobar la parella minima o
     *                   màxima.
     * @param punts      Conjunt de punts a utilitzar.
     */
    public BruteForceProcess(boolean minimitzar, Point2D.Double[] punts) {
        super(minimitzar, punts);
    }

    /**
     * Mètode principal de càlcul. Divideix els punts en blocs per
     * paral·lelitzar el càlcul
     * de distàncies utilitzant múltiples fils.
     */
    @Override
    protected void calcular() {
        int numThreads = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);

        int numPunts = punts.length;
        int blockSize = numPunts / numThreads;

        ArrayList<Future<?>> futures = new ArrayList<>();
        // Creació de tasques i enviament a l'executor
        for (int t = 0; t < numThreads; t++) {
            int start = t * blockSize;
            int end = (t == numThreads - 1) ? numPunts : (t + 1) * blockSize;
            futures.add(executor.submit(() -> {
                for (int i = start; i < end; i++) {
                    for (int j = i + 1; j < numPunts; j++) {
                        setSolucio(punts[i], punts[j]);
                    }
                }
            }));
        }

        // Iniciam totes les tasques de forma concurrent
        for (Future<?> future : futures) {
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Retorna el mètode de càlcul utilitzat en aquesta classe (Força Bruta).
     *
     * @return El mètode FORCA_BRUTA.
     */
    @Override
    public Metode getMetode() {
        return Metode.FORCA_BRUTA;
    }
}
