package algoritmosavanzadosp3;

import controller.Controller;
import model.Model;
import view.View;

public class AlgoritmosAvanzadosP3 {

    public static void main(String[] args) {
        MVCInit();
    }

    private static void MVCInit() {
        Model modelo = new Model();
        View vista = new View();
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
