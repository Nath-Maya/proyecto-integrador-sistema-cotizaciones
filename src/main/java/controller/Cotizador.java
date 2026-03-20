package controller;

import model.CostosMateriaPrima;

public class Cotizador {
    public static double calcular_costo(double ancho, double alto, String material, double costo_tinta, double porcentaje_perdida, CostosMateriaPrima costos) {
                
        // CALCULAR EL AREA DEL PAPEL
        double area = ancho * alto;
        // OBTENER EL COSTO DEL MATERIAL UTILIZADO
        double costo_material = costos.obtener_costo(material);
        // CALCULAR EL COSTO BASE DE LA COTIZACION
        double base = area * (costo_material + costo_tinta);
        // CALCULAR EL COSTO FINAL DE LA COTIZACION INCLUYENDO EL PORCENTAJE DE PERDIDA
        double perdida = base * porcentaje_perdida;
        // DEVOLVER EL COSTO FINAL DE LA COTIZACION
        return base + perdida;
    }
}
