/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alejandria;

/**
   La clase libro nos sirve para añadir los datos al JList
 * @author Fran1488
 */
public class Libro {

        private String titulo;
        private int id;

        public Libro(String titulo, int id) {
            this.titulo = titulo;
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public String getTitulo() {
            return titulo;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setTitulo(String titulo) {
            this.titulo = titulo;
        }

        @Override
        public String toString() {
            return titulo;
        }
    }
