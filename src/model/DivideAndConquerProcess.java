package model;
import controlador.Controlador;
import controlador.Notificacio;
import java.awt.geom.Point2D;
import java.util.Arrays;
import java.util.Comparator;

public class DivideAndConquerProcess extends Thread {
    private final Controlador controlador;
    private final Model model;

    public DivideAndConquerProcess(Controlador c) {
        this.controlador = c;
        this.model = c.getModelo();
     }

    @Override
    public void run() {
        // Ordenem per X abans de començar
        long tiempoI = System.nanoTime();

        Point2D.Double[] punts = model.getPuntos();

        Arrays.sort(punts, Comparator.comparingDouble(Point2D.Double::getX));
        divideAndConquer(punts, 0, punts.length - 1);

        long tempsExecucio = System.nanoTime() - tiempoI;
        model.setTemps(tempsExecucio / 1_000_000_000.0);

        controlador.notificar(Notificacio.FINALITZA);
    }

    private double divideAndConquer(Point2D.Double[] punts, int start, int end) {
        if (end - start <= 3) {
            double valorATornar = model.isMinimizar() ? Double.MAX_VALUE : Double.MIN_VALUE;

            for (int i = start; i <= end; i++) {
                for (int j = i + 1; j <= end; j++) {
                    double d = punts[i].distance(punts[j]);
                    model.setSolucioSiEs(punts[i], punts[j]);
                    if (model.isMinimizar()) {
                        valorATornar = Math.min(valorATornar, d);
                    } else {
                        valorATornar = Math.max(valorATornar, d);
                    }
                }
            }
            return valorATornar;
        } else {
            int mid = (start + end) / 2;
            double distanciaEsq = divideAndConquer(punts, start, mid);
            double distanciaDreta = divideAndConquer(punts, mid + 1, end);
            double millorDistancia = model.isMinimizar() ? Math.min(distanciaEsq, distanciaDreta) : Math.max(distanciaEsq, distanciaDreta);

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
                    model.setSolucioSiEs(franja[i], franja[j]);
                    if (model.isMinimizar()) {
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
