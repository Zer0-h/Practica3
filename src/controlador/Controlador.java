package controlador;

import model.BruteForceProcess;
import model.DivideAndConquerProcess;
import model.Metode;
import model.Model;
import vista.Vista;

public class Controlador implements Notificar{

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

        vista.mostrar();
    }

    public void iniciarProces() {
        // Inicializamos el array de soluciones
        model.initSoluciones();
        model.setMostrarLineaSolucio(false);

        Thread proces;
        if (model.getMetodo() == Metode.FUERZA_BRUTA) {
            proces = new BruteForceProcess(this);
        } else {
            proces = new DivideAndConquerProcess(this);
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
