package controller;

import java.util.Arrays;
import java.util.HashMap;
import model.Method;
import static model.Method.AVIDO;
import model.Model;
import model.Punto;
import view.View;

public class Controller {

    private Model modelo;
    private View vista;

    private HashMap frecuencias;

    public Controller() {
    }

    public Controller(Model modelo, View vista) {
        this.modelo = modelo;
        this.vista = vista;
    }

    public void start() {
        Punto[] puntos = modelo.getPuntos();
        Method metodo = modelo.getMetodo();
        boolean minimizar = modelo.isMinimizar();

        // Inicializamos el array de soluciones
        modelo.initSoluciones();

        long tiempoI = System.nanoTime();
        switch (metodo) {
            case FUERZA_BRUTA ->
                encontrarParejasBF(puntos);
            case DIVIDE_Y_VENCERAS -> {
                encontrarParejasDC(puntos, minimizar);
                System.out.println(frecuencias);
            }
            case AVIDO ->
                encontrarParejasLejanasAvido(puntos);
            case HEURISTICO ->
                encontrarParejasLejanas(puntos);
            default ->
                throw new AssertionError();
        }
        this.vista.setTime(System.nanoTime() - tiempoI);
        this.vista.setBestResult();
        this.vista.paintGraph();

    }

    private void encontrarParejasBF(Punto[] puntos) {

        // Buscamos las distancias de todos los puntos entre ellos
        for (int i = 0; i < puntos.length; i++) {
            for (int j = i + 1; j < puntos.length; j++) {
                // Comprobamos si es solución y la guardamos
                Punto[] posibleSolucion = {puntos[i], puntos[j]};
                modelo.pushSolucion(posibleSolucion);
            }
        }

    }

    private double encontrarParejasDC(Punto[] puntos, boolean minimizar) {
        frecuencias = new HashMap();
        Arrays.sort(puntos);
        return encontrarParejasDC(puntos, 0, puntos.length - 1, minimizar);
    }

    private double encontrarParejasDC(Punto[] puntos, int left, int right, boolean minimizar) {
        if (right - left == 2) {
            return Punto.distancia(puntos[left], puntos[right]);
        } else if (right - left < 2) {
            if (minimizar) {
                return Double.MAX_VALUE;
            } else {
                return Double.MIN_VALUE;
            }
        } else if (right - left == 3) {
            Double d1 = Punto.distancia(puntos[left], puntos[left + 1]);
            Double d2 = Punto.distancia(puntos[left + 1], puntos[right]);
            Double d3 = Punto.distancia(puntos[left], puntos[right]);
            if (minimizar) {
                return Math.min(Math.min(d1, d2), d3);
            } else {
                return Math.max(Math.max(d1, d2), d3);
            }
        } else {
            // Partimos los datos por la mitad
            int middle = (right + left) / 2;
            double distanceLeft = encontrarParejasDC(puntos, left, middle - 1, minimizar);
            double distanceRight = encontrarParejasDC(puntos, middle, right, minimizar);

            // Obtenemos la mínima/máxima distancia entre los puntos del array
            // izquierdo y los puntos del array derecho
            double distanciaPorcion = Math.max(distanceLeft, distanceRight);

            // Guardamos los puntos que caen en la porción de ancho 2 veces el
            // máximo de la distancia izquierda o derecha con el centro en middle
            Punto[] porcion = new Punto[right - left + 1];
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
                    double distanceAux = Punto.distancia(porcion[i], porcion[j]);
                    if (distanceAux < distanciaPorcion && minimizar) {
                        distanciaPorcion = distanceAux;
                    } else if (distanceAux > distanciaPorcion && !minimizar) {
                        distanciaPorcion = distanceAux;
                    }
                    // Comprobamos si es solución y la guardamos
                    Punto[] posibleSolucion = {porcion[i], porcion[j]};
                    modelo.pushSolucion(posibleSolucion);
                }
            }
            return distanciaPorcion;
        }
    }

    private void encontrarParejasLejanasAvido(Punto[] puntos){
        double width = (double) vista.getGraphWidth();
        double height = (double) vista.getGraphHeight();

        for(Punto p: puntos){
            p.setDistanciaPivote(new Punto (width/2, height/2));
        }

        Arrays.sort(puntos, (a, b)
                -> Double.compare(b.getDistanciaPivote() ,a.getDistanciaPivote()));

        puntos = Arrays.copyOf(puntos, (int) Math.sqrt(puntos.length));
        System.out.println("Puntos a comprobar: " + puntos.length);

        for(Punto p1: puntos){
            for (Punto p2: puntos){
                if(p1 != p2){
                    Punto[] posibleSolucion = {p1, p2};
                    modelo.pushSolucion(posibleSolucion);
                }
            }
        }

    }

    private void encontrarParejasLejanasUniforme(Punto[] puntos) {
        double width = (double) vista.getGraphWidth();
        double height = (double) vista.getGraphHeight();

        //Recorrer los puntos y separarlos en 4 grupos
        Punto[][] esquinas = new Punto[4][];
        esquinas[0] = new Punto[puntos.length / 2];
        esquinas[1] = new Punto[puntos.length / 2];
        esquinas[2] = new Punto[puntos.length / 2];
        esquinas[3] = new Punto[puntos.length / 2];

        int[] tamaños = new int[4];
        Arrays.fill(tamaños, 0);

        Punto pSupIzq = new Punto(0.0, 0.0);
        Punto pSupDer = new Punto(width, 0.0);
        Punto pInfIzq = new Punto(0.0, height);
        Punto pInfDer = new Punto(width, height);

        //unión de esquinas
        double distanciaLimite = Punto.distancia(pSupIzq, pInfDer) / 16;

        //Repartimos los puntos en las 4 esquinaas correspondientes
        for (Punto p : puntos) {
            if (p.getX() < width / 2) {
                if (p.getY() < height / 2) {
                    p.setDistanciaPivote(pSupIzq);
                    if(p.getDistanciaPivote() <= distanciaLimite){
                        esquinas[0][tamaños[0]++] = p;
                    }
                } else {
                    p.setDistanciaPivote(pInfIzq);
                    if(p.getDistanciaPivote() <= distanciaLimite){
                        esquinas[1][tamaños[1]++] = p;
                    }
                }
            } else {
                if (p.getY() < height / 2) {
                    p.setDistanciaPivote(pSupDer);
                    if(p.getDistanciaPivote() <= distanciaLimite){
                        esquinas[2][tamaños[2]++] = p;
                    }
                } else {
                    p.setDistanciaPivote(pInfDer);
                    if(p.getDistanciaPivote() <= distanciaLimite){
                        esquinas[3][tamaños[3]++] = p;
                    }
                }
            }
        }

        // Ordenamos las 4 listas por la distancia a la esquina más cercana
        int indxTam = 0;
        for (Punto[] esquina: esquinas){
             Arrays.sort(esquina, 0, tamaños[indxTam] - 1, (a, b)
                -> Double.compare(a.getDistanciaPivote(), b.getDistanciaPivote()));

             indxTam++;
        }

        esquinas[0] = Arrays.copyOf(esquinas[0], tamaños[0] - 1);
        esquinas[1] = Arrays.copyOf(esquinas[1], tamaños[1]);
        esquinas[2] = Arrays.copyOf(esquinas[2], tamaños[2]);
        esquinas[3] = Arrays.copyOf(esquinas[3], tamaños[3] - 1);

        System.out.println("Puntos a comprobar: " + puntos.length);
        int total = 0;
        for(int i = 0; i < tamaños.length; i++){
            System.out.println("\tArray " + i + ": " + tamaños[i]);
            total += tamaños[i];
        }
        System.out.println("Total de puntos: " + total);

        for(Punto p1: esquinas[0]){
            for(Punto p2: esquinas[3]){
                Punto[] posibleSolucion = {p1, p2};
                modelo.pushSolucion(posibleSolucion);
            }
        }

        for(Punto p1: esquinas[1]){
            for(Punto p2: esquinas[2]){
                Punto[] posibleSolucion = {p1, p2};
                modelo.pushSolucion(posibleSolucion);
            }
        }

    }

    private void encontrarParejasLejanasGaussiana(Punto[] puntos) {
        double width = (double) vista.getGraphWidth();
        double height = (double) vista.getGraphHeight();

        //Recorrer los puntos y separarlos en 4 grupos
        Punto[][] esquinas = new Punto[4][];
        esquinas[0] = new Punto[puntos.length / 2];
        esquinas[1] = new Punto[puntos.length / 2];
        esquinas[2] = new Punto[puntos.length / 2];
        esquinas[3] = new Punto[puntos.length / 2];

        int[] tamaños = new int[4];
        Arrays.fill(tamaños, 0);

        Punto pCentro = new Punto(width/2, height/2);

        //unión de esquinas
        double distanciaLimite = Punto.distancia(new Punto(0.0, 0.0), new Punto(width, height)) / 4;

        //Repartimos los puntos en las 4 esquinaas correspondientes
        for (Punto p : puntos) {
            if (p.getX() < width / 2) {
                if (p.getY() < height / 2) {
                    p.setDistanciaPivote(pCentro);
                    if(p.getDistanciaPivote() >= distanciaLimite){
                        esquinas[0][tamaños[0]++] = p;
                    }
                } else {
                    p.setDistanciaPivote(pCentro);
                    if(p.getDistanciaPivote() >= distanciaLimite){
                        esquinas[1][tamaños[1]++] = p;
                    }
                }
            } else {
                if (p.getY() < height / 2) {
                    p.setDistanciaPivote(pCentro);
                    if(p.getDistanciaPivote() >= distanciaLimite){
                        esquinas[2][tamaños[2]++] = p;
                    }
                } else {
                    p.setDistanciaPivote(pCentro);
                    if(p.getDistanciaPivote() >= distanciaLimite){
                        esquinas[3][tamaños[3]++] = p;
                    }
                }
            }
        }

        esquinas[0] = Arrays.copyOf(esquinas[0], tamaños[0]);
        esquinas[1] = Arrays.copyOf(esquinas[1], tamaños[1]);
        esquinas[2] = Arrays.copyOf(esquinas[2], tamaños[2]);
        esquinas[3] = Arrays.copyOf(esquinas[3], tamaños[3]);

        System.out.println("Puntos a comprobar: " + puntos.length);
        int total = 0;
        for(int i = 0; i < tamaños.length; i++){
            System.out.println("\tArray " + i + ": " + tamaños[i]);
            total += tamaños[i];
        }
        System.out.println("Total de puntos: " + total);

        for(Punto p1: esquinas[0]){
            for(Punto p2: esquinas[1]){
                Punto[] posibleSolucion = {p1, p2};
                modelo.pushSolucion(posibleSolucion);
            }
            for(Punto p2: esquinas[2]){
                Punto[] posibleSolucion = {p1, p2};
                modelo.pushSolucion(posibleSolucion);
            }
            for(Punto p2: esquinas[3]){
                Punto[] posibleSolucion = {p1, p2};
                modelo.pushSolucion(posibleSolucion);
            }
        }


        for(Punto p1: esquinas[1]){
            for(Punto p2: esquinas[2]){
                Punto[] posibleSolucion = {p1, p2};
                modelo.pushSolucion(posibleSolucion);
            }
            for(Punto p2: esquinas[3]){
                Punto[] posibleSolucion = {p1, p2};
                modelo.pushSolucion(posibleSolucion);
            }
        }
        for(Punto p1: esquinas[2]){
            for(Punto p2: esquinas[3]){
                Punto[] posibleSolucion = {p1, p2};
                modelo.pushSolucion(posibleSolucion);
            }
        }

    }




    private void encontrarParejasLejanasChi2v1(Punto[] puntos) {
        double width = (double) vista.getGraphWidth();
        double height = (double) vista.getGraphHeight();

        Punto pChi1 = new Punto(width * 3 / 8, height * 3 / 8);
        Punto pChi2 = new Punto(width * 5 / 8, height * 5 / 8);

        double distanciaLimite1 = Punto.distancia(pChi1, new Punto(width * 9 / 16, height * 9 / 16));
        double distanciaLimite2 = Punto.distancia(pChi2, new Punto(width * 7 / 16, height * 7 / 16));

        Punto[] SelectedPoints = new Punto[puntos.length/4];
        int length = 0;
        //Repartimos los puntos en las 4 esquinaas correspondientes
        for (Punto p1 : puntos) {
            if(Punto.distancia(p1, pChi1) > distanciaLimite1 &
                    Punto.distancia(p1, pChi2) > distanciaLimite2){
                SelectedPoints[length++] = p1;
            }
        }

        SelectedPoints = Arrays.copyOf(SelectedPoints, length);
        System.out.println("Puntos a comprobar: " + SelectedPoints.length);

        for(Punto p1: SelectedPoints){
            for (Punto p2: SelectedPoints){
                if(p1 != p2){
                    Punto[] posibleSolucion = {p1, p2};
                    modelo.pushSolucion(posibleSolucion);
                }
            }
        }
    }


    private void encontrarParejasLejanas(Punto[] puntos) {
        switch(modelo.getDistribucion()){
            case GAUSSIAN ->
                encontrarParejasLejanasGaussiana(puntos);
            case EXPONENCIAL ->
                encontrarParejasLejanasChi2v1(puntos);
            case UNIFORME ->
                encontrarParejasLejanasUniforme(puntos);
            default ->
                throw new AssertionError();
        }
    }

    public Model getModelo() {
        return modelo;
    }

    public void setModelo(Model modelo) {
        this.modelo = modelo;
    }

    public View getVista() {
        return vista;
    }

    public void setVista(View vista) {
        this.vista = vista;
    }

}
