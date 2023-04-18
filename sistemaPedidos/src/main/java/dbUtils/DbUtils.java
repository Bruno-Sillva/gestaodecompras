/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dbUtils;

import Enums.ConfiguracaoBancoDeDadosEnum;
import Utils.arquivoUtils;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import model.PedidoDeCompra;
import model.Produto;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import model.PedidoDeCompraItens;
import model.Usuario;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 *
 * @author bssil
 */
public class DbUtils {
    
    private Connection conexao;
    String url = "jdbc:mariadb://localhost:3306/gestaocompras";
    static final String JDBC_DRIVER = "org.mariadb.jdbc.Driver";
    /*
    String DB_URL = "jdbc:mariadb://localhost/gestaocompras";
    String USER = "root";
    String PASS = "root@123";*/
    
private static HikariConfig config = new HikariConfig();
private static HikariDataSource ds;
  
    
    
    
    String jdbc_url;
    String jdbc_user;
    String jdbc_pass;
    String jdbc_driver;
    
    public DbUtils(){       
    Properties props = arquivoUtils.lerArquivoDeConfiguracao();
    Properties p = new Properties();
   // p.setProperty("ApplicationName", "GESTAODECOMPRAS/PEDIDODECOMPRA");
     jdbc_url = props.getProperty(ConfiguracaoBancoDeDadosEnum.JDBC_URL.getValor());
     jdbc_user = props.getProperty(ConfiguracaoBancoDeDadosEnum.JDBC_USERNAME.getValor());
     jdbc_pass = props.getProperty(ConfiguracaoBancoDeDadosEnum.JDBC_PASSWORD.getValor());
     jdbc_driver = props.getProperty(ConfiguracaoBancoDeDadosEnum.JDBC_DRIVER.getValor());
     props.setProperty("ApplicationName", "GESTAODECOMPRAS");
    /* config.setJdbcUrl(jdbc_url);
     config.setUsername(jdbc_user);
     config.setPassword(jdbc_pass);
     config.setDataSourceProperties(p);
       config.addDataSourceProperty( "cachePrepStmts" , "true" );
        config.addDataSourceProperty( "prepStmtCacheSize" , "250" );
        config.addDataSourceProperty( "prepStmtCacheSqlLimit" , "2048" );
        ds = new HikariDataSource( config );*/
    }
    
    public DbUtils(Properties props){
     jdbc_url = props.getProperty(ConfiguracaoBancoDeDadosEnum.JDBC_URL.getValor());
     jdbc_user = props.getProperty(ConfiguracaoBancoDeDadosEnum.JDBC_USERNAME.getValor());
     jdbc_pass = props.getProperty(ConfiguracaoBancoDeDadosEnum.JDBC_PASSWORD.getValor());
     jdbc_driver = props.getProperty(ConfiguracaoBancoDeDadosEnum.JDBC_DRIVER.getValor());
    }

    public Connection getConnection() {
        try {        
            return ds.getConnection();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
         return null;
    }  
    
    public void fechaConexao(Connection con, ResultSet rs, PreparedStatement p){
        try {
            if(con != null){
                con.close();
            }
            if(rs != null){
                rs.close();
            }
            if(p != null){
                p.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
 
    public static boolean testeDeConexao(Properties props){
        DbUtils db = new DbUtils(props);
        return db.conectar();     
    }
    
    
    public  boolean conectar(){
        
        try {
            System.out.println("DRIVER DE CONEXAO" + jdbc_driver);
            Class.forName(jdbc_driver);
        } catch (Exception e) {
            
            System.out.println("ERRO: "+e);
        }
        
        try {      
            this.conexao = DriverManager.getConnection(jdbc_url, jdbc_user, jdbc_pass);
            if(conexao !=null){
               return true;
            }
            
        } catch (SQLException e) {
            System.out.println(e);
        }
        return false;
    }
    
   public Connection conectarAoDB(){
       Connection con = null;
        try {      
            try {
              Class.forName(jdbc_driver);  
            } catch (Exception e) {
                System.out.println(e);
            }
            con = DriverManager.getConnection(jdbc_url, jdbc_user, jdbc_pass);
            return con;
            
        } catch (SQLException e) {
            System.out.println(e);
        }
        return con;
   }
    
    
    
    public ArrayList getProdutos() throws SQLException{
        Connection con = null;
        String query = "Select produto_id||' - '||nome as nome from produto";
        ArrayList produtos = new ArrayList();
        try {
            con = this.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query);
             while(rs.next()) {
              produtos.add(rs.getString("NOME"));
            }
             return produtos;
        } catch (SQLException e) {
            System.out.println(e);
        }finally{
            con.close();
        }
      return produtos;
    }
    
    public List buscarListaDeProdutos() throws SQLException{
        Connection con = null;
        String query = "Select * from produtos";
        
        List<Produto> listaDeProdutos = new ArrayList<>();
        try {
            con = this.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query);
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
            con.close();
        }
        return listaDeProdutos;
    }
    
    public void adicionarItemAoPedido(int produto_id, int numPed, BigDecimal valor, int quantidade) throws SQLException{
        int rows = 0;
        Connection con = null;
        if(produtoJaExisteNoPedidoComOMesmoValor(produto_id, numPed,valor)){
            String updateQuantidadeDoProdutoNoPedido = "update pedido_comprai set qt = (qt + ?) where numped=? and produto_id = ? and cast(valor as decimal(10,2)) = cast(? as decimal(10,2))";
            try {
                con = this.getConnection();
                PreparedStatement statement = con.prepareStatement(updateQuantidadeDoProdutoNoPedido); ;
                statement.setInt(1, quantidade);
                statement.setInt(2, numPed);
                 statement.setInt(3, produto_id);
                 statement.setBigDecimal(4, valor);
                rows = statement.executeUpdate();
              //  atualizaCabecalhoDoPedido(numPed);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            } 
        } else{  
        String insert = "insert into pedido_comprai (numped, produto_id,valor, qt) VALUES(?,?,?,?)" ;
            try {
              con = this.getConnection();
             PreparedStatement statement = con.prepareStatement(insert); 
              statement.setInt(1, numPed);
              statement.setInt(2, produto_id);
              statement.setBigDecimal(3, valor);
                statement.setDouble(4, quantidade);
              rows = statement.executeUpdate();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }finally{
                con.close();
            }
        }
          if(rows >0){
            atualizaCabecalhoDoPedido(numPed);
        }
    }
   
    public void atualizarObsItem( int produto_id, int numPed, String obs){
        String update = " update pedido_comprai set obs = ? where numped = ? and produto_id = ?";
        int rows = 0;
            try {
                PreparedStatement statement = conexao.prepareStatement(update); 
                statement.setString(1, obs);
                statement.setInt(2, numPed);
                statement.setInt(3, produto_id);
                rows = statement.executeUpdate();
                
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
    }
    
    private void atualizaCabecalhoDoPedido(int numPed) throws SQLException{
    //   int itensPedido = this.quantidadeDeItensdoPedido(numPed);
       String update = "update pedido_compra \n" +
"set valor = ?, qt_itens =?\n" +
"where numped =?";
       Connection con = null;
        try {
           con = this.conectarAoDB();
            PreparedStatement p = con.prepareStatement(update);
            p.setBigDecimal(1, this.valorDoPedido(numPed));
            p.setInt(2, this.quantidadeDeItensdoPedido(numPed));
            p.setInt(3, numPed);
            p.execute();
        } catch (Exception e) {
           e.printStackTrace();
        }finally{
            con.close();
        }
       
    }
    
    private BigDecimal valorDoPedido(int numPed) throws SQLException{
      BigDecimal valorTotal = null;
      String select = "select \n" +
"coalesce(round(sum((pc.valor*pc.qt)),2),0)valorTotal\n" +
"from pedido_comprai pc \n" +
"where numped = ?";
      Connection con = null;
        try {
            con = this.conectarAoDB();
            PreparedStatement p = con.prepareStatement(select);
            p.setInt(1, numPed);
            ResultSet rs = p.executeQuery();
            while(rs.next()){
                valorTotal = rs.getBigDecimal("valorTotal");
                System.out.println(valorTotal);
            }
            return valorTotal;
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }finally{
            con.close();
        }
      return valorTotal;
        
    }
    
    
    private int quantidadeDeItensdoPedido(int numPed) throws SQLException{
        String sql = "select count(*)qt_itens from pedido_comprai where numped = ?";
        int qtItens = 0;
         Connection con = this.conectarAoDB();
        try {
          
         PreparedStatement statement = con.prepareStatement(sql); 
         statement.setInt(1, numPed);
         ResultSet rs = statement.executeQuery();
          while(rs.next()){
              qtItens = rs.getInt("qt_itens");
          }
            System.out.println("Qt_itens do pedido: " + qtItens);
          return qtItens;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }finally{
            con.close();
        }
        return 0;
    }
    
    private boolean produtoJaExisteNoPedidoComOMesmoValor(int produto_id, int numPed, BigDecimal valor) throws SQLException{
        String sql = "select count(*) qtItem from pedido_comprai where numped = ? and produto_id = ? and cast(valor as decimal(10,2)) = cast(? as decimal(10,2))";
        int qtProdutoNoPedido =0;
        System.out.println("valor a ser comparado: " + valor);
        Connection con = null;
        try {
            con = this.getConnection();
           PreparedStatement statement = con.prepareStatement(sql);  
           statement.setInt(1, numPed);
           statement.setInt(2, produto_id);
            System.out.println(valor.toString());
           statement.setBigDecimal(3, valor);
           ResultSet rs = statement.executeQuery();
           while(rs.next()){
               qtProdutoNoPedido = rs.getInt("qtItem");
           }
            return qtProdutoNoPedido >0;
              
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }finally{
            con.close();
        }
        return true;
    }
    

    
    public void atualizaQtItemDoPedido(int numPed, double quantidade, int produto_id) throws SQLException{
       String update = "update pedido_comprai set qt = ? where numped = ? and produto_id = ?";
       Connection con = null;
        System.out.println("PEDIDO: " + numPed + " QT " +quantidade+" PRODUTO " +produto_id);
        try {
            con = this.conectarAoDB();
         PreparedStatement statement = con.prepareStatement(update); 
        statement.setInt(1, (int)quantidade);  
        statement.setInt(2, numPed);  
         statement.setInt(3, produto_id);  
       int row = statement.executeUpdate();
            System.out.println("Linhas atualizadas: " + row);
            if(row>0){
                 atualizaCabecalhoDoPedido(numPed);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            con.close();
        }
    }
    
    
    public List produtosDoPedido(int numPed) throws SQLException{
        Connection con = null;
        String sql = "SELECT\n" +
"pc.produto_id,\n" +
"COALESCE(pc.qt,0) qt,\n" +
"pc.valor,\n" +
"p.nome nome,\n" +
"pc.obs,\n" +
"round(pc.valor*pc.qt,2)vlTotal\n" +
"from pedido_comprai pc\n" +
"left join produtos p on p.produto_id = pc.produto_id  where numped = " + numPed;
       List<Produto> listaDeProdutos = new ArrayList<>();
        try {
            con = this.getConnection();
            Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql);
             while(rs.next()) {
              Produto produto = new Produto();
              produto.setId(rs.getInt("produto_id"));
              produto.setName(rs.getString("nome"));
              produto.setValor(new BigDecimal(rs.getString("valor")));
              produto.setQuantidade(Double.parseDouble(rs.getString("qt")));
              produto.setObs(rs.getString("obs"));
              produto.setValorTotal(new BigDecimal(rs.getString("vlTotal")));
              listaDeProdutos.add(produto);
            }
             return listaDeProdutos;
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            con.close();
        }
        return listaDeProdutos;
    }
    
    private void atualizaQtItensEValorPedido(int numPed){
        String update = "update pedido_compra \n" +
"set valor = (select sum(valor)valorTotal from pedido_comprai pc where numped = ?),\n" +
"qt_itens = (select count(*)qt_itens from pedido_comprai where numped = ?)\n" +
"where numped = ?" ;
        try {
         PreparedStatement statement = conexao.prepareStatement(update); 
        statement.setInt(1, numPed);
        statement.setInt(2, numPed);
        statement.setInt(3, numPed);
        statement.executeUpdate();

        } catch (Exception e) {
        JOptionPane.showMessageDialog(null, e);
        }
        
    }
    
    public void removeItemDoPedido(int produto_id, int numPed, BigDecimal valor){
        String delete = "delete FROM pedido_comprai where numped = ? and produto_id = ? and cast(valor as decimal(10,2)) = cast(? as decimal(10,2)) ";
        try {
           PreparedStatement p = conexao.prepareStatement(delete);
           p.setInt(1, numPed);
           p.setInt(2, produto_id);
           p.setBigDecimal(3, valor);
          int rows = p.executeUpdate();
          if(rows>0){
              atualizaCabecalhoDoPedido(numPed);
          }

        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
        
    }
    
    
    public PedidoDeCompra novoPedidoDeCompra() throws SQLException{ 
        Connection con = null;
        SimpleDateFormat formatarData = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<Produto> itens = new PedidoDeCompraItens().getProdutos();
        Date hoje = new Date();
        String dataDeHoje = formatarData.format(hoje);
        System.out.println("Data de hoje " + dataDeHoje);
        String insert = "insert into pedido_compra (obs, data) VALUES(?,?)" ;
        try {
             con = this.getConnection();
              PreparedStatement statement = con.prepareStatement(insert,Statement.RETURN_GENERATED_KEYS);
              statement.setString(1, "");
              statement.setString(2, dataDeHoje);
              int affectedRows = statement.executeUpdate();
                if (affectedRows == 0) {
            throw new SQLException("Erro ao criar novo pedido de compra");
            
        }
             try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                PedidoDeCompra pedido = new PedidoDeCompra(generatedKeys.getInt(1), 1, url, BigDecimal.valueOf(0.0), hoje);
                return pedido;
            }
            else {
                throw new SQLException("Erro ao criar novo pedido de compra");
            }
           
                
                
        } catch (SQLException e) {
            System.out.println(e);
        }
        
    }catch (SQLException e) {
            System.out.println(e);  
}finally{
            con.close();
        }
    return null;
    }

    public boolean login(String login, String senha){
        String sql = "select * from usuario where login = ?";
         this.conectar();
       
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
      String senhaEncriptada = bCryptPasswordEncoder.encode(senha);
      
        System.out.println(senhaEncriptada);
        System.out.println("Usuario: " + login + " Senha: " + senha);
        try {
           PreparedStatement p = this.conexao.prepareStatement(sql); 
           p.setString(1, login);
           ResultSet rs = p.executeQuery();
           if(!rs.next()){
               System.out.println("UAI");
               return false;
        } else {
               String hashAChecar = rs.getString("senha");
               System.out.println("SENHA BD:" + hashAChecar);
               if(BCrypt.checkpw(senha, hashAChecar)){
                 return true;  
               }
               
               
               
            }
        } catch (Exception e) {
            System.out.println(e);
        }finally {
            try {
                this.conexao.close();
            } catch (Exception e) {
                System.out.println(e);
            }
    }
        
        return false;
    }
    
     public Usuario logar(String login, String senha){
          System.out.println("REALIZANDO LOGIN");
        String sql = "select * from usuario where login = ?";
        Usuario usuario = new Usuario();
         this.conectar();
       
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
      String senhaEncriptada = bCryptPasswordEncoder.encode(senha);
      
       
        try {
           PreparedStatement p = this.conexao.prepareStatement(sql); 
           p.setString(1, login);
           ResultSet rs = p.executeQuery();
           if(!rs.next()){
               System.out.println("UAI");
               return null;
        } else {
               String hashAChecar = rs.getString("senha");
               System.out.println("SENHA BD:" + hashAChecar);
               if(BCrypt.checkpw(senha, hashAChecar)){
                 usuario.setLogin(rs.getString("login"));
                 usuario.setNome(rs.getString("nome"));
                 return usuario;
               }              
            }
        } catch (Exception e) {
            System.out.println(e);
        }finally {
            try {
                this.conexao.close();
            } catch (Exception e) {
                System.out.println(e);
            }
    }
        
        return null;
    }
    
    
    
}
    
