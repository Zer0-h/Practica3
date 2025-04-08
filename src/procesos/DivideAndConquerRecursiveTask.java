package procesos;

import java.awt.geom.Point2D;
import java.util.Arrays;
import java.util.Comparator;
import java.util.concurrent.RecursiveTask;

/**
 * Classe DivideAndConquerRecursiveTask: Implementa la tasca recursiva per
 * calcular la parella de punts
 * més propera o més llunyana utilitzant l'algorisme Divideix i Venceràs.
 *
 * Utilitza el model Fork/Join per aprofitar el paral·lelisme en el càlcul.
 *
 * @author tonitorres
 */
public class DivideAndConquerRecursiveTask extends RecursiveTask<Point2D.Double[]> {

    private final Point2D.Double[] punts;  // Conjunt de punts a tractar
    private final int start, end;          // Índexs d'inici i final de la subtaula de punts
    private final boolean minimaDistancia; // Model que conté les dades i la solució

    /**
     * Constructor per inicialitzar la tasca recursiva.
     *
     * @param p Conjunt de punts a processar.
     * @param s Índex d'inici del subconjunt de punts.
     * @param e Índex de final del subconjunt de punts.
     * @param m Si el problema és de trobar la parella mínima o màxima.
     */
    public DivideAndConquerRecursiveTask(Point2D.Double[] p, int s, int e, boolean m) {
        punts = p;
        start = s;
        end = e;
        minimaDistancia = m;

    }

    /**
     * Mètode principal que executa la tasca recursiva.
     * Divideix el problema si és prou gran, o el resol directament si és petit.
     *
     * @return La millor parella de punts trobada.
     */
    @Override
    protected Point2D.Double[] compute() {
        if (end - start <= 3) {
            return calcularDirectament(punts, start, end);
        }

        int mid = (start + end) / 2;

        // Divideix el problema en dues tasques paral·leles
        DivideAndConquerRecursiveTask leftTask = new DivideAndConquerRecursiveTask(punts, start, mid, minimaDistancia);
        DivideAndConquerRecursiveTask rightTask = new DivideAndConquerRecursiveTask(punts, mid + 1, end, minimaDistancia);

        // Llança la tasca esquerra i calcula la dreta
        leftTask.fork();
        Point2D.Double[] solucioDreta = rightTask.compute();
        Point2D.Double[] solucioEsquerra = leftTask.join();

        // Troba la millor solució entre les dues
        Point2D.Double[] millorSolucio = getMillorSolucio(solucioEsquerra, solucioDreta);

        // Combina els resultats de les subtaques per trobar la millor solució a la franja
        return combinarResultats(punts, start, end, mid, millorSolucio);
    }

    /**
     * Troba la millor solució entre dues parelles de punts.
     */
    private Point2D.Double[] getMillorSolucio(Point2D.Double[] sol1, Point2D.Double[] sol2) {
        if (sol1 == null) {
            return sol2;
        }
        if (sol2 == null) {
            return sol1;
        }

        double dist1 = sol1[0].distance(sol1[1]);
        double dist2 = sol2[0].distance(sol2[1]);

        return (minimaDistancia && dist1 < dist2) || (!minimaDistancia && dist1 > dist2) ? sol1 : sol2;
    }

    /**
     * Calcula la millor solució directament si el conjunt de punts és petit.
     */
    private Point2D.Double[] calcularDirectament(Point2D.Double[] punts, int start, int end) {
        Point2D.Double[] millorSolucio = null;

        for (int i = start; i <= end; i++) {
            for (int j = i + 1; j <= end; j++) {
                Point2D.Double[] p = new Point2D.Double[]{punts[i], punts[j]};

                millorSolucio = getMillorSolucio(p, millorSolucio);
            }
        }

        return millorSolucio;
    }

    /**
     * Combina els resultats de les subtaques per trobar la millor solució a la
     * franja.
     */
    private Point2D.Double[] combinarResultats(Point2D.Double[] punts, int start, int end, int mid, Point2D.Double[] millorSolucio) {
        double millorDistancia = millorSolucio[0].distance(millorSolucio[1]);
        double midX = punts[mid].getX();
        Point2D.Double[] franja = new Point2D.Double[end - start + 1];
        int idx = 0;

        // Construcció de la franja central
        for (int i = start; i <= end; i++) {
            if (Math.abs(punts[i].getX() - midX) < millorDistancia) {
                franja[idx++] = punts[i];
            }
        }

        // Ordena la franja per coordenada Y
        Arrays.sort(franja, 0, idx, Comparator.comparingDouble(p -> p.getY()));

        // Compara les distàncies dins la franja
        for (int i = 0; i < idx; i++) {
            for (int j = i + 1; j < idx && (franja[j].getY() - franja[i].getY()) < millorDistancia; j++) {
                millorSolucio = getMillorSolucio(new Point2D.Double[]{franja[i], franja[j]}, millorSolucio);
                millorDistancia = millorSolucio[0].distance(millorSolucio[1]);
            }
        }

        return millorSolucio;
    }
}
