package controlador;

import java.awt.geom.Point2D;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import model.Method;
import model.Model;
import vista.Vista;

public class Controller {

    private Model modelo;
    private Vista vista;

    public Controller() {
    }

    public Controller(Model modelo, Vista vista) {
        this.modelo = modelo;
        this.vista = vista;
    }

    public void start() {
        Point2D.Double[] puntos = modelo.getPuntos();
        Method metodo = modelo.getMetodo();
        boolean minimizar = modelo.isMinimizar();

        // Inicializamos el array de soluciones
        modelo.initSoluciones();

        long tiempoI = System.nanoTime();
        switch (metodo) {
            case FUERZA_BRUTA ->
                trobarParellaCurtaClasic(puntos);
            case DIVIDE_Y_VENCERAS -> {
                trobarParellaDivideAndConquer(puntos, minimizar);
            }
            default ->
                throw new AssertionError();
        }
        this.vista.setTime(System.nanoTime() - tiempoI);
        this.vista.setBestResult();
        this.vista.paintGraph();
    }

    private void trobarParellaCurtaClasic(Point2D.Double[] puntos) {
        // Buscamos las distancias de todos los puntos entre ellos
        for (int i = 0; i < puntos.length; i++) {
            for (int j = i + 1; j < puntos.length; j++) {
                // Comprobamos si es solución y la guardamos
                Point2D.Double[] posibleSolucion = {puntos[i], puntos[j]};
                modelo.pushSolucion(posibleSolucion);
            }
        }

    }

    private double trobarParellaDivideAndConquer(Point2D.Double[] punts, boolean minimizar) {
        // Ordenem per X abans de començar
        Arrays.sort(punts, Comparator.comparingDouble(p -> p.getX()));
        return dc(punts, 0, punts.length - 1, minimizar);
    }

    private double dc(Point2D.Double[] punts, int left, int right, boolean minimizar) {
        if (right - left <= 3) {
            double minMax = minimizar ? Double.MAX_VALUE : Double.MIN_VALUE;

            for (int i = left; i <= right; i++) {
                for (int j = i + 1; j <= right; j++) {
                    double d = punts[i].distance(punts[j]);
                    Point2D.Double[] possible = {punts[i], punts[j]};
                    modelo.pushSolucion(possible);
                    if (minimizar) {
                        minMax = Math.min(minMax, d);
                    } else {
                        minMax = Math.max(minMax, d);
                    }
                }
            }
            return minMax;
        }

        int mid = (left + right) / 2;
        double dLeft = dc(punts, left, mid, minimizar);
        double dRight = dc(punts, mid + 1, right, minimizar);
        double d = minimizar ? Math.min(dLeft, dRight) : Math.max(dLeft, dRight);

        double midX = punts[mid].getX();
        Point2D.Double[] franja = new Point2D.Double[right - left + 1];
        int idx = 0;

        for (int i = left; i <= right; i++) {
            if (Math.abs(punts[i].getX() - midX) < d) {
                franja[idx++] = punts[i];
            }
        }

        Arrays.sort(franja, 0, idx, Comparator.comparingDouble(p -> p.getY()));

        for (int i = 0; i < idx; i++) {
            for (int j = i + 1; j < idx && (franja[j].getY() - franja[i].getY()) < d; j++) {
                double dist = franja[i].distance(franja[j]);
                Point2D.Double[] possible = {franja[i], franja[j]};
                modelo.pushSolucion(possible);
                if (minimizar) {
                    d = Math.min(d, dist);
                } else {
                    d = Math.max(d, dist);
                }
            }
        }

        return d;
    }


    public Model getModelo() {
        return modelo;
    }

    public void setModelo(Model modelo) {
        this.modelo = modelo;
    }

    public Vista getVista() {
        return vista;
    }

    public void setVista(Vista vista) {
        this.vista = vista;
    }

}
