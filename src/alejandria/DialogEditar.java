package alejandria;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author Fran1488
 */
public class DialogEditar extends JDialog {

    public DialogEditar(JFrame parent, int id, String titulo) {
        //bloquear la ventana padre
        super(parent, ModalityType.APPLICATION_MODAL);
        setTitle("Editar información");
        //al cerrar, elimino la ventana
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setPreferredSize(new Dimension(300, 300));

        //obtener los datos 
        DB database = new DB();
        String[] detalles = database.getLibroDetails(id);
        String[] labels = {"Autor :", "Editor :", "Género :", "Fecha :"};
        JTextField[] inputs = new JTextField[5];

        //crear el Jpanel
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 2, 2, 2);
        //cambiar el fondo
        panel.setBackground(new java.awt.Color(166, 180, 193));

        //añadir el título
        inputs[4]= new JTextField(titulo, 10);
        inputs[4].setForeground(new java.awt.Color(19, 35, 61));
        JLabel l = new JLabel("Titulo :", JLabel.TRAILING);
        l.setForeground(new java.awt.Color(19, 35, 61));
        addComponents(l, inputs[4], panel, gbc);
        
        //añadimos los componentes al JPanel
        for (int i = 0; i < labels.length; i++) {
            l = new JLabel(labels[i], JLabel.TRAILING);
            l.setForeground(new java.awt.Color(19, 35, 61));
            //añadimos el texto
            inputs[i] = new JTextField(detalles[i], 10);
            //color del texto
            inputs[i].setForeground(new java.awt.Color(19, 35, 61));
            //añadimos los componentes
            addComponents(l, inputs[i], panel, gbc);
        }
        
        //añado un botón para cancelar y otro para guardar
        JButton save = new JButton("Guardar");
        save.setBackground(new java.awt.Color(96, 136, 162));
        save.setForeground(new java.awt.Color(19, 35, 61));
        JButton cancelar = new JButton("Cancelar");
        cancelar.setBackground(new java.awt.Color(96, 136, 162));
        cancelar.setForeground(new java.awt.Color(19, 35, 61));

        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridwidth = GridBagConstraints.RELATIVE;
        panel.add(save, gbc);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        panel.add(cancelar, gbc);
        
        //escuchadores para cancelar y guardar
        cancelar.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        
        save.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
               database.editLibro(id, new String[]{inputs[4].getText(), inputs[0].getText(), 
                   inputs[1].getText(), inputs[2].getText(), inputs[3].getText()});
                   dispose();
            }
        });
        
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

    private void addComponents(JLabel label, JTextField tf, JPanel p,
        GridBagConstraints gbc) {
        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridwidth = GridBagConstraints.RELATIVE;
        p.add(label, gbc);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        p.add(tf, gbc);
    }
}
