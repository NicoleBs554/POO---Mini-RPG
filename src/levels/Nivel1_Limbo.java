package levels;

import entidades.Jugador;
import entidades.Enemigo;
import java.util.Scanner;

public class Nivel1_Limbo extends Nivel {
    
    public Nivel1_Limbo() {
        this.nombre = "Bosque de los Olvidados";
        this.descripcion = "Un bosque encantado donde habitan proyectos abandonados y sueños olvidados.";
        this.enemigosRequeridos = 5;
    }

    @Override
    public boolean ejecutarNivel(Jugador jugador, Scanner scanner) {
        System.out.println("Debes derrotar " + enemigosRequeridos + " enemigos para avanzar.");
        
        Enemigo[] enemigos = {
            new Enemigo("Perezaflor", "Planta", 20, 3, 1, 2),
            new Enemigo("Libro-ciempiés", "Insecto", 25, 4, 2, 3),
            new Enemigo("Lamento rodante", "Espíritu", 15, 5, 0, 2),
            new Enemigo("Araña despistada", "Aracnido", 30, 6, 3, 4),
            new Enemigo("Fantasma procrastinador", "Fantasma", 35, 7, 2, 5)
        };
        
        int enemigosDerrotados = 0;
        
        for (Enemigo enemigo : enemigos) {
            System.out.println("\nTe enfrentas a: " + enemigo.getNombre());
            enemigo.mostrarInfo();
            
            boolean victoria = combateBasico(jugador, enemigo, scanner);
            
            if (victoria) {
                enemigosDerrotados++;
                System.out.println("Enemigos derrotados: " + enemigosDerrotados + "/" + enemigosRequeridos);
                
                if (!jugador.estaVivo()) {
                    return false;
                }
            } else {
                return false;
            }
        }
        
        return enemigosDerrotados >= enemigosRequeridos;
    }
}