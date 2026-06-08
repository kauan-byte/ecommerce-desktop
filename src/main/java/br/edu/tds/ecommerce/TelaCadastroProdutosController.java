package br.edu.tds.ecommerce;

import java.io.File;
import java.net.URL;
import java.nio.file.Files; // Adicionado para cópia do arquivo
import java.nio.file.Path;  // Adicionado para manipulação de caminhos
import java.nio.file.StandardCopyOption; // Adicionado para substituir arquivos iguais
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

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
    
    // Atributos de controle de arquivo e imagem adicionados (Passo 7)
    private File arquivoSelecionado;
    private String caminhoImagem;
    
    private Produto produtoEdicao;
    
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
        
        // Copia a imagem selecionada fisicamente para a pasta interna antes de mandar pro banco (Passo 8)
        if (arquivoSelecionado != null) {
            caminhoImagem = salvarImagem(arquivoSelecionado);
        } else if (produtoEdicao != null) {
            // Mantém a imagem anterior caso esteja editando e não alterou a foto
            caminhoImagem = produtoEdicao.getImagem();
        }
        
        if (status) {
            Produto p = new Produto();
            p.setNome(txtNome.getText());
            p.setCategoria(cbCategoria.getValue());
            p.setPreco(Double.parseDouble(txtPreco.getText()));
            p.setQuantidade(Integer.parseInt(txtQuantidade.getText()));
            p.setImagem(caminhoImagem); // Grava a referência relativa (ex: imagens_produtos/12345_foto.jpg)
            p.setDescricao(txtDescricao.getText());
            p.setAtivo(cAtivo.isSelected());
            
            ProdutoDAO dao = new ProdutoDAO();
            dao.cadastrarProduto(p);
            mostrarAlerta("Produto cadastrado com sucesso!");
            
        } else {
            mostrarAlerta("Todos os campos são obrigatórios");
        }
    }

    @FXML
    private void selecionarImagem() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Selecionar Imagem");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Imagens", "*.png", "*.jpg", "*.jpeg")
        );

        // Atribui o retorno diretamente na variável de escopo global (Passo 7)
        arquivoSelecionado = fileChooser.showOpenDialog(txtImagem.getScene().getWindow());

        if (arquivoSelecionado != null) {
            Image image = new Image(arquivoSelecionado.toURI().toString());
            imgProduto.setImage(image);
            
            // Exibe provisoriamente o caminho local no TextField para dar feedback ao usuário
            txtImagem.setText(arquivoSelecionado.getAbsolutePath());
        }
    }

    // Método responsável pela persistência do arquivo físico no projeto (Passo 6)
    private String salvarImagem(File arquivo) {
        try {
            String pastaDestino = "imagens_produtos/";
            File diretorio = new File(pastaDestino);

            if (!diretorio.exists()) {
                diretorio.mkdir();
            }

            // Evita sobreposição de arquivos com o mesmo nome usando timestamp
            String nomeArquivo = System.currentTimeMillis() + "_" + arquivo.getName();

            Path origem = arquivo.toPath();
            Path destino = Path.of(pastaDestino, nomeArquivo);

            Files.copy(origem, destino, StandardCopyOption.REPLACE_EXISTING);

            return destino.toString(); // Retorna a string do caminho relativo

        } catch (Exception e) {
            e.printStackTrace();
            return null;
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

        // Carrega a imagem a partir do caminho salvo relativo ao abrir modo Edição (Passo 10)
        if (produtoEdicao.getImagem() != null && !produtoEdicao.getImagem().isEmpty()) {
            try {
                File arquivo = new File(produtoEdicao.getImagem());
                if (arquivo.exists()) {
                    Image image = new Image(arquivo.toURI().toString());
                    imgProduto.setImage(image);
                }
            } catch (Exception e) {
                System.out.println("Erro ao carregar a imagem armazenada: " + e.getMessage());
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
}