package alejandria;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Time;
import java.util.TreeMap;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicArrowButton;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
public class PDFreader extends JFrame implements ActionListener {

    private final DB database = new DB();
    private int currentPage;
    private int numberOfPages;
    private long time;
    private JButton light = new JButton();
    private boolean nocturno = true;

    private final BasicArrowButton westArrow, eastArrow;
    private final JLabel LabelImage, lblStatus;
    private final JScrollPane scrollPane;
    private final JTextField pagina;
    private final JPanel info, preferences, turnOff;
    private final PDFRenderer render;
    private final JSlider slide;

    private final JProgressBar progress = new JProgressBar();

    /**
     * Constructor por defecto que inicia el lector PDF
     *
     * @param doc
     * @param id
     * @throws IOException
     */
    public PDFreader(PDDocument doc, int id) throws IOException {
        render = new PDFRenderer(doc);
        numberOfPages = doc.getNumberOfPages();
        this.setTitle(database.getTituloById(id));
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        getContentPane().setLayout(new BorderLayout());
        //obtengo el tiempo actual
        time = System.currentTimeMillis();
        //obtener la última página por la que se quedó el usuario y su scroll
        TreeMap<Integer, Integer> tree = database.getReadPage(id);
        currentPage = tree.firstKey();
        try {
            //cambiar el icono
            InputStream imgStream = PDFreader.class.getResourceAsStream("owl.png");
            setIconImage(ImageIO.read(imgStream));
            //cambiar turn off the lights
            imgStream = PDFreader.class.getResourceAsStream("light.png");
            light = new JButton(new ImageIcon(ImageIO.read(imgStream)));
        } catch (IOException ex) {
            System.out.println("Image not found" + ex.getMessage());
        }

        ImageIcon PageImage = new ImageIcon();
        LabelImage = new JLabel(PageImage);

        scrollPane = new JScrollPane(LabelImage);
        scrollPane.getVerticalScrollBar().setUnitIncrement(20);
        //cambiamos la posición del scroll
        scrollPane.getVerticalScrollBar().setValue(tree.get(currentPage));

        progress.setPreferredSize(new Dimension(120, 25));
        progress.setStringPainted(true);
        progress.setValue(100);

        lblStatus = new JLabel(" de " + numberOfPages, SwingConstants.CENTER);
        lblStatus.setHorizontalAlignment(JLabel.CENTER);
        lblStatus.setVerticalAlignment(JLabel.CENTER);

        pagina = new JTextField((currentPage + 1) + "", 4);

        westArrow = new BasicArrowButton(BasicArrowButton.WEST);
        eastArrow = new BasicArrowButton(BasicArrowButton.EAST);
        
        slide = new JSlider(-50, 50, 0);

        //panel para el cambio de páginas
        info = new JPanel(new FlowLayout());
        info.setBackground(new java.awt.Color(166, 180, 193));
        info.add(westArrow);
        info.add(pagina);
        info.add(lblStatus);
        info.add(eastArrow);
        
        //panel turn off the lights
        turnOff = new JPanel(new FlowLayout());
        turnOff.setBackground(new java.awt.Color(166, 180, 193));
        turnOff.setPreferredSize(new Dimension(200, 35));
        light.setPreferredSize(new Dimension(30, 30));
        turnOff.add(slide, BorderLayout.EAST);
        
        //panel para el tamaño y la barra de progreso
        preferences = new JPanel(new FlowLayout());
        preferences.setBackground(new java.awt.Color(166, 180, 193));  
        preferences.setPreferredSize(new Dimension(200, 35));
        preferences.add(light, BorderLayout.EAST);
        preferences.add(progress, BorderLayout.WEST);
        
        //footer
        JPanel statusBar = new JPanel(new BorderLayout(10, 0));
        statusBar.setBackground(new java.awt.Color(166, 180, 193));
        statusBar.setBorder(BorderFactory.createEmptyBorder(3, 5, 3, 5));
        statusBar.add(info, BorderLayout.CENTER);
        statusBar.add(preferences, BorderLayout.WEST);
        statusBar.add(turnOff, BorderLayout.EAST);

        //activamos o desactivamos las flechas
        if (currentPage == 0) {
            westArrow.setEnabled(false);
        }
        if (numberOfPages == currentPage) {
            eastArrow.setEnabled(false);
        }

        westArrow.addActionListener(this);
        eastArrow.addActionListener(this);

        getContentPane().add(scrollPane, BorderLayout.CENTER);
        getContentPane().add(statusBar, BorderLayout.SOUTH);

        pack();
        setVisible(true);
        openPage();

        //cambiar el focus para que funcione el keyListener
        setFocusable(true);
        requestFocusInWindow();

        addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getValue() + 20);
                } else if (e.getKeyCode() == KeyEvent.VK_UP) {
                    scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getValue() - 20);
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_PAGE_UP) {
                    eastArrow.doClick();
                } else if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_PAGE_DOWN) {
                    westArrow.doClick();
                }
            }
        });

        pagina.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
                if (!Character.isDigit(e.getKeyChar())) {
                    e.consume();
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    //obtengo el valor introducido por el usuario
                    int buffer = Integer.parseInt(pagina.getText());
                    //hay que restarle 1 porque para el usuario el libro empieza por la página 1, pero para el programador por la 0
                    buffer--;
                    if (buffer < numberOfPages) {
                        currentPage = buffer;
                        openPage();
                    } else {
                        pagina.setText(currentPage + "");
                        requestFocusInWindow();
                    }
                }
            }
        });

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent evt) {
                try {
                    doc.close();
                    //el 1000 es para transformar los miliseconds en seconds
                    time = (System.currentTimeMillis() - time) / 1000;
                    database.addRead(currentPage, Time.valueOf(Utils.convertSecondsToHMmSs(time)), scrollPane.getVerticalScrollBar().getValue(), id);
                    //comprobamos si hemos terminado el libro
                    if (currentPage == numberOfPages) {
                        database.marcarComoLeido(id);
                    }
                } catch (IOException ex) {
                }
            }
        });

        light.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                nocturno = !nocturno;
                openPage();
            }
        });
        
        slide.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider)e.getSource();
                 if (!source.getValueIsAdjusting()) {
                     openPage();
                 }
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent Ev) {
        // TODO Auto-generated method stub
        if (Ev.getSource() == eastArrow) {
            currentPage++;
        }
        if (Ev.getSource() == westArrow) {
            currentPage--;
        }
        openPage();
    }

    /**
     * Abre una página del pdf
     */
    private void openPage() {
        if (currentPage == 0) {
            westArrow.setEnabled(false);
        } else {
            westArrow.setEnabled(true);
        }

        if (currentPage == numberOfPages - 1) {
            eastArrow.setEnabled(false);
        } else {
            eastArrow.setEnabled(true);
        }

        //cambiar de página y actualizar la barra de progreso
        new MyWorker().execute();

        pagina.setText((currentPage + 1) + "");
        //cambiamos el focus, para que la caja de texto lo pierda
        requestFocusInWindow();
    }

    private class MyWorker extends SwingWorker<String, Void> {

        protected String doInBackground() {
            progress.setIndeterminate(true);
            progress.setValue(0);
            progress.setString("Cargando...");

            //cargo la página
            try {
                ImageIcon PageImage;
                //mostrar la imagen original o invertida
                if (nocturno) {
                    PageImage = new ImageIcon(invertImage(render.renderImageWithDPI(currentPage, 140+slide.getValue())));
                } else {
                    PageImage = new ImageIcon(render.renderImageWithDPI(currentPage, 140+slide.getValue()));
                }
                LabelImage.setIcon(PageImage);
            } catch (IOException | NullPointerException e) {
                System.out.println("Error renderizando página " + e);
            }

            return "Done.";
        }

        protected void done() {
            progress.setIndeterminate(false);
            progress.setValue(100);
            progress.setString("100%");
            scrollPane.getVerticalScrollBar().setValue(0);
        }
    }

    /**
     * Invierte los colores de las imágenes
     *
     * @param img
     * @return
     */
    private BufferedImage invertImage(BufferedImage img) {
        for (int x = 0; x < img.getWidth(); x++) {
            for (int y = 0; y < img.getHeight(); y++) {
                int rgba = img.getRGB(x, y);
                Color col = new Color(rgba, true);
                col = new Color(255 - col.getRed(),
                        255 - col.getGreen(),
                        255 - col.getBlue());
                img.setRGB(x, y, col.getRGB());
            }
        }

        return img;
    }
}
