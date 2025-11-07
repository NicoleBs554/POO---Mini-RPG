package combat;

import entities.Jugador;

public class CombatUtils {
    private static Jugador jugadorGlobal;
    
    public static void setJugadorGlobal(Jugador jugador) {
        jugadorGlobal = jugador;
    }
    
    public static boolean jugadorTieneAntorcha() {
        return jugadorGlobal != null && jugadorGlobal.isTieneAntorcha();
    }
    
    public static boolean jugadorTieneEspejo() {
        return jugadorGlobal != null && jugadorGlobal.isTieneEspejoVerdad();
    }
    
    public static boolean jugadorTieneAmuleto() {
        return jugadorGlobal != null && jugadorGlobal.isTieneAmuletoPerdon();
    }
    
    public static int calcularDanoConBonificaciones(int danoBase, String tipoEnemigo) {
        int danoFinal = danoBase;
        
        // Bonificaciones por items de la trama
        if (jugadorTieneAntorcha() && tipoEnemigo.equalsIgnoreCase("espíritu")) {
            danoFinal = (int)(danoFinal * 1.5);
            System.out.println("¡La antorcha brilla intensamente! Daño aumentado contra espíritus.");
        }
        
        if (jugadorTieneEspejo() && tipoEnemigo.equalsIgnoreCase("hechicero")) {
            danoFinal = (int)(danoFinal * 1.5);
            System.out.println("¡El espejo refleja la verdad! Daño aumentado contra hechiceros.");
        }
        
        if (jugadorTieneAmuleto() && tipoEnemigo.equalsIgnoreCase("familiar")) {
            danoFinal = (int)(danoFinal * 1.3);
            System.out.println("¡El amuleto del perdón protege! Daño aumentado contra familiares simbólicos.");
        }
        
        return danoFinal;
    }
}
