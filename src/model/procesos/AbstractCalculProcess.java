package model.procesos;

import controlador.Controlador;
import controlador.Notificacio;
import java.awt.geom.Point2D;
import model.Metode;
import model.Model;

/**
 * @author tonitorres
 */
public abstract class AbstractCalculProcess extends Thread {

    protected final Controlador controlador;
    protected final Model model;
    protected final Point2D.Double[] punts;
    protected final boolean calculaCMInicial;

    // Constructor per a càlculs normals (amb punts del model)
    public AbstractCalculProcess(Controlador controlador) {
        this.controlador = controlador;
        this.model = controlador.getModel();
        this.punts = model.getPuntos();
        this.calculaCMInicial = false;
    }

    // Constructor per a càlculs específics (constant computacional)
    public AbstractCalculProcess(Controlador controlador, Point2D.Double[] punts) {
        this.controlador = controlador;
        this.model = controlador.getModel();
        this.punts = punts;
        this.calculaCMInicial = true;
    }

    @Override
    public void run() {
        long tiempoI = System.nanoTime();
        calcular();
        long tempsExecucio = System.nanoTime() - tiempoI;

        double seconds = tempsExecucio / 1_000_000_000.0;

        // Actualització de la constant multiplicativa
        model.updateConstant(punts.length, seconds, getMetode());
        model.setTemps(seconds);

        if (!this.calculaCMInicial) {
            controlador.notificar(Notificacio.FINALITZA);
        }
    }

    // Mètode abstracte per calcular la solució
    protected abstract void calcular();

    // Mètode abstracte per obtenir el mètode específic (FUERZA_BRUTA o DIVIDE_Y_VENCERAS)
    protected abstract Metode getMetode();
}
