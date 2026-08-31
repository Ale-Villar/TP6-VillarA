package resol.villara;

import resol.villara.Controllers.VideojuegoController;
import resol.villara.Controllers.VentaController;
import resol.villara.Views.VistaGeneral;

public class Main {
    public static void main(String[] args) {
        
        System.out.println("Iniciando el sistema de gestión...");

        VistaGeneral vistaGeneral = new VistaGeneral();
        VideojuegoController videojuegoController = new VideojuegoController();
        VentaController ventaController = new VentaController();

        int opcion = -1;
        while (opcion != 0) {
            opcion = vistaGeneral.mostrarMenuPrincipal(); 

            switch (opcion) {
                case 1:
                    videojuegoController.iniciar(); 
                    break;
                case 2:
                    ventaController.iniciar(); 
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