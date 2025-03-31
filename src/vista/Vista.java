package vista;

import model.Model;
import controlador.Controlador;
import controlador.Notificacio;
import controlador.Notificar;
import java.awt.*;
import javax.swing.*;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;
import model.Distribucio;
import model.Metode;
import model.Tipus;

public class Vista extends JFrame implements Notificar {

    private Controlador controlador;
    private Model modelo;

    // Panells
    private TopPanel topPanel;
    private BottomPanel bottomPanel;
    private GraphPanel graphPanel;

    public Vista() {}

    public Vista(Controlador controlador) {
        this.controlador = controlador;
        this.modelo = controlador.getModelo();
    }

    public void mostrar() {
        setTitle("Pràctica 3 - Divideix i Venceràs");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setSize(1200, 900);
        setLocationRelativeTo(null);

        // Panell Superior
        topPanel = new TopPanel(this);
        add(topPanel, BorderLayout.NORTH);

        // Panell Central (Gràfic)
        graphPanel = new GraphPanel(this, 800, 600);
        add(graphPanel, BorderLayout.CENTER);

        // Panell Inferior
        bottomPanel = new BottomPanel(this);
        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);

        modelo.setPanelSize(graphPanel.getWidth(), graphPanel.getHeight());
    }

    public void pintar() {
        graphPanel.repaint();
    }

    public void setBestResult() {
        bottomPanel.displayBestResult(modelo.getPuntsSolucio());
    }

    public Model getModelo() {
        return modelo;
    }

    protected void startClicked() {
        if (modelo.tePunts()) {
            Tipus problema = topPanel.getProximity();
            Metode typeSolution = topPanel.getSolution();

            modelo.setMinimizar(problema == Tipus.PROPER);
            modelo.setMetodo(typeSolution);

            bottomPanel.startProgress();
            toggleInProgress(false);  // Disable the button

            controlador.notificar(Notificacio.ARRANCAR);
        }
    }

    protected void generatePointsClicked() {
        Distribucio distribution = topPanel.getDistribution();
        modelo.setDistribucion(distribution);
        modelo.generarDatos(topPanel.getQuantityPoints());
        pintar();
    }

    private void finalitza() {
        bottomPanel.stopProgress();
        toggleInProgress(true);  // Disable the button
        bottomPanel.setTime(this.modelo.getTemps());
        setBestResult();
        pintar();
    }

    protected void toggleInProgress(boolean inProgress) {
        topPanel.toggleInProgress(inProgress);
    }

    @Override
    public void notificar(Notificacio n) {
        switch (n) {
            case Notificacio.FINALITZA ->
                finalitza();
        }
    }
}
