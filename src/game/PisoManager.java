package game;

import java.util.HashMap;
import java.util.Map;

public class PisoManager {
    private Map<Integer, Boolean> pisosCompletados;
    private boolean juegoCompletado;

    public PisoManager() {
        this.pisosCompletados = new HashMap<>();
        this.pisosCompletados.put(1, false);
        this.pisosCompletados.put(2, false);
        this.pisosCompletados.put(3, false);
        this.juegoCompletado = false;
    }

    public void marcarPisoCompletado(int numeroPiso) {
        if (pisosCompletados.containsKey(numeroPiso)) {
            pisosCompletados.put(numeroPiso, true);
            System.out.println("✅ Piso " + numeroPiso + " marcado como completado.");
            
            // Verificar si todos los pisos están completados
            if (isTodosPisosCompletados()) {
                juegoCompletado = true;
                System.out.println("¡Todos los pisos han sido completados!");
            }
        }
    }

    public boolean isPisoCompletado(int numeroPiso) {
        return pisosCompletados.getOrDefault(numeroPiso, false);
    }

    public int getPisosCompletados() {
        int completados = 0;
        for (boolean completado : pisosCompletados.values()) {
            if (completado) completados++;
        }
        return completados;
    }

    public boolean isTodosPisosCompletados() {
        return pisosCompletados.values().stream().allMatch(Boolean::booleanValue);
    }

    public boolean isJuegoCompletado() {
        return juegoCompletado;
    }

    public int getProgresoPorcentual() {
        int totalPisos = pisosCompletados.size();
        int completados = getPisosCompletados();
        return (completados * 100) / totalPisos;
    }

    public String getEstadoPisos() {
        StringBuilder estado = new StringBuilder();
        estado.append("Estado de los Pisos:\n");
        for (Map.Entry<Integer, Boolean> entry : pisosCompletados.entrySet()) {
            estado.append("  Piso ").append(entry.getKey()).append(": ")
                  .append(entry.getValue() ? "✅ Completado" : "❌ Pendiente").append("\n");
        }
        return estado.toString();
    }

    public void reiniciarProgreso() {
        for (Integer piso : pisosCompletados.keySet()) {
            pisosCompletados.put(piso, false);
        }
        juegoCompletado = false;
        System.out.println("🔄 Progreso reiniciado. Todos los pisos están pendientes.");
    }
}