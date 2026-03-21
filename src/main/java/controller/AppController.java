package controller;

import model.Cliente;
import model.CostosMateriaPrima;
import repository.ClienteRepository;
import repository.FileClienteRepository;
import util.EnvLoader;
import util.Validaciones;

import java.util.List;

public class AppController {
    private final CostosMateriaPrima costos;
    private final ClienteRepository clienteRepository;

    private double costo_tinta = 0;
    private double porcentaje_perdida = 0;

    private String appUser = null;

    public AppController() {
        costos = new CostosMateriaPrima();
        clienteRepository = new FileClienteRepository();
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
}
