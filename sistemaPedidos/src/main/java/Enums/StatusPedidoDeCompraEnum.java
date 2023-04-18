/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package Enums;

/**
 *
 * @author bssil
 */
public enum StatusPedidoDeCompraEnum {
    ABERTO("A"),
    FINALIZADO("F"),
    CANCELADO("C");
    
      public String getValor() {
        return valor;
    }
      
      
    private final String valor;

    private StatusPedidoDeCompraEnum(String valor) {
        this.valor = valor;
    }
    
    
}
