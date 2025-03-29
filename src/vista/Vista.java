package vista;

import model.Model;
import controlador.Controlador;
import controlador.Notificacio;
import controlador.Notificar;
import java.awt.*;
import javax.swing.*;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;
import model.Distribution;
import model.Method;

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

    public void setBestResult() {
        bottomPanel.displayBestResult(modelo.getPuntsSolucio(), modelo.getMejorDistancia());
    }

    public Model getModelo() {
        return modelo;
    }

    protected void startClicked() {
        if (modelo.exists()) {
            String proximity = topPanel.getProximity();
            Method typeSolution = topPanel.getSolution();

            modelo.setMinimizar(proximity.equals("Cerca"));
            modelo.setMetodo(typeSolution);

            controlador.notificar(Notificacio.ARRANCAR);
        }
    }

    protected void generatePointsClicked() {
        Distribution distribution = topPanel.getDistribution();
        int n = topPanel.getQuantityPoints();
        modelo.reset(distribution, n);
        paintGraph();
    }

    private void finalitza() {
        bottomPanel.setTime(this.modelo.getTemps());
        setBestResult();
        paintGraph();
    }

    @Override
    public void notificar(Notificacio n) {
        switch (n) {
            case Notificacio.FINALITZA ->
                finalitza();
        }
    }
}
