package resol.villara.Views;

import resol.villara.Models.Videojuego;
import java.util.List;
import java.util.Scanner;

public class VideojuegoVista {
    private Scanner scanner;

    public VideojuegoVista() {
        this.scanner = new Scanner(System.in);
    }

    // Muestra el menú y devuelve la opción elegida por el usuario[cite: 3]
    public int mostrarMenuVideojuegos() {
        System.out.println("\n=== GESTIÓN DE VIDEOJUEGOS ==="); //[cite: 3]
        System.out.println("1. Listar videojuegos"); //[cite: 3]
        System.out.println("2. Buscar videojuego por ID"); //[cite: 3]
        System.out.println("3. Agregar videojuego"); //[cite: 3]
        System.out.println("4. Actualizar videojuego"); //[cite: 3]
        System.out.println("5. Eliminar videojuego"); //[cite: 3]
        System.out.println("6. Videojuegos que necesitan reposición"); //[cite: 3]
        System.out.println("7. Videojuegos disponibles para la venta"); //[cite: 3]
        System.out.println("0. Volver al menú principal"); //[cite: 3]
        System.out.print("Seleccione una opción: ");
        
        int opcion = scanner.nextInt();
        scanner.nextLine(); // Limpiar el buffer del teclado
        return opcion;
    }

    // Pide los datos por teclado y arma el objeto SIN ID (para crear uno nuevo)[cite: 2]
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

    // Método genérico para mostrar cualquier mensaje de texto[cite: 2]
    public void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
    }

    // Método para imprimir una lista de videojuegos recibida desde el Controlador[cite: 2]
    public void mostrarListaVideojuegos(List<Videojuego> lista) {
        if (lista.isEmpty()) {
            System.out.println("No hay videojuegos para mostrar.");
            return;
        }
        for (Videojuego v : lista) {
            System.out.println("ID: " + v.getId() + " | Nombre: " + v.getNombre() + " | Precio: $" + v.getPrecio() + " | Stock: " + v.getUnidadesDisponibles() + " | Reposición en: " + v.getNivelReposicion());
        }
    }
}