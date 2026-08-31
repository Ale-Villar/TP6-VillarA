package resol.villara.DAO;

import resol.villara.Models.Videojuego;
import resol.villara.exceptions.VideoJuegoNoEncontradoException;
import java.sql.SQLException;
import java.util.List;

public interface VideoJuegoDAO {
    List<Videojuego> listarVideojuegos() throws SQLException;
    List<Videojuego> listarDisponibles() throws SQLException;
    List<Videojuego> listarQueNecesitanReposicion() throws SQLException;
    Videojuego obtenerPorId(long id) throws SQLException, VideoJuegoNoEncontradoException;
    Videojuego agregarVideojuego(Videojuego v) throws SQLException, Exception;
    boolean actualizarVideojuego(Videojuego v) throws SQLException;
    boolean eliminarVideojuego(long id) throws SQLException;
}
