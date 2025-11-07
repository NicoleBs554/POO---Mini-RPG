package entities;

import java.util.*;

import combat.Utils;

public class NPC {
    private String nombre;
    private String rol;
    private Map<String, String> dialogos;
    private List<String> itemsOfrecidos;
    private boolean haInteractuado;
    private String pistaImportante;
    
    public NPC(String nombre, String rol) {
        this.nombre = nombre;
        this.rol = rol;
        this.dialogos = new HashMap<>();
        this.itemsOfrecidos = new ArrayList<>();
        this.haInteractuado = false;
        this.pistaImportante = "";
        
        inicializarDialogosBasicos();
    }
    
    private void inicializarDialogosBasicos() {
        dialogos.put("saludo", "Hola, viajero. ¿En qué puedo ayudarte?");
        dialogos.put("despedida", "Que tengas un buen viaje.");
        dialogos.put("default", "No tengo mucho que decir sobre eso.");
    }
    
    // Métodos de interacción
    public void interactuar(Jugador jugador, Scanner scanner) {
        System.out.println("\n=== INTERACTUANDO CON " + nombre.toUpperCase() + " ===");
        System.out.println(nombre + ": " + getDialogo("saludo"));
        
        boolean continuarInteraccion = true;
        while (continuarInteraccion && jugador.estaVivo()) {
            mostrarOpcionesInteraccion();
            String opcion = scanner.nextLine().trim();
            
            switch (opcion) {
                case "1":
                    hablar(jugador, scanner);
                    break;
                case "2":
                    ofrecerItems(jugador);
                    break;
                case "3":
                    darPista(jugador);
                    break;
                case "4":
                    System.out.println(nombre + ": " + getDialogo("despedida"));
                    continuarInteraccion = false;
                    break;
                default:
                    System.out.println("Opción no válida.");
                    break;
            }
        }
        
        haInteractuado = true;
    }
    
    private void mostrarOpcionesInteraccion() {
        System.out.println("\nOpciones:");
        System.out.println("1. Conversar");
        System.out.println("2. " + (itemsOfrecidos.isEmpty() ? "Pedir ayuda" : "Recibir ofrecimiento"));
        System.out.println("3. Buscar pistas");
        System.out.println("4. Despedirse");
        System.out.print("Elige una opción: ");
    }
    
    private void hablar(Jugador jugador, Scanner scanner) {
        System.out.println("\nTemas de conversación:");
        List<String> temas = new ArrayList<>(dialogos.keySet());
        temas.remove("saludo");
        temas.remove("despedida");
        temas.remove("default");
        
        for (int i = 0; i < temas.size(); i++) {
            System.out.println((i + 1) + ". " + temas.get(i));
        }
        System.out.println((temas.size() + 1) + ". Preguntar sobre algo más");
        
        System.out.print("¿Sobre qué quieres hablar?: ");
        String opcion = scanner.nextLine().trim();
        
        try {
            int indice = Integer.parseInt(opcion) - 1;
            if (indice >= 0 && indice < temas.size()) {
                System.out.println(nombre + ": " + getDialogo(temas.get(indice)));
            } else if (indice == temas.size()) {
                System.out.print("¿Qué quieres preguntar?: ");
                String pregunta = scanner.nextLine();
                System.out.println(nombre + ": " + getDialogo("default"));
            }
        } catch (NumberFormatException e) {
            // Buscar tema por nombre
            String dialogo = getDialogo(opcion.toLowerCase());
            System.out.println(nombre + ": " + dialogo);
        }
        
        // Posibilidad de ganar experiencia por conversar
        if (Utils.rnd(1, 10) <= 3) {
            int exp = Utils.rnd(1, 3);
            jugador.ganarExperiencia(exp);
            System.out.println("¡Ganas " + exp + " puntos de experiencia por la conversación!");
        }
    }
    
    private void ofrecerItems(Jugador jugador) {
        if (itemsOfrecidos.isEmpty()) {
            System.out.println(nombre + ": Lo siento, no tengo nada que ofrecerte en este momento.");
            return;
        }
        
        System.out.println(nombre + ": Tengo estos items que podrían ayudarte:");
        for (int i = 0; i < itemsOfrecidos.size(); i++) {
            System.out.println((i + 1) + ". " + itemsOfrecidos.get(i));
        }
        
        System.out.print("¿Qué item te gustaría?: ");
        // En una implementación completa, aquí se manejaría la selección y transferencia de items
        System.out.println(nombre + ": Toma este " + itemsOfrecidos.get(0) + ". Espero que te sea útil.");
        // jugador.agregarItem(itemsOfrecidos.remove(0));
    }
    
    private void darPista(Jugador jugador) {
        if (!pistaImportante.isEmpty()) {
            System.out.println(nombre + ": " + pistaImportante);
            
            // Recompensa por obtener pista importante
            if (!haInteractuado) {
                jugador.ganarExperiencia(5);
                System.out.println("¡Ganas 5 puntos de experiencia por la pista valiosa!");
            }
        } else {
            System.out.println(nombre + ": No tengo pistas importantes en este momento.");
        }
    }
    
    // Métodos de configuración
    public void agregarDialogo(String contexto, String dialogo) {
        dialogos.put(contexto.toLowerCase(), dialogo);
    }
    
    public void agregarItemOfrecido(String item) {
        itemsOfrecidos.add(item);
    }
    
    public void setPistaImportante(String pista) {
        this.pistaImportante = pista;
    }
    
    // Getters
    public String getNombre() { return nombre; }
    public String getRol() { return rol; }
    public String getDialogo(String contexto) {
        return dialogos.getOrDefault(contexto.toLowerCase(), dialogos.get("default"));
    }
    public boolean isHaInteractuado() { return haInteractuado; }
    
    // NPCs específicos de la trama
    public static NPC crearMensajero() {
        NPC mensajero = new NPC("Mensajero Herido", "Mensajero del Bosque");
        mensajero.agregarDialogo("bosque", "El bosque está lleno de proyectos abandonados... ten cuidado.");
        mensajero.agregarDialogo("antorcha", "La luz revela lo que la oscuridad oculta. Busca una antorcha.");
        mensajero.agregarItemOfrecido("Poción Menor");
        mensajero.setPistaImportante("Los espejos en Dite mienten... busca la verdad detrás de las apariencias.");
        return mensajero;
    }
    
    public static NPC crearGuardianPortal() {
        NPC guardian = new NPC("Guardián del Portal", "Protector Antiguo");
        guardian.agregarDialogo("portal", "Este portal lleva a reinos de autoconocimiento. ¿Estás preparado?");
        guardian.agregarDialogo("proposito", "Cada viajero enfrenta sus propios demonios. Los tuyos te esperan.");
        guardian.setPistaImportante("En el Palacio de los Recuerdos, la aceptación es tu mejor arma.");
        return guardian;
    }
}