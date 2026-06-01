package br.edu.tds.ecommerce;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class TelaLoginController {

    @FXML
    private TextField txtUsuario;
    @FXML
    private PasswordField txtSenha;
    @FXML
    private Label lblUsuario;
<<<<<<< HEAD
    @FXML 
=======
    @FXML
>>>>>>> e08e4b9 (codigo com dashboard)
    private Label lblSenha;

    @FXML
    private void abrirTelaCadastroUsuario() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/edu/tds/ecommerce/telaCadastroUsuario.fxml"));

        Parent root = loader.load();

        TelaCadastroUsuarioController controller = loader.getController();

        //Trocando de tela 
        Stage stage = (Stage) txtUsuario.getScene().getWindow();
        stage.setScene(new Scene(root));
    }

    @FXML
    private void realizarLogin() throws IOException {
<<<<<<< HEAD
       String usuario = txtUsuario.getText();
       String senha = txtSenha.getText();
       
       if(usuario.isEmpty() && senha.isEmpty()){
           lblUsuario.setText("*Campo usuario é obrigatorio");
           lblSenha.setText("*Campo senha é obrigatorio");
           
           System.out.println("Campo usuário e senha são obrigatórios");
           return;
       }
       
       if(usuario.isEmpty()){
           lblUsuario.setText("* Campo usuario é obrigatorio");
           lblSenha.setText("");
           System.out.println("Campo usuario é obrigatório");
           return;
       }
       if(senha.isEmpty()){
           lblUsuario.setText("");
           lblSenha.setText("*Campo senha é obrigatorio");
           System.out.println("Campo senha é obrigatório");
           return;
       }
       
       lblUsuario.setText("");
       lblSenha.setText("");
       
       UsuarioDAO dao = new UsuarioDAO();
       Boolean login = dao.login(usuario, senha);
       
       if(login){
            // Login com sucesso
            mostrarAlerta("Login bem sucedido");
            System.out.println("Login feito");
            
            // ADAPTAÇÃO: Carrega o dashboard.fxml
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/br/edu/tds/ecommerce/dashboard.fxml")
            );
=======

        String usuario = txtUsuario.getText();
        String senha = txtSenha.getText();

        if (usuario.isEmpty() && senha.isEmpty()) {
            lblUsuario.setText("* Campo usuário é obrigatório");
            lblSenha.setText("* Campo senha é obrigatório");
            System.out.println("Campo usuário e campo senha são obrigatórios");
            return;
        }

        if (usuario.isEmpty()) {
            lblUsuario.setText("* Campo usuário é obrigatório");
            lblSenha.setText("");
            System.out.println("Campo usuário é obrigatório");
            return;
        }
        if (senha.isEmpty()) {
            lblUsuario.setText("");
            lblSenha.setText("* Campo senha é obrigatório");
            System.out.println("Campo senha é obrigatório");
            return;
        }

        lblUsuario.setText("");
        lblSenha.setText("");

        UsuarioDAO dao = new UsuarioDAO();
        Boolean login = dao.login(usuario, senha);
       

        if (login) {
            //Login com sucesso
            System.out.println("Login feito");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/edu/tds/ecommerce/telaGerenciamentoUsuarios.fxml"));

>>>>>>> e08e4b9 (codigo com dashboard)
            Parent root = loader.load();

            // Vincula ao DashboardController profissional
            DashboardController controller = loader.getController();

            //Trocando de tela
            Stage stage = (Stage) txtUsuario.getScene().getWindow();
            stage.setScene(new Scene(root));
            
<<<<<<< HEAD
            // MAXIMIZA DASHBOARD (Deixa Fullscreen)
            stage.setMaximized(true);
            stage.show();
            
       } else {
            // Falha no login (usuario ou senha invalido)
            lblUsuario.setText("Usuário/Senha incorreto(a)");
            lblSenha.setText("Usuário/Senha incorreto(a)");
            System.out.println("Falha no login");
       }
       
       System.out.println("Usuário " + usuario);
       System.out.println("Senha " + senha);
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
=======
            
        } else {
            //Falha no login (usuário ou senha inválido)
            lblUsuario.setText("Usuário/Senha incorreto(a)");
            lblSenha.setText("Usuário/Senha incorreto(a)");
            System.out.println("Falha no login");
        }

        System.out.println("Usuário: " + usuario);
        System.out.println("Senha  : " + senha);

    }

}
>>>>>>> e08e4b9 (codigo com dashboard)
