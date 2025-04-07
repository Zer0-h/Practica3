package vista;

import java.awt.*;
import java.awt.geom.Point2D;
import javax.swing.*;

/**
 * Panell inferior de la interfície gràfica que mostra el temps estimat i el
 * temps d'execució real,
 * així com els resultats de la solució i una barra de progrés.
 *
 * @autor tonitorres
 */
public class BottomPanel extends JPanel {

    private final JLabel timeLabel;       // Etiqueta per mostrar el temps d'execució
    private final JTextArea textArea;     // Àrea de text per mostrar la solució
    private final JProgressBar progressBar; // Barra de progrés per indicar el càlcul
    private double tempsEstimat;          // Temps estimat d'execució

    /**
     * Constructor que inicialitza el panell inferior amb la disposició i els
     * components necessaris.
     */
    public BottomPanel() {
        setLayout(new BorderLayout());

        tempsEstimat = 0.0;

        // Inicialització de l'etiqueta de temps d'execució
        timeLabel = new JLabel("Temps estimat: -- | Temps resultat: --");
        add(timeLabel, BorderLayout.NORTH);

        // Inicialització de l'àrea de text per mostrar la solució
        textArea = new JTextArea(5, 30);
        textArea.setEditable(false);
        add(new JScrollPane(textArea), BorderLayout.CENTER);

        // Inicialització de la barra de progrés
        progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);  // La barra es mou mentre es calcula
        progressBar.setVisible(false);       // No visible per defecte
        add(progressBar, BorderLayout.SOUTH);
    }

    /**
     * Actualitza el temps estimat d'execució i refresca l'etiqueta de temps.
     *
     * @param t Temps estimat en segons.
     */
    public void setTempsEstimat(double t) {
        tempsEstimat = t;
        actualitzaEtiquetaTemps(null);
    }

    /**
     * Actualitza el temps real d'execució i refresca l'etiqueta de temps.
     *
     * @param tempsReal Temps d'execució real en segons.
     */
    public void setTempsReal(double tempsReal) {
        actualitzaEtiquetaTemps(tempsReal);
    }

    /**
     * Actualitza l'etiqueta que mostra el temps estimat i el temps real.
     *
     * @param tempsReal Temps real d'execució (pot ser null si encara no està
     *                  disponible).
     */
    private void actualitzaEtiquetaTemps(Double tempsReal) {
        String text = (tempsReal == null)
                ? String.format("Temps Estimat: %.2f s | Temps Real: -- s", tempsEstimat)
                : String.format("Temps Estimat: %.2f s | Temps Real: %.2f s", tempsEstimat, tempsReal);
        timeLabel.setText(text);
    }

    /**
     * Mostra la solució calculada a l'àrea de text.
     *
     * @param sol Array amb dos punts que formen la millor solució trobada.
     */
    public void displaySolution(Point2D.Double[] sol) {
        if (sol != null && sol[0] != null && sol[1] != null) {
            String resultat = String.format(
                    "Solució:\nPunt 1: (%.2f, %.2f)\nPunt 2: (%.2f, %.2f)\nDistància: %.4f",
                    sol[0].getX(), sol[0].getY(), sol[1].getX(), sol[1].getY(), sol[0].distance(sol[1])
            );
            textArea.setText(resultat);
        } else {
            textArea.setText("No hi ha solució disponible.");
        }
    }

    /**
     * Inicia la visualització de la barra de progrés quan comença un càlcul.
     */
    public void startProgress() {
        progressBar.setVisible(true);
    }

    /**
     * Atura la visualització de la barra de progrés quan acaba un càlcul.
     */
    public void stopProgress() {
        progressBar.setVisible(false);
    }
}
