package model;

import java.io.*;
import java.util.ArrayList;

public class BaseDatos implements Serializable{
    private ArrayList<Empresa> empresas;

    // Constructor para inicializar la lista de empresas
    public BaseDatos(){
        empresas = new ArrayList<>();
    }

    // Método para agregar una nueva empresa a la lista de empresas
    public void agregar_empresa(Empresa e){
        empresas.add(e);
    }

    // Método para buscar una empresa por su NIT, devuelve la empresa si se encuentra o null si no se encuentra
    public Empresa buscar_por_nit(String nit) {
        for (Empresa e : empresas) {
            if (e.getNit().equals(nit)){
                return e;
            }
        }
        return null;
    }
    public Empresa buscar_por_nombre (String nombre_empresa) {
        for (Empresa e : empresas) {
            if (e.getNombre_empresa().equalsIgnoreCase(nombre_empresa)){
                return e;
            }
        }
        return null;
    }
    public ArrayList<Empresa> getEmpresas(){
        return empresas;
    }
}
