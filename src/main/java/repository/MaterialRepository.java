package repository;

import model.MaterialItem;

import java.util.List;

public interface MaterialRepository {
    List<MaterialItem> listar();
    boolean guardar(MaterialItem material);
}
