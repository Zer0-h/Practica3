package vista;

import java.awt.Color;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import model.Distribucio;
import model.Metode;
import model.Tipus;

/**
 * Panell superior de la interfície gràfica.
 * Inclou les opcions per seleccionar la distribució, el problema, el nombre de punts
 * i el mètode de solució, així com botons per generar els punts i iniciar el càlcul.
 *
 * @autor tonitorres
 */
public class TopPanel extends JPanel {

    private final Vista vista;

    // Components d'interacció
    private JComboBox<Distribucio> distribucioCombo;
    private JComboBox<Tipus> problemaCombo;
    private JComboBox<Metode> solucioCombo;
    private JComboBox<String> quantitatPuntsCombo;

    private JButton botoGenerar;
    private JButton botoIniciar;

    /**
     * Constructor que inicialitza el panell superior amb la vista associada.
     *
     * @param vista La vista principal de l'aplicació.
     */
    public TopPanel(Vista vista) {
        this.vista = vista;
        this.init();
    }

    /**
     * Inicialitza el panell amb els components gràfics i els botons.
     */
    private void init() {
        // Configuració bàsica del panell
        this.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        this.setBackground(Color.LIGHT_GRAY);

        // Etiqueta per la distribució
        JLabel distribucioLabel = new JLabel("Distribució:");
        this.add(distribucioLabel);

        // ComboBox per seleccionar la distribució
        this.distribucioCombo = new JComboBox<>(Distribucio.values());
        this.add(distribucioCombo);

        // Etiqueta per al tipus de problema
        JLabel problemaLabel = new JLabel("Problema:");
        this.add(problemaLabel);

        // ComboBox per seleccionar el problema (proximitat o llunyania)
        this.problemaCombo = new JComboBox<>(Tipus.values());
        this.add(problemaCombo);

        // Etiqueta per la quantitat de punts
        JLabel quantitatPuntsLabel = new JLabel("Punts:");
        this.add(quantitatPuntsLabel);

        // ComboBox per seleccionar el nombre de punts a generar
        this.quantitatPuntsCombo = new JComboBox<>(new String[]{
            "1000", "10000", "100000", "1000000",
            "2500000", "5000000", "7500000", "10000000"
        });
        this.add(quantitatPuntsCombo);

        // Etiqueta per al mètode de solució
        JLabel solucioLabel = new JLabel("Solució:");
        this.add(solucioLabel);

        // ComboBox per seleccionar el mètode de solució
        this.solucioCombo = new JComboBox<>(Metode.values());
        this.add(solucioCombo);

        // Botó per generar punts
        this.botoGenerar = new JButton("Generar Punts");
        this.add(botoGenerar);

        // Botó per iniciar el càlcul
        this.botoIniciar = new JButton("Iniciar");
        this.add(botoIniciar);

        // Assignació d'accions als botons
        botoGenerar.addActionListener(e -> vista.generatePointsClicked());
        botoIniciar.addActionListener(e -> vista.startClicked());
    }

    /**
     * Activa o desactiva els botons en funció de l'estat del procés.
     *
     * @param enExecucio Indica si el procés està en execució.
     */
    protected void toggleInProgress(boolean enExecucio) {
        botoIniciar.setEnabled(enExecucio);
        botoGenerar.setEnabled(enExecucio);
    }

    // GETTERS

    /**
     * Retorna la distribució seleccionada.
     *
     * @return Distribució seleccionada al ComboBox.
     */
    protected Distribucio getDistribucio() {
        return (Distribucio) this.distribucioCombo.getSelectedItem();
    }

    /**
     * Retorna el tipus de problema seleccionat (proximitat o llunyania).
     *
     * @return Tipus de problema seleccionat al ComboBox.
     */
    protected Tipus getProblema() {
        return (Tipus) this.problemaCombo.getSelectedItem();
    }

    /**
     * Retorna la quantitat de punts seleccionada.
     *
     * @return Nombre de punts seleccionat al ComboBox.
     */
    protected int getQuantitatPunts() {
        return Integer.parseInt((String) this.quantitatPuntsCombo.getSelectedItem());
    }

    /**
     * Retorna el mètode de solució seleccionat.
     *
     * @return Mètode seleccionat al ComboBox.
     */
    protected Metode getSolucio() {
        return (Metode) this.solucioCombo.getSelectedItem();
    }
}
