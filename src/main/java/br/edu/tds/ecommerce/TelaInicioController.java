package br.edu.tds.ecommerce;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class TelaInicioController implements Initializable {

    @FXML private VBox vboxResenhas;
    @FXML private TextField txtNomeFilme;
    @FXML private TextArea txtComentario;
    @FXML private TextField txtNota;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        carregarAvaliacoes();
    }

    @FXML
    public void salvarAvaliacao(ActionEvent event) {
        String sql = "INSERT INTO avaliacoes (id_usuario, nome_filme, nota, comentario) VALUES (?, ?, ?, ?)";

        // Validação básica
        if (txtNomeFilme.getText().isEmpty() || txtComentario.getText().isEmpty()) {
            exibirAlerta("Aviso", "Preencha o nome do filme e o comentário!", Alert.AlertType.WARNING);
            return;
        }

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // id_usuario fixo em 1 para teste (certifique-se de que o id 1 existe no banco)
            stmt.setInt(1, 1); 
            stmt.setString(2, txtNomeFilme.getText());
            stmt.setInt(3, Integer.parseInt(txtNota.getText()));
            stmt.setString(4, txtComentario.getText());

            stmt.executeUpdate();

            // Limpa os campos após o sucesso
            txtNomeFilme.clear();
            txtComentario.clear();
            txtNota.setText("10");

            // Atualiza a lista na tela
            carregarAvaliacoes();

        } catch (SQLException e) {
            exibirAlerta("Erro de Banco", e.getMessage(), Alert.AlertType.ERROR);
        } catch (NumberFormatException e) {
            exibirAlerta("Erro de Nota", "A nota deve ser um número entre 0 e 10", Alert.AlertType.ERROR);
        }
    }

    public void carregarAvaliacoes() {
        vboxResenhas.getChildren().clear();
        
        String sql = "SELECT u.nomeUsuario, a.nome_filme, a.nota, a.comentario " +
                     "FROM avaliacoes a " +
                     "JOIN usuarios u ON a.id_usuario = u.id_usuario " +
                     "ORDER BY a.id_avaliacao DESC";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String usuario = rs.getString("nomeUsuario");
                String filme = rs.getString("nome_filme");
                int nota = rs.getInt("nota");
                String comentario = rs.getString("comentario");

                vboxResenhas.getChildren().add(criarCardAvaliacao(usuario, filme, nota, comentario));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao carregar avaliações: " + e.getMessage());
        }
    }

    private AnchorPane criarCardAvaliacao(String usuario, String filme, int nota, String comentario) {
        AnchorPane card = new AnchorPane();
        card.setStyle("-fx-background-color: #1a1a1a; -fx-border-color: red; -fx-border-width: 0 0 0 5;");
        card.setPrefWidth(680.0);
        card.setPadding(new Insets(10));

        Label lblCabecalho = new Label(usuario + " avaliou " + filme);
        lblCabecalho.setTextFill(Color.RED);
        lblCabecalho.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        lblCabecalho.setLayoutX(15); lblCabecalho.setLayoutY(10);

        Label lblComent = new Label(comentario);
        lblComent.setTextFill(Color.WHITE);
        lblComent.setWrapText(true);
        lblComent.setMaxWidth(550);
        lblComent.setLayoutX(15); lblComent.setLayoutY(35);

        Label lblNota = new Label("★ " + nota);
        lblNota.setTextFill(Color.GOLD);
        lblNota.setStyle("-fx-font-weight: bold;");
        lblNota.setLayoutX(600); lblNota.setLayoutY(10);

        card.getChildren().addAll(lblCabecalho, lblComent, lblNota);
        return card;
    }

    @FXML
    public void abrirGerenciamento(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("telaGerenciamentoUsuarios.fxml"));      
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    private void exibirAlerta(String titulo, String msg, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}