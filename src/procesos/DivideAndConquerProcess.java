package procesos;

import java.awt.geom.Point2D;
import java.util.Arrays;
import java.util.Comparator;
import java.util.concurrent.ForkJoinPool;
import model.Metode;

/**
 * Classe DivideAndConquerProcess: Implementa el càlcul de la parella de punts
 * més propera
 * o més llunyana utilitzant el mètode Divideix i Venceràs (complexitat
 * O(n·log(n))).
 *
 * Utilitza un ForkJoinPool per executar tasques paral·leles i aprofitar al
 * màxim els recursos de la CPU.
 *
 * @author tonitorres
 */
public class DivideAndConquerProcess extends AbstractCalculProcess {

    // Utilitzem un pool global per a tota l'aplicació
    private static final ForkJoinPool forkJoinPool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());

    /**
     * Constructor amb punts específics (utilitzat per a càlculs de constants).
     *
     * @param minimitzar Si el problema es per trobar la parella minima o
     *                   màxima.
     * @param punts      Conjunt de punts a utilitzar.
     */
    public DivideAndConquerProcess(boolean minimitzar, Point2D.Double[] punts) {
        super(minimitzar, punts);
    }

    /**
     * Mètode principal de càlcul que executa l'algorisme Divideix i Venceràs
     * per trobar la
     * parella de punts més propera o més llunyana, aprofitant el paral·lelisme.
     */
    @Override
    protected void calcular() {
        // Clonem l'array de punts per evitar modificar les dades originals
        Point2D.Double[] copia = new Point2D.Double[punts.length];
        System.arraycopy(punts, 0, copia, 0, punts.length);

        // Ordenem els punts per la coordenada X per preparar-los per a l'algorisme
        Arrays.sort(copia, Comparator.comparingDouble(Point2D.Double::getX));

        // Invoquem el càlcul de forma paral·lela amb ForkJoinPool
        Point2D.Double[] solucio = forkJoinPool.invoke(new DivideAndConquerRecursiveTask(copia, 0, copia.length - 1, minimaDistancia));

        setSolucio(solucio[0], solucio[1]);
    }

    /**
     * Retorna el mètode utilitzat en aquesta classe (Divideix i Venceràs).
     *
     * @return El mètode DIVIDEIX.
     */
    @Override
    public Metode getMetode() {
        return Metode.DIVIDEIX;
    }
}
