package entities;

import items.*;
import combat.Habilidad;
import combat.Utils;

import java.util.*;

public class Jugador {
    // Atributos básicos
    private String nombre;
    private int nivel;
    private int experiencia;
    private int experienciaParaSiguienteNivel;
    
    // Atributos de combate
    private int hp;
    private int maxHp;
    private int energia;
    private int maxEnergia;
    private int ataqueBase;
    private int defensaBase;
    private int agilidad;
    private int inteligencia;
    
    // Atributos de progreso
    private GestorInventario inventario;
    private List<Habilidad> habilidades;
    private Map<String, Integer> efectos;
    
    // Flags de estado
    private boolean tieneAntorcha;
    private boolean tieneEspejoVerdad;
    private boolean tieneAmuletoPerdon;
    
    // Cheats (para desarrollo) - Solo una versión para evitar duplicación
    private boolean cheatVidaInfinita = false;
    private boolean cheatUnGolpe = false;
    private boolean cheatSaltarSalas = false;
    
    // Constructor
    public Jugador(String nombre, int maxHp, int maxEnergia, int ataqueBase) {
        this.nombre = nombre;
        this.nivel = 1;
        this.experiencia = 0;
        this.experienciaParaSiguienteNivel = 30;
        
        this.maxHp = maxHp;
        this.hp = maxHp;
        this.maxEnergia = maxEnergia;
        this.energia = maxEnergia;
        this.ataqueBase = ataqueBase;
        this.defensaBase = 5;
        this.agilidad = 10;
        this.inteligencia = 8;
        
        // Inicializar sistema de inventario mejorado
        this.inventario = new GestorInventario(20); // Capacidad inicial de 20 slots
        this.habilidades = new ArrayList<>();
        this.efectos = new HashMap<>();
        
        // Habilidades iniciales
        habilidades.add(new Habilidad("Ataque Básico", 0, 1, "Un ataque físico básico"));
        habilidades.add(new Habilidad("Concentración", 2, 0, "Aumenta tu ataque por un turno"));
        
        // Items iniciales usando el nuevo sistema
        inventario.agregarItem(Consumible.crearPocionMenor());
        inventario.agregarItem(Consumible.crearVendaje());
        inventario.agregarItem(Consumible.crearPocionMenor()); // Una poción extra
    }
    
    // Métodos de estado
    public boolean estaVivo() {
        return hp > 0;
    }

    // Wrapper en inglés
    public boolean isAlive() { return estaVivo(); }
    
    public void mostrarEstado() {
        System.out.println("\n=== ESTADO DE " + nombre.toUpperCase() + " ===");
        System.out.println("Nivel: " + nivel + " | EXP: " + experiencia + "/" + experienciaParaSiguienteNivel);
        System.out.println("HP: " + hp + "/" + maxHp);
        System.out.println("Energía: " + energia + "/" + maxEnergia);
        System.out.println("Ataque Base: " + ataqueBase + " | Defensa Base: " + defensaBase);
        System.out.println("Agilidad: " + agilidad + " | Inteligencia: " + inteligencia);
        System.out.println("Oro: " + inventario.getOro() + " monedas");
        
        // Mostrar estadísticas con equipo
        System.out.println("--- CON EQUIPO ---");
        System.out.println("Ataque Total: " + getAtaque() + " | Defensa Total: " + getDefensa());
        
        if (!efectos.isEmpty()) {
            System.out.println("Efectos activos:");
            for (Map.Entry<String, Integer> efecto : efectos.entrySet()) {
                System.out.println("  • " + efecto.getKey() + " (" + efecto.getValue() + " turnos)");
            }
        }
    }

    // Wrapper en inglés
    public void showStatus() { mostrarEstado(); }
    
    // Métodos de combate - Ahora consideran el equipo
    public int atacar() {
        int danoBase = getAtaque() + Utils.rnd(1, 3);
        if (cheatUnGolpe) {
            System.out.println("¡Cheat OneHitKill activado!");
            return 999;
        }
        return danoBase;
    }
    
    public int atacar(Habilidad habilidad) {
        if (energia < habilidad.getCostoEnergia()) {
            System.out.println("Energía insuficiente para " + habilidad.getNombre());
            return 0;
        }
        
        energia -= habilidad.getCostoEnergia();
        return habilidad.calcularDano(this);
    }
    
    public void recibirDano(int dano) {
        if (cheatVidaInfinita) {
            System.out.println("¡Cheat Vida Infinita activado! No recibes daño.");
            return;
        }
        
        int danoReal = Math.max(1, dano - getDefensa());
        hp = Math.max(0, hp - danoReal);
        System.out.println(nombre + " recibe " + danoReal + " puntos de daño!");
        
        if (hp == 0) {
            System.out.println("¡" + nombre + " ha sido derrotado!");
        }
    }
    
    public void curar(int cantidad) {
        int hpAnterior = hp;
        hp = Math.min(maxHp, hp + cantidad);
        int curado = hp - hpAnterior;
        System.out.println(nombre + " recupera " + curado + " HP.");
    }

    // Wrapper en inglés
    public void heal(int amount) { curar(amount); }
    
    public void restaurarEnergia(int cantidad) {
        int energiaAnterior = energia;
        energia = Math.min(maxEnergia, energia + cantidad);
        int restaurado = energia - energiaAnterior;
        System.out.println(nombre + " recupera " + restaurado + " puntos de energía.");
    }

    // Wrapper en inglés
    public void restoreEnergy(int amount) { restaurarEnergia(amount); }
    
    // Métodos de progresión
    public void ganarExperiencia(int exp) {
        experiencia += exp;
        System.out.println("¡Ganas " + exp + " puntos de experiencia!");
        
        while (experiencia >= experienciaParaSiguienteNivel) {
            subirNivel();
        }
    }

    // Wrapper en inglés
    public void addXp(int xp) { ganarExperiencia(xp); }
    
    private void subirNivel() {
        nivel++;
        experiencia -= experienciaParaSiguienteNivel;
        experienciaParaSiguienteNivel = nivel * 30;
        
        // Mejoras de estadísticas base
        maxHp += 10;
        hp = maxHp; // Curar completamente al subir de nivel
        maxEnergia += 2;
        energia = maxEnergia;
        ataqueBase += 2;
        defensaBase += 1;
        agilidad += 1;
        inteligencia += 1;
        
        System.out.println("\n✨ ¡" + nombre + " ha subido al nivel " + nivel + "! ✨");
        System.out.println("¡HP máximo aumentado a " + maxHp + "!");
        System.out.println("¡Energía máxima aumentada a " + maxEnergia + "!");
        System.out.println("¡Estadísticas base mejoradas!");
        
        // Aprender nueva habilidad cada 3 niveles
        if (nivel % 3 == 0) {
            aprenderNuevaHabilidad();
        }
        
        // Aumentar capacidad de inventario cada 5 niveles
        if (nivel % 5 == 0) {
            inventario.setCapacidadMaxima(inventario.getCapacidadMaxima() + 5);
            System.out.println("¡Capacidad de inventario aumentada a " + inventario.getCapacidadMaxima() + "!");
        }
    }
    
    private void aprenderNuevaHabilidad() {
        Habilidad[] habilidadesPosibles = {
            new Habilidad("Golpe Certero", 3, 2, "Ataque que ignora parte de la defensa"),
            new Habilidad("Escudo Protector", 2, 0, "Aumenta la defensa por 2 turnos"),
            new Habilidad("Ráfaga Veloz", 4, 1, "Ataque rápido que puede atacar dos veces"),
            new Habilidad("Curar Heridas", 5, 0, "Cura 20 HP al usuario"),
            new Habilidad("Foco Mental", 3, 0, "Aumenta la inteligencia temporalmente")
        };
        
        Habilidad nuevaHabilidad = habilidadesPosibles[Utils.rnd(0, habilidadesPosibles.length - 1)];
        habilidades.add(nuevaHabilidad);
        System.out.println("¡Has aprendido la habilidad: " + nuevaHabilidad.getNombre() + "!");
    }
    
    // Métodos de inventario - Delegados al GestorInventario
    public void mostrarInventario() {
        inventario.mostrarInventario();
    }
    
    public void mostrarEquipo() {
        inventario.mostrarEquipo();
    }
    
    public void mostrarEstadoCompleto() {
        mostrarEstado();
        inventario.mostrarEquipo();
    }
    
    public boolean agregarItem(Item item) {
        return inventario.agregarItem(item);
    }
    
    public boolean usarItem(String nombreItem) {
        return inventario.usarItem(nombreItem, this);
    }

    // Wrapper en inglés
    public boolean useItem(String nombreItem) { return usarItem(nombreItem); }
    
    public boolean usarItemEnCombate(String nombreItem) {
        return inventario.usarItemEnCombate(nombreItem, this);
    }
    
    public boolean equiparItem(Item item) {
        return inventario.equiparItem(item, this);
    }
    
    public boolean desequiparItem(String slot) {
        return inventario.desequiparItem(slot, this);
    }
    
    public boolean tieneItem(String nombreItem) {
        return inventario.tieneItem(nombreItem);
    }

    // Wrapper en inglés
    public boolean hasItem(String nombreItem) { return tieneItem(nombreItem); }
    
    public boolean tieneItemEspecial(String efectoUnico) {
        return inventario.tieneItemEspecial(efectoUnico);
    }
    
    public Item buscarItem(String nombreItem) {
        return inventario.buscarItem(nombreItem);
    }
    
    // Métodos de habilidades
    public void mostrarHabilidades() {
        System.out.println("\n=== HABILIDADES ===");
        for (Habilidad habilidad : habilidades) {
            System.out.println("• " + habilidad.getNombre() + 
                " (Costo: " + habilidad.getCostoEnergia() + " EN) - " + habilidad.getDescripcion());
        }
    }
    
    public Habilidad getHabilidad(String nombre) {
        return habilidades.stream()
            .filter(h -> h.getNombre().equalsIgnoreCase(nombre))
            .findFirst()
            .orElse(null);
    }
    
    // Métodos de efectos/estados
    public void aplicarEfecto(String efecto, int duracion) {
        efectos.put(efecto, duracion);
        System.out.println(nombre + " sufre de " + efecto + " por " + duracion + " turnos.");
    }
    
    public void removerEfecto(String efecto) {
        if (efectos.remove(efecto) != null) {
            System.out.println("El efecto " + efecto + " ha sido removido.");
        }
    }
    
    public void actualizarEfectos() {
        efectos.entrySet().removeIf(entry -> {
            int turnosRestantes = entry.getValue() - 1;
            if (turnosRestantes <= 0) {
                System.out.println("El efecto " + entry.getKey() + " ha terminado.");
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
    
    // Métodos de utilidad para la trama
    public boolean checkHabilidad(int dificultad) {
        int roll = Utils.rnd(1, 20) + (agilidad / 2);
        boolean exito = roll >= dificultad;
        System.out.println("Check de habilidad: " + roll + " vs " + dificultad + " -> " + (exito ? "ÉXITO" : "FALLO"));
        return exito;
    }
    
    public void agregarOro(int cantidad) {
        inventario.agregarOro(cantidad);
    }

    // Wrapper en inglés
    public void addGold(int amount) { agregarOro(amount); }

    // Añadir item por nombre (crea un ItemEspecial genérico si no existe otra fábrica)
    public void addItem(String nombreItem) {
        ItemEspecial especial = new ItemEspecial(nombreItem, "Objeto otorgado", TipoItem.FRAGMENTO, 0,
                                                 nombreItem.toLowerCase().replaceAll(" ", "_"), false, "");
        agregarItem(especial);
    }

    // Wrapper para recibir daño en inglés
    public void damage(int dano) { recibirDano(dano); }

    // Wrapper para skill checks en inglés
    public boolean skillCheck(int dificultad) { return checkHabilidad(dificultad); }
    
    public boolean gastarOro(int cantidad) {
        return inventario.gastarOro(cantidad);
    }
    
    // Getters y Setters mejorados para compatibilidad
    public String getNombre() { return nombre; }
    public int getNivel() { return nivel; }
    public int getExperiencia() { return experiencia; }
    public int getExperienciaParaSiguienteNivel() { return experienciaParaSiguienteNivel; }
    
    // Getters y Setters para HP con lógica de validación
    public int getHp() { return hp; }
    public void setHp(int hp) { 
        this.hp = Math.max(0, Math.min(hp, maxHp)); 
    }
    public int getMaxHp() { return maxHp; }
    public void setMaxHp(int maxHp) { 
        this.maxHp = maxHp; 
        if (hp > maxHp) hp = maxHp;
    }
    
    // Getters y Setters para Energía con lógica de validación
    public int getEnergia() { return energia; }
    public void setEnergia(int energia) { 
        this.energia = Math.max(0, Math.min(energia, maxEnergia)); 
    }
    public int getMaxEnergia() { return maxEnergia; }
    public void setMaxEnergia(int maxEnergia) { 
        this.maxEnergia = maxEnergia; 
        if (energia > maxEnergia) energia = maxEnergia;
    }
    
    // Métodos de conveniencia para compatibilidad con Piso
    public int getEn() { return getEnergia(); }
    public void setEn(int energia) { setEnergia(energia); }
    public int getMaxEn() { return getMaxEnergia(); }
    public void setMaxEn(int maxEnergia) { setMaxEnergia(maxEnergia); }
    
    // Getters de estadísticas que consideran el equipo
    public int getAtaque() {
        int ataqueTotal = ataqueBase;
        // En una implementación completa, aquí se sumarían bonificaciones del equipo
        return ataqueTotal;
    }
    
    public int getDefensa() {
        int defensaTotal = defensaBase;
        // En una implementación completa, aquí se sumarían bonificaciones del equipo
        return defensaTotal;
    }
    
    public int getAtaqueBase() { return ataqueBase; }
    public void setAtaqueBase(int ataque) { this.ataqueBase = ataque; }
    
    public int getDefensaBase() { return defensaBase; }
    public void setDefensaBase(int defensa) { this.defensaBase = defensa; }
    
    public int getAgilidad() { return agilidad; }
    public void setAgilidad(int agilidad) { this.agilidad = agilidad; }
    
    public int getInteligencia() { return inteligencia; }
    public void setInteligencia(int inteligencia) { this.inteligencia = inteligencia; }
    
    public int getOro() { return inventario.getOro(); }
    
    public GestorInventario getInventario() { return inventario; }
    
    // Getters y Setters para cheats unificados
    public boolean isCheatVidaInfinita() { return cheatVidaInfinita; }
    public void setCheatVidaInfinita(boolean cheatVidaInfinita) { 
        this.cheatVidaInfinita = cheatVidaInfinita; 
    }
    
    public boolean isCheatUnGolpe() { return cheatUnGolpe; }
    public void setCheatUnGolpe(boolean cheatUnGolpe) { 
        this.cheatUnGolpe = cheatUnGolpe; 
    }
    
    public boolean isCheatSaltarSalas() { return cheatSaltarSalas; }
    public void setCheatSaltarSalas(boolean cheatSaltarSalas) { 
        this.cheatSaltarSalas = cheatSaltarSalas; 
    }
    
    // Wrappers en inglés para cheats (para compatibilidad con código existente)
    public boolean isCheatInfiniteHealth() { return isCheatVidaInfinita(); }
    public void setCheatInfiniteHealth(boolean value) { setCheatVidaInfinita(value); }
    
    public boolean isCheatOneHitKill() { return isCheatUnGolpe(); }
    public void setCheatOneHitKill(boolean value) { setCheatUnGolpe(value); }
    
    public boolean isCheatSkipRooms() { return isCheatSaltarSalas(); }
    public void setCheatSkipRooms(boolean value) { setCheatSaltarSalas(value); }
    
    public boolean isTieneAntorcha() { return tieneAntorcha; }
    public void setTieneAntorcha(boolean tiene) { 
        this.tieneAntorcha = tiene;
        if (tiene) {
            System.out.println("¡Ahora tienes una fuente de luz! Puedes ver en la oscuridad.");
        }
    }
    
    public boolean isTieneEspejoVerdad() { return tieneEspejoVerdad; }
    public void setTieneEspejoVerdad(boolean tiene) { 
        this.tieneEspejoVerdad = tiene;
        if (tiene) {
            System.out.println("¡El espejo de la verdad te ayuda a discernir la realidad!");
        }
    }
    
    public boolean isTieneAmuletoPerdon() { return tieneAmuletoPerdon; }
    public void setTieneAmuletoPerdon(boolean tiene) { 
        this.tieneAmuletoPerdon = tiene;
        if (tiene) {
            System.out.println("¡El amuleto del perdón te protege de ataques emocionales!");
        }
    }
    
    // Métodos específicos para la trama del juego
    public boolean puedeVerEnOscuridad() {
        return tieneAntorcha || tieneItem("Antorcha") || tieneItem("Antorcha de la Curiosidad");
    }
    
    public boolean puedeRevelarVerdad() {
        return tieneEspejoVerdad || tieneItem("Espejo") || tieneItem("Espejo de la Verdad");
    }
    
    public boolean tieneProteccionEmocional() {
        return tieneAmuletoPerdon || tieneItem("Amuleto") || tieneItem("Amuleto del Perdón");
    }
    
    // Método para recibir objetos especiales de la trama
    public void recibirItemTrama(String tipoItem) {
        switch (tipoItem.toLowerCase()) {
            case "antorcha":
                ItemEspecial antorcha = ItemEspecial.crearAntorchaCuriosidad();
                if (inventario.agregarItem(antorcha)) {
                    setTieneAntorcha(true);
                }
                break;
                
            case "espejo":
                ItemEspecial espejo = ItemEspecial.crearEspejoVerdad();
                if (inventario.agregarItem(espejo)) {
                    setTieneEspejoVerdad(true);
                }
                break;
                
            case "amuleto":
                Armadura amuleto = Armadura.crearAmuletoPerdon();
                if (inventario.agregarItem(amuleto)) {
                    setTieneAmuletoPerdon(true);
                }
                break;
                
            case "llave_progreso":
                ItemEspecial llave = ItemEspecial.crearLlaveProgreso();
                inventario.agregarItem(llave);
                break;
                
            default:
                System.out.println("Item de trama desconocido: " + tipoItem);
                break;
        }
    }
    
    // Método para verificar el progreso de la trama basado en items
    public boolean tieneItemsParaProgresar(int piso) {
        switch (piso) {
            case 1:
                return tieneAntorcha || tieneItem("Antorcha de la Curiosidad");
            case 2:
                return tieneEspejoVerdad || tieneItem("Espejo de la Verdad");
            case 3:
                return tieneAmuletoPerdon || tieneItem("Amuleto del Perdón");
            default:
                return true;
        }
    }
    
    // Método para restaurar completamente al jugador (útil entre pisos)
    public void restaurarCompletamente() {
        hp = maxHp;
        energia = maxEnergia;
        efectos.clear();
        System.out.println(nombre + " ha sido restaurado completamente.");
    }
    
    // Método para verificar si puede descansar (útil para la mecánica de descanso)
    public boolean puedeDescansar() {
        return hp < maxHp || energia < maxEnergia;
    }
}