package controlador;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import model.ComparativaResultat;
import static model.Metode.CONVEX_HULL;
import static model.Metode.DIVIDEIX;
import static model.Metode.FORCA_BRUTA;
import model.Model;
import model.procesos.AbstractCalculProcess;
import model.procesos.BruteForceProcess;
import model.procesos.ConvexHullProcess;
import model.procesos.DivideAndConquerProcess;
import vista.Vista;

/**
 * Classe Controlador:
 * S'encarrega de gestionar la interacció entre el model i la vista, seguint el
 * patró arquitectònic MVC.
 * Controla el flux d'execució de l'aplicació i coordina les operacions entre
 * les dades i la interfície gràfica.
 *
 * Aquesta classe centralitza la lògica de coordinació, assegurant que el model
 * i la vista
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
            case FORCA_BRUTA ->
                proces = new BruteForceProcess(this);
            case DIVIDEIX ->
                proces = new DivideAndConquerProcess(this);
            case CONVEX_HULL -> {
                // Comprovació de validesa: el convex hull no serveix per a la parella més propera
                if (model.esMinimizar()) {
                    vista.notificar(Notificacio.INVALID);
                    return;
                } else {
                    proces = new ConvexHullProcess(this);
                }
            }
            default ->
                throw new IllegalArgumentException("Error: Mètode desconegut en arrencada");
        }

        // Iniciar el procés seleccionat
        proces.start();
    }

    /**
     * Inicia la comparativa dels processos de manera paral·lela i mostra els
     * resultats
     * a mesura que es completen.
     */
    public void comparativaProcessos() {
        model.resetSolucio();
        model.setMostrarLineaSolucio(false);

        // Obtenir punts generats
        Point2D.Double[] punts = model.getPunts();

        // Crear processos per a cada mètode
        List<Callable<ComparativaResultat>> tasques = new ArrayList<>();
        tasques.add(crearTascaComparativa(new BruteForceProcess(this, punts), "Força Bruta"));
        tasques.add(crearTascaComparativa(new DivideAndConquerProcess(this, punts), "Divideix i Venceràs"));

        // Només afegir Convex Hull si estem en mode maximitzar
        if (!model.esMinimizar()) {
            tasques.add(crearTascaComparativa(new ConvexHullProcess(this, punts), "Convex Hull"));
        }

        // Utilitzar un executor per paral·lelitzar les comparatives
        ExecutorService executor = Executors.newFixedThreadPool(tasques.size());

        // Executem les tasques de forma asíncrona
        for (Callable<ComparativaResultat> tasca : tasques) {
            executor.submit(() -> {
                try {
                    ComparativaResultat resultat = tasca.call();
                    vista.mostrarResultatComparativa(resultat);
                } catch (Exception e) {
                    vista.mostrarResultatComparativa(new ComparativaResultat("Error", 0, null));
                }
            });
        }

        executor.shutdown();
    }

    /**
     * Crea una tasca per comparar un procés concret.
     *
     * @param proces El procés d'algorisme.
     * @param nom    El nom del procés.
     *
     * @return Una tasca Callable que retorna el resultat com a objecte
     *         ComparativaResultat.
     */
    private Callable<ComparativaResultat> crearTascaComparativa(AbstractCalculProcess proces, String nom) {
        return () -> {
            long startTime = System.nanoTime();
            Point2D.Double[] puntsSolucio = proces.calcularSolucio();
            long elapsedTime = System.nanoTime() - startTime;
            double segons = elapsedTime / 1_000_000_000.0;

            if (puntsSolucio == null) {
                return new ComparativaResultat(nom, 0, null);
            }

            return new ComparativaResultat(nom, segons, puntsSolucio);
        };
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
            case Notificacio.ARRANCAR ->
                iniciarProces();
            case Notificacio.FINALITZA -> {
                model.setMostrarLineaSolucio(true); // Mostrar la línia de solució quan finalitzi el càlcul
                vista.notificar(notificacio);
            }
            case Notificacio.COMPARAR ->
                comparativaProcessos();
        }
    }
}
