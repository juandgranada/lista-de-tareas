package view;

import dao.TareaController;
import model.TareaModel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class TareaView extends JFrame {
    private JTextField txtNombre, txtDescripcion;
    private JComboBox<String> comboPrioridad, comboEstado;
    private JTextField txtFechaCreacion, txtHoraInicio, txtHoraFinal;
    private JTable tabla;
    private DefaultTableModel tablaModelo;
    private TareaController controller;
    private int tareaSeleccionada = -1;

    public TareaView() {
        try {
            controller = new TareaController();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al conectar con la base de datos: " + e.getMessage());
            System.exit(1);
        }

        setTitle("Gestión de Tareas");
        setSize(1000, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Panel de formulario
        JPanel formulario = new JPanel(new GridLayout(9, 2));
        formulario.setBorder(BorderFactory.createTitledBorder("Datos de la tarea"));

        formulario.add(new JLabel("Nombre:"));
        txtNombre = new JTextField();
        formulario.add(txtNombre);

        formulario.add(new JLabel("Descripción:"));
        txtDescripcion = new JTextField();
        formulario.add(txtDescripcion);

        formulario.add(new JLabel("Prioridad:"));
        comboPrioridad = new JComboBox<>(new String[]{"Baja", "Media", "Alta"});
        formulario.add(comboPrioridad);

        formulario.add(new JLabel("Estado:"));
        comboEstado = new JComboBox<>(new String[]{"Pendiente", "En proceso", "Terminada"});
        formulario.add(comboEstado);

        formulario.add(new JLabel("Fecha creación:"));
        txtFechaCreacion = new JTextField();
        txtFechaCreacion.setEditable(false);
        formulario.add(txtFechaCreacion);

        formulario.add(new JLabel("Hora inicio:"));
        txtHoraInicio = new JTextField();
        txtHoraInicio.setEditable(false);
        formulario.add(txtHoraInicio);

        formulario.add(new JLabel("Hora final:"));
        txtHoraFinal = new JTextField();
        txtHoraFinal.setEditable(false);
        formulario.add(txtHoraFinal);

        // Botones
        JButton btnAgregar = new JButton("Agregar");
        JButton btnEditar = new JButton("Editar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnLimpiar = new JButton("Limpiar");

        formulario.add(btnAgregar);
        formulario.add(btnEditar);
        formulario.add(btnEliminar);
        formulario.add(btnLimpiar);

        // Tabla
        tablaModelo = new DefaultTableModel(
                new String[]{"ID", "Nombre", "Descripción", "Prioridad", "Estado", "Fecha creación", "Hora inicio", "Hora final"},
                0
        );
        tabla = new JTable(tablaModelo);
        JScrollPane scrollPane = new JScrollPane(tabla);

        // Layout general
        setLayout(new BorderLayout());
        add(formulario, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Listeners
        btnAgregar.addActionListener(e -> agregarTarea());
        btnEditar.addActionListener(e -> editarTarea());
        btnEliminar.addActionListener(e -> eliminarTarea());
        btnLimpiar.addActionListener(e -> limpiarCampos());

        tabla.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int fila = tabla.getSelectedRow();
                if (fila >= 0) {
                    tareaSeleccionada = (int) tabla.getValueAt(fila, 0);
                    txtNombre.setText(tabla.getValueAt(fila, 1).toString());
                    txtDescripcion.setText(tabla.getValueAt(fila, 2).toString());
                    comboPrioridad.setSelectedItem(tabla.getValueAt(fila, 3).toString());
                    comboEstado.setSelectedItem(tabla.getValueAt(fila, 4).toString());

                    txtFechaCreacion.setText(tabla.getValueAt(fila, 5) != null ? tabla.getValueAt(fila, 5).toString() : "");
                    txtHoraInicio.setText(tabla.getValueAt(fila, 6) != null ? tabla.getValueAt(fila, 6).toString() : "");
                    txtHoraFinal.setText(tabla.getValueAt(fila, 7) != null ? tabla.getValueAt(fila, 7).toString() : "");
                }
            }
        });

        cargarTabla();
        setVisible(true);
    }

    private void agregarTarea() {
        String nombre = txtNombre.getText();
        String descripcion = txtDescripcion.getText();
        String prioridad = comboPrioridad.getSelectedItem().toString();
        String estado = comboEstado.getSelectedItem().toString();

        if (nombre.isEmpty() || descripcion.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nombre y descripción son obligatorios.");
            return;
        }

        TareaModel tarea = new TareaModel(
                0,
                nombre,
                descripcion,
                prioridad,
                estado,
                LocalDate.now(), // fechaCreacion
                null,
                null
        );

        controller.agregarTarea(tarea);
        cargarTabla();
        limpiarCampos();
    }

    private void editarTarea() {
        if (tareaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona una tarea para editar.");
            return;
        }

        String nombre = txtNombre.getText();
        String descripcion = txtDescripcion.getText();
        String prioridad = comboPrioridad.getSelectedItem().toString();
        String estado = comboEstado.getSelectedItem().toString();

        TareaModel tarea = new TareaModel(
                tareaSeleccionada,
                nombre,
                descripcion,
                prioridad,
                estado,
                LocalDate.parse(txtFechaCreacion.getText()),
                txtHoraInicio.getText().isEmpty() ? null : java.time.LocalTime.parse(txtHoraInicio.getText()),
                txtHoraFinal.getText().isEmpty() ? null : java.time.LocalTime.parse(txtHoraFinal.getText())
        );

        controller.actualizarTarea(tareaSeleccionada, tarea);
        cargarTabla();
        limpiarCampos();
    }

    private void eliminarTarea() {
        if (tareaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona una tarea para eliminar.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "¿Estás seguro de eliminar esta tarea?");
        if (confirm == JOptionPane.YES_OPTION) {
            controller.eliminarTarea(tareaSeleccionada);
            cargarTabla();
            limpiarCampos();
        }
    }

    private void limpiarCampos() {
        tareaSeleccionada = -1;
        txtNombre.setText("");
        txtDescripcion.setText("");
        comboPrioridad.setSelectedIndex(0);
        comboEstado.setSelectedIndex(0);
        txtFechaCreacion.setText("");
        txtHoraInicio.setText("");
        txtHoraFinal.setText("");
    }

    private void cargarTabla() {
        try {
            tablaModelo.setRowCount(0);
            List<TareaModel> lista = controller.obtenerTareas();
            for (TareaModel t : lista) {
                tablaModelo.addRow(new Object[]{
                        t.getId(),
                        t.getNombre(),
                        t.getDescripcion(),
                        t.getPrioridad(),
                        t.getEstado(),
                        t.getFechaCreacion() != null ? t.getFechaCreacion().toString() : "",
                        t.getHoraInicio() != null ? t.getHoraInicio().toString() : "",
                        t.getHoraFinal() != null ? t.getHoraFinal().toString() : ""
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar datos: " + e.getMessage());
        }
    }
}
