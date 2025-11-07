package game;

import entities.Jugador;
import levels.Piso;
import levels.Piso1_Limbo;
import levels.Piso2_Dite;
import levels.Piso3_Recuerdos;
import java.util.Scanner;

public class Game {
    private Jugador jugador;
    private Piso pisoActual;
    private int pisoNumero;
    private Scanner scanner;
    private boolean juegoActivo;
    private GameState estadoJuego;
    private PisoManager pisoManager;

    public Game() {
        this.scanner = new Scanner(System.in);
        this.juegoActivo = true;
        this.pisoNumero = 1;
        this.estadoJuego = new GameState();
        this.pisoManager = new PisoManager();
    }

    public void iniciarJuego() {
        mostrarIntro();
        inicializarJugador();
        
        while (juegoActivo && jugador.estaVivo()) {
            ejecutarPisoActual();
            
            if (jugador.estaVivo() && pisoManager.isPisoCompletado(pisoNumero)) {
                manejarCompletacionPiso();
            }
        }
        
        mostrarFinal();
        scanner.close();
    }

    private void mostrarIntro() {
        System.out.println("╔══════════════════════════════════════════════════╗");
        System.out.println("║           EL VIAJE INESPERADO DE LEO            ║");
        System.out.println("║      De las Sombras a la Autodescubrimiento     ║");
        System.out.println("╚══════════════════════════════════════════════════╝");
        
        System.out.println("\nEn un pueblo ordinario, donde los colores se desvanecen en la rutina...");
        System.out.println("Leo, un niño de espíritu curioso, siente que hay algo más allá de lo cotidiano.");
        System.out.println("Tras descubrir un portal antiguo en el bosque, se embarca en un viaje que");
        System.out.println("reinterpreta los círculos del Infierno dantesco como reinos de crecimiento personal.");
        System.out.println("\nPresiona ENTER para comenzar...");
        scanner.nextLine();
    }

    private void inicializarJugador() {
        System.out.println("\n¿Cómo deberíamos llamarte en esta aventura?");
        String nombre = scanner.nextLine().trim();
        
        if (nombre.isEmpty()) {
            nombre = "Leo";
            System.out.println("¡Perfecto! Serás " + nombre + ", el aventurero curioso.");
        } else {
            System.out.println("¡Bienvenido, " + nombre + "! Que tu curiosidad te guíe.");
        }
        
        this.jugador = new Jugador(nombre, 100, 20, 10);
        this.pisoActual = new Piso1_Limbo(jugador, scanner);
        
        System.out.println("\nTus estadísticas iniciales:");
        jugador.mostrarEstado();
        System.out.println("\nPresiona ENTER cuando estés listo para comenzar...");
        scanner.nextLine();
    }

    private void ejecutarPisoActual() {
        System.out.println("\n" + "█".repeat(60));
        System.out.println("🌳  PISO " + pisoNumero + ": " + getNombrePisoActual() + "  🌳");
        System.out.println("█".repeat(60));
        
        System.out.println(getDescripcionPisoActual());
        System.out.println("\nTu jornada comienza ahora...");
        
        pisoActual.play();
        
        // Verificar si el jugador completó el piso
        if (jugador.estaVivo()) {
            pisoManager.marcarPisoCompletado(pisoNumero);
        }
    }

    private String getNombrePisoActual() {
        switch (pisoNumero) {
            case 1: return "BOSQUE DE LOS OLVIDADOS";
            case 2: return "CIUDAD DE LAS SOMBRAS ENGAÑOSAS";
            case 3: return "PALACIO DE LOS RECUERDOS";
            default: return "PISO DESCONOCIDO";
        }
    }

    private String getDescripcionPisoActual() {
        switch (pisoNumero) {
            case 1: 
                return "Un bosque encantado donde habitan proyectos abandonados y sueños olvidados.\n" +
                       "Los árboles susurran historias de lo que pudo ser y nunca fue.";
            case 2:
                return "Una ciudad gótica donde los espejos reflejan mentiras y las sombras ocultan verdades.\n" +
                       "Aquí, los fanáticos y herejes habitan en distorsiones de la realidad.";
            case 3:
                return "Un castillo helado donde los recuerdos familiares toman forma y confrontan.\n" +
                       "Cada habitación guarda ecos del pasado que deben ser reconciliados.";
            default: return "";
        }
    }

    private void manejarCompletacionPiso() {
        System.out.println("\n🎊 ¡PISO " + pisoNumero + " COMPLETADO! 🎊");
        System.out.println("════════════════════════════════════════");
        
        otorgarRecompensasPiso();
        mostrarProgresoTotal();
        
        if (pisoNumero < 3) {
            avanzarAlSiguientePiso();
        } else {
            juegoActivo = false;
            estadoJuego.marcarJuegoCompletado();
        }
    }

    private void otorgarRecompensasPiso() {
        System.out.println("\n--- RECOMPENSAS OBTENIDAS ---");
        
        switch (pisoNumero) {
            case 1:
                System.out.println("🌿 Recompensas del Bosque de los Olvidados:");
                System.out.println("   - 25 monedas de oro");
                System.out.println("   - Antorcha de la Curiosidad");
                System.out.println("   - +30 puntos de experiencia");
                System.out.println("   - Habilidad: Visión en la Oscuridad");
                
                jugador.agregarOro(25);
                jugador.agregarItem("Antorcha de la Curiosidad");
                jugador.agregarItem("Visión en la Oscuridad");
                jugador.ganarExperiencia(30);
                estadoJuego.agregarLogro("EXPLORADOR_INICIAL");
                break;
                
            case 2:
                System.out.println("🏰 Recompensas de la Ciudad de las Sombras:");
                System.out.println("   - 40 monedas de oro");
                System.out.println("   - Espejo de la Verdad");
                System.out.println("   - +45 puntos de experiencia");
                System.out.println("   - Habilidad: Discernimiento");
                
                jugador.agregarOro(40);
                jugador.agregarItem("Espejo de la Verdad");
                jugador.agregarItem("Discernimiento");
                jugador.ganarExperiencia(45);
                estadoJuego.agregarLogro("BUSCADOR_DE_VERDAD");
                break;
                
            case 3:
                System.out.println("❄️  Recompensas del Palacio de los Recuerdos:");
                System.out.println("   - 60 monedas de oro");
                System.out.println("   - Medalla de Autoconocimiento");
                System.out.println("   - +60 puntos de experiencia");
                System.out.println("   - Habilidad: Autoaceptación");
                
                jugador.agregarOro(60);
                jugador.agregarItem("Medalla de Autoconocimiento");
                jugador.agregarItem("Autoaceptación");
                jugador.ganarExperiencia(60);
                estadoJuego.agregarLogro("MAESTRO_INTERIOR");
                break;
        }
        
        // Recompensa base por completar cualquier piso
        jugador.agregarItem("Llave del Progreso");
        System.out.println("   - Llave del Progreso");
    }

    private void mostrarProgresoTotal() {
        System.out.println("\n--- PROGRESO GENERAL ---");
        System.out.println("Pisos completados: " + pisoManager.getPisosCompletados() + "/3");
        System.out.println("Nivel actual: " + jugador.getNivel());
        System.out.println("Experiencia: " + jugador.getExperiencia() + "/" + jugador.getExperienciaParaSiguienteNivel());
        System.out.println("Oro acumulado: " + jugador.getOro() + " monedas");
        
        System.out.println("\nLogros obtenidos:");
        for (String logro : estadoJuego.getLogros()) {
            System.out.println("   ✓ " + logro);
        }
    }

    private void avanzarAlSiguientePiso() {
        pisoNumero++;
        estadoJuego.setPisoActual(pisoNumero);
        
        System.out.println("\n🌄 AVANZANDO AL SIGUIENTE PISO... 🌄");
        System.out.println("Te preparas para enfrentar nuevos desafíos...");
        
        // Crear nuevo piso
        switch (pisoNumero) {
            case 2:
                pisoActual = new Piso2_Dite(jugador, scanner);
                break;
            case 3:
                pisoActual = new Piso3_Recuerdos(jugador, scanner);
                break;
        }
        
        System.out.println("\nPresiona ENTER para continuar hacia el Piso " + pisoNumero + "...");
        scanner.nextLine();
    }

    private void mostrarFinal() {
        System.out.println("\n" + "✨".repeat(65));
        
        if (jugador.estaVivo() && pisoManager.isJuegoCompletado()) {
            mostrarFinalVictoria();
        } else if (!jugador.estaVivo()) {
            mostrarFinalDerrota();
        } else {
            mostrarFinalAbandonado();
        }
        
        System.out.println("✨".repeat(65));
    }

    private void mostrarFinalVictoria() {
        System.out.println("                    ¡FELICIDADES, " + jugador.getNombre().toUpperCase() + "!");
        System.out.println("               HAS COMPLETADO 'EL VIAJE INESPERADO DE LEO'");
        System.out.println();
        System.out.println("Leo regresa al pueblo transformado, llevando en su corazón las lecciones");
        System.out.println("aprendidas en cada círculo de su viaje interior. Los colores del pueblo");
        System.out.println("parecen más vibrantes, y las rutinas ahora tienen espacio para la curiosidad.");
        System.out.println();
        System.out.println("🏆 ESTADÍSTICAS FINALES:");
        System.out.println("   • Nivel alcanzado: " + jugador.getNivel());
        System.out.println("   • Oro acumulado: " + jugador.getOro() + " monedas");
        System.out.println("   • Pisos completados: 3/3");
        System.out.println("   • Logros: " + estadoJuego.getLogros().size() + " obtenidos");
        System.out.println();
        System.out.println("«El verdadero viaje no es cruzar nuevos paisajes,");
        System.out.println(" sino tener nuevos ojos.» - Marcel Proust");
    }

    private void mostrarFinalDerrota() {
        System.out.println("                   EL VIAJE TERMINA AQUÍ...");
        System.out.println();
        System.out.println("A veces, el camino hacia el autoconocimiento encuentra obstáculos");
        System.out.println("que parecen insuperables. Pero cada final es solo una pausa,");
        System.out.println("un momento para descansar y reconsiderar el camino.");
        System.out.println();
        System.out.println("📊 PROGRESO ALCANZADO:");
        System.out.println("   • Piso alcanzado: " + pisoNumero + "/3");
        System.out.println("   • Nivel: " + jugador.getNivel());
        System.out.println("   • Logros: " + estadoJuego.getLogros().size() + " obtenidos");
        System.out.println();
        System.out.println("La aventura puede continuar cuando estés listo para intentarlo de nuevo.");
    }

    private void mostrarFinalAbandonado() {
        System.out.println("                 EL VIAJE QUEDA EN PAUSA...");
        System.out.println();
        System.out.println("Cada aventurero elige su propio ritmo. Tal vez hoy no era el momento,");
        System.out.println("pero el portal siempre estará allí, esperando tu regreso.");
        System.out.println();
        System.out.println("⏸️  PROGRESO GUARDADO:");
        System.out.println("   • Piso actual: " + pisoNumero + "/3");
        System.out.println("   • Progreso: " + pisoManager.getProgresoPorcentual() + "% completado");
        System.out.println();
        System.out.println("Cuando la curiosidad te llame de nuevo, estarás listo para continuar.");
    }

    // Métodos de acceso para pruebas y extensión
    public Jugador getJugador() { return jugador; }
    public int getPisoActual() { return pisoNumero; }
    public GameState getEstadoJuego() { return estadoJuego; }
    public boolean isJuegoActivo() { return juegoActivo; }
}
