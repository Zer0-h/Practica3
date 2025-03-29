package vista;

import java.awt.Color;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import model.Distribution;
import model.Method;

public class TopPanel extends JPanel {

    private final Vista vista;

    private JComboBox<Distribution> distribution;
    private JComboBox<String> proximity;
    private JComboBox<Method> solution;
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

        this.distribution = new JComboBox<>(model.Distribution.values());
        this.add(distribution);

        // Proximitat
        JLabel proximityLabel = new JLabel("Proximitat:");
        this.add(proximityLabel);

        this.proximity = new JComboBox<>(new String[]{"Cerca", "Lejos"});
        this.add(proximity);

        // Quantitat de Punts
        JLabel quantityPointsLabel = new JLabel("Punts:");
        this.add(quantityPointsLabel);

        this.quantityPoints = new JComboBox<>(new String[]{"1000", "10000", "100000", "1000000", "2500000", "5000000", "7500000", "10000000"});
        this.add(quantityPoints);

        // Solució
        JLabel solutionLabel = new JLabel("Solució:");
        this.add(solutionLabel);

        this.solution = new JComboBox<>(model.Method.values());
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

    // GETTERS
    protected Distribution getDistribution() {
        return (Distribution) this.distribution.getSelectedItem();
    }

    protected String getProximity() {
        return (String) this.proximity.getSelectedItem();
    }

    protected int getQuantityPoints() {
        return Integer.parseInt((String) this.quantityPoints.getSelectedItem());
    }

    protected Method getSolution() {
        return (Method) this.solution.getSelectedItem();
    }
}
