package br.edu.tds.ecommerce;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Label;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class TelaCadastroUsuarioController implements Initializable {

    @FXML
    private TextField txtEmail;
    @FXML
    private TextField txtNomeCompleto;
    @FXML
    private TextField txtUserName;
    @FXML
    private PasswordField txtSenha;
    @FXML
    private TextField txtCpf;

    @FXML
    private Label lblEmail;
    @FXML
    private Label lblNomeCompleto;
    @FXML
    private Label lblUserName;
    @FXML
    private Label lblSenha;
    @FXML
    private Label lblCpf;
    @FXML
    private Text lblTelaEditarUsuario;
    @FXML
    private Button btnCadastrar;
    private Usuario usuarioEdicao;

    @FXML
    private void cadastrarUsuario() throws IOException {
        if (usuarioEdicao == null) {
            inserirUsuario();
        } else {
            atualizarUsuario();
        }
    }

    @FXML
    private void inserirUsuario() {

        limparMensagensErro();

        String nomeCompleto = txtNomeCompleto.getText().trim();
        String nomeUsuario = txtUserName.getText().trim();
        String email = txtEmail.getText().trim();
        String CPF = txtCpf.getText().trim();
        String senha = txtSenha.getText().trim();

        boolean temErro = false;

        if (nomeCompleto.isEmpty()) {
            lblNomeCompleto.setText("O nome completo é obrigatório.");
            temErro = true;
        }
        if (nomeUsuario.isEmpty()) {
            lblUserName.setText("O nome de usuário é obrigatório.");
            temErro = true;
        }
        if (email.isEmpty()) {
            lblEmail.setText("O e-mail é obrigatório.");
            temErro = true;
        }
        if (CPF.isEmpty()) {
            lblCpf.setText("O CPF é obrigatório.");
            temErro = true;
        }
        if (senha.isEmpty()) {
            lblSenha.setText("A senha é obrigatória.");
            temErro = true;
        }

        if (temErro) {
            return;
        }

        UsuarioDAO dao = new UsuarioDAO();
        Usuario u = new Usuario(nomeCompleto, nomeUsuario, email, senha, CPF);

        try {
            dao.cadastrar(u);
            mostrarAlerta("Sucesso", "Usuário cadastrado com sucesso!");
            limparCampos();
        } catch (Exception e) {
            mostrarAlerta("Erro", "Falha ao conectar com o banco: " + e.getMessage());
        }
    }
    
    @FXML
    private void atualizarUsuario() throws IOException {

        limparMensagensErro();

        String nomeCompleto = txtNomeCompleto.getText().trim();
        String nomeUsuario = txtUserName.getText().trim();
        String email = txtEmail.getText().trim();
        String CPF = txtCpf.getText().trim();
        String senha = txtSenha.getText().trim();

        boolean temErro = false;

        if (nomeCompleto.isEmpty()) {
            lblNomeCompleto.setText("O nome completo é obrigatório.");
            temErro = true;
        }
        if (nomeUsuario.isEmpty()) {
            lblUserName.setText("O nome de usuário é obrigatório.");
            temErro = true;
        }
        if (email.isEmpty()) {
            lblEmail.setText("O e-mail é obrigatório.");
            temErro = true;
        }
        if (CPF.isEmpty()) {
            lblCpf.setText("O CPF é obrigatório.");
            temErro = true;
        }
        if (senha.isEmpty()) {
            lblSenha.setText("A senha é obrigatória.");
            temErro = true;
        }

        if (temErro) {
            return;
        }
        
        
        UsuarioDAO dao = new UsuarioDAO();
        Usuario u = new Usuario(nomeCompleto, nomeUsuario, email, senha, CPF);
        dao.atualizar(u);
        mostrarAlerta("O cadastro de "+ u.getNomeCompleto() + "foi atualizado com sucesso");
         FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/br/edu/tds/ecommerce/telaGerenciamentoUsuarios.fxml"));
            Parent root = loader.load();

            TelaGerenciamentoUsuariosController controller = loader.getController();

            Stage stage = (Stage) txtNomeCompleto.getScene().getWindow();
            stage.setScene(new Scene(root));


        try {
            dao.atualizar(u);
            mostrarAlerta("Sucesso", "Usuário cadastrado com sucesso!");
            limparCampos();
        } catch (Exception e) {
            mostrarAlerta("Erro", "Falha ao conectar com o banco: " + e.getMessage());
        }
        
    }

    @FXML
    private void abrirTelaLogin(ActionEvent event) throws IOException {   
       FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/br/edu/tds/ecommerce/TelaLogin.fxml"));
            Parent root = loader.load();

            TelaLoginController controller = loader.getController();

            Stage stage = (Stage) txtNomeCompleto.getScene().getWindow();
            stage.setScene(new Scene(root));
    }

    private void mostrarAlerta(String titulo, String mensagem) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private void limparMensagensErro() {
        lblNomeCompleto.setText("");
        lblUserName.setText("");
        lblEmail.setText("");
        lblCpf.setText("");
        lblSenha.setText("");
    }

    private void limparCampos() {
        txtNomeCompleto.clear();
        txtUserName.clear();
        txtEmail.clear();
        txtCpf.clear();
        txtSenha.clear();
        limparMensagensErro();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        limparMensagensErro();
    }

    public void setUsuario(Usuario u) {
        usuarioEdicao = u;
        txtNomeCompleto.setText(u.getNomeCompleto());
        txtUserName.setText(u.getNomeUsuario());
        txtSenha.setText(u.getSenha());
        txtEmail.setText(u.getEmail());
        txtCpf.setText(u.getCPF());

        lblTelaEditarUsuario.setText("Atualizar conta de usuario");
        btnCadastrar.setText("Atualizar");

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
