package vista;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import model.ComparativaResultat;

/**
 * Classe ComparativaVista: Mostra una finestra amb una taula comparativa dels
 * resultats dels diferents processos.
 * Inclou un botó per tancar la finestra.
 *
 * @autor tonitorres
 */
public class ComparativaVista extends JFrame {

    private final DefaultTableModel tableModel;
    private final JTable comparativaTable;

    public ComparativaVista() {
        setTitle("Comparativa de Mètodes");
        setSize(600, 400);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Crear el model de la taula amb columnes
        String[] columnNames = {"Mètode", "Temps (s)", "Punt 1", "Punt 2", "Distància"};
        tableModel = new DefaultTableModel(columnNames, 0);

        // Crear la taula i assignar-li el model
        comparativaTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(comparativaTable);

        // Botó per tancar la finestra
        JButton closeButton = new JButton("Tancar");
        closeButton.addActionListener(e -> dispose());

        // Afegir els components a la finestra
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(closeButton, BorderLayout.SOUTH);

        add(panel);
    }

    /**
     * Mètode per afegir un resultat nou a la taula.
     *
     * @param resultat Resultat del procés.
     */
    public void afegirResultat(ComparativaResultat resultat) {
        String punt1Text = String.format("(%.2f, %.2f)", resultat.getSolucio()[0].getX(), resultat.getSolucio()[1].getY());
        String punt2Text = String.format("(%.2f, %.2f)", resultat.getSolucio()[1].getX(), resultat.getSolucio()[1].getY());
        tableModel.addRow(new Object[]{resultat.getMetode(), String.format("%.4f", resultat.getTemps()), punt1Text, punt2Text, String.format("%.4f", resultat.getDistanciaSolucio())});
    }
}
