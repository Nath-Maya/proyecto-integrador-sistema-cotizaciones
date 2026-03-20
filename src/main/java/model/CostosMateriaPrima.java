package model;

import java.util.HashMap;

public class CostosMateriaPrima {
    private HashMap<String, Double> costos_material;

    // Constructor para inicializar el HashMap de costos de materiales
    public CostosMateriaPrima(){
        costos_material = new HashMap<>();
    }

    //  Método para actualizar el costo de un material específico, recibe el nombre del material y su nuevo costo
    public void actualizar_costo(String material, double costo) {
        costos_material.put(material, costo);
    }
    public double obtener_costo(String material){
        return costos_material.getOrDefault(material, 0.0);
    }
}
