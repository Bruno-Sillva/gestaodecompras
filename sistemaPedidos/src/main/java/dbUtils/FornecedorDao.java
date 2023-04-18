/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dbUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
import model.Fornecedor;
import model.Usuario;

/**
 *
 * @author bssil
 */
public class FornecedorDao {
   DbUtils dbUtils = new DbUtils();
   
   
  public String cadastrarFornecedor(Fornecedor fornecedor,Usuario usuarioLogado){
      Connection con = null;
      ResultSet rs = null;
      PreparedStatement statement = null;
      String fornecedorCadastrado = null;
      DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");  
   LocalDateTime now = LocalDateTime.now();  
   Date hoje = new Date();

      StringBuilder insert = new StringBuilder();
      insert.append("insert into fornecedor(fantasia,logradouro,bairro,uf,cep,fone,cnpj,obs,tipo_pessoa,inscricao_estadual,nome,numero,id_usuario_insert,status,data_insercao) ");
      insert.append(" values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
      try {
        con = dbUtils.conectarAoDB();
        statement = con.prepareStatement(insert.toString(),Statement.RETURN_GENERATED_KEYS); 
        statement.setString(1, fornecedor.getFantasia());
        statement.setString(2, fornecedor.getLogradouro());
        statement.setString(3, fornecedor.getBairro());
        statement.setString(4, fornecedor.getUf());
        statement.setString(5, fornecedor.getCep());
        statement.setString(6, fornecedor.getFone());
        statement.setString(7, fornecedor.getCnpj());
        statement.setString(8, fornecedor.getObs());
        statement.setString(9, fornecedor.getTipoPessoa());
        statement.setString(10, fornecedor.getInscricaoEstadual());
        statement.setString(11, fornecedor.getNome());
        statement.setString(12, fornecedor.getNumero());
         statement.setInt(13, usuarioLogado.getId());
          statement.setString(14, "A");
          statement.setDate(15,  new java.sql.Date(hoje.getTime()));
        int rows = statement.executeUpdate();
        if(rows>0){
        ResultSet result = statement.getGeneratedKeys();
        if(result.next()){
           return Integer.toString(result.getInt(1));
        }
        }
      } catch (Exception e) {
         return e.toString();
      }finally{
          dbUtils.fechaConexao(con, rs, statement);
      }
      return fornecedorCadastrado;
  }

  public List fornecedores(){
      Connection con = null;
      ResultSet rs = null;
      PreparedStatement p = null;
      String select = "select \n" +
"fornecedor_id,\n" +
"nome,\n" +
"fantasia,\n" +
"logradouro,\n" +
"bairro,\n" +
"uf,\n" +
"cep,\n" +
"cnpj,\n" +
"obs,\n" +
"case \n" +
"	when tipo_pessoa = 'F' then 'FISICA'\n" +
"	when tipo_pessoa = 'J' then 'JURIDICA'\n" +
"	else tipo_pessoa\n" +
"end tipo_pessoa,\n" +
"coalesce(status,'I')status \n"+
"from fornecedor";
      List<Fornecedor> fornecedores = new ArrayList<>();
       
      try {
            con = dbUtils.conectarAoDB();
            p = con.prepareStatement(select);
            rs = p.executeQuery();
            while(rs.next()){
            Fornecedor fornec = new Fornecedor();
            fornec.setNome(rs.getString("nome"));
            fornec.setId(rs.getInt("fornecedor_id"));
            fornec.setFantasia(rs.getString("fantasia"));
            fornec.setLogradouro(rs.getString("logradouro"));
            fornec.setBairro(rs.getString("bairro"));
            fornec.setUf(rs.getString("UF"));
            fornec.setCep(rs.getString("cep"));
            fornec.setCnpj(rs.getString("cnpj"));
            fornec.setTipoPessoa(rs.getString("tipo_pessoa"));
            fornec.setAtivo(rs.getString("status"));
            fornecedores.add(fornec);
             }
             return fornecedores;
      } catch (Exception e) {
          JOptionPane.showMessageDialog(null, e);
      }finally{
          dbUtils.fechaConexao(con, rs, p);
      }
      return fornecedores;
  }
  
  public String editarFornecedor(Fornecedor fornecedor, Usuario usuarioLogado){
      Connection con = null;
      ResultSet rs = null;
      PreparedStatement statement = null;
      String retorno = null;
      String update = "update fornecedor \n" +
"set fantasia = ?,\n" +
"logradouro =?,\n" +
"bairro =?,\n" +
"uf=?,\n" +
"cep=?,\n" +
"fone=?,\n" +
"cnpj=?,\n" +
"tipo_pessoa=?,\n" +
"nome=?,\n" +
"status=?,\n" +
"id_usuario_update =?,\n" +
"data_update =?\n" +
"where fornecedor_id = ?";
      SimpleDateFormat formatter = new SimpleDateFormat("yyyy/M/dd HH:mm:ss");  
    Date data = new Date();  
      try {  
        con = dbUtils.conectarAoDB();
        statement = con.prepareStatement(update,Statement.RETURN_GENERATED_KEYS);  
        statement.setString(1, fornecedor.getFantasia());
        statement.setString(2, fornecedor.getLogradouro());
        statement.setString(3, fornecedor.getBairro());
        statement.setString(4, fornecedor.getUf());
        statement.setString(5, fornecedor.getCep());
        statement.setString(6, fornecedor.getFone());
        statement.setString(7, fornecedor.getCnpj());
        statement.setString(8, fornecedor.getTipoPessoa());
        statement.setString(9, fornecedor.getNome());
        statement.setString(10, fornecedor.getAtivo());
        statement.setInt(11, usuarioLogado.getId());
        statement.setString(12, formatter.format(data));
           statement.setInt(13, fornecedor.getId());
         int rows = statement.executeUpdate();
         if(rows>0){
             return "Fornecedor atualizado";
         }
      } catch (Exception e) {
      return e.toString();
      }finally{
          dbUtils.fechaConexao(con, rs, statement);
      }
      return retorno;
  }
  
  public Fornecedor buscarFornecedor(int fornecedorId){
     Connection con = null;
     ResultSet rs = null;
     PreparedStatement statement = null;
      String select = "select \n" +
"fornecedor_id,\n" +
"nome,\n" +
"fantasia,\n" +
"logradouro,\n" +
"bairro,\n" +
"uf,\n" +
"cep,\n" +
"cnpj,\n" +
"obs,\n" +
"case \n" +
"	when tipo_pessoa = 'F' then 'FISICA'\n" +
"	when tipo_pessoa = 'J' then 'JURIDICA'\n" +
"	else tipo_pessoa\n" +
"end tipo_pessoa,\n" +
"coalesce(status,'I')status \n"+
"from fornecedor where fornecedor_id = ?";
      Fornecedor fornec = new Fornecedor();
      con = dbUtils.conectarAoDB();
      try {
           statement = con.prepareStatement(select);
           statement.setInt(1, fornecedorId);
            rs = statement.executeQuery();
             
             while(rs.next()){
            fornec.setNome(rs.getString("nome"));
            fornec.setId(rs.getInt("fornecedor_id"));
            fornec.setFantasia(rs.getString("fantasia"));
            fornec.setLogradouro(rs.getString("logradouro"));
            fornec.setBairro(rs.getString("bairro"));
            fornec.setUf(rs.getString("UF"));
            fornec.setCep(rs.getString("cep"));
            fornec.setCnpj(rs.getString("cnpj"));
            fornec.setTipoPessoa(rs.getString("tipo_pessoa"));
            fornec.setAtivo(rs.getString("status"));
             }
             return fornec;
      } catch (Exception e) {
          e.printStackTrace();
      }finally{
        dbUtils.fechaConexao(con, rs, statement);    
          }
      return fornec;
  }
  
}
