package levels;

import java.util.Scanner;
import combat.Combate;
import entities.Jugador;
import entities.Enemigo;
import combat.Utils;
import game.GameState;
import java.util.*;

public class Piso1_Limbo extends Piso {
    private GameState estadoJuego;
    
    public Piso1_Limbo(Jugador p, Scanner in) {
        super(p, in);
    }
    
    // Método para inyectar GameState
    public void setGameState(GameState estadoJuego) {
        this.estadoJuego = estadoJuego;
    }
    
    @Override
    public void play() {
        System.out.println("\n=== PISO 1: BOSQUE DE LOS OLVIDADOS ===");
        System.out.println("Un bosque encantado donde los proyectos abandonados y sueños olvidados toman vida.");
        
        if (!preRoom("Sala 1: Encuentro con Perezaflor")) sala1();
        if (!p.isAlive()) return;
        
        if (!preRoom("Sala 2: Plataformas de Hojas Móviles")) sala2();
        if (!p.isAlive()) return;
        
        if (!preRoom("Sala 3: Libro-Ciempiés Obstinado")) sala3();
        if (!p.isAlive()) return;
        
        if (!preRoom("Sala 4: Telarañas de la Araña Despistada")) sala4();
        if (!p.isAlive()) return;
        
        if (!preRoom("Sala 5: Fantasma de la Procrastinación")) sala5();
    }
    
    private void sala1() {
        System.out.println("\n--- Sala 1: Encuentro con Perezaflor ---");
        System.out.println("Una planta gigante con flores que emiten esporas ralentizadoras bloquea tu camino.");
        System.out.println("Perezaflor: '¿Tan rápido quieres pasar? Descansa un poco...'");
        
        System.out.println("Opciones: 1. Atacar / 2. Intentar esquivar esporas / 3. Usar objeto / 4. Habilidad especial");
        
        String opt = in.nextLine().trim();
        if ("2".equals(opt)) {
            boolean success = p.skillCheck(6);
            if (success) {
                System.out.println("Esquivas hábilmente las esporas y pasas de largo. Encuentras 5 monedas.");
                p.addGold(5);
                p.addXp(8);
                
                // Actualizar GameState por éxito de habilidad
                if (estadoJuego != null) {
                    estadoJuego.incrementarPuzzlesResueltos();
                }
                return;
            } else {
                System.out.println("Las esporas te alcanzan! Te sientes pesado y lento.");
                System.out.println("La Perezaflor te ataca!");
            }
        }
        
        Enemigo perezaflor = new Enemigo("Perezaflor", "Planta", 18, 3, 2, 75);
        perezaflor.setFraseCombate("¡Descansa para siempre!");
        boolean won = Combate.fight(p, Arrays.asList(perezaflor), in);
        if (won) {
            // Actualizar GameState
            if (estadoJuego != null) {
                estadoJuego.incrementarEnemigosDerrotados();
            }
            
            int oro = Utils.rnd(8, 12);
            p.addGold(oro);
            p.addItem("Espora Ralentizadora");
            p.addXp(15);
            System.out.println("Pista obtenida: 'Las hojas que se mueven llevan a lugares ocultos'");
        }
    }
    
    private void sala2() {
        System.out.println("\n--- Sala 2: Plataformas de Hojas Móviles ---");
        System.out.println("Un abismo con hojas gigantes que flotan y se mueven erraticamente.");
        System.out.println("Debes saltar con precisión entre ellas.");
        
        boolean exitoSalto = false;
        int intentos = 0;
        
        while (!exitoSalto && intentos < 3) {
            System.out.println("Opciones: 1. Saltar con cuidado / 2. Saltar rápidamente / 3. Usar habilidad de equilibrio");
            String opt = in.nextLine().trim();
            
            if ("1".equals(opt)) {
                if (Utils.rnd(1, 10) <= 7) {
                    exitoSalto = true;
                    System.out.println("Saltas con precisión y llegas al otro lado!");
                } else {
                    System.out.println("La hoja se mueve! Casi caes.");
                }
            } else if ("2".equals(opt)) {
                if (Utils.rnd(1, 10) <= 5) {
                    exitoSalto = true;
                    System.out.println("Un salto rápido y arriesgado! Llegas al otro lado.");
                } else {
                    System.out.println("Demasiado rápido! Resbalas y pierdes equilibrio.");
                    p.damage(3);
                }
            } else if ("3".equals(opt)) {
                boolean success = p.skillCheck(5);
                if (success) {
                    exitoSalto = true;
                    System.out.println("Usas tu habilidad y saltas perfectamente!");
                } else {
                    System.out.println("Fallaste el salto!");
                }
            }
            intentos++;
        }
        
        if (exitoSalto) {
            // Actualizar GameState por puzzle resuelto
            if (estadoJuego != null) {
                estadoJuego.incrementarPuzzlesResueltos();
            }
            
            System.out.println("Encuentras un cofre oculto con 10 monedas y una poción.");
            p.addGold(10);
            p.addItem("Poción menor");
            p.addXp(10);
        } else {
            System.out.println("Caes al abismo! Pierdes 8 HP.");
            p.damage(8);
        }
    }
    
    private void sala3() {
        System.out.println("\n--- Sala 3: Libro-Ciempiés Obstinado ---");
        System.out.println("Un libro antiguo con patas de ciempiés bloquea el camino, sus páginas susurran.");
        System.out.println("Libro-Ciempiés: 'Nadie me lee... nadie me comprende...'");
        
        System.out.println("Opciones: 1. Combatir / 2. Intentar razonar / 3. Buscar punto débil / 4. Huir");
        
        String opt = in.nextLine().trim();
        if ("2".equals(opt)) {
            System.out.println("Intentas razonar con el libro:");
            System.out.println("Respuestas: A. 'Te leeré si me dejas pasar' / B. 'Eres muy interesante' / C. 'Todos te olvidaron'");
            String r = in.nextLine().trim().toUpperCase();
            if ("A".equals(r)) {
                // Actualizar GameState por puzzle resuelto
                if (estadoJuego != null) {
                    estadoJuego.incrementarPuzzlesResueltos();
                }
                
                System.out.println("Libro-Ciempiés: '¡Al fin alguien me valorará!' Te deja pasar y te da un hechizo.");
                p.addItem("Hechizo de Comprensión");
                p.addXp(12);
                return;
            } else if ("B".equals(r)) {
                // Actualizar GameState por puzzle resuelto
                if (estadoJuego != null) {
                    estadoJuego.incrementarPuzzlesResueltos();
                }
                
                System.out.println("El libro se conmueve y te da un fragmento de conocimiento.");
                p.addItem("Fragmento de Sabiduría");
                p.addXp(8);
                return;
            } else {
                System.out.println("El libro se enfurece! '¡Cómo te atreves!'");
            }
        } else if ("3".equals(opt)) {
            boolean success = p.skillCheck(6);
            if (success) {
                System.out.println("Encuentras que el lomo del libro es su punto débil. Atacas con ventaja!");
                Enemigo libro = new Enemigo("Libro-Ciempiés", "Libro", 22, 4, 1, 70);
                libro.setFraseCombate("¡Mis páginas arden!");
                boolean won = Combate.fight(p, Arrays.asList(libro), in);
                if (won) {
                    // Actualizar GameState
                    if (estadoJuego != null) {
                        estadoJuego.incrementarEnemigosDerrotados();
                    }
                    recompensaSala3();
                }
                return;
            }
        }
        
        Enemigo libro = new Enemigo("Libro-Ciempiés", "Libro", 22, 4, 3, 70);
        libro.setFraseCombate("¡Nadie me comprende!");
        boolean won = Combate.fight(p, Arrays.asList(libro), in);
        if (won) {
            // Actualizar GameState
            if (estadoJuego != null) {
                estadoJuego.incrementarEnemigosDerrotados();
            }
            recompensaSala3();
        }
    }
    
    private void recompensaSala3() {
        p.addGold(Utils.rnd(10, 15));
        p.addItem("Página Antigua");
        p.addXp(18);
    }
    
    private void sala4() {
        System.out.println("\n--- Sala 4: Telarañas de la Araña Despistada ---");
        System.out.println("Telarañas gigantescas bloquean el camino. Una araña distraída teje patrones caóticos.");
        System.out.println("Araña Despistada: '¿Dónde puse mi siguiente hilo? No me acuerdo...'");
        
        System.out.println("Opciones: 1. Cortar telarañas / 2. Sigilo entre hilos / 3. Distraer a la araña / 4. Atacar directamente");
        
        String opt = in.nextLine().trim();
        if ("2".equals(opt)) {
            boolean success = p.skillCheck(7);
            if (success) {
                // Actualizar GameState por puzzle resuelto
                if (estadoJuego != null) {
                    estadoJuego.incrementarPuzzlesResueltos();
                }
                
                System.out.println("Te mueves sigilosamente entre los hilos y evitas la confrontación.");
                System.out.println("Encuentras un objeto brillante en una telaraña: Anillo de Agilidad");
                p.addItem("Anillo de Agilidad");
                p.addXp(15);
                return;
            } else {
                System.out.println("Tropezaste con un hilo! La araña te detecta.");
            }
        } else if ("3".equals(opt)) {
            System.out.println("Lanzas una piedra para distraer a la araña...");
            if (Utils.rnd(1, 10) <= 6) {
                // Actualizar GameState por puzzle resuelto
                if (estadoJuego != null) {
                    estadoJuego.incrementarPuzzlesResueltos();
                }
                
                System.out.println("La araña se distrae! Pasas tranquilamente.");
                p.addXp(10);
                return;
            } else {
                System.out.println("La araña nota tu truco y se enfurece!");
            }
        }
        
        Enemigo araña = new Enemigo("Araña Despistada", "Araña", 25, 5, 2, 80);
        araña.setFraseCombate("¡Ahí estás!");
        boolean won = Combate.fight(p, Arrays.asList(araña), in);
        if (won) {
            // Actualizar GameState
            if (estadoJuego != null) {
                estadoJuego.incrementarEnemigosDerrotados();
            }
            
            p.addGold(Utils.rnd(12, 18));
            p.addItem("Seda de Araña");
            p.addXp(20);
        }
    }
    
    private void sala5() {
        System.out.println("\n--- Sala 5: Fantasma de la Procrastinación ---");
        System.out.println("Una figura etérea que crea clones ilusorios. Se ríe burlonamente.");
        System.out.println("Fantasma: '¿Tanta prisa? Podrías dejarlo para después...'");
        
        System.out.println("Opciones: 1. Combatir clones / 2. Buscar al real / 3. Usar espejo / 4. Habilidad especial");
        
        // Puzzle de clones
        System.out.println("El fantasma crea 3 clones idénticos. ¿Cuál es el real?");
        System.out.println("Clone 1: Se ve sólido y real");
        System.out.println("Clone 2: Tiene una sombra tenue");
        System.out.println("Clone 3: Parpadea ligeramente");
        
        String opt = in.nextLine().trim();
        
        if ("2".equals(opt)) {
            System.out.println("Correcto! El clone 2 es el real - los fantasmas no proyectan sombras!");
            
            // Actualizar GameState por puzzle resuelto
            if (estadoJuego != null) {
                estadoJuego.incrementarPuzzlesResueltos();
            }
        } else {
            System.out.println("Incorrecto! Atacas un clone ilusorio.");
            p.damage(5);
        }
        
        Enemigo fantasma = new Enemigo("Fantasma de la Procrastinación", "Espíritu", 30, 6, 4, 75);
        fantasma.setFraseCombate("¡Mañana lo haré mejor!");
        
        boolean won = Combate.fight(p, Arrays.asList(fantasma), in);
        if (won) {
            // Actualizar GameState - jefe derrotado
            if (estadoJuego != null) {
                estadoJuego.incrementarEnemigosDerrotados();
                estadoJuego.agregarEvento("Jefe Piso 1 - Fantasma de la Procrastinación derrotado");
            }
            
            System.out.println("Has derrotado al Fantasma de la Procrastinación!");
            p.addGold(Utils.rnd(20, 30));
            p.addItem("Esencia de Determinación");
            p.addXp(35);
            System.out.println("Una puerta brillante se abre hacia el siguiente piso...");
        }
    }
}