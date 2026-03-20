package util;

public class Validaciones {

    //METODO DE VALIDACION PARA VER SI UN STRING ES UN NUMERO, DEVUELVE TRUE SI ES UN NUMERO Y FALSE SI NO LO ES
    public static boolean es_numero (String texto) {

        if (texto == null){
            return false;
        }

        if (texto.isEmpty()){
            return false;
        }
       
        try {
            Double.parseDouble(texto);
            return true;
        }
        catch(NumberFormatException e){
            return false;
        }
    }

    //METODO DE VALIDACION PARA VER SI UN STRING ES SOLO TEXTO, DEVUELVE TRUE SI ES SOLO TEXTO Y FALSE SI NO LO ES
    public static boolean es_texto (String texto) {

        if (texto == null) {
            return false;
        }

        if(texto.isEmpty()){
            return false;
        }

        return texto.matches("[a-zA-Z ]+");
    }

    //METODO DE VALIDACION PARA VER SI UN STRING ES UN ENTERO, DEVUELVE TRUE SI ES UN ENTERO Y FALSE SI NO LO ES
    public static boolean es_entero (String texto){
        if(texto == null){
            return false;
        }

        if(texto.isEmpty()){
            return false;
        }
        try {
            Integer.parseInt(texto);
            return true;
        }
        catch (NumberFormatException e){
            return false;
        }
    }

    //METODO DE VALIDACION PARA VER SI UN STRING ES UN TELEFONO, DEVUELVE TRUE SI ES UN TELEFONO Y FALSE SI NO LO ES
    
    public static boolean es_telefono(String texto){

        if(texto == null) {
            return false;
        }
        
        return texto.matches("\\d{7,10}");
    }

    public static boolean es_correo (String texto) {

        if(texto == null){
            return false;
        }
        return texto.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }
}
