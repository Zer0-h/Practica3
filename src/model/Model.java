package model;

import view.View;
import controller.Controller;
import java.util.Random;

public class Model {

    // PUNTEROS DEL PATRÓN MVC
    private View vista;
    private Controller controlador;

    private int N; // Número de puntos
    private Punto[] puntos; // Puntos generados según la distribución
    private Punto[] mejorSolucion; // Pareja que forma la mejor solución
    private Double mejorDistancia; // Distancia de la mejor solución

    private Distribution distribucion; // Distribución para generar los puntos
    private Method metodo; // Método algoritmico para resolver el problema
    private boolean minimizar; // Opción para minimizar o maximizar la distáncia entre puntos
    private int ANCHO; // Ancho de la ventana
    private int ALTO; // Alto de la ventana


    // CONSTRUCTORS
    public Model() {
        this.mejorSolucion = new Punto[2];
    }

    public Model(View vista, Controller controlador, int n) {
        this.vista = vista;
        this.controlador = controlador;
        this.N = n;
        this.puntos = null;
        this.mejorSolucion = new Punto[2];
        ANCHO = vista.getGraphWidth();
        ALTO = vista.getGraphHeight();
    }

    private void generarDatos() {
        ANCHO = vista.getGraphWidth();
        ALTO = vista.getGraphHeight();
        puntos = new Punto[N];
        Random rnd = new Random();
        switch (this.distribucion) {
            case GAUSSIAN -> {
                double[] xg = distribucioGaussiana(N);
                double[] yg = distribucioGaussiana(N);
                for (int i = 0; i < puntos.length; i++) {
                    double x = (xg[i] + 1) * ANCHO / 2;// Campana de Gauss en el centro de la ventana
                    double y = (yg[i] + 1) * ALTO / 2;
                    puntos[i] = new Punto(x, y);
                }
            }
            case EXPONENCIAL -> {
                double[] xg = distribucioExponencial(N);
                double[] yg = distribucioExponencial(N);
                for (int i = 0; i < puntos.length; i++) {
                    double x = (xg[i] + 1) * ANCHO / 2;
                    double y = (yg[i] + 1) * ALTO / 2;
                    puntos[i] = new Punto(x, y);
                }
            }
            case UNIFORME -> {
                for (int i = 0; i < puntos.length; i++) {
                    double x = rnd.nextDouble() * ANCHO;
                    double y = rnd.nextDouble() * ALTO;
                    puntos[i] = new Punto(x, y);
                }
            }
            default ->
                throw new AssertionError();
        }
    }

    private double[] distribucioGaussiana(int n) {
        Random rand = new Random();
        double[] v = new double[n];
        double maxAbs = 0;
        for (int i = 0; i < v.length; i++) {
            v[i] = rand.nextGaussian();
            if (Math.abs(v[i]) > maxAbs) {
                maxAbs = Math.abs(v[i]);
            }
        }
        for (int i = 0; i < v.length; i++) {
            v[i] = v[i] / maxAbs;
        }
        return v;
    }

    private double[] distribucioExponencial(int n) {
        Random rand = new Random();
        double[] v = new double[n];
        double max = 0;
        for (int i = 0; i < n; i++) {
            double u = rand.nextDouble(); // U(0,1)
            v[i] = -Math.log(1 - u); // Exponencial(1)
            if (v[i] > max) {
                max = v[i];
            }
        }
        // Normalitzem a l'interval [-1, 1]
        for (int i = 0; i < n; i++) {
            v[i] = (v[i] / max) * 2 - 1;
        }
        return v;
    }

    /**
     * Inicializa los atributos soluciones y distancias para
     */
    public void initSoluciones() {
        mejorSolucion = new Punto[2];
        mejorSolucion[0] = new Punto(0d, 0d);
        mejorSolucion[1] = new Punto(300d, 300d);
        mejorDistancia = minimizar ? Double.MAX_VALUE : Double.MIN_VALUE;
    }

    public void pushSolucion(Punto[] puntos) {
        double distancia = Punto.distancia(puntos[0], puntos[1]);
        if ((minimizar && distancia < mejorDistancia) || (!minimizar && distancia > mejorDistancia)) {
            mejorDistancia = distancia;
            mejorSolucion[0] = puntos[0];
            mejorSolucion[1] = puntos[1];
        }
    }

    // GETTERS & SETTERS
    public View getVista() {
        return vista;
    }

    public void setVista(View vista) {
        this.vista = vista;
    }

    public Controller getControlador() {
        return controlador;
    }

    public void setControlador(Controller controlador) {
        this.controlador = controlador;
    }

    public Punto[] getPuntos() {
        return puntos;
    }

    public void setPuntos(Punto[] puntos) {
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

    public Punto[] getMejorSolucion() {
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
