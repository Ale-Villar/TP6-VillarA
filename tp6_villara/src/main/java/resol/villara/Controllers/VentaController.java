package resol.villara.Controllers;

import resol.villara.DAO.VentaDAO;
import resol.villara.DAO.VentaDAOImpl;
import resol.villara.DAO.VideoJuegoDAO;
import resol.villara.DAO.VideoJuegoDAOImpl;
import resol.villara.Models.Venta;
import resol.villara.Models.Videojuego;
import resol.villara.DTOS.VentaDto;
import resol.villara.Views.VentaVista;
import resol.villara.exceptions.VentaInvalidaException;
import resol.villara.exceptions.VideoJuegoNoEncontradoException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VentaController {
    private final VentaDAO ventaDao;
    private final VideoJuegoDAO videojuegoDao;
    private final VentaVista vista;

    public VentaController() {
        this.ventaDao = new VentaDAOImpl();
        this.videojuegoDao = new VideoJuegoDAOImpl();
        this.vista = new VentaVista(); 
    }

    public void iniciar() {
        int opcion = -1;
        while (opcion != 0) {
            opcion = vista.mostrarMenuVentas(); 
            switch (opcion) {
                case 3: registrarVenta(); break;
                case 5: reporteMesActual(); break;
                case 0: vista.mostrarMensaje("Volviendo al menú principal..."); break;
                default: vista.mostrarMensaje("Opción en desarrollo o no válida.");
            }
        }
    }

    private void registrarVenta() {
        try {
            Venta nueva = vista.pedirDatosNuevaVenta(); 
            ventaDao.registrarVenta(nueva); 
            vista.mostrarMensaje("Venta registrada exitosamente. Se descontó el stock.");
        } catch (VentaInvalidaException | VideoJuegoNoEncontradoException e) {
            vista.mostrarMensaje("Validación fallida: " + e.getMessage()); 
        } catch (SQLException e) {
            vista.mostrarMensaje("Error en BD: " + e.getMessage());
        } catch (Exception e) {
            vista.mostrarMensaje("Error: " + e.getMessage());
        }
    }

    private void reporteMesActual() {
        try {
            List<Venta> reporte = ventaDao.listarVentasDelMes();
            List<VentaDto> reporteDto = new ArrayList<>();
            
            for (Venta v : reporte) {
                Videojuego juego = videojuegoDao.obtenerPorId(v.getVideojuegoId());
                reporteDto.add(new VentaDto(v.getId(), v.getFecha(), juego.getNombre(), v.getCantidad(), v.getPorcentajeDescuento(), v.getTotalFinal()));
            }
            
            vista.mostrarMensaje("--- Reporte de Ventas (Mes Actual) ---");
            vista.mostrarListadoVentas(reporteDto);
        } catch (SQLException | VideoJuegoNoEncontradoException e) {
            vista.mostrarMensaje("Error al generar reporte: " + e.getMessage());
        }
    }
}