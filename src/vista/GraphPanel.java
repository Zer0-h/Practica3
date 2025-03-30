package vista;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;

public class GraphPanel extends JPanel {

    private final Vista vista;

    public GraphPanel(Vista vista, int width, int height) {
        this.vista = vista;
        setPreferredSize(new Dimension(width, height));
        setBackground(Color.WHITE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (!vista.getModelo().tePunts()) {
            return;
        }

        Graphics2D g2d = (Graphics2D) g;
        Point2D.Double[] puntos = vista.getModelo().getPuntos();
        Point2D.Double[] mejor = vista.getModelo().getPuntsSolucio();

        g2d.setColor(Color.BLACK);

        for (Point2D.Double p : puntos) {
            g2d.fillOval((int) p.getX(), (int) p.getY(), 4, 4);
        }

        if (mejor != null) {
            g2d.setColor(Color.RED);
            g2d.drawLine((int) mejor[0].getX(), (int) mejor[0].getY(),
                         (int) mejor[1].getX(), (int) mejor[1].getY());
        }
    }
}
