package vista;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;

/**
 * @author tonitorres
 */
public class GraphPanel extends JPanel {

    private Point2D.Double[] puntos;
    private Point2D.Double[] puntsSolucio;

    public GraphPanel(int width, int height) {
        setPreferredSize(new Dimension(width, height));
        setBackground(Color.WHITE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (puntos == null) {
            return;
        }

        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.BLACK);

        for (Point2D.Double p : puntos) {
            g2d.fillOval((int) p.getX(), (int) p.getY(), 2, 2);
        }

        if (puntsSolucio != null) {
            g2d.setColor(Color.RED);
            g2d.drawLine((int) puntsSolucio[0].getX(), (int) puntsSolucio[0].getY(),
                         (int) puntsSolucio[1].getX(), (int) puntsSolucio[1].getY());
        }
    }

    public void colocaPunts(Point2D.Double[] puntos) {
        this.puntos = puntos;
        this.puntsSolucio = null;
        repaint();
    }

    public void dibuixaLineaSolucio(Point2D.Double[] millorSolucio) {
        this.puntsSolucio = millorSolucio;
        repaint();
    }
}
