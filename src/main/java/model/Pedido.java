package model;

import java.io.Serializable;

public class Pedido implements Serializable {

    private double ancho;
    private double alto;
    private String material;
    private String tipo_impresion;
    private double costo_final;
    private boolean aprobado;

    public Pedido(double ancho, double alto, String material, String tipo_impresion, double costo_final){
        this.ancho = ancho;
        this.alto = alto;
        this.material = material;
        this.tipo_impresion = tipo_impresion;
        this.costo_final = costo_final;
        this.aprobado = false;
    }

    public void aprobarPedido() {
        aprobado = true;
    }
    
    public double getCosto(){
        return costo_final;
    }
}
