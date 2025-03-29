package controlador;

import java.awt.geom.Point2D;
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
        modelo.iniciarCalculConcurrent(metodo, puntos, minimizar);
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
