package alejandria;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;

/**
 *
 * @author Fran1488
 */
public class DialogEstadisticasGloables extends JDialog {

    public DialogEstadisticasGloables(JFrame parent) {
        //bloquear la ventana padre
        super(parent, ModalityType.APPLICATION_MODAL);
        setTitle("Estadísticas globales");
        //al cerrar, elimino la ventana
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        //obtener los datos 
        DB database = new DB();

        //llenar el JLabel
        JLabel datos = new JLabel("<html> <body style='background-color: #6088A2; color: #13233D; font-size: 1.2em; margin: 10px;'>"
                + " <p>Autor favorito  : " + database.getAutorFavorito() + "</p>"
                + " <p>Libros añadidos : " + database.getTotalBooks() + "</p>"
                + " <p>Libro favorito  : " + database.getFavoriteBook() + "</p>"
                + " <p>Libros sin leer : " + database.getUnreadBooks() + "</p>"
                + " <p>Páginas leídas  : " + database.getTotalPages() + "</p>"
                + " <p>Tiempo total    : " + database.getTotalTime() + "</p>"
                + " </body> </html>");

        //crear botón aceptar
        JButton aceptar = new JButton("Aceptar");
        aceptar.setHorizontalAlignment(SwingConstants.CENTER);

        aceptar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        //crear el Jpanel
        JPanel panel = new JPanel(new BorderLayout());
        //cambiar el fondo
        panel.setBackground(new java.awt.Color(96, 136, 162));
        //añadir los componentes
        panel.add(datos, BorderLayout.NORTH);
        panel.add(aceptar);
        //cambiar el contenido
        panel.setOpaque(true);
        setContentPane(panel);

        //mostrar la ventana
        pack();
        //centrar la ventana
        setLocationByPlatform(true);
        setLocationRelativeTo(parent);
        setVisible(true);
    }

    /**
     * Este método me sirve para cerrar la ventana al pulsar ENTER
     *
     * @return
     */
    @Override
    protected JRootPane createRootPane() {
        JRootPane rootPane = new JRootPane();
        KeyStroke stroke = KeyStroke.getKeyStroke("ENTER");
        Action actionListener = new AbstractAction() {
            public void actionPerformed(ActionEvent actionEvent) {
                dispose();
            }
        };
        InputMap inputMap = rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        inputMap.put(stroke, "ENTER");
        rootPane.getActionMap().put("ENTER", actionListener);

        return rootPane;
    }
}
