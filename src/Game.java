package src;

import entidades.Jugador;
import levels.Nivel;
import levels.Nivel1_Limbo;
import levels.Nivel2_Dite;
import levels.Nivel3_Recuerdos;
import java.util.Scanner;

public class Game {
    private Jugador jugador;
    private Nivel nivelActual;
    private int nivelNumero;
    private Scanner scanner;
    private boolean juegoActivo;

    public Game() {
        this.scanner = new Scanner(System.in);
        this.juegoActivo = true;
        this.nivelNumero = 1;
    }

    public void iniciarJuego() {
        System.out.println("=== EL VIAJE DEL PEQUEÑO VALIENTE ===");
        System.out.println("Bienvenido, viajero. ¿Cuál es tu nombre?");
        String nombre = scanner.nextLine();
        
        this.jugador = new Jugador(nombre, 100, 10, 5);
        this.nivelActual = new Nivel1_Limbo();
        
        buclePrincipal();
    }

    private void buclePrincipal() {
        while (juegoActivo && jugador.estaVivo()) {
            System.out.println("\n=== NIVEL " + nivelNumero + " ===");
            nivelActual.mostrarDescripcion();
            
            boolean nivelCompletado = nivelActual.ejecutarNivel(jugador, scanner);
            
            if (nivelCompletado) {
                System.out.println("¡Nivel " + nivelNumero + " completado!");
                recompensarJugador();
                avanzarNivel();
            } else {
                System.out.println("Has fallado en el nivel. ¡Inténtalo de nuevo!");
            }
        }
        
        if (jugador.estaVivo()) {
            System.out.println("¡Felicidades! Has completado toda la aventura.");
        } else {
            System.out.println("Game Over. Tu viaje ha terminado.");
        }
        
        scanner.close();
    }

    private void recompensarJugador() {
        System.out.println("\n--- RECOMPENSA ---");
        System.out.println("Has obtenido:");
        System.out.println("- 1 Llave de Paso");
        System.out.println("- 1 Estrella de Logro");
        System.out.println("- +5 puntos de experiencia");
        
        jugador.agregarItem("Llave de Paso");
        jugador.agregarItem("Estrella de Logro");
        jugador.ganarExperiencia(5);
    }

    private void avanzarNivel() {
        nivelNumero++;
        switch (nivelNumero) {
            case 2:
                nivelActual = new Nivel2_Dite();
                break;
            case 3:
                nivelActual = new Nivel3_Recuerdos();
                break;
            default:
                juegoActivo = false;
                break;
        }
    }
}