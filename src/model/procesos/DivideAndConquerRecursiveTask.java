package model.procesos;

import java.awt.geom.Point2D;
import java.util.Arrays;
import java.util.Comparator;
import java.util.concurrent.RecursiveTask;
import model.Model;

/**
 * Classe DivideAndConquerRecursiveTask: Implementa la tasca recursiva per calcular la parella de punts
 * més propera o més llunyana utilitzant l'algorisme Divideix i Venceràs.
 *
 * Utilitza el model Fork/Join per aprofitar el paral·lelisme en el càlcul.
 *
 * @author tonitorres
 */
public class DivideAndConquerRecursiveTask extends RecursiveTask<Double> {

    private final Point2D.Double[] punts;  // Conjunt de punts a tractar
    private final int start, end;          // Índexs d'inici i final de la subtaula de punts
    private final Model model;             // Model que conté les dades i la solució

    /**
     * Constructor per inicialitzar la tasca recursiva.
     *
     * @param punts Conjunt de punts a processar.
     * @param start Índex d'inici del subconjunt de punts.
     * @param end Índex de final del subconjunt de punts.
     * @param model El model de dades per actualitzar la solució.
     */
    public DivideAndConquerRecursiveTask(Point2D.Double[] punts, int start, int end, Model model) {
        this.punts = punts;
        this.start = start;
        this.end = end;
        this.model = model;
    }

    /**
     * Mètode principal que executa la tasca recursiva.
     * Divideix el problema si és prou gran, o el resol directament si és petit.
     *
     * @return La millor distància trobada en el subconjunt de punts.
     */
    @Override
    protected Double compute() {
        if (end - start <= 3) {
            return calcularDirectament(punts, start, end);
        }

        int mid = (start + end) / 2;

        // Divideix el problema en dues tasques paral·leles
        DivideAndConquerRecursiveTask leftTask = new DivideAndConquerRecursiveTask(punts, start, mid, model);
        DivideAndConquerRecursiveTask rightTask = new DivideAndConquerRecursiveTask(punts, mid + 1, end, model);

        // Llança la tasca esquerra i calcula la dreta
        leftTask.fork();
        double distDreta = rightTask.compute();
        double distEsq = leftTask.join();

        // Troba la millor distància entre els dos resultats
        double millorDistancia = getMillorDistancia(distEsq, distDreta);

        // Combina els resultats de les subtaques per trobar la distància mínima a la franja
        return combinarResultats(punts, start, end, mid, millorDistancia);
    }

    /**
     * Calcula la millor distància entre dues.
     *
     * @param dist1 Primera distància.
     * @param dist2 Segona distància.
     * @return La millor (mínima o màxima) distància segons el mode (minimitzar o maximitzar).
     */
    private double getMillorDistancia(double dist1, double dist2) {
        if (model.isMinimizar()) {
            return Math.min(dist1, dist2);
        } else {
            return Math.max(dist1, dist2);
        }
    }

    /**
     * Calcula la distància directament si el conjunt de punts és petit.
     *
     * @param punts Conjunt de punts.
     * @param start Índex d'inici.
     * @param end Índex de final.
     * @return La millor distància calculada.
     */
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

    /**
     * Combina els resultats de les subtaques per trobar la millor distància entre els punts
     * de la franja situada al voltant del punt mig.
     *
     * @param punts Conjunt de punts.
     * @param start Índex d'inici.
     * @param end Índex de final.
     * @param mid Índex del punt mig.
     * @param millorDistancia Distància mínima trobada fins al moment.
     * @return La millor distància després de combinar els resultats.
     */
    private double combinarResultats(Point2D.Double[] punts, int start, int end, int mid, double millorDistancia) {
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
                double dist = franja[i].distance(franja[j]);
                model.setSolucioSiEs(franja[i], franja[j]);
                millorDistancia = getMillorDistancia(millorDistancia, dist);
            }
        }

        return millorDistancia;
    }
}
