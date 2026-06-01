/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.tds.ecommerce;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author douglas
 */
public class ProdutoDAO {

    public void cadastrarProduto(Produto p) throws SQLException {
        String sql = "INSERT INTO produtos "
                + "(nome, descricao, categoria, preco, "
                + "quantidade, imagem, ativo) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = Conexao.conectar(); PreparedStatement stmt
                = conn.prepareStatement(sql)) {

            stmt.setString(1, p.getNome());
            stmt.setString(2, p.getDescricao());
            stmt.setString(3, p.getCategoria());
            stmt.setDouble(4, p.getPreco());
            stmt.setInt(5, p.getQuantidade());
            stmt.setString(6, p.getImagem());
            stmt.setBoolean(7, p.isAtivo());
            stmt.executeUpdate();
            System.out.println("Produto cadastrado com sucesso!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
