package model;

import java.io.Serializable;
import java.util.ArrayList;


public class Empresa implements Serializable {
    private String nit;
    private String nombre_empresa;
    private String direccion;
    private String correo;
    private String telefono;
    private String contrasena;


    private ArrayList<Pedido> historial_pedidos;

    public Empresa(String nit, String nombre_empresa, String direccion, String correo, String telefono, String contrasena) {
        this.nit = nit;
        this.nombre_empresa = nombre_empresa;
        this.direccion = direccion;
        this.correo = correo;
        this.telefono = telefono;
        this.contrasena = contrasena;
        historial_pedidos = new ArrayList<>();
    }

    public String getNit() {
        return nit;
    }

    public String getNombre_empresa(){
        return nombre_empresa;
    }

    public String getContrasena(){
        return contrasena;
    }

    public ArrayList<Pedido> getHistorial_pedidos() {
        return historial_pedidos;
    }

    public void agregar_pedido(Pedido p) {
        historial_pedidos.add(p);
    }
}
