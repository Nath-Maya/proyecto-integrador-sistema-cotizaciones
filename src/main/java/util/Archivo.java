package util;

// Clase para manejar la lectura y escritura de datos en un archivo
import java.io.*;
import model.BaseDatos;

public class Archivo{
    
    // Nombre del archivo donde se guardarán los datos de las empresas
    private static final String NOMBRE_ARCHIVO = "empresas.dat";
    public static void guardar(BaseDatos bd){

        // Guardar el objeto BaseDatos en un archivo utilizando ObjectOutputStream
        try{
            ObjectOutputStream oss =
                new ObjectOutputStream(new FileOutputStream(NOMBRE_ARCHIVO));

                // Escribir el objeto BaseDatos en el archivo
                oss.writeObject(bd);
                oss.close();

                System.out.println("Los datos se han guardado correctamente"); 
        }
        // Manejar posibles excepciones durante la escritura del archivo
        catch (IOException e) {
            System.out.println("Error al guardar los datos");
            e.printStackTrace();
        }
    }

    // Método que se encarga de cargar los datos desde el archivo y devolver un objeto BaseDatos
     public static BaseDatos cargar() {
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(NOMBRE_ARCHIVO));

            // Leer el objeto Base de datos desde el archivo y devolverlo
            BaseDatos bd = (BaseDatos) ois.readObject();
            ois.close();

            System.out.println("Datos cargados correctamente");
            return bd;

        } 
        catch (FileNotFoundException e) {
            System.out.println("No existe archivo");
            return new BaseDatos();

        } 
            catch (IOException | ClassNotFoundException e) {
            System.out.println("Error al cargar los datos");
            e.printStackTrace();
            return new BaseDatos();
        }
    }
}
