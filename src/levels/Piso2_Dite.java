package levels;

import entities.Jugador;
import entities.Enemigo;
import combat.Combate;;
import combat.Utils;
import java.util.*;

public class Piso2_Dite extends Piso {
    private boolean puzzleResuelto = false;
    
    public Piso2_Dite(Jugador p, Scanner in) {
        super(p, in);
    }
    
    @Override
    public void play() {
        System.out.println("\n=== PISO 2: CIUDAD DE LAS SOMBRAS ENGAÑOSAS ===");
        System.out.println("Una ciudad gótica donde los espejos mienten y las sombras ocultan la verdad.");
        
        if (!preRoom("Sala 1: Fanático de Cristal")) sala1();
        if (!p.isAlive()) return;
        
        if (!preRoom("Sala 2: Espejos Engañosos")) sala2();
        if (!p.isAlive()) return;
        
        if (!preRoom("Sala 3: Ardilla Acaparadora")) sala3();
        if (!p.isAlive()) return;
        
        if (!preRoom("Sala 4: Serpiente de la Envidia")) sala4();
        if (!p.isAlive()) return;
        
        if (!preRoom("Sala 5: Espejo Hechicero")) sala5();
    }
    
    private void sala1() {
        System.out.println("\n--- Sala 1: Fanático de Cristal ---");
        System.out.println("Un ser de cristal que lanza hechizos rígidos y predecibles.");
        System.out.println("Fanático: 'Solo hay UNA verdad! La mía!'");
        
        System.out.println("Opciones: 1. Atacar frontal / 2. Buscar patrones / 3. Romper cristal / 4. Usar magia");
        
        String opt = in.nextLine().trim();
        if ("2".equals(opt)) {
            boolean success = p.skillCheck(6);
            if (success) {
                System.out.println("Descubres su patrón de ataque! Atacas en su punto ciego.");
                Enemy fanatico = new Enemy("Fanático de Cristal", 28, 5, 4, 70, "¡Imposible!");
                fanatico.hp -= 8;
                boolean won = Combat.fight(p, Arrays.asList(fanatico), in);
                if (won) recompensaSala1();
                return;
            }
        } else if ("3".equals(opt)) {
            System.out.println("Golpeas fuertemente el cristal...");
            if (Utils.rnd(1, 10) <= 4) {
                System.out.println("El cristal se agrieta! Está debilitado.");
                Enemy fanatico = new Enemy("Fanático de Cristal", 20, 5, 2, 70, "¡Mi forma perfecta!");
                boolean won = Combat.fight(p, Arrays.asList(fanatico), in);
                if (won) recompensaSala1();
                return;
            }
        }
        
        Enemigo fanatico = new Enemigo("Fanático de Cristal", 28, 5, 4, 70, "¡La verdad duele!");
        boolean won = Combate.fight(p, Arrays.asList(fanatico), in);
        if (won) recompensaSala1();
    }
    
    private void recompensaSala1() {
        p.addGold(Utils.rnd(12, 18));
        p.addItem("Fragmento de Cristal");
        p.addXp(20);
        System.out.println("Pista: 'Los espejos muestran lo que quieren ver, no lo que es'");
    }
    
    private void sala2() {
        System.out.println("\n--- Sala 2: Espejos Engañosos ---");
        System.out.println("Un corredor lleno de espejos que distorsionan la realidad.");
        
        if (!puzzleResuelto) {
            System.out.println("Debes resolver el puzzle de los espejos para continuar.");
            resolverPuzzleEspejos();
        }
        
        if (puzzleResuelto) {
            System.out.println("Los espejos se alinean y revelan el camino verdadero.");
            p.addItem("Espejo de la Verdad");
            p.addXp(15);
        } else {
            System.out.println("Los espejos te confunden y pierdes 5 HP.");
            p.damage(5);
        }
    }
    
    private void resolverPuzzleEspejos() {
        System.out.println("\n=== PUZZLE DE LOS ESPEJOS ===");
        System.out.println("Cuatro espejos muestran símbolos:");
        System.out.println("Espejo 1: ◯ (Círculo) - Inicio");
        System.out.println("Espejo 2: △ (Triángulo) - Camino"); 
        System.out.println("Espejo 3: □ (Cuadrado) - Verdad");
        System.out.println("Espejo 4: ☆ (Estrella) - Destino");
        System.out.println("\n¿Cuál es el orden correcto? (ej: 1 2 3 4)");
        
        String respuesta = in.nextLine().trim();
        if (respuesta.equals("3 2 1 4") || respuesta.equals("3214")) {
            System.out.println("✅ Correcto! Los espejos se alinean en la secuencia: Verdad -> Camino -> Inicio -> Destino");
            puzzleResuelto = true;
        } else {
            System.out.println("❌ Secuencia incorrecta. Los espejos se distorsionan más.");
        }
    }
    
    private void sala3() {
        System.out.println("\n--- Sala 3: Ardilla Acaparadora ---");
        System.out.println("Una ardilla nerviosa que roba objetos brillantes.");
        System.out.println("Ardilla: '¡Mío! ¡Todo mío!'");
        
        if (p.hasItem("Nuez Dorada")) {
            System.out.println("Usas la Nuez Dorada para atraer a la ardilla...");
            System.out.println("La ardilla suelta tu objeto robado y huye.");
            p.addItem("Objeto Recuperado");
            p.addXp(10);
            return;
        }
        
        System.out.println("Opciones: 1. Perseguir / 2. Trampa / 3. Negociar / 4. Rendirse");
        
        String opt = in.nextLine().trim();
        if ("2".equals(opt)) {
            boolean success = p.skillCheck(5);
            if (success) {
                System.out.println("Armas una trampa ingeniosa y atrapas a la ardilla!");
                p.addGold(15);
                p.addItem("Nuez Dorada");
                p.addXp(12);
                return;
            }
        } else if ("3".equals(opt)) {
            System.out.println("Intentas negociar con la ardilla...");
            System.out.println("Ofreces: A. 5 monedas / B. Una poción / C. Un hechizo");
            String oferta = in.nextLine().trim().toUpperCase();
            if ("A".equals(oferta) && p.gold >= 5) {
                System.out.println("La ardilla acepta las monedas y te devuelve tu objeto.");
                p.gold -= 5;
                p.addItem("Objeto Recuperado");
                p.addXp(8);
                return;
            }
        }
        
        System.out.println("La ardilla escapa con tu objeto! Pierdes un item aleatorio.");
        // Implementar pérdida de item aleatorio
        p.addXp(5);
    }
    
    private void sala4() {
        System.out.println("\n--- Sala 4: Serpiente de la Envidia ---");
        System.out.println("Una serpiente que escupe veneno y crea ilusiones de lo que deseas.");
        System.out.println("Serpiente: '¿Por qué ellos tienen lo que tú no?'");
        
        System.out.println("Opciones: 1. Combatir / 2. Resistir veneno / 3. Usar antídoto / 4. Ignorar ilusiones");
        
        String opt = in.nextLine().trim();
        boolean resistencia = false;
        
        if ("2".equals(opt)) {
            resistencia = p.skillCheck(7);
            if (resistencia) {
                System.out.println("Resistes el veneno! La serpiente está sorprendida.");
            }
        } else if ("3".equals(opt) && p.hasItem("Antídoto")) {
            System.out.println("Usas el antídoto y eres inmune al veneno.");
            resistencia = true;
            p.useItem("Antídoto");
        }
        
        Enemigo serpiente = new Enemigo("Serpiente de la Envidia", 32, 6, 3, 75, "¡Quiero lo que tienes!");
        if (resistencia) {
            serpiente.damageModifier = 0.5f; // Mitad de daño por veneno
        }
        
        boolean won = Combat.fight(p, Arrays.asList(serpiente), in);
        if (won) {
            p.addGold(Utils.rnd(15, 22));
            p.addItem("Veneno Purificado");
            p.addXp(25);
        }
    }
    
    private void sala5() {
        System.out.println("\n--- Sala 5: Espejo Hechicero ---");
        System.out.println("Un hechicero que se esconde detrás de espejos y proyecta tus propios miedos.");
        System.out.println("Espejo Hechicero: 'Mírate... ¿realmente puedes confiar en lo que ves?'");
        
        if (p.hasItem("Espejo de la Verdad")) {
            System.out.println("Usas el Espejo de la Verdad para romper sus ilusiones!");
            System.out.println("El hechicero queda expuesto y vulnerable.");
        }
        
        System.out.println("Opciones: 1. Romper espejos / 2. Atacar reflejos / 3. Habilidad verdadera / 4. Cerrar ojos");
        
        Enemigo hechicero = new Enemigo("Espejo Hechicero", 35, 7, 5, 80, "¡Tu reflejo te traicionará!");
        
        String opt = in.nextLine().trim();
        if ("1".equals(opt)) {
            System.out.println("Rompes los espejos alrededor...");
            if (Utils.rnd(1, 10) <= 6) {
                System.out.println("El hechicero pierde su escondite! Está aturdido.");
                hechicero.hp -= 8;
            }
        } else if ("4".equals(opt)) {
            System.out.println("Cierras los ojos y confías en otros sentidos...");
            boolean success = p.skillCheck(8);
            if (success) {
                System.out.println("Localizas al hechicero real por su respiración! Atacas con precisión.");
                hechicero.hp -= 12;
            }
        }
        
        boolean won = Combate.fight(p, Arrays.asList(hechicero), in);
        if (won) {
            System.out.println("El Espejo Hechicero se desvanece en mil fragmentos!");
            p.addGold(Utils.rnd(25, 35));
            p.addItem("Fragmento de Espejo Mágico");
            p.addXp(40);
            System.out.println("Un portal oscuro se abre hacia el palacio helado...");
        }
    }
}