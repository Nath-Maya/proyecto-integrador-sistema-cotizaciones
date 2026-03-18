public class Validaciones {

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

    public static boolean es_texto (String texto) {

        if (texto == null) {
            return false;
        }

        if(texto.isEmpty()){
            return false;
        }

        return texto.matches("[a-zA-Z ]+");
    }

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
