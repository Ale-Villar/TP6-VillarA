package resol.villara.Controllers;
import resol.villara.Models.Venta;
import resol.villara.Views.VentaVista;
import resol.villara.exceptions.VentaInvalidaException;
import resol.villara.exceptions.VideoJuegoNoEncontradoException;
import java.sql.SQLException;
import java.util.List;

public class VentaController {
    private Venta modelo;
    private VentaVista vista;

    public VentaController() {
        this.modelo = new Venta(); // Instancia del modelo[cite: 2]
        this.vista = new VentaVista(); // Instancia de la vista[cite: 2]
    }

    public void iniciar() {
        int opcion = -1;
        while (opcion != 0) {
            opcion = vista.mostrarMenuVentas(); //[cite: 3]

            switch (opcion) {
                case 3:
                    registrarVenta();
                    break;
                case 5:
                    reporteMesActual();
                    break;
                case 0:
                    vista.mostrarMensaje("Volviendo...");
                    break;
                default:
                    vista.mostrarMensaje("Opción en desarrollo o no válida.");
            }
        }
    }

    private void registrarVenta() {
        try {
            Venta nueva = vista.pedirDatosNuevaVenta(); // La vista escanea los datos[cite: 2]
            modelo.registrarVenta(nueva); // El modelo verifica stock, descuenta y guarda[cite: 2, 3]
            vista.mostrarMensaje("Venta registrada exitosamente. Se descontó el stock.");
        } catch (VentaInvalidaException | VideoJuegoNoEncontradoException e) {
            vista.mostrarMensaje("Validación fallida: " + e.getMessage()); // Captura excepciones[cite: 3]
        } catch (SQLException e) {
            vista.mostrarMensaje("Error en base de datos: " + e.getMessage());
        } catch (Exception e) {
            vista.mostrarMensaje("Error de stock: " + e.getMessage());
        }
    }

    private void reporteMesActual() {
        try {
            List<Venta> reporte = modelo.reporteMesActual(); // Modelo consulta base de datos[cite: 2, 3]
            vista.mostrarMensaje("--- Reporte de Ventas (Mes Actual) ---");
            vista.mostrarListadoVentas(reporte); // Vista dibuja resultados[cite: 2]
        } catch (SQLException e) {
            vista.mostrarMensaje("Error al generar reporte: " + e.getMessage());
        }
    }
}
