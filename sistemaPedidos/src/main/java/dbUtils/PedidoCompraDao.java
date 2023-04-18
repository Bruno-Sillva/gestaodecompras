/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dbUtils;

import Enums.StatusPedidoDeCompraEnum;
import Utils.DataUtils;
import java.math.BigDecimal;
import java.math.MathContext;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import javax.swing.JOptionPane;
import model.Fornecedor;
import model.PedidoDeCompra;
import model.PedidoDeCompraItens;
import model.Produto;
import model.Usuario;

/**
 *
 * @author bssil
 */
public class PedidoCompraDao {
    DbUtils dbUtils = new DbUtils();
    
     public PedidoDeCompra novoPedidoDeCompra(PedidoDeCompra novoPedido, Usuario usuarioLogado) { 
        Connection con = null;
        PreparedStatement statement = null;
        SimpleDateFormat formatarData = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<Produto> itens = new PedidoDeCompraItens().getProdutos();
        Date hoje = new Date();
        Date hj = new java.sql.Date(hoje.getTime());
         
        String dataDeHoje = formatarData.format(hoje);
        System.out.println("Data de hoje " + dataDeHoje);
        String insert = "insert into pedido_compra (obs1, data,filial_id,fornecedor_id,id_user_insert,status,id_autorizador) "
                + "VALUES(?,?,?,?,?,?,?)" ;
        try {
              con = dbUtils.conectarAoDB();
              statement = con.prepareStatement(insert,Statement.RETURN_GENERATED_KEYS);
              statement.setString(1, "");
              statement.setDate(2, new java.sql.Date(hoje.getTime()));
              statement.setInt(3, novoPedido.getFilial_id());
              statement.setInt(4, novoPedido.getFornecedorId());
              statement.setInt(5, usuarioLogado.getId());
              statement.setString(6, "A");
              if(novoPedido.getUsuarioAutorizador() != null){
                  statement.setInt(7, novoPedido.getUsuarioAutorizador().getId()); 
              }else{
               statement.setInt(7, 0);    
              }
              int affectedRows = statement.executeUpdate();
                if (affectedRows == 0) {
            throw new SQLException("Erro ao criar novo pedido de compra");
            
        }
             try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                PedidoDeCompra pedido = new PedidoDeCompra(generatedKeys.getInt(1), 1, novoPedido.getObs(), BigDecimal.valueOf(0.0), hoje);
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
            System.out.println("Deveria fechar a conexão ao criar o pedido");
            dbUtils.fechaConexao(con, null, statement);
        }
    return null;
    }

     public void atualizaObs1Pedido(int numPed, String obs){
          String update = "update pedido_compra set obs1 = ? where numped = ?";
          try {
              System.out.println("Atualizando o pedido " + numPed +" com a obs1 " + obs);
               Connection con = dbUtils.conectarAoDB();
              PreparedStatement statement = con.prepareStatement(update);   
              statement.setString(1, obs);
              statement.setInt(2, numPed);
              statement.executeUpdate();
          } catch (Exception e) {
              JOptionPane.showMessageDialog(null, e);
          }
      }
      
     public void atualizaObs2Pedido(int numPed, String obs){
          String update = "update pedido_compra set obs2 = ? where numped = ?";
          try {
              System.out.println("Atualizando o pedido " + numPed +" com a obs2 " + obs);
               Connection con = dbUtils.conectarAoDB();
              PreparedStatement statement = con.prepareStatement(update);   
              statement.setString(1, obs);
              statement.setInt(2, numPed);
              statement.executeUpdate();
          } catch (Exception e) {
              JOptionPane.showMessageDialog(null, e);
          }
      }

     public boolean cancelaPedido(int numPed, Usuario usuarioLogado){
        if(statusDoPedido(numPed).equalsIgnoreCase(StatusPedidoDeCompraEnum.ABERTO.getValor())){
         String update = "update pedido_compra set status = 'C', data_cancelamento = ?, id_user_cancel=? where numped = ?";
         Connection con = null;
         PreparedStatement statement = null;
         ResultSet rs = null;
            try {
            con = dbUtils.conectarAoDB();
            statement = con.prepareStatement(update);
            statement.setDate(1, (java.sql.Date) DataUtils.sqlDateHoje());    
            statement.setInt(2, usuarioLogado.getId());
            statement.setInt(3, numPed);
            int rows = statement.executeUpdate();
            if(rows>0){
                return true;
            }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }finally{
            dbUtils.fechaConexao(con, rs, statement);
        }
           
            
        }else{
            return false;
        }
        return false;    
            
      }
      
     private String statusDoPedido(int numPed) {
          String select = "select status from pedido_compra where numped = ?";
          String retorno = null;
          Connection con = null;
         PreparedStatement statement = null;
         ResultSet rs = null;
          try {
              con = dbUtils.conectarAoDB();
              statement = con.prepareStatement(select);
              statement.setInt(1, numPed);
              rs = statement.executeQuery();
              while(rs.next()){
               retorno = rs.getString("status");
              }
              statement.close();
              rs.close();
              con.close();
              return retorno;
          } catch (Exception e) {
          return e.toString();
          }finally{
              dbUtils.fechaConexao(con, rs, statement);
          }
      }
      
     public PedidoDeCompra buscaPedidoDeCompra(int numPed) {
         System.out.println("Buscando pedido: " + numPed);
         String cabecalhoPedido = "select \n" +
"numped,\n" +
"filial_id,\n" +
"obs1,\n" +
"obs2,\n" +
"coalesce(qt_itens,0)qt_itens,\n" +
"round(coalesce(valor,0),2)valor,\n" +
"data,\n" +
"fornecedor_id,\n" +
"id_user_insert,\n" +
"id_user_update,\n" +
"data_update,\n" +
"status,\n" +
"NF,\n" +
"id_autorizador,\n" +
"data_autorizado,\n" +
"data_cancelamento,\n" +
"id_user_cancel \n" +
"from pedido_compra pc"
+ " where pc.numped = ? ";
          PedidoDeCompra pedido = new PedidoDeCompra();
         Connection con = null;
         PreparedStatement statement = null;
         ResultSet rs = null;
          try {  
           con = dbUtils.conectarAoDB();
           statement = con.prepareStatement(cabecalhoPedido);
           statement.setInt(1, numPed);
           rs = statement.executeQuery();
           while(rs.next()){
            pedido.setNumped(rs.getInt("numped"));
            pedido.setFilial_id(rs.getInt("filial_id"));
            pedido.setObs(rs.getString("obs1"));
            pedido.setObs2(rs.getString("obs2"));
            pedido.setQtItens(rs.getInt("qt_itens"));
            pedido.setValor(rs.getBigDecimal("valor"));
            pedido.setDataPedido(rs.getString("data"));
            pedido.setFornecedorId(rs.getInt("fornecedor_id"));
            pedido.setDataUpdate(rs.getString("data_update"));
            pedido.setStatus(rs.getString("status"));
            pedido.setNf(rs.getString("NF"));
            pedido.setDataAutorizado(rs.getString("data_autorizado"));
            pedido.setDataCancelamento(rs.getString("data_cancelamento"));
            pedido.setIdUserAprovador(rs.getInt("id_autorizador"));
            pedido.setIdUserCancelamento(rs.getInt("id_user_cancel"));
            pedido.setIdUserCriacao(rs.getInt("id_user_insert"));
            pedido.setIdUserUpdate(rs.getInt("id_user_update"));
           }
           Fornecedor fornecPedido = new FornecedorDao().buscarFornecedor(pedido.getFornecedorId());
           pedido.setFornecedor(fornecPedido);
           //Usuario usuarioCriacao = new UsuarioDao().buscaUsuario(pedido.getIdUserCriacao());
           //Usuario usuarioUpdate = new UsuarioDao().buscaUsuario(pedido.getIdUserUpdate());
          // Usuario usuarioAprovador = new UsuarioDao().buscaUsuario(pedido.getIdUserAprovador());
        //   Usuario usuarioCancelamento = new UsuarioDao().buscaUsuario(pedido.getIdUserCancelamento());
           pedido.setUsuarioCriacao(null);
           pedido.setUsuarioUpdate(null);
           pedido.setUsuarioAutorizador(null);
           pedido.setUsuarioCancelamento(null);
          // List produtos = new ProdutoDao().
           return pedido;
           
          } catch (Exception e) {
             e.printStackTrace();
          }finally{
            dbUtils.fechaConexao(con, rs, statement);
        }
          return pedido;
      }
      
     private List<Produto> buscaItensDoPedido(int numPed){
          return null;
      }
      
     public List pedidosDaSemana() {
         List<PedidoDeCompra> pedidos = new ArrayList<>();
         Connection con = null;
         PreparedStatement statement = null;
         ResultSet rs = null;
          String pedidosPorSemana = "select  \n" +
"pc.numped,  \n" +
"round(coalesce(valor,0),2)valor,  \n" +
"coalesce(qt_itens,0)qt_itens,  \n" +
"pc.data,  \n" +
"id_user_insert,  \n" +
"coalesce(pc.id_autorizador,0)id_autorizador,  \n" +
"pc.status,  \n" +
"f.fornecedor_id,"                 +
"f.nome fornecedor,\n" +
"u.nome criadoPor,\n" +
"autorizador.nome autorizador\n" +
"from pedido_compra pc   \n" +
"left join fornecedor f on f.fornecedor_id = pc.fornecedor_id \n" +
"left join usuario u on u.usuario_id = pc.id_user_insert \n" +
"left join usuario autorizador on autorizador.usuario_id = pc.id_autorizador \n" +
"where data>= ?\n" +
"and data <=?\n" +
"order by \"data\",numped ";
         
          try {
              con = dbUtils.conectarAoDB();
           statement = con.prepareStatement(pedidosPorSemana);
           statement.setDate(1, DataUtils.primeiroDiaDaSemana());
           statement.setDate(2, DataUtils.ultimaDiaDaSemana());
           rs = statement.executeQuery();
           while(rs.next()){
               PedidoDeCompra pedido = new PedidoDeCompra();
               pedido.setNumped(rs.getInt("numped"));
               pedido.setValor(new BigDecimal(rs.getString("valor")));
               pedido.setQtItens(rs.getInt("qt_itens"));
               pedido.setDataPedido(rs.getString("data"));
               pedido.setIdUserCriacao(rs.getInt("id_user_insert"));
               pedido.setIdUserAprovador(rs.getInt("id_autorizador"));
               pedido.setStatus(rs.getString("status"));
               pedido.setFornecedorId(rs.getInt("fornecedor_id"));
               if(pedido.getIdUserAprovador() != 0){
                 Usuario aprovador = new Usuario();
                 aprovador.setNome(rs.getString("autorizador"));
                 aprovador.setId(pedido.getIdUserAprovador());
                 pedido.setUsuarioAutorizador(aprovador);
               }else{
                   pedido.setUsuarioAutorizador(new Usuario("", "", 10));
               }
              Usuario usuarioCriacao = new Usuario();
              usuarioCriacao.setId(pedido.getIdUserCriacao());
              usuarioCriacao.setNome(rs.getString("criadoPor"));
              pedido.setUsuarioCriacao(usuarioCriacao);
              Fornecedor fornec = new Fornecedor();
              fornec.setId(rs.getInt("fornecedor_id"));
              fornec.setNome(rs.getString("fornecedor"));
               pedido.setFornecedor(fornec);
               pedidos.add(pedido);
           }
           return pedidos;
          
         
          } catch (Exception e) {
              e.printStackTrace();       
      } finally{
            dbUtils.fechaConexao(con, rs, statement);
        }
       return pedidos;
      }

     public void adicionarItemAoPedido(int produto_id, int numPed, BigDecimal valor, int quantidade) throws SQLException{
        int rows = 0;
         Connection con = null;
         PreparedStatement statement = null;
         ResultSet rs = null;
        if(produtoJaExisteNoPedidoComOMesmoValor(produto_id, numPed,valor)){
            String updateQuantidadeDoProdutoNoPedido = "update pedido_comprai set qt = (qt + ?) where numped=? and produto_id = ? and cast(valor as decimal(10,2)) = cast(? as decimal(10,2))";
            try {
                con = dbUtils.conectarAoDB();
                statement = con.prepareStatement(updateQuantidadeDoProdutoNoPedido); ;
                statement.setInt(1, quantidade);
                statement.setInt(2, numPed);
                statement.setInt(3, produto_id);
                statement.setBigDecimal(4, valor);
                rows = statement.executeUpdate();
            } catch (Exception e) {
                    e.printStackTrace();
            } 
        } else{  
        String insert = "insert into pedido_comprai (numped, produto_id,valor, qt) VALUES(?,?,?,?)" ;
            try {
              con = dbUtils.conectarAoDB();
              statement = con.prepareStatement(insert); 
              statement.setInt(1, numPed);
              statement.setInt(2, produto_id);
              statement.setBigDecimal(3, valor);
                statement.setDouble(4, quantidade);
              rows = statement.executeUpdate();
            } catch (Exception e) {
               e.printStackTrace();
            }finally{
                dbUtils.fechaConexao(con, rs, statement);
            }
        }
          if(rows >0){
            atualizaCabecalhoDoPedido(numPed);
        }
    }

     private boolean produtoJaExisteNoPedidoComOMesmoValor(int produto_id, int numPed, BigDecimal valor) throws SQLException{
        String sql = "select count(*) qtItem from pedido_comprai where numped = ? and produto_id = ? and cast(valor as decimal(10,2)) = cast(? as decimal(10,2))";
        int qtProdutoNoPedido =0;
        System.out.println("valor a ser comparado: " + valor);
         Connection con = null;
         PreparedStatement statement = null;
         ResultSet rs = null;
        try {
           con = dbUtils.conectarAoDB();
           statement = con.prepareStatement(sql);  
           statement.setInt(1, numPed);
           statement.setInt(2, produto_id);
           statement.setBigDecimal(3, valor);
           rs = statement.executeQuery();
           while(rs.next()){
               qtProdutoNoPedido = rs.getInt("qtItem");
           }
            return qtProdutoNoPedido >0;
              
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
          dbUtils.fechaConexao(con, rs, statement);
        }
        return true;
    }
              
     private void atualizaCabecalhoDoPedido(int numPed){
  
       String update = "update pedido_compra \n" +
"set valor = ?, qt_itens =?\n" +
"where numped =?";
          Connection con = null;
         PreparedStatement statement = null;
         ResultSet rs = null;
        try {
           con = dbUtils.conectarAoDB();
            statement = con.prepareStatement(update);
            statement.setBigDecimal(1, this.valorDoPedido(numPed));
            statement.setInt(2, this.quantidadeDeItensdoPedido(numPed));
            statement.setInt(3, numPed);
            statement.execute();
        } catch (Exception e) {
           e.printStackTrace();
        }finally{
           dbUtils.fechaConexao(con, rs, statement);
        }
       
    }

     private BigDecimal valorDoPedido(int numPed) throws SQLException{
      BigDecimal valorTotal = null;
      String select = "select \n" +
"coalesce(round(sum((pc.valor*pc.qt)),2),0)valorTotal\n" +
"from pedido_comprai pc \n" +
"where numped = ?";
         Connection con = null;
         PreparedStatement statement = null;
         ResultSet rs = null;
        try {
            con = dbUtils.conectarAoDB();
            statement = con.prepareStatement(select);
            statement.setInt(1, numPed);
            rs = statement.executeQuery();
            while(rs.next()){
                valorTotal = rs.getBigDecimal("valorTotal");
                System.out.println(valorTotal);
            }
            return valorTotal;
            
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            dbUtils.fechaConexao(con, rs, statement);
        }
      return valorTotal;
        
    }

     private int quantidadeDeItensdoPedido(int numPed) throws SQLException{
        String sql = "select count(*)qt_itens from pedido_comprai where numped = ?";
        int qtItens = 0;
        Connection con = null;
         PreparedStatement statement = null;
         ResultSet rs = null;
        try {
         con = dbUtils.conectarAoDB();
         statement = con.prepareStatement(sql); 
         statement.setInt(1, numPed);
         rs = statement.executeQuery();
          while(rs.next()){
              qtItens = rs.getInt("qt_itens");
          }
            System.out.println("Qt_itens do pedido: " + qtItens);
          return qtItens;
        } catch (Exception e) {
           e.printStackTrace();
        }finally{
            dbUtils.fechaConexao(con, rs, statement);
        }
        return 0;
    }
    
     public List produtosDoPedido(int numPed){
        Connection con = null;
         PreparedStatement statement = null;
         ResultSet rs = null;
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
            con = dbUtils.conectarAoDB();
            statement = con.prepareStatement(sql);
             rs = statement.executeQuery();
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
           dbUtils.fechaConexao(con, rs, statement);
        }
        return listaDeProdutos;
    }

       public void removeItemDoPedido(int produto_id, int numPed, BigDecimal valor){
        Connection con = null;
         PreparedStatement statement = null;
         ResultSet rs = null;
           String delete = "delete FROM pedido_comprai where numped = ? and produto_id = ? and cast(valor as decimal(10,2)) = cast(? as decimal(10,2)) ";
        try {
            con = dbUtils.conectarAoDB();
           statement = con.prepareStatement(delete);
           statement.setInt(1, numPed);
           statement.setInt(2, produto_id);
           statement.setBigDecimal(3, valor);
          int rows = statement.executeUpdate();
          if(rows>0){
              atualizaCabecalhoDoPedido(numPed);
          }

        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            dbUtils.fechaConexao(con, rs, statement);
        }
        
    }


}
