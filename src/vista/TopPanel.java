package vista;

import javax.swing.*;
import java.awt.*;
import model.Distribution;
import model.Method;

public class TopPanel extends JPanel {

    private Vista vista;
    private JComboBox<Distribution> distribution;
    private JComboBox<String> proximity;
    private JComboBox<Method> solution;
    private JComboBox<String> quantityPoints;
    private JButton generateB;

    public TopPanel(Vista vista) {
        this.vista = vista;
        setLayout(new FlowLayout());

        add(new JLabel("Distribució:"));
        distribution = new JComboBox<>(Distribution.values());
        add(distribution);

        add(new JLabel("Proximitat:"));
        proximity = new JComboBox<>(new String[]{"Cerca", "Lejos"});
        add(proximity);

        add(new JLabel("Solució:"));
        solution = new JComboBox<>(Method.values());
        add(solution);

        add(new JLabel("Punts:"));
        quantityPoints = new JComboBox<>(new String[]{"1000", "10000", "100000"});
        add(quantityPoints);

        generateB = new JButton("Generar Punts");
        generateB.addActionListener(e -> vista.generatePointsClicked());
        add(generateB);
    }

    protected Distribution getDistribution() {
        return (Distribution) distribution.getSelectedItem();
    }

    protected String getProximity() {
        return (String) proximity.getSelectedItem();
    }

    protected int getQuantityPoints() {
        return Integer.parseInt((String) quantityPoints.getSelectedItem());
    }

    protected Method getSolution() {
        return (Method) solution.getSelectedItem();
    }
}
