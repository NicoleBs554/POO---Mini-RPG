package items;

public class Arma {
    private String nombre;
    private int dano;
    private String tipo;

    public Arma(String nombre, int dano, String tipo) {
        this.nombre = nombre;
        this.dano = dano;
        this.tipo = tipo;
    }

    // Getters
    public String getNombre() { return nombre; }
    public int getDano() { return dano; }
    public String getTipo() { return tipo; }
}