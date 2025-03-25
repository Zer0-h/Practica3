package vista;

import controlador.Controlador;
import controlador.Notificacio;
import java.awt.*;
import javax.swing.*;
import model.Model;

/**
 * Panell gràfic que representa el tauler de joc per al problema del Tromino.
 * Gestiona la interacció de l'usuari mitjançant clics i redibuixa el tauler
 * segons l'estat del model.
 *
 * @author tonitorres
 */
public class TaulerPanel extends JPanel {

    private final Controlador controlador;
    private final Model model;

    /**
     * Constructor que inicialitza el panell i afegeix un listener per gestionar
     * els clics de l'usuari.
     *
     * @param c Instància principal de l'aplicació
     */
    public TaulerPanel(Controlador c) {
        this.controlador = c;
        this.model = controlador.getModel();
        setPreferredSize(new Dimension(768, 768));

        // Activa el doble buffering per a un dibuix més suau
        setDoubleBuffered(true);

        // Afegeix un listener per capturar clics de l'usuari al tauler
        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                // Evita la interacció si l'algorisme està en execució o ja està resolt
                if (model.getEnExecucio() || model.getResolt()) {
                    return;
                }

                int midaPanel = Math.min(getWidth(), getHeight());
                int midaCasella = midaPanel / model.getMidaTauler();

                int fila = e.getY() / midaCasella;
                int columna = e.getX() / midaCasella;

                if (!esClicValid(fila, columna)) {
                    return;
                }

                if (model.hiHaForatSeleccionat()) {
                    model.netejarForat();
                }

                model.assignarForat(fila, columna);
                controlador.notificar(Notificacio.SELECCIONA_FORAT);
                repaint();
            }
        });
    }

    /**
     * Verifica si el clic està dins dels límits vàlids del tauler.
     *
     * @param fila    Fila on s'ha fet clic
     * @param columna Columna on s'ha fet clic
     *
     * @return Cert si el clic és vàlid, fals en cas contrari
     */
    private boolean esClicValid(int fila, int columna) {
        return fila < model.getMidaTauler() && columna < model.getMidaTauler();
    }

    /**
     * Força la repintada del panell.
     */
    public void pintar() {
        repaint();
    }

    /**
     * Dibuixa el tauler i el seu contingut.
     *
     * @param g Objecte Graphics per dibuixar el contingut
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int midaPanel = Math.min(getWidth(), getHeight());
        int midaCasella = midaPanel / model.getMidaTauler();

        dibuixarQuadrícula(g2, midaCasella);
        omplirCaselles(g2, midaCasella);
        dibuixarBordesTrominos(g2, midaCasella);
    }

    /**
     * Dibuixa la quadrícula del tauler.
     *
     * @param g2          Objecte Graphics2D per dibuixar
     * @param midaCasella Mida de cada casella en píxels
     */
    private void dibuixarQuadrícula(Graphics2D g2, int midaCasella) {
        g2.setColor(Color.BLACK);
        int midaTauler = model.getMidaTauler();

        for (int i = 0; i <= midaTauler; i++) {
            g2.drawLine(0, i * midaCasella, midaTauler * midaCasella, i * midaCasella);
            g2.drawLine(i * midaCasella, 0, i * midaCasella, midaTauler * midaCasella);
        }
    }

    /**
     * Omple les caselles del tauler amb els colors corresponents als trominos i
     * als forats.
     *
     * @param g2          Objecte Graphics2D per dibuixar
     * @param midaCasella Mida de cada casella en píxels
     */
    private void omplirCaselles(Graphics2D g2, int midaCasella) {
        int midaTauler = model.getMidaTauler();

        for (int fila = 0; fila < midaTauler; fila++) {
            for (int columna = 0; columna < midaTauler; columna++) {
                int x = columna * midaCasella;
                int y = fila * midaCasella;

                if (model.esCasellaForat(fila, columna)) {
                    g2.setColor(Color.BLACK);
                } else if (model.esCasellaTromino(fila, columna)) {
                    g2.setColor(model.getColorPerTromino(fila, columna));
                } else {
                    continue;
                }

                g2.fillRect(x, y, midaCasella, midaCasella);
            }
        }
    }

    /**
     * Dibuixa els bordes dels trominos per delimitar-ne les peces.
     *
     * @param g2          Objecte Graphics2D per dibuixar
     * @param midaCasella Mida de cada casella en píxels
     */
    private void dibuixarBordesTrominos(Graphics2D g2, int midaCasella) {
        g2.setColor(Color.BLACK);
        int midaTauler = model.getMidaTauler();

        for (int fila = 0; fila < midaTauler; fila++) {
            for (int columna = 0; columna < midaTauler; columna++) {
                if (model.esCasellaTromino(fila, columna)) {
                    int x = columna * midaCasella;
                    int y = fila * midaCasella;

                    if (model.esVoraSuperiorTromino(fila, columna)) {
                        g2.drawLine(x, y, x + midaCasella, y);
                    }
                    if (model.esVoraEsquerraTromino(fila, columna)) {
                        g2.drawLine(x, y, x, y + midaCasella);
                    }
                    if (model.esVoraInferiorTromino(fila, columna)) {
                        g2.drawLine(x, y + midaCasella, x + midaCasella, y + midaCasella);
                    }
                    if (model.esVoraDretaTromino(fila, columna)) {
                        g2.drawLine(x + midaCasella, y, x + midaCasella, y + midaCasella);
                    }
                }
            }
        }
    }
}
