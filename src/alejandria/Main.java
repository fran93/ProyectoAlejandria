package alejandria;

import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;
import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.TransferHandler;
import javax.swing.filechooser.FileNameExtensionFilter;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.Metadata;
import nl.siegmann.epublib.epub.EpubReader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;

/**
 *
 * @author Fran1488
 */
public class Main extends javax.swing.JFrame {

    private final DB database = new DB();
    private JPopupMenu popup;
    KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        

    /**
     * Creates new form Main
     */
    public Main() {
        initComponents();
        setExtendedState(MAXIMIZED_BOTH);
        manager.addKeyEventDispatcher(new MyDispatcher());
        try {
            //cambiar el icono
            InputStream imgStream = PDFreader.class.getResourceAsStream("owl.png");
            BufferedImage myImg;
            myImg = ImageIO.read(imgStream);
            setIconImage(myImg);
        } catch (IOException ex) {
            System.out.println("Image not found" + ex.getMessage());
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        explorador = new javax.swing.JFileChooser();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        listaLibros = new javax.swing.JList();
        progressBar = new javax.swing.JProgressBar();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        addBook = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        editBook = new javax.swing.JMenuItem();
        deleteBook = new javax.swing.JMenuItem();
        readBook = new javax.swing.JMenuItem();
        generals = new javax.swing.JMenu();
        globals = new javax.swing.JMenuItem();
        estadisticaLibro = new javax.swing.JMenuItem();
        filter = new javax.swing.JMenu();
        filters_author = new javax.swing.JMenuItem();
        filters_editor = new javax.swing.JMenuItem();
        filters_genero = new javax.swing.JMenuItem();
        unfilter = new javax.swing.JMenuItem();
        unread = new javax.swing.JCheckBoxMenuItem();
        jMenu3 = new javax.swing.JMenu();
        search_by_title = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Proyecto Alejandría");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jPanel2.setBackground(new java.awt.Color(96, 136, 162));

        listaLibros.setBackground(new java.awt.Color(166, 180, 193));
        listaLibros.setForeground(new java.awt.Color(19, 35, 61));
        listaLibros.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                listaLibrosMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(listaLibros);

        progressBar.setOrientation(1);
        progressBar.setToolTipText("");
        progressBar.setValue(100);
        progressBar.setName(""); // NOI18N
        progressBar.setString("");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 544, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 345, Short.MAX_VALUE)
                    .addComponent(progressBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jMenuBar1.setBackground(new java.awt.Color(96, 136, 162));

        jMenu1.setBackground(new java.awt.Color(166, 180, 193));
        jMenu1.setText("Libros");

        addBook.setBackground(new java.awt.Color(166, 180, 193));
        addBook.setText("Añadir libro");
        addBook.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addBookActionPerformed(evt);
            }
        });
        jMenu1.add(addBook);
        jMenu1.add(jSeparator1);

        editBook.setBackground(new java.awt.Color(166, 180, 193));
        editBook.setText("Editar libro");
        editBook.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editBookActionPerformed(evt);
            }
        });
        jMenu1.add(editBook);

        deleteBook.setBackground(new java.awt.Color(166, 180, 193));
        deleteBook.setText("Eliminar libro");
        deleteBook.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteBookActionPerformed(evt);
            }
        });
        jMenu1.add(deleteBook);

        readBook.setBackground(new java.awt.Color(166, 180, 193));
        readBook.setText("Leer libro");
        readBook.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                readBookActionPerformed(evt);
            }
        });
        jMenu1.add(readBook);

        jMenuBar1.add(jMenu1);

        generals.setText("Estadísticas");

        globals.setText("Globales");
        globals.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                globalsActionPerformed(evt);
            }
        });
        generals.add(globals);

        estadisticaLibro.setText("Libro");
        estadisticaLibro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                estadisticaLibroActionPerformed(evt);
            }
        });
        generals.add(estadisticaLibro);

        jMenuBar1.add(generals);

        filter.setText("Filtrar");

        filters_author.setText("Autor");
        filters_author.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                filters_authorActionPerformed(evt);
            }
        });
        filter.add(filters_author);

        filters_editor.setText("Editor");
        filters_editor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                filters_editorActionPerformed(evt);
            }
        });
        filter.add(filters_editor);

        filters_genero.setText("Género");
        filters_genero.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                filters_generoActionPerformed(evt);
            }
        });
        filter.add(filters_genero);

        unfilter.setText("Sin filtrar");
        unfilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                unfilterActionPerformed(evt);
            }
        });
        filter.add(unfilter);

        unread.setSelected(true);
        unread.setText("Sin leer");
        unread.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                unreadActionPerformed(evt);
            }
        });
        filter.add(unread);

        jMenuBar1.add(filter);

        jMenu3.setText("Buscar");

        search_by_title.setText("Búsqueda por título");
        search_by_title.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                search_by_titleActionPerformed(evt);
            }
        });
        jMenu3.add(search_by_title);

        jMenuBar1.add(jMenu3);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void addBookActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addBookActionPerformed
        FileNameExtensionFilter filtro = new FileNameExtensionFilter("Libros, epub y pdf", "epub", "pdf");
        explorador.removeChoosableFileFilter(explorador.getAcceptAllFileFilter());
        explorador.setFileFilter(filtro);
        explorador.setDialogTitle("Añadir libro");

        if (explorador.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            new MyWorkerAddBook(explorador.getSelectedFile()).execute();
        }
    }//GEN-LAST:event_addBookActionPerformed

    private void editBookActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editBookActionPerformed
        Libro sbook = (Libro) listaLibros.getSelectedValue();
        try {
            new DialogEditar(this, sbook.getId(), sbook.getTitulo());
        } catch (NullPointerException ex) {
            JOptionPane.showMessageDialog(this, "Debes seleccionar un libro.");
        }
    }//GEN-LAST:event_editBookActionPerformed

    private void deleteBookActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteBookActionPerformed
        int option = JOptionPane.showConfirmDialog(this, "¿Está seguro de que desea eliminar el libro seleccionado?");
        if (option == JOptionPane.OK_OPTION) {
            try {
                Libro sbook = (Libro) listaLibros.getSelectedValue();
                database.eliminarLibro(sbook.getId());
                Utils.eliminarLibro(sbook.getId());
                inicializarLista();
            } catch (NullPointerException ex) {
                JOptionPane.showMessageDialog(this, "Debes seleccionar un libro.");
            }
        }
    }//GEN-LAST:event_deleteBookActionPerformed

    private void readBookActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_readBookActionPerformed
        try {
            Libro sbook = (Libro) listaLibros.getSelectedValue();
            leerLibro(sbook.getId());
        } catch (NullPointerException ex) {
            JOptionPane.showMessageDialog(this, "Debes seleccionar un libro.");
        }
    }//GEN-LAST:event_readBookActionPerformed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        database.createDB();
        inicializarLista();
        inicializarPopup();
        System.setProperty("file.encoding", "UTF-8");
    }//GEN-LAST:event_formWindowOpened

    private void listaLibrosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_listaLibrosMouseClicked
        if (evt.getClickCount() == 2) {
            Libro sbook = (Libro) listaLibros.getSelectedValue();
            leerLibro(sbook.getId());
        }
    }//GEN-LAST:event_listaLibrosMouseClicked

    private void globalsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_globalsActionPerformed
        new DialogEstadisticasGloables(this);
    }//GEN-LAST:event_globalsActionPerformed

    private void estadisticaLibroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_estadisticaLibroActionPerformed
        Libro sbook = (Libro) listaLibros.getSelectedValue();
        try {
            new DialogEstadisticasLibro(this, sbook.getId(), sbook.getTitulo());
        } catch (NullPointerException ex) {
            JOptionPane.showMessageDialog(this, "Debes seleccionar un libro.");
        }
    }//GEN-LAST:event_estadisticaLibroActionPerformed

    private void filters_authorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_filters_authorActionPerformed
        JList list = new JList(database.getAuthors());
        JOptionPane.showMessageDialog(null, list, "Seleccione un autor", JOptionPane.PLAIN_MESSAGE);
        inicializarLista((String) list.getSelectedValue(), 0);
    }//GEN-LAST:event_filters_authorActionPerformed

    private void filters_editorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_filters_editorActionPerformed
        JList list = new JList(database.getEditors());
        JOptionPane.showMessageDialog(null, list, "Seleccione un editor", JOptionPane.PLAIN_MESSAGE);
        inicializarLista((String) list.getSelectedValue(), 1);
    }//GEN-LAST:event_filters_editorActionPerformed

    private void filters_generoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_filters_generoActionPerformed
        JList list = new JList(database.getGeneros());
        JOptionPane.showMessageDialog(null, list, "Seleccione un género", JOptionPane.PLAIN_MESSAGE);
        inicializarLista((String) list.getSelectedValue(), 2);
    }//GEN-LAST:event_filters_generoActionPerformed

    private void search_by_titleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_search_by_titleActionPerformed
        String response = javax.swing.JOptionPane.showInputDialog("Introduzca el título del libro que desea buscar");
        if (!"".equals(response) && null != response) {
            inicializarLista(response, 3);
        }
    }//GEN-LAST:event_search_by_titleActionPerformed

    private void unfilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_unfilterActionPerformed
        inicializarLista();
    }//GEN-LAST:event_unfilterActionPerformed

    private void unreadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_unreadActionPerformed
        inicializarLista(!unread.isSelected() + "", 4);
    }//GEN-LAST:event_unreadActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Main().setVisible(true);
            }
        });
    }

    /**
     * Método que lee un libro pasandole un id
     *
     * @param id
     */
    private void leerLibro(int id) {
        String fichero = database.getFichero(id);
        if (fichero.endsWith(".pdf")) {
            try {
                File f = new File("libros/" + id + "/" + fichero);
                PDDocument doc = PDDocument.load(f);
                //compruebo si el documento está cifrado
                if (doc.isEncrypted()) {
                    doc.setAllSecurityToBeRemoved(true);
                }
                new PDFreader(doc, id);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "No ha sido posible leer el pdf " + ex);
            }
        } else {
            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    ReadHTML browser = new ReadHTML("libros/" + id + "/" + fichero, id);
                    browser.setVisible(true);
                }
            });
        }
    }

    /**
     * Método que refresca la lista con todos los libros
     */
    private void inicializarLista() {
        listaLibros.setDragEnabled(true);
        TransferHandler handler = new TransferHandler() {
            @Override
            public boolean canImport(TransferHandler.TransferSupport info) {
                // Solo se importan listas de archivos
                if (!info.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                    return false;
                }
                return true;
            }

            @Override
            public boolean importData(TransferHandler.TransferSupport info) {
                if (!info.isDrop()) {
                    return false;
                }
                // Check for FileList flavor
                if (!info.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                    return false;
                }
                // Get the fileList that is being dropped.
                Transferable t = info.getTransferable();
                List<File> data;
                try {
                    data = (List<File>) t.getTransferData(DataFlavor.javaFileListFlavor);
                } catch (UnsupportedFlavorException | IOException ex) {
                    return false;
                }
                for (File file : data) {
                    new MyWorkerAddBook(file).execute();
                }
                return true;
            }
        };
        listaLibros.setTransferHandler(handler);
        //creamos un nuevo modelo de datos
        DefaultListModel<Libro> listModel = new DefaultListModel<>();
        //rellenar el libro usando los datos de la base de datos
        TreeMap<Integer, String> tree = database.getListaLibros();
        for (Integer key : tree.keySet()) {
            listModel.addElement(new Libro(tree.get(key), key));
        }

        listaLibros.setModel(listModel);
        listaLibros.setCellRenderer(new LibroRenderer());
        listaLibros.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        listaLibros.setVisibleRowCount(2);
        listaLibros.setFixedCellHeight(300);
        listaLibros.setFixedCellWidth(250);
    }

    /**
     * Método que refresca el Jlist 0 Autor 1 Editor 2 Género 3 Título 4 Leídos
     */
    private void inicializarLista(String filtro, int tipo) {
        //creamos un nuevo modelo de datos
        DefaultListModel<Libro> listModel = new DefaultListModel<>();
        //rellenar el libro usando los datos de la base de datos
        TreeMap<Integer, String> tree = database.getListaLibros(filtro, tipo);
        if (tree.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No se encontraron libros con esos criterios de búsqueda");
        } else {
            for (Integer key : tree.keySet()) {
                listModel.addElement(new Libro(tree.get(key), key));
            }
            //listModel.addElement(new Libro("La lucha de Plamen", 1));
            listaLibros.setModel(listModel);
            listaLibros.setCellRenderer(new LibroRenderer());
            listaLibros.setLayoutOrientation(JList.HORIZONTAL_WRAP);
            listaLibros.setVisibleRowCount(2);
            listaLibros.setFixedCellHeight(300);
            listaLibros.setFixedCellWidth(250);
        }
    }

    /**
     * Método que inicializa el menú del botón derecho del ratón
     */
    private void inicializarPopup() {
        //Create the popup menu.
        popup = new JPopupMenu();
        JMenuItem menuItem = new JMenuItem("Editar libro");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Libro sbook = (Libro) listaLibros.getSelectedValue();
                new DialogEditar(Main.this, sbook.getId(), sbook.getTitulo());
            }
        });
        popup.add(menuItem);

        menuItem = new JMenuItem("Eliminar libro");
        menuItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                int option = JOptionPane.showConfirmDialog(Main.this, "¿Está seguro de que desea eliminar el libro seleccionado?");
                if (option == JOptionPane.OK_OPTION) {
                    Libro sbook = (Libro) listaLibros.getSelectedValue();
                    database.eliminarLibro(sbook.getId());
                    Utils.eliminarLibro(sbook.getId());
                    inicializarLista();
                }
            }
        });
        popup.add(menuItem);

        menuItem = new JMenuItem("Estadísticas");
        menuItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                Libro sbook = (Libro) listaLibros.getSelectedValue();
                new DialogEstadisticasLibro(Main.this, sbook.getId(), sbook.getTitulo());
            }
        });
        popup.add(menuItem);

        menuItem = new JMenuItem("Leer");
        menuItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                Libro sbook = (Libro) listaLibros.getSelectedValue();
                leerLibro(sbook.getId());
            }
        });
        popup.add(menuItem);

        //Add listener to components that can bring up popup menus.
        MouseListener popupListener = new PopupListener();
        listaLibros.addMouseListener(popupListener);
    }

    private class MyDispatcher implements KeyEventDispatcher {
        @Override
        public boolean dispatchKeyEvent(KeyEvent e) {
            if (e.getID() == KeyEvent.KEY_PRESSED) {
                if(e.isControlDown() && e.getKeyCode()==KeyEvent.VK_F){
                    search_by_title.doClick();
                }
            } else if (e.getID() == KeyEvent.KEY_RELEASED) {
                
            } else if (e.getID() == KeyEvent.KEY_TYPED) {
                
            }
            return false;
        }
    }

    private class PopupListener extends MouseAdapter {

        @Override
        public void mousePressed(MouseEvent e) {
            //al hacer click derecho cambio el elemento seleccionado de la lista
            JList list = (JList) e.getSource();
            int row = list.locationToIndex(e.getPoint());
            list.setSelectedIndex(row);
            //muestro el menú
            maybeShowPopup(e);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            //al hacer click derecho cambio el elemento seleccionado de la lista
            JList list = (JList) e.getSource();
            int row = list.locationToIndex(e.getPoint());
            list.setSelectedIndex(row);
            //muestro el menú
            maybeShowPopup(e);
        }

        private void maybeShowPopup(MouseEvent e) {
            if (e.isPopupTrigger()) {
                popup.show(e.getComponent(),
                        e.getX(), e.getY());
            }
        }
    }

    /**
     * Esta clase sirve para que se muestre la carga al añadir un libro.
     */
    private class MyWorkerAddBook extends SwingWorker<String, Void> {

        File f;

        public MyWorkerAddBook(File f) {
            this.f = f;
        }

        protected String doInBackground() {
            progressBar.setIndeterminate(true);
            progressBar.setValue(0);
            addBook(f);
            return "Done.";
        }

        protected void done() {
            progressBar.setIndeterminate(false);
            progressBar.setValue(100);
        }
    }

    /**
     * Este método te añade un libro a la ase de datos
     *
     * @param f
     */
    private void addBook(File f) {
        //añadimos un pdf
        if (f.getName().endsWith(".pdf")) {
            try (PDDocument doc = PDDocument.load(f)) {
                //compruebo si el documento está cifrado
                if (doc.isEncrypted()) {
                    doc.setAllSecurityToBeRemoved(true);
                }
                //añadimos el libro a la base de datos, extrayendo los metadatos                   
                PDDocumentInformation info = doc.getDocumentInformation();
                //controlamos que la fecha sea correcta
                Calendar cal;
                try {
                    cal = info.getModificationDate();
                    cal.getTime();
                } catch (NullPointerException ex) {
                    cal = Calendar.getInstance();
                }
                database.addBook(
                        info.getAuthor(),
                        //Obtenemos el título a partir del nombre del fichero, y quitamos los _
                        f.getName().substring(0, f.getName().length() - 4).replace('_', ' '),
                        info.getSubject() == null ? "No hay datos" : info.getSubject(),
                        info.getProducer() == null ? "No hay datos" : info.getProducer(),
                        f.getName(),
                        "pdf",
                        new SimpleDateFormat("yyyy-mm-dd").parse(cal.get(Calendar.YEAR) + "-" + cal.get(Calendar.MONTH) + "-" + cal.get(Calendar.DAY_OF_MONTH))
                );
                //guardamos el libro en la aplicación
                Utils.saveBook(f, database.maxLibro(), "pdf");
                inicializarLista();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "No ha sido posible leer el pdf. " + ex);
            } catch (ParseException ex) {
                JOptionPane.showMessageDialog(this, "Error al convertir la fecha. " + ex);
            }
            //añadimos el libro a la base de datos, extrayendo los metadatos
        } else if (f.getName().endsWith(".epub")) {
            try {
                EpubReader epubReader = new EpubReader();
                Book book = epubReader.readEpub(new FileInputStream(f));
                Metadata metadata = book.getMetadata();
                Date time;
                //parseamos la fecha, algunos epubs contienen un string en la fecha como por ejemplo publicación:
                //con lo cual nuestro parseador(cambia el formato de la fecha) falla y para solucionar esto, obtengo 
                //solo los 10 últimos caracteres de la fecha, así me aseguro que el parseador recibe solo la fecha
                try {
                    time = metadata.getDates().isEmpty() ? new Date() : new SimpleDateFormat("yyyy-mm-dd").parse(metadata.getDates().get(0).toString().substring(metadata.getDates().get(0).toString().length() - 10));
                } catch (ParseException ex) {
                    time = new Date();
                }
                database.addBook(
                        metadata.getAuthors().isEmpty() ? "No hay datos" : metadata.getAuthors().get(0).getFirstname() + " " + metadata.getAuthors().get(0).getLastname(),
                        metadata.getFirstTitle(),
                        metadata.getSubjects().isEmpty() ? "No hay datos" : metadata.getSubjects().get(0),
                        metadata.getPublishers().isEmpty() ? "No hay datos" : metadata.getPublishers().get(0),
                        f.getName(),
                        "epub",
                        time
                );
                Utils.saveBook(f, database.maxLibro(), "epub");
                inicializarLista();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "No ha sido posible leer el epub. " + ex);
            }
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem addBook;
    private javax.swing.JMenuItem deleteBook;
    private javax.swing.JMenuItem editBook;
    private javax.swing.JMenuItem estadisticaLibro;
    private javax.swing.JFileChooser explorador;
    private javax.swing.JMenu filter;
    private javax.swing.JMenuItem filters_author;
    private javax.swing.JMenuItem filters_editor;
    private javax.swing.JMenuItem filters_genero;
    private javax.swing.JMenu generals;
    private javax.swing.JMenuItem globals;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JList listaLibros;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JMenuItem readBook;
    private javax.swing.JMenuItem search_by_title;
    private javax.swing.JMenuItem unfilter;
    private javax.swing.JCheckBoxMenuItem unread;
    // End of variables declaration//GEN-END:variables
}
