package model;

public class Administrador {

    private String usuario;
    private String contrasena;

    // Constructor para crear un nuevo administrador con un nombre de usuario y contraseña
    public Administrador(String usuario, String contrasena){
        this.usuario = usuario;
        this.contrasena = contrasena;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getContrasena(){
        return contrasena;
    }
}
