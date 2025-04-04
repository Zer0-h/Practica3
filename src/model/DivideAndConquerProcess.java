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
        Arrays.sort(punts, Comparator.comparingDouble(Point2D.Double::getX));
        forkJoinPool.invoke(new DivideAndConquerRecursiveTask(punts, 0, punts.length - 1, model));
    }

    @Override
    protected Metode getMetode() {
        return Metode.DIVIDE_Y_VENCERAS;
    }
}
