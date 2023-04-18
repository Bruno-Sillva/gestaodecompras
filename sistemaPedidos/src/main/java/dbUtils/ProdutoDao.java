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
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
import model.DepartamentoProduto;
import model.ListaDeProdutos;
import model.Produto;
import model.UnidadeProduto;
import model.Usuario;

/**
 *
 * @author bssil
 */
public class ProdutoDao {
    DbUtils dbUtils = new DbUtils();
    
    
    
    
    
    public boolean cadastrarUnidadeProduto(String descricao){
        String insert = "insert into unidade_produto (descricao) values (?)";
        System.out.println("CADASTRANDO UNIDADE DO PRODUTO");
        Connection conexao = dbUtils.conectarAoDB();
         try {
             PreparedStatement statement = conexao.prepareStatement(insert); 
              statement.setString(1, descricao);
             int rows = statement.executeUpdate();
              if(rows>0){
                  System.out.println(rows);
                  return true;
              }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        return false;
    }
    
    public List buscarUnidadesProdutos(){
         String select = "select * from unidade_produto";
        Connection conexao = dbUtils.conectarAoDB();
         List<UnidadeProduto> listaUnidadesProduto = new ArrayList<>();
        try {
           Statement stmt = conexao.createStatement();
             ResultSet rs = stmt.executeQuery(select);
             while(rs.next()){
                 UnidadeProduto unidadeProduto = new UnidadeProduto();
                 unidadeProduto.setId(rs.getInt("unidade_id"));
                 System.out.println(rs.getString("descricao"));
                 unidadeProduto.setDescricao(rs.getString("descricao"));
                 listaUnidadesProduto.add(unidadeProduto);
             }
             return listaUnidadesProduto;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        return listaUnidadesProduto;
    }

    public String cadastrarProduto(String nome, int unidade_id, String obs, int departamento_id, int id_usuario_criacao, String cfop, String gtin){
       String insert = "insert into produtos (nome, obs, unidade_id, departamento_id,id_usuario_criacao, cfop, gtin) values (?,?,?,?,?,?,?)";
        try {
            Connection conexao = dbUtils.conectarAoDB();
            PreparedStatement statement = conexao.prepareStatement(insert,Statement.RETURN_GENERATED_KEYS); 
            statement.setString(1, nome);       
            statement.setString(2, obs);
            statement.setInt(3, unidade_id);
             statement.setInt(4, departamento_id);
             statement.setInt(5, id_usuario_criacao);
             statement.setString(6, cfop);
             statement.setString(7, gtin);
            int rows = statement.executeUpdate();
            ResultSet result = statement.getGeneratedKeys();
            if(result.next() && rows >0){
            return Integer.toOctalString(result.getInt(1));
            }     
        } catch (Exception e) {
            System.out.println(e);
        }
        
        return null;
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
    
    public List buscarProduto(){
        String select = "select  \n" +
"produto_id, \n" +
"nome, \n" +
"coalesce(obs,'')obs, \n" +
"coalesce(unidade_id,0)unidade_id, \n" +
"coalesce(departamento_id,0)departamento_id, \n" +
"coalesce(cfop,'')cfop, \n" +
"coalesce(gtin,'')gtin, \n" +
"coalesce(to_char(data_inativacao,'dd/mm/yyyy'),'')data_inativacao  \n" +
"from produtos p ";
        Connection conexao = dbUtils.conectarAoDB();
        List<Produto> lista = new ArrayList<>();
        try {
            Statement stmt = conexao.createStatement();
             ResultSet rs = stmt.executeQuery(select);
             while(rs.next()){
              Produto produto = new Produto();
              produto.setId(rs.getInt("produto_id"));
              produto.setName(rs.getString("nome"));
          produto.setObs(rs.getString("obs"));
              produto.setUnidadeId(rs.getInt("unidade_id"));
              produto.setDepartamentoId(rs.getInt("departamento_id"));
           produto.setCfop(rs.getString("cfop"));
            produto.setGtin(rs.getString("gtin"));
             produto.setDataInativacao(rs.getString("data_inativacao"));
              lista.add(produto);
             }
             return lista;
        } catch (Exception e) {
             JOptionPane.showMessageDialog(null, "ERRO SQL: " + e);
        }
        return lista;
    }
    
    public List buscarProdutos(){
        String select = "select \n" +
"p.produto_id ,\n" +
"p.nome,\n" +
"p.obs,\n" +
"coalesce(dp.descricao,'SEM DEPARTAMENTO') departamento,\n" +
"case \n" +
"	when p.data_inativacao is not null then 'N'\n" +
"	else 'S' \n" +
"end as inativo \n" +
"from produtos p \n" +
"left join departamento_produtos dp on dp.departamento_id  = p.departamento_id ";
        Connection conexao = dbUtils.conectarAoDB();
        List<ListaDeProdutos> lista = new ArrayList<>();
        try {
           Statement stmt = conexao.createStatement();
             ResultSet rs = stmt.executeQuery(select);
             while(rs.next()){
                 ListaDeProdutos listap =  new ListaDeProdutos();
                 listap.setProduto_id(rs.getInt("produto_id"));
                 listap.setNome(rs.getString("nome"));
                 listap.setObs(rs.getString("obs"));
                 listap.setDepartamento(rs.getString("departamento"));
                 listap.setInativo(rs.getString("inativo"));
                 lista.add(listap);
             }
             return lista;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        return lista;
    }

    public boolean atualizarProduto(Produto produto, Usuario usuario){
        String update = "update produtos p \n" +
"set nome = ?, \n" +
"obs =?, \n" +
"departamento_id =?, \n" +
"unidade_id =?, \n" +
"data_inativacao = ?, \n" +
"id_usuario_alteracao =?, \n" +
"gtin =?, \n" +
"cfop =? \n" +
"where produto_id =?";
       SimpleDateFormat formatter = new SimpleDateFormat("yyyy/M/dd HH:mm:ss");  
    Date data = new Date();  
        try {
       Connection conexao = dbUtils.conectarAoDB();
       PreparedStatement statement = conexao.prepareStatement(update,Statement.RETURN_GENERATED_KEYS);  
       statement.setString(1, produto.getName());
        statement.setString(2, produto.getObs());
         statement.setInt(3, produto.getDepartamentoId());
         statement.setInt(4, produto.getUnidadeId());
         if(produto.isAtivo()){
            statement.setString(5, null);
         }
         else{
          statement.setString(5, formatter.format(data));   
         }
         statement.setInt(6,usuario.getId());
         statement.setString(7, produto.getGtin());
            System.out.println("dbUtils.ProdutoDao.atualizarProduto()" + produto.getCfop());
         statement.setString(8, produto.getCfop());
         statement.setInt(9, produto.getId());
         int rows = statement.executeUpdate();
         if(rows>0){
             return true;
         }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        return false;
    }
    
    public List buscarListaDeProdutos() throws SQLException{
        Connection con = null;
         PreparedStatement statement = null;
         ResultSet rs = null;
        String query = "Select * from produtos";
        
        List<Produto> listaDeProdutos = new ArrayList<>();
        try {
            con = dbUtils.conectarAoDB();
             statement = con.prepareStatement(query);
             rs = statement.executeQuery();
             while(rs.next()) {
              Produto produto = new Produto();
              produto.setId(rs.getInt("produto_id"));
              produto.setName(rs.getString("NOME"));
              listaDeProdutos.add(produto);
            }
             return listaDeProdutos;
        } catch (SQLException e) {
            System.out.println(e);
        }finally{
           dbUtils.fechaConexao(con, rs, statement);
        }
        return listaDeProdutos;
    }
    
}
