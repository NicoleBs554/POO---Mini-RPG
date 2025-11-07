package items;

import entities.Jugador;

public abstract class Item {
    protected String nombre;
    protected String descripcion;
    protected TipoItem tipo;
    protected int valor;
    protected boolean apilable;
    protected int cantidadMaxima;
    protected int cantidad;
    protected boolean equipable;
    protected boolean consumible;
    
    public Item(String nombre, String descripcion, TipoItem tipo, int valor, 
                boolean apilable, int cantidadMaxima, boolean equipable, boolean consumible) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.tipo = tipo;
        this.valor = valor;
        this.apilable = apilable;
        this.cantidadMaxima = cantidadMaxima;
        this.cantidad = 1;
        this.equipable = equipable;
        this.consumible = consumible;
    }
    
    // Método abstracto que debe implementar cada subclase
    public abstract void usar(Jugador jugador);
    
    // Método para aplicar efectos al equipar (para armas y armaduras)
    public void aplicarEfectosEquipo(Jugador jugador) {
        // Implementación por defecto vacía
    }
    
    // Método para remover efectos al desequipar
    public void removerEfectosEquipo(Jugador jugador) {
        // Implementación por defecto vacía
    }
    
    // Métodos de gestión de cantidad
    public boolean puedeApilar() {
        return apilable && cantidad < cantidadMaxima;
    }
    
    public boolean apilar(Item otroItem) {
        if (this.apilable && otroItem.getClass().equals(this.getClass()) && 
            otroItem.nombre.equals(this.nombre) && this.cantidad < this.cantidadMaxima) {
            int espacioDisponible = this.cantidadMaxima - this.cantidad;
            int cantidadAApilar = Math.min(espacioDisponible, otroItem.cantidad);
            this.cantidad += cantidadAApilar;
            otroItem.cantidad -= cantidadAApilar;
            return true;
        }
        return false;
    }
    
    public Item dividir(int cantidadDividir) {
        if (!apilable || cantidadDividir >= cantidad) {
            return null;
        }
        
        try {
            Item nuevoItem = this.getClass().getDeclaredConstructor().newInstance();
            nuevoItem.cantidad = cantidadDividir;
            this.cantidad -= cantidadDividir;
            return nuevoItem;
        } catch (Exception e) {
            return null;
        }
    }
    
    // Getters
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public TipoItem getTipo() { return tipo; }
    public int getValor() { return valor; }
    public boolean isApilable() { return apilable; }
    public int getCantidadMaxima() { return cantidadMaxima; }
    public int getCantidad() { return cantidad; }
    public boolean isEquipable() { return equipable; }
    public boolean isConsumible() { return consumible; }
    
    // Setters
    public void setCantidad(int cantidad) { 
        if (apilable) {
            this.cantidad = Math.min(cantidad, cantidadMaxima);
        } else {
            this.cantidad = 1;
        }
    }
    
    @Override
    public String toString() {
        String info = nombre + " - " + descripcion;
        if (apilable && cantidad > 1) {
            info += " (x" + cantidad + ")";
        }
        info += " [" + tipo.getNombre() + "]";
        info += " - Valor: " + valor + " monedas";
        return info;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Item item = (Item) obj;
        return nombre.equals(item.nombre) && tipo == item.tipo;
    }
    
    @Override
    public int hashCode() {
        return nombre.hashCode() + tipo.hashCode();
    }
}