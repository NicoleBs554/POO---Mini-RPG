package items;

import entities.Jugador;
import combat.CombatUtils;

public class Arma extends Item {
    private int danoBase;
    private int danoExtra;
    private int precision;
    private int velocidadAtaque;
    private String efectoEspecial;
    private int durabilidad;
    private int durabilidadMaxima;
    
    public Arma(String nombre, String descripcion, int valor, int danoBase, 
                int precision, int velocidadAtaque, String efectoEspecial, int durabilidadMaxima) {
        super(nombre, descripcion, TipoItem.ARMA_MELÉE, valor, false, 1, true, false);
        this.danoBase = danoBase;
        this.danoExtra = 0;
        this.precision = precision;
        this.velocidadAtaque = velocidadAtaque;
        this.efectoEspecial = efectoEspecial;
        this.durabilidadMaxima = durabilidadMaxima;
        this.durabilidad = durabilidadMaxima;
    }
    
    @Override
    public void usar(Jugador jugador) {
        // En el sistema de combate, el arma se usa automáticamente al atacar
        System.out.println("Equipas " + nombre + " como arma principal.");
        reducirDurabilidad(1);
    }
    
    @Override
    public void aplicarEfectosEquipo(Jugador jugador) {
        // Aplicar bonificaciones al equipar el arma
        jugador.setAtaque(jugador.getAtaque() + danoBase + danoExtra);
        System.out.println(nombre + " equipada! +" + (danoBase + danoExtra) + " al ataque.");
        
        // Aplicar efectos especiales
        aplicarEfectoEspecial(jugador);
    }
    
    @Override
    public void removerEfectosEquipo(Jugador jugador) {
        // Remover bonificaciones al desequipar
        jugador.setAtaque(jugador.getAtaque() - (danoBase + danoExtra));
        System.out.println(nombre + " desequipada. -" + (danoBase + danoExtra) + " al ataque.");
        
        // Remover efectos especiales
        removerEfectoEspecial(jugador);
    }
    
    private void aplicarEfectoEspecial(Jugador jugador) {
        if (efectoEspecial != null && !efectoEspecial.isEmpty()) {
            switch (efectoEspecial.toLowerCase()) {
                case "veneno":
                    System.out.println("El arma está envenenada. Los ataques pueden envenenar enemigos.");
                    break;
                case "fuego":
                    System.out.println("El arma arde con fuego. Daño extra contra enemigos de hielo.");
                    break;
                case "hielo":
                    System.out.println("El arma está congelada. Puede ralentizar enemigos.");
                    break;
                case "sangrado":
                    System.out.println("El arma causa sangrado. Daño continuo tras el ataque.");
                    break;
                case "luz":
                    System.out.println("El arma brilla con luz. Efectiva contra criaturas oscuras.");
                    break;
            }
        }
    }
    
    private void removerEfectoEspecial(Jugador jugador) {
        // En una implementación completa, se removerían los efectos aplicados
    }
    
    public void reducirDurabilidad(int cantidad) {
        durabilidad = Math.max(0, durabilidad - cantidad);
        if (durabilidad <= 0) {
            System.out.println("¡" + nombre + " se ha roto!");
            // En una implementación completa, se desequiparía automáticamente
        }
    }
    
    public void reparar(int cantidad) {
        durabilidad = Math.min(durabilidadMaxima, durabilidad + cantidad);
        System.out.println(nombre + " reparada. Durabilidad: " + durabilidad + "/" + durabilidadMaxima);
    }
    
    public void mejorar(int danoExtra) {
        this.danoExtra += danoExtra;
        System.out.println(nombre + " mejorada! +" + danoExtra + " daño extra.");
    }
    
    // Getters específicos
    public int getDanoTotal() { return danoBase + danoExtra; }
    public int getPrecision() { return precision; }
    public int getVelocidadAtaque() { return velocidadAtaque; }
    public String getEfectoEspecial() { return efectoEspecial; }
    public int getDurabilidad() { return durabilidad; }
    public int getDurabilidadMaxima() { return durabilidadMaxima; }
    public double getPorcentajeDurabilidad() { 
        return (double) durabilidad / durabilidadMaxima * 100; 
    }
    
    // Métodos estáticos para crear armas predefinidas
    public static Arma crearEspadaBasica() {
        return new Arma("Espada de Aprendiz", "Una espada básica para principiantes", 
                       50, 8, 85, 3, "", 100);
    }
    
    public static Arma crearAntorchaCombate() {
        return new Arma("Antorcha de Combate", "Antorcha que puede usarse como arma", 
                       30, 5, 70, 2, "fuego", 80);
    }
    
    public static Arma crearBastonVerdad() {
        return new Arma("Bastón de la Verdad", "Bastón que revela mentiras", 
                       120, 6, 90, 4, "luz", 150);
    }
}