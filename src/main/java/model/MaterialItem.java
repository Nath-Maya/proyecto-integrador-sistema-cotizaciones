package model;

import java.io.Serializable;

public class MaterialItem implements Serializable {
    private String material;
    private double ancho;
    private double largo;
    private int stock;
    private double precioPorHoja;

    public MaterialItem(String material, double ancho, double largo, int stock, double precioPorHoja) {
        this.material = material;
        this.ancho = ancho;
        this.largo = largo;
        this.stock = stock;
        this.precioPorHoja = precioPorHoja;
    }

    public String getMaterial() {
        return material;
    }

    public double getAncho() {
        return ancho;
    }

    public double getLargo() {
        return largo;
    }

    public int getStock() {
        return stock;
    }

    public double getPrecioPorHoja() {
        return precioPorHoja;
    }

    public double getTotal() {
        return stock * precioPorHoja;
    }
}
