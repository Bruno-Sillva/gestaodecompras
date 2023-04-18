/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dbUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import model.DepartamentoProduto;

/**
 *
 * @author bssil
 */
public class DepartamentoDao {
     DbUtils dbUtils = new DbUtils();
     
     
     
     public boolean inserirNovoDepartamento(String descricao){
            String insert = "insert into departamento_produtos (descricao) values (?)";
        try {
            Connection conexao = dbUtils.conectarAoDB();
            PreparedStatement statement = conexao.prepareStatement(insert,Statement.RETURN_GENERATED_KEYS); 
            statement.setString(1, descricao);       
            int rows = statement.executeUpdate();
            ResultSet result = statement.getGeneratedKeys();
            if(rows >0){
            return true;
            }     
        } catch (Exception e) {
            System.out.println(e);
        }
         return false;
     }
      
     
      public List buscarDepartamentosProduto(){
        String select = "select * from departamento_produtos";
        Connection conexao = dbUtils.conectarAoDB();
         List<DepartamentoProduto> listaDepartamentosProduto = new ArrayList<>();
         try {
           Statement stmt = conexao.createStatement();
             ResultSet rs = stmt.executeQuery(select);
             while(rs.next()){
                 DepartamentoProduto dp = new DepartamentoProduto();
                 dp.setId(rs.getInt("departamento_id"));
                 dp.setDescricao(rs.getString("descricao"));
                 listaDepartamentosProduto.add(dp);
             }
             return listaDepartamentosProduto;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
         return listaDepartamentosProduto;
}
 
}
