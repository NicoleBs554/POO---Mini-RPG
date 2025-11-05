package entidades;

public class Enemigo extends Personaje {
    private String tipo;
    private int experienciaOtorgada;

    public Enemigo(String nombre, String tipo, int vida, int fuerza, int defensa, int experienciaOtorgada) {
        super(nombre, vida, fuerza, defensa);
        this.tipo = tipo;
        this.experienciaOtorgada = experienciaOtorgada;
    }

    public int atacar() {
        return fuerza;
    }

    public int getExperienciaOtorgada() {
        return experienciaOtorgada;
    }

    public String getTipo() {
        return tipo;
    }

    public void mostrarInfo() {
        System.out.println(tipo + ": " + nombre + " (Vida: " + vida + ")");
    }
}