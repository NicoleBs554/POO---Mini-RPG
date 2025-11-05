package entities;

import items.Arma;
import items.Armadura;
import java.util.ArrayList;

public class Personaje {
    protected String nombre;
    protected int vida;
    protected int vidaMaxima;
    protected int fuerza;
    protected int defensa;
    protected int nivel;
    protected int experiencia;
    protected Arma armaEquipada;
    protected Armadura armaduraEquipada;
    protected ArrayList<String> inventario;

    public Personaje(String nombre, int vida, int fuerza, int defensa) {
        this.nombre = nombre;
        this.vida = vida;
        this.vidaMaxima = vida;
        this.fuerza = fuerza;
        this.defensa = defensa;
        this.nivel = 1;
        this.experiencia = 0;
        this.inventario = new ArrayList<>();
    }

    // Getters y Setters básicos
    public String getNombre() { return nombre; }
    public int getVida() { return vida; }
    public int getFuerza() { return fuerza; }
    public int getDefensa() { return defensa; }
    public boolean estaVivo() { return vida > 0; }

    public void recibirDano(int dano) {
        int danoReal = Math.max(1, dano - defensa);
        this.vida -= danoReal;
        System.out.println(nombre + " recibe " + danoReal + " puntos de daño.");
    }

    public void curar(int cantidad) {
        this.vida = Math.min(vidaMaxima, vida + cantidad);
        System.out.println(nombre + " recupera " + cantidad + " puntos de vida.");
    }

    public void agregarItem(String item) {
        inventario.add(item);
    }

    public boolean tieneItem(String item) {
        return inventario.contains(item);
    }

    public void mostrarEstado() {
        System.out.println("\n=== ESTADO DE " + nombre.toUpperCase() + " ===");
        System.out.println("Vida: " + vida + "/" + vidaMaxima);
        System.out.println("Fuerza: " + fuerza);
        System.out.println("Defensa: " + defensa);
        System.out.println("Nivel: " + nivel);
        System.out.println("Experiencia: " + experiencia);
        if (armaEquipada != null) {
            System.out.println("Arma: " + armaEquipada.getNombre());
        }
        System.out.println("Inventario: " + inventario);
    }
}