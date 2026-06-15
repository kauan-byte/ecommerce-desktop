package br.edu.tds.ecommerce;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author douglas
 */
public class TelaCadastroProdutosController implements Initializable {

    @FXML
    private TextField txtNome;

    @FXML
    private ComboBox<String> cbCategoria;

    @FXML
    private TextField txtPreco;

    @FXML
    private TextField txtQuantidade;

    @FXML
    private TextField txtImagem;

    @FXML
    private ImageView imgProduto;

    @FXML
    private TextArea txtDescricao;

    @FXML
    private CheckBox cAtivo;

    private Produto produtoEdicao;

    private File arquivoSelecionado;
    
    private String caminhoImagem;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cbCategoria.getItems().add(0, "Celulares");
        cbCategoria.getItems().add(1, "Eletrônicos");
        cbCategoria.getItems().add(2, "Informática");
        cbCategoria.getItems().add(3, "Jogos");
        cbCategoria.getItems().add(4, "Livros");
        cbCategoria.getItems().add(5, "Roupas");
    }

    private boolean validarCampos() {
        boolean validado = true;

        if (txtNome.getText().isEmpty()) {
            txtNome.setStyle("-fx-background-color: transparent; -fx-border-color: red; -fx-border-width: 0 0 3 0;");
            validado = false;
        }

        if (txtPreco.getText().isEmpty()) {
            txtPreco.setStyle("-fx-background-color: transparent; -fx-border-color: red; -fx-border-width: 0 0 3 0;");
            validado = false;
        }

        if (txtQuantidade.getText().isEmpty()) {
            txtQuantidade.setStyle("-fx-background-color: transparent; -fx-border-color: red; -fx-border-width: 0 0 3 0;");
            validado = false;
        }

        if (txtImagem.getText().isEmpty()) {
            txtImagem.setStyle("-fx-background-color: transparent; -fx-border-color: red; -fx-border-width: 0 0 3 0;");
            validado = false;
        }

        return validado;
    }

    @FXML
    private void salvarProduto() throws SQLException {

        txtNome.setStyle("-fx-background-color: transparent; -fx-border-color: #0598ff; -fx-border-width: 0 0 3 0;");
        txtPreco.setStyle("-fx-background-color: transparent; -fx-border-color: #0598ff; -fx-border-width: 0 0 3 0;");
        txtQuantidade.setStyle("-fx-background-color: transparent; -fx-border-color: #0598ff; -fx-border-width: 0 0 3 0;");
        txtImagem.setStyle("-fx-background-color: transparent; -fx-border-color: #0598ff; -fx-border-width: 0 0 3 0;");

        boolean status = validarCampos();

        if (status) {
            String urlCloudinary = null;

            // 1. Envia a imagem para o Cloudinary se houver um arquivo selecionado
            if (arquivoSelecionado != null) {
                try {
                    Cloudinary cloudinary = CloudinaryConfig.getCloudinary();
                    Map uploadResult = cloudinary.uploader().upload(
                            arquivoSelecionado, 
                            ObjectUtils.emptyMap()
                    );
                    
                    // Recupera a URL segura da nuvem
                    urlCloudinary = (String) uploadResult.get("secure_url");
                    
                } catch (Exception e) {
                    e.printStackTrace();
                    mostrarAlerta("Erro ao enviar a imagem para o Cloudinary. O produto não foi salvo.");
                    return; // Aborta o método para não cadastrar sem o link da imagem
                }
            } else if (produtoEdicao != null) {
                // Caso seja uma edição e o usuário não alterou a foto, mantém a antiga
                urlCloudinary = produtoEdicao.getImagem();
            }

            // 2. Monta o objeto Produto com a URL obtida
            Produto p = new Produto();
            p.setNome(txtNome.getText());
            p.setCategoria(cbCategoria.getValue());
            p.setPreco(Double.parseDouble(txtPreco.getText()));
            p.setQuantidade(Integer.parseInt(txtQuantidade.getText()));
            p.setImagem(urlCloudinary); // Grava o link 'https://...'
            p.setDescricao(txtDescricao.getText());
            p.setAtivo(cAtivo.isSelected());

            // 3. Persiste no banco de dados
            ProdutoDAO dao = new ProdutoDAO();
            dao.cadastrarProduto(p);
            mostrarAlerta("Produto cadastrado com sucesso!");

            // Retorna para a tela de gerenciamento após o cadastro de sucesso
            voltarParaGerenciamento();

        } else {
            mostrarAlerta("Todos os campos são obrigatórios");
        }
    }

    public void setProduto(Produto p) {
        produtoEdicao = p;

        txtNome.setText(produtoEdicao.getNome());
        cbCategoria.setValue(produtoEdicao.getCategoria());
        txtPreco.setText(String.valueOf(produtoEdicao.getPreco()));
        txtQuantidade.setText(String.valueOf(produtoEdicao.getQuantidade()));
        txtImagem.setText(produtoEdicao.getImagem());
        txtDescricao.setText(produtoEdicao.getDescricao());
        cAtivo.setSelected(produtoEdicao.isAtivo());

        // Carrega o ImageView tratando se é um link web ou arquivo local antigo
        if (produtoEdicao.getImagem() != null && !produtoEdicao.getImagem().isEmpty()) {
            if (produtoEdicao.getImagem().startsWith("http")) {
                imgProduto.setImage(new Image(produtoEdicao.getImagem()));
            } else {
                imgProduto.setImage(new Image(new File(produtoEdicao.getImagem()).toURI().toString()));
            }
        }
    }

    private void mostrarAlerta(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sistema");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    @FXML
    private void selecionarImagem() {

        FileChooser fileChooser = new FileChooser();

        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter(
                        "Imagens",
                        "*.png",
                        "*.jpg",
                        "*.jpeg"
                )
        );

        arquivoSelecionado = fileChooser.showOpenDialog(
                txtImagem.getScene().getWindow()
        );

        if (arquivoSelecionado != null) {

            Image image = new Image(
                    arquivoSelecionado
                            .toURI()
                            .toString()
            );

            imgProduto.setImage(image);

            txtImagem.setText(
                    arquivoSelecionado.getAbsolutePath()
            );
        }
    }

    @FXML
    private void voltarParaGerenciamento() {
        try {
            // Carrega o FXML com o nome exato fornecido (telaGerenciamentoProdutos.fxml)
            Parent root = FXMLLoader.load(getClass().getResource("telaGerenciamentoProdutos.fxml"));
            
            // Obtém o Stage atual a partir do componente txtNome
            Stage stage = (Stage) txtNome.getScene().getWindow();
            
            // Define e exibe a nova cena
            stage.setScene(new Scene(root));
            stage.setTitle("Gerenciamento de Produtos");
            stage.show();
            
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Erro ao retornar para a tela de gerenciamento.");
        }
    }
}