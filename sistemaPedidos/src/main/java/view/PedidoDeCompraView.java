/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package view;

import Enums.ColunaTabela;
import Enums.StatusPedidoDeCompraEnum;
import Utils.DataUtils;
import Utils.Planilha;
import com.ctc.wstx.util.DataUtil;
import com.formdev.flatlaf.FlatLightLaf;
import dbUtils.DbUtils;
import dbUtils.FilialDao;
import dbUtils.FornecedorDao;
import dbUtils.PedidoCompraDao;
import dbUtils.ProdutoDao;
import dbUtils.UsuarioDao;
import java.awt.Component;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.ArrayList;
import java.util.Locale;
import java.util.regex.Matcher;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.MaskFormatter;
import model.Filial;
import model.Fornecedor;
import model.PedidoDeCompra;
import model.Produto;
import model.Usuario;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author bssil
 */
public class PedidoDeCompraView extends javax.swing.JFrame {

    DefaultListModel listaDeProdutos = new DefaultListModel();
    DefaultListModel listaDeFiliais = new DefaultListModel();
    DefaultListModel listaDeFornecedores = new DefaultListModel();
    DefaultListModel listaDeAutorizadores = new DefaultListModel();
    DefaultComboBoxModel listaDeAutorizadoresComboBox =  new DefaultComboBoxModel();
    PedidoCompraDao pedidoDao = new PedidoCompraDao();
    
    
    DefaultTableModel tabelaDeProdutos = new DefaultTableModel(){       
    @Override
    public boolean isCellEditable(int row, int column) {
       if(column ==2 || column == 3 || column == 4){
           return true;
       }else{
              return false;
       }
    
    }
    };
    DefaultTableModel tabelaDePedidosSemana = new DefaultTableModel(){       
    @Override
    public boolean isCellEditable(int row, int column) {
              return false;
    }
    };
    DbUtils dbUtils = new DbUtils();
    List<Produto> listaDeProdutosDoPedido = new ArrayList<>();
    Usuario usuarioLogado;
    PedidoDeCompra novoPedido = new PedidoDeCompra();
    PedidoDeCompra pedidoAEditar = null;
    
    /**
     * Creates new form MainView
     * @param usuarioLogado
     */
    public PedidoDeCompraView(Usuario usuarioLogado) {
        initComponents();
        popUpMenu.add(searchBox);
        jPopUpAutorizador.add(jPanelListaDeAutorizadores);
        jPopUpFiliais.add(jPanelListadeFiliais);
        jPopUpFornecedor.add(jPanelListaDeFornecedores);
        //iniciarProdutos();
        iniciarTabelaDeProdutos();
         buscaListaDeAutorizadores();
      iniciarTabelaDePedidos();
       desabilitaTela();
    //  habilitaTela();
        alteracaoNaTabela();
        this.usuarioLogado = usuarioLogado;
        this.setTitle("Pedido de compra                                                      usuário " + usuarioLogado.getNome());
        usuarioLogadoLabel.setText(usuarioLogado.getNome());
   

    }

    
    
    
    
    
    private ArrayList getProdutos(){
        ArrayList produtos= null;
        try {
            produtos = dbUtils.getProdutos();
        } catch (SQLException ex) {
            Logger.getLogger(PedidoDeCompraView.class.getName()).log(Level.SEVERE, null, ex);
        }
        return produtos;
    }
    
    private void iniciarTabelaDeProdutos(){
        tabelaDeProdutos.addColumn("ID");
        tabelaDeProdutos.addColumn("NOME");
        tabelaDeProdutos.addColumn("QUANTIDADE");
        tabelaDeProdutos.addColumn("VALOR UNT");
        tabelaDeProdutos.addColumn("OBS");
        tabelaDeProdutos.addColumn("VL TOTAL ITEM");
        
    }
    
    private void iniciarTabelaDePedidos(){
        tabelaDePedidosSemana.addColumn("NUMPED");
        tabelaDePedidosSemana.addColumn("VALOR");
        tabelaDePedidosSemana.addColumn("QT ITENS");
        tabelaDePedidosSemana.addColumn("DATA");
        tabelaDePedidosSemana.addColumn("CRIADO POR");
        tabelaDePedidosSemana.addColumn("AUTORIZADOR");
        tabelaDePedidosSemana.addColumn("FORNECEDOR");
        tabelaDePedidosSemana.addColumn("STATUS");
    }
    
    
    private void buscaProdutos(String produtoPesquisado){
        try {
            System.out.println("Buscando: " + produtoPesquisado);
            DefaultListModel produtosPesquisados = new DefaultListModel();
            List<Produto> produtos = new ProdutoDao().buscarListaDeProdutos();
            produtos.stream().forEach((produto)->{
                String nomeProduto = produto.toString().toUpperCase();
                if(nomeProduto.contains(produtoPesquisado.toUpperCase())){
                    produtosPesquisados.addElement(produto);
                }
                else if(produtoPesquisado.substring(0,1).contains(".")){
                    produtosPesquisados.addElement(produto);
                }
            });
            
            listaDeProdutos = produtosPesquisados;
            JlistaDeProdutos.setModel(listaDeProdutos);
        } catch (SQLException ex) {
            Logger.getLogger(PedidoDeCompraView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    private void buscaPedidosDaSemana(){
       tabelaDePedidosSemana.setRowCount(0);
        List<PedidoDeCompra> pedidosDaSemana = pedidoDao.pedidosDaSemana();
        
        pedidosDaSemana.stream().forEach((pedido)->{
            
          tabelaDePedidosSemana.addRow(new Object[]{
           pedido.getNumped(),
          pedido.getValor(),
          pedido.getQtItens(),
          pedido.getDataPedido(),
          pedido.getUsuarioCriacao().getNome(),
          pedido.getUsuarioAutorizador().getNome(),
          pedido.getFornecedor().toString(),
         pedido.getStatus()});
        });
        tabelaDePedidosPorSemana.setModel(tabelaDePedidosSemana);
        qtPedidosDaSemanaTextField.setText(Integer.toString(tabelaDePedidosPorSemana.getRowCount()));
        semanaAtualTextField.setText(Integer.toString(DataUtils.semanaAtual()));
    }
    
    private void iniciarListaDeProdutos(){
        getProdutos().stream().forEach((produto) ->{
        listaDeProdutos.addElement(produto);
        });
    }
    
    private void iniciarProdutos(){
        List<Produto> produtos = new ArrayList<>();
        try {
            produtos = dbUtils.buscarListaDeProdutos();
        } catch (SQLException ex) {
            Logger.getLogger(PedidoDeCompraView.class.getName()).log(Level.SEVERE, null, ex);
        }
        produtos.stream().forEach((produto) ->{
        listaDeProdutos.addElement(produto);
        });
    }
    
    private void desabilitaTela(){
        Component[] componentes = pedidoDeCompraPanel.getComponents();
       Component[] componentesTab = jTabItensPedido.getComponents();
       Component[] componentesPanelItensPedido = jPanelItensPedidos.getComponents();
        System.out.println(componentes.length);
        for(Component componente : componentes){  
           
            componente.setEnabled(false);            
        }
        for(Component c : componentesTab){
            c.setEnabled(false);   
        }
        for(Component c: componentesPanelItensPedido){
            c.setEnabled(false);
        }
        jComboBox1.setEditable(true);
         jComboBox1.setEnabled(true);
         filialNovoPedidoCompra.setEnabled(true);
        fornecedorNovoPedido.setEditable(true);
        fornecedorNovoPedido.setText("");
        filialNovoPedidoCompra.setText("");
        jTable1.setEnabled(false);
        novoPedidoDeCompraButton.setEnabled(true);
    }
    
    private void habilitaTela(){
         Component[] componentes = pedidoDeCompraPanel.getComponents();
         Component[] componentesPanelItensPedido = jPanelItensPedidos.getComponents();
      
        for(Component componente : componentes){  
   
            componente.setEnabled(true);            
        }
          for(Component c: componentesPanelItensPedido){
            c.setEnabled(true);
        }
      
        jComboBox1.setEditable(false);
         jComboBox1.setEnabled(false);
        filialNovoPedidoCompra.setEnabled(false);
        fornecedorNovoPedido.setEditable(false);
        jTable1.setEnabled(true);
        novoPedidoDeCompraButton.setEnabled(false);
    }
    
    private void filtroDePesquisa(String produtoPesquisado){
        DefaultListModel produtosPesquisados = new DefaultListModel();
        ArrayList produtos = getProdutos();
        produtos.stream().forEach((produto)->{
        String nomeProduto = produto.toString().toUpperCase();
        if(nomeProduto.contains(produtoPesquisado.toUpperCase())){
            produtosPesquisados.addElement(produto);
        }
        else if(produtoPesquisado.substring(0,1).contains(".")){
        produtosPesquisados.addElement(produto);
        }
        });
        
        listaDeProdutos = produtosPesquisados;
        JlistaDeProdutos.setModel(listaDeProdutos);
        
    }
    
    private void buscaFiliais(String filialPesquisada){
        DefaultListModel lista = new DefaultListModel();
        List<Filial> filiais = new FilialDao().buscarFilials();
        filiais.stream().forEach((filial)->{
            if(filial.toString().contains(filialPesquisada.toUpperCase())){
              lista.addElement(filial);
            }
        });
        listaDeFiliais = lista;
        jListaDeFiliais.setModel(lista);
    }
    
    private void buscaFornecedores(String fornecedorPesquisado){
        DefaultListModel lista = new DefaultListModel();
        List<Fornecedor> fornecedores = new FornecedorDao().fornecedores();
        fornecedores.stream().forEach((fornecedor)->{
            if(fornecedor.toString().contains(fornecedorPesquisado.toUpperCase())){
                lista.addElement(fornecedor);
            }
        });
        listaDeFornecedores = lista;
        jListaDeFornecedores.setModel(lista);
    }
    
    
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        popUpMenu = new javax.swing.JPopupMenu();
        searchBox = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        JlistaDeProdutos = new javax.swing.JList<>();
        jPopUpFiliais = new javax.swing.JPopupMenu();
        jPopUpFornecedor = new javax.swing.JPopupMenu();
        jPopUpAutorizador = new javax.swing.JPopupMenu();
        jPanelListadeFiliais = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jListaDeFiliais = new javax.swing.JList<>();
        jPanelListaDeFornecedores = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jListaDeFornecedores = new javax.swing.JList<>();
        jPanelListaDeAutorizadores = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        jlistaDeAutoriazadores = new javax.swing.JList<>();
        pedidoDeCompraTab = new javax.swing.JTabbedPane();
        pedidoDeCompraPanel = new javax.swing.JPanel();
        novoPedidoDeCompraButton = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        pedidoIDTextField = new javax.swing.JTextField();
        qtItensPedidoTextField = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        dataDoPedidoTextField = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        valorTotalPedidoTextField = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        filialNovoPedidoCompra = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        fornecedorNovoPedido = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jSeparator1 = new javax.swing.JSeparator();
        finalizarPedidoButton = new javax.swing.JButton();
        jTabItensPedido = new javax.swing.JTabbedPane();
        jPanelItensPedidos = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        produtoTextField = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        qtProdutoTextField = new javax.swing.JTextField();
        adicionarProdutoButton = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        valorProdutoTextField = new javax.swing.JFormattedTextField();
        jPanel4 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jScrollPane7 = new javax.swing.JScrollPane();
        obs2NovoPedidoTextArea = new javax.swing.JTextArea();
        jScrollPane8 = new javax.swing.JScrollPane();
        obs1NovoPedidoTextArea = new javax.swing.JTextArea();
        cancelarNovoPedidoButton = new javax.swing.JButton();
        relatorioPanel = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        idPedidoTextField = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        tabelaDePedidosPorSemana = new javax.swing.JTable();
        imprimirRelatorioButton = new javax.swing.JButton();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        semanaAtualTextField = new javax.swing.JTextField();
        qtPedidosDaSemanaTextField = new javax.swing.JTextField();
        exportarParaExcelButton = new javax.swing.JButton();
        editarPedidoPanel = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        pedidoAEditarTextField = new javax.swing.JTextField();
        editarPedidoButton = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel18 = new javax.swing.JLabel();
        pedidoAEditarTextField1 = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        pedidoAEditarTextField2 = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        pedidoAEditarTextField3 = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        pedidoAEditarTextField4 = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        pedidoAEditarTextField5 = new javax.swing.JTextField();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        salvarPedidoEditadoButton = new javax.swing.JButton();
        cancelarPedidoEditadoButton = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        usuarioLogadoLabel = new javax.swing.JLabel();

        popUpMenu.setFocusable(false);

        JlistaDeProdutos.setModel(listaDeProdutos);
        JlistaDeProdutos.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        JlistaDeProdutos.setToolTipText("");
        JlistaDeProdutos.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                JlistaDeProdutosValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(JlistaDeProdutos);

        javax.swing.GroupLayout searchBoxLayout = new javax.swing.GroupLayout(searchBox);
        searchBox.setLayout(searchBoxLayout);
        searchBoxLayout.setHorizontalGroup(
            searchBoxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 479, Short.MAX_VALUE)
            .addGroup(searchBoxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 479, Short.MAX_VALUE))
        );
        searchBoxLayout.setVerticalGroup(
            searchBoxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
            .addGroup(searchBoxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE))
        );

        jPopUpFiliais.setFocusable(false);

        jPopUpFornecedor.setFocusable(false);

        jPopUpAutorizador.setFocusable(false);

        jListaDeFiliais.setModel(listaDeFiliais);
        jListaDeFiliais.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jListaDeFiliaisValueChanged(evt);
            }
        });
        jScrollPane4.setViewportView(jListaDeFiliais);

        javax.swing.GroupLayout jPanelListadeFiliaisLayout = new javax.swing.GroupLayout(jPanelListadeFiliais);
        jPanelListadeFiliais.setLayout(jPanelListadeFiliaisLayout);
        jPanelListadeFiliaisLayout.setHorizontalGroup(
            jPanelListadeFiliaisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 620, Short.MAX_VALUE)
            .addGroup(jPanelListadeFiliaisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 620, Short.MAX_VALUE))
        );
        jPanelListadeFiliaisLayout.setVerticalGroup(
            jPanelListadeFiliaisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 157, Short.MAX_VALUE)
            .addGroup(jPanelListadeFiliaisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanelListadeFiliaisLayout.createSequentialGroup()
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        jListaDeFornecedores.setModel(listaDeFornecedores);
        jListaDeFornecedores.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jListaDeFornecedoresValueChanged(evt);
            }
        });
        jScrollPane5.setViewportView(jListaDeFornecedores);

        javax.swing.GroupLayout jPanelListaDeFornecedoresLayout = new javax.swing.GroupLayout(jPanelListaDeFornecedores);
        jPanelListaDeFornecedores.setLayout(jPanelListaDeFornecedoresLayout);
        jPanelListaDeFornecedoresLayout.setHorizontalGroup(
            jPanelListaDeFornecedoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 473, Short.MAX_VALUE)
        );
        jPanelListaDeFornecedoresLayout.setVerticalGroup(
            jPanelListaDeFornecedoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelListaDeFornecedoresLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jlistaDeAutoriazadores.setModel(listaDeAutorizadores);
        jScrollPane6.setViewportView(jlistaDeAutoriazadores);

        javax.swing.GroupLayout jPanelListaDeAutorizadoresLayout = new javax.swing.GroupLayout(jPanelListaDeAutorizadores);
        jPanelListaDeAutorizadores.setLayout(jPanelListaDeAutorizadoresLayout);
        jPanelListaDeAutorizadoresLayout.setHorizontalGroup(
            jPanelListaDeAutorizadoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 601, Short.MAX_VALUE)
        );
        jPanelListaDeAutorizadoresLayout.setVerticalGroup(
            jPanelListaDeAutorizadoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 139, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        pedidoDeCompraTab.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                pedidoDeCompraTabStateChanged(evt);
            }
        });

        novoPedidoDeCompraButton.setText("Novo");
        novoPedidoDeCompraButton.setToolTipText("");
        novoPedidoDeCompraButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                novoPedidoDeCompraButtonActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel1.setText("ID");

        pedidoIDTextField.setEditable(false);
        pedidoIDTextField.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        pedidoIDTextField.setFocusable(false);

        qtItensPedidoTextField.setEditable(false);
        qtItensPedidoTextField.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        qtItensPedidoTextField.setEnabled(false);
        qtItensPedidoTextField.setFocusable(false);

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel4.setText("Qt itens:");

        jLabel5.setText("Data do pedido:");

        dataDoPedidoTextField.setEditable(false);
        dataDoPedidoTextField.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        dataDoPedidoTextField.setEnabled(false);
        dataDoPedidoTextField.setFocusable(false);

        jLabel12.setText("VALOR TOTAL");

        valorTotalPedidoTextField.setEditable(false);
        valorTotalPedidoTextField.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jLabel10.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel10.setText("FILIAL");

        filialNovoPedidoCompra.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        filialNovoPedidoCompra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                filialNovoPedidoCompraActionPerformed(evt);
            }
        });
        filialNovoPedidoCompra.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                filialNovoPedidoCompraKeyReleased(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel11.setText("FORNECEDOR");

        fornecedorNovoPedido.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        fornecedorNovoPedido.setToolTipText("");
        fornecedorNovoPedido.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                fornecedorNovoPedidoKeyReleased(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel13.setText("AUTORIZADOR");

        jComboBox1.setModel(listaDeAutorizadoresComboBox);
        jComboBox1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox1ItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pedidoIDTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(qtItensPedidoTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dataDoPedidoTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(valorTotalPedidoTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(filialNovoPedidoCompra, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fornecedorNovoPedido, javax.swing.GroupLayout.PREFERRED_SIZE, 546, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 589, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(83, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(filialNovoPedidoCompra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10)
                    .addComponent(jLabel11)
                    .addComponent(fornecedorNovoPedido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pedidoIDTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(qtItensPedidoTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5)
                    .addComponent(dataDoPedidoTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12)
                    .addComponent(valorTotalPedidoTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(17, Short.MAX_VALUE))
        );

        finalizarPedidoButton.setText("Finalizar pedido");
        finalizarPedidoButton.setToolTipText("");
        finalizarPedidoButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                finalizarPedidoButtonActionPerformed(evt);
            }
        });

        jTable1.setModel(tabelaDeProdutos);
        jTable1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jTable1);

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel2.setText("Produto");

        produtoTextField.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        produtoTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                produtoTextFieldActionPerformed(evt);
            }
        });
        produtoTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                produtoTextFieldKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                produtoTextFieldKeyTyped(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel3.setText("Valor unitário");

        qtProdutoTextField.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        qtProdutoTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                qtProdutoTextFieldActionPerformed(evt);
            }
        });

        adicionarProdutoButton.setText("Adicionar");
        adicionarProdutoButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                adicionarProdutoButtonActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel6.setText("QT");

        valorProdutoTextField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));

        javax.swing.GroupLayout jPanelItensPedidosLayout = new javax.swing.GroupLayout(jPanelItensPedidos);
        jPanelItensPedidos.setLayout(jPanelItensPedidosLayout);
        jPanelItensPedidosLayout.setHorizontalGroup(
            jPanelItensPedidosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelItensPedidosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelItensPedidosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 1276, Short.MAX_VALUE)
                    .addGroup(jPanelItensPedidosLayout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(produtoTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 467, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(valorProdutoTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(13, 13, 13)
                        .addComponent(jLabel6)
                        .addGap(12, 12, 12)
                        .addComponent(qtProdutoTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(36, 36, 36)
                        .addComponent(adicionarProdutoButton)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanelItensPedidosLayout.setVerticalGroup(
            jPanelItensPedidosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelItensPedidosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelItensPedidosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(produtoTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(adicionarProdutoButton)
                    .addComponent(qtProdutoTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(valorProdutoTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 302, Short.MAX_VALUE))
        );

        jTabItensPedido.addTab("Itens", jPanelItensPedidos);

        jLabel14.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel14.setText("OBS 1");

        jLabel15.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel15.setText("OBS 2");

        obs2NovoPedidoTextArea.setColumns(20);
        obs2NovoPedidoTextArea.setRows(5);
        obs2NovoPedidoTextArea.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                obs2NovoPedidoTextAreaFocusLost(evt);
            }
        });
        jScrollPane7.setViewportView(obs2NovoPedidoTextArea);

        obs1NovoPedidoTextArea.setColumns(20);
        obs1NovoPedidoTextArea.setRows(5);
        obs1NovoPedidoTextArea.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                obs1NovoPedidoTextAreaFocusLost(evt);
            }
        });
        jScrollPane8.setViewportView(obs1NovoPedidoTextArea);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel14)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel15)
                        .addGap(29, 29, 29)
                        .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 598, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel4Layout.createSequentialGroup()
                    .addGap(84, 84, 84)
                    .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 598, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(606, Short.MAX_VALUE)))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jLabel14)
                .addGap(150, 150, 150)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel15)
                    .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(46, 46, 46))
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel4Layout.createSequentialGroup()
                    .addGap(38, 38, 38)
                    .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(201, Short.MAX_VALUE)))
        );

        jTabItensPedido.addTab("Obs", jPanel4);

        cancelarNovoPedidoButton.setText("Cancelar pedido");
        cancelarNovoPedidoButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelarNovoPedidoButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pedidoDeCompraPanelLayout = new javax.swing.GroupLayout(pedidoDeCompraPanel);
        pedidoDeCompraPanel.setLayout(pedidoDeCompraPanelLayout);
        pedidoDeCompraPanelLayout.setHorizontalGroup(
            pedidoDeCompraPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator1)
            .addGroup(pedidoDeCompraPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(pedidoDeCompraPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(novoPedidoDeCompraButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cancelarNovoPedidoButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(finalizarPedidoButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jTabItensPedido)
        );
        pedidoDeCompraPanelLayout.setVerticalGroup(
            pedidoDeCompraPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pedidoDeCompraPanelLayout.createSequentialGroup()
                .addGroup(pedidoDeCompraPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pedidoDeCompraPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(pedidoDeCompraPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(novoPedidoDeCompraButton)
                            .addComponent(finalizarPedidoButton))
                        .addGap(26, 26, 26)
                        .addComponent(cancelarNovoPedidoButton))
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabItensPedido)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pedidoDeCompraTab.addTab("Novo pedido de compra", pedidoDeCompraPanel);

        jLabel7.setText("Pedido");

        idPedidoTextField.setToolTipText("");

        tabelaDePedidosPorSemana.setModel(tabelaDePedidosSemana);
        tabelaDePedidosPorSemana.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        tabelaDePedidosPorSemana.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelaDePedidosPorSemanaMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tabelaDePedidosPorSemana);

        imprimirRelatorioButton.setText("Imprimir");
        imprimirRelatorioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                imprimirRelatorioButtonActionPerformed(evt);
            }
        });

        jLabel16.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel16.setText("SEMANA");

        jLabel17.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel17.setText("PEDIDOS");

        semanaAtualTextField.setEditable(false);
        semanaAtualTextField.setHorizontalAlignment(javax.swing.JTextField.LEFT);

        qtPedidosDaSemanaTextField.setEditable(false);
        qtPedidosDaSemanaTextField.setHorizontalAlignment(javax.swing.JTextField.LEFT);

        exportarParaExcelButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sheets.png"))); // NOI18N
        exportarParaExcelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportarParaExcelButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout relatorioPanelLayout = new javax.swing.GroupLayout(relatorioPanel);
        relatorioPanel.setLayout(relatorioPanelLayout);
        relatorioPanelLayout.setHorizontalGroup(
            relatorioPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(relatorioPanelLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(idPedidoTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(imprimirRelatorioButton)
                .addGap(339, 339, 339)
                .addComponent(jLabel16)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(semanaAtualTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel17)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(qtPedidosDaSemanaTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, relatorioPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 1276, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, relatorioPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(exportarParaExcelButton)
                .addGap(23, 23, 23))
        );
        relatorioPanelLayout.setVerticalGroup(
            relatorioPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(relatorioPanelLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(relatorioPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(idPedidoTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(imprimirRelatorioButton)
                    .addComponent(jLabel16)
                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(semanaAtualTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(qtPedidosDaSemanaTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(32, 32, 32)
                .addComponent(exportarParaExcelButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 420, Short.MAX_VALUE)
                .addContainerGap())
        );

        pedidoDeCompraTab.addTab("Relatório", relatorioPanel);

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel8.setText("PEDIDO");

        pedidoAEditarTextField.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        pedidoAEditarTextField.setToolTipText("");

        editarPedidoButton.setText("Editar");
        editarPedidoButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editarPedidoButtonActionPerformed(evt);
            }
        });

        jLabel18.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel18.setText("FILIAL");

        pedidoAEditarTextField1.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        pedidoAEditarTextField1.setToolTipText("");

        jLabel19.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel19.setText("DATA");

        pedidoAEditarTextField2.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        pedidoAEditarTextField2.setToolTipText("");

        jLabel20.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel20.setText("QT ITENS");

        pedidoAEditarTextField3.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        pedidoAEditarTextField3.setToolTipText("");

        jLabel21.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel21.setText("VALOR TOTAL");

        pedidoAEditarTextField4.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        pedidoAEditarTextField4.setToolTipText("");

        jLabel22.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel22.setText("CRIADO POR");

        pedidoAEditarTextField5.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        pedidoAEditarTextField5.setToolTipText("");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1288, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 395, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Itens", jPanel1);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1288, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 395, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Obs", jPanel3);

        salvarPedidoEditadoButton.setText("Salvar");

        cancelarPedidoEditadoButton.setText("Cancelar pedido");

        javax.swing.GroupLayout editarPedidoPanelLayout = new javax.swing.GroupLayout(editarPedidoPanel);
        editarPedidoPanel.setLayout(editarPedidoPanelLayout);
        editarPedidoPanelLayout.setHorizontalGroup(
            editarPedidoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator2)
            .addGroup(editarPedidoPanelLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jLabel18)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pedidoAEditarTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel19)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pedidoAEditarTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel20)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pedidoAEditarTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pedidoAEditarTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pedidoAEditarTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jTabbedPane1)
            .addGroup(editarPedidoPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pedidoAEditarTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(editarPedidoButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(salvarPedidoEditadoButton)
                .addGap(41, 41, 41)
                .addComponent(cancelarPedidoEditadoButton)
                .addGap(25, 25, 25))
        );
        editarPedidoPanelLayout.setVerticalGroup(
            editarPedidoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(editarPedidoPanelLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(editarPedidoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(editarPedidoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(pedidoAEditarTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(editarPedidoButton)
                        .addComponent(salvarPedidoEditadoButton)
                        .addComponent(cancelarPedidoEditadoButton))
                    .addComponent(jLabel8))
                .addGap(18, 18, 18)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(editarPedidoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(pedidoAEditarTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel19)
                    .addComponent(pedidoAEditarTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel20)
                    .addComponent(pedidoAEditarTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel21)
                    .addComponent(pedidoAEditarTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel22)
                    .addComponent(pedidoAEditarTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jTabbedPane1))
        );

        pedidoDeCompraTab.addTab("Editar pedido", editarPedidoPanel);

        jLabel9.setText("Usuário:");

        usuarioLogadoLabel.setText("Usuário:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pedidoDeCompraTab, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(usuarioLogadoLabel)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(usuarioLogadoLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pedidoDeCompraTab))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    
    private void alteracaoNaTabela(){
        
       tabelaDeProdutos.addTableModelListener(new TableModelListener(){
           @Override
           public void tableChanged(TableModelEvent e) {
            if(e.getType() == 0){
              int produtoASerAlterado = Integer.parseInt(tabelaDeProdutos.getValueAt(e.getFirstRow(), 0).toString());
              if(e.getColumn() == ColunaTabela.QUANTIDADE.getValor()){
                  try {
              int valorAlterado = Integer.parseInt(jTable1.getValueAt(e.getFirstRow(), e.getColumn()).toString());
              dbUtils.atualizaQtItemDoPedido(novoPedido.getNumped(), 
               Double.parseDouble(jTable1.getValueAt(e.getFirstRow(),
                       e.getColumn()).toString()), produtoASerAlterado);
           
                  } catch (Exception ex) {
                  JOptionPane.showMessageDialog(rootPane, ex);
                  ex.printStackTrace();
                      atualizarTabelaProdutos(novoPedido.getNumped());
                  }
            //  int valorAlterado = Integer.parseInt(jTable1.getValueAt(e.getFirstRow(), e.getColumn()).toString());
            atualizarTabelaProdutos(novoPedido.getNumped());
        
                 //  atualizarTabelaProdutos(Integer.parseInt(pedidoIDTextField.getText()));
              }
              if(e.getColumn() == ColunaTabela.OBS.getValor()){
                  dbUtils.atualizarObsItem(produtoASerAlterado, novoPedido.getNumped(), jTable1.getValueAt(e.getFirstRow(), e.getColumn()).toString());
                   atualizarTabelaProdutos(Integer.parseInt(pedidoIDTextField.getText()));
              }
            if(e.getColumn() == ColunaTabela.VALOR.getValor()){
         
            }
            }
             
           }
       });

    }
    
    
    
    private void JlistaDeProdutosValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_JlistaDeProdutosValueChanged
        try {
           Produto produtoSelecionado = (Produto)listaDeProdutos.getElementAt(JlistaDeProdutos.getSelectedIndex());
    
        System.out.println("Produto ID: " + produtoSelecionado.getId() +  "   "
                + "Produto: " + produtoSelecionado.getName());  
        produtoTextField.setText(produtoSelecionado.toString());
        
      //  tabelaDeProdutos.addRow(new Object[]{produtoSelecionado.getId(), produtoSelecionado.getName(), "10", "15"});
        
        
        } catch (Exception e) {
            System.out.println("Erro: " + e);
        }
       
       
     
    }//GEN-LAST:event_JlistaDeProdutosValueChanged

    private void jListaDeFiliaisValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jListaDeFiliaisValueChanged
       if(pedidoDeCompraTab.getSelectedIndex()==0){
           if(jListaDeFiliais.getSelectedIndex()>=0){
           Filial filial = (Filial)listaDeFiliais.getElementAt(jListaDeFiliais.getSelectedIndex());
           filialNovoPedidoCompra.setText(filial.toString());
           novoPedido.setFilial_id(filial.getId());
           }
       }
    }//GEN-LAST:event_jListaDeFiliaisValueChanged

    private void jListaDeFornecedoresValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jListaDeFornecedoresValueChanged
      if(pedidoDeCompraTab.getSelectedIndex()==0){
          if(jListaDeFornecedores.getSelectedIndex()>=0){
            Fornecedor fornec = (Fornecedor)listaDeFornecedores.getElementAt(jListaDeFornecedores.getSelectedIndex());
            fornecedorNovoPedido.setText(fornec.toString());
            novoPedido.setFornecedorId(fornec.getId());
          }
      }
    }//GEN-LAST:event_jListaDeFornecedoresValueChanged

    private void pedidoDeCompraTabStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_pedidoDeCompraTabStateChanged
        limparTela();
        desabilitaTela();
        if(pedidoDeCompraTab.getSelectedIndex()==1){
            // pedidoDao.pedidosPorSemana(0, 0);
            buscaPedidosDaSemana();
        }
    }//GEN-LAST:event_pedidoDeCompraTabStateChanged

    private void exportarParaExcelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportarParaExcelButtonActionPerformed
        JFileChooser salvarArquivo = new JFileChooser();
        int opcao = salvarArquivo.showSaveDialog(relatorioPanel);
        if(opcao ==0){
            File caminhoDoArquivo = salvarArquivo.getSelectedFile();
            try {
                Planilha.exportarParaPlanilha(tabelaDePedidosPorSemana, caminhoDoArquivo.getAbsolutePath());
            } catch (FileNotFoundException ex) {
                Logger.getLogger(PedidoDeCompraView.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_exportarParaExcelButtonActionPerformed

    private void imprimirRelatorioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_imprimirRelatorioButtonActionPerformed
        Connection con = dbUtils.conectarAoDB();
        String relatorio = "C:\\Users\\bssil\\OneDrive\\Documentos\\NetBeansProjects\\copiarArquivosJava\\sistemaPedidos\\src\\main\\java\\relatorios\\pedidodecompra.jasper";
        try {
            HashMap filtro = new HashMap();
            filtro.put("numped", Integer.valueOf(idPedidoTextField.getText()));
            JasperPrint print = JasperFillManager.fillReport(relatorio, filtro, con);
            JasperViewer.viewReport(print, false);

        } catch (Exception e) {
            System.out.println(e);
        }
    }//GEN-LAST:event_imprimirRelatorioButtonActionPerformed

    private void tabelaDePedidosPorSemanaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelaDePedidosPorSemanaMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tabelaDePedidosPorSemanaMouseClicked

    private void cancelarNovoPedidoButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelarNovoPedidoButtonActionPerformed
        if(pedidoDao.cancelaPedido(novoPedido.getNumped(), usuarioLogado)){
            JOptionPane.showMessageDialog(rootPane, "Pedido " + novoPedido.getNumped() + " cancelado");
            limparTela();
            desabilitaTela();
        }
    }//GEN-LAST:event_cancelarNovoPedidoButtonActionPerformed

    private void obs1NovoPedidoTextAreaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_obs1NovoPedidoTextAreaFocusLost
        pedidoDao.atualizaObs1Pedido(novoPedido.getNumped(), obs1NovoPedidoTextArea.getText());
    }//GEN-LAST:event_obs1NovoPedidoTextAreaFocusLost

    private void obs2NovoPedidoTextAreaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_obs2NovoPedidoTextAreaFocusLost
        pedidoDao.atualizaObs2Pedido(novoPedido.getNumped(), obs2NovoPedidoTextArea.getText());
    }//GEN-LAST:event_obs2NovoPedidoTextAreaFocusLost

    private void adicionarProdutoButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_adicionarProdutoButtonActionPerformed
        try {
            Produto produtoSelecionado = (Produto)listaDeProdutos.getElementAt(JlistaDeProdutos.getSelectedIndex());
            listaDeProdutosDoPedido.add(produtoSelecionado);
            BigDecimal valor = new BigDecimal(valorProdutoTextField.getText().replace(".","").replace(",","."));
            pedidoDao.adicionarItemAoPedido(
                produtoSelecionado.getId(),
                novoPedido.getNumped(),
                valor,
                Integer.parseInt(qtProdutoTextField.getText()));
            List<Produto> produtos = new ArrayList<>();
            produtos = pedidoDao.produtosDoPedido(novoPedido.getNumped());
            atualizarTabelaProdutos(novoPedido.getNumped());
            /*PedidoDeCompra ped = pedidoDao.buscaPedidoDeCompra(Integer.parseInt(pedidoIDTextField.getText()));
            this.valorTotalPedidoTextField.setText(ped.getValor().toString());
            System.out.println(ped.getValor());
            System.out.println(ped.getQtItens());

            this.qtItensPedidoTextField.setText(Integer.toString(ped.getQtItens()));*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_adicionarProdutoButtonActionPerformed

    private void qtProdutoTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_qtProdutoTextFieldActionPerformed

    }//GEN-LAST:event_qtProdutoTextFieldActionPerformed

    private void produtoTextFieldKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_produtoTextFieldKeyTyped

    }//GEN-LAST:event_produtoTextFieldKeyTyped

    private void produtoTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_produtoTextFieldKeyReleased

        String produtoAPesquisar = produtoTextField.getText().trim();
        if(!produtoAPesquisar.equals("")){
            buscaProdutos(produtoAPesquisar);
            popUpMenu.show(produtoTextField, 0, produtoTextField.getHeight());
        }
    }//GEN-LAST:event_produtoTextFieldKeyReleased

    private void produtoTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_produtoTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_produtoTextFieldActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        if(evt.getButton()==3){
            int linhaSelecionada = jTable1.getSelectedRow();
            String produtoSelecionado = tabelaDeProdutos.getValueAt(linhaSelecionada, 1).toString();
            int produto_id = (int)tabelaDeProdutos.getValueAt(linhaSelecionada, 0);
            String messagem = "Deseja remover o produto " + produtoSelecionado + " do pedido?";
            int numPed = Integer.parseInt(pedidoIDTextField.getText());
            BigDecimal valor = (BigDecimal)tabelaDeProdutos.getValueAt(linhaSelecionada, 3);
            int opcao = JOptionPane.showConfirmDialog(rootPane, messagem);

            System.out.println(opcao);
            if(opcao == 0){
                System.out.println("Remover o produto " + produtoSelecionado + " ID: " + produto_id + " do pedido: "  +numPed);
                pedidoDao.removeItemDoPedido(produto_id,numPed, valor);
                atualizarTabelaProdutos(numPed);

            }
        }
    }//GEN-LAST:event_jTable1MouseClicked

    private void finalizarPedidoButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_finalizarPedidoButtonActionPerformed
        limparTela();
        desabilitaTela();

    }//GEN-LAST:event_finalizarPedidoButtonActionPerformed

    private void jComboBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox1ItemStateChanged

    }//GEN-LAST:event_jComboBox1ItemStateChanged

    private void fornecedorNovoPedidoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_fornecedorNovoPedidoKeyReleased
        buscaFornecedores(fornecedorNovoPedido.getText());
        jPopUpFornecedor.show(fornecedorNovoPedido, 0, fornecedorNovoPedido.getHeight());
    }//GEN-LAST:event_fornecedorNovoPedidoKeyReleased

    private void filialNovoPedidoCompraKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_filialNovoPedidoCompraKeyReleased
        buscaFiliais(filialNovoPedidoCompra.getText());
        jPopUpFiliais.show(filialNovoPedidoCompra, 0, filialNovoPedidoCompra.getHeight());
    }//GEN-LAST:event_filialNovoPedidoCompraKeyReleased

    private void filialNovoPedidoCompraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_filialNovoPedidoCompraActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_filialNovoPedidoCompraActionPerformed

    private void novoPedidoDeCompraButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_novoPedidoDeCompraButtonActionPerformed

        if(novoPedido.getFornecedorId() >0 && novoPedido.getFilial_id()>0){
            if(jComboBox1.getSelectedIndex() >0){
                try {
                    Usuario usuarioAutorizador = (Usuario)jComboBox1.getSelectedItem();
                    novoPedido.setUsuarioAutorizador(usuarioAutorizador);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(rootPane, e);
                }

            }else{
                jComboBox1.setSelectedIndex(-1);
            }
            try {
                this.novoPedido = pedidoDao.novoPedidoDeCompra(novoPedido, usuarioLogado);

                if(novoPedido.getNumped() >0){
                    habilitaTela();
                    pedidoIDTextField.setText(Integer.toString(novoPedido.getNumped()));
                    dataDoPedidoTextField.setText(novoPedido.getData());
                    qtItensPedidoTextField.setText("0");
                    valorTotalPedidoTextField.setText("0");
                }
                else{
                    JOptionPane.showMessageDialog(rootPane, "Não foi possível criar o pedido");
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(rootPane, e);
            }
        }else{
            JOptionPane.showMessageDialog(rootPane, "Fornecedor e filiais não informados");
        }

    }//GEN-LAST:event_novoPedidoDeCompraButtonActionPerformed

    private void editarPedidoButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editarPedidoButtonActionPerformed
        if(!pedidoAEditarTextField.getText().isEmpty()){     
        pedidoAEditar = pedidoDao.buscaPedidoDeCompra(Integer.parseInt(pedidoAEditarTextField.getText()));
       if(pedidoAEditar != null){
           System.out.println(pedidoAEditar.getNumped());
       }
        }
        pedidoAEditar = null;
    }//GEN-LAST:event_editarPedidoButtonActionPerformed

    
    private void limparTela(){
        tabelaDeProdutos.setRowCount(0);
        pedidoIDTextField.setText("");
        dataDoPedidoTextField.setText("");
        listaDeProdutos.setSize(0);
        qtItensPedidoTextField.setText("");
        valorProdutoTextField.setText(null);
        produtoTextField.setText(null);
        qtProdutoTextField.setText(null);
        valorTotalPedidoTextField.setText(null);
        obs1NovoPedidoTextArea.setText("");
        obs2NovoPedidoTextArea.setText("");
        jTabItensPedido.setSelectedIndex(0);
    }
    
    private void atualizarTabelaProdutos(int numPed){
       
      tabelaDeProdutos.setRowCount(0);
      List<Produto> listaDeProdutos = pedidoDao.produtosDoPedido(numPed);
      listaDeProdutos.stream().forEach((produto)->{
      tabelaDeProdutos.addRow(new Object[]{produto.getId(), produto.getName(),produto.getQuantidade(), produto.getValor(), produto.getObs(), produto.getValorTotal()});
      });    
     PedidoDeCompra ped = pedidoDao.buscaPedidoDeCompra(novoPedido.getNumped());
      this.valorTotalPedidoTextField.setText(ped.getValor().toString());
      this.qtItensPedidoTextField.setText(Integer.toString(ped.getQtItens()));
    }
    
    
    
    
    
    private void buscaListaDeAutorizadores(){
     List<Usuario> lista = new UsuarioDao().usuariosAutorizadores();
     jComboBox1.addItem(new Usuario());
     lista.stream().forEach((usuario)->{
       jComboBox1.addItem(usuario);
     });
     jComboBox1.setSelectedIndex(-1);
    }
    
    
    /**
     * 
     */
    public static void main(Usuario usuario) {
        try {
UIManager.setLookAndFeel( new FlatLightLaf() );
        } catch (Exception e) {
        }
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                
                new PedidoDeCompraView(usuario).setVisible(true);
                
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JList<String> JlistaDeProdutos;
    private javax.swing.JButton adicionarProdutoButton;
    private javax.swing.JButton cancelarNovoPedidoButton;
    private javax.swing.JButton cancelarPedidoEditadoButton;
    private javax.swing.JTextField dataDoPedidoTextField;
    private javax.swing.JButton editarPedidoButton;
    private javax.swing.JPanel editarPedidoPanel;
    private javax.swing.JButton exportarParaExcelButton;
    private javax.swing.JTextField filialNovoPedidoCompra;
    private javax.swing.JButton finalizarPedidoButton;
    private javax.swing.JTextField fornecedorNovoPedido;
    private javax.swing.JTextField idPedidoTextField;
    private javax.swing.JButton imprimirRelatorioButton;
    private javax.swing.JComboBox<Usuario> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JList<String> jListaDeFiliais;
    private javax.swing.JList<String> jListaDeFornecedores;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanelItensPedidos;
    private javax.swing.JPanel jPanelListaDeAutorizadores;
    private javax.swing.JPanel jPanelListaDeFornecedores;
    private javax.swing.JPanel jPanelListadeFiliais;
    private javax.swing.JPopupMenu jPopUpAutorizador;
    private javax.swing.JPopupMenu jPopUpFiliais;
    private javax.swing.JPopupMenu jPopUpFornecedor;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTabbedPane jTabItensPedido;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JList<String> jlistaDeAutoriazadores;
    private javax.swing.JButton novoPedidoDeCompraButton;
    private javax.swing.JTextArea obs1NovoPedidoTextArea;
    private javax.swing.JTextArea obs2NovoPedidoTextArea;
    private javax.swing.JTextField pedidoAEditarTextField;
    private javax.swing.JTextField pedidoAEditarTextField1;
    private javax.swing.JTextField pedidoAEditarTextField2;
    private javax.swing.JTextField pedidoAEditarTextField3;
    private javax.swing.JTextField pedidoAEditarTextField4;
    private javax.swing.JTextField pedidoAEditarTextField5;
    private javax.swing.JPanel pedidoDeCompraPanel;
    private javax.swing.JTabbedPane pedidoDeCompraTab;
    private javax.swing.JTextField pedidoIDTextField;
    private javax.swing.JPopupMenu popUpMenu;
    private javax.swing.JTextField produtoTextField;
    private javax.swing.JTextField qtItensPedidoTextField;
    private javax.swing.JTextField qtPedidosDaSemanaTextField;
    private javax.swing.JTextField qtProdutoTextField;
    private javax.swing.JPanel relatorioPanel;
    private javax.swing.JButton salvarPedidoEditadoButton;
    private javax.swing.JPanel searchBox;
    private javax.swing.JTextField semanaAtualTextField;
    private javax.swing.JTable tabelaDePedidosPorSemana;
    private javax.swing.JLabel usuarioLogadoLabel;
    private javax.swing.JFormattedTextField valorProdutoTextField;
    private javax.swing.JTextField valorTotalPedidoTextField;
    // End of variables declaration//GEN-END:variables
}
