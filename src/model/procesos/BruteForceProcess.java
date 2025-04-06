package model.procesos;

import controlador.Controlador;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import model.Metode;

/**
 * Classe BruteForceProcess: Implementa el càlcul de la parella de punts més propera o més llunyana
 * utilitzant el mètode de força bruta (complexitat O(n²)).
 *
 * Utilitza paral·lelisme amb un ExecutorService per optimitzar el càlcul.
 * Cada fil calcula distàncies en un bloc diferent de punts.
 *
 * @autor tonitorres
 */
public class BruteForceProcess extends AbstractCalculProcess {

    /**
     * Constructor per defecte: utilitza els punts del model.
     * @param controlador El controlador de l'aplicació.
     */
    public BruteForceProcess(Controlador controlador) {
        super(controlador);
    }

    /**
     * Constructor amb punts específics (utilitzat per a càlculs de constants).
     * @param controlador El controlador de l'aplicació.
     * @param punts Conjunt de punts a utilitzar.
     */
    public BruteForceProcess(Controlador controlador, Point2D.Double[] punts) {
        super(controlador, punts);
    }

    /**
     * Mètode principal de càlcul. Divideix els punts en blocs per paral·lelitzar el càlcul
     * de distàncies utilitzant múltiples fils.
     */
    @Override
    protected void calcular() {
        int numThreads = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);

        int numPunts = punts.length;
        int blockSize = numPunts / numThreads;

        List<Future<Void>> futures = new ArrayList<>();

        // Creació de tasques i enviament a l'executor
        for (int t = 0; t < numThreads; t++) {
            int start = t * blockSize;
            int end = (t == numThreads - 1) ? numPunts : (t + 1) * blockSize;

            futures.add(executor.submit(crearTascaDistancia(start, end, numPunts)));
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

    /**
     * Crea una tasca de càlcul de distància per a un bloc de punts.
     * @param start Índex d'inici del bloc.
     * @param end Índex de final del bloc.
     * @param numPunts Nombre total de punts.
     * @return Una tasca Callable que calcula les distàncies en el bloc indicat.
     */
    private Callable<Void> crearTascaDistancia(int start, int end, int numPunts) {
        return () -> {
            for (int i = start; i < end; i++) {
                for (int j = i + 1; j < numPunts; j++) {
                    model.setSolucioSiEs(punts[i], punts[j]);
                }
            }
            return null;
        };
    }

    /**
     * Retorna el mètode de càlcul utilitzat en aquesta classe (Força Bruta).
     * @return El mètode FORCA_BRUTA.
     */
    @Override
    protected Metode getMetode() {
        return Metode.FORCA_BRUTA;
    }
}
