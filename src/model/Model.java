package model;

import java.awt.geom.Point2D;
import java.util.Random;

public class Model {
    private Point2D.Double[] puntos; // Puntos generados según la distribución
    private Point2D.Double[] puntsSolucio; // Pareja que forma la mejor solución
    private Double mejorDistancia; // Distancia de la mejor solución

    private Distribucio distribucion; // Distribución para generar los puntos
    private Metode metodo; // Método algoritmico para resolver el problema
    private boolean minimizar; // Opción para minimizar o maximizar la distáncia entre puntos
    private int ANCHO; // Ancho de la ventana
    private int ALTO; // Alto de la ventana
    private double temps;

    // CONSTRUCTORS
    public Model() {
        this.puntos = null;
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
        switch (this.distribucion) {
            case GAUSSIAN -> {
                for (int i = 0; i < puntos.length; i++) {
                    double x = generateGaussianValue(rnd, ANCHO);
                    double y = generateGaussianValue(rnd, ALTO);
                    puntos[i] = new Point2D.Double(x, y);
                }
            }
            case EXPONENCIAL -> {
                for (int i = 0; i < puntos.length; i++) {
                    double x = generateExponentialValue(rnd, ANCHO);
                    double y = generateExponentialValue(rnd, ALTO);
                    puntos[i] = new Point2D.Double(x, y);
                }
            }
            case UNIFORME -> {
                for (int i = 0; i < puntos.length; i++) {
                    double x = rnd.nextDouble() * ANCHO;
                    double y = rnd.nextDouble() * ALTO;
                    puntos[i] = new Point2D.Double(x, y);
                }
            }
            default -> throw new AssertionError();
        }
    }

    private double generateGaussianValue(Random rnd, int max) {
        double value;

        do {
            value = rnd.nextGaussian(max / 2, max / 6);
        } while (value > max || value < 0);
        return value;
    }

    private double generateExponentialValue(Random rnd, int max) {
        double lambda = 2.0 / max;  // Scale to have mean X/2
        double value;
        do {
            value = -Math.log(1 - rnd.nextDouble()) / lambda;
        } while (value > max);
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
