package combat;

import entities.Jugador;
import entities.Enemigo;

public class Habilidad {
    private String nombre;
    private int costoEnergia;
    private int multiplicadorDano;
    private String descripcion;
    private TipoHabilidad tipo;
    
    public enum TipoHabilidad {
        OFENSIVA,
        DEFENSIVA,
        APOYO,
        ESTADO
    }
    
    public Habilidad(String nombre, int costoEnergia, int multiplicadorDano, String descripcion) {
        this.nombre = nombre;
        this.costoEnergia = costoEnergia;
        this.multiplicadorDano = multiplicadorDano;
        this.descripcion = descripcion;
        this.tipo = determinarTipo();
    }
    
    private TipoHabilidad determinarTipo() {
        String nombreLower = nombre.toLowerCase();
        if (nombreLower.contains("ataque") || nombreLower.contains("golpe") || 
            nombreLower.contains("daño") || nombreLower.contains("ofensiva")) {
            return TipoHabilidad.OFENSIVA;
        } else if (nombreLower.contains("escudo") || nombreLower.contains("defensa") || 
                   nombreLower.contains("protección") || nombreLower.contains("curar")) {
            return TipoHabilidad.DEFENSIVA;
        } else if (nombreLower.contains("estado") || nombreLower.contains("ralentizar") || 
                   nombreLower.contains("confundir") || nombreLower.contains("debilitar")) {
            return TipoHabilidad.ESTADO;
        } else {
            return TipoHabilidad.APOYO;
        }
    }
    
    public int calcularDano(Jugador jugador) {
        int danoBase = jugador.getAtaque();
        
        switch (tipo) {
            case OFENSIVA:
                return (int)(danoBase * (multiplicadorDano * 0.8 + 0.5));
            case DEFENSIVA:
                return (int)(danoBase * (multiplicadorDano * 0.3 + 0.5));
            case ESTADO:
                return (int)(danoBase * (multiplicadorDano * 0.5 + 0.3));
            case APOYO:
                return (int)(danoBase * (multiplicadorDano * 0.4 + 0.4));
            default:
                return danoBase;
        }
    }
    
    public int calcularDano(Enemigo enemigo) {
        int danoBase = enemigo.getAtaque();
        
        switch (tipo) {
            case OFENSIVA:
                return (int)(danoBase * (multiplicadorDano * 0.7 + 0.5));
            case DEFENSIVA:
                return (int)(danoBase * (multiplicadorDano * 0.2 + 0.3));
            case ESTADO:
                return (int)(danoBase * (multiplicadorDano * 0.4 + 0.2));
            case APOYO:
                return (int)(danoBase * (multiplicadorDano * 0.3 + 0.3));
            default:
                return danoBase;
        }
    }
    
    public void aplicarEfecto(Jugador objetivo) {
        // Implementar efectos de habilidades de estado
        switch (nombre.toLowerCase()) {
            case "concentración":
                objetivo.aplicarEfecto("Ataque Aumentado", 2);
                break;
            case "esporas ralentizadoras":
                objetivo.aplicarEfecto("Ralentizado", 2);
                break;
        }
    }
    
    public void aplicarEfecto(Enemigo objetivo) {
        switch (nombre.toLowerCase()) {
            case "hechizo confuso":
                objetivo.aplicarEfecto("Confundido", 1);
                break;
            case "crítica destructiva":
                objetivo.aplicarEfecto("Debilitado", 2);
                break;
        }
    }
    
    // Getters
    public String getNombre() { return nombre; }
    public int getCostoEnergia() { return costoEnergia; }
    public int getMultiplicadorDano() { return multiplicadorDano; }
    public String getDescripcion() { return descripcion; }
    public TipoHabilidad getTipo() { return tipo; }
    
    // Habilidades predefinidas para la trama
    public static Habilidad[] getHabilidadesTrama() {
        return new Habilidad[] {
            new Habilidad("Luz Reveladora", 4, 2, "Usa la antorcha para revelar debilidades"),
            new Habilidad("Verdad Desnuda", 5, 3, "El espejo de la verdad muestra puntos débiles"),
            new Habilidad("Perdón Sanador", 6, 0, "El amuleto del perdón cura heridas emocionales"),
            new Habilidad("Memoria Reconfortante", 3, 1, "Los recuerdos felices fortalecen el espíritu")
        };
    }
}