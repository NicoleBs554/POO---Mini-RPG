package items;

public enum TipoItem {
    // Armas
    ARMA_MELÉE("Arma de Melée", "Para combate cuerpo a cuerpo"),
    ARMA_RANGO("Arma a Distancia", "Para ataques a distancia"),
    ARMA_MÁGICA("Arma Mágica", "Para ataques con energía mágica"),
    
    // Armaduras
    ARMADURA_LIGERA("Armadura Ligera", "Protección básica con movilidad"),
    ARMADURA_MEDIA("Armadura Media", "Balance entre protección y movilidad"),
    ARMADURA_PESADA("Armadura Pesada", "Máxima protección, menos movilidad"),
    ESCUDO("Escudo", "Protección adicional en combate"),
    
    // Consumibles
    POCIÓN("Poción", "Restaura salud o energía"),
    ELIXIR("Elixir", "Efectos temporales o permanentes"),
    VENDAJE("Vendaje", "Curación básica"),
    ANTÍDOTO("Antídoto", "Cura efectos de estado"),
    
    // Items especiales de la trama
    LLAVE("Llave", "Abre puertas o cofres"),
    ANTORCHA("Antorcha", "Fuente de luz y herramienta"),
    ESpejo("Espejo", "Herramienta para puzzles"),
    AMULETO("Amuleto", "Objeto con propiedades mágicas"),
    FRAGMENTO("Fragmento", "Pieza de colección o historia"),
    
    // Misceláneos
    MATERIAL("Material", "Para crafting o venta"),
    DOCUMENTO("Documento", "Información o pistas"),
    COLECCIONABLE("Coleccionable", "Objetos de valor sentimental");
    
    private final String nombre;
    private final String descripcion;
    
    TipoItem(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
    }
    
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
} {
    
}
