/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package br.edu.tds.ecommerce;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author douglas
 */
public class TelaGerenciamentoProdutosController implements Initializable {

    @FXML
    private TableView<Produto> tabelaProdutos;

    @FXML
    private TableColumn<Produto, String> colNome;

    @FXML
    private TableColumn<Produto, String> colCategoria;

    @FXML
    private TableColumn<Produto, Double> colPreco;

    @FXML
    private TableColumn<Produto, Integer> colQuantidade;

    @FXML
    private TableColumn<Produto, Boolean> colAtivo;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        colNome.setCellValueFactory(new PropertyValueFactory<>("Nome"));
        colCategoria.setCellValueFactory(new PropertyValueFactory<>("Categoria"));
        colPreco.setCellValueFactory(new PropertyValueFactory<>("Preco"));
        colQuantidade.setCellValueFactory(new PropertyValueFactory<>("Quantidade"));
        colAtivo.setCellValueFactory(new PropertyValueFactory<>("Ativo"));

        carregarProdutos();
    }

    private void carregarProdutos() {

        ObservableList<Produto> listaProdutos = FXCollections.observableArrayList();

        String sql = "SELECT * FROM produtos";
        try (Connection conn = Conexao.conectar()) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Produto p = new Produto();
                p.setId(Integer.parseInt(rs.getString("id")));
                p.setNome(rs.getString("nome"));
                p.setCategoria(rs.getString("categoria"));
                p.setPreco(Double.parseDouble(rs.getString("preco")));
                p.setQuantidade(Integer.parseInt(rs.getString("quantidade")));
                p.setImagem(rs.getString("imagem"));
                p.setAtivo(rs.getString("ativo").equals("0") ? false : true);
                p.setDescricao(rs.getString("descricao"));

                System.out.println("Ativo: " + rs.getString("ativo"));
                listaProdutos.add(p);
            }

            tabelaProdutos.setItems(listaProdutos);

        } catch (Exception e) {
        }
    }

    @FXML
    private void abrirTelaCadastroProduto() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/edu/tds/ecommerce/telaCadastroProdutos.fxml"));

        Parent root = loader.load();

        TelaCadastroProdutosController controller = loader.getController();

        //Trocando de tela
        Stage stage = (Stage) tabelaProdutos.getScene().getWindow();
        stage.setScene(new Scene(root));
    }

    @FXML
    private void excluirProduto() throws SQLException {

        Produto pSelecionado = tabelaProdutos.getSelectionModel().getSelectedItem();

        if (pSelecionado == null) {
            mostrarAlerta("Selecione um produto");
            return;
        }

        String sql = "DELETE FROM produtos WHERE id = ?";

        try (Connection conn = Conexao.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, String.valueOf(pSelecionado.getId()));
            stmt.executeUpdate();

            carregarProdutos();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void editarProduto() throws SQLException {

        Produto pSelecionado = tabelaProdutos.getSelectionModel().getSelectedItem();

        if (pSelecionado == null) {
            mostrarAlerta("Selecione um produto");
            return;
        }

        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/edu/tds/ecommerce/telaCadastroProdutos.fxml"));

            Parent root = loader.load();

            TelaCadastroProdutosController controller = loader.getController();

            //Envia os dados da tela Gerenciamento de Usuarios
            //para o controlador de Cadastro de Usuarios
            controller.setProduto(pSelecionado);

            //Trocando de tela
            Stage stage = (Stage) tabelaProdutos.getScene().getWindow();
            stage.setScene(new Scene(root));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void mostrarAlerta(String msg) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sistema");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

}
