package vista;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class RightLateralPanel extends JPanel {

    private Vista vista;
    private int x, y, width, height;
    private JButton startB;
    public JPanel soluciones;
    private TimePanel timePanel;

    public RightLateralPanel(Vista v) {
        this.vista = v;
        init();
    }

    private void init() {
        this.setLayout(null);

        this.x = this.vista.getWidth() + 10 - this.vista.MARGENLAT;
        this.y = this.vista.MARGENVER;
        this.width = this.vista.MARGENLAT - 20;
        this.height = this.vista.getHeight() - this.vista.MARGENVER - 40;

        this.setBounds(x, y, width, height);
        this.setBackground(new Color(255, 200, 200));
        this.setBorder(new LineBorder(Color.BLACK, 2));

        JLabel timeLabel = new JLabel("Temps d'execució (ns):");
        timeLabel.setBounds(10, 10, width - 10, 30);
        this.add(timeLabel);

        timePanel = new TimePanel(10, 50, width - 20, 30);
        this.add(timePanel);

        startB = new JButton("Iniciar càlcul");
        startB.setBounds(10, height - 100, width - 20, 40);
        this.add(startB);

        JLabel resultLabel = new JLabel("Millor solució trobada:");
        resultLabel.setBounds(10, 100, width - 10, 30);
        this.add(resultLabel);

        soluciones = new JPanel();
        soluciones.setLayout(null);
        soluciones.setBounds(10, 130, width - 20, 140);
        this.add(soluciones);

        startB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                vista.startClicked();
            }
        });

        this.setVisible(true);
    }

    private class TimePanel extends JPanel {

        private JLabel timeLabel;

        private TimePanel(int x, int y, int width, int height) {
            this.setBounds(x, y, width, height);
            this.timeLabel = new JLabel("");
            this.add(timeLabel);
        }

        public void setTime(long nanoseconds) {
            this.timeLabel.setText(String.valueOf(nanoseconds));
        }
    }

    public void setTime(long nanoseconds) {
        this.timePanel.setTime(nanoseconds);
    }
}
