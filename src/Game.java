package com.elianepg.rpg;

import java.util.Scanner;

public class Game {
    private Personaje jugador;
    private Nivel[] niveles;
    private int nivelActual;
    private boolean enJuego;

    public static void main(String[] args) {
        Game juego = new Game();
        juego.inicializarJuego();
        juego.buclePrincipal();
    }

    public void inicializarJuego() {
        // Inicializar jugador
        jugador = new Personaje("Leo", 100, 10, 5);
        // Inicializar niveles
        niveles = new Nivel[3];
        niveles[0] = new Nivel(1, "Limbo", "Un bosque de seres olvidados...");
        niveles[1] = new Nivel(2, "Ciudad de Dite", "Una ciudad de sombras engañosas...");
        niveles[2] = new Nivel(3, "Palacio de Recuerdos", "Un castillo helado de traiciones...");
        nivelActual = 0;
        enJuego = true;
    }

    public void buclePrincipal() {
        Scanner scanner = new Scanner(System.in);
        while (enJuego) {
            // Mostrar menú principal
            System.out.println("=== MENÚ PRINCIPAL ===");
            System.out.println("1. Ver estado del personaje");
            System.out.println("2. Ir al siguiente nivel");
            System.out.println("3. Salir del juego");
            System.out.print("Elige una opción: ");
            int opcion = scanner.nextInt();
            switch (opcion) {
                case 1:
                    jugador.mostrarEstado();
                    break;
                case 2:
                    if (nivelActual < niveles.length) {
                        Nivel nivel = niveles[nivelActual];
                        nivel.iniciarNivel(jugador);
                        if (nivel.isCompletado()) {
                            nivelActual++;
                        }
                    } else {
                        System.out.println("¡Has completado todos los niveles!");
                        enJuego = false;
                    }
                    break;
                case 3:
                    enJuego = false;
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        }
        scanner.close();
        System.out.println("¡Gracias por jugar!");
    }
}