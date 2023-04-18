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
import model.Usuario;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 *
 * @author bssil
 */
public class UsuarioDao {
    DbUtils dbUtils = new DbUtils();
    
    
    public boolean cadastrarUsuario(Usuario usuario){
      StringBuilder insert =  new StringBuilder();
         insert.append("insert into usuario (nome, login,senha,autorizador_pedido,email,fone) ");
         insert.append("values(?,?,?,?,?,?) ");
         String senha = usuario.getSenha();
         BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
      String senhaEncriptada = bCryptPasswordEncoder.encode(senha);
         try {
          Connection conexao = dbUtils.conectarAoDB();
          PreparedStatement statement = conexao.prepareStatement(insert.toString(),Statement.RETURN_GENERATED_KEYS); 
          statement.setString(1, usuario.getNome());
          statement.setString(2, usuario.getLogin());
          statement.setString(3, senhaEncriptada);
          statement.setString(4, usuario.getAutorizaPedidos());
          statement.setString(5, usuario.getEmail());
          statement.setString(6, usuario.getFone());
          int rows = statement.executeUpdate();
          if(rows>0){
            return true;  
          }else{
              return false;
          }
                  
        } catch (SQLException e) {
             JOptionPane.showMessageDialog(null, "ERRO AO CADASTRAR USUÁRIO " + e);
        }
         catch(Exception e){
         JOptionPane.showMessageDialog(null, "ERRO AO CADASTRAR USUÁRIO " + e);    
         }
        return false;
    }
    
    public List buscarUsuarios(String usuarioAPesquisar){
      List<Usuario> listaDeUsuario = new ArrayList<>();
      if(usuarioAPesquisar.isEmpty()){
      String select = "select \n" +
"u.usuario_id,\n" +
"u.login,\n" +
"u.nome,\n" +
"coalesce(u.autorizador_pedido, 'N') autorizador_pedido,\n" +
"u.email,\n" +
"u.fone\n" +
"from usuario u ";
      Connection conexao = dbUtils.conectarAoDB();
          try {
          Statement stmt = conexao.createStatement();
      ResultSet rs = stmt.executeQuery(select);  
      while(rs.next()){
        Usuario usuario = new Usuario();
        usuario.setId(rs.getInt("usuario_id"));
        usuario.setLogin(rs.getString("login"));
        usuario.setNome(rs.getString("nome"));
        usuario.setAutorizaPedidos(rs.getString("autorizador_pedido"));
        usuario.setEmail(rs.getString("email"));
        usuario.setFone(rs.getString("fone"));
        listaDeUsuario.add(usuario);
      }
      return listaDeUsuario;
          } catch (Exception e) {
          e.printStackTrace();
          }finally{
              try {
                conexao.close();   
              } catch (Exception e) {
                  e.printStackTrace();
              }
            
          }
       
      }
      
      
      return listaDeUsuario;
    }

    public Usuario buscaUsuario(int userId){
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement p = null;
        String select = "select \n" +
"u.usuario_id,\n" +
"u.login,\n" +
"u.nome,\n" +
"coalesce(u.autorizador_pedido, 'N') autorizador_pedido,\n" +
"u.email,\n" +
"u.fone\n" +
"from usuario u ";
        con = dbUtils.conectarAoDB();
        Usuario usuario = new Usuario();
          try {
           p = con.prepareStatement(select);
       rs = p.executeQuery();  
      while(rs.next()){
        usuario.setId(rs.getInt("usuario_id"));
        usuario.setLogin(rs.getString("login"));
        usuario.setNome(rs.getString("nome"));
        usuario.setAutorizaPedidos(rs.getString("autorizador_pedido"));
        usuario.setEmail(rs.getString("email"));
        usuario.setFone(rs.getString("fone"));

      }
      return usuario;
          } catch (Exception e) {
          JOptionPane.showMessageDialog(null, e);
          }finally{
              dbUtils.fechaConexao(con, rs, p);
          }
          return usuario;
    }

    public List usuariosAutorizadores(){
     List<Usuario> listaDeUsuario = new ArrayList<>();
     String select = "select \n" +
"u.usuario_id,\n" +
"u.login,\n" +
"u.nome,\n" +
"coalesce(u.autorizador_pedido, 'N') autorizador_pedido,\n" +
"u.email,\n" +
"u.fone\n" +
"from usuario u "+
 "where autorizador_pedido = 'S'";
       Connection conexao = dbUtils.conectarAoDB();
          try {
          Statement stmt = conexao.createStatement();
      ResultSet rs = stmt.executeQuery(select);  
      while(rs.next()){
        Usuario usuario = new Usuario();
        usuario.setId(rs.getInt("usuario_id"));
        usuario.setLogin(rs.getString("login"));
        usuario.setNome(rs.getString("nome"));
        usuario.setAutorizaPedidos(rs.getString("autorizador_pedido"));
        usuario.setEmail(rs.getString("email"));
        usuario.setFone(rs.getString("fone"));
        listaDeUsuario.add(usuario);
      }
      return listaDeUsuario;
          } catch (Exception e) {
          JOptionPane.showMessageDialog(null, e);
          }
     return listaDeUsuario;
    }
    
    
    
}
