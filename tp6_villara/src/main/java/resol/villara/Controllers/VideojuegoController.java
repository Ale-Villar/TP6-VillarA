package resol.villara.Controllers;

import resol.villara.Models.Videojuego;
import resol.villara.Views.VideojuegoVista;
import java.sql.SQLException;
import java.util.List;

public class VideojuegoController {
    private Videojuego modelo;
    private VideojuegoVista vista;

    public VideojuegoController() {
        this.modelo = new Videojuego(); // Instancia del modelo[cite: 2]
        this.vista = new VideojuegoVista(); // Instancia de la vista[cite: 2]
    }

    public void iniciar() {
        int opcion = -1;
        while (opcion != 0) {
            // El controlador le pide a la vista que muestre el menú y capture la opción[cite: 2]
            opcion = vista.mostrarMenuVideojuegos(); 

            switch (opcion) {
                case 1:
                    // Falta implementar listar todos en el modelo, pero sería similar a listarDisponibles
                    vista.mostrarMensaje("Función 'Listar todos' en desarrollo...");
                    break;
                case 3:
                    agregarVideojuego();
                    break;
                case 6:
                    listarReposicion();
                    break;
                case 7:
                    listarDisponibles();
                    break;
                case 0:
                    vista.mostrarMensaje("Volviendo al menú principal...");
                    break;
                default:
                    vista.mostrarMensaje("Opción no válida.");
            }
        }
    }

    // El Controlador orquesta: pide datos a la Vista, se los da al Modelo y maneja errores[cite: 2]
    private void agregarVideojuego() {
        try {
            Videojuego nuevoJuego = vista.pedirDatosNuevoVideojuego();
            modelo.crearVideojuego(nuevoJuego);
            vista.mostrarMensaje("¡Videojuego agregado exitosamente!");
        } catch (SQLException e) {
            vista.mostrarMensaje("Error en la base de datos al guardar: " + e.getMessage()); //[cite: 3]
        } catch (Exception e) {
            vista.mostrarMensaje("Regla de negocio fallida: " + e.getMessage()); //[cite: 3]
        }
    }

    private void listarDisponibles() {
        try {
            // El modelo devuelve los datos, el controlador los pasa a la vista[cite: 2]
            List<Videojuego> disponibles = modelo.listarVideojuegosDisponibles();
            vista.mostrarListaVideojuegos(disponibles);
        } catch (SQLException e) {
            vista.mostrarMensaje("Error al obtener la lista de disponibles: " + e.getMessage());
        }
    }

    private void listarReposicion() {
        try {
            List<Videojuego> reposicion = modelo.listarVideojuegosParaReposicion();
            vista.mostrarMensaje("--- Videojuegos que necesitan reposición ---");
            vista.mostrarListaVideojuegos(reposicion);
        } catch (SQLException e) {
            vista.mostrarMensaje("Error al consultar reposición: " + e.getMessage());
        }
    }
}