package model;
import java.awt.geom.Point2D;
import java.util.Arrays;
import java.util.Comparator;
import vista.Vista;

public class DivideAndConquerProcess extends Thread {
    private final Point2D.Double[] punts;
    private final boolean minimizar;
    private final Model modelo;
    private final Vista vista;

    public DivideAndConquerProcess(Point2D.Double[] punts, boolean minimizar, Model modelo, Vista vista) {
        this.punts = punts;
        this.minimizar = minimizar;
        this.modelo = modelo;
        this.vista = vista;
    }

    @Override
    public void run() {
        // Ordenem per X abans de començar
        long tiempoI = System.nanoTime();

        Arrays.sort(punts, Comparator.comparingDouble(Point2D.Double::getX));
        divideAndConquer(punts, 0, punts.length - 1, minimizar);

        long tempsExecucio = System.nanoTime() - tiempoI;
        this.vista.setTime(tempsExecucio / 1_000_000_000.0);
        this.vista.setBestResult();
        this.vista.paintGraph();

        //modelo(millorDistancia);
    }

    private double divideAndConquer(Point2D.Double[] punts, int start, int end, boolean minimizar) {
        if (end - start <= 3) {
            double valorATornar = minimizar ? Double.MAX_VALUE : Double.MIN_VALUE;

            for (int i = start; i <= end; i++) {
                for (int j = i + 1; j <= end; j++) {
                    double d = punts[i].distance(punts[j]);
                    modelo.pushSolucion(punts[i], punts[j]);
                    if (minimizar) {
                        valorATornar = Math.min(valorATornar, d);
                    } else {
                        valorATornar = Math.max(valorATornar, d);
                    }
                }
            }
            return valorATornar;
        } else {
            int mid = (start + end) / 2;
            double distanciaEsq = divideAndConquer(punts, start, mid, minimizar);
            double distanciaDreta = divideAndConquer(punts, mid + 1, end, minimizar);
            double millorDistancia = minimizar ? Math.min(distanciaEsq, distanciaDreta) : Math.max(distanciaEsq, distanciaDreta);

            double midX = punts[mid].getX();
            Point2D.Double[] franja = new Point2D.Double[end - start + 1];
            int idx = 0;

            for (int i = start; i <= end; i++) {
                if (Math.abs(punts[i].getX() - midX) < millorDistancia) {
                    franja[idx++] = punts[i];
                }
            }

            Arrays.sort(franja, 0, idx, Comparator.comparingDouble(p -> p.getY()));

            for (int i = 0; i < idx; i++) {
                for (int j = i + 1; j < idx && (franja[j].getY() - franja[i].getY()) < millorDistancia; j++) {
                    double dist = franja[i].distance(franja[j]);
                    modelo.pushSolucion(franja[i], franja[j]);
                    if (minimizar) {
                        millorDistancia = Math.min(millorDistancia, dist);
                    } else {
                        millorDistancia = Math.max(millorDistancia, dist);
                    }
                }
            }

            return millorDistancia;
        }
    }
}
