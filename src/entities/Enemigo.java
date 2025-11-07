package entities;

import combat.CombatUtils;
import combat.Habilidad;
import combat.Utils;

import java.util.*;

public class Enemigo {
    private String nombre;
    private String tipo;
    private int hp;
    private int maxHp;
    private int ataque;
    private int defensa;
    private int agilidad;
    private int experienciaOtorgada;
    private List<Habilidad> habilidades;
    private Map<String, Integer> efectos;
    private String fraseCombate;
    
    // Flags especiales para mecánicas de la trama
    private boolean debilAntorcha;
    private boolean debilEspejo;
    private boolean creaIlusiones;
    private boolean aplicaRalentizacion;
    
    public Enemigo(String nombre, String tipo, int maxHp, int ataque, int defensa, int experienciaOtorgada) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.maxHp = maxHp;
        this.hp = maxHp;
        this.ataque = ataque;
        this.defensa = defensa;
        this.agilidad = Utils.rnd(5, 15);
        this.experienciaOtorgada = experienciaOtorgada;
        this.habilidades = new ArrayList<>();
        this.efectos = new HashMap<>();
        this.fraseCombate = "¡Prepárate para luchar!";
        
        // Habilidades básicas según tipo
        inicializarHabilidadesPorTipo();
    }
    
    private void inicializarHabilidadesPorTipo() {
        switch (tipo.toLowerCase()) {
            case "planta":
                habilidades.add(new Habilidad("Esporas Ralentizadoras", 0, 1, "Ralentiza al objetivo"));
                debilAntorcha = true;
                aplicaRalentizacion = true;
                fraseCombate = "¡Las esporas te envolverán!";
                break;
                
            case "espíritu":
                habilidades.add(new Habilidad("Ataque Fantasma", 0, 2, "Ignora parte de la defensa"));
                creaIlusiones = true;
                fraseCombate = "¡Los olvidados se alzan!";
                break;
                
            case "hechicero":
                habilidades.add(new Habilidad("Hechizo Confuso", 2, 1, "Desorienta al objetivo"));
                debilEspejo = true;
                fraseCombate = "¡La magia es mi aliada!";
                break;
                
            case "familiar":
                habilidades.add(new Habilidad("Crítica Destructiva", 0, 2, "Ataque que reduce moral"));
                fraseCombate = "¡Siempre supimos que fallarías!";
                break;
                
            default:
                habilidades.add(new Habilidad("Ataque Básico", 0, 1, "Ataque físico básico"));
                break;
        }
    }
    
    // Métodos de estado
    public boolean estaVivo() {
        return hp > 0;
    }
    
    public void mostrarInfo() {
        System.out.println("\n=== " + nombre.toUpperCase() + " ===");
        System.out.println("Tipo: " + tipo);
        System.out.println("HP: " + hp + "/" + maxHp);
        System.out.println("Ataque: " + ataque + " | Defensa: " + defensa);
        
        if (!habilidades.isEmpty()) {
            System.out.println("Habilidades:");
            for (Habilidad habilidad : habilidades) {
                System.out.println("  • " + habilidad.getNombre());
            }
        }
    }
    
    // Métodos de combate
    public int atacar() {
        int danoBase = ataque + Utils.rnd(1, 3);
        
        // Modificadores por efectos
        if (tieneEfecto("Debilitado")) {
            danoBase = (int)(danoBase * 0.7);
        }
        if (tieneEfecto("Fortalecido")) {
            danoBase = (int)(danoBase * 1.3);
        }
        
        return danoBase;
    }
    
    public int usarHabilidad() {
        if (habilidades.isEmpty() || Utils.rnd(1, 10) <= 6) {
            return atacar(); // 60% de probabilidad de ataque básico
        }
        
        Habilidad habilidad = habilidades.get(Utils.rnd(0, habilidades.size() - 1));
        System.out.println(nombre + " usa " + habilidad.getNombre() + "!");
        return habilidad.calcularDano(this);
    }
    
    public void recibirDano(int dano) {
        // Debilidades especiales
        if (debilAntorcha && CombatUtils.jugadorTieneAntorcha()) {
            dano = (int)(dano * 1.5);
            System.out.println("¡El enemigo es débil a la antorcha! Daño aumentado.");
        }
        
        if (debilEspejo && CombatUtils.jugadorTieneEspejo()) {
            dano = (int)(dano * 1.5);
            System.out.println("¡El espejo de la verdad revela su debilidad! Daño aumentado.");
        }
        
        int danoReal = Math.max(1, dano - defensa);
        hp = Math.max(0, hp - danoReal);
        System.out.println(nombre + " recibe " + danoReal + " puntos de daño!");
        
        if (hp == 0) {
            System.out.println("¡" + nombre + " ha sido derrotado!");
        }
    }
    
    public void aplicarEfectoCombate(Jugador jugador) {
        if (aplicaRalentizacion && Utils.rnd(1, 10) <= 3) {
            jugador.aplicarEfecto("Ralentizado", 2);
            System.out.println("¡" + nombre + " te ha ralentizado!");
        }
        
        if (creaIlusiones && hp < maxHp * 0.5 && Utils.rnd(1, 10) <= 4) {
            System.out.println("¡" + nombre + " crea ilusiones para confundirte!");
            jugador.aplicarEfecto("Confundido", 1);
        }
    }
    
    // Métodos de efectos/estados
    public void aplicarEfecto(String efecto, int duracion) {
        efectos.put(efecto, duracion);
        System.out.println(nombre + " sufre de " + efecto);
    }
    
    public void actualizarEfectos() {
        efectos.entrySet().removeIf(entry -> {
            int turnosRestantes = entry.getValue() - 1;
            if (turnosRestantes <= 0) {
                return true;
            } else {
                entry.setValue(turnosRestantes);
                return false;
            }
        });
    }
    
    public boolean tieneEfecto(String efecto) {
        return efectos.containsKey(efecto);
    }
    
    // Getters y Setters
    public String getNombre() { return nombre; }
    public String getTipo() { return tipo; }
    public int getHp() { return hp; }
    public int getMaxHp() { return maxHp; }
    public int getAtaque() { return ataque; }
    public int getDefensa() { return defensa; }
    public int getAgilidad() { return agilidad; }
    public int getExperienciaOtorgada() { return experienciaOtorgada; }
    public String getFraseCombate() { return fraseCombate; }
    public void setFraseCombate(String frase) { this.fraseCombate = frase; }
    public boolean isDebilAntorcha() { return debilAntorcha; }
    public boolean isDebilEspejo() { return debilEspejo; }
    public boolean isCreaIlusiones() { return creaIlusiones; }
    
    // Método para enemigos especiales de la trama
    public static Enemigo crearEnemigoEspecial(String tipoEspecial) {
        switch (tipoEspecial) {
            case "FANTASMA_PROCRASTINACION":
                Enemigo fantasma = new Enemigo("Fantasma de la Procrastinación", "Espíritu", 35, 8, 4, 40);
                fantasma.setFraseCombate("¿Tan rápido? Podrías dejarlo para después...");
                fantasma.creaIlusiones = true;
                fantasma.habilidades.add(new Habilidad("Clones Ilusorios", 3, 2, "Crea copias que distraen"));
                return fantasma;
                
            case "ESPEJO_HECHICERO":
                Enemigo espejo = new Enemigo("Espejo Hechicero", "Hechicero", 45, 10, 6, 50);
                espejo.setFraseCombate("¿Puedes confiar en lo que ves?");
                espejo.debilEspejo = true;
                espejo.habilidades.add(new Habilidad("Reflejo Engañoso", 4, 2, "Refleja parte del daño"));
                return espejo;
                
            case "SOMBRA_PASADO":
                Enemigo sombra = new Enemigo("Sombra del Pasado", "Familiar", 60, 12, 8, 70);
                sombra.setFraseCombate("Conozco todas tus debilidades...");
                sombra.habilidades.add(new Habilidad("Ataque Inseguro", 5, 3, "Daño basado en inseguridades"));
                sombra.habilidades.add(new Habilidad("Duda Paralizante", 3, 2, "Inmoviliza con dudas"));
                return sombra;
                
            default:
                return new Enemigo("Enemigo Genérico", "Normal", 20, 5, 2, 15);
        }
    }
}