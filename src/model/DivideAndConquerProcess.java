package model;

import controlador.Controlador;
import java.awt.geom.Point2D;
import java.util.Arrays;
import java.util.Comparator;
import java.util.concurrent.ForkJoinPool;

public class DivideAndConquerProcess extends AbstractCalculProcess {

    // Utilitzem un pool global per a tota l'aplicació
    private static final ForkJoinPool forkJoinPool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());

    public DivideAndConquerProcess(Controlador controlador) {
        super(controlador);
    }

    public DivideAndConquerProcess(Controlador controlador, Point2D.Double[] punts) {
        super(controlador, punts);
    }

    @Override
    protected void calcular() {
        // Copiam l'array ja que
        Point2D.Double[] copia = new Point2D.Double[punts.length];
        System.arraycopy(punts, 0, copia, 0, punts.length);

        Arrays.sort(copia, Comparator.comparingDouble(Point2D.Double::getX));
        forkJoinPool.invoke(new DivideAndConquerRecursiveTask(copia, 0, copia.length - 1, model));
    }

    @Override
    protected Metode getMetode() {
        return Metode.DIVIDE_Y_VENCERAS;
    }
}
