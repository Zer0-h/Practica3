package vista;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import model.Punt;
public class PanelPunts extends JPanel {

    private Vista vista;

    public PanelPunts(Vista v, int width, int height) {
        vista = v;
        Border borde = new LineBorder(Color.BLACK, 2);
        setBorder(borde);
        setLayout(null);
        setBounds(vista.MARGENLAT, vista.MARGENVER,
                width, height);
        setBackground(Color.WHITE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (vista.getModelo().getPuntos() == null) {
            return;
        }

        Graphics2D g2d = (Graphics2D) g;

        Punt[] puntos = vista.getModelo().getPuntos();
        Punt[] mejor = vista.getModelo().getMejorSolucion();

        g2d.setColor(Color.RED);
        for (Punt p : puntos) {
            g2d.drawLine((int) p.getX(), (int) p.getY(), (int) p.getX(), (int) p.getY());
        }

        if (mejor != null) {
            g2d.setColor(Color.BLUE);
            int x1 = (int) mejor[0].getX();
            int y1 = (int) mejor[0].getY();
            int x2 = (int) mejor[1].getX();
            int y2 = (int) mejor[1].getY();
            g2d.drawLine(x1, y1, x2, y2);
        }
    }
}
