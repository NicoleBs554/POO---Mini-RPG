package items;

import entities.Jugador;

public class Armadura extends Item {
    private int defensaBase;
    private int defensaExtra;
    private int resistenciaMagica;
    private int agilidadPenalizacion;
    private String efectoEspecial;
    private int durabilidad;
    private int durabilidadMaxima;
    private String slot; // "cabeza", "pecho", "manos", "pies", etc.
    
    public Armadura(String nombre, String descripcion, TipoItem tipo, int valor, 
                   int defensaBase, int resistenciaMagica, int agilidadPenalizacion, 
                   String efectoEspecial, int durabilidadMaxima, String slot) {
        super(nombre, descripcion, tipo, valor, false, 1, true, false);
        this.defensaBase = defensaBase;
        this.defensaExtra = 0;
        this.resistenciaMagica = resistenciaMagica;
        this.agilidadPenalizacion = agilidadPenalizacion;
        this.efectoEspecial = efectoEspecial;
        this.durabilidadMaxima = durabilidadMaxima;
        this.durabilidad = durabilidadMaxima;
        this.slot = slot;
    }
    
    @Override
    public void usar(Jugador jugador) {
        System.out.println("Equipas " + nombre + " en " + slot + ".");
        reducirDurabilidad(1);
    }
    
    @Override
    public void aplicarEfectosEquipo(Jugador jugador) {
        // Aplicar bonificaciones de defensa
        jugador.setDefensa(jugador.getDefensa() + defensaBase + defensaExtra);
        System.out.println(nombre + " equipada! +" + (defensaBase + defensaExtra) + " a la defensa.");
        
        // Aplicar penalización de agilidad
        if (agilidadPenalizacion > 0) {
            jugador.setAgilidad(jugador.getAgilidad() - agilidadPenalizacion);
            System.out.println("Penalización de -" + agilidadPenalizacion + " a la agilidad.");
        }
        
        // Aplicar resistencia mágica
        if (resistenciaMagica > 0) {
            System.out.println("Resistencia mágica aumentada en " + resistenciaMagica + ".");
        }
        
        // Aplicar efectos especiales
        aplicarEfectoEspecial(jugador);
    }
    
    @Override
    public void removerEfectosEquipo(Jugador jugador) {
        // Remover bonificaciones
        jugador.setDefensa(jugador.getDefensa() - (defensaBase + defensaExtra));
        System.out.println(nombre + " desequipada. -" + (defensaBase + defensaExtra) + " a la defensa.");
        
        // Remover penalización de agilidad
        if (agilidadPenalizacion > 0) {
            jugador.setAgilidad(jugador.getAgilidad() + agilidadPenalizacion);
            System.out.println("Penalización de agilidad removida.");
        }
        
        // Remover efectos especiales
        removerEfectoEspecial(jugador);
    }
    
    private void aplicarEfectoEspecial(Jugador jugador) {
        if (efectoEspecial != null && !efectoEspecial.isEmpty()) {
            switch (efectoEspecial.toLowerCase()) {
                case "regeneracion":
                    System.out.println("La armadura regenera salud lentamente.");
                    break;
                case "resistencia_elemental":
                    System.out.println("Resistencia a elementos aumentada.");
                    break;
                case "sigilo":
                    System.out.println("Movimientos más silenciosos.");
                    break;
                case "luz":
                    System.out.println("La armadura emite luz suave.");
                    break;
                case "proteccion_oscuridad":
                    System.out.println("Protección contra ataques oscuros.");
                    break;
            }
        }
    }
    
    private void removerEfectoEspecial(Jugador jugador) {
        // Remover efectos aplicados
    }
    
    public void reducirDurabilidad(int cantidad) {
        durabilidad = Math.max(0, durabilidad - cantidad);
        if (durabilidad <= 0) {
            System.out.println("¡" + nombre + " se ha roto!");
        }
    }
    
    public void reparar(int cantidad) {
        durabilidad = Math.min(durabilidadMaxima, durabilidad + cantidad);
        System.out.println(nombre + " reparada. Durabilidad: " + durabilidad + "/" + durabilidadMaxima);
    }
    
    public void mejorar(int defensaExtra) {
        this.defensaExtra += defensaExtra;
        System.out.println(nombre + " mejorada! +" + defensaExtra + " defensa extra.");
    }
    
    // Getters específicos
    public int getDefensaTotal() { return defensaBase + defensaExtra; }
    public int getResistenciaMagica() { return resistenciaMagica; }
    public int getAgilidadPenalizacion() { return agilidadPenalizacion; }
    public String getEfectoEspecial() { return efectoEspecial; }
    public int getDurabilidad() { return durabilidad; }
    public int getDurabilidadMaxima() { return durabilidadMaxima; }
    public String getSlot() { return slot; }
    public double getPorcentajeDurabilidad() { 
        return (double) durabilidad / durabilidadMaxima * 100; 
    }
    
    // Métodos estáticos para crear armaduras predefinidas
    public static Armadura crearTunicaAprendiz() {
        return new Armadura("Túnica de Aprendiz", "Túnica básica para principiantes", 
                           TipoItem.ARMADURA_LIGERA, 40, 3, 2, 0, "", 80, "pecho");
    }
    
    public static Armadura crearCapaExplorador() {
        return new Armadura("Capa de Explorador", "Capa que protege sin restringir movimiento", 
                           TipoItem.ARMADURA_LIGERA, 60, 2, 1, 0, "sigilo", 70, "espalda");
    }
    
    public static Armadura crearAmuletoPerdon() {
        return new Armadura("Amuleto del Perdón", "Protege contra ataques emocionales", 
                           TipoItem.AMULETO, 150, 1, 10, 0, "proteccion_oscuridad", 200, "cuello");
    }
}