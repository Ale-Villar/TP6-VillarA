package resol.villara.DAO;

import resol.villara.Models.Videojuego;
import resol.villara.config.ConexionBD;
import resol.villara.exceptions.VideoJuegoNoEncontradoException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VideoJuegoDAOImpl implements VideoJuegoDAO {
    
    @Override
    public List<Videojuego> listarVideojuegos() throws SQLException {
        List<Videojuego> lista = new ArrayList<>();
        String sql = "SELECT * FROM videojuegos";
        try (Connection conn = ConexionBD.obtenerConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(extraerVideojuego(rs));
            }
        }
        return lista;
    }

    @Override
    public List<Videojuego> listarDisponibles() throws SQLException {
        List<Videojuego> lista = new ArrayList<>();
        String sql = "SELECT * FROM videojuegos WHERE suspendido = 1"; 
        try (Connection conn = ConexionBD.obtenerConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(extraerVideojuego(rs));
            }
        }
        return lista;
    }

    @Override
    public List<Videojuego> listarQueNecesitanReposicion() throws SQLException {
        List<Videojuego> lista = new ArrayList<>();
        String sql = "SELECT * FROM videojuegos WHERE unidadesDisponibles < nivelReposicion";
        try (Connection conn = ConexionBD.obtenerConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(extraerVideojuego(rs));
            }
        }
        return lista;
    }

    @Override
    public Videojuego obtenerPorId(long id) throws SQLException, VideoJuegoNoEncontradoException {
        String sql = "SELECT * FROM videojuegos WHERE id = ?";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extraerVideojuego(rs);
                }
            }
        }
        throw new VideoJuegoNoEncontradoException("No existe un videojuego con el ID: " + id);
    }

    @Override
    public Videojuego agregarVideojuego(Videojuego v) throws SQLException, Exception {
        if (v.getPrecio() <= 0 || v.getUnidadesDisponibles() < 0) { 
            throw new Exception("Error: El precio debe ser > 0 y unidades >= 0.");
        }

        String sql = "INSERT INTO videojuegos (nombre, genero, precio, unidadesDisponibles, nivelReposicion, suspendido) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, v.getNombre());
            stmt.setString(2, v.getGenero());
            stmt.setDouble(3, v.getPrecio());
            stmt.setInt(4, v.getUnidadesDisponibles());
            stmt.setInt(5, v.getNivelReposicion());
            stmt.setInt(6, v.getSuspendido());
            stmt.executeUpdate();
        }
        return v;
    }

    @Override
    public boolean actualizarVideojuego(Videojuego v) throws SQLException {
        String sql = "UPDATE videojuegos SET nombre=?, genero=?, precio=?, unidadesDisponibles=?, nivelReposicion=?, suspendido=? WHERE id=?";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, v.getNombre());
            stmt.setString(2, v.getGenero());
            stmt.setDouble(3, v.getPrecio());
            stmt.setInt(4, v.getUnidadesDisponibles());
            stmt.setInt(5, v.getNivelReposicion());
            stmt.setInt(6, v.getSuspendido());
            stmt.setInt(7, v.getId());
            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean eliminarVideojuego(long id) throws SQLException {
        String sql = "DELETE FROM videojuegos WHERE id = ?";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    private Videojuego extraerVideojuego(ResultSet rs) throws SQLException {
        return new Videojuego(
                rs.getInt("id"),
                rs.getString("nombre"),
                rs.getString("genero"),
                rs.getDouble("precio"),
                rs.getInt("unidadesDisponibles"),
                rs.getInt("nivelReposicion"),
                rs.getInt("suspendido")
        );
    }
}