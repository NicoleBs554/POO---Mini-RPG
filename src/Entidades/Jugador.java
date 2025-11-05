package entidades;

import items.Arma;
import items.Armadura;

public class Jugador extends Personaje {
    private int estrellas;

    public Jugador(String nombre, int vida, int fuerza, int defensa) {
        super(nombre, vida, fuerza, defensa);
        this.estrellas = 0;
    }

    public void equiparArma(Arma arma) {
        this.armaEquipada = arma;
        System.out.println("Has equipado: " + arma.getNombre());
    }

    public void equiparArmadura(Armadura armadura) {
        this.armaduraEquipada = armadura;
        this.defensa += armadura.getDefensa();
        System.out.println("Has equipado: " + armadura.getNombre());
    }

    public int atacar() {
        int danoBase = fuerza;
        if (armaEquipada != null) {
            danoBase += armaEquipada.getDano();
        }
        return danoBase;
    }

    public void ganarExperiencia(int exp) {
        this.experiencia += exp;
        System.out.println("Has ganado " + exp + " puntos de experiencia.");
        
        // Sistema simple de subida de nivel
        if (experiencia >= nivel * 10) {
            subirNivel();
        }
    }

    private void subirNivel() {
        nivel++;
        vidaMaxima += 10;
        vida = vidaMaxima;
        fuerza += 2;
        defensa += 1;
        experiencia = 0;
        
        System.out.println("¡¡Has subido al nivel " + nivel + "!!");
        System.out.println("Vida máxima: +10");
        System.out.println("Fuerza: +2");
        System.out.println("Defensa: +1");
    }

    public void agregarEstrella() {
        estrellas++;
        System.out.println("¡Has obtenido una Estrella de Logro! (" + estrellas + "/3)");
    }
}