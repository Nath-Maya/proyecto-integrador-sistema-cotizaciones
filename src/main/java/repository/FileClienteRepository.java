package repository;

import model.Cliente;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileClienteRepository implements ClienteRepository {

    private final String filePath;

    public FileClienteRepository() {
        this("data/clientes.dat");
    }

    public FileClienteRepository(String filePath) {
        this.filePath = filePath;
        asegurarDirectorio();
    }

    @Override
    public boolean guardar(Cliente cliente) {
        List<Cliente> clientes = cargarTodo();
        for (Cliente c : clientes) {
            if (c.getDocumento().equals(cliente.getDocumento())) {
                return false;
            }
        }
        clientes.add(cliente);
        persistir(clientes);
        return true;
    }

    @Override
    public Cliente buscarPorDocumento(String documento) {
        for (Cliente c : cargarTodo()) {
            if (c.getDocumento().equals(documento)) {
                return c;
            }
        }
        return null;
    }

    @Override
    public List<Cliente> listar() {
        return new ArrayList<>(cargarTodo());
    }

    @SuppressWarnings("unchecked")
    private List<Cliente> cargarTodo() {
        File f = new File(filePath);
        if (!f.exists()) {
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
            return (List<Cliente>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new ArrayList<>();
        }
    }

    private void persistir(List<Cliente> clientes) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(clientes);
        } catch (IOException e) {
            throw new RuntimeException("No fue posible guardar clientes", e);
        }
    }

    private void asegurarDirectorio() {
        File f = new File(filePath);
        File parent = f.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }
    }
}
