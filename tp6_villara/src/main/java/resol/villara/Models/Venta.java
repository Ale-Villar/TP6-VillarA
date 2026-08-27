package resol.villara.Models;

import resol.villara.config.ConexionBD;
import resol.villara.exceptions.VentaInvalidaException;
import resol.villara.exceptions.VideoJuegoNoEncontradoException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Venta {
    private int id;
    private LocalDate fecha;
    private int cantidad;
    private int videojuegoId;

    private int porcentajeDescuento; 
    private double totalFinal;

    public Venta() {}
    public Venta(LocalDate fecha, int cantidad, int videojuegoId) {
        this.fecha = fecha;
        this.cantidad = cantidad;
        this.videojuegoId = videojuegoId;
    }

    public Venta(int id, LocalDate fecha, int cantidad, int videojuegoId) {
        this.id = id;
        this.fecha = fecha;
        this.cantidad = cantidad;
        this.videojuegoId = videojuegoId;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public LocalDate getFecha() {
        return fecha;
    }
    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }
    public int getCantidad() {
        return cantidad;
    }
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
    public int getVideojuegoId() {
        return videojuegoId;
    }
    public void setVideojuegoId(int videojuegoId) {
        this.videojuegoId = videojuegoId;
    }
    public int getPorcentajeDescuento() {
        return porcentajeDescuento;
    }
    public void setPorcentajeDescuento(int porcentajeDescuento) {
        this.porcentajeDescuento = porcentajeDescuento;
    }
    public double getTotalFinal() {
        return totalFinal;
    }
    public void setTotalFinal(double totalFinal) {
        this.totalFinal = totalFinal;
    }

    public void crearTabla() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS ventas (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "fecha DATE NOT NULL, " +
                "cantidad INT NOT NULL, " +
                "videojuegoId INT NOT NULL, " +
                "descuento INT NOT NULL, " +
                "total DECIMAL(10,2) NOT NULL, " +
                "FOREIGN KEY (videojuegoId) REFERENCES videojuegos(id))";
        try (Connection conn = ConexionBD.obtenerConexion();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        }
    }

    public void registrarVenta(Venta venta) throws SQLException, VentaInvalidaException, VideoJuegoNoEncontradoException, Exception {
        // Regla: La cantidad debe ser mayor a cero[cite: 3, 8]
        if (venta.cantidad <= 0) {
            throw new VentaInvalidaException("La cantidad debe ser mayor a 0.");
        }
        
        // Regla: La fecha no puede ser futura[cite: 3]
        if (venta.fecha.isAfter(LocalDate.now())) {
            throw new VentaInvalidaException("La fecha de venta no puede ser en el futuro.");
        }

        // Instanciamos el modelo de Videojuego para buscar sus datos[cite: 2]
        Videojuego modeloJuego = new Videojuego();
        Videojuego juego = modeloJuego.buscarPorId(venta.videojuegoId);

        // Regla: Verificar stock suficiente[cite: 3]
        if (juego.getUnidadesDisponibles() < venta.cantidad) {
            throw new Exception("Stock insuficiente. Solo hay " + juego.getUnidadesDisponibles() + " unidades disponibles.");
        }

        // Regla: Calcular descuento según volumen[cite: 3]
        int descuento = 0;
        if (venta.cantidad >= 2 && venta.cantidad <= 4) descuento = 5;
        else if (venta.cantidad >= 5 && venta.cantidad <= 9) descuento = 10;
        else if (venta.cantidad >= 10) descuento = 15;

        // Calcular el total final[cite: 3]
        double subtotal = juego.getPrecio() * venta.cantidad;
        double montoDescuento = subtotal * (descuento / 100.0);
        double total = subtotal - montoDescuento;

        try (Connection conn = ConexionBD.obtenerConexion()) {
            // Descontar stock del videojuego[cite: 3]
            String sqlActualizarStock = "UPDATE videojuegos SET unidadesDisponibles = unidadesDisponibles - ? WHERE id = ?";
            try (PreparedStatement stmtStock = conn.prepareStatement(sqlActualizarStock)) {
                stmtStock.setInt(1, venta.cantidad);
                stmtStock.setInt(2, venta.videojuegoId);
                stmtStock.executeUpdate();
            }

            // Insertar la venta[cite: 3]
            String sqlInsertar = "INSERT INTO ventas (fecha, cantidad, videojuegoId, descuento, total) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement stmtVenta = conn.prepareStatement(sqlInsertar)) {
                stmtVenta.setDate(1, Date.valueOf(venta.fecha));
                stmtVenta.setInt(2, venta.cantidad);
                stmtVenta.setInt(3, venta.videojuegoId);
                stmtVenta.setInt(4, descuento);
                stmtVenta.setDouble(5, total);
                stmtVenta.executeUpdate();
            }
        }
    }

    public List<Venta> reporteMesActual() throws SQLException {
        List<Venta> lista = new ArrayList<>();
        // Regla: mostrar ventas del mes actual[cite: 3]
        String sql = "SELECT * FROM ventas WHERE MONTH(fecha) = MONTH(CURRENT_DATE()) AND YEAR(fecha) = YEAR(CURRENT_DATE())";
        
        try (Connection conn = ConexionBD.obtenerConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Venta v = new Venta(rs.getDate("fecha").toLocalDate(), rs.getInt("cantidad"), rs.getInt("videojuegoId"));
                v.id = rs.getInt("id");
                v.porcentajeDescuento = rs.getInt("descuento");
                v.totalFinal = rs.getDouble("total");
                lista.add(v);
            }
        }
        return lista;
    }
}
