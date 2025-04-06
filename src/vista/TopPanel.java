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
 * @author tonitorres
 */
public class TopPanel extends JPanel {

    private final Vista vista;

    private JComboBox<Distribucio> distribution;
    private JComboBox<Tipus> problema;
    private JComboBox<Metode> solution;
    private JComboBox<String> quantityPoints;

    private JButton generateB;
    private JButton startB;

    public TopPanel(Vista v) {
        this.vista = v;
        this.init();
    }

    private void init() {
        this.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        this.setBackground(Color.LIGHT_GRAY);

        // Distribució
        JLabel distributionLabel = new JLabel("Distribució:");
        this.add(distributionLabel);

        this.distribution = new JComboBox<>(Distribucio.values());
        this.add(distribution);

        // Proximitat
        JLabel problemaLabel = new JLabel("Problema:");
        this.add(problemaLabel);

        this.problema = new JComboBox<>(Tipus.values());
        this.add(problema);

        // Quantitat de Punts
        JLabel quantityPointsLabel = new JLabel("Punts:");
        this.add(quantityPointsLabel);

        this.quantityPoints = new JComboBox<>(new String[]{"1000", "10000", "100000", "1000000", "2500000", "5000000", "7500000", "10000000"});
        this.add(quantityPoints);

        // Solució
        JLabel solutionLabel = new JLabel("Solució:");
        this.add(solutionLabel);

        this.solution = new JComboBox<>(Metode.values());
        this.add(solution);

        // Botó per generar punts
        this.generateB = new JButton("Generar Punts");
        this.add(generateB);

        // Botó per iniciar càlcul
        this.startB = new JButton("Iniciar");
        this.add(startB);

        // Accions dels botons
        generateB.addActionListener(e -> vista.generatePointsClicked());
        startB.addActionListener(e -> vista.startClicked());
    }

    protected void toggleInProgress(boolean inProgress) {
        startB.setEnabled(inProgress);
        generateB.setEnabled(inProgress);
    }

    // GETTERS
    protected Distribucio getDistribution() {
        return (Distribucio) this.distribution.getSelectedItem();
    }

    protected Tipus getProximity() {
        return (Tipus) this.problema.getSelectedItem();
    }

    protected int getQuantityPoints() {
        return Integer.parseInt((String) this.quantityPoints.getSelectedItem());
    }

    protected Metode getSolution() {
        return (Metode) this.solution.getSelectedItem();
    }
}
