package util;

import java.io.*;
import model.BaseDatos;

public class Archivo{
    
    private static final String NOMBRE_ARCHIVO = "empresas.dat";
    public static void guardar(BaseDatos bd){

        try{
            ObjectOutputStream oss =
                new ObjectOutputStream(new FileOutputStream(NOMBRE_ARCHIVO));

                oss.writeObject(bd);
                oss.close();

                System.out.println("Los datos se han guardado correctamente");
        }
        catch (IOException e) {
            System.out.println("Error al guardar los datos");
            e.printStackTrace();
        }
    }

     public static BaseDatos cargar() {
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(NOMBRE_ARCHIVO));

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
