package model;

import java.util.HashMap;

public class CostosMateriaPrima {
    private HashMap<String, Double> costos_material;

    public CostosMateriaPrima(){
        costos_material = new HashMap<>();
    }

    public void actualizar_costo(String material, double costo) {
        costos_material.put(material, costo);
    }
    public double obtener_costo(String material){
        return costos_material.getOrDefault(material, 0.0);
    }
}
