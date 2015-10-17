package alejandria;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Time;
import java.util.TreeMap;
import javafx.concurrent.Worker;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.ScrollEvent;
import javax.imageio.ImageIO;
import javax.swing.event.ChangeEvent;
import javax.swing.plaf.basic.BasicArrowButton;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ReadHTML extends JFrame {

    private final JFXPanel jfxPanel = new JFXPanel();
    private final DB database = new DB();
    private WebEngine engine;

    private final JPanel panel = new JPanel(new BorderLayout());
    private JLabel lblStatus;
    private JPanel info, preferences, turnOff;
    private JTextField pagina;
    private JButton light = new JButton();
    private JSlider slide;

    private final JProgressBar progressBar = new JProgressBar();
    private final BasicArrowButton westArrow = new BasicArrowButton(BasicArrowButton.WEST);
    private final BasicArrowButton eastArrow = new BasicArrowButton(BasicArrowButton.EAST);
    private final int id;

    private static ReadEpub read;    
    private int scroll = 0;
    private long time;
    private boolean nocturno = true;

    public ReadHTML(String libro, int id) {
        super();
        read = new ReadEpub(libro);
        this.id = id;
        initComponents();
        //obtener la última página por la que se quedó el usuario y su scroll
        TreeMap<Integer, Integer> tree = database.getReadPage(id);
        read.setPage(tree.firstKey());
        scroll = tree.get(tree.firstKey());
        loadPage(read.getPageContent());
    }

    private void initComponents() {
        createScene();
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

        //obtengo el tiempo actual
        time = System.currentTimeMillis();

        progressBar.setPreferredSize(new Dimension(120, 25));
        progressBar.setStringPainted(true);

        lblStatus = new JLabel(" de " + read.getTotalPage(), SwingConstants.CENTER);
        lblStatus.setHorizontalAlignment(JLabel.CENTER);
        lblStatus.setVerticalAlignment(JLabel.CENTER);

        pagina = new JTextField(((read.getPage() + 1)) + "", 4);
        
        slide = new JSlider(0, 9, 4);

        //panel para el cambio de páginas
        info = new JPanel();
        info.setBackground(new java.awt.Color(166, 180, 193));
        info.setLayout(new FlowLayout());
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
        preferences.add(progressBar, BorderLayout.WEST);

        //footer
        JPanel statusBar = new JPanel(new BorderLayout(10, 0));
        statusBar.setBackground(new java.awt.Color(166, 180, 193));
        statusBar.setBorder(BorderFactory.createEmptyBorder(3, 5, 3, 5));
        statusBar.add(info, BorderLayout.CENTER);
        statusBar.add(preferences, BorderLayout.WEST);
        statusBar.add(turnOff, BorderLayout.EAST);

        panel.add(jfxPanel, BorderLayout.CENTER);
        panel.add(statusBar, BorderLayout.SOUTH);

        getContentPane().add(panel);

        setPreferredSize(this.getToolkit().getScreenSize());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        pack();

        eastArrow.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (read.getTotalPage() != read.getPage()) {
                    read.setPage(read.getPage() + 1);
                    ReadHTML.this.loadPage(read.getPageContent());
                }
            }
        });

        westArrow.addActionListener((ActionEvent e) -> {
            if (0 != read.getPage()) {
                read.setPage(read.getPage() - 1);
                ReadHTML.this.loadPage(read.getPageContent());
            }
        });

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent evt) {
                //el 1000 es para transformar los miliseconds en seconds
                time = (System.currentTimeMillis() - time) / 1000;
                database.addRead(read.getPage(), Time.valueOf(Utils.convertSecondsToHMmSs(time)), scroll, id);
                if (read.readedBook()) {
                    database.marcarComoLeido(id);
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
                    if (buffer < read.getTotalPage()) {
                        read.setPage(buffer);
                        loadPage(read.getPageContent());
                    } else {
                        pagina.setText(read.getPage() + "");
                        requestFocusInWindow();
                    }
                }
            }
        });
        
        light.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                nocturno = !nocturno;
                loadPage(read.getPageContent());
            }
        });
        
        slide.addChangeListener(new javax.swing.event.ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider)e.getSource();
                 if (!source.getValueIsAdjusting()) {
                     loadPage(read.getPageContent());
                 }
            }
        });
    }

    private void createScene() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                WebView view = new WebView();
                engine = view.getEngine();

                engine.titleProperty().addListener(new ChangeListener<String>() {
                    @Override
                    public void changed(ObservableValue<? extends String> observable, String oldValue, final String newValue) {
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                ReadHTML.this.setTitle(newValue);
                            }
                        });
                    }
                });

                engine.getLoadWorker().workDoneProperty().addListener(new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, final Number newValue) {
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setValue(newValue.intValue());
                            }
                        });
                    }
                });

                engine.getLoadWorker().stateProperty().addListener((ObservableValue<? extends Worker.State> observable, Worker.State oldValue, Worker.State newValue) -> {
                    if (newValue == Worker.State.SUCCEEDED) {
                        //muevo el scroll
                        engine.executeScript("window.scrollTo(0, " + scroll + ");");
                        // Can safely access DOM and set styles.
                        Document doc = engine.getDocument();
                        NodeList bodyTags = doc.getElementsByTagName("body");
                        //obtenemos la etiqueta body y la modificamos
                        if (bodyTags != null && bodyTags.getLength() > 0) {
                            Node body = bodyTags.item(0);
                            Attr newAttr = doc.createAttribute("style");
                            if (nocturno) {
                                newAttr.setValue("background-color:black;color:#f0ffff;font-family: verdana;font-size:1."+slide.getValue()+"em;text-align:justify;margin:1em 3em 1em 3em;");
                            } else {
                                newAttr.setValue("font-family: verdana;font-size:1."+slide.getValue()+"em;text-align:justify;margin:1em 3em 1em 3em;");
                            }
                            body.getAttributes().setNamedItem(newAttr);
                        }
                        NodeList h3 = doc.getElementsByTagName("h3");
                        if (h3 != null && h3.getLength() > 0) {
                            Node body = h3.item(0);
                            Attr newAttr = doc.createAttribute("style");
                            newAttr.setValue("text-align:center;");
                            body.getAttributes().setNamedItem(newAttr);
                        }
                        NodeList h2 = doc.getElementsByTagName("h2");
                        if (h2 != null && h2.getLength() > 0) {
                            Node body = h2.item(0);
                            Attr newAttr = doc.createAttribute("style");
                            newAttr.setValue("text-align:center;");
                            body.getAttributes().setNamedItem(newAttr);
                        }
                        NodeList h1 = doc.getElementsByTagName("h1");
                        if (h1 != null && h1.getLength() > 0) {
                            Node body = h1.item(0);
                            Attr newAttr = doc.createAttribute("style");
                            newAttr.setValue("text-align:center;");
                            body.getAttributes().setNamedItem(newAttr);
                        }
                    }
                });

                /**
                 * Este escuchador pasa de página cuando el usuario pulsa las
                 * teclas de dirección
                 */
                view.setOnKeyReleased(new EventHandler<javafx.scene.input.KeyEvent>() {

                    @Override
                    public void handle(javafx.scene.input.KeyEvent event) {
                        if (event.getCode() == KeyCode.RIGHT || event.getCode() == KeyCode.PAGE_UP) {
                            eastArrow.doClick();
                        } else if (event.getCode() == KeyCode.LEFT || event.getCode() == KeyCode.PAGE_DOWN) {
                            westArrow.doClick();
                        }
                    }
                });

                /**
                 * Este escuchador guarda la posición del scroll mediante
                 * javascript
                 */
                view.setOnScroll(new EventHandler<ScrollEvent>() {
                    @Override
                    public void handle(ScrollEvent event) {
                        scroll = (int) engine.executeScript("var doc = document.documentElement;(window.pageYOffset || doc.scrollTop)  - (doc.clientTop || 0);");
                    }
                });

                jfxPanel.setScene(new Scene(view));
            }
        });
        //evita excepciones debido a la integración de componentes FX en Swing
        Platform.setImplicitExit(true);
    }

    private void loadPage(String content) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                //modificamos la url de las imágenes, esto es debido a que WEBVIEW solo puede acceder a recursos locales mediante el protocolo file:///
                String cc = content.replaceAll("../Images/", "file:///" + new File("").getAbsolutePath().replace('\\', '/') + "/libros/" + id + "/Images/");
                engine.loadContent(cc);
                pagina.setText(((read.getPage() + 1)) + "");

            }
        });
    }
}
