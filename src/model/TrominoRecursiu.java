package model;

import controlador.Controlador;
import controlador.Notificacio;
import controlador.Notificar;
import java.util.ArrayList;
import java.util.concurrent.*;

/**
 * Classe encarregada de resoldre el problema del Tromino de manera recursiva.
 * Només la primera crida utilitza fils per paral·lelitzar la resolució.
 * Implementa el patró d'esdeveniments per comunicar-se amb la resta del
 * programa.
 *
 * @author tonitorres
 */
public class TrominoRecursiu extends Thread implements Notificar {

    private boolean aturat;
    private final Controlador controlador;
    private final Model model;
    private ExecutorService executor;

    /**
     * Constructor que inicialitza la instància del controlador.
     *
     * @param c Instància del controlador principal
     */
    public TrominoRecursiu(Controlador c) {
        this.controlador = c;
        this.model = c.getModel();
    }

    /**
     * Mètode principal d'execució del fil. Crida el procés de resolució del
     * problema del Tromino i mesura el temps d'execució.
     */
    @Override
    public void run() {
        aturat = false;
        executor = Executors.newFixedThreadPool(4);

        long iniciTemps = System.nanoTime();

        try {
            resoldreTromino(model.getMidaTauler(), 0, 0, model.getForatX(), model.getForatY(), true);
        } finally {
            executor.shutdown();
            try {
                if (!executor.awaitTermination(10, TimeUnit.SECONDS)) {
                    executor.shutdownNow();
                }
            } catch (InterruptedException e) {
                executor.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }

        long tempsTotal = System.nanoTime() - iniciTemps;
        model.calculaConstantTromino(tempsTotal);
        model.setTempsExecucio(tempsTotal / 1_000_000_000.0);

        controlador.notificar(Notificacio.FINALITZA);
    }

    /**
     * Mètode recursiu per resoldre el problema del Tromino.
     * La primera crida utilitza fils, després es fa recursió normal.
     *
     * @param mida         Mida de la submatriu actual
     * @param iniciX       Coordenada X inicial
     * @param iniciY       Coordenada Y inicial
     * @param foratX       Coordenada X del forat
     * @param foratY       Coordenada Y del forat
     * @param utilitzaFils Indica si s'ha d'executar amb fils (només en la
     *                     primera crida)
     */
    private void resoldreTromino(int mida, int iniciX, int iniciY, int foratX, int foratY, boolean utilitzaFils) {
        if (aturat) {
            return;
        }

        int numTromino = model.incrementaIOBtenirTrominoActual();

        if (mida == 2) { // Cas base
            colocaTrominoBase(iniciX, iniciY, numTromino);
            return;
        }

        int centreX = iniciX + mida / 2 - 1;
        int centreY = iniciY + mida / 2 - 1;

        boolean foratEsqSup = foratX < centreX + 1 && foratY < centreY + 1;
        boolean foratDretSup = foratX < centreX + 1 && foratY >= centreY + 1;
        boolean foratEsqInf = foratX >= centreX + 1 && foratY < centreY + 1;
        boolean foratDretInf = foratX >= centreX + 1 && foratY >= centreY + 1;

        // Col·locar tromino al centre cobrint els 3 quadrants restants
        if (!foratEsqSup) {
            model.colocaTromino(centreX, centreY, numTromino);
        }
        if (!foratDretSup) {
            model.colocaTromino(centreX, centreY + 1, numTromino);
        }
        if (!foratEsqInf) {
            model.colocaTromino(centreX + 1, centreY, numTromino);
        }
        if (!foratDretInf) {
            model.colocaTromino(centreX + 1, centreY + 1, numTromino);
        }

        actualitzaVista();
        pausaExecucio();

        if (utilitzaFils) { // Només la primera crida utilitza fils
            ArrayList<Future<?>> futures = new ArrayList<>();
            futures.add(executor.submit(() -> resoldreTromino(mida / 2, iniciX, iniciY, foratEsqSup ? foratX : centreX, foratEsqSup ? foratY : centreY, false)));
            futures.add(executor.submit(() -> resoldreTromino(mida / 2, iniciX, iniciY + mida / 2, foratDretSup ? foratX : centreX, foratDretSup ? foratY : centreY + 1, false)));
            futures.add(executor.submit(() -> resoldreTromino(mida / 2, iniciX + mida / 2, iniciY, foratEsqInf ? foratX : centreX + 1, foratEsqInf ? foratY : centreY, false)));
            futures.add(executor.submit(() -> resoldreTromino(mida / 2, iniciX + mida / 2, iniciY + mida / 2, foratDretInf ? foratX : centreX + 1, foratDretInf ? foratY : centreY + 1, false)));

            try {
                for (Future<?> future : futures) {
                    future.get();
                }
            } catch (InterruptedException | ExecutionException e) {
                Thread.currentThread().interrupt();
            }
        } else { // La resta de crides es fan de manera recursiva normal
            resoldreTromino(mida / 2, iniciX, iniciY, foratEsqSup ? foratX : centreX, foratEsqSup ? foratY : centreY, false);
            resoldreTromino(mida / 2, iniciX, iniciY + mida / 2, foratDretSup ? foratX : centreX, foratDretSup ? foratY : centreY + 1, false);
            resoldreTromino(mida / 2, iniciX + mida / 2, iniciY, foratEsqInf ? foratX : centreX + 1, foratEsqInf ? foratY : centreY, false);
            resoldreTromino(mida / 2, iniciX + mida / 2, iniciY + mida / 2, foratDretInf ? foratX : centreX + 1, foratDretInf ? foratY : centreY + 1, false);
        }
    }

    /**
     * Notifica la vista perquè es refresqui el tauler.
     */
    private void actualitzaVista() {
        controlador.notificar(Notificacio.PINTAR);
    }

    /**
     * Fa una pausa curta per evitar que el procés sigui massa ràpid per
     * visualització.
     */
    private void pausaExecucio() {
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Atura l'execució de la resolució del Tromino.
     */
    public void atura() {
        aturat = true;
    }

    /**
     * Coloca un tromino al cas base 2x2.
     */
    private void colocaTrominoBase(int x, int y, int numTromino) {
        if (model.esCasellaBuida(x, y)) {
            model.colocaTromino(x, y, numTromino);
        }
        if (model.esCasellaBuida(x, y + 1)) {
            model.colocaTromino(x, y + 1, numTromino);
        }
        if (model.esCasellaBuida(x + 1, y)) {
            model.colocaTromino(x + 1, y, numTromino);
        }
        if (model.esCasellaBuida(x + 1, y + 1)) {
            model.colocaTromino(x + 1, y + 1, numTromino);
        }

        actualitzaVista();
        pausaExecucio();
    }

    /**
     * Implementació del patró d'esdeveniments: escolta notificacions externes.
     *
     * @param n Tipus de notificació rebuda
     */
    @Override
    public void notificar(Notificacio n) {
        if (n == Notificacio.ATURAR) {
            atura();
        }
    }
}
