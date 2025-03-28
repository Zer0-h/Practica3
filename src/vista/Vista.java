package vista;

import model.Model;
import controlador.Controller;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;
import model.Distribution;
import model.Method;

public class Vista extends JFrame {

    private Controller controlador;
    private Model modelo;

    protected final int MARGENLAT = 300;
    protected final int MARGENVER = 20;

    private int GraphWidth;
    private int GraphHeight;

    private LeftLateralPanel leftPanel;
    private RightLateralPanel rightPanel;
    private PanelPunts graphPanel;

    public Vista() {}

    public Vista(Controller controlador, Model modelo) {
        this.controlador = controlador;
        this.modelo = modelo;
    }

    public void mostrar() {
        setTitle("Algorismes Avançats - Pràctica 3");
        setLayout(null);
        setResizable(false);

        this.GraphWidth = 800;
        this.GraphHeight = 800;

        setSize(this.GraphWidth + this.MARGENLAT * 2, this.GraphHeight + this.MARGENVER + 40);

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);

        graphPanel = new PanelPunts(this, GraphWidth, GraphHeight);
        add(graphPanel);

        leftPanel = new LeftLateralPanel(this);
        add(leftPanel);

        rightPanel = new RightLateralPanel(this);
        add(rightPanel);

        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        graphPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                BufferedImage captura = getCaptura(graphPanel, e.getX() - 20, e.getY() - 20, e.getX() + 20, e.getY() + 20);
                Image capturaEscalada = captura.getScaledInstance(captura.getWidth() * 10, captura.getHeight() * 10, Image.SCALE_SMOOTH);
                JLabel capturaLabel = new JLabel(new ImageIcon(capturaEscalada));
                JPanel panelCaptura = new JPanel();
                panelCaptura.setLayout(null);
                panelCaptura.setPreferredSize(new Dimension(capturaEscalada.getWidth(null), capturaEscalada.getHeight(null)));
                panelCaptura.add(capturaLabel, BorderLayout.CENTER);
                JDialog dialogCaptura = new JDialog();
                dialogCaptura.setTitle("Zoom");
                dialogCaptura.setBounds(graphPanel.getX() + 244, graphPanel.getY(), 500, 500);
                dialogCaptura.setResizable(false);
                dialogCaptura.add(capturaLabel);
                dialogCaptura.setVisible(true);
            }
        });
    }

    private BufferedImage getCaptura(JPanel panel, int x1, int y1, int x2, int y2) {
        int width = x2 - x1;
        int height = y2 - y1;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();
        panel.paint(graphics.create(-x1, -y1, panel.getWidth(), panel.getHeight()));
        graphics.dispose();
        return image;
    }

    protected void startClicked() {
        if (this.modelo.exists()) {
            modelo.setMinimizar(leftPanel.getProximity().equals("Cerca"));
            modelo.setMetodo(leftPanel.getMethod());
            controlador.start();
        }
    }

    protected void generatePointsClicked() {
        Distribution distribution = leftPanel.getDistribution();
        int n = leftPanel.getQuantityPoints();
        modelo.reset(distribution, n);
        paintGraph();
    }

    public void paintGraph() {
        this.graphPanel.repaint();
    }

    public void setTime(long nanoseconds) {
        rightPanel.setTime(nanoseconds);
    }

    public void setBestResult() {
        rightPanel.soluciones.removeAll();
        Point2D.Double[] sol = modelo.getMejorSolucion();
        Double dist = modelo.getMejorDistancia();

        if (sol == null || dist == null) return;

        DecimalFormat df = new DecimalFormat("#.#####");
        Font font = new Font("Arial Black", Font.PLAIN, 12);

        JLabel pntL = new JLabel("Millor solució:");
        pntL.setFont(font);
        pntL.setBounds(10, 6, rightPanel.soluciones.getWidth() - 20, 12);

        JLabel labelX1 = new JLabel("x1: " + df.format(sol[0].getX()));
        labelX1.setBounds(10, 18, rightPanel.soluciones.getWidth() - 20, 12);
        JLabel labelY1 = new JLabel("y1: " + df.format(sol[0].getY()));
        labelY1.setBounds(10, 30, rightPanel.soluciones.getWidth() - 20, 12);

        JLabel labelX2 = new JLabel("x2: " + df.format(sol[1].getX()));
        labelX2.setBounds(10, 42, rightPanel.soluciones.getWidth() - 20, 12);
        JLabel labelY2 = new JLabel("y2: " + df.format(sol[1].getY()));
        labelY2.setBounds(10, 54, rightPanel.soluciones.getWidth() - 20, 12);

        JLabel distanciaLabel = new JLabel("Distància: " + df.format(dist));
        distanciaLabel.setBounds(10, 66, rightPanel.soluciones.getWidth() - 20, 12);

        rightPanel.soluciones.add(pntL);
        rightPanel.soluciones.add(labelX1);
        rightPanel.soluciones.add(labelY1);
        rightPanel.soluciones.add(labelX2);
        rightPanel.soluciones.add(labelY2);
        rightPanel.soluciones.add(distanciaLabel);

        rightPanel.repaint();
    }

    public Controller getControlador() {
        return controlador;
    }

    public void setControlador(Controller controlador) {
        this.controlador = controlador;
    }

    public Model getModelo() {
        return modelo;
    }

    public void setModelo(Model modelo) {
        this.modelo = modelo;
    }

    public int getGraphWidth() {
        return GraphWidth;
    }

    public int getGraphHeight() {
        return GraphHeight;
    }
}
