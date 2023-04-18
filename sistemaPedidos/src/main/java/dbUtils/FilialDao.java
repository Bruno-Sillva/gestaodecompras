/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dbUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import model.Filial;

/**
 *
 * @author bssil
 */
public class FilialDao {
     DbUtils dbUtils = new DbUtils();
     
     
     public boolean cadastrarFilial(Filial filial){
         Connection con= null;
         PreparedStatement statement = null;
         StringBuilder insert =  new StringBuilder();
         insert.append("insert into filial (nome, fantasia,cnpj,logradouro,cep,uf,fone,municipio,email,numero,bairro) ");
         insert.append("values(?,?,?,?,?,?,?,?,?,?,?) ");
         try {
          con = dbUtils.conectarAoDB();
          statement = con.prepareStatement(insert.toString(),Statement.RETURN_GENERATED_KEYS); 
          statement.setString(1, filial.getNome());
           statement.setString(2, filial.getFantasia());
            statement.setString(3, filial.getCnpj());
             statement.setString(4, filial.getLogradouro());
              statement.setString(5, filial.getCep());
               statement.setString(6, filial.getUf());
                statement.setString(7, filial.getFone());
                 statement.setString(8, filial.getMunicipio());
                  statement.setString(9, filial.getEmail());
                  statement.setString(10, filial.getNumero());
                  statement.setString(11, filial.getBairro());
                   int rows = statement.executeUpdate();
                   if(rows>0){
                       return true;
                   }
         } catch (Exception e) {
             JOptionPane.showMessageDialog(null, e);
         }finally{
             dbUtils.fechaConexao(con, null, statement);
         }
         
         return false;
     }
     
     
     public List buscarFilials(){
         Connection con = null;
         ResultSet rs = null;
         PreparedStatement p = null;
         List<Filial> listaDeFiliais = new ArrayList<>();
         String select = "select * from filial";
         con = dbUtils.conectarAoDB();
         try {
         p = con.prepareStatement(select);
         rs = p.executeQuery();
         while(rs.next()){
         Filial filial = new Filial();
         filial.setId(rs.getInt("filial_id"));
         filial.setBairro(rs.getString("bairro"));
         filial.setCep(rs.getString("cep"));
         filial.setCnpj(rs.getString("cnpj"));
         filial.setEmail(rs.getString("email"));
         filial.setFantasia(rs.getString("fantasia"));
         filial.setFone(rs.getString("fone"));
         filial.setLogradouro(rs.getString("logradouro"));
         filial.setMunicipio(rs.getString("municipio"));
         filial.setNome(rs.getString("nome"));
         filial.setNumero(rs.getString("numero"));
         filial.setUf(rs.getString("uf"));
         listaDeFiliais.add(filial);
         }
         return listaDeFiliais;
         }
         catch(Exception e){
            JOptionPane.showMessageDialog(null, "ERRO AO PESQUISAR A FILIAL: " + e); 
         }finally{
             dbUtils.fechaConexao(con, rs, p);
         }
          return listaDeFiliais;
     }
     
}
