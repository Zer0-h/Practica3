package controlador;

import java.awt.geom.Point2D;
import model.Method;
import model.Model;
import vista.Vista;

public class Controller implements Notificar{

    private Model model;
    private Vista vista;

    public Controller() {
    }

    public Controller(Model modelo, Vista vista) {
        this.model = modelo;
        this.vista = vista;
    }

    public static void main(String[] args) {
        new Controller().iniciar();
    }

    public void iniciar() {
        model = new Model();
        vista = new Vista(this);

        model.setVista(vista);
        model.setControlador(this);

        vista.setModelo(model);
        vista.setControlador(this);

        vista.mostrar();
    }

    public void iniciarProces() {
        Point2D.Double[] puntos = model.getPuntos();
        Method metodo = model.getMetodo();
        boolean minimizar = model.isMinimizar();

        // Inicializamos el array de soluciones
        model.initSoluciones();
        model.iniciarCalculConcurrent(metodo, puntos, minimizar);
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
            case Notificacio.PINTAR ->
                System.out.println("XD");
        }
    }

}
