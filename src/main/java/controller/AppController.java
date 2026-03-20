package controller;

import model.*;
import util.Archivo;

import java.util.ArrayList;
import java.util.List;

public class AppController {
    private BaseDatos bd;
    private CostosMateriaPrima costos;

    private double costo_tinta = 0;
    private double porcentaje_perdida = 0;

    private Empresa empresa_actual = null;
    private Administrador admin = new Administrador("admin", "1234");
    private Administrador admin_actual = null;
    private String appUser = null; // usuario de la aplicación (Nathalia/Santiago)

    public AppController() {
        // Cargar datos persistidos
        bd = Archivo.cargar();
        costos = new CostosMateriaPrima();
    }

    // App-level login (dos usuarios permitidos)
    public boolean appLogin(String usuario, String contrasena) {
        if (("Nathalia Maya".equals(usuario) && "1234".equals(contrasena)) ||
            ("Santiago Isaza".equals(usuario) && "5678".equals(contrasena))) {
            this.appUser = usuario;
            return true;
        }
        return false;
    }

    public void appLogout() {
        this.appUser = null;
    }

    public boolean isAppLoggedIn() {
        return this.appUser != null;
    }

    public String getAppUser() { return this.appUser; }

    public boolean registrarEmpresa(String nit, String nombre, String direccion, String correo, String telefono, String contrasena) {
        if (bd.buscar_por_nit(nit) != null) {
            return false; // ya existe
        }
        Empresa e = new Empresa(nit, nombre, direccion, correo, telefono, contrasena);
        bd.agregar_empresa(e);
        Archivo.guardar(bd);
        return true;
    }

    public Empresa loginEmpresa(String nit, String contrasena) {
        Empresa e = bd.buscar_por_nit(nit);
        if (e != null && e.getContrasena().equals(contrasena)) {
            empresa_actual = e;
            return e;
        }
        return null;
    }

    public boolean loginAdmin(String usuario, String contrasena) {
        if (admin.getUsuario().equals(usuario) && admin.getContrasena().equals(contrasena)) {
            admin_actual = admin;
            return true;
        }
        return false;
    }

    public double cotizar(double ancho, double alto, String material, String tipo_impresion) {
        double costo = Cotizador.calcular_costo(ancho, alto, material, costo_tinta, porcentaje_perdida, costos);
        Pedido p = new Pedido(ancho, alto, material, tipo_impresion, costo);
        if (empresa_actual != null) {
            empresa_actual.agregar_pedido(p);
            Archivo.guardar(bd);
        }
        return costo;
    }

    // Cotizar y opcionalmente asociar a una empresa por nit (si nit == null no se asocia)
    public double cotizar(double ancho, double alto, String material, String tipo_impresion, String nit) {
        double costo = Cotizador.calcular_costo(ancho, alto, material, costo_tinta, porcentaje_perdida, costos);
        Pedido p = new Pedido(ancho, alto, material, tipo_impresion, costo);
        Empresa target = null;
        if (nit != null && !nit.isEmpty()) {
            target = bd.buscar_por_nit(nit);
        }
        if (target != null) {
            target.agregar_pedido(p);
            Archivo.guardar(bd);
        } else if (empresa_actual != null) {
            empresa_actual.agregar_pedido(p);
            Archivo.guardar(bd);
        }
        return costo;
    }

    public void configurarCostos(double tinta, String material, double costoMaterial, double porcentajePerdida) {
        this.costo_tinta = tinta;
        this.porcentaje_perdida = porcentajePerdida;
        costos.actualizar_costo(material, costoMaterial);
        Archivo.guardar(bd);
    }

    public List<Empresa> listarEmpresas() {
        return new ArrayList<>(bd.getEmpresas());
    }

    public List<Pedido> historialEmpresa(Empresa e) {
        return e.getHistorial_pedidos();
    }

    public List<Pedido> historialGlobal() {
        List<Pedido> all = new ArrayList<>();
        for (Empresa e : bd.getEmpresas()) {
            all.addAll(e.getHistorial_pedidos());
        }
        return all;
    }

    public Empresa getEmpresaActual() { return empresa_actual; }
}
