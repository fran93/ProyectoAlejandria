package alejandria;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.Resource;
import nl.siegmann.epublib.domain.Spine;
import nl.siegmann.epublib.domain.SpineReference;
import nl.siegmann.epublib.epub.EpubReader;
/**
 *
 * @author Fran
 */
public class ReadEpub {
    private Book book;
    private String[] episode;
    private int page;
    
    /**
     * Contructor por defecto.
     * @param titulo 
     */
    public ReadEpub(String titulo) {
        try {
            // leer un archivo epub
            EpubReader epubReader = new EpubReader();
            book = epubReader.readEpub(new FileInputStream(titulo));     
            Spine spine = new Spine(book.getTableOfContents());
            //creo un array  para almacenar los capítulos
            episode = new String[spine.size()];
            int i = 0;
            page = 0;
            //recorro las secciones y las voy almacenando en el array
            for (SpineReference bookSection : spine.getSpineReferences()) {
                episode[i]="";
                Resource res = bookSection.getResource();
                //obtengo todo el contenido de la sección 
                try (BufferedReader br = new BufferedReader(new InputStreamReader(res.getInputStream()))){
                    while(br.ready()){
                        episode[i]+=br.readLine();
                    }
                    i++;
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        } catch (IOException ex) {               
            System.out.println(ex.getMessage());
        }
    }
    
    /**
     * Imprime el título del libro
     * @return 
     */
    public String getTitle(){
           List<String> titles = book.getMetadata().getTitles();
           return (titles.isEmpty() ? "book has no title" : titles.get(0));
    }

    /**
     * Devuelve el contenido de la página actual
     * @return 
     */
    public String getPageContent() {
        return episode[page];
    }

    /**
     * Cambia de página
     * @param page 
     */
    public void setPage(int page) {
        this.page = page;
    }

    /**
     * Devuelve el número de página actual
     * @return 
     */
    public int getPage() {
        return page;
    }
    
    /**
     * Devuelve el número de páginas
     * @return 
     */
    public int getTotalPage(){
        return episode.length;
    }
    
    /**
     * Devuelve si un libro ha sido leído o no
     * @return 
     */
    public boolean readedBook(){
        return episode.length==page;
    }
    
    
    
    
}
