package main;

import controlador.Controller;
import model.Model;
import vista.Vista;

public class main {

    public static void main(String[] args) {
        MVCInit();
    }

    private static void MVCInit() {
        Model modelo = new Model();
        Vista vista = new Vista();
        Controller controlador = new Controller();

        modelo.setVista(vista);
        modelo.setControlador(controlador);

        vista.setModelo(modelo);
        vista.setControlador(controlador);

        controlador.setModelo(modelo);
        controlador.setVista(vista);

        vista.mostrar();
    }

}
