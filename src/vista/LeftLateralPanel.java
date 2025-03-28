package vista;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import model.Distribution;
import model.Method;

public class LeftLateralPanel extends JPanel {

    private Vista vista;

    private JComboBox<Distribution> distribution;
    private JComboBox<String> proximity;
    private JComboBox<Method> method;
    private JComboBox<String> quantityPoints;
    private JButton generateB;

    public LeftLateralPanel(Vista v) {
        this.vista = v;
        init();
    }

    private void init() {
        setLayout(null);
        int width = vista.MARGENLAT - 20;
        int height = vista.getHeight() - vista.MARGENVER - 40;
        setBounds(10, vista.MARGENVER, width, height);
        setBackground(new Color(200, 220, 255));
        setBorder(new LineBorder(Color.BLACK, 2));

        JLabel distributionLabel = new JLabel("Distribució de punts:");
        distributionLabel.setBounds(10, 10, width - 20, 30);
        add(distributionLabel);

        distribution = new JComboBox<>(Distribution.values());
        distribution.setBounds(10, 40, width - 20, 30);
        add(distribution);

        JLabel proximityLabel = new JLabel("Proximitat (Cerca/Lejos):");
        proximityLabel.setBounds(10, 80, width - 20, 30);
        add(proximityLabel);

        proximity = new JComboBox<>(new String[]{"Cerca", "Lejos"});
        proximity.setBounds(10, 110, width - 20, 30);
        add(proximity);

        JLabel methodLabel = new JLabel("Mètode de càlcul:");
        methodLabel.setBounds(10, 150, width - 20, 30);
        add(methodLabel);

        method = new JComboBox<>(Method.values());
        method.setBounds(10, 180, width - 20, 30);
        add(method);

        JLabel quantityPointsLabel = new JLabel("Nombre de punts:");
        quantityPointsLabel.setBounds(10, 220, width - 20, 30);
        add(quantityPointsLabel);

        quantityPoints = new JComboBox<>(new String[]{"1000", "10000", "100000", "1000000", "2500000", "5000000"});
        quantityPoints.setBounds(10, 250, width - 20, 30);
        add(quantityPoints);

        generateB = new JButton("Generar punts");
        generateB.setBounds(10, height - 100, width - 20, 40);
        add(generateB);

        generateB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                vista.generatePointsClicked();
            }
        });
    }

    public Distribution getDistribution() {
        return (Distribution) distribution.getSelectedItem();
    }

    public String getProximity() {
        return (String) proximity.getSelectedItem();
    }

    public int getQuantityPoints() {
        return Integer.parseInt((String) quantityPoints.getSelectedItem());
    }

    public Method getMethod() {
        return (Method) method.getSelectedItem();
    }
}
