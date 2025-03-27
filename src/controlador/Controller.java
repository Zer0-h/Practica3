package controlador;

import java.util.Arrays;
import java.util.HashMap;
import model.Method;
import static model.Method.AVIDO;
import model.Model;
import model.Punt;
import vista.Vista;

public class Controller {

    private Model modelo;
    private Vista vista;

    private HashMap frecuencias;

    public Controller() {
    }

    public Controller(Model modelo, Vista vista) {
        this.modelo = modelo;
        this.vista = vista;
    }

    public void start() {
        Punt[] puntos = modelo.getPuntos();
        Method metodo = modelo.getMetodo();
        boolean minimizar = modelo.isMinimizar();

        // Inicializamos el array de soluciones
        modelo.initSoluciones();

        long tiempoI = System.nanoTime();
        switch (metodo) {
            case FUERZA_BRUTA ->
                trobarParellaCurtaClasic(puntos);
            case DIVIDE_Y_VENCERAS -> {
                trobarParellaDivideAndConquer(puntos, minimizar);
                System.out.println(frecuencias);
            }
            case AVIDO ->
                encontrarParejasLejanasAvido(puntos);
            default ->
                throw new AssertionError();
        }
        this.vista.setTime(System.nanoTime() - tiempoI);
        this.vista.setBestResult();
        this.vista.paintGraph();
    }

    private void trobarParellaCurtaClasic(Punt[] puntos) {
        // Buscamos las distancias de todos los puntos entre ellos
        for (int i = 0; i < puntos.length; i++) {
            for (int j = i + 1; j < puntos.length; j++) {
                // Comprobamos si es solución y la guardamos
                Punt[] posibleSolucion = {puntos[i], puntos[j]};
                modelo.pushSolucion(posibleSolucion);
            }
        }

    }

    private double trobarParellaDivideAndConquer(Punt[] puntos, boolean minimizar) {
        frecuencias = new HashMap();
        Arrays.sort(puntos);
        return trobarParellaDivideAndConquer(puntos, 0, puntos.length - 1, minimizar);
    }

    private double trobarParellaDivideAndConquer(Punt[] puntos, int left, int right, boolean minimizar) {
        if (right - left == 2) {
            return Punt.distancia(puntos[left], puntos[right]);
        } else if (right - left < 2) {
            if (minimizar) {
                return Double.MAX_VALUE;
            } else {
                return Double.MIN_VALUE;
            }
        } else if (right - left == 3) {
            Double d1 = Punt.distancia(puntos[left], puntos[left + 1]);
            Double d2 = Punt.distancia(puntos[left + 1], puntos[right]);
            Double d3 = Punt.distancia(puntos[left], puntos[right]);
            if (minimizar) {
                return Math.min(Math.min(d1, d2), d3);
            } else {
                return Math.max(Math.max(d1, d2), d3);
            }
        } else {
            // Partimos los datos por la mitad
            int middle = (right + left) / 2;
            double distanceLeft = trobarParellaDivideAndConquer(puntos, left, middle - 1, minimizar);
            double distanceRight = trobarParellaDivideAndConquer(puntos, middle, right, minimizar);

            // Obtenemos la mínima/máxima distancia entre los puntos del array
            // izquierdo y los puntos del array derecho
            double distanciaPorcion = Math.max(distanceLeft, distanceRight);

            // Guardamos los puntos que caen en la porción de ancho 2 veces el
            // máximo de la distancia izquierda o derecha con el centro en middle
            Punt[] porcion = new Punt[right - left + 1];
            int k = 0;
            for (int i = left; i <= right; i++) {
                if (Math.abs(puntos[i].getX() - puntos[middle].getX()) < distanciaPorcion) {
                    porcion[k++] = puntos[i];
                }
            }


            // Guardamos la frecuencia de aparición de cada tamaño de porción
            if (frecuencias.containsKey(k)) {
                int frecuenciaActual = (int)frecuencias.get(k);
                frecuencias.put(k, frecuenciaActual + 1);
            } else {
                frecuencias.put(k, 1);
            }


            // Ordenamos la porción por la coordenada y
            Arrays.sort(porcion, 0, k, (a, b)
                    -> Double.compare(a.getY(), b.getY()));

            // Buscamos los puntos mas cercanos/lejanos en la sección con fuerza bruta
            for (int i = 0; i < k; i++) {
                for (int j = i + 1; j < k && porcion[j].getY() - porcion[i].getY() < distanciaPorcion; j++) {
                    double distanceAux = Punt.distancia(porcion[i], porcion[j]);
                    if (distanceAux < distanciaPorcion && minimizar) {
                        distanciaPorcion = distanceAux;
                    } else if (distanceAux > distanciaPorcion && !minimizar) {
                        distanciaPorcion = distanceAux;
                    }
                    // Comprobamos si es solución y la guardamos
                    Punt[] posibleSolucion = {porcion[i], porcion[j]};
                    modelo.pushSolucion(posibleSolucion);
                }
            }
            return distanciaPorcion;
        }
    }

    private void encontrarParejasLejanasAvido(Punt[] puntos){
        double width = (double) vista.getGraphWidth();
        double height = (double) vista.getGraphHeight();

        for(Punt p: puntos){
            p.setDistanciaPivote(new Punt (width/2, height/2));
        }

        Arrays.sort(puntos, (a, b)
                -> Double.compare(b.getDistanciaPivote() ,a.getDistanciaPivote()));

        puntos = Arrays.copyOf(puntos, (int) Math.sqrt(puntos.length));
        System.out.println("Puntos a comprobar: " + puntos.length);

        for(Punt p1: puntos){
            for (Punt p2: puntos){
                if(p1 != p2){
                    Punt[] posibleSolucion = {p1, p2};
                    modelo.pushSolucion(posibleSolucion);
                }
            }
        }

    }

    public Model getModelo() {
        return modelo;
    }

    public void setModelo(Model modelo) {
        this.modelo = modelo;
    }

    public Vista getVista() {
        return vista;
    }

    public void setVista(Vista vista) {
        this.vista = vista;
    }

}
