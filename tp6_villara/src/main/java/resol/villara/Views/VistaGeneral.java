package resol.villara.Views;

import java.util.Scanner;

public class VistaGeneral {
    private Scanner scanner;

    public VistaGeneral() {
        this.scanner = new Scanner(System.in);
    }

    public int mostrarMenuPrincipal() {
        System.out.println("\n=== MENÚ PRINCIPAL ==="); //
        System.out.println("1. Gestión de Videojuegos"); //
        System.out.println("2. Gestión de Ventas"); //[cite: 3]
        System.out.println("0. Salir"); //[cite: 3]
        System.out.print("Seleccione una opción: "); //[cite: 3]
        
        int opcion = scanner.nextInt();
        scanner.nextLine(); // Limpiar el buffer
        return opcion;
    }
}