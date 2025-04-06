package model.procesos;

import controlador.Controlador;
import controlador.Notificacio;
import java.awt.geom.Point2D;
import model.Metode;
import model.Model;

/**
 * Classe AbstractCalculProcess: Classe abstracta per gestionar el procés de càlcul
 * d'algorismes per trobar la parella de punts més propera o més llunyana.
 * Utilitza el patró plantilla per definir el flux d'execució comú i delega el càlcul
 * específic als subclasses.
 *
 * @author tonitorres
 */
public abstract class AbstractCalculProcess extends Thread {

    protected final Controlador controlador; // Referència al controlador principal
    protected final Model model; // Referència al model de dades
    protected final Point2D.Double[] punts; // Conjunt de punts a processar
    protected final boolean calculaCMInicial; // Flag per indicar si es calcula la constant multiplicativa inicial

    /**
     * Constructor per a càlculs normals (utilitzant punts del model).
     * @param controlador El controlador principal de l'aplicació.
     */
    public AbstractCalculProcess(Controlador controlador) {
        this.controlador = controlador;
        this.model = controlador.getModel();
        this.punts = model.getPunts();
        this.calculaCMInicial = false;
    }

    /**
     * Constructor per a càlculs específics (per calcular la constant multiplicativa).
     * @param controlador El controlador principal de l'aplicació.
     * @param punts Conjunt de punts generats per a càlcul inicial de constants.
     */
    public AbstractCalculProcess(Controlador controlador, Point2D.Double[] punts) {
        this.controlador = controlador;
        this.model = controlador.getModel();
        this.punts = punts;
        this.calculaCMInicial = true;
    }

    /**
     * Mètode que s'executa quan el fil és iniciat.
     * Gestiona el càlcul de la solució i el temps d'execució.
     */
    @Override
    public void run() {
        long tempsInicial = System.nanoTime();
        calcular(); // Mètode abstracte per realitzar el càlcul específic
        long tempsExecucio = System.nanoTime() - tempsInicial;

        // Convertim el temps a segons
        double segons = tempsExecucio / 1_000_000_000.0;

        // Actualització de la constant multiplicativa segons el mètode d'algorisme
        model.actualitzarConstant(punts.length, segons, getMetode());
        model.setTemps(segons);

        // Notifiquem la finalització només si no és el càlcul de la constant inicial
        if (!this.calculaCMInicial) {
            controlador.notificar(Notificacio.FINALITZA);
        }
    }

    /**
     * Mètode abstracte que implementarà el càlcul específic segons l'algorisme utilitzat.
     * Aquest mètode serà implementat per les subclasses concretes.
     */
    protected abstract void calcular();

    /**
     * Mètode abstracte que retorna el mètode algorísmic concret.
     * @return El mètode utilitzat (FUERZA_BRUTA, DIVIDE_Y_VENCERAS o CONVEX_HULL).
     */
    protected abstract Metode getMetode();
}
