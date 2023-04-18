/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package view;

import com.formdev.flatlaf.FlatLightLaf;
import dbUtils.UsuarioDao;
import java.awt.Component;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import model.Usuario;

/**
 *
 * @author bssil
 */
public class CadastroDeUsuarioView extends javax.swing.JFrame {

    /**
     * Creates new form CadastroDeUsuarioView
     * @param usuarioLogado
     */
    public CadastroDeUsuarioView(Usuario usuarioLogado) {
        initComponents();
        this.usuarioLogado = usuarioLogado;
        this.usuarioLogadoLabel.setText(usuarioLogado.getNome());
        buscarUsuarios();
    }

    Usuario usuarioLogado;
    UsuarioDao usuarioDao = new UsuarioDao();
    DefaultTableModel tabelaDeUsuarioModel = new DefaultTableModel();
    
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        usuarioLogadoLabel = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        usuarioAFiltrarTextField = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabelaUsuariosJTable = new javax.swing.JTable();
        cadastroUsuarioPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        nomeUsuarioCadastrarTextField = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        loginUsuarioCadastrarTextField = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        senhaUsuarioCadastrarTextField = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        emailUsuarioCadastrarTextField = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        foneUsuarioCadastrarTextField = new javax.swing.JTextField();
        autorizaPedidosCadastrarTextField = new javax.swing.JCheckBox();
        salvarCadastroDeUsuarioButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(0, 153, 204));

        usuarioLogadoLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/user.png"))); // NOI18N
        usuarioLogadoLabel.setText("usuarioLogado");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(usuarioLogadoLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(usuarioLogadoLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jTabbedPane1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jTabbedPane1StateChanged(evt);
            }
        });

        usuarioAFiltrarTextField.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        usuarioAFiltrarTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                usuarioAFiltrarTextFieldKeyReleased(evt);
            }
        });

        jLabel6.setText("PESQUISAR");

        tabelaUsuariosJTable.setModel(tabelaDeUsuarioModel);
        jScrollPane1.setViewportView(tabelaUsuariosJTable);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(usuarioAFiltrarTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 859, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(77, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(usuarioAFiltrarTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Usuarios", jPanel2);

        jLabel1.setText("NOME");

        nomeUsuarioCadastrarTextField.setHorizontalAlignment(javax.swing.JTextField.LEFT);

        jLabel2.setText("LOGIN");

        loginUsuarioCadastrarTextField.setHorizontalAlignment(javax.swing.JTextField.LEFT);

        jLabel3.setText("SENHA");

        senhaUsuarioCadastrarTextField.setHorizontalAlignment(javax.swing.JTextField.LEFT);

        jLabel4.setText("EMAIL");

        emailUsuarioCadastrarTextField.setHorizontalAlignment(javax.swing.JTextField.LEFT);

        jLabel5.setText("FONE");

        foneUsuarioCadastrarTextField.setHorizontalAlignment(javax.swing.JTextField.LEFT);

        autorizaPedidosCadastrarTextField.setText("Autoriza pedidos");
        autorizaPedidosCadastrarTextField.setToolTipText("Esse usuário poderá ser atribuído autorizador ao cadastrar um pedido.");

        salvarCadastroDeUsuarioButton.setText("Salvar");
        salvarCadastroDeUsuarioButton.setToolTipText("");
        salvarCadastroDeUsuarioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                salvarCadastroDeUsuarioButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout cadastroUsuarioPanelLayout = new javax.swing.GroupLayout(cadastroUsuarioPanel);
        cadastroUsuarioPanel.setLayout(cadastroUsuarioPanelLayout);
        cadastroUsuarioPanelLayout.setHorizontalGroup(
            cadastroUsuarioPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cadastroUsuarioPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(cadastroUsuarioPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(cadastroUsuarioPanelLayout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(nomeUsuarioCadastrarTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 667, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(cadastroUsuarioPanelLayout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(loginUsuarioCadastrarTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(senhaUsuarioCadastrarTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(cadastroUsuarioPanelLayout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(emailUsuarioCadastrarTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 412, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(foneUsuarioCadastrarTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(autorizaPedidosCadastrarTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(308, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, cadastroUsuarioPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(salvarCadastroDeUsuarioButton)
                .addGap(161, 161, 161))
        );
        cadastroUsuarioPanelLayout.setVerticalGroup(
            cadastroUsuarioPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cadastroUsuarioPanelLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(cadastroUsuarioPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(nomeUsuarioCadastrarTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(cadastroUsuarioPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(loginUsuarioCadastrarTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(senhaUsuarioCadastrarTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(cadastroUsuarioPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(emailUsuarioCadastrarTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(foneUsuarioCadastrarTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(autorizaPedidosCadastrarTextField)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(salvarCadastroDeUsuarioButton)
                .addContainerGap(316, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Cadastrar usuário", cadastroUsuarioPanel);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jTabbedPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void salvarCadastroDeUsuarioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_salvarCadastroDeUsuarioButtonActionPerformed
      if(checaCadastroUsuario()){
       Usuario usuario = new Usuario();
       usuario.setNome(nomeUsuarioCadastrarTextField.getText());
       usuario.setLogin(loginUsuarioCadastrarTextField.getText());
       usuario.setSenha(senhaUsuarioCadastrarTextField.getText());
       usuario.setEmail("");
       usuario.setFone("123");
       if(autorizaPedidosCadastrarTextField.isSelected()){
       usuario.setAutorizaPedidos("S");
       }else{
        usuario.setAutorizaPedidos("N");   
       }
          System.out.println(usuario.getNome());
       if(usuarioDao.cadastrarUsuario(usuario)){
        JOptionPane.showMessageDialog(rootPane, "Usuário " + usuario.getLogin() + " cadastrado.");
        limpaCamposCadastroUsuario();
       }
      }
    }//GEN-LAST:event_salvarCadastroDeUsuarioButtonActionPerformed

    private void usuarioAFiltrarTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_usuarioAFiltrarTextFieldKeyReleased
       final TableRowSorter<TableModel> sorter = new TableRowSorter<>(tabelaUsuariosJTable.getModel());
      tabelaUsuariosJTable.setRowSorter(sorter);
       try {
            sorter.setRowFilter(RowFilter.regexFilter(usuarioAFiltrarTextField.getText()));
        } catch (Exception e) {
        }
    }//GEN-LAST:event_usuarioAFiltrarTextFieldKeyReleased

    private void jTabbedPane1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jTabbedPane1StateChanged
       if(jTabbedPane1.getSelectedIndex()==0){
        buscarUsuarios();
       }
    }//GEN-LAST:event_jTabbedPane1StateChanged

    
    private boolean checaCadastroUsuario(){
        if(!nomeUsuarioCadastrarTextField.getText().isEmpty() 
         &&  !loginUsuarioCadastrarTextField.getText().isEmpty()
         && !senhaUsuarioCadastrarTextField.getText().isEmpty()){
          return true;  
        }else{
            JOptionPane.showMessageDialog(rootPane, "Nome, login e senha são obrigatórios!");  
            return false;
            
        }
    }
    
    
    private void limpaCamposCadastroUsuario(){
       Component[] componentes = cadastroUsuarioPanel.getComponents();
       for(Component c: componentes){
         if( c instanceof JTextField){
            ((JTextField) c).setText("");
         }  
       }
    }
    
    private void buscarUsuarios(){
      tabelaDeUsuarioModel.setRowCount(0);
      tabelaDeUsuarioModel.setColumnCount(0);
        List<Usuario> listaDeUsuario = usuarioDao.buscarUsuarios("");
      tabelaDeUsuarioModel.addColumn("ID");
      tabelaDeUsuarioModel.addColumn("LOGIN");
      tabelaDeUsuarioModel.addColumn("NOME");
      tabelaDeUsuarioModel.addColumn("AUTORIZA PEDIDOS");
      tabelaDeUsuarioModel.addColumn("EMAIL");
      tabelaDeUsuarioModel.addColumn("FONE");
      listaDeUsuario.stream().forEach((usuario)->{
       tabelaDeUsuarioModel.addRow(new Object[]{
       usuario.getId(),
       usuario.getLogin(),
       usuario.getNome(),
       usuario.getAutorizaPedidos(),
       usuario.getEmail(),
       usuario.getFone()
       });
      });
     
              
    }
    
    
    /**
     * @param usuarioLogado
     * @param args the command line arguments
     */
    public static void main(Usuario usuarioLogado) {
          try {
UIManager.setLookAndFeel( new FlatLightLaf() );
        } catch (Exception e) {
        }
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new CadastroDeUsuarioView(usuarioLogado).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox autorizaPedidosCadastrarTextField;
    private javax.swing.JPanel cadastroUsuarioPanel;
    private javax.swing.JTextField emailUsuarioCadastrarTextField;
    private javax.swing.JTextField foneUsuarioCadastrarTextField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField loginUsuarioCadastrarTextField;
    private javax.swing.JTextField nomeUsuarioCadastrarTextField;
    private javax.swing.JButton salvarCadastroDeUsuarioButton;
    private javax.swing.JTextField senhaUsuarioCadastrarTextField;
    private javax.swing.JTable tabelaUsuariosJTable;
    private javax.swing.JTextField usuarioAFiltrarTextField;
    private javax.swing.JLabel usuarioLogadoLabel;
    // End of variables declaration//GEN-END:variables
}
