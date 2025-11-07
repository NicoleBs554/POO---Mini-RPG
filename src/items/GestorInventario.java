package items;

import entities.Jugador;
import java.util.*;

public class GestorInventario {
    private List<Item> inventario;
    private Map<String, Item> equipo; // slot -> item equipado
    private int capacidadMaxima;
    private int oro;
    
    public GestorInventario(int capacidadMaxima) {
        this.inventario = new ArrayList<>();
        this.equipo = new HashMap<>();
        this.capacidadMaxima = capacidadMaxima;
        this.oro = 0;
        
        // Inicializar slots de equipo
        inicializarSlotsEquipo();
    }
    
    private void inicializarSlotsEquipo() {
        equipo.put("mano_principal", null);
        equipo.put("mano_secundaria", null);
        equipo.put("cabeza", null);
        equipo.put("pecho", null);
        equipo.put("manos", null);
        equipo.put("pies", null);
        equipo.put("cuello", null);
        equipo.put("anillo1", null);
        equipo.put("anillo2", null);
    }
    
    // Métodos de gestión del inventario
    public boolean agregarItem(Item item) {
        if (inventario.size() >= capacidadMaxima && !hayEspacioPara(item)) {
            System.out.println("Inventario lleno. No puedes llevar más items.");
            return false;
        }
        
        // Intentar apilar si es apilable
        if (item.isApilable()) {
            for (Item itemExistente : inventario) {
                if (itemExistente.apilar(item)) {
                    System.out.println("Item apilado: " + item.getNombre() + 
                                     " (Total: " + itemExistente.getCantidad() + ")");
                    return true;
                }
            }
        }
        
        // Si no se pudo apilar, agregar como nuevo item
        if (inventario.size() < capacidadMaxima) {
            inventario.add(item);
            System.out.println("Agregado al inventario: " + item.getNombre());
            return true;
        }
        
        return false;
    }
    
    private boolean hayEspacioPara(Item item) {
        if (item.isApilable()) {
            for (Item itemExistente : inventario) {
                if (itemExistente.puedeApilar() && itemExistente.equals(item)) {
                    return true;
                }
            }
        }
        return inventario.size() < capacidadMaxima;
    }
    
    public boolean removerItem(Item item) {
        return inventario.remove(item);
    }
    
    public boolean removerItem(String nombreItem) {
        Iterator<Item> iterator = inventario.iterator();
        while (iterator.hasNext()) {
            Item item = iterator.next();
            if (item.getNombre().equalsIgnoreCase(nombreItem)) {
                iterator.remove();
                System.out.println("Removido del inventario: " + nombreItem);
                return true;
            }
        }
        return false;
    }
    
    public Item buscarItem(String nombreItem) {
        for (Item item : inventario) {
            if (item.getNombre().equalsIgnoreCase(nombreItem)) {
                return item;
            }
        }
        return null;
    }
    
    public List<Item> buscarItemsPorTipo(TipoItem tipo) {
        List<Item> resultados = new ArrayList<>();
        for (Item item : inventario) {
            if (item.getTipo() == tipo) {
                resultados.add(item);
            }
        }
        return resultados;
    }
    
    // Métodos de equipamiento
    public boolean equiparItem(Item item, Jugador jugador) {
        if (!item.isEquipable()) {
            System.out.println(item.getNombre() + " no se puede equipar.");
            return false;
        }
        
        String slot = determinarSlot(item);
        if (slot == null) {
            System.out.println("No se puede determinar el slot para " + item.getNombre());
            return false;
        }
        
        // Desequipar item actual si existe
        Item itemActual = equipo.get(slot);
        if (itemActual != null) {
            desequiparItem(slot, jugador);
        }
        
        // Equipar nuevo item
        equipo.put(slot, item);
        inventario.remove(item);
        
        // Aplicar efectos del item
        if (item instanceof Arma) {
            ((Arma) item).aplicarEfectosEquipo(jugador);
        } else if (item instanceof Armadura) {
            ((Armadura) item).aplicarEfectosEquipo(jugador);
        }
        
        System.out.println("Equipado: " + item.getNombre() + " en " + slot);
        return true;
    }
    
    public boolean desequiparItem(String slot, Jugador jugador) {
        Item item = equipo.get(slot);
        if (item == null) {
            System.out.println("No hay nada equipado en " + slot);
            return false;
        }
        
        // Remover efectos del item
        if (item instanceof Arma) {
            ((Arma) item).removerEfectosEquipo(jugador);
        } else if (item instanceof Armadura) {
            ((Armadura) item).removerEfectosEquipo(jugador);
        }
        
        // Mover al inventario
        if (agregarItem(item)) {
            equipo.put(slot, null);
            System.out.println("Desequipado: " + item.getNombre() + " de " + slot);
            return true;
        } else {
            System.out.println("No hay espacio en el inventario para desequipar " + item.getNombre());
            return false;
        }
    }
    
    private String determinarSlot(Item item) {
        if (item instanceof Arma) {
            // Priorizar mano principal
            if (equipo.get("mano_principal") == null) {
                return "mano_principal";
            } else if (equipo.get("mano_secundaria") == null) {
                return "mano_secundaria";
            } else {
                return "mano_principal"; // Reemplazar el de mano principal
            }
        } else if (item instanceof Armadura) {
            Armadura armadura = (Armadura) item;
            return armadura.getSlot();
        }
        return null;
    }
    
    // Métodos de uso de items
    public boolean usarItem(String nombreItem, Jugador jugador) {
        Item item = buscarItem(nombreItem);
        if (item == null) {
            System.out.println("No tienes " + nombreItem + " en el inventario.");
            return false;
        }
        
        if (item.isEquipable()) {
            return equiparItem(item, jugador);
        } else if (item.isConsumible()) {
            item.usar(jugador);
            
            // Si es consumible y se agotó, removerlo
            if (item.getCantidad() <= 0) {
                removerItem(item);
            }
            return true;
        } else {
            item.usar(jugador);
            return true;
        }
    }
    
    // Métodos de visualización
    public void mostrarInventario() {
        System.out.println("\n=== INVENTARIO ===");
        System.out.println("Capacidad: " + inventario.size() + "/" + capacidadMaxima);
        System.out.println("Oro: " + oro + " monedas");
        
        if (inventario.isEmpty()) {
            System.out.println("El inventario está vacío.");
            return;
        }
        
        // Agrupar items por tipo
        Map<TipoItem, List<Item>> itemsPorTipo = new TreeMap<>();
        for (Item item : inventario) {
            itemsPorTipo.computeIfAbsent(item.getTipo(), k -> new ArrayList<>()).add(item);
        }
        
        // Mostrar por categorías
        for (Map.Entry<TipoItem, List<Item>> entry : itemsPorTipo.entrySet()) {
            System.out.println("\n" + entry.getKey().getNombre() + ":");
            for (Item item : entry.getValue()) {
                System.out.println("  • " + item.toString());
            }
        }
    }
    
    public void mostrarEquipo() {
        System.out.println("\n=== EQUIPO ===");
        boolean tieneEquipo = false;
        
        for (Map.Entry<String, Item> entry : equipo.entrySet()) {
            if (entry.getValue() != null) {
                System.out.println(entry.getKey() + ": " + entry.getValue().getNombre());
                tieneEquipo = true;
            }
        }
        
        if (!tieneEquipo) {
            System.out.println("No tienes nada equipado.");
        }
    }
    
    public void mostrarEstadoEquipo(Jugador jugador) {
        System.out.println("\n=== ESTADO CON EQUIPO ===");
        jugador.mostrarEstado();
        mostrarEquipo();
    }
    
    // Métodos de gestión de oro
    public void agregarOro(int cantidad) {
        oro += cantidad;
        System.out.println("Obtienes " + cantidad + " monedas de oro. Total: " + oro);
    }
    
    public boolean gastarOro(int cantidad) {
        if (oro >= cantidad) {
            oro -= cantidad;
            System.out.println("Gastas " + cantidad + " monedas de oro. Restante: " + oro);
            return true;
        } else {
            System.out.println("No tienes suficiente oro. Necesitas: " + cantidad + ", Tienes: " + oro);
            return false;
        }
    }
    
    // Getters
    public List<Item> getInventario() { return new ArrayList<>(inventario); }
    public Map<String, Item> getEquipo() { return new HashMap<>(equipo); }
    public int getCapacidadMaxima() { return capacidadMaxima; }
    public int getOro() { return oro; }
    public int getEspacioDisponible() { return capacidadMaxima - inventario.size(); }
    
    // Setters
    public void setCapacidadMaxima(int capacidadMaxima) { 
        this.capacidadMaxima = capacidadMaxima; 
    }
    
    // Métodos de utilidad
    public boolean tieneItem(String nombreItem) {
        return buscarItem(nombreItem) != null;
    }
    
    public boolean tieneItemEspecial(String efectoUnico) {
        for (Item item : inventario) {
            if (item instanceof ItemEspecial) {
                ItemEspecial especial = (ItemEspecial) item;
                if (especial.getEfectoUnico().equals(efectoUnico)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public int contarItemsPorTipo(TipoItem tipo) {
        int count = 0;
        for (Item item : inventario) {
            if (item.getTipo() == tipo) {
                count += item.getCantidad();
            }
        }
        return count;
    }
    
    // Método para usar items en combate
    public boolean usarItemEnCombate(String nombreItem, Jugador jugador) {
        Item item = buscarItem(nombreItem);
        if (item == null) {
            System.out.println("No tienes " + nombreItem + " en el inventario.");
            return false;
        }
        
        if (!item.isConsumible()) {
            System.out.println(nombreItem + " no se puede usar en combate.");
            return false;
        }
        
        item.usar(jugador);
        
        // Si se agotó, removerlo
        if (item.getCantidad() <= 0) {
            removerItem(item);
        }
        
        return true;
    }
}