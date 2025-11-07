package combat;

import entities.Jugador;
import entities.Enemigo;
import combat.Habilidad;
import java.util.*;

public class Combate {
    private Jugador jugador;
    private List<Enemigo> enemigos;
    private Scanner scanner;
    private boolean combateActivo;
    private int turno;
    
    public Combate(Jugador jugador, List<Enemigo> enemigos, Scanner scanner) {
        this.jugador = jugador;
        this.enemigos = new ArrayList<>(enemigos);
        this.scanner = scanner;
        this.combateActivo = true;
        this.turno = 1;
    }
    
    public boolean iniciarCombate() {
        System.out.println("\n⚔️ ¡COMBATE INICIADO! ⚔️");
        mostrarInformacionCombate();
        
        for (Enemigo enemigo : enemigos) {
            System.out.println("¡" + enemigo.getNombre() + " aparece! " + enemigo.getFraseCombate());
        }
        
        while (combateActivo && jugador.estaVivo() && hayEnemigosVivos()) {
            System.out.println("\n--- TURNO " + turno + " ---");
            ejecutarTurnoJugador();
            
            if (!hayEnemigosVivos()) break;
            
            ejecutarTurnoEnemigos();
            
            // Actualizar efectos
            jugador.actualizarEfectos();
            for (Enemigo enemigo : enemigos) {
                if (enemigo.estaVivo()) {
                    enemigo.actualizarEfectos();
                }
            }
            
            turno++;
        }
        
        return determinarResultado();
    }
    
    private void ejecutarTurnoJugador() {
        System.out.println("\n--- TU TURNO ---");
        jugador.mostrarEstado();
        
        boolean accionCompletada = false;
        while (!accionCompletada) {
            System.out.println("\nOpciones:");
            System.out.println("1. Atacar");
            System.out.println("2. Usar habilidad");
            System.out.println("3. Usar objeto");
            System.out.println("4. Examinar enemigos");
            System.out.println("5. Intentar huir");
            System.out.print("Elige una acción: ");
            
            String opcion = scanner.nextLine().trim();
            
            switch (opcion) {
                case "1":
                    accionCompletada = atacar();
                    break;
                case "2":
                    accionCompletada = usarHabilidad();
                    break;
                case "3":
                    accionCompletada = usarObjeto();
                    break;
                case "4":
                    examinarEnemigos();
                    break;
                case "5":
                    accionCompletada = intentarHuir();
                    break;
                default:
                    System.out.println("Opción no válida.");
                    break;
            }
        }
    }
    
    private boolean atacar() {
        Enemigo objetivo = seleccionarObjetivo();
        if (objetivo == null) return false;
        
        int dano = jugador.atacar();
        System.out.println("¡Atacas a " + objetivo.getNombre() + "!");
        objetivo.recibirDano(dano);
        
        return true;
    }
    
    private boolean usarHabilidad() {
        if (jugador.getEnergia() <= 0) {
            System.out.println("No tienes energía suficiente para usar habilidades.");
            return false;
        }
        
        System.out.println("\nTus habilidades:");
        // Mostrar habilidades del jugador
        
        System.out.print("Escribe el nombre de la habilidad a usar: ");
        String nombreHabilidad = scanner.nextLine().trim();
        
        Habilidad habilidad = jugador.getHabilidad(nombreHabilidad);
        if (habilidad == null) {
            System.out.println("No conoces esa habilidad.");
            return false;
        }
        
        if (jugador.getEnergia() < habilidad.getCostoEnergia()) {
            System.out.println("Energía insuficiente para " + habilidad.getNombre());
            return false;
        }
        
        Enemigo objetivo = seleccionarObjetivo();
        if (objetivo == null) return false;
        
        int dano = jugador.atacar(habilidad);
        System.out.println("¡Usas " + habilidad.getNombre() + " contra " + objetivo.getNombre() + "!");
        objetivo.recibirDano(dano);
        
        // Efectos especiales de habilidades
        aplicarEfectosHabilidad(habilidad, objetivo);
        
        return true;
    }
    
    private void aplicarEfectosHabilidad(Habilidad habilidad, Enemigo objetivo) {
        switch (habilidad.getNombre().toLowerCase()) {
            case "esporas ralentizadoras":
                objetivo.aplicarEfecto("Ralentizado", 2);
                break;
            case "hechizo confuso":
                objetivo.aplicarEfecto("Confundido", 1);
                break;
            case "escudo protector":
                jugador.aplicarEfecto("Defensa Aumentada", 2);
                break;
        }
    }
    
    private boolean usarObjeto() {
        jugador.mostrarInventario();
        System.out.print("Escribe el nombre del objeto a usar (o 'cancelar'): ");
        String nombreObjeto = scanner.nextLine().trim();
        
        if (nombreObjeto.equalsIgnoreCase("cancelar")) {
            return false;
        }
        
        return jugador.usarItem(nombreObjeto);
    }
    
    private void examinarEnemigos() {
        System.out.println("\n--- EXAMINAR ENEMIGOS ---");
        for (int i = 0; i < enemigos.size(); i++) {
            Enemigo enemigo = enemigos.get(i);
            if (enemigo.estaVivo()) {
                System.out.println((i + 1) + ". " + enemigo.getNombre() + 
                    " - HP: " + enemigo.getHp() + "/" + enemigo.getMaxHp());
                enemigo.mostrarInfo();
            }
        }
    }
    
    private boolean intentarHuir() {
        int probabilidadHuida = 40 + (jugador.getAgilidad() / 2);
        if (Utils.rnd(1, 100) <= probabilidadHuida) {
            System.out.println("¡Logras huir del combate!");
            combateActivo = false;
            return true;
        } else {
            System.out.println("¡No puedes huir! Los enemigos te bloquean el paso.");
            return true; // El turno termina aunque falle la huida
        }
    }
    
    private Enemigo seleccionarObjetivo() {
        List<Enemigo> enemigosVivos = getEnemigosVivos();
        if (enemigosVivos.isEmpty()) {
            System.out.println("No hay enemigos vivos.");
            return null;
        }
        
        System.out.println("\nSelecciona un objetivo:");
        for (int i = 0; i < enemigosVivos.size(); i++) {
            Enemigo enemigo = enemigosVivos.get(i);
            System.out.println((i + 1) + ". " + enemigo.getNombre() + 
                " - HP: " + enemigo.getHp() + "/" + enemigo.getMaxHp());
        }
        
        System.out.print("Elige un objetivo (1-" + enemigosVivos.size() + "): ");
        try {
            int eleccion = Integer.parseInt(scanner.nextLine().trim()) - 1;
            if (eleccion >= 0 && eleccion < enemigosVivos.size()) {
                return enemigosVivos.get(eleccion);
            }
        } catch (NumberFormatException e) {
            // Fall through to default
        }
        
        System.out.println("Selección inválida. Atacando al primer enemigo.");
        return enemigosVivos.get(0);
    }
    
    private void ejecutarTurnoEnemigos() {
        System.out.println("\n--- TURNO DE LOS ENEMIGOS ---");
        
        for (Enemigo enemigo : getEnemigosVivos()) {
            if (!jugador.estaVivo()) break;
            
            System.out.println("\n" + enemigo.getNombre() + " ataca!");
            
            // Aplicar efectos de combate especiales
            enemigo.aplicarEfectoCombate(jugador);
            
            // Decidir acción
            int dano = enemigo.usarHabilidad();
            jugador.recibirDano(dano);
            
            // Pequeña pausa para legibilidad
            try { Thread.sleep(1000); } catch (InterruptedException e) {}
        }
    }
    
    private boolean determinarResultado() {
        if (!jugador.estaVivo()) {
            System.out.println("\n💀 Has sido derrotado...");
            return false;
        }
        
        if (!hayEnemigosVivos()) {
            System.out.println("\n🎉 ¡VICTORIA! Has derrotado a todos los enemigos.");
            
            // Recompensas
            int experienciaTotal = 0;
            int oroTotal = 0;
            
            for (Enemigo enemigo : enemigos) {
                experienciaTotal += enemigo.getExperienciaOtorgada();
                oroTotal += Utils.rnd(5, 15);
            }
            
            jugador.ganarExperiencia(experienciaTotal);
            jugador.agregarOro(oroTotal);
            
            System.out.println("Recompensas:");
            System.out.println("• " + experienciaTotal + " puntos de experiencia");
            System.out.println("• " + oroTotal + " monedas de oro");
            
            // Posibilidad de loot adicional
            if (Utils.rnd(1, 10) <= 3) {
                String[] lootPosible = {"Poción Menor", "Vendaje", "Elixir Energético"};
                String loot = lootPosible[Utils.rnd(0, lootPosible.length - 1)];
                System.out.println("• " + loot);
                // jugador.agregarItem(loot);
            }
            
            return true;
        }
        
        return false;
    }
    
    private List<Enemigo> getEnemigosVivos() {
        List<Enemigo> vivos = new ArrayList<>();
        for (Enemigo enemigo : enemigos) {
            if (enemigo.estaVivo()) {
                vivos.add(enemigo);
            }
        }
        return vivos;
    }
    
    private boolean hayEnemigosVivos() {
        return !getEnemigosVivos().isEmpty();
    }
    
    private void mostrarInformacionCombate() {
        System.out.println("Participantes:");
        System.out.println("• " + jugador.getNombre() + " (Tú)");
        for (Enemigo enemigo : enemigos) {
            System.out.println("• " + enemigo.getNombre());
        }
    }
    
    // Método estático para combates rápidos (como en Piso1.java)
    public static boolean combateRapido(Jugador jugador, List<Enemigo> enemigos, Scanner scanner) {
        Combate combate = new Combate(jugador, enemigos, scanner);
        return combate.iniciarCombate();
    }
}