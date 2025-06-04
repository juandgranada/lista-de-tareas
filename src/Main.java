import view.TareaView;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Establecer el look and feel del sistema (opcional, pero se ve más nativo)
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        // Ejecutar la interfaz Swing
        SwingUtilities.invokeLater(() -> new TareaView().setVisible(true));
    }
}
