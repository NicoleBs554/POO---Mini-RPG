package items;

import entities.Jugador;

public class Consumible extends Item {
    private int saludRestaurada;
    private int energiaRestaurada;
    private String efectoEstado;
    private int duracionEfecto;
    private boolean instantaneo;
    
    public Consumible(String nombre, String descripcion, int valor, 
                     int saludRestaurada, int energiaRestaurada, 
                     String efectoEstado, int duracionEfecto, boolean instantaneo) {
        super(nombre, descripcion, TipoItem.POCIÓN, valor, true, 10, false, true);
        this.saludRestaurada = saludRestaurada;
        this.energiaRestaurada = energiaRestaurada;
        this.efectoEstado = efectoEstado;
        this.duracionEfecto = duracionEfecto;
        this.instantaneo = instantaneo;
    }
    
    @Override
    public void usar(Jugador jugador) {
        System.out.println("Usas " + nombre + ".");
        
        // Restaurar salud
        if (saludRestaurada > 0) {
            int saludAnterior = jugador.getHp();
            jugador.curar(saludRestaurada);
            int saludRestauradaReal = jugador.getHp() - saludAnterior;
            System.out.println("Restauras " + saludRestauradaReal + " puntos de salud.");
        }
        
        // Restaurar energía
        if (energiaRestaurada > 0) {
            int energiaAnterior = jugador.getEnergia();
            jugador.restaurarEnergia(energiaRestaurada);
            int energiaRestauradaReal = jugador.getEnergia() - energiaAnterior;
            System.out.println("Restauras " + energiaRestauradaReal + " puntos de energía.");
        }
        
        // Aplicar efectos de estado
        if (efectoEstado != null && !efectoEstado.isEmpty()) {
            aplicarEfectoEstado(jugador);
        }
        
        // Reducir cantidad
        this.cantidad--;
        
        if (instantaneo) {
            System.out.println("El efecto es instantáneo.");
        } else {
            System.out.println("El efecto durará " + duracionEfecto + " turnos.");
        }
    }
    
    private void aplicarEfectoEstado(Jugador jugador) {
        switch (efectoEstado.toLowerCase()) {
            case "fortalecimiento":
                jugador.aplicarEfecto("Fortalecido", duracionEfecto);
                System.out.println("Te sientes más fuerte! Ataque aumentado.");
                break;
            case "proteccion":
                jugador.aplicarEfecto("Protegido", duracionEfecto);
                System.out.println("Te sientes protegido! Defensa aumentada.");
                break;
            case "agilidad":
                jugador.aplicarEfecto("Ágil", duracionEfecto);
                System.out.println("Te sientes más ágil! Velocidad aumentada.");
                break;
            case "curacion_veneno":
                if (jugador.tieneEfecto("Envenenado")) {
                    jugador.removerEfecto("Envenenado");
                    System.out.println("El veneno ha sido curado.");
                }
                break;
            case "curacion_ralentizacion":
                if (jugador.tieneEfecto("Ralentizado")) {
                    jugador.removerEfecto("Ralentizado");
                    System.out.println("La ralentización ha sido curada.");
                }
                break;
            case "vision_nocturna":
                jugador.aplicarEfecto("Visión Nocturna", duracionEfecto);
                System.out.println("Puedes ver en la oscuridad!");
                break;
        }
    }
    
    // Getters específicos
    public int getSaludRestaurada() { return saludRestaurada; }
    public int getEnergiaRestaurada() { return energiaRestaurada; }
    public String getEfectoEstado() { return efectoEstado; }
    public int getDuracionEfecto() { return duracionEfecto; }
    public boolean isInstantaneo() { return instantaneo; }
    
    // Métodos estáticos para crear consumibles predefinidos
    public static Consumible crearPocionMenor() {
        return new Consumible("Poción Menor", "Restaura una pequeña cantidad de salud", 
                             15, 15, 0, "", 0, true);
    }
    
    public static Consumible crearPocionMedia() {
        return new Consumible("Poción Media", "Restaura una cantidad moderada de salud", 
                             30, 30, 0, "", 0, true);
    }
    
    public static Consumible crearElixirEnergia() {
        return new Consumible("Elixir de Energía", "Restaura puntos de energía", 
                             25, 0, 20, "", 0, true);
    }
    
    public static Consumible crearAntidoto() {
        return new Consumible("Antídoto", "Cura efectos de veneno", 
                             20, 0, 0, "curacion_veneno", 0, true);
    }
    
    public static Consumible crearVendaje() {
        return new Consumible("Vendaje", "Curación básica para heridas", 
                             10, 10, 0, "", 0, true);
    }
    
    public static Consumible crearPocionFortalecimiento() {
        return new Consumible("Poción de Fortalecimiento", "Aumenta el ataque temporalmente", 
                             40, 0, 0, "fortalecimiento", 3, false);
    }
}