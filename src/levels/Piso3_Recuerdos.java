package levels;

import entities.Jugador;
import entities.Enemigo;
import combat.Combate;
import combat.Utils;
import game.GameState;
import java.util.*;

public class Piso3_Recuerdos extends Piso {
    private boolean puzzleSonidosResuelto = false;
    private int recuerdosSanadores = 0;
    private GameState estadoJuego;
    
    public Piso3_Recuerdos(Jugador p, Scanner in) {
        super(p, in);
    }
    
    public void setGameState(GameState estadoJuego) {
        this.estadoJuego = estadoJuego;
    }
    
    @Override
    public void play() {
        System.out.println("\n=== PISO 3: PALACIO DE LOS RECUERDOS ===");
        System.out.println("Un castillo helado donde los recuerdos familiares toman forma y te confrontan.");
        
        if (!preRoom("Sala 1: Tío Crítico")) sala1();
        if (!p.isAlive()) return;
        
        if (!preRoom("Sala 2: Primera Celosa")) sala2();
        if (!p.isAlive()) return;
        
        if (!preRoom("Sala 3: Abuelo Rigidez")) sala3();
        if (!p.isAlive()) return;
        
        if (!preRoom("Sala 4: Hermano Mayor Burlesco")) sala4();
        if (!p.isAlive()) return;
        
        if (!preRoom("Sala 5: Sombra del Pasado")) sala5();
    }
    
    private void sala1() {
        System.out.println("\n--- Sala 1: Tío Crítico ---");
        System.out.println("Una figura familiar que lanza frases cortantes que crean obstáculos de hielo.");
        System.out.println("Tío Crítico: '¿Otra vez intentando algo? Sabes que no lo lograrás...'");
        
        System.out.println("Opciones: 1. Ignorar críticas / 2. Contraargumentar / 3. Usar confianza / 4. Atacar");
        
        String opt = in.nextLine().trim();
        if ("2".equals(opt)) {
            System.out.println("Contraargumentas sus críticas:");
            System.out.println("A. 'Cada intento me hace más fuerte' / B. 'Al menos lo intento' / C. 'Tú también fallaste'");
            String respuesta = in.nextLine().trim().toUpperCase();
            if ("A".equals(respuesta)) {
                System.out.println("El Tío se queda sin palabras! Su poder se debilita.");
                Enemigo tio = new Enemigo("Tío Crítico", "Familiar", 25, 4, 3, 70);
                tio.setFraseCombate("¡Bah! Pura suerte...");
                boolean won = Combate.fight(p, Arrays.asList(tio), in);
                if (won) {
                    if (estadoJuego != null) {
                        estadoJuego.incrementarEnemigosDerrotados();
                    }
                    recompensaSala1();
                }
                return;
            }
        } else if ("3".equals(opt) && p.hasItem("Amuleto de Confianza")) {
            System.out.println("El amuleto brilla, protegiéndote de las palabras dañinas.");
            recuerdosSanadores++;
        }
        
        Enemigo tio = new Enemigo("Tío Crítico", "Familiar", 25, 4, 3, 70);
        tio.setFraseCombate("¡No eres lo suficientemente bueno!");
        boolean won = Combate.fight(p, Arrays.asList(tio), in);
        if (won) {
            if (estadoJuego != null) {
                estadoJuego.incrementarEnemigosDerrotados();
            }
            recompensaSala1();
        }
    }
    
    private void recompensaSala1() {
        p.addGold(Utils.rnd(15, 20));
        p.addItem("Fragmento de Autoestima");
        p.addXp(22);
    }
    
    private void sala2() {
        System.out.println("\n--- Sala 2: Primera Celosa ---");
        System.out.println("Una figura que invoca clones ilusorios de tus logros y los de otros.");
        System.out.println("Primera Celosa: 'Mira lo que ELLOS tienen... ¿por qué tú no?'");
        
        System.out.println("Opciones: 1. Enfocar en tu camino / 2. Compararte / 3. Usar gratitud / 4. Atacar clones");
        
        String opt = in.nextLine().trim();
        if ("1".equals(opt)) {
            boolean success = p.skillCheck(6);
            if (success) {
                System.out.println("Te enfocas en tu propio progreso! Los clones pierden poder.");
                Enemigo primera = new Enemigo("Prima Celosa", "Familiar", 22, 3, 2, 75);
                primera.setFraseCombate("¡Pero merezco más!");
                boolean won = Combate.fight(p, Arrays.asList(primera), in);
                if (won) {
                    if (estadoJuego != null) {
                        estadoJuego.incrementarEnemigosDerrotados();
                    }
                    recompensaSala2();
                }
                return;
            }
        } else if ("3".equals(opt)) {
            System.out.println("Recuerdas lo que tienes y sientes gratitud...");
            System.out.println("La envidia pierde fuerza frente a la apreciación.");
            recuerdosSanadores++;
        }
        
        Enemigo primera = new Enemigo("Prima Celosa", "Familiar", 22, 3, 2, 75);
        primera.setFraseCombate("¡Quiero lo tuyo!");
        boolean won = Combate.fight(p, Arrays.asList(primera), in);
        if (won) {
            if (estadoJuego != null) {
                estadoJuego.incrementarEnemigosDerrotados();
            }
            recompensaSala2();
        }
    }
    
    private void recompensaSala2() {
        p.addGold(Utils.rnd(12, 18));
        p.addItem("Esencia de Gratitud");
        p.addXp(20);
    }
    
    private void sala3() {
        System.out.println("\n--- Sala 3: Abuelo Rigidez ---");
        System.out.println("Crea laberintos mentales de 'deberías' y 'siempre se ha hecho así'.");
        System.out.println("Abuelo Rigidez: 'En mis tiempos... las cosas eran diferentes y MEJORES'");
        
        if (!puzzleSonidosResuelto) {
            System.out.println("Debes resolver el puzzle de los sonidos para sanar esta rigidez...");
            resolverPuzzleSonidos();
        }
        
        if (puzzleSonidosResuelto) {
            System.out.println("Los sonidos armoniosos ablandan la rigidez del Abuelo.");
            Enemigo abuelo = new Enemigo("Abuelo Rigidez", "Familiar", 20, 5, 4, 65);
            abuelo.setFraseCombate("¿Tal vez... hay nuevas formas?");
            boolean won = Combate.fight(p, Arrays.asList(abuelo), in);
            if (won) {
                if (estadoJuego != null) {
                    estadoJuego.incrementarEnemigosDerrotados();
                }
                recompensaSala3();
            }
        } else {
            System.out.println("La rigidez mental te afecta! Pierdes 5 HP.");
            p.damage(5);
            Enemigo abuelo = new Enemigo("Abuelo Rigidez", "Familiar", 30, 5, 4, 65);
            abuelo.setFraseCombate("¡A la antigua usanza!");
            boolean won = Combate.fight(p, Arrays.asList(abuelo), in);
            if (won) {
                if (estadoJuego != null) {
                    estadoJuego.incrementarEnemigosDerrotados();
                }
                recompensaSala3();
            }
        }
    }
    
    private void resolverPuzzleSonidos() {
        System.out.println("\n=== PUZZLE DE LOS SONIDOS SANADORES ===");
        System.out.println("Escuchas cuatro sonidos del pasado:");
        System.out.println("1. 🎻 Violín (nostalgia)");
        System.out.println("2. ⚡ Tormenta (conflicto)");
        System.out.println("3. 🌊 Olas (paz)");
        System.out.println("4. 👶 Risa infantil (inocencia)");
        System.out.println("\n¿Cuál es el camino para sanar?");
        System.out.println("A. 1-2-3-4 (Nostalgia->Conflicto->Paz->Inocencia)");
        System.out.println("B. 2-1-4-3 (Conflicto->Nostalgia->Inocencia->Paz)");
        System.out.println("C. 3-4-2-1 (Paz->Inocencia->Conflicto->Nostalgia)");
        
        String respuesta = in.nextLine().trim().toUpperCase();
        if ("B".equals(respuesta)) {
            if (estadoJuego != null) {
                estadoJuego.incrementarPuzzlesResueltos();
            }
            
            System.out.println("Correcto! Sanación completa: Enfrentar conflicto -> Recordar con cariño -> Recuperar inocencia -> Encontrar paz");
            puzzleSonidosResuelto = true;
            p.addItem("Amuleto del Perdón");
            recuerdosSanadores += 2;
        } else {
            System.out.println("Esa secuencia no lleva a la sanación verdadera.");
        }
    }
    
    private void recompensaSala3() {
        p.addGold(Utils.rnd(18, 25));
        p.addItem("Sabiduría Adaptable");
        p.addXp(25);
    }
    
    private void sala4() {
        System.out.println("\n--- Sala 4: Hermano Mayor Burlesco ---");
        System.out.println("Convierte el suelo en superficies resbaladizas de inseguridad.");
        System.out.println("Hermano: 'Jaja! Mira cómo tropezas! Siempre tan torpe'");
        
        System.out.println("Opciones: 1. Reír contigo / 2. Enojarte / 3. Demostrar habilidad / 4. Ignorar");
        
        String opt = in.nextLine().trim();
        if ("1".equals(opt)) {
            System.out.println("Te ríes de ti mismo! La burla pierde su poder.");
            System.out.println("Hermano: 'Eh... eso no era lo que esperaba'");
            recuerdosSanadores++;
        } else if ("3".equals(opt)) {
            boolean success = p.skillCheck(7);
            if (success) {
                System.out.println("Demuestras tu verdadera habilidad! Tu hermano se sorprende.");
                p.addXp(15);
                return;
            }
        }
        
        Enemigo hermano = new Enemigo("Hermano Mayor Burlesco", "Familiar", 28, 4, 3, 78);
        hermano.setFraseCombate("¡Siempre serás el pequeño!");
        boolean won = Combate.fight(p, Arrays.asList(hermano), in);
        if (won) {
            if (estadoJuego != null) {
                estadoJuego.incrementarEnemigosDerrotados();
            }
            
            p.addGold(Utils.rnd(20, 28));
            p.addItem("Confianza Adquirida");
            p.addXp(28);
        }
    }
    
    private void sala5() {
        System.out.println("\n--- Sala 5: Sombra del Pasado ---");
        System.out.println("Tu propia inseguridad personificada. Se alimenta de tus dudas.");
        System.out.println("Sombra: 'Mírate... ¿realmente crees que puedes vencer? Sabes quién ERES'");
        
        int debilidad = recuerdosSanadores * 5;
        System.out.println("Tus recuerdos sanadores debilitan a la Sombra (-" + debilidad + " HP de ventaja)");
        
        System.out.println("Opciones: 1. Enfrentar miedos / 2. Usar recuerdos felices / 3. Aceptación / 4. Combate final");
        
        Enemigo sombra = new Enemigo("Sombra del Pasado", "Familiar", 40, 8, 6, 82);
        sombra.setFraseCombate("¡Conozco todas tus debilidades!");
        
        String opt = in.nextLine().trim();
        if ("2".equals(opt) && p.hasItem("Recuerdo Feliz")) {
            System.out.println("Usas un recuerdo feliz! La Sombra retrocede dolorida.");
            p.useItem("Recuerdo Feliz");
        } else if ("3".equals(opt)) {
            System.out.println("Aceptas tus imperfecciones... La Sombra pierde poder sobre ti.");
        }
        
        System.out.println("=== COMBATE FINAL ===");
        boolean won = Combate.fight(p, Arrays.asList(sombra), in);
        
        if (won) {
            if (estadoJuego != null) {
                estadoJuego.incrementarEnemigosDerrotados();
                estadoJuego.agregarEvento("Jefe Final - Sombra del Pasado derrotado");
            }
            
            desenlaceFinal();
        }
    }
    
    private void desenlaceFinal() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("LA SOMBRA SE DISUELVE EN LUZ ✨");
        System.out.println("=".repeat(50));
        System.out.println("La Sombra del Pasado: 'Tal vez... tal vez eres más fuerte de lo que pensé'");
        System.out.println("Las voces familiares se desvanecen, convertidas en susurros de apoyo.");
        System.out.println("Has enfrentado tus demonios internos y emergiste victorioso.");
        
        p.addGold(50);
        p.addItem("Medalla de Superación Personal");
        p.addXp(60);
        
        System.out.println("\nRecompensas finales:");
        System.out.println("- 50 monedas de oro");
        System.out.println("- Medalla de Superación Personal");
        System.out.println("- 60 puntos de experiencia");
        
        System.out.println("\n¡FELICIDADES! HAS COMPLETADO 'EL VIAJE INESPERADO DE LEO' 🎉");
        System.out.println("Leo regresa al pueblo transformado, llevando consigo la sabiduría");
        System.out.println("obtenida en este viaje through los círculos de sus miedos y dudas.");
    }
}