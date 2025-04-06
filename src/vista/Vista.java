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

/**
 * Classe Vista: Interfície gràfica de l'aplicació
 * S'encarrega de mostrar els elements gràfics, gestionar els panells
 * i interactuar amb el controlador.
 *
 * @author tonitorres
 */
public class Vista extends JFrame implements Notificar {

    private Controlador controlador;
    private Model model;

    // Panells de la vista
    private TopPanel topPanel;
    private BottomPanel bottomPanel;
    private GraphPanel graphPanel;

    /**
     * Constructor per defecte.
     */
    public Vista() {}

    /**
     * Constructor que inicialitza la vista amb el controlador.
     * @param controlador Controlador de l'aplicació.
     */
    public Vista(Controlador controlador) {
        this.controlador = controlador;
        this.model = controlador.getModel();
    }

    /**
     * Configura i mostra la finestra principal.
     */
    public void mostrar() {
        setTitle("Pràctica 3 - Divideix i Venceràs");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setSize(1200, 900);
        setLocationRelativeTo(null);

        // Panell superior amb les opcions de configuració
        topPanel = new TopPanel(this);
        add(topPanel, BorderLayout.NORTH);

        // Panell central per al gràfic
        graphPanel = new GraphPanel(800, 600);
        add(graphPanel, BorderLayout.CENTER);

        // Panell inferior per a informació i resultats
        bottomPanel = new BottomPanel();
        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);

        // Ajustar la mida del panell en el model
        model.setMidaPanel(graphPanel.getWidth(), graphPanel.getHeight());
    }

    /**
     * Repinta el panell gràfic.
     */
    public void pintar() {
        graphPanel.repaint();
    }

    /**
     * Actualitza el resultat òptim en el panell inferior.
     */
    public void setMillorResultat() {
        bottomPanel.displaySolution(model.getPuntsSolucio());
    }

    /**
     * Obté el model associat a la vista.
     * @return Model de dades.
     */
    public Model getModel() {
        return model;
    }

    /**
     * Acció en clicar el botó d'iniciar el càlcul.
     */
    protected void startClicked() {
        if (model.tePunts()) {
            Tipus problema = topPanel.getProblema();
            Metode tipusSolucio = topPanel.getSolucio();

            // Configura el model segons el tipus de problema
            model.setMinimizar(problema == Tipus.PROPER);
            model.setMetode(tipusSolucio);

            // Calcula el temps estimat abans d'iniciar
            bottomPanel.setTempsEstimat(model.calcularTempsEstimacio());

            // Mostra la barra de progrés i desactiva els botons
            bottomPanel.startProgress();
            toggleInProgress(false);

            // Notifica al controlador per començar el procés
            controlador.notificar(Notificacio.ARRANCAR);
        }
    }

    /**
     * Acció en clicar el botó de generar punts.
     */
    protected void generatePointsClicked() {
        Distribucio distribucio = topPanel.getDistribucio();
        model.setDistribucio(distribucio);
        model.generarNuvolPunts(topPanel.getQuantitatPunts());
        graphPanel.colocaPunts(model.getPunts());
        pintar();
    }

    /**
     * Finalitza el càlcul i mostra els resultats.
     */
    private void finalitza() {
        bottomPanel.stopProgress();
        toggleInProgress(true);
        graphPanel.dibuixaLineaSolucio(model.getPuntsSolucio());
        bottomPanel.setTempsReal(model.getTemps());
        setMillorResultat();
        pintar();
    }

    /**
     * Activa o desactiva els botons segons l'estat d'execució.
     * @param enExecucio Estat d'execució (true si s'està executant).
     */
    protected void toggleInProgress(boolean enExecucio) {
        topPanel.toggleInProgress(enExecucio);
    }

    /**
     * Mostra un missatge d'error en cas d'operació no vàlida.
     */
    protected void invalid() {
        toggleInProgress(true);
        bottomPanel.stopProgress();
        JOptionPane.showMessageDialog(
            null,
            "No es pot executar el procés " + Metode.CONVEX_HULL + " per a la parella de punts més propera",
            "Error d'execució",
            JOptionPane.WARNING_MESSAGE
        );
    }

    /**
     * Mètode que rep notificacions del controlador.
     * @param n Tipus de notificació.
     */
    @Override
    public void notificar(Notificacio n) {
        switch (n) {
            case Notificacio.FINALITZA -> finalitza();
            case Notificacio.INVALID -> invalid();
        }
    }
}
