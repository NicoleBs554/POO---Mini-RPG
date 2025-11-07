package game;

import java.util.ArrayList;
import java.util.List;

public class GameState {
    private int pisoActual;
    private boolean juegoCompletado;
    private List<String> logros;
    private List<String> eventos;
    private int totalEnemigosDerrotados;
    private int totalPuzzlesResueltos;
    private long tiempoInicio;
    private long tiempoJugado;

    public GameState() {
        this.pisoActual = 1;
        this.juegoCompletado = false;
        this.logros = new ArrayList<>();
        this.eventos = new ArrayList<>();
        this.totalEnemigosDerrotados = 0;
        this.totalPuzzlesResueltos = 0;
        this.tiempoInicio = System.currentTimeMillis();
    }

    // Métodos de gestión del estado del juego
    public void avanzarPiso() {
        if (pisoActual < 3) {
            pisoActual++;
        }
    }

    public void retrocederPiso() {
        if (pisoActual > 1) {
            pisoActual--;
        }
    }

    public void marcarJuegoCompletado() {
        this.juegoCompletado = true;
        this.tiempoJugado = System.currentTimeMillis() - tiempoInicio;
        agregarLogro("VIAJERO_COMPLETO");
    }

    public void agregarLogro(String logro) {
        if (!logros.contains(logro)) {
            logros.add(logro);
            System.out.println("🎖️  Logro desbloqueado: " + traducirLogro(logro));
        }
    }

    public void agregarEvento(String evento) {
        eventos.add(evento + " - Tiempo: " + System.currentTimeMillis());
    }

    public void incrementarEnemigosDerrotados() {
        totalEnemigosDerrotados++;
        if (totalEnemigosDerrotados % 10 == 0) {
            agregarLogro("CAZADOR_" + totalEnemigosDerrotados);
        }
    }

    public void incrementarPuzzlesResueltos() {
        totalPuzzlesResueltos++;
        if (totalPuzzlesResueltos % 5 == 0) {
            agregarLogro("RESOLVEDOR_" + totalPuzzlesResueltos);
        }
    }

    private String traducirLogro(String logro) {
        switch (logro) {
            case "EXPLORADOR_INICIAL": return "Explorador Inicial";
            case "BUSCADOR_DE_VERDAD": return "Buscador de la Verdad";
            case "MAESTRO_INTERIOR": return "Maestro Interior";
            case "VIAJERO_COMPLETO": return "Viajero Completo";
            case "CAZADOR_10": return "Cazador Novato (10 enemigos)";
            case "CAZADOR_20": return "Cazador Experimentado (20 enemigos)";
            case "CAZADOR_30": return "Cazador Maestro (30 enemigos)";
            case "RESOLVEDOR_5": return "Resolvedor de Misterios (5 puzzles)";
            case "RESOLVEDOR_10": return "Maestro de Puzzles (10 puzzles)";
            default: return logro;
        }
    }

    // Getters
    public int getPisoActual() { return pisoActual; }
    public boolean isJuegoCompletado() { return juegoCompletado; }
    public List<String> getLogros() { return new ArrayList<>(logros); }
    public List<String> getEventos() { return new ArrayList<>(eventos); }
    public int getTotalEnemigosDerrotados() { return totalEnemigosDerrotados; }
    public int getTotalPuzzlesResueltos() { return totalPuzzlesResueltos; }
    public long getTiempoJugado() { 
        return juegoCompletado ? tiempoJugado : System.currentTimeMillis() - tiempoInicio; 
    }

    public void setPisoActual(int piso) {
        if (piso >= 1 && piso <= 3) {
            this.pisoActual = piso;
        }
    }
}
