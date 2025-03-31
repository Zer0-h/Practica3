package vista;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;

public class BottomPanel extends JPanel {

    private final JLabel timeLabel;
    private final JTextArea resultArea;
    private final JProgressBar progressBar;

    public BottomPanel() {
        setLayout(new BorderLayout());

        // Temps d'execució
        timeLabel = new JLabel("Temps: ");
        add(timeLabel, BorderLayout.NORTH);

        // Àrea de resultats
        resultArea = new JTextArea(5, 30);
        resultArea.setEditable(false);
        add(new JScrollPane(resultArea), BorderLayout.CENTER);

        // Barra de progrés
        progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.setVisible(false);
        add(progressBar, BorderLayout.SOUTH);
    }

    public void setTime(double seconds) {
        timeLabel.setText(String.format("Temps %.2f s", seconds));
    }

    public void displayBestResult(Point2D.Double[] sol) {
        resultArea.setText(String.format("Solució:\nPunt 1: (%f, %f)\nPunt 2: (%f, %f)\nDistància: %f", sol[0].getX(), sol[0].getY(), sol[1].getX(), sol[1].getY(), sol[0].distance(sol[1])));
    }

    public void startProgress() {
        progressBar.setVisible(true);
    }

    public void stopProgress() {
        progressBar.setVisible(false);
    }
}
