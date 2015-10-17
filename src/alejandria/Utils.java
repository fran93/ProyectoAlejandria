package alejandria;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.imageio.ImageIO;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.MediaType;
import nl.siegmann.epublib.domain.Resource;
import nl.siegmann.epublib.epub.EpubReader;
import nl.siegmann.epublib.service.MediatypeService;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.h2.store.fs.FileUtils;
import org.imgscalr.Scalr;

/**
 *
 * @author Fran1488
 */
public class Utils {

    /**
     * Método que copia el libro añadido por el usuario a el directorio libros
     * de la aplicación
     *
     * @param f
     * @param id
     * @param formato
     */
    public static void saveBook(File f, int id, String formato) {
        //compruebo si el formato es pdf o epub
        if (formato.equals("pdf")) {
            //creo el directorio
            new File("libros/" + id + "/").mkdirs();
            extractPdfCover(f, id);
            scaleImage("libros/" + id + "/cover.jpg");
        } else {
            //creo los directorios
            new File("libros/" + id + "/Images").mkdirs();
            extractEpubCover(f, "libros/" + id);
            scaleImage("libros/" + id + "/cover.jpg");
            extractAllImages(f, "libros/" + id + "/Images");
        }
        //copio el archivo 
        try (FileInputStream f1 = new FileInputStream(f);
                FileOutputStream f2 = new FileOutputStream("libros/" + id + "/" + f.getName())) {
            //el buffer simplemente sirve para optimizar el proceso de copiado
            byte[] buffer = new byte[1000];
            while (f1.read(buffer) > 0) {
                f2.write(buffer);
            }
        } catch (IOException ex) {
            System.out.println("Problemas al guardar el archivo " + ex);
        }

    }

    public static void eliminarLibro(int id) {
        FileUtils.deleteRecursive("libros/" + id + "/", true);
    }

    /**
     * Método que redimensiona una imagen pasada por parámetro y la convierte en
     * miniatura
     *
     * @param imagePath
     */
    private static void scaleImage(String imagePath) {
        try {
            BufferedImage thumbnail = ImageIO.read(new File(imagePath));
            thumbnail = Scalr.resize(thumbnail, Scalr.Method.SPEED, 150, 220, Scalr.OP_ANTIALIAS);
            ImageIO.write(thumbnail, "jpg", new File(imagePath));
        } catch (IOException ex) {
            System.out.println("Problemas al crear el thumbnail " + ex);
        }
    }

    /**
     * Método que extrae la portada del pdf y al convierte en imagen
     *
     * @param f
     * @param id
     */
    private static void extractPdfCover(File f, int id) {
        try (PDDocument doc = PDDocument.load(f)) {
            //compruebo si el documento está cifrado
            if (doc.isEncrypted()) {
                doc.setAllSecurityToBeRemoved(true);
            }

            PDFRenderer render = new PDFRenderer(doc);
             ImageIO.write(render.renderImage(0), "jpg", new File("libros/" + id + "/cover.jpg"));
        } catch (IOException ex) {
            System.out.println("Problemas al crear el thumbnail " + ex);
        }
    }

    /**
     * Método que obtiene la portada de un epub
     *
     * @param f
     * @param path
     */
    private static void extractEpubCover(File f, String path) {
        try {
            EpubReader epubReader = new EpubReader();
            Book book = epubReader.readEpub(new FileInputStream(f));
            //obtener la portada
            Resource coverImage = book.getCoverImage();
            //guardarla en un archivo
            try (FileOutputStream f1 = new FileOutputStream(path + "/" + "cover.jpg");
                    InputStream sv = coverImage.getInputStream()) {
                byte[] buffer = new byte[1000];
                //voy leyendo hasta que no queden bytes
                while (sv.read(buffer) > 0) {
                    f1.write(buffer);
                }
            }
        } catch (IOException ex) {
            System.out.println("Problemas al obtener las imágenes " + ex);
        }
    }

    /**
     * Método que obtiene todas las imágenes de la aplicación
     *
     * @param f
     * @param path
     */
    private static void extractAllImages(File f, String path) {
        try {
            EpubReader epubReader = new EpubReader();
            Book book = epubReader.readEpub(new FileInputStream(f));
            List<Resource> resources = book.getResources().getResourcesByMediaTypes(new MediaType[]{MediatypeService.PNG, MediatypeService.GIF, MediatypeService.JPG});
            for (Resource resource : resources) {
                try (FileOutputStream f1 = new FileOutputStream(path + "/" + resource.getId());
                        InputStream sv = resource.getInputStream()) {
                    byte[] buffer = new byte[1000];
                    //voy leyendo hasta que no queden bytes
                    while (sv.read(buffer) > 0) {
                        f1.write(buffer);
                    }
                }
            }
        } catch (IOException ex) {
            System.out.println("Problemas al obtener las imágenes " + ex);
        }
    }

    /**
     * Convierte los segundos en formato hh:mm:ss
     *
     * @param seconds
     * @return
     */
    public static String convertSecondsToHMmSs(long seconds) {
        long s = seconds % 60;
        long m = (seconds / 60) % 60;
        long h = (seconds / (60 * 60)) % 24;
        return String.format("%d:%02d:%02d", h, m, s);
    }

    /**
     * Suma dos tiempos pasados
     *
     * @param t1
     * @param t2
     * @return
     */
    public static Time SumTime(Time t1, Time t2) {
        long tm = 0;

        //tiempo 1
        String[] arr = t1.toString().split(":");
        tm += Integer.parseInt(arr[2]);
        tm += 60 * Integer.parseInt(arr[1]);
        tm += 3600 * Integer.parseInt(arr[0]);

        //tiempo 2
        arr = t2.toString().split(":");
        tm += Integer.parseInt(arr[2]);
        tm += 60 * Integer.parseInt(arr[1]);
        tm += 3600 * Integer.parseInt(arr[0]);

        long hh = tm / 3600;
        tm %= 3600;
        long mm = tm / 60;
        tm %= 60;
        long ss = tm;

        return Time.valueOf(format(hh) + ":" + format(mm) + ":" + format(ss));
    }

    /**
     * Método complementario para SumTime
     *
     * @param s
     * @return
     */
    private static String format(long s) {
        if (s < 10) {
            return "0" + s;
        } else {
            return "" + s;
        }
    }

    public static String convertDateToDatabaseFormat(String date) {
        try {
            Date parse = new SimpleDateFormat("dd-MM-yyyy").parse(date);
            return new SimpleDateFormat("yyyy-MM-dd").format(parse);
        } catch (ParseException ex) {
        }
        return null;
    }

}
