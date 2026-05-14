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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
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
 * @author aluno
 */
public class TelaGerenciamentoUsuariosController implements Initializable {

    @FXML
    private TableView<Usuario> tabelaUsuarios;

    @FXML
    private TableColumn<Usuario, String> colNomeCompleto;

    @FXML
    private TableColumn<Usuario, String> colUsuario;

    @FXML
    private TableColumn<Usuario, String> colEmail;

    @FXML
    private TableColumn<Usuario, String> colCPF;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            // TODO
            colNomeCompleto.setCellValueFactory(new PropertyValueFactory<>("nomeCompleto"));
            colUsuario.setCellValueFactory(new PropertyValueFactory<>("nomeUsuario"));
            colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
            colCPF.setCellValueFactory(new PropertyValueFactory<>("CPF"));
            carregarUsuarios();
        } catch (SQLException ex) {
            System.getLogger(TelaGerenciamentoUsuariosController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }

    @FXML
    private void abrirTelaCadastroUsuario() throws IOException {
         FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/br/edu/tds/ecommerce/TelaCadastro.fxml"));
            Parent root = loader.load();

            TelaCadastroUsuarioController controller = loader.getController();

            Stage stage = (Stage) tabelaUsuarios.getScene().getWindow();
            stage.setScene(new Scene(root));
    }

    private void carregarUsuarios() throws SQLException {

        System.out.println("Dentro do método carregarUsuarios()é");

        ObservableList<Usuario> listaUsuarios = FXCollections.observableArrayList();
        String sql = "SELECT * FROM usuarios";
        try (Connection conn = Conexao.conectar()) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Usuario u = new Usuario();
                u.setNomeCompleto(rs.getString("nomeCompleto"));
                u.setNomeUsuario(rs.getString("nomeUsuario"));
                u.setEmail(rs.getString("email"));
                u.setCPF(rs.getString("cpf"));
                u.setSenha(rs.getString("senha"));

                System.out.println("Usuário: " + u.getNomeCompleto());

                listaUsuarios.add(u);
                System.out.println("Nº de usuarios: " + listaUsuarios.size());
            }
            tabelaUsuarios.setItems(listaUsuarios);
        }
    }

    @FXML
    private void excluirUsuario() throws SQLException {

        Usuario uSelecionado = tabelaUsuarios.getSelectionModel().getSelectedItem();

        if (uSelecionado == null) {
            System.out.println("Selecione um usuário");
            return;
        }

        System.out.println("Usuario: " + uSelecionado.getNomeUsuario());

        String sql = "DELETE FROM usuarios WHERE nomeUsuario = ?";

        try (Connection conn = Conexao.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, uSelecionado.getNomeUsuario());
            stmt.executeUpdate();

            carregarUsuarios();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    private void editarUsuario() throws SQLException {

        Usuario uSelecionado = tabelaUsuarios.getSelectionModel().getSelectedItem();

        if (uSelecionado == null) {
            mostrarAlerta("Selecione um usuário");
            return;
        }
        try {

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/br/edu/tds/ecommerce/TelaCadastro.fxml"));
            Parent root = loader.load();

            TelaCadastroUsuarioController controller = loader.getController();

            controller.setUsuario(uSelecionado);

            Stage stage = (Stage) tabelaUsuarios.getScene().getWindow();
            stage.setScene(new Scene(root));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void mostrarAlerta(String msg) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sistema");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
    
    
}
