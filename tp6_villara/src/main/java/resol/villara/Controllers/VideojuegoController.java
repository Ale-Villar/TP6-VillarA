package resol.villara.Controllers;

import resol.villara.DAO.VideoJuegoDAO;
import resol.villara.DAO.VideoJuegoDAOImpl;
import resol.villara.Models.Videojuego;
import resol.villara.DTOS.VideojuegoDto;
import resol.villara.Views.VideojuegoVista;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VideojuegoController {
    private final VideoJuegoDAO dao;
    private final VideojuegoVista vista;

    public VideojuegoController() {
        this.dao = new VideoJuegoDAOImpl();
        this.vista = new VideojuegoVista(); 
    }

    public void iniciar() {
        int opcion = -1;
        while (opcion != 0) {
            opcion = vista.mostrarMenuVideojuegos();
            switch (opcion) {
                case 3: agregarVideojuego(); break;
                case 6: listarReposicion(); break;
                case 7: listarDisponibles(); break;
                case 0: vista.mostrarMensaje("Volviendo al menú principal..."); break;
                default: vista.mostrarMensaje("Opción en desarrollo o no válida.");
            }
        }
    }

    private void agregarVideojuego() {
        try {
            Videojuego nuevoJuego = vista.pedirDatosNuevoVideojuego();
            dao.agregarVideojuego(nuevoJuego);
            vista.mostrarMensaje("¡Videojuego agregado exitosamente!");
        } catch (SQLException e) {
            vista.mostrarMensaje("Error de BD: " + e.getMessage());
        } catch (Exception e) {
            vista.mostrarMensaje("Regla de negocio fallida: " + e.getMessage());
        }
    }

    private void listarDisponibles() {
        try {
            List<Videojuego> disponibles = dao.listarDisponibles();
            List<VideojuegoDto> listaDtos = new ArrayList<>();
            for (Videojuego v : disponibles) {
                listaDtos.add(new VideojuegoDto(v.getId(), v.getNombre(), v.getPrecio(), v.necesitaReposicion()));
            }
            vista.mostrarListaVideojuegos(listaDtos);
        } catch (SQLException e) {
            vista.mostrarMensaje("Error al obtener disponibles: " + e.getMessage());
        }
    }

    private void listarReposicion() {
        try {
            List<Videojuego> reposicion = dao.listarQueNecesitanReposicion();
            List<VideojuegoDto> listaDtos = new ArrayList<>();
            for (Videojuego v : reposicion) {
                listaDtos.add(new VideojuegoDto(v.getId(), v.getNombre(), v.getPrecio(), v.necesitaReposicion()));
            }
            vista.mostrarMensaje("--- Videojuegos que necesitan reposición ---");
            vista.mostrarListaVideojuegos(listaDtos);
        } catch (SQLException e) {
            vista.mostrarMensaje("Error al consultar reposición: " + e.getMessage());
        }
    }
}