/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package Enums;

/**
 *
 * @author bssil
 */
public enum ConfiguracaoBancoDeDadosEnum {
    JDBC_PASSWORD("jdbc.password"),
    JDBC_USERNAME("jdbc.username"),
    JDBC_URL("jdbc.url"),
    JDBC_DRIVER("jdbc.driver");

    public static ConfiguracaoBancoDeDadosEnum getJDBC_PASSWORD() {
        return JDBC_PASSWORD;
    }

    public static ConfiguracaoBancoDeDadosEnum getJDBC_USERNAME() {
        return JDBC_USERNAME;
    }

    public static ConfiguracaoBancoDeDadosEnum getJDBC_URL() {
        return JDBC_URL;
    }

    public static ConfiguracaoBancoDeDadosEnum getJDBC_DRIVER() {
        return JDBC_DRIVER;
    }

    public String getValor() {
        return valor;
    }
    
    private final String valor;

    private ConfiguracaoBancoDeDadosEnum(String valor) {
        this.valor = valor;
    }
}
