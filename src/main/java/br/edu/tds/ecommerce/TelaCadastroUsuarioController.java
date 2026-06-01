/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package br.edu.tds.ecommerce;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author douglas
 */
public class TelaCadastroUsuarioController implements Initializable {

    @FXML
    private TextField txtNomeCompleto;
    @FXML
    private TextField txtNomeUsuario;
    @FXML
    private PasswordField txtSenha;
    @FXML
    private TextField txtEmail;
    @FXML
    private TextField txtCPF;
    @FXML
    private ComboBox<String> cbRole;
    @FXML
    private Text lblTelaEditarUsuario;
    @FXML
    private Button btnCadastrar;

    private Usuario usuarioEdicao;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        cbRole.getItems().add(0, "cliente");
        cbRole.getItems().add(1, "admin");
    }

    @FXML
    private void abrirTelaLogin() throws IOException {
        System.out.println("Entrou no método login");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/edu/tds/ecommerce/telaLogin.fxml"));

        Parent root = loader.load();

        TelaLoginController controller = loader.getController();

        //Trocando de tela
        Stage stage = (Stage) txtNomeCompleto.getScene().getWindow();
        stage.setScene(new Scene(root));
    }

    @FXML
    private void cadastrarUsuario() throws IOException {

        if (usuarioEdicao == null) {
            inserirUsuario();
        } else {
            atualizarUsuario();
        }

    }

    @FXML
    private void inserirUsuario() throws IOException {

        String nomeCompleto = txtNomeCompleto.getText();
        String nomeUsuario = txtNomeUsuario.getText();
        String senha = txtSenha.getText();
        String email = txtEmail.getText();
        String cpf = txtCPF.getText();
        String role = cbRole.getValue();
        

        txtNomeCompleto.setStyle("-fx-background-color: transparent; -fx-border-color: #0598ff; -fx-border-width: 0 0 3 0;");
        txtNomeUsuario.setStyle("-fx-background-color: transparent; -fx-border-color: #0598ff; -fx-border-width: 0 0 3 0;");
        txtSenha.setStyle("-fx-background-color: transparent; -fx-border-color: #0598ff; -fx-border-width: 0 0 3 0;");
        txtEmail.setStyle("-fx-background-color: transparent; -fx-border-color: #0598ff; -fx-border-width: 0 0 3 0;");
        txtCPF.setStyle("-fx-background-color: transparent; -fx-border-color: #0598ff; -fx-border-width: 0 0 3 0;");

        if (nomeCompleto.isEmpty()) {
            txtNomeCompleto.setStyle("-fx-background-color: transparent; -fx-border-color: red; -fx-border-width: 0 0 3 0;");
        }
        if (nomeUsuario.isEmpty()) {
            txtNomeUsuario.setStyle("-fx-background-color: transparent; -fx-border-color: red; -fx-border-width: 0 0 3 0;");
        }
        if (senha.isEmpty()) {
            txtSenha.setStyle("-fx-background-color: transparent; -fx-border-color: red; -fx-border-width: 0 0 3 0;");
        }
        if (email.isEmpty()) {
            txtEmail.setStyle("-fx-background-color: transparent; -fx-border-color: red; -fx-border-width: 0 0 3 0;");
        }
        if (cpf.isEmpty()) {
            txtCPF.setStyle("-fx-background-color: transparent; -fx-border-color: red; -fx-border-width: 0 0 3 0;");
        }

        if (!(nomeCompleto.isEmpty() || nomeUsuario.isEmpty() || senha.isEmpty() || email.isEmpty() || cpf.isEmpty())) {
            //Cadastrando um usuário no BD
            UsuarioDAO dao = new UsuarioDAO();
            Usuario u = new Usuario(nomeCompleto, nomeUsuario, email, senha, cpf, role);
            dao.cadastrar(u);
            mostrarAlerta("O cadastro de " + u.getNomeCompleto() + " foi cadastrado com sucesso");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/edu/tds/ecommerce/telaGerenciamentoUsuarios.fxml"));

            Parent root = loader.load();

            TelaGerenciamentoUsuariosController controller = loader.getController();

            //Trocando de tela
            Stage stage = (Stage) txtNomeCompleto.getScene().getWindow();
            stage.setScene(new Scene(root));

        }

    }

    @FXML
    private void atualizarUsuario() throws IOException {

        String nomeCompleto = txtNomeCompleto.getText();
        String nomeUsuario = txtNomeUsuario.getText();
        String senha = txtSenha.getText();
        String email = txtEmail.getText();
        String cpf = txtCPF.getText();
        String role = cbRole.getValue();

        txtNomeCompleto.setStyle("-fx-background-color: transparent; -fx-border-color: #0598ff; -fx-border-width: 0 0 3 0;");
        txtNomeUsuario.setStyle("-fx-background-color: transparent; -fx-border-color: #0598ff; -fx-border-width: 0 0 3 0;");
        txtSenha.setStyle("-fx-background-color: transparent; -fx-border-color: #0598ff; -fx-border-width: 0 0 3 0;");
        txtEmail.setStyle("-fx-background-color: transparent; -fx-border-color: #0598ff; -fx-border-width: 0 0 3 0;");
        txtCPF.setStyle("-fx-background-color: transparent; -fx-border-color: #0598ff; -fx-border-width: 0 0 3 0;");

        if (nomeCompleto.isEmpty()) {
            txtNomeCompleto.setStyle("-fx-background-color: transparent; -fx-border-color: red; -fx-border-width: 0 0 3 0;");
        }
        if (nomeUsuario.isEmpty()) {
            txtNomeUsuario.setStyle("-fx-background-color: transparent; -fx-border-color: red; -fx-border-width: 0 0 3 0;");
        }
        if (senha.isEmpty()) {
            txtSenha.setStyle("-fx-background-color: transparent; -fx-border-color: red; -fx-border-width: 0 0 3 0;");
        }
        if (email.isEmpty()) {
            txtEmail.setStyle("-fx-background-color: transparent; -fx-border-color: red; -fx-border-width: 0 0 3 0;");
        }
        if (cpf.isEmpty()) {
            txtCPF.setStyle("-fx-background-color: transparent; -fx-border-color: red; -fx-border-width: 0 0 3 0;");
        }

        if (!(nomeCompleto.isEmpty() || nomeUsuario.isEmpty() || senha.isEmpty() || email.isEmpty() || cpf.isEmpty())) {
            //Atualizando um usuário no BD
            UsuarioDAO dao = new UsuarioDAO();
            Usuario u = new Usuario(nomeCompleto, nomeUsuario, email, senha, cpf, role);
            System.out.println("Nome: " + u.getNomeCompleto());
            System.out.println("Usuario: " + u.getNomeUsuario());
            System.out.println("Email: " + u.getEmail());
            System.out.println("Senha: " + u.getSenha());
            System.out.println("CPF: " + u.getCpf());
            dao.atualizar(u);
            mostrarAlerta("O cadastro de " + u.getNomeCompleto() + " foi atualizado com sucesso");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/edu/tds/ecommerce/telaGerenciamentoUsuarios.fxml"));

            Parent root = loader.load();

            TelaGerenciamentoUsuariosController controller = loader.getController();

            //Trocando de tela
            Stage stage = (Stage) txtNomeCompleto.getScene().getWindow();
            stage.setScene(new Scene(root));
        }

    }

    public void setUsuario(Usuario u) {

        usuarioEdicao = u;
        //Estou colocando informações vindas do BD
        //no formulário de edição
        txtNomeCompleto.setText(u.getNomeCompleto());
        txtNomeUsuario.setText(u.getNomeUsuario());
        txtSenha.setText(u.getSenha());
        txtEmail.setText(u.getEmail());
        txtCPF.setText(u.getCpf());
        cbRole.setValue(u.getRole());

        lblTelaEditarUsuario.setText("Atualizar conta de usuário");
        btnCadastrar.setText("Salvar");
    }

    private void mostrarAlerta(String msg) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sistema");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

}
