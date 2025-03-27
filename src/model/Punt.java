package model;

public class Punt implements Comparable<Punt> {

    private Double x, y;

    private Double distanciaPivote;


    private static final Double MARGEN = 0.0001; // Margen para considerar dos puntos iguales.
    // CONSTRUCTORS
    public Punt(Double x, Double y) {
        this.x = x;
        this.y = y;
    }

    // CLASS METHODS
    @Override
    public int compareTo(Punt o) {
        if (o.x > this.x - MARGEN && o.x < this.x + MARGEN) {
            return 0;
        } else if (this.x < o.x) {
            return -1;
        } else {
            return 1;
        }
    }

    public static double distancia(Punt p1, Punt p2) {
        double dx = p1.x - p2.x;
        double dy = p1.y - p2.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    // SETTERS & GETTERS
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setDistanciaPivote (Punt pPivote){
        this.distanciaPivote = Punt.distancia(this, pPivote);
    }

    public void setDistanciaPivote (Double d){
        this.distanciaPivote = d;
    }

    public double getDistanciaPivote(){
        return this.distanciaPivote;
    }


    @Override
    public String toString(){
        return "{Punto: x=" + this.x + " , y=" + this.y + " }";
    }

}
