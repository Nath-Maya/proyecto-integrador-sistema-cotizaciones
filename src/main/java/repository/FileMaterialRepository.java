package repository;

import model.MaterialItem;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileMaterialRepository implements MaterialRepository {

    private final String filePath;

    public FileMaterialRepository() {
        this("data/materiales.dat");
    }

    public FileMaterialRepository(String filePath) {
        this.filePath = filePath;
        asegurarDirectorio();
        seedSiNoExiste();
    }

    @Override
    public List<MaterialItem> listar() {
        return new ArrayList<>(cargarTodo());
    }

    @Override
    public boolean guardar(MaterialItem material) {
        List<MaterialItem> materiales = cargarTodo();
        for (MaterialItem m : materiales) {
            if (m.getMaterial().equalsIgnoreCase(material.getMaterial())) {
                return false;
            }
        }
        materiales.add(material);
        persistir(materiales);
        return true;
    }

    @SuppressWarnings("unchecked")
    private List<MaterialItem> cargarTodo() {
        File f = new File(filePath);
        if (!f.exists()) {
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
            return (List<MaterialItem>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new ArrayList<>();
        }
    }

    private void persistir(List<MaterialItem> materiales) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(materiales);
        } catch (IOException e) {
            throw new RuntimeException("No fue posible guardar materiales", e);
        }
    }

    private void seedSiNoExiste() {
        File f = new File(filePath);
        if (f.exists()) {
            return;
        }

        List<MaterialItem> seed = new ArrayList<>();
        seed.add(new MaterialItem("Maule calibre 40", 70, 100, 3000, 1200));
        seed.add(new MaterialItem("Maule calibre 35", 70, 100, 1250, 1120));
        seed.add(new MaterialItem("Ultra calibre 48", 70, 100, 4550, 1350));
        persistir(seed);
    }

    private void asegurarDirectorio() {
        File f = new File(filePath);
        File parent = f.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }
    }
}
