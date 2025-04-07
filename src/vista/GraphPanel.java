package vista;

import java.awt.*;
import java.awt.geom.Point2D;
import javax.swing.*;

/**
 * Panell gràfic que representa els punts generats i la línia que connecta la
 * millor solució trobada.
 * Es mostra com una àrea de dibuix dins de la interfície gràfica.
 *
 * @author tonitorres
 */
public class GraphPanel extends JPanel {

    private Point2D.Double[] punts;        // Conjunt de punts generats
    private Point2D.Double[] puntsSolucio; // Parella de punts que formen la millor solució

    /**
     * Constructor que inicialitza el panell gràfic amb les dimensions
     * especificades.
     *
     * @param amplada Amplada del panell gràfic.
     * @param altura  Altura del panell gràfic.
     */
    public GraphPanel(int amplada, int altura) {
        setPreferredSize(new Dimension(amplada, altura));
        setBackground(Color.WHITE);
    }

    /**
     * Mètode que s'executa automàticament per pintar els components gràfics
     * dins el panell.
     *
     * @param g L'objecte gràfic utilitzat per dibuixar.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Si no hi ha punts generats, no dibuixar res
        if (punts == null) {
            return;
        }

        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.BLACK); // Color per als punts

        // Dibuixar tots els punts generats
        for (Point2D.Double p : punts) {
            g2d.fillOval((int) p.getX(), (int) p.getY(), 2, 2);
        }

        // Dibuixar la línia de la millor solució trobada (si existeix)
        if (puntsSolucio != null && puntsSolucio[0] != null && puntsSolucio[1] != null) {
            g2d.setColor(Color.RED); // Color per a la línia de solució
            g2d.drawLine((int) puntsSolucio[0].getX(), (int) puntsSolucio[0].getY(),
                    (int) puntsSolucio[1].getX(), (int) puntsSolucio[1].getY());
        }
    }

    /**
     * Mètode per col·locar el conjunt de punts que es representaran al panell.
     *
     * @param punts Array de punts generats.
     */
    public void colocaPunts(Point2D.Double[] punts) {
        this.punts = punts;
        this.puntsSolucio = null; // Inicialitza sense solució
        repaint(); // Actualitza el panell
    }

    /**
     * Dibuixa la línia que uneix la millor solució trobada.
     *
     * @param millorSolucio Array amb els dos punts que formen la millor
     *                      solució.
     */
    public void dibuixaLineaSolucio(Point2D.Double[] millorSolucio) {
        this.puntsSolucio = millorSolucio;
        repaint(); // Actualitza el panell amb la línia de solució
    }
}
