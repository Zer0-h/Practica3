package model;

import java.awt.geom.Point2D;
import java.util.Random;

/**
 * @author tonitorres
 */
public class Model {

    private Point2D.Double[] punts; // Punts generats segons la distribució
    private Point2D.Double[] puntsSolucio; // Parella que forma la millor solució
    private Double millorDistancia; // Distància de la millor solució

    private Distribucio distribucio; // Distribució per generar els punts
    private Metode metode; // Mètode algorísmic per resoldre el problema
    private boolean minimizar; // Opció per minimitzar o maximitzar la distància
    private int amplada; // Amplada de la finestra
    private int altura; // Altura de la finestra
    private double temps; // Temps d'execució
    private boolean mostrarLineaSolucio; // Flag per mostrar la línia de solució

    // Constants multiplicatives per a cada algorisme
    private double constantBruteForce;
    private double constantDivideConquer;
    private double constantConvexHull;

    // Constructor per defecte
    public Model() {
    }

    /**
     * Calcula el temps estimat en funció del nombre de punts (N) i el mètode seleccionat.
     * @return Temps estimat en segons.
     */
    public double calcularTempsEstimacio() {
        int n = punts.length;
        double tempsEstimat;

        switch (metode) {
            case FUERZA_BRUTA -> tempsEstimat = constantBruteForce * Math.pow(n, 2);
            case DIVIDE_Y_VENCERAS -> tempsEstimat = constantDivideConquer * n * Math.log(n);
            case CONVEX_HULL -> tempsEstimat = constantConvexHull * n * Math.log(n);
            default -> throw new IllegalArgumentException("Mètode desconegut: " + metode);
        }

        return tempsEstimat;
    }

    /**
     * Actualitza la constant multiplicativa després d'una execució.
     * @param n Nombre de punts.
     * @param tempsExecucio Temps d'execució.
     * @param metode Mètode utilitzat.
     */
    public void actualitzarConstant(long n, double tempsExecucio, Metode metode) {
        switch (metode) {
            case FUERZA_BRUTA -> constantBruteForce = tempsExecucio / (n * n);
            case DIVIDE_Y_VENCERAS -> constantDivideConquer = tempsExecucio / (n * Math.log(n));
            case CONVEX_HULL -> constantConvexHull = tempsExecucio / (n * Math.log(n));
            default -> throw new IllegalArgumentException("Mètode desconegut: " + metode);
        }
    }

    /**
     * Defineix la mida del panell de visualització.
     * @param amplada Amplada del panell gràfic.
     * @param altura Altura del panell gràfic.
     */
    public void setMidaPanel(int amplada, int altura) {
        this.amplada = amplada;
        this.altura = altura;
    }

    /**
     * Genera un conjunt de punts aleatoris segons la distribució seleccionada.
     * @param numeroPunts Nombre de punts a generar.
     */
    public void generarNuvolPunts(int numeroPunts) {
        puntsSolucio = null;
        millorDistancia = null;
        mostrarLineaSolucio = false;

        punts = new Point2D.Double[numeroPunts];
        Random rnd = new Random();

        for (int i = 0; i < punts.length; i++) {
            double x = generarValor(rnd, amplada);
            double y = generarValor(rnd, altura);
            punts[i] = new Point2D.Double(x, y);
        }
    }


    /**
     * Genera un conjunt auxiliar de punts per al càlcul de constants.
     * @param n Nombre de punts a generar.
     * @return Array de punts generats.
     */
    public Point2D.Double[] generarPunts(int n) {
        Point2D.Double[] puntsAux = new Point2D.Double[n];
        Random rnd = new Random();
        for (int i = 0; i < n; i++) {
            double x = rnd.nextDouble() * amplada;
            double y = rnd.nextDouble() * altura;
            puntsAux[i] = new Point2D.Double(x, y);
        }
        return puntsAux;
    }

    /**
     * Genera un valor aleatori segons la distribució actual.
     */
    private double generarValor(Random rnd, int max) {
        return switch (this.distribucio) {
            case GAUSSIAN -> generarValorGaussian(rnd, max);
            case EXPONENCIAL -> generarValorExponencial(rnd, max);
            case UNIFORME -> rnd.nextDouble() * max;
            default -> throw new AssertionError("Distribució no reconeguda: " + distribucio);
        };
    }

    private double generarValorGaussian(Random rnd, int max) {
        double valor;
        do {
            valor = rnd.nextGaussian(max / 2.0, max / 6.0);
        } while (valor < 0 || valor > max);
        return valor;
    }

    private double generarValorExponencial(Random rnd, int max) {
        double lambda = 2.0 / max;
        double valor;
        do {
            valor = -Math.log(1 - rnd.nextDouble()) / lambda;
        } while (valor < 0 || valor > max);
        return valor;
    }

    /**
     * Reinicialitza la solució abans de començar un càlcul nou.
     */
    public void resetSolucio() {
        puntsSolucio = null;
        millorDistancia = minimizar ? Double.MAX_VALUE : Double.MIN_VALUE;
    }

    public void setMostrarLineaSolucio(boolean value) {
        mostrarLineaSolucio = value;
    }

    public boolean getMostrarLineaSolucio() {
        return mostrarLineaSolucio;
    }

    public void setSolucioSiEs(Point2D.Double punt1, Point2D.Double punt2) {
        double distancia = punt1.distance(punt2);
        if ((minimizar && distancia < millorDistancia) || (!minimizar && distancia > millorDistancia)) {
            millorDistancia = distancia;
            puntsSolucio = new Point2D.Double[]{punt1, punt2};
        }
    }

    public void setTemps(double valor) {
        temps = valor;
    }

    public double getTemps() {
        return temps;
    }

    public Point2D.Double[] getPunts() {
        return punts;
    }

    public Distribucio getDistribucio() {
        return distribucio;
    }

    public void setDistribucio(Distribucio distribucio) {
        this.distribucio = distribucio;
    }

    public boolean isMinimizar() {
        return minimizar;
    }

    public void setMinimizar(boolean minimizar) {
        this.minimizar = minimizar;
    }

    public Metode getMetode() {
        return metode;
    }

    public void setMetode(Metode metode) {
        this.metode = metode;
    }

    public Point2D.Double[] getPuntsSolucio() {
        return puntsSolucio;
    }

    public boolean tePunts() {
        return this.punts != null;
    }

    public double producteVectorial(Point2D.Double a, Point2D.Double b, Point2D.Double c) {
        return (b.getX() - a.getX()) * (c.getY() - a.getY()) - (b.getY() - a.getY()) * (c.getX() - a.getX());
    }
}
