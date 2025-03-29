package vista;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.awt.geom.Point2D;

public class BottomPanel extends JPanel {

    private final JLabel timeLabel;
    private final JTextArea resultArea;

    public BottomPanel(Vista vista) {
        setLayout(new BorderLayout());
        timeLabel = new JLabel("Temps: ");
        add(timeLabel, BorderLayout.NORTH);

        resultArea = new JTextArea(5, 30);
        resultArea.setEditable(false);
        add(new JScrollPane(resultArea), BorderLayout.CENTER);
    }

    public void setTime(double seconds) {
        timeLabel.setText(String.format("Temps %.2f s", seconds));
    }

    public void displayBestResult(Point2D.Double[] sol, Double dist) {
        DecimalFormat df = new DecimalFormat("#.##########");
        resultArea.setText("Millor Solució:\n");
        resultArea.append("x1: " + df.format(sol[0].getX()) + " | y1: " + df.format(sol[0].getY()) + "\n");
        resultArea.append("x2: " + df.format(sol[1].getX()) + " | y2: " + df.format(sol[1].getY()) + "\n");
        resultArea.append("Distància: " + df.format(dist) + "\n");
    }
}
