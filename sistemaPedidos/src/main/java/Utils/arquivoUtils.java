/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;
import javax.swing.JOptionPane;

/**
 *
 * @author bssil
 */
public class arquivoUtils {
    
    
    
    
    public static Properties lerArquivoDeConfiguracao(){
        Properties props = new Properties();
        try {
        File directory = new File(".");
        File arquivoDeConfiguracao = new File(directory.getCanonicalPath()+"\\config.properties");   
        if(checaSeOArquivoExiste()){
        try{
            FileInputStream in = new FileInputStream(arquivoDeConfiguracao);
            props.load(in);
            in.close();
            }catch(Exception e){
                JOptionPane.showMessageDialog(null, e);
            }
        return props;
        }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        return props;
    }
    
    public static boolean salvarArquivoDeConfiguracao(Properties props){
        try {
        File directory = new File(".");
        File arquivoDeConfiguracao = new File(directory.getCanonicalPath()+"\\config.properties");  
        FileOutputStream outputStrem = new FileOutputStream(arquivoDeConfiguracao);
        props.store(outputStrem, "ARQUIVO DE CONFIGURAÇÃO GESTÃO DE COMPRAS");    
        outputStrem.close();
        return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        return false;
    }
    
    public static boolean checaSeOArquivoExiste(){
        try {
         File directory = new File(".");
        File arquivoDeConfiguracao = new File(directory.getCanonicalPath()+"\\config.properties");     
        if(arquivoDeConfiguracao.exists() && !arquivoDeConfiguracao.isDirectory()){
           return true;
        }
        } catch (Exception e) {
           JOptionPane.showMessageDialog(null, e);
        }
        return false;
    }
    
    
    
    
    
    
    
    
}
