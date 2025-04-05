package controlador;

import java.awt.geom.Point2D;
import model.AbstractCalculProcess;
import model.BruteForceProcess;
import model.ConvexHullProcess;
import model.DivideAndConquerProcess;
import static model.Metode.CONVEX_HULL;
import static model.Metode.DIVIDE_Y_VENCERAS;
import static model.Metode.FUERZA_BRUTA;
import model.Model;
import vista.Vista;

public class Controlador implements Notificar {

    private Model model;
    private Vista vista;

    public Controlador() {
    }

    public Controlador(Model modelo, Vista vista) {
        this.model = modelo;
        this.vista = vista;
    }

    public static void main(String[] args) {
        new Controlador().iniciar();
    }

    public void iniciar() {
        model = new Model();
        vista = new Vista(this);
        calcularConstants();
        vista.mostrar();
    }

    private void calcularConstants() {
        Point2D.Double[] punts = model.generarPunts(10000);
        model.initSoluciones();
        AbstractCalculProcess proces1 = new BruteForceProcess(this, punts);
        AbstractCalculProcess proces2 = new DivideAndConquerProcess(this, punts);
        AbstractCalculProcess proces3 = new ConvexHullProcess(this, punts);

        proces1.start();
        proces2.start();
        proces3.start();
    }

    public void iniciarProces() {
        // Inicializamos el array de soluciones
        model.initSoluciones();
        model.setMostrarLineaSolucio(false);

        AbstractCalculProcess proces;

        switch (model.getMetodo()) {
            case FUERZA_BRUTA -> proces = new BruteForceProcess(this);
            case DIVIDE_Y_VENCERAS -> proces = new DivideAndConquerProcess(this);
            case CONVEX_HULL -> {
                if (model.isMinimizar()) {
                    vista.notificar(Notificacio.INVALID);
                    return;
                } else {
                    proces = new ConvexHullProcess(this);
                }
            }
            default -> throw new IllegalArgumentException("Intentant arrancar metode desconegut");
        }

        proces.start();
    }

    public Model getModelo() {
        return model;
    }

    public Vista getVista() {
        return vista;
    }

    @Override
    public void notificar(Notificacio n) {
        switch (n) {
            case Notificacio.ARRANCAR ->
                iniciarProces();
            case Notificacio.FINALITZA -> {
                model.setMostrarLineaSolucio(true);
                vista.notificar(n);
            }
        }
    }

}
