package model.procesos;

import controlador.Controlador;
import controlador.Notificacio;
import java.awt.geom.Point2D;
import model.Metode;
import model.Model;

/**
 * Classe AbstractCalculProcess: Classe abstracta per gestionar el procés de
 * càlcul
 * d'algorismes per trobar la parella de punts més propera o més llunyana.
 * Utilitza el patró plantilla per definir el flux d'execució comú i delega el
 * càlcul
 * específic als subclasses.
 *
 * @author tonitorres
 */
public abstract class AbstractCalculProcess extends Thread {

    protected final Controlador controlador; // Referència al controlador principal
    protected final Model model; // Referència al model de dades
    protected final Point2D.Double[] punts; // Conjunt de punts a processar
    protected final boolean calculaCMInicial; // Flag per indicar si es calcula la constant multiplicativa inicial
    protected Point2D.Double[] puntsSolucio;  // Solució específica del procés
    protected double millorDistancia;
    protected boolean minimaDistancia;

    /**
     * Constructor per a càlculs normals (utilitzant punts del model).
     *
     * @param c El controlador principal de l'aplicació.
     */
    public AbstractCalculProcess(Controlador c) {
        controlador = c;
        model = controlador.getModel();
        punts = model.getPunts();
        calculaCMInicial = false;
        puntsSolucio = null;
        minimaDistancia = model.esMinimizar();
        millorDistancia = minimaDistancia ? Double.MAX_VALUE : Double.MIN_VALUE;
    }

    /**
     * Constructor per a càlculs específics (per calcular la constant
     * multiplicativa).
     *
     * @param c El controlador principal de l'aplicació.
     * @param p Conjunt de punts generats per a càlcul inicial de constants.
     */
    public AbstractCalculProcess(Controlador c, Point2D.Double[] p) {
        controlador = c;
        model = controlador.getModel();
        punts = p;
        calculaCMInicial = true;
        puntsSolucio = null;
        minimaDistancia = model.esMinimizar();
        millorDistancia = minimaDistancia ? Double.MAX_VALUE : Double.MIN_VALUE;
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
        model.setSolucio(puntsSolucio);

        // Notifiquem la finalització només si no és el càlcul de la constant inicial
        if (!this.calculaCMInicial) {
            controlador.notificar(Notificacio.FINALITZA);
        }
    }

    /**
     * Guarda la solució localment si es vàlida.
     *
     * @param p1
     * @param p2
     */
    protected void setSolucio(Point2D.Double p1, Point2D.Double p2) {
        double distancia = p1.distance(p2);

        if ((minimaDistancia && distancia < millorDistancia) || (!minimaDistancia && distancia > millorDistancia)) {
            millorDistancia = distancia;
            puntsSolucio = new Point2D.Double[]{p1, p2};
        }
    }

    /**
     * Mètode que retorna la solució calculada per aquest procés.
     *
     * @return Array amb els dos punts de la millor solució.
     */
    public Point2D.Double[] calcularSolucio() {
        calcular();
        return puntsSolucio;
    }

    /**
     * Retorna la millor solució trobada pel procés.
     *
     * @return Conjunt de dos punts que formen la millor solució.
     */
    public Point2D.Double[] getPuntsSolucio() {
        return puntsSolucio;
    }

    /**
     * Mètode abstracte que implementarà el càlcul específic segons l'algorisme
     * utilitzat.
     * Aquest mètode serà implementat per les subclasses concretes.
     */
    protected abstract void calcular();

    /**
     * Mètode abstracte que retorna el mètode algorísmic concret.
     *
     * @return El mètode utilitzat (FUERZA_BRUTA, DIVIDE_Y_VENCERAS o
     *         CONVEX_HULL).
     */
    protected abstract Metode getMetode();
}
