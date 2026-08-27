package resol.villara;

import resol.villara.Controllers.VideojuegoController;
import resol.villara.Controllers.VentaController;
import resol.villara.Models.Videojuego;
import resol.villara.Models.Venta;
import resol.villara.Views.VistaGeneral;

public class Main {
    public static void main(String[] args) {
        
        // 1. Inicializar la base de datos y crear tablas si no existen
        try {
            new Videojuego().crearTabla();
            new Venta().crearTabla();
            System.out.println("Base de datos conectada y tablas verificadas.");
        } catch (Exception e) {
            System.out.println("Error crítico al iniciar la base de datos: " + e.getMessage());
            return; // Detiene la ejecución si falla la BD
        }

        // 2. Instanciar la vista principal y los controladores
        VistaGeneral vistaGeneral = new VistaGeneral();
        VideojuegoController videojuegoController = new VideojuegoController();
        VentaController ventaController = new VentaController();

        // 3. Bucle del menú principal con switch[cite: 3]
        int opcion = -1;
        while (opcion != 0) {
            opcion = vistaGeneral.mostrarMenuPrincipal(); //[cite: 3]

            switch (opcion) {
                case 1:
                    videojuegoController.iniciar(); // Deriva al menú de videojuegos[cite: 3]
                    break;
                case 2:
                    ventaController.iniciar(); // Deriva al menú de ventas[cite: 3]
                    break;
                case 0:
                    System.out.println("Saliendo del sistema de gestión...");
                    break;
                default:
                    System.out.println("Opción incorrecta. Intente nuevamente.");
            }
        }
    }
}