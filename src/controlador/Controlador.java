package controlador;

import model.Model;
import model.TrominoRecursiu;
import vista.Vista;

/**
 * Classe principal del programa que gestiona la inicialització i la comunicació
 * entre el Model, la Vista i el Controlador seguint el patró MVC.
 *
 * @author tonitorres
 */
public class Controlador implements Notificar {

    // Vista de la interfície gràfica d'usuari (GUI)
    private Vista vista;

    // Model que conté la lògica i les dades del problema
    private Model model;

    // Controlador que implementa la resolució del problema de Tromino de forma recursiva
    private TrominoRecursiu trominoRecursiu;

    /**
     * Mètode principal que inicia l'execució del programa.
     *
     * @param args Arguments de la línia de comandes (no s'utilitzen)
     */
    public static void main(String[] args) {
        new Controlador().iniciar();
    }

    /**
     * Inicialitza el Model i la Vista.
     *
     * - Crea el model i inicialitza el tauler amb la mida mínima disponible.
     * - Crea la vista i li passa una referència d'aquesta classe per gestionar
     * notificacions.
     */
    public void iniciar() {
        model = new Model();
        model.inicialitzaTauler(model.getMidesSeleccionables()[0]); // Comença amb la mida mínima
        vista = new Vista(this);
    }

    /**
     * Retorna el Model actual de l'aplicació.
     *
     * @return instància del Model
     */
    public Model getModel() {
        return model;
    }

    /**
     * Gestiona les notificacions rebudes d'altres components (Vista o
     * Controlador). S'encarrega d'executar accions segons el tipus de
     * notificació rebut.
     *
     * @param n Tipus de notificació
     */
    @Override
    public void notificar(Notificacio n) {
        switch (n) {
            case Notificacio.ARRANCAR ->
                iniciarResolucio(); // Inicia la resolució del problema
            case Notificacio.ATURAR ->
                aturarResolucio();   // Atura la resolució del problema
            case Notificacio.FINALITZA -> {
                // Marca el model com a no en execució i finalitzat i notifica a la vista.
                model.setEnExecucio(false);
                model.setResolt(true);
                vista.notificar(n);
            }
            case Notificacio.PINTAR, Notificacio.SELECCIONA_FORAT ->
                // Notifica la vista per repintar o gestionar la selecció d'un forat
                vista.notificar(n);
        }
    }

    /**
     * Inicia la resolució del problema:
     *
     * - Marca el model com en execució.
     * - Crea un nou controlador `TrominoRecursiu` i inicia el procés en un fil
     * independent.
     */
    private void iniciarResolucio() {
        model.setEnExecucio(true);
        trominoRecursiu = new TrominoRecursiu(this);
        trominoRecursiu.start();
    }

    /**
     * Atura la resolució del problema:
     *
     * - Marca el model com a no en execució.
     * - Indica al controlador que ha d'aturar-se.
     */
    private void aturarResolucio() {
        model.setEnExecucio(false);
        trominoRecursiu.atura();
    }
}
