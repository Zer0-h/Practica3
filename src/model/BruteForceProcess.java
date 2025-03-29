package model;
import controlador.Controlador;
import controlador.Notificacio;
import java.awt.geom.Point2D;

public class BruteForceProcess extends Thread {
    private final Controlador controlador;

    public BruteForceProcess(Controlador c) {
        controlador = c;
    }

    @Override
    public void run() {
        Model model = controlador.getModelo();

        long tiempoI = System.nanoTime();
        calcularBruteFroce(model);

        long tempsExecucio = System.nanoTime() - tiempoI;

        model.setTemps(tempsExecucio / 1_000_000_000.0);

        controlador.notificar(Notificacio.FINALITZA);
    }

    private void calcularBruteFroce(Model model) {
        double millorDistancia = Double.MAX_VALUE;
        Point2D.Double[] punts = model.getPuntos();

        for (int i = 0; i < punts.length; i++) {
            for (int j = i + 1; j < punts.length; j++) {
                double distancia = punts[i].distance(punts[j]);
                model.pushSolucion(punts[i], punts[j]);
                millorDistancia = Math.min(millorDistancia, distancia);
            }
        }
    }
}
