package items;

import entities.Jugador;


public class ItemEspecial extends Item {
    private String efectoUnico;
    private boolean usado;
    private boolean necesarioParaProgreso;
    private String relacionPuzzle;
    
    public ItemEspecial(String nombre, String descripcion, TipoItem tipo, int valor, 
                       String efectoUnico, boolean necesarioParaProgreso, String relacionPuzzle) {
        super(nombre, descripcion, tipo, valor, false, 1, false, true);
        this.efectoUnico = efectoUnico;
        this.usado = false;
        this.necesarioParaProgreso = necesarioParaProgreso;
        this.relacionPuzzle = relacionPuzzle;
    }
    
    @Override
    public void usar(Jugador jugador) {
        if (usado && !esReutilizable()) {
            System.out.println(nombre + " ya ha sido usado y no se puede usar de nuevo.");
            return;
        }
        
        System.out.println("Usas " + nombre + ".");
        aplicarEfectoUnico(jugador);
        
        if (!esReutilizable()) {
            usado = true;
            System.out.println(nombre + " se ha consumido.");
        }
    }
    
    private void aplicarEfectoUnico(Jugador jugador) {
        switch (efectoUnico.toLowerCase()) {
            case "iluminar_sala":
                System.out.println("La habitación se ilumina revelando secretos ocultos.");
                jugador.setTieneAntorcha(true);
                break;
                
            case "revelar_verdades":
                System.out.println("Los espejos dejan de mentir. La verdad se hace visible.");
                jugador.setTieneEspejoVerdad(true);
                break;
                
            case "sanar_recuerdos":
                System.out.println("Los recuerdos dolorosos pierden su poder sobre ti.");
                jugador.setTieneAmuletoPerdon(true);
                break;
                
            case "abrir_puerta_especial":
                System.out.println("Una puerta secreta se revela y se abre.");
                // Lógica para abrir puertas especiales
                break;
                
            case "debilitar_guardian":
                System.out.println("El guardián parece afectado por el objeto.");
                // Lógica para debilitar enemigos especiales
                break;
                
            case "activar_mecanismo":
                System.out.println("Un mecanismo antiguo cobra vida.");
                // Lógica para activar puzzles
                break;
                
            default:
                System.out.println("El objeto brilla con energía mágica, pero nada parece suceder...");
                break;
        }
        
        // Efectos específicos de la trama
        aplicarEfectosTrama(jugador);
    }
    
    private void aplicarEfectosTrama(Jugador jugador) {
        // Efectos que avanzan la trama del juego
        switch (nombre.toLowerCase()) {
            case "antorcha de la curiosidad":
                System.out.println("Con la antorcha, puedes ver pasajes ocultos en la oscuridad.");
                break;
                
            case "espejo de la verdad":
                System.out.println("El espejo refleja la verdadera naturaleza de las cosas.");
                break;
                
            case "llave del progreso":
                System.out.println("Esta llave abre puertas que antes estaban selladas.");
                break;
                
            case "medalla de autoconocimiento":
                System.out.println("La medalla te da claridad sobre tus fortalezas internas.");
                jugador.ganarExperiencia(25);
                break;
                
            case "fragmento de historia":
                System.out.println("El fragmento revela parte de la historia olvidada.");
                // Revelar pista de la trama
                break;
        }
    }
    
    private boolean esReutilizable() {
        // Algunos items especiales se pueden usar múltiples veces
        switch (efectoUnico.toLowerCase()) {
            case "iluminar_sala":
            case "revelar_verdades":
            case "sanar_recuerdos":
                return true;
            default:
                return false;
        }
    }
    
    // Getters específicos
    public String getEfectoUnico() { return efectoUnico; }
    public boolean isUsado() { return usado; }
    public boolean isNecesarioParaProgreso() { return necesarioParaProgreso; }
    public String getRelacionPuzzle() { return relacionPuzzle; }
    
    // Métodos estáticos para crear items especiales de la trama
    public static ItemEspecial crearAntorchaCuriosidad() {
        return new ItemEspecial("Antorcha de la Curiosidad", 
                               "Ilumina lo que los ojos no pueden ver", 
                               TipoItem.ANTORCHA, 0, "iluminar_sala", true, "sala_oscura");
    }
    
    public static ItemEspecial crearEspejoVerdad() {
        return new ItemEspecial("Espejo de la Verdad", 
                               "Muestra lo real tras las apariencias", 
                               TipoItem.ESpejo, 0, "revelar_verdades", true, "puzzle_espejos");
    }
    
    public static ItemEspecial crearLlaveProgreso() {
        return new ItemEspecial("Llave del Progreso", 
                               "Abre el camino hacia adelante", 
                               TipoItem.LLAVE, 0, "abrir_puerta_especial", true, "puerta_sellada");
    }
    
    public static ItemEspecial crearFragmentoHistoria() {
        return new ItemEspecial("Fragmento de Historia", 
                               "Un pedazo de historia personal perdida", 
                               TipoItem.FRAGMENTO, 5, "", false, "");
    }
}