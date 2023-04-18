/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package Enums;

/**
 *
 * @author bssil
 */
public enum ColunaTabela {
    
        PRODUTO_ID(0),
        PRODUTO(1),
        QUANTIDADE(2),
        VALOR(3),
        OBS(4);
 
        private final int valor;
 
        private ColunaTabela(int valor) {
            this.valor = valor;
        }
 
        public int getValor() {
            return this.valor;
        }
    
}
