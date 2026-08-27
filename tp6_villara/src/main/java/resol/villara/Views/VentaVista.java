package resol.villara.Views;

import resol.villara.Models.Venta;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;
public class VentaVista {
    private Scanner scanner;

    public VentaVista() {
        this.scanner = new Scanner(System.in);
    }

    public int mostrarMenuVentas() {
        System.out.println("\n=== GESTIÓN DE VENTAS ==="); //[cite: 3]
        System.out.println("1. Listar ventas"); //[cite: 3]
        System.out.println("2. Buscar venta por ID"); //[cite: 3]
        System.out.println("3. Registrar venta"); //[cite: 3]
        System.out.println("4. Buscar ventas de un videojuego"); //[cite: 3]
        System.out.println("5. Reporte de ventas del mes actual"); //[cite: 3]
        System.out.println("0. Volver al menú principal"); //[cite: 3]
        System.out.print("Seleccione una opción: ");
        
        int opcion = scanner.nextInt();
        scanner.nextLine();
        return opcion;
    }
    public Venta pedirDatosNuevaVenta() {
        System.out.print("Ingrese el ID del videojuego a vender: ");
        int idJuego = scanner.nextInt();
        System.out.print("Ingrese la cantidad a vender: ");
        int cantidad = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Ingrese la fecha (YYYY-MM-DD): ");
        String fechaStr = scanner.nextLine();
        
        LocalDate fecha = LocalDate.parse(fechaStr, DateTimeFormatter.ISO_LOCAL_DATE);
        return new Venta(fecha, cantidad, idJuego);
    }

    public void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
    }

    public void mostrarListadoVentas(List<Venta> lista) {
        if (lista.isEmpty()) {
            System.out.println("No se encontraron ventas.");
            return;
        }
        // Regla: mostrar id, fecha, videojuego, cantidad, descuento %, total[cite: 3]
        for (Venta v : lista) {
            System.out.println("ID Venta: " + v.getId() + " | Fecha: " + v.getFecha() + 
                               " | ID Juego: " + v.getVideojuegoId() + " | Cant: " + v.getCantidad() + 
                               " | Desc: " + v.getPorcentajeDescuento() + "% | Total: $" + v.getTotalFinal());
        }
    }
}
