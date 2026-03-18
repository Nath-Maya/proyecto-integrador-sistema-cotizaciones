public class Administrador {

    private String usuario;
    private String contrasena;

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