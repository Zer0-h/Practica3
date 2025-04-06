package vista;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;

/**
 * @author tonitorres
 */
public class BottomPanel extends JPanel {

    private final JLabel timeLabel;
    private final JTextArea textArea;
    private final JProgressBar progressBar;
    private double tempsEstimat;

    public BottomPanel() {
        setLayout(new BorderLayout());

        tempsEstimat = 0.0;

        // Temps d'execució
        timeLabel = new JLabel("Temps estimat: -- | Temps resultat: --");
        add(timeLabel, BorderLayout.NORTH);

        // Àrea de resultats
        textArea = new JTextArea(5, 30);
        textArea.setEditable(false);
        add(new JScrollPane(textArea), BorderLayout.CENTER);

        // Barra de progrés
        progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.setVisible(false);
        add(progressBar, BorderLayout.SOUTH);
    }

    public void setTempsEstimat(double t) {
        tempsEstimat = t;
        actualitzaEtiquetaTemps(null);
    }

    public void setTempsReal(double tempsReal) {
        actualitzaEtiquetaTemps(tempsReal);
    }

    private void actualitzaEtiquetaTemps(Double tempsReal) {
        timeLabel.setText(
                tempsReal == null
                        ? String.format("Temps Estimat: %.2f s | Temps Real: -- s", tempsEstimat)
                        : String.format("Temps Estimat: %.2f s | Temps Real: %.2f s", tempsEstimat, tempsReal)
        );
    }

    public void displaySolution(Point2D.Double[] sol) {
        if (sol != null && sol[0] != null && sol[1] != null) {
            textArea.setText(String.format("Solució:\nPunt 1: (%f, %f)\nPunt 2: (%f, %f)\nDistància: %f", sol[0].getX(), sol[0].getY(), sol[1].getX(), sol[1].getY(), sol[0].distance(sol[1])));
        }
    }

    public void startProgress() {
        progressBar.setVisible(true);
    }

    public void stopProgress() {
        progressBar.setVisible(false);
    }
}
