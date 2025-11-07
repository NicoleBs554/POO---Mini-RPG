package levels;

import entities.Jugador;
import java.util.*;

public abstract class Piso {
    protected Jugador p;
    protected Scanner in;
    protected Set<String> restedRooms = new HashSet<>();
    
    public Piso(Jugador p, Scanner in) {
        this.p = p;
        this.in = in;
    }
    
    public abstract void play();
    
    protected boolean preRoom(String roomName) {
    while (true) {
        System.out.println("\n— Zona de preparación: " + roomName + " —");
        System.out.println("Opciones: 1. Descansar (recuperar HP/EN una vez) 2. Inventario 3. Ver estado 4. Continuar 5. Cheats");
        System.out.println("Nota: solo puedes descansar 1 vez por sala.");
        String o = in.nextLine().trim();
        if ("1".equals(o)) {
            if (restedRooms.contains(roomName)) {
                System.out.println("Ya descansaste antes de esta sala.");
            } else {
                int heal = 6;
                int restoreEn = 2;
                // Usar getters y setters en lugar de acceso directo
                p.setHp(p.getHp() + heal);
                p.setEnergia(p.getEnergia() + restoreEn);
                restedRooms.add(roomName);
                System.out.println("Descansas brevemente. Recuperas " + heal + " HP y " + restoreEn + " EN.");
                p.mostrarEstado();
            }
        } else if ("2".equals(o)) {
            p.mostrarInventario();
            System.out.println("Escribe el nombre exacto del objeto para usarlo, o ENTER para volver:");
            String item = in.nextLine().trim();
            if (!item.isEmpty()) p.usarItem(item);
        } else if ("3".equals(o)) {
            p.mostrarEstado();
        } else if ("5".equals(o)) {
            cheatsMenu();
        } else {
            // Usar getter en lugar de acceso directo
            if (p.isCheatSaltarSalas()) {
                System.out.println("Cheat 'Saltar salas' activo. ¿Deseas saltar esta sala? (S/N)");
                String s = in.nextLine().trim().toUpperCase();
                if ("S".equals(s) || "Y".equals(s)) {
                    System.out.println("Sala " + roomName + " saltada por cheat.");
                    return true;
                }
            }
            return false;
        }
    }
}

protected void cheatsMenu() {
    while (true) {
        System.out.println("\n--- Menú Cheats ---");
        // Usar getters en lugar de acceso directo
        System.out.println("Cheat 1: Vida infinita (actual: " + (p.isCheatVidaInfinita() ? "ON" : "OFF") + ")");
        System.out.println("Cheat 2: Matar de un golpe (OneHitKill) (actual: " + (p.isCheatUnGolpe() ? "ON" : "OFF") + ")");
        System.out.println("Cheat 3: Saltar salas (permite 'saltar sala' en la preRoom) (actual: " + (p.isCheatSaltarSalas() ? "ON" : "OFF") + ")");
        System.out.println("Opciones: A. Alternar 1  B. Alternar 2  C. Alternar 3  X. Volver");
        String c = in.nextLine().trim().toUpperCase();
        if ("A".equals(c)) { 
            p.setCheatVidaInfinita(!p.isCheatVidaInfinita());
            System.out.println("Vida infinita -> " + p.isCheatVidaInfinita()); 
        } else if ("B".equals(c)) { 
            p.setCheatUnGolpe(!p.isCheatUnGolpe());
            System.out.println("OneHitKill -> " + p.isCheatUnGolpe()); 
        } else if ("C".equals(c)) { 
            p.setCheatSaltarSalas(!p.isCheatSaltarSalas());
            System.out.println("Saltar salas -> " + p.isCheatSaltarSalas()); 
        } else if ("X".equals(c)) break;
        else System.out.println("Opción inválida.");
    }
}

    
}