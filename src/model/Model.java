package model;

import java.awt.geom.Point2D;
import java.util.Random;

public class Model {
    private Point2D.Double[] puntos; // Puntos generados según la distribución
    private Point2D.Double[] puntsSolucio; // Pareja que forma la mejor solución
    private Double mejorDistancia; // Distancia de la mejor solución

    private Distribucio distribucion; // Distribución para generar los puntos
    private Metode metodo; // Método algoritmico para resolver el problema
    private boolean minimizar; // Opción para minimizar o maximizar la distancia entre puntos
    private int ANCHO; // Ancho de la ventana
    private int ALTO; // Alto de la ventana
    private double temps;

    // CONSTRUCTOR
    public Model() {
        this.puntsSolucio = new Point2D.Double[2];
        temps = 0;
    }

    public void setPanelSize(int width, int height) {
        ANCHO = width;
        ALTO = height;
    }

    public void generarDatos(int numeroPunts) {
        this.puntsSolucio = null;
        this.mejorDistancia = null;

        puntos = new Point2D.Double[numeroPunts];
        Random rnd = new Random();

        for (int i = 0; i < puntos.length; i++) {
            double x = generateValue(rnd, ANCHO);
            double y = generateValue(rnd, ALTO);
            puntos[i] = new Point2D.Double(x, y);
        }
    }

    // Generador de valor segons la distribució
    private double generateValue(Random rnd, int max) {
        switch (this.distribucion) {
            case GAUSSIAN -> {
                return generateGaussianValue(rnd, max);
            }
            case EXPONENCIAL -> {
                return generateExponentialValue(rnd, max);
            }
            case UNIFORME -> {
                return rnd.nextDouble() * max;
            }
            default -> throw new AssertionError("Distribució no reconeguda: " + distribucion);
        }
    }

    private double generateGaussianValue(Random rnd, int max) {
        double value;
        do {
            value = rnd.nextGaussian(max / 2.0, max / 6.0);
        } while (value < 0 || value > max);
        return value;
    }

    private double generateExponentialValue(Random rnd, int max) {
        double lambda = 2.0 / max; // Scale to have mean X/2
        double value;
        do {
            value = -Math.log(1 - rnd.nextDouble()) / lambda;
        } while (value < 0 || value > max);
        return value;
    }

    /**
     * Inicializa los atributos soluciones y distancias para
     */
    public void initSoluciones() {
        puntsSolucio = new Point2D.Double[2];
        puntsSolucio[0] = new Point2D.Double(0, 0);
        puntsSolucio[1] = new Point2D.Double(0, 0);
        mejorDistancia = minimizar ? Double.MAX_VALUE : Double.MIN_VALUE;
    }

    public void setSolucioSiEs(Point2D.Double punt1, Point2D.Double punt2) {
        double distancia = punt1.distance(punt2);
        if ((minimizar && distancia < mejorDistancia) || (!minimizar && distancia > mejorDistancia)) {
            mejorDistancia = distancia;
            puntsSolucio[0] = punt1;
            puntsSolucio[1] = punt2;
        }
    }

    public void setTemps(double value) {
        temps = value;
    }

    public double getTemps() {
        return temps;
    }

    public Point2D.Double[] getPuntos() {
        return puntos;
    }

    public void setPuntos(Point2D.Double[] puntos) {
        this.puntos = puntos;
    }

    public Distribucio getDistribucion() {
        return distribucion;
    }

    public void setDistribucion(Distribucio distribucion) {
        this.distribucion = distribucion;
    }

    public boolean isMinimizar() {
        return minimizar;
    }

    public void setMinimizar(boolean minimizar) {
        this.minimizar = minimizar;
    }

    public Metode getMetodo() {
        return metodo;
    }

    public void setMetodo(Metode metodo) {
        this.metodo = metodo;
    }

    public Point2D.Double[] getPuntsSolucio() {
        return puntsSolucio;
    }

    public boolean tePunts() {
        return this.puntos != null;
    }
}
