/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author bssil
 */
public class Usuario {
    String nome;
    String login;
    String senha;
    String autorizaPedidos;
    String email;
    String fone;

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getAutorizaPedidos() {
        return autorizaPedidos;
    }

    public void setAutorizaPedidos(String autorizaPedidos) {
        this.autorizaPedidos = autorizaPedidos;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFone() {
        return fone;
    }

    public void setFone(String fone) {
        this.fone = fone;
    }
    int id;

    public Usuario(){
        
    }

    public Usuario(String nome, String login, int id) {
        this.nome = nome;
        this.login = login;
        this.id = id;
    }
    
    
    
    
    
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getLogin() {
        return login;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLogin(String login) {
        this.login = login;
    }
    
    
    @Override 
        public String toString(){
        return this.nome;
    }
}
