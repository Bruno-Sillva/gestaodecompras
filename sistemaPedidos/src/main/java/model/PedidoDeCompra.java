/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 *
 * @author bssil
 */
public class PedidoDeCompra {
    private int numped;
    private int filial_id;
    private String obs;
    private String obs2;
    private int qtItens;
    private BigDecimal valor;
    private Date data;
    private String dataPedido;
    private int fornecedorId;
    Fornecedor fornecedor;
    Usuario usuarioCriacao;
    Usuario usuarioUpdate;
    private String dataUpdate;
    private String status;
    private String nf;
    Usuario usuarioAutorizador;
    private String dataAutorizado;
    private String dataCancelamento;
    Usuario usuarioCancelamento;
    private int idUserUpdate;
    private int idUserCriacao;
    private int idUserCancelamento;
    private int idUserAprovador;
    List<Produto> produtosDoPedido;

    public List<Produto> getProdutosDoPedido() {
        return produtosDoPedido;
    }

    public void setProdutosDoPedido(List<Produto> produtosDoPedido) {
        this.produtosDoPedido = produtosDoPedido;
    }
    

    public int getIdUserAprovador() {
        return idUserAprovador;
    }

    public void setIdUserAprovador(int idUserAprovador) {
        this.idUserAprovador = idUserAprovador;
    }

    public int getIdUserUpdate() {
        return idUserUpdate;
    }

    public void setIdUserUpdate(int idUserUpdate) {
        this.idUserUpdate = idUserUpdate;
    }

    public int getIdUserCriacao() {
        return idUserCriacao;
    }

    public void setIdUserCriacao(int idUserCriacao) {
        this.idUserCriacao = idUserCriacao;
    }

    public int getIdUserCancelamento() {
        return idUserCancelamento;
    }

    public void setIdUserCancelamento(int idUserCancelamento) {
        this.idUserCancelamento = idUserCancelamento;
    }
    
    
    
    
    
    
    public String getDataPedido() {
        return dataPedido;
    }

    public void setDataPedido(String dataPedido) {
        this.dataPedido = dataPedido;
    }
   
    

    public String getObs2() {
        return obs2;
    }

    public void setObs2(String obs2) {
        this.obs2 = obs2;
    }

    public int getQtItens() {
        return qtItens;
    }

    public void setQtItens(int qtItens) {
        this.qtItens = qtItens;
    }

    public Fornecedor getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
    }

    public Usuario getUsuarioCriacao() {
        return usuarioCriacao;
    }

    public void setUsuarioCriacao(Usuario usuarioCriacao) {
        this.usuarioCriacao = usuarioCriacao;
    }

    public Usuario getUsuarioUpdate() {
        return usuarioUpdate;
    }

    public void setUsuarioUpdate(Usuario usuarioUpdate) {
        this.usuarioUpdate = usuarioUpdate;
    }

    public String getDataUpdate() {
        return dataUpdate;
    }

    public void setDataUpdate(String dataUpdate) {
        this.dataUpdate = dataUpdate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNf() {
        return nf;
    }

    public void setNf(String nf) {
        this.nf = nf;
    }

    public Usuario getUsuarioAutorizador() {
        return usuarioAutorizador;
    }

    public void setUsuarioAutorizador(Usuario usuarioAutorizador) {
        this.usuarioAutorizador = usuarioAutorizador;
    }

    public String getDataAutorizado() {
        return dataAutorizado;
    }

    public void setDataAutorizado(String dataAutorizado) {
        this.dataAutorizado = dataAutorizado;
    }

    public String getDataCancelamento() {
        return dataCancelamento;
    }

    public void setDataCancelamento(String dataCancelamento) {
        this.dataCancelamento = dataCancelamento;
    }

    public Usuario getUsuarioCancelamento() {
        return usuarioCancelamento;
    }

    public void setUsuarioCancelamento(Usuario usuarioCancelamento) {
        this.usuarioCancelamento = usuarioCancelamento;
    }
    
    
    

    public int getFornecedorId() {
        return fornecedorId;
    }

    public void setFornecedorId(int fornecedorId) {
        this.fornecedorId = fornecedorId;
    }
    private List<Produto> listaDeProdutos;

    public PedidoDeCompra(int numped, int filial_id, String obs, BigDecimal valor, Date data, List<Produto> listaDeProdutos) {
        this.numped = numped;
        this.filial_id = filial_id;
        this.obs = obs;
        this.valor = valor;
        this.data = data;
        this.listaDeProdutos = listaDeProdutos;
    }
    
    public PedidoDeCompra(int numped, int filial_id, String obs, BigDecimal valor, Date data){
        this.numped = numped;
        this.filial_id = filial_id;
        this.obs = obs;
        this.valor = valor;
        this.data = data;
    }

    public int getNumped() {
        return numped;
    }

    public void setNumped(int numped) {
        this.numped = numped;
    }

    public int getFilial_id() {
        return filial_id;
    }

    public void setFilial_id(int filial_id) {
        this.filial_id = filial_id;
    }

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getData() {
        SimpleDateFormat formatarData = new SimpleDateFormat("dd/MM/yyyy");
        return formatarData.format(this.data);
    }

    public PedidoDeCompra() {
    }

    public void setData(Date data) {
        this.data = data;
    }

    public List<Produto> getListaDeProdutos() {
        return listaDeProdutos;
    }

    public void setListaDeProdutos(List<Produto> listaDeProdutos) {
        this.listaDeProdutos = listaDeProdutos;
    }
    
    
    
}
