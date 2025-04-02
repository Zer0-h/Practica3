package model;

import controlador.Controlador;
import java.awt.geom.Point2D;

public class BruteForceProcess extends AbstractCalculProcess {

    public BruteForceProcess(Controlador controlador) {
        super(controlador);
    }

    public BruteForceProcess(Controlador controlador, Point2D.Double[] punts) {
        super(controlador, punts);
    }

    @Override
    protected void calcular() {
        for (int i = 0; i < punts.length; i++) {
            for (int j = i + 1; j < punts.length; j++) {
                model.setSolucioSiEs(punts[i], punts[j]);
            }
        }
    }

    @Override
    protected Metode getMetode() {
        return Metode.FUERZA_BRUTA;
    }
}
