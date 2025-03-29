package model;

import vista.Vista;
import controlador.Controller;
import java.awt.geom.Point2D;
import java.util.Random;

public class Model {

    // PUNTEROS DEL PATRÓN MVC
    private Vista vista;
    private Controller controlador;

    private int N; // Número de puntos
    private Point2D.Double[] puntos; // Puntos generados según la distribución
    private Point2D.Double[] mejorSolucion; // Pareja que forma la mejor solución
    private Double mejorDistancia; // Distancia de la mejor solución

    private Distribution distribucion; // Distribución para generar los puntos
    private Method metodo; // Método algoritmico para resolver el problema
    private boolean minimizar; // Opción para minimizar o maximizar la distáncia entre puntos
    private int ANCHO; // Ancho de la ventana
    private int ALTO; // Alto de la ventana
    private long temps;

    // CONSTRUCTORS
    public Model() {
        this.mejorSolucion = new Point2D.Double[2];
        temps = 0;
    }

    public Model(Vista vista, Controller controlador, int n) {
        this.vista = vista;
        this.controlador = controlador;
        this.N = n;
        this.puntos = null;
        this.mejorSolucion = new Point2D.Double[2];
        temps = 0;
    }

    public void setPanelSize(int width, int height) {
        ANCHO = width;
        ALTO = height;
    }

    private void generarDatos() {
        puntos = new Point2D.Double[N];
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

        verificarDistribucions();
    }

    private double generateGaussianValue(Random rnd, int max) {
        double value;

        do {
            value = rnd.nextGaussian(max / 2, max / 6);
        } while (value > max || value < 0);
        return value;
    }

    public void verificarDistribucions() {
        boolean correcte = true;

        for (Point2D.Double punt : puntos) {
            double x = punt.getX();
            double y = punt.getY();

            if (x < 0 || x > ANCHO || y < 0 || y > ALTO) {
                System.out.println("ERROR: Punt fora de límits -> x: " + x + ", y: " + y);
                correcte = false;
            }
        }

        if (correcte) {
            System.out.println("Tots els punts estan dins dels límits correctament.");
        } else {
            System.out.println("Hi ha punts fora dels límits.");
        }
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
        mejorSolucion = new Point2D.Double[2];
        mejorSolucion[0] = new Point2D.Double(0d, 0d);
        mejorSolucion[1] = new Point2D.Double(300d, 300d);
        mejorDistancia = minimizar ? Double.MAX_VALUE : Double.MIN_VALUE;
    }

    public void pushSolucion(Point2D.Double punt1, Point2D.Double punt2) {
        double distancia = punt1.distance(punt2);
        if ((minimizar && distancia < mejorDistancia) || (!minimizar && distancia > mejorDistancia)) {
            mejorDistancia = distancia;
            mejorSolucion[0] = punt1;
            mejorSolucion[1] = punt2;
        }
    }

    // GETTERS & SETTERS
    public Vista getVista() {
        return vista;
    }

    public void setVista(Vista vista) {
        this.vista = vista;
    }

    public Controller getControlador() {
        return controlador;
    }

    public void setControlador(Controller controlador) {
        this.controlador = controlador;
    }

    public void setTemps(long value) {
        temps = value;
    }

    public long getTemps() {
        return temps;
    }

    public Point2D.Double[] getPuntos() {
        return puntos;
    }

    public void setPuntos(Point2D.Double[] puntos) {
        this.puntos = puntos;
    }

    public Distribution getDistribucion() {
        return distribucion;
    }

    public void setDistribucion(Distribution distribucion) {
        this.distribucion = distribucion;
    }

    public boolean isMinimizar() {
        return minimizar;
    }

    public void setMinimizar(boolean minimizar) {
        this.minimizar = minimizar;
    }

    public Method getMetodo() {
        return metodo;
    }

    public void setMetodo(Method metodo) {
        this.metodo = metodo;
    }

    public Point2D.Double[] getMejorSolucion() {
        return mejorSolucion;
    }

    public Double getMejorDistancia() {
        return mejorDistancia;
    }

    public void reset(Distribution distribution, int n) {
        this.distribucion = distribution;
        this.N = n;
        this.mejorSolucion = null;
        this.mejorDistancia = null;
        this.generarDatos();
    }

    public boolean exists() {
        return this.puntos != null;
    }

}
