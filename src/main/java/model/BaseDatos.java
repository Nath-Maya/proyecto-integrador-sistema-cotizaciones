package model;

import java.io.*;
import java.util.ArrayList;

public class BaseDatos implements Serializable{
    private ArrayList<Empresa> empresas;

    public BaseDatos(){
        empresas = new ArrayList<>();
    }

    public void agregar_empresa(Empresa e){
        empresas.add(e);
    }

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
