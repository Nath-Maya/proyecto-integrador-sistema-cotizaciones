package repository;

import model.Cliente;
import java.util.List;

public interface ClienteRepository {
    boolean guardar(Cliente cliente);
    Cliente buscarPorDocumento(String documento);
    List<Cliente> listar();
}
