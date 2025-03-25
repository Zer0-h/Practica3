package vista;

import controlador.Controlador;
import controlador.Notificacio;
import controlador.Notificar;
import java.awt.*;
import javax.swing.*;
import model.Model;

/**
 * Classe Vista que representa la interfície gràfica de l'usuari (GUI).
 * Controla la interacció amb l'usuari i mostra l'estat del procés de resolució
 * del problema del Tromino. Implementa el patró MVC com a part de la Vista.
 *
 * @author tonitorres
 */
public class Vista extends JFrame implements Notificar {

    private final JButton btnIniciar, btnNetejar, btnAturar;
    private final JComboBox<Integer> selectorPotencia;
    private final JComboBox<String> selectorColor;
    private JLabel lblTemps;
    private final TaulerPanel taulerPanel;
    private final Controlador controlador;
    private double tempsEstimat = 0.0;

    /**
     * Constructor que inicialitza la interfície gràfica.
     *
     * @param c Instància principal de l'aplicació
     */
    public Vista(Controlador c) {
        controlador = c;
        setTitle("Pràctica 2 - Backtracking");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Creació dels components principals de la interfície
        selectorPotencia = new JComboBox<>(controlador.getModel().getMidesSeleccionables());
        selectorColor = new JComboBox<>(new String[]{"Blanc", "Vermell", "Blau", "Magenta", "Groc", "Verd", "Taronja", "Cian"});
        btnIniciar = new JButton("Iniciar");
        btnNetejar = new JButton("Neteja");
        btnAturar = new JButton("Aturar");

        inicialitzarBotons();
        JPanel panelControl = crearPanellControl();
        JPanel panelTemps = crearPanellTemps();

        add(panelControl, BorderLayout.NORTH);
        taulerPanel = new TaulerPanel(controlador);
        add(taulerPanel, BorderLayout.CENTER);
        add(panelTemps, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Inicialitza els botons i els seus listeners per gestionar les accions de
     * l'usuari.
     */
    private void inicialitzarBotons() {
        botonsModeInici();

        Model model = controlador.getModel();

        // Canviar potencia del tauler quan l'usuari selecciona una nova potència
        selectorPotencia.addActionListener(e -> {
            if (!model.getEnExecucio()) {
                model.inicialitzaTauler((int) selectorPotencia.getSelectedItem());
                botonsModeInici();
                taulerPanel.pintar();
            }
        });

        // Canviar els colors dels trominos
        selectorColor.addActionListener(e -> model.setTrominoColor(getColorPerNom((String) selectorColor.getSelectedItem())));

        // Iniciar la resolució del problema quan es prem el botó "Iniciar"
        btnIniciar.addActionListener(e -> {
            controlador.notificar(Notificacio.ARRANCAR);
            activarModeExecucio();
            setTempsEstimat(model.estimaTempsExecucio());
        });

        // Netejar el tauler quan es prem el botó "Neteja"
        btnNetejar.addActionListener(e -> netejarTauler());

        // Aturar l'execució del procés quan es prem el botó "Aturar"
        btnAturar.addActionListener(e -> {
            controlador.notificar(Notificacio.ATURAR);
            desactivarModeExecucio();
        });
    }

    /**
     * Crea el panell de control amb la selecció de potència i els botons.
     *
     * @return JPanel amb els controls de l'usuari
     */
    private JPanel crearPanellControl() {
        JPanel panel = new JPanel();
        panel.add(new JLabel("Profunditat:"));
        panel.add(selectorPotencia);
        panel.add(new JLabel("Color:"));
        panel.add(selectorColor);
        panel.add(btnIniciar);
        panel.add(btnNetejar);
        panel.add(btnAturar);
        return panel;
    }

    /**
     * Crea el panell de visualització del temps estimat i real.
     *
     * @return JPanel amb la informació del temps d'execució
     */
    private JPanel crearPanellTemps() {
        JPanel panel = new JPanel();
        lblTemps = new JLabel("Temps Estimat: -- s | Temps Real: -- s");
        panel.add(lblTemps);
        return panel;
    }

    private void botonsModeInici() {
        btnIniciar.setEnabled(false);
        btnNetejar.setEnabled(false);
        btnAturar.setEnabled(false);
    }

    /**
     * Activa el mode d'execució, desactivant alguns botons per evitar
     * interaccions incorrectes.
     */
    private void activarModeExecucio() {
        selectorPotencia.setEnabled(false);
        btnIniciar.setEnabled(false);
        btnNetejar.setEnabled(false);
        btnAturar.setEnabled(true);
    }

    /**
     * Desactiva el mode d'execució i permet a l'usuari interaccionar de nou amb
     * els controls.
     */
    private void desactivarModeExecucio() {
        selectorPotencia.setEnabled(true);
        btnNetejar.setEnabled(true);
        btnAturar.setEnabled(false);
    }

    /**
     * Estableix el temps estimat d'execució.
     *
     * @param tempsEstimat Temps estimat en segons
     */
    public void setTempsEstimat(double tempsEstimat) {
        this.tempsEstimat = tempsEstimat;
        actualitzaEtiquetaTemps(null);
    }

    /**
     * Estableix el temps real d'execució un cop finalitzada la resolució.
     *
     * @param tempsReal Temps real en segons
     */
    public void setTempsReal(double tempsReal) {
        actualitzaEtiquetaTemps(tempsReal);
    }

    /**
     * Actualitza la visualització del temps d'execució a la interfície gràfica.
     *
     * @param tempsReal Temps real d'execució, o Optional.empty() si encara no
     *                  s'ha finalitzat
     */
    private void actualitzaEtiquetaTemps(Double tempsReal) {
        lblTemps.setText(
                tempsReal == null
                        ? String.format("Temps Estimat: %.2f s | Temps Real: -- s", tempsEstimat)
                        : String.format("Temps Estimat: %.2f s | Temps Real: %.2f s", tempsEstimat, tempsReal)
        );
    }

    /**
     * Gestiona les notificacions rebudes del controlador i actualitza la
     * interfície gràfica en conseqüència.
     *
     * @param notificacio Tipus de notificació rebuda
     */
    @Override
    public void notificar(Notificacio notificacio) {
        switch (notificacio) {
            case Notificacio.PINTAR ->
                taulerPanel.pintar();
            case Notificacio.FINALITZA -> {
                setTempsReal(controlador.getModel().getTempsExecucio());
                desactivarModeExecucio();
            }
            case Notificacio.SELECCIONA_FORAT -> {
                btnIniciar.setEnabled(true);
                btnNetejar.setEnabled(true);
            }
        }
    }

    /**
     * Retorna el color corresponent segons el nom seleccionat.
     *
     * @param colorNom Nom del color seleccionat
     *
     * @return Color associat
     */
    private Color getColorPerNom(String colorNom) {
        return switch (colorNom) {
            case "Vermell" ->
                Color.RED;
            case "Blau" ->
                Color.BLUE;
            case "Magenta" ->
                Color.MAGENTA;
            case "Groc" ->
                Color.YELLOW;
            case "Verd" ->
                Color.GREEN;
            case "Taronja" ->
                Color.ORANGE;
            case "Cian" ->
                Color.CYAN;
            default ->
                Color.WHITE;
        };
    }

    /**
     * Reinicia el tauler.
     */
    private void netejarTauler() {
        controlador.getModel().inicialitzaTauler((int) selectorPotencia.getSelectedItem());
        taulerPanel.pintar();
        botonsModeInici();
    }
}
