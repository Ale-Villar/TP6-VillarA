package resol.villara.Models;

import resol.villara.config.ConexionBD;
import resol.villara.exceptions.VideoJuegoNoEncontradoException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Videojuego {
    private int id;
    private String nombre;
    private String genero;
    private double precio;
    private int unidadesDisponibles; //[cite: 3]
    private int nivelReposicion; //[cite: 3]
    private int suspendido; // 1 = disponible, 0 = no disponible[cite: 3]

    public Videojuego() {
    }

    public Videojuego(String nombre, String genero, double precio, int unidadesDisponibles, int nivelReposicion, int suspendido) {
        this.nombre = nombre;
        this.genero = genero;
        this.precio = precio;
        this.unidadesDisponibles = unidadesDisponibles;
        this.nivelReposicion = nivelReposicion;
        this.suspendido = suspendido;
    }

    public Videojuego(int id, String nombre, String genero, double precio, int unidadesDisponibles, int nivelReposicion, int suspendido) {
        this.id = id;
        this.nombre = nombre;
        this.genero = genero;
        this.precio = precio;
        this.unidadesDisponibles = unidadesDisponibles;
        this.nivelReposicion = nivelReposicion;
        this.suspendido = suspendido;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getUnidadesDisponibles() {
        return unidadesDisponibles;
    }

    public void setUnidadesDisponibles(int unidadesDisponibles) {
        this.unidadesDisponibles = unidadesDisponibles;
    }

    public int getNivelReposicion() {
        return nivelReposicion;
    }

    public void setNivelReposicion(int nivelReposicion) {
        this.nivelReposicion = nivelReposicion;
    }

    public int getSuspendido() {
        return suspendido;
    }

    public void setSuspendido(int suspendido) {
        this.suspendido = suspendido;
    }
    
    // --- LÓGICA DE NEGOCIO Y BASE DE DATOS (JDBC) ---
   public boolean necesitaReposicion(){
        return this.unidadesDisponibles < this.nivelReposicion; 
    }

    public void crearTabla() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS videojuegos (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "nombre VARCHAR(100) NOT NULL, " +
                "genero VARCHAR(50) NOT NULL, " +
                "precio DECIMAL(10,2) NOT NULL, " +
                "unidadesDisponibles INT NOT NULL, " +
                "nivelReposicion INT NOT NULL, " +
                "suspendido INT NOT NULL)";
        try (Connection conn = ConexionBD.obtenerConexion();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } 
    }
    
    
    public void crearVideojuego(Videojuego v) throws SQLException, Exception {
       if (v.getPrecio() <= 0 || v.getUnidadesDisponibles() < 0) {
            throw new Exception("Error: El precio debe ser mayor a 0 y las unidades disponibles no pueden ser negativas.");
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
    }
    public List<Videojuego> listarVideojuegosDisponibles() throws SQLException {
        List<Videojuego> lista = new ArrayList<>();
        // Regla: Los juegos suspendidos no se muestran en el listado de disponibles[cite: 3]
        String sql = "SELECT * FROM videojuegos WHERE suspendido = 1";

        try (Connection conn = ConexionBD.obtenerConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Videojuego v = new Videojuego(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("genero"),
                        rs.getDouble("precio"),
                        rs.getInt("unidadesDisponibles"),
                        rs.getInt("nivelReposicion"),
                        rs.getInt("suspendido")
                );
                lista.add(v);
            }
        }
        return lista;
    }

    public List<Videojuego> listarVideojuegosParaReposicion() throws SQLException {
        List<Videojuego> lista = new ArrayList<>();
        // Regla: mostrar los que necesitan reposición[cite: 3]
        String sql = "SELECT * FROM videojuegos WHERE unidadesDisponibles < nivelReposicion";

        try (Connection conn = ConexionBD.obtenerConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Videojuego v = new Videojuego(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("genero"),
                        rs.getDouble("precio"),
                        rs.getInt("unidadesDisponibles"),
                        rs.getInt("nivelReposicion"),
                        rs.getInt("suspendido")
                );
                lista.add(v);
            }
        }
        return lista;
    }

    public Videojuego buscarPorId(int id) throws VideoJuegoNoEncontradoException, SQLException {
        String sql = "SELECT * FROM videojuegos WHERE id = ?";

        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
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
        }
        throw new VideoJuegoNoEncontradoException("No existe un videojuego con el ID: " + id);
    }
}