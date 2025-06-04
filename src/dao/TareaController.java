package dao;

import model.TareaModel;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class TareaController {
    private Connection conn;

    public TareaController() throws SQLException {
        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/lista_de_tareas", "root", "");
    }

    public void agregarTarea(TareaModel tarea) {
        String sql = "INSERT INTO tareas (nombre, descripcion, prioridad, estado, fecha_creacion, hora_inicio, hora_final) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, tarea.getNombre());
            stmt.setString(2, tarea.getDescripcion());
            stmt.setString(3, tarea.getPrioridad());
            stmt.setString(4, tarea.getEstado());
            stmt.setDate(5, Date.valueOf(tarea.getFechaCreacion()));

            if (tarea.getHoraInicio() != null)
                stmt.setTime(6, Time.valueOf(tarea.getHoraInicio()));
            else
                stmt.setNull(6, Types.TIME);

            if (tarea.getHoraFinal() != null)
                stmt.setTime(7, Time.valueOf(tarea.getHoraFinal()));
            else
                stmt.setNull(7, Types.TIME);

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void actualizarTarea(int id, TareaModel tarea) {
        // Control de tiempo según estado
        if (tarea.getEstado().equals("En proceso") && tarea.getHoraInicio() == null) {
            tarea.setHoraInicio(LocalTime.now());
        }
        if (tarea.getEstado().equals("Terminada") && tarea.getHoraFinal() == null) {
            tarea.setHoraFinal(LocalTime.now());
        }

        String sql = "UPDATE tareas SET nombre=?, descripcion=?, prioridad=?, estado=?, hora_inicio=?, hora_final=? WHERE id=?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, tarea.getNombre());
            stmt.setString(2, tarea.getDescripcion());
            stmt.setString(3, tarea.getPrioridad());
            stmt.setString(4, tarea.getEstado());

            if (tarea.getHoraInicio() != null)
                stmt.setTime(5, Time.valueOf(tarea.getHoraInicio()));
            else
                stmt.setNull(5, Types.TIME);

            if (tarea.getHoraFinal() != null)
                stmt.setTime(6, Time.valueOf(tarea.getHoraFinal()));
            else
                stmt.setNull(6, Types.TIME);

            stmt.setInt(7, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void eliminarTarea(int id) {
        String sql = "DELETE FROM tareas WHERE id=?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<TareaModel> obtenerTareas() {
        List<TareaModel> lista = new ArrayList<>();
        String sql = "SELECT * FROM tareas";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                TareaModel tarea = new TareaModel(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("descripcion"),
                        rs.getString("prioridad"),
                        rs.getString("estado"),
                        rs.getDate("fecha_creacion").toLocalDate(),
                        rs.getTime("hora_inicio") != null ? rs.getTime("hora_inicio").toLocalTime() : null,
                        rs.getTime("hora_final") != null ? rs.getTime("hora_final").toLocalTime() : null
                );
                lista.add(tarea);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }
}
