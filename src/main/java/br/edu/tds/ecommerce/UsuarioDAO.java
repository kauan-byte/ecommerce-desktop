    /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.tds.ecommerce;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author douglas
 */
public class UsuarioDAO {

    //Cadastra usuários
    public void cadastrar(Usuario usuario) {
        String sql = "INSERT INTO usuarios (nomeCompleto, nomeUsuario, email, senha, cpf, role) VALUES (?,?,?,?,?,?)";
        try (Connection conn = Conexao.conectar()) {
            PreparedStatement stmt = conn.prepareCall(sql);
            stmt.setString(1, usuario.getNomeCompleto());
            stmt.setString(2, usuario.getNomeUsuario());
            stmt.setString(3, usuario.getEmail());
            stmt.setString(4, usuario.getSenha());
            stmt.setString(5, usuario.getCpf());
            stmt.setString(6, usuario.getRole());
            stmt.executeUpdate();
            System.out.println("Usuário " + usuario.getNomeCompleto() + " foi cadastrado com sucesso!");
        } catch (Exception e) {
            System.out.println("Erro ao cadastrar: " + e.getMessage());
        }
    }

    //Testa o login de usuário
    public boolean login(String email, String senha) {
        System.out.println("email: " + email);
        System.out.println("senha: " + senha);
        String sql = "SELECT * FROM usuarios WHERE email = ? AND senha = ?";
        try (Connection conn = Conexao.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            stmt.setString(2, senha);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (Exception e) {
            System.out.println("Erro no Login: " + e.getMessage());
            return false;
        }
    }

    void atualizar(Usuario u) {
        String sql = "UPDATE usuarios SET nomeCompleto=?, nomeUsuario=?, email=?, senha=?, cpf=?, role=? WHERE nomeUsuario=?";
        try (Connection conn = Conexao.conectar();PreparedStatement stmt = conn.prepareCall(sql)){
            System.out.println("daoNome: " + u.getNomeCompleto());
            System.out.println("daoUsuario: " + u.getNomeUsuario());
            System.out.println("daoEmail: " + u.getEmail());
            System.out.println("daoSenha: " + u.getSenha());
            System.out.println("daoCPF: " + u.getCpf());
            
            
            stmt.setString(1, u.getNomeCompleto());
            stmt.setString(2, u.getNomeUsuario());
            stmt.setString(3, u.getEmail());
            stmt.setString(4, u.getSenha());
            stmt.setString(5, u.getCpf());
            stmt.setString(6, u.getRole());
            stmt.setString(7, u.getNomeUsuario());
            
            System.out.println(stmt.executeUpdate());
            System.out.println("Usuário " + u.getNomeCompleto() + " foi atualizado com sucesso!");
        } catch (Exception e) {
            System.out.println("Erro ao cadastrar: " + e.getMessage());
        }
    }
}
