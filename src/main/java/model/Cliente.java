package model;

import java.io.Serializable;

public class Cliente implements Serializable {
    private String documento;
    private String nombre;
    private String correo;
    private String telefono;
    private String direccion;

    public Cliente(String documento, String nombre, String correo, String telefono, String direccion) {
        this.documento = documento;
        this.nombre = nombre;
        this.correo = correo;
        this.telefono = telefono;
        this.direccion = direccion;
    }

    public String getDocumento() {
        return documento;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getDireccion() {
        return direccion;
    }
}
