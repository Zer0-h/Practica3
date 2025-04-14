package controlador;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.SwingWorker;
import model.ComparativaResultat;
import model.Metode;
import static model.Metode.CONVEX_HULL;
import static model.Metode.DIVIDEIX;
import static model.Metode.FORCA_BRUTA;
import model.Model;
import procesos.AbstractCalculProcess;
import procesos.BruteForceProcess;
import procesos.ConvexHullProcess;
import procesos.DivideAndConquerProcess;
import vista.Vista;

/**
 * Classe Controlador:
 * S'encarrega de gestionar la interacció entre el model i la vista, seguint el
 * patró arquitectònic MVC.
 * Controla el flux d'execució de l'aplicació i coordina les operacions entre
 * les dades i la interfície gràfica.
 *
 * Aquesta classe centralitza la lògica de coordinació, assegurant que el model
 * i la vista es mantinguin desacoblats mitjançant la gestió d'esdeveniments.
 *
 * @author tonitorres
 */
public class Controlador {

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
        Point2D.Double[] punts = model.generarPunts(100000);

        // Crear processos per cada algorisme
        AbstractCalculProcess procesBruteForce = new BruteForceProcess(true, punts);
        AbstractCalculProcess procesDivideConquer = new DivideAndConquerProcess(true, punts);
        AbstractCalculProcess procesConvexHull = new ConvexHullProcess(false, punts);

        // Iniciar els processos de forma concurrent
        executarProcesCM(procesBruteForce, punts.length);
        executarProcesCM(procesDivideConquer, punts.length);
        executarProcesCM(procesConvexHull, punts.length);
    }

    private void executarProcesCM(AbstractCalculProcess proces, long n) {
        long tempsInicial = System.nanoTime();

        // Llança el procés en segon pla amb SwingWorker
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                proces.run();
                return null;
            }

            @Override
            protected void done() {
                long tempsExecucio = System.nanoTime() - tempsInicial;
                double segons = tempsExecucio / 1_000_000_000.0;
                model.actualitzarConstant(n, segons, proces.getMetode());
            }
        };
        worker.execute();
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
                proces = new BruteForceProcess(model.esMinimizar(), model.getPunts());
            case DIVIDEIX ->
                proces = new DivideAndConquerProcess(model.esMinimizar(), model.getPunts());
            case CONVEX_HULL -> {
                // Comprovació de validesa: el convex hull no serveix per a la parella més propera
                if (model.esMinimizar()) {
                    vista.errorExecucio("No es pot executar el procés " + Metode.CONVEX_HULL + " per a la parella de punts més propera");
                    return;
                } else {
                    proces = new ConvexHullProcess(model.esMinimizar(), model.getPunts());
                }
            }
            default ->
                throw new IllegalArgumentException("Error: Mètode desconegut en arrencada");
        }

        long tempsInicial = System.nanoTime();

        // Llança el procés en segon pla
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                proces.run();
                return null;
            }

            @Override
            protected void done() {
                long tempsExecucio = System.nanoTime() - tempsInicial;
                double segons = tempsExecucio / 1_000_000_000.0;
                model.actualitzarConstant(model.getPunts().length, segons, proces.getMetode());
                model.setTemps(segons);
                model.setSolucio(proces.getPuntsSolucio());
                vista.finalitza();
            }
        };
        worker.execute();
    }

    /**
     * Inicia la comparativa dels processos de manera paral·lela i mostra els
     * resultats
     * a mesura que es completen.
     */
    public void comparativaProcessos() {
        model.resetSolucio();
        model.setMostrarLineaSolucio(false);

        // Crear processos per a cada mètode
        List<Callable<ComparativaResultat>> tasques = new ArrayList<>();
        tasques.add(crearTascaComparativa(new BruteForceProcess(model.esMinimizar(), model.getPunts()), "Força Bruta"));
        tasques.add(crearTascaComparativa(new DivideAndConquerProcess(model.esMinimizar(), model.getPunts()), "Divideix i Venceràs"));

        // Només afegir Convex Hull si estem en mode maximitzar
        if (!model.esMinimizar()) {
            tasques.add(crearTascaComparativa(new ConvexHullProcess(model.esMinimizar(), model.getPunts()), "Convex Hull"));
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
}
