import java.util.Scanner;

public class MenuSistema {

    private BaseDatos bd = new BaseDatos();
    private costos_materia_prima costos = new costos_materia_prima();

    private double costo_tinta = 0;
    private double porcentaje_perdida = 0;

    private Empresa empresa_actual = null;
    private Administrador admin = new Administrador("admin", "1234");
    private Administrador admin_actual = null;

    private Scanner sc = new Scanner (System.in);
    public void iniciar() {
        int opcion;

        do {
            if (empresa_actual == null) {
                if (admin_actual == null) {
                    menu_publico();
                }
                else {
                    menu_admin();
                }
            }
            else {
                menu_empresa();
            }

            opcion = Integer.parseInt(sc.nextLine());

            if(empresa_actual == null) {
                if(admin_actual == null) {
                    switch(opcion){
                        case 1:
                            registrar_empresa();
                            break;
                        
                        case 2:
                            login_empresa();
                            break;

                        case 3:
                            login_admin();
                            break;
                    }
                }
            }
            else {
                switch(opcion) {
                    case 1:
                        cotizar_pedido();
                        break;
                    
                    case 2:
                        ver_historial();
                        break;
                    
                    case 3:
                        empresa_actual = null;
                        System.out.println ("Sesion de empresa cerrada");
                        break;
                }
            }
            if(empresa_actual == null) {
                if(admin_actual != null) {

                    switch(opcion){

                        case 1:
                            configurar_costos();
                            break;

                        case 2:
                            ver_empresas();
                            break;

                        case 3:
                            historial_global();
                            break;

                        case 4:
                            admin_actual = null;
                            System.out.println("Sesion del administrador cerrada");
                    }
                }
            }
        } 
        while (true);
    }

    private void menu_publico(){
        System.out.println("1. Registrar empresa");
        System.out.println("2. Inicio de sesion empresa");
        System.out.println("3. Inicio de sesion administrador");
    }

    private void menu_empresa() {
        System.out.println("1. Cotizar pedido");
        System.out.println("2. Historial de pedidos");
        System.out.println("3. Cerrar sesion");
    }
    private void menu_admin(){
        System.out.println("1. Configurar costos de materia prima");
        System.out.println("2. Ver empresas");
        System.out.println("3. Historial completo");
        System.out.println("4. Cerrar sesion");
    }

    private void registrar_empresa(){
        System.out.println("Nit: ");
        String nit = sc.nextLine();

        System.out.println("Nombre de empresa: ");
        String nombre_empresa = sc.nextLine();

        System.out.println("Direccion: ");
        String direccion = sc.nextLine();

        System.out.println("Correo: ");
        String correo = sc.nextLine();

        System.out.println("Telefono: ");
        String telefono = sc.nextLine();

        System.out.println("Contraseña: ");
        String contrasena = sc.nextLine();

        Empresa e = new Empresa(nit, nombre_empresa, direccion, correo, telefono, contrasena);
        bd.agregar_empresa(e);

        System.out.println("Empresa registrada");
    }

    private void login_empresa(){
        System.out.println("Nit: ");
        String nit = sc.nextLine();

        System.out.println("Contraseña: ");
        String contrasena = sc.nextLine();

        Empresa e = bd.buscar_por_nit(nit);

        if (e != null) {
            if (e.getContrasena().equals(pass)) {
                empresa_actual = e;
                System.out.println("Inicio de sesión exitoso");
            } else {
                System.out.println("Contraseña incorrecta");
            }
        } else {
            System.out.println("Empresa no encontrada");
        }
    }

    private void login_admin() {
        System.out.println("Usuario: ");
        String u = sc.nextLine();

        System.out.println("Contraseña: ");
        String p = sc.nextLine();

        if (admin.getUsuario().equals(u) && admin.getContrasena().equals(p)) {
            admin_actual = admin;
            System.out.println("Inicio de sesión como administrador");
        } else {
            System.out.println("Credenciales incorrectas");
        }
    }

    private void configurar_costos() {

        System.out.println("Costo tinta: ");
        double t = Double.parseDouble(sc.nextLine());

        System.out.println("Material: ");
        String material = sc.nextLine();

        System.out.println("Costo material: ");
        double costo = Double.parseDouble(sc.nextLine());

        System.out.println("Porcentaje de pérdida (ej: 0.1 para 10%): ");
        double perdida = Double.parseDouble(sc.nextLine());

        costo_tinta = t;
        porcentaje_perdida = perdida;

        costos.actualizar_costo(material, costo);

        System.out.println("Costos actualizados correctamente");
    }

    private void cotizar_pedido() {

        System.out.println("Ancho: ");
        double ancho = Double.parseDouble(sc.nextLine());

        System.out.println("Alto: ");
        double alto = Double.parseDouble(sc.nextLine());

        System.out.println("Material: ");
        String material = sc.nextLine();

        System.out.println("Tipo de impresión: ");
        String tipo = sc.nextLine();

        double costo = Cotizador.calcular_costo(
                ancho, alto, material,
                costo_tinta, porcentaje_perdida, costos
        );

        Pedido p = new Pedido(ancho, alto, material, tipo, costo);

        empresa_actual.agregar_pedido(p);

        Archivo.guardar(bd);

        System.out.println("Cotización generada. Costo: " + costo);
    }

    private void ver_historial() {
        for (Pedido p : empresa_actual.getHistorial_pedidos()) {
            System.out.println("Costo: " + p.getCosto());
        }
    }

    private void ver_empresas() {
        for (Empresa e : bd.getEmpresas()) {
            System.out.println("Empresa: " + e.getNombre_empresa());
        }
    }

    private void historial_global() {
        for (Empresa e : bd.getEmpresas()) {
            System.out.println("\nEmpresa: " + e.getNombre_empresa());

            for (Pedido p : e.getHistorial_pedidos()) {
                System.out.println("Costo: " + p.getCosto());
            }
        }
    }
}