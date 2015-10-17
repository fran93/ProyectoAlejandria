package alejandria;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.TreeMap;

/**
 *
 * @author Fran1488
 */
public class DB {

    private Statement sta;

    /**
     * Constructor por defecto que se conecta a la base de datos.
     */
    public DB() {
        try {
            Connection con = DriverManager.getConnection("jdbc:h2:./biblioteca", "librero", "");
            sta = con.createStatement();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            System.exit(0);
        }

    }

    /**
     * Método que crea las tablas
     */
    public void createDB() {
        try {
            sta.executeUpdate("create table if not exists libro("
                    + "id int NOT NULL auto_increment,"
                    + "autor varchar(50),"
                    + "titulo varchar(100) not null,"
                    + "genero varchar(40),"
                    + "editor varchar(80),"
                    + "fichero varchar(100),"
                    + "formato varchar(4) not null,"
                    + "fecha date,"
                    + "leido boolean default false,"
                    + "PRIMARY KEY (id),"
                    + "check (formato in ('epub',  'pdf'))"
                    + ")");
            sta.executeUpdate("create table if not exists lectura("
                    + "id bigint NOT NULL auto_increment,"
                    + "pagina int,"
                    + "tiempo time,"
                    + "scroll int,"
                    + "libro int,"
                    + "PRIMARY KEY (id),"
                    + "FOREIGN KEY (libro) REFERENCES libro(id) ON DELETE CASCADE"
                    + ")");
        } catch (SQLException ex) {
            System.out.println("Error al crear la base de datos: " + ex.getMessage());
        }
    }

    /**
     * Método que añade un libro a la base de datos
     *
     * @param autor
     * @param titulo
     * @param genero
     * @param editor
     * @param fichero
     * @param formato
     * @param fecha
     */
    public void addBook(String autor, String titulo, String genero, String editor, String fichero, String formato, Date fecha) {
        try {
            sta.executeUpdate("insert into libro(autor, titulo, genero, editor, fichero, formato, fecha) values("
                    + "'" + autor + "', "
                    + "'" + titulo + "',  "
                    + "'" + genero + "', "
                    + "'" + editor + "', "
                    + "'" + fichero + "', "
                    + "'" + formato + "', "
                    + "'" + new SimpleDateFormat("yyyy-mm-dd").format(fecha) + "')");
        } catch (SQLException ex) {
            try {
                //el error más frecuente es la fecha, por lo tanto intento introducirla de nuevo dejandola en null
                sta.executeUpdate("insert into libro(autor, titulo, genero, editor, fichero, formato) values("
                        + "'" + autor + "', "
                        + "'" + titulo + "',  "
                        + "'" + genero + "', "
                        + "'" + editor + "', "
                        + "'" + fichero + "', "
                        + "'" + formato + "')");
            } catch (SQLException ex1) {
                System.out.println("Error al añadir libro en la base de datos: " + ex1.getMessage());
            }
        }
    }

    /**
     * Método que devuelve el título del libro a partir del id
     *
     * @param id
     * @return
     */
    public String getTituloById(int id) {
        String titulo = "";
        try {
            ResultSet rs = sta.executeQuery("SELECT titulo FROM libro where id=" + id);
            rs.next();
            titulo = rs.getString("titulo");
        } catch (SQLException ex) {
            System.out.println("Error obteniendo el título " + ex);
        }
        return titulo;
    }

    /**
     * Método que añade una lectura
     *
     * @param pagina
     * @param tiempo
     * @param scroll
     * @param libro
     */
    public void addRead(int pagina, Time tiempo, int scroll, int libro) {
        try {
            sta.executeUpdate("insert into lectura(pagina, tiempo, scroll, libro) values("
                    + pagina + ","
                    + "'" + tiempo + "',"
                    + scroll + ","
                    + libro + ")");
        } catch (SQLException ex1) {
            System.out.println("Error al añadir libro en la base de datos: " + ex1.getMessage());
        }
    }

    /**
     * Método que devuelve la última página que has leído
     *
     * @param id
     * @return
     */
    public TreeMap<Integer, Integer> getReadPage(int id) {
        TreeMap tree = new TreeMap<>();
        try {
            ResultSet rs = sta.executeQuery("SELECT id, pagina, scroll FROM lectura WHERE libro=" + id + " ORDER BY id DESC, pagina DESC, scroll DESC LIMIT 1");

            if (rs.next()) {
                tree.put(rs.getInt("pagina"), rs.getInt("scroll"));
            } else {
                tree.put(0, 0);
            }

        } catch (SQLException ex) {
            System.out.println("Error con las lecturas " + ex);
        }
        return tree;
    }

    /**
     * Método que obtiene los detalles de un libro pasado por parámetro
     *
     * @param id
     * @return
     */
    public String[] getLibroDetails(int id) {
        String[] buffer = new String[5];
        try {
            ResultSet rs = sta.executeQuery("select autor, genero, editor, fecha, leido from libro where id=" + id);
            rs.next();
            buffer[0] = rs.getString("autor").equals("null") ? "No hay datos" : rs.getString("autor");
            buffer[1] = rs.getString("editor").equals("null") ? "No hay datos" : rs.getString("editor");
            buffer[2] = rs.getString("genero").equals("null") ? "No hay datos" : rs.getString("genero");
            buffer[3] = rs.getDate("fecha") == null ? "No hay datos" : new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("fecha"));
            buffer[4] = rs.getBoolean("leido") ? "Sí" : "No";
        } catch (SQLException ex) {
            System.out.println("Libro no encontrado: " + ex.getMessage());
        }
        return buffer;
    }

    /**
     * Método que modifica el libro pasado por parámetro.
     *
     * @param id
     * @param datos
     */
    public void editLibro(int id, String[] datos) {
        try {
            sta.executeUpdate("update libro set titulo='" + datos[0] + "', autor='" + datos[1] + "', editor='" + datos[2]
                    + "', genero='" + datos[3] + "', fecha='" + Utils.convertDateToDatabaseFormat(datos[4]) + "' where id=" + id);
        } catch (SQLException e) {
            try {
                sta.executeUpdate("update libro set titulo='" + datos[0] + "', autor='" + datos[1] + "', editor='" + datos[2]
                        + "', genero='" + datos[3] + "' where id=" + id);
            } catch (SQLException ex) {
                System.out.println("Error actualizando el libro: " + ex.getMessage());
            }

        }
    }

    /**
     * Devuelve el nombre del fichero pasandole el id por parámetro
     *
     * @param id
     * @return
     */
    public String getFichero(int id) {
        try {
            ResultSet rs = sta.executeQuery("select fichero from libro where id=" + id);
            rs.next();

            return rs.getString("fichero");
        } catch (SQLException ex) {
            System.out.println("Nombre del archivo no encontrado: " + ex.getMessage());
        }
        return null;
    }

    /**
     * Devuelve un TreeMap con los libros en orden inverso
     *
     * @return
     */
    public TreeMap<Integer, String> getListaLibros() {
        //creo un TreeMap con el orden invertido, así los últimos libros añadidos salen en primer lugar.
        TreeMap tree = new TreeMap<>(Collections.reverseOrder());
        try {
            ResultSet rs = sta.executeQuery("SELECT id, titulo FROM libro");
            while (rs.next()) {
                tree.put(rs.getInt("id"), rs.getString("titulo"));
            }
        } catch (SQLException ex) {
            System.out.println("Error listando los libros: " + ex.getMessage());
        }

        return tree;
    }

    /**
     * Devuelve un TreeMap con los datos filtrados 0 Autor 1 Editor 2 Género 3
     * Título 4 Leídos
     *
     * @param filtro
     * @param tipo
     * @return
     */
    public TreeMap<Integer, String> getListaLibros(String filtro, int tipo) {
        //creo un TreeMap con el orden invertido, así los últimos libros añadidos salen en primer lugar.
        TreeMap tree = new TreeMap<>(Collections.reverseOrder());
        ResultSet rs;
        try {
            //dependiendo del tipo de filtro se hace una consulta u otra
            switch (tipo) {
                case 0:
                    rs = sta.executeQuery("SELECT id, titulo FROM libro WHERE autor='" + filtro + "'");
                    break;
                case 1:
                    rs = sta.executeQuery("SELECT id, titulo FROM libro WHERE editor='" + filtro + "'");
                    break;
                case 2:
                    rs = sta.executeQuery("SELECT id, titulo FROM libro WHERE genero='" + filtro + "'");
                    break;
                case 3:
                    rs = sta.executeQuery("SELECT id, titulo FROM libro WHERE titulo LIKE '%" + filtro + "%'");
                    break;
                case 4:
                    rs = sta.executeQuery("SELECT id, titulo FROM libro WHERE leido=" + filtro);
                    break;
                default:
                    rs = sta.executeQuery("SELECT id, titulo FROM libro");
                    break;
            }
            while (rs.next()) {
                tree.put(rs.getInt("id"), rs.getString("titulo"));
            }
        } catch (SQLException ex) {
            System.out.println("Error listando los libros: " + ex.getMessage());
        }

        return tree;
    }

    /**
     * Método que devuelve el id del último libro añadido
     *
     * @return
     */
    public int maxLibro() {
        int i = 0;
        try {
            ResultSet rs = sta.executeQuery("select max(id) from libro");
            rs.next();
            i = rs.getInt("max(id)");

        } catch (SQLException ex) {
            System.out.println("Error al obtener el id: " + ex.getMessage());
        }
        return i;
    }

    /**
     * Que no se me olvide borrarlo.
     *
     * @param id
     */
    public void eliminarLibro(int id) {
        try {
            sta.executeUpdate("delete from libro where id=" + id);
        } catch (SQLException ex) {
            System.out.println("Error al borrar libro: " + ex.getMessage());
        }
    }

    /**
     * Método que devuelve el número de libros añadidos por el usuario
     *
     * @return
     */
    public int getTotalBooks() {
        int i = 0;
        try {
            ResultSet rs = sta.executeQuery("select count(id) from libro");
            rs.next();
            i = rs.getInt("count(id)");

        } catch (SQLException ex) {
            System.out.println("Error al obtener el id: " + ex.getMessage());
        }
        return i;
    }

    /**
     * Método que te devuelve el tiempo total que has pasado leyendo.
     *
     * @return
     */
    public Time getTotalTime() {
        Time sum = Time.valueOf("00:00:00");
        //h2 no soporta la suma de datos Time, así que lo haré manualmente
        try {
            ResultSet rs = sta.executeQuery("select tiempo from lectura");
            while (rs.next()) {
                Time buffer = rs.getTime("tiempo");
                sum = Utils.SumTime(buffer, sum);
            }
        } catch (SQLException ex) {
            System.out.println("Error al obtener el tiempo: " + ex.getMessage());
        }
        return sum;
    }

    /**
     * Método que te devuelve el tiempo total que has pasado leyendo.
     *
     * @param libro
     * @return
     */
    public Time getTotalTime(int libro) {
        Time sum = Time.valueOf("00:00:00");
        //h2 no soporta la suma de datos Time, así que lo haré manualmente
        try {
            ResultSet rs = sta.executeQuery("select tiempo from lectura where libro=" + libro);
            while (rs.next()) {
                Time buffer = rs.getTime("tiempo");
                sum = Utils.SumTime(buffer, sum);
            }
        } catch (SQLException ex) {
            System.out.println("Error al obtener el tiempo: " + ex.getMessage());
        }
        return sum;
    }

    /**
     * Método que devuelve el total de página leídas
     *
     * @return
     */
    public int getTotalPages() {
        int i = 0;
        try {
            //obtengo las páginas leídas de cada libro y las voy sumando
            ResultSet rs = sta.executeQuery("select max(pagina) from lectura group by(libro)");
            while (rs.next()) {
                i += rs.getInt("max(pagina)");
            }
        } catch (SQLException ex) {
            System.out.println("Error al obtener el id: " + ex.getMessage());
        }
        return i;
    }

    /**
     * Método que devuelve el total de página leídas de un libro
     *
     * @param libro
     * @return
     */
    public int getTotalPages(int libro) {
        int i = 0;
        try {
            //obtengo las páginas leídas de cada libro y las voy sumando
            ResultSet rs = sta.executeQuery("select max(pagina) from lectura where libro=" + libro);
            rs.next();
            i += rs.getInt("max(pagina)");
        } catch (SQLException ex) {
            System.out.println("Error al obtener el id: " + ex.getMessage());
        }
        return i;
    }

    /**
     * Devuelve el autor favorito del usuario.
     *
     * @return
     */
    public String getAutorFavorito() {
        String buffer = "Desconocido";
        try {
            //obtengo el autor que más libros tiene añadidos
            ResultSet rs = sta.executeQuery("select autor, count(autor) "
                    + "from libro "
                    + "group by autor "
                    + "order by count(autor) desc "
                    + "limit 1;");
            rs.next();
            buffer = rs.getString("autor");
        } catch (SQLException ex) {
            System.out.println("Error al obtener el autor: " + ex.getMessage());
        }
        return buffer;
    }

    /**
     * Marca un libro como leído.
     *
     * @param id
     */
    public void marcarComoLeido(int id) {
        try {
            sta.executeUpdate("update libro set leido = true where id =" + id);
        } catch (SQLException ex) {
            System.out.println("Error marcando libro como leído " + ex.getMessage());
        }
    }

    /**
     * Devuelve el número de libros sin leer
     *
     * @return
     */
    public int getUnreadBooks() {
        int i = 0;
        try {
            //obtengo los libros sin leer
            ResultSet rs = sta.executeQuery("select count(id) from libro where leido=false");
            while (rs.next()) {
                i = rs.getInt("count(id)");
            }
        } catch (SQLException ex) {
            System.out.println("Error al obtener libros sin leer: " + ex.getMessage());
        }
        return i;
    }

    /**
     * Devuelve el libro favorito del usuario
     *
     * @return
     */
    public String getFavoriteBook() {
        String buffer = "Desconocido";
        try {
            //obtengo el libro que más veces ha leído 
            ResultSet rs = sta.executeQuery("select libro, count(libro) "
                    + "from lectura "
                    + "group by libro "
                    + "order by count(libro) desc "
                    + "limit 1;");
            rs.next();
            //se obtiene el id del libro más leído
            int i = rs.getInt("libro");
            //se obtiene el título de ese libro
            buffer = getTituloById(i);
        } catch (SQLException ex) {
            System.out.println("Error al obtener el autor: " + ex.getMessage());
        }
        return buffer;
    }

    /**
     * Devuelve los autores de la base de datos
     *
     * @return
     */
    public String[] getAuthors() {
        ArrayList<String> authors = new ArrayList<>();
        try {
            ResultSet rs = sta.executeQuery("SELECT DISTINCT autor FROM libro ORDER BY autor");
            while (rs.next()) {
                authors.add(rs.getString("autor"));
            }
        } catch (SQLException ex) {
            System.out.println("Error al obtener el autor: " + ex.getMessage());
        }
        //convierto el array List en array
        String[] buffer = new String[authors.size()];
        buffer = authors.toArray(buffer);
        return buffer;
    }

    /**
     * Devuelve los editores de la base de datos
     *
     * @return
     */
    public String[] getEditors() {
        ArrayList<String> editors = new ArrayList<>();
        try {
            ResultSet rs = sta.executeQuery("SELECT DISTINCT editor FROM libro ORDER BY editor");
            while (rs.next()) {
                editors.add(rs.getString("editor"));
            }
        } catch (SQLException ex) {
            System.out.println("Error al obtener el editor: " + ex.getMessage());
        }
        //convierto el array List en array
        String[] buffer = new String[editors.size()];
        buffer = editors.toArray(buffer);
        return buffer;
    }

    /**
     * Devuelve los géneros literarios de la base de datos
     *
     * @return
     */
    public String[] getGeneros() {
        ArrayList<String> generos = new ArrayList<>();
        try {
            ResultSet rs = sta.executeQuery("SELECT DISTINCT genero FROM libro ORDER BY genero");
            while (rs.next()) {
                generos.add(rs.getString("genero"));
            }
        } catch (SQLException ex) {
            System.out.println("Error al obtener el género: " + ex.getMessage());
        }
        //convierto el array List en array
        String[] buffer = new String[generos.size()];
        buffer = generos.toArray(buffer);
        return buffer;
    }

    /**
     * Método que borra la base de datos
     */
    public void deleteDB() {
        try {
            sta.executeUpdate("drop table libro");
            sta.executeUpdate("drop table lectura");
        } catch (SQLException ex) {
            System.out.println("Error al borrar la base de datos: " + ex.getMessage());
        }
    }

}
