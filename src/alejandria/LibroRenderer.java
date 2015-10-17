/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alejandria;

import java.awt.Component;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 *
 * @author Fran1488
 */
/**
     * Clase que cambia la forma en la que se muestran las celdas del JLIST
     */
    public class LibroRenderer extends JLabel implements ListCellRenderer<Libro> {
        private DB database = new DB();
        
        public LibroRenderer() {
            setOpaque(true);
            setHorizontalAlignment(JLabel.CENTER);
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends Libro> list, Libro libro, int index, boolean isSelected, boolean cellHasFocus) {
            int id = libro.getId();
            ImageIcon imageIcon = new ImageIcon("libros/" + libro.getId() + "/cover.jpg");

            setIcon(imageIcon);
            setText(libro.getTitulo());
            setVerticalTextPosition(JLabel.BOTTOM);
            setHorizontalTextPosition(JLabel.CENTER);

            //obtengo los datos del libro y los pongo en el Tooltip
            String[] detalles = database.getLibroDetails(id);
            setToolTipText("<html> <body style='background-color: #6088A2; color: #13233D; font-size: 1.2em; margin: 10px;'>"
                    + " <p>Título : " + libro.getTitulo() + "</p>"
                    + " <p>Autor  : " + detalles[0] + "</p>"
                    + " <p>Editor : " + detalles[1] + "</p>"
                    + " <p>Género : " + detalles[2] + "</p>"
                    + " <p>Fecha  : " + detalles[3] + "</p>"
                    + " <p>Leído  : " + detalles[4] + "</p>"
                    + " </body> </html>");

            setForeground(new java.awt.Color(19, 35, 61));
            setBorder(BorderFactory.createLineBorder(new java.awt.Color(96, 136, 162)));

            if (isSelected) {
                setBackground(new java.awt.Color(96, 136, 162));
            } else {
                setBackground(new java.awt.Color(166, 180, 193));
            }

            return this;
        }
    }
