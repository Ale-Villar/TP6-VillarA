package resol.villara.DAO;

import resol.villara.Models.Venta;
import resol.villara.exceptions.VentaInvalidaException;
import resol.villara.exceptions.VideoJuegoNoEncontradoException;
import java.sql.SQLException;
import java.util.List;

public interface VentaDAO {
    List<Venta> listarVentas() throws SQLException;
    void registrarVenta(Venta venta) throws SQLException, VentaInvalidaException, VideoJuegoNoEncontradoException, Exception;
    List<Venta> listarPorVideojuego(long idVideojuego) throws SQLException;
    List<Venta> listarVentasDelMes() throws SQLException;
}
