/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package Enums;

/**
 *
 * @author bssil
 */
public enum TiposDePessoaEnum {
    JURIDICA("J"),
    FISICA("F");
    
     public String getValor() {
        return valor;
    }
     
    
    
    private final String valor;

    private TiposDePessoaEnum(String valor) {
        this.valor = valor;
    }
    
    @Override
     public String toString(){
    return this.name();
    }
     
     public static String valor(String valorProcurado){
         for(TiposDePessoaEnum tipo : TiposDePessoaEnum.values()){
            if(tipo.name().equalsIgnoreCase(valorProcurado)){
                return tipo.getValor();
                
            }
            
        }
          throw new IllegalArgumentException();
     }
}
