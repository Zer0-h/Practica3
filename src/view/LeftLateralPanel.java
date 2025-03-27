package view;

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

/**
 * Panel lateral izquierdo de la ventana principal.
 */
public class LeftLateralPanel extends JPanel {

    private View vista;

    private JComboBox distribution;
    private JComboBox proximity;
    private JComboBox solution;
    private JComboBox quantityPoints;

    private JButton generateB;

    private int x, y, width, height;

    /**
     * Panel Lateral izquierdo encargado de la configuración del algoritmo y los
     * datos de la aplicación
     */
    public LeftLateralPanel(View v) {
        this.vista = v;
        this.init();
    }

    /**
     * Método encargado de la inicialización del JPanel y todos los componentes
     * que lo componen (JLabels, JComboBoxs y otros JPanels)
     */
    private void init() {
        this.setLayout(null);

        this.x = 10;
        this.y = this.vista.MARGENVER;
        this.width = this.vista.MARGENLAT - 20;
        this.height = this.vista.getHeight() - this.vista.MARGENVER - 40;

        this.setBounds(x, y, width, height);
        this.setBackground(Color.CYAN);

        this.setBorder(new LineBorder(Color.BLACK, 2));

        // TIPO DE DISTRIBUCIÓN
        JLabel distributionLabel = new JLabel("Tipo de Distribución:");
        distributionLabel.setLayout(null);
        distributionLabel.setBounds(10, 10, width - 20, 30);
        this.add(distributionLabel);

        this.distribution = new JComboBox<>(model.Distribution.values());
        this.distribution.setLayout(null);
        this.distribution.setBounds(10, 40,
                width - 20, 30);
        this.add(distribution);

        // TIPO DE PROXiMIDAD (Parejas Lejanas o Cercanas)
        JLabel proximityLabel = new JLabel("Proximidad:");
        proximityLabel.setLayout(null);
        proximityLabel.setBounds(10, 80, width - 20, 30);
        this.add(proximityLabel);

        this.proximity = new JComboBox<>(new String[]{"Cerca", "Lejos"});
        this.proximity.setLayout(null);
        this.proximity.setBounds(10, 110, width - 20, 30);
        this.add(proximity);

        // TIPO DE APROXIMACIÓN A LA SOLUCION
        JLabel solutionLabel = new JLabel("Tipo de Solución:");
        solutionLabel.setLayout(null);
        solutionLabel.setBounds(10, 200, width - 20, 30);
        this.add(solutionLabel);

        this.solution = new JComboBox<>(model.Method.values());
        this.solution.setLayout(null);
        this.solution.setBounds(10, 230, width - 20, 30);
        this.add(solution);

        // CANTIDAD DE PUNTOS (N)
        JLabel quantityPointsLabel = new JLabel("Cantidad de Puntos:");
        quantityPointsLabel.setLayout(null);
        quantityPointsLabel.setBounds(10, 270, width - 20, 30);
        this.add(quantityPointsLabel);

        this.quantityPoints = new JComboBox<>(new String[]{"1000", "10000", "100000", "1000000", "2500000", "5000000", "7500000", "10000000"});
        this.quantityPoints.setLayout(null);
        this.quantityPoints.setBounds(10, 300, width - 20, 30);
        this.add(quantityPoints);

        // GENERATE BUTTON
        this.generateB = new JButton("Generar Puntos");
        this.generateB.setLayout(null);
        this.generateB.setBounds(10, height - 100, width - 20, 90);
        this.add(generateB);

        generateB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                vista.generatePointsClicked();
            }

        });

        this.setVisible(true);

    }

    // GETTERS
    protected Distribution getDistribution() {
        return Distribution.valueOf(this.distribution.getSelectedItem().toString());
    }

    protected String getProximity() {
        return this.proximity.getSelectedItem().toString();
    }

    protected int getQuantityPoints() {
        return Integer.parseInt(this.quantityPoints.getSelectedItem().toString());
    }

    protected Method getSolution() {
        return Method.valueOf(this.solution.getSelectedItem().toString());
    }
}
