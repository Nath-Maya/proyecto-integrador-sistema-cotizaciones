package controller;

import model.CostosMateriaPrima;

public class Cotizador {
    public static double calcular_costo(double ancho, double alto, String material, double costo_tinta, double porcentaje_perdida, CostosMateriaPrima costos) {
                
        double area = ancho * alto;
        double costo_material = costos.obtener_costo(material);
        double base = area * (costo_material + costo_tinta);
        double perdida = base * porcentaje_perdida;
        return base + perdida;
    }
}
