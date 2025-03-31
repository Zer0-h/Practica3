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

        double seconds = tempsExecucio / 1_000_000_000.0;
        model.setTemps(seconds);

        // Actualització de la constant
        model.updateConstant(model.getPuntos().length, seconds);

        controlador.notificar(Notificacio.FINALITZA);
    }

    private void calcularBruteFroce(Model model) {
        Point2D.Double[] punts = model.getPuntos();

        for (int i = 0; i < punts.length; i++) {
            for (int j = i + 1; j < punts.length; j++) {
                model.setSolucioSiEs(punts[i], punts[j]);
            }
        }
    }
}
