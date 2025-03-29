package model;
import java.awt.geom.Point2D;
import vista.Vista;

public class BruteForceProcess extends Thread {
    private final Point2D.Double[] punts;
    private final Model modelo;
    private final Vista vista;

    public BruteForceProcess(Point2D.Double[] punts, Model modelo, Vista vista) {
        this.punts = punts;
        this.modelo = modelo;
        this.vista = vista;
    }

    @Override
    public void run() {
        long tiempoI = System.nanoTime();
        calcularBruteFroce();

        long tempsExecucio = System.nanoTime() - tiempoI;
        this.vista.setTime(tempsExecucio / 1_000_000_000.0);
        this.vista.setBestResult();
        this.vista.paintGraph();

        //modelo.setBestDistance(millorDistancia);
    }

    private void calcularBruteFroce() {
        double millorDistancia = Double.MAX_VALUE;
        for (int i = 0; i < punts.length; i++) {
            for (int j = i + 1; j < punts.length; j++) {
                double distancia = punts[i].distance(punts[j]);
                modelo.pushSolucion(punts[i], punts[j]);
                millorDistancia = Math.min(millorDistancia, distancia);
            }
        }
    }
}
