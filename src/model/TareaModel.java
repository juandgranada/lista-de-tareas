package model;
import java.time.LocalDate;
import java.time.LocalTime;

public class TareaModel {
    private int id;
    private String nombre;
    private String descripcion;
    private String prioridad;
    private String estado;

    private LocalDate fechaCreacion;
    private LocalTime horaInicio;
    private LocalTime horaFinal;

    public TareaModel(int id, String nombre, String descripcion, String prioridad, String estado,
                      LocalDate fechaCreacion, LocalTime horaInicio, LocalTime horaFinal) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.prioridad = prioridad;
        this.estado = estado;
        this.fechaCreacion = fechaCreacion;
        this.horaInicio = horaInicio;
        this.horaFinal = horaFinal;
    }

    public TareaModel(String nombre, String descripcion, String prioridad, String estado) {
        this(0, nombre, descripcion, prioridad, estado, LocalDate.now(), null, null);
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(String prioridad) {
        this.prioridad = prioridad;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDate fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public LocalTime getHoraFinal() {
        return horaFinal;
    }

    public void setHoraFinal(LocalTime horaFinal) {
        this.horaFinal = horaFinal;
    }
}
