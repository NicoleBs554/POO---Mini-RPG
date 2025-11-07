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
    private boolean equipada;
    private int bonificacionDefensa; // Nueva variable para almacenar la bonificación
    
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
        this.equipada = false;
        this.bonificacionDefensa = defensaBase;
    }
    
    @Override
    public void usar(Jugador jugador) {
        if (!equipada) {
            equipar(jugador);
        } else {
            desequipar(jugador);
        }
    }
    
    public void equipar(Jugador jugador) {
        if (!equipada) {
            System.out.println("Equipas " + nombre + " en " + slot + ".");
            aplicarEfectosEquipo(jugador);
            equipada = true;
            reducirDurabilidad(1);
        }
    }
    
    public void desequipar(Jugador jugador) {
        if (equipada) {
            System.out.println("Desequipas " + nombre + " de " + slot + ".");
            removerEfectosEquipo(jugador);
            equipada = false;
        }
    }
    
    @Override
    public void aplicarEfectosEquipo(Jugador jugador) {
        // En lugar de modificar atributos directamente, almacenamos la bonificación
        System.out.println(nombre + " equipada! +" + bonificacionDefensa + " a la defensa.");
        
        // Aplicar penalización de agilidad (solo mensaje por ahora)
        if (agilidadPenalizacion > 0) {
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
        System.out.println(nombre + " desequipada. -" + bonificacionDefensa + " a la defensa.");
        
        // Remover penalización de agilidad
        if (agilidadPenalizacion > 0) {
            System.out.println("Penalización de agilidad removida.");
        }
        
        // Remover efectos especiales
        removerEfectoEspecial(jugador);
    }
    
    // Nuevo método para obtener la bonificación de defensa
    public int getBonificacionDefensa() {
        return bonificacionDefensa;
    }
    
    // Nuevo método para verificar si está equipada
    public boolean isEquipada() {
        return equipada;
    }
    
    // Nuevo método para obtener la penalización de agilidad
    public int getPenalizacionAgilidad() {
        return agilidadPenalizacion;
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
            equipada = false;
        }
    }
    
    public void reparar(int cantidad) {
        durabilidad = Math.min(durabilidadMaxima, durabilidad + cantidad);
        System.out.println(nombre + " reparada. Durabilidad: " + durabilidad + "/" + durabilidadMaxima);
    }
    
    public void mejorar(int defensaExtra) {
        this.defensaExtra += defensaExtra;
        this.bonificacionDefensa = defensaBase + this.defensaExtra;
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