package resol.villara.DAO;

import resol.villara.Models.Venta;
import resol.villara.Models.Videojuego;
import resol.villara.config.ConexionBD;
import resol.villara.exceptions.VentaInvalidaException;
import resol.villara.exceptions.VideoJuegoNoEncontradoException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class VentaDAOImpl implements VentaDAO {
    
    @Override
    public List<Venta> listarVentas() throws SQLException {
        List<Venta> lista = new ArrayList<>();
        String sql = "SELECT * FROM ventas";
        try (Connection conn = ConexionBD.obtenerConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(extraerVenta(rs));
            }
        }
        return lista;
    }

    @Override
    public void registrarVenta(Venta venta) throws SQLException, VentaInvalidaException, VideoJuegoNoEncontradoException, Exception {
        if (venta.getCantidad() <= 0) {
            throw new VentaInvalidaException("La cantidad debe ser mayor a 0.");
        }
        if (venta.getFecha().isAfter(LocalDate.now())) {
            throw new VentaInvalidaException("La fecha de venta no puede ser en el futuro.");
        }

        VideoJuegoDAO videojuegoDao = new VideoJuegoDAOImpl();
        Videojuego juego = videojuegoDao.obtenerPorId(venta.getVideojuegoId());

        if (juego.getUnidadesDisponibles() < venta.getCantidad()) {
            throw new Exception("Stock insuficiente. Solo hay " + juego.getUnidadesDisponibles() + " unidades disponibles.");
        }

        int descuento = 0;
        if (venta.getCantidad() >= 2 && venta.getCantidad() <= 4) descuento = 5;
        else if (venta.getCantidad() >= 5 && venta.getCantidad() <= 9) descuento = 10;
        else if (venta.getCantidad() >= 10) descuento = 15;

        double subtotal = juego.getPrecio() * venta.getCantidad();
        double montoDescuento = subtotal * (descuento / 100.0);
        double total = subtotal - montoDescuento;

        try (Connection conn = ConexionBD.obtenerConexion()) {
            String sqlStock = "UPDATE videojuegos SET unidadesDisponibles = unidadesDisponibles - ? WHERE id = ?";
            try (PreparedStatement stmtStock = conn.prepareStatement(sqlStock)) {
                stmtStock.setInt(1, venta.getCantidad());
                stmtStock.setLong(2, venta.getVideojuegoId());
                stmtStock.executeUpdate();
            }

            String sqlVenta = "INSERT INTO ventas (fecha, cantidad, videojuegoId, descuento, total) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement stmtVenta = conn.prepareStatement(sqlVenta)) {
                stmtVenta.setDate(1, Date.valueOf(venta.getFecha()));
                stmtVenta.setInt(2, venta.getCantidad());
                stmtVenta.setLong(3, venta.getVideojuegoId());
                stmtVenta.setInt(4, descuento);
                stmtVenta.setDouble(5, total);
                stmtVenta.executeUpdate();
            }
        }
    }

    @Override
    public List<Venta> listarPorVideojuego(long idVideojuego) throws SQLException {
        List<Venta> lista = new ArrayList<>();
        String sql = "SELECT * FROM ventas WHERE videojuegoId = ?";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, idVideojuego);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(extraerVenta(rs));
                }
            }
        }
        return lista;
    }

    @Override
    public List<Venta> listarVentasDelMes() throws SQLException {
        List<Venta> lista = new ArrayList<>();
        String sql = "SELECT * FROM ventas WHERE MONTH(fecha) = MONTH(CURRENT_DATE()) AND YEAR(fecha) = YEAR(CURRENT_DATE())";
        try (Connection conn = ConexionBD.obtenerConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(extraerVenta(rs));
            }
        }
        return lista;
    }

    private Venta extraerVenta(ResultSet rs) throws SQLException {
        Venta v = new Venta(
                rs.getDate("fecha").toLocalDate(),
                rs.getInt("cantidad"),
                rs.getInt("videojuegoId")
        );
        v.setId(rs.getInt("id"));
        v.setPorcentajeDescuento(rs.getInt("descuento"));
        v.setTotalFinal(rs.getDouble("total"));
        return v;
    }
}