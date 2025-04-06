package controlador;

import java.awt.geom.Point2D;
import model.procesos.AbstractCalculProcess;
import model.procesos.BruteForceProcess;
import model.procesos.ConvexHullProcess;
import model.procesos.DivideAndConquerProcess;
import static model.Metode.CONVEX_HULL;
import model.Model;
import vista.Vista;
import static model.Metode.FORCA_BRUTA;
import static model.Metode.DIVIDEIX;

/**
 * Classe Controlador:
 * S'encarrega de gestionar la interacció entre el model i la vista, seguint el patró arquitectònic MVC.
 * Controla el flux d'execució de l'aplicació i coordina les operacions entre les dades i la interfície gràfica.
 *
 * Funcions principals:
 * - Iniciar el programa i configurar els components principals.
 * - Gestionar l'execució dels algorismes de càlcul segons el mètode seleccionat.
 * - Notificar la vista dels resultats i actualitzacions.
 * - Controlar l'inici dels processos de càlcul i la notificació de finalització.
 *
 * Aquesta classe centralitza la lògica de coordinació, assegurant que el model i la vista
 * es mantinguin desacoblats mitjançant la gestió d'esdeveniments.
 *
 * @author tonitorres
 */
public class Controlador implements Notificar {

    private Model model;
    private Vista vista;

    // Punt d'entrada principal de l'aplicació
    public static void main(String[] args) {
        new Controlador().inicialitzar();
    }

    // Inicialitza el model, la vista i les constants computacionals
    public void inicialitzar() {
        model = new Model();
        vista = new Vista(this);
        calcularConstants();
        vista.mostrar();
    }

    // Calcula les constants computacionals per als diferents algorismes
    private void calcularConstants() {
        // Generem un conjunt de punts per calcular les constants
        model.resetSolucio();
        Point2D.Double[] punts = model.generarPunts(10000);

        // Crear processos per cada algorisme
        AbstractCalculProcess procesBruteForce = new BruteForceProcess(this, punts);
        AbstractCalculProcess procesDivideConquer = new DivideAndConquerProcess(this, punts);
        AbstractCalculProcess procesConvexHull = new ConvexHullProcess(this, punts);

        // Iniciar els processos de forma concurrent
        procesBruteForce.start();
        procesDivideConquer.start();
        procesConvexHull.start();
    }

    // Inicia el procés de càlcul segons el mètode seleccionat
    public void iniciarProces() {
        // Reseteam la solució prèvia
        model.resetSolucio();

        // Oculta la línia de solució inicialment
        model.setMostrarLineaSolucio(false);

        AbstractCalculProcess proces;

        // Selecció de l'algorisme segons el mètode
        switch (model.getMetode()) {
            case FORCA_BRUTA -> proces = new BruteForceProcess(this);
            case DIVIDEIX -> proces = new DivideAndConquerProcess(this);
            case CONVEX_HULL -> {
                // Comprovació de validesa: el convex hull no serveix per a la parella més propera
                if (model.isMinimizar()) {
                    vista.notificar(Notificacio.INVALID);
                    return;
                } else {
                    proces = new ConvexHullProcess(this);
                }
            }
            default -> throw new IllegalArgumentException("Error: Mètode desconegut en arrencada");
        }

        // Iniciar el procés seleccionat
        proces.start();
    }

    // Obtenir el model actual
    public Model getModel() {
        return model;
    }

    // Obtenir la vista actual
    public Vista getVista() {
        return vista;
    }

    // Mètode per gestionar notificacions entre el model, la vista i el controlador
    @Override
    public void notificar(Notificacio notificacio) {
        switch (notificacio) {
            case Notificacio.ARRANCAR -> iniciarProces();
            case Notificacio.FINALITZA -> {
                model.setMostrarLineaSolucio(true); // Mostrar la línia de solució quan finalitzi el càlcul
                vista.notificar(notificacio);
            }
        }
    }
}
