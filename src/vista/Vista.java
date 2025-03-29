package vista;

import model.Model;
import controlador.Controller;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import javax.swing.*;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;
import model.Distribution;
import model.Method;

public class Vista extends JFrame {

    private Controller controlador;
    private Model modelo;

    // Panells
    private TopPanel topPanel;
    private BottomPanel bottomPanel;
    private GraphPanel graphPanel;

    public Vista() {}

    public Vista(Controller controlador, Model modelo) {
        this.controlador = controlador;
        this.modelo = modelo;
    }

    public void mostrar() {
        setTitle("Pràctica 3 - Algorismes Avançats");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setSize(1000, 900);
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

    public void paintGraph() {
        graphPanel.repaint();
    }

    public void setTime(long nanoseconds) {
        bottomPanel.setTime(nanoseconds);
    }

    public void setBestResult() {
        bottomPanel.displayBestResult(modelo.getMejorSolucion(), modelo.getMejorDistancia());
    }

    public Controller getControlador() {
        return controlador;
    }

    public Model getModelo() {
        return modelo;
    }

    public void setControlador(Controller controlador) {
        this.controlador = controlador;
    }

    public void setModelo(Model modelo) {
        this.modelo = modelo;
    }

    protected void startClicked() {
        if (modelo.exists()) {
            String proximity = topPanel.getProximity();
            Method typeSolution = topPanel.getSolution();

            modelo.setMinimizar(proximity.equals("Cerca"));
            modelo.setMetodo(typeSolution);

            controlador.start();
        }
    }

    protected void generatePointsClicked() {
        Distribution distribution = topPanel.getDistribution();
        int n = topPanel.getQuantityPoints();
        modelo.reset(distribution, n);
        paintGraph();
    }
}
