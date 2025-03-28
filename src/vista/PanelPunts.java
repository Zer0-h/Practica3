package vista;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class PanelPunts extends JPanel {

    private Vista vista;

    public PanelPunts(Vista v, int width, int height) {
        this.vista = v;
        setBorder(new LineBorder(Color.BLACK, 2));
        setLayout(null);
        setBounds(vista.MARGENLAT, vista.MARGENVER, width, height);
        setBackground(Color.WHITE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (vista.getModelo().getPuntos() == null) return;

        Graphics2D g2d = (Graphics2D) g;
        Point2D.Double[] puntos = vista.getModelo().getPuntos();
        Point2D.Double[] mejor = vista.getModelo().getMejorSolucion();

        g2d.setColor(Color.RED);
        for (Point2D.Double p : puntos) {
            g2d.fillOval((int)p.getX() - 2, (int)p.getY() - 2, 5, 5);
        }

        if (mejor != null) {
            g2d.setColor(Color.BLUE);
            g2d.drawLine((int) mejor[0].getX(), (int) mejor[0].getY(),
                         (int) mejor[1].getX(), (int) mejor[1].getY());
        }
    }
}
