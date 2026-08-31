package resol.villara.Views;

import resol.villara.Models.Venta;
import resol.villara.DTOS.VentaDto; // Importamos el DTO
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
        System.out.println("\n=== GESTIÓN DE VENTAS ==="); 
        System.out.println("1. Listar ventas"); 
        System.out.println("2. Buscar venta por ID"); 
        System.out.println("3. Registrar venta"); 
        System.out.println("4. Buscar ventas de un videojuego"); 
        System.out.println("5. Reporte de ventas del mes actual"); 
        System.out.println("0. Volver al menú principal"); 
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

    // Modificado para recibir VentaDto y mostrar el nombre del juego en lugar del ID
    public void mostrarListadoVentas(List<VentaDto> lista) {
        if (lista.isEmpty()) {
            System.out.println("No se encontraron ventas.");
            return;
        }
        for (VentaDto v : lista) {
            System.out.println("ID Venta: " + v.getId() + " | Fecha: " + v.getFecha() + 
                               " | Juego: " + v.getNombreVideojuego() + // Usamos el nombre que viene en el DTO
                               " | Cant: " + v.getCantidad() + 
                               " | Desc: " + v.getPorcentajeDescuento() + "% | Total: $" + v.getTotal());
        }
    }
}