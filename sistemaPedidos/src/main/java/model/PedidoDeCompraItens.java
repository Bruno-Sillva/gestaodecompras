/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author bssil
 */
public class PedidoDeCompraItens {
    private int numped;
    private List<Produto> produtos;

    public PedidoDeCompraItens(int numped, List<Produto> produtos) {
        this.numped = numped;
        this.produtos = produtos;
    }
    
    public PedidoDeCompraItens(){
        
    }

    public int getNumped() {
        return numped;
    }

    public void setNumped(int numped) {
        this.numped = numped;
    }

    public List<Produto> getProdutos() {
        return produtos;
    }

    public void setProdutos(List<Produto> produtos) {
        this.produtos = produtos;
    }
    
}
