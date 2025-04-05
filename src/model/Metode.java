package model;

public enum Metode {
    FUERZA_BRUTA("Clàsic O(n²)"),
    DIVIDE_Y_VENCERAS("Divideix i Venceràs O(n·log(n))"),
    CONVEX_HULL("Convex Hull + Rotating Calipers O(n·log(n))");

    private final String description;

    Metode(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return description;
    }
}
