package levels;

import entities.Jugador;
import java.util.Scanner;

public abstract class Nivel {
    protected String nombre;
    protected String descripcion;
    protected int enemigosRequeridos;

    public abstract boolean ejecutarNivel(Jugador jugador, Scanner scanner);

    public void mostrarDescripcion() {
        System.out.println(descripcion);
    }

    protected boolean combateBasico(Jugador jugador, Enemigo enemigo, Scanner scanner) {
        System.out.println("\n--- COMBATE vs " + enemigo.getNombre().toUpperCase() + " ---");
        
        while (jugador.estaVivo() && enemigo.estaVivo()) {
            // Turno del jugador
            System.out.println("\nTu turno:");
            System.out.println("1. Atacar");
            System.out.println("2. Usar objeto");
            System.out.println("3. Ver estado");
            System.out.print("Elige: ");
            
            int opcion = scanner.nextInt();
            scanner.nextLine(); // Limpiar buffer
            
            switch (opcion) {
                case 1:
                    int danoJugador = jugador.atacar();
                    enemigo.recibirDano(danoJugador);
                    System.out.println("Infliges " + danoJugador + " puntos de daño.");
                    break;
                case 2:
                    System.out.println("Usas un objeto... (implementar sistema de objetos)");
                    break;
                case 3:
                    jugador.mostrarEstado();
                    continue;
                default:
                    System.out.println("Opción inválida.");
                    continue;
            }
            
            // Turno del enemigo si sigue vivo
            if (enemigo.estaVivo()) {
                int danoEnemigo = enemigo.atacar();
                jugador.recibirDano(danoEnemigo);
            }
        }
        
        if (jugador.estaVivo()) {
            System.out.println("¡Has derrotado a " + enemigo.getNombre() + "!");
            jugador.ganarExperiencia(enemigo.getExperienciaOtorgada());
            return true;
        }
        
        return false;
    }
}
