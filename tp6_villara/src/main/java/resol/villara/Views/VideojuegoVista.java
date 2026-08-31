package resol.villara.Views;

import resol.villara.Models.Videojuego;
import resol.villara.DTOS.VideojuegoDto;
import java.util.List;
import java.util.Scanner;

public class VideojuegoVista {
    private Scanner scanner;

    public VideojuegoVista() {
        this.scanner = new Scanner(System.in);
    }

    public int mostrarMenuVideojuegos() {
        System.out.println("\n=== GESTIÓN DE VIDEOJUEGOS ==="); 
        System.out.println("1. Listar videojuegos"); 
        System.out.println("2. Buscar videojuego por ID"); 
        System.out.println("3. Agregar videojuego"); 
        System.out.println("4. Actualizar videojuego"); 
        System.out.println("5. Eliminar videojuego"); 
        System.out.println("6. Videojuegos que necesitan reposición"); 
        System.out.println("7. Videojuegos disponibles para la venta"); 
        System.out.println("0. Volver al menú principal"); 
        System.out.print("Seleccione una opción: ");
        
        int opcion = scanner.nextInt();
        scanner.nextLine(); 
        return opcion;
    }

    public Videojuego pedirDatosNuevoVideojuego() {
        System.out.print("Ingrese el nombre: ");
        String nombre = scanner.nextLine();
        System.out.print("Ingrese el género: ");
        String genero = scanner.nextLine();
        System.out.print("Ingrese el precio: ");
        double precio = scanner.nextDouble();
        System.out.print("Ingrese las unidades disponibles: ");
        int unidades = scanner.nextInt();
        System.out.print("Ingrese el nivel de reposición: ");
        int nivelRepo = scanner.nextInt();
        System.out.print("¿Está suspendido? (1=disponible, 0=no disponible): ");
        int suspendido = scanner.nextInt();

        return new Videojuego(nombre, genero, precio, unidades, nivelRepo, suspendido);
    }

    public void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
    }

    // La vista ahora imprime los datos desde el DTO
    public void mostrarListaVideojuegos(List<VideojuegoDto> lista) {
        if (lista.isEmpty()) {
            System.out.println("No hay videojuegos para mostrar.");
            return;
        }
        for (VideojuegoDto v : lista) {
            System.out.println("ID: " + v.getId() + " | Nombre: " + v.getNombre() + 
                               " | Precio: $" + v.getPrecio() + 
                               " | ¿Necesita reposición?: " + (v.isNecesitaReposicion() ? "Sí" : "No"));
        }
    }
}