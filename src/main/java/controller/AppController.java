package controller;

import model.Cliente;
import model.CostosMateriaPrima;
import model.MaterialItem;
import repository.ClienteRepository;
import repository.FileClienteRepository;
import repository.FileMaterialRepository;
import repository.MaterialRepository;
import util.EnvLoader;
import util.Validaciones;

import java.util.List;

public class AppController {
    private final CostosMateriaPrima costos;
    private final ClienteRepository clienteRepository;
    private final MaterialRepository materialRepository;

    private double costo_tinta = 0;
    private double porcentaje_perdida = 0;

    private String appUser = null;

    public AppController() {
        costos = new CostosMateriaPrima();
        clienteRepository = new FileClienteRepository();
        materialRepository = new FileMaterialRepository();
        sincronizarCostosDesdeInventario();
    }

    public boolean appLogin(String usuario, String contrasena) {
        String user1 = EnvLoader.get("APP_USER_1_NAME", "Nathalia Maya");
        String pass1 = EnvLoader.get("APP_USER_1_PASSWORD", "1234");
        String user2 = EnvLoader.get("APP_USER_2_NAME", "Santiago Isaza");
        String pass2 = EnvLoader.get("APP_USER_2_PASSWORD", "5678");

        if ((user1.equals(usuario) && pass1.equals(contrasena)) ||
            (user2.equals(usuario) && pass2.equals(contrasena))) {
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

    public String getAppUser() {
        return this.appUser;
    }

    public boolean registrarCliente(String documento, String nombre, String correo, String telefono, String direccion) {
        if (!Validaciones.es_documento(documento)) {
            throw new IllegalArgumentException("El documento debe tener entre 5 y 15 dígitos");
        }
        if (!Validaciones.es_texto(nombre)) {
            throw new IllegalArgumentException("El nombre debe contener solo letras");
        }
        if (!Validaciones.es_correo(correo)) {
            throw new IllegalArgumentException("El correo no es válido");
        }
        if (!Validaciones.es_telefono(telefono)) {
            throw new IllegalArgumentException("El teléfono no es válido");
        }
        if (direccion == null || direccion.trim().isEmpty()) {
            throw new IllegalArgumentException("La dirección es obligatoria");
        }

        Cliente cliente = new Cliente(
                documento.trim(),
                nombre.trim(),
                correo.trim(),
                telefono.trim(),
                direccion.trim()
        );

        return clienteRepository.guardar(cliente);
    }

    public Cliente buscarCliente(String documento) {
        return clienteRepository.buscarPorDocumento(documento);
    }

    public List<Cliente> listarClientes() {
        return clienteRepository.listar();
    }

    public double cotizar(double ancho, double alto, String material, String tipo_impresion) {
        return Cotizador.calcular_costo(ancho, alto, material, costo_tinta, porcentaje_perdida, costos);
    }

    public List<MaterialItem> listarMateriales() {
        return materialRepository.listar();
    }

    public boolean agregarMaterial(String material, double ancho, double largo, int stock, double precioPorHoja) {
        if (material == null || material.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del material es obligatorio");
        }
        if (ancho <= 0 || largo <= 0) {
            throw new IllegalArgumentException("Ancho y largo deben ser mayores que cero");
        }
        if (stock < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo");
        }
        if (precioPorHoja <= 0) {
            throw new IllegalArgumentException("El precio por hoja debe ser mayor que cero");
        }

        MaterialItem item = new MaterialItem(material.trim(), ancho, largo, stock, precioPorHoja);
        boolean saved = materialRepository.guardar(item);
        if (saved) {
            costos.actualizar_costo(item.getMaterial(), item.getPrecioPorHoja());
        }
        return saved;
    }

    private void sincronizarCostosDesdeInventario() {
        for (MaterialItem m : materialRepository.listar()) {
            costos.actualizar_costo(m.getMaterial(), m.getPrecioPorHoja());
        }
    }
}
