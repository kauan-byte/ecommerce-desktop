/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package br.edu.tds.ecommerce;

import java.net.URL;
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
    private TextArea txtDescricao;
    
    @FXML
    private CheckBox cAtivo;
    
    private Produto produtoEdicao;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        cbCategoria.getItems().add(0,"Celulares");
        cbCategoria.getItems().add(1,"Eletrônicos");
        cbCategoria.getItems().add(2,"Informática");
        cbCategoria.getItems().add(3,"Jogos");
        cbCategoria.getItems().add(4,"Livros");
        cbCategoria.getItems().add(5,"Roupas");
        
        
        //cbCategoria.getItems().addAll("Eletrônio","Informática");
        
        //ObservableList<String> opcoes = FXCollections.observableArrayList("Item 1", "Item 2");
        //cbCategoria.setItems(opcoes);
    }    
    
    private boolean validarCampos(){
        
        boolean validado = true;
        
        if(txtNome.getText().isEmpty()){
          txtNome.setStyle("-fx-background-color: transparent; -fx-border-color: red; -fx-border-width: 0 0 3 0;");
          validado = false;
        }
        
        if(txtPreco.getText().isEmpty()){
          txtPreco.setStyle("-fx-background-color: transparent; -fx-border-color: red; -fx-border-width: 0 0 3 0;");    
          validado = false;
        }
        
        if(txtQuantidade.getText().isEmpty()){
          txtQuantidade.setStyle("-fx-background-color: transparent; -fx-border-color: red; -fx-border-width: 0 0 3 0;");    
          validado = false;
        }
        
        if(txtImagem.getText().isEmpty()){
          txtImagem.setStyle("-fx-background-color: transparent; -fx-border-color: red; -fx-border-width: 0 0 3 0;");    
          validado = false;
        }
        
        return validado;
    }
    
    @FXML
    private void salvarProduto() throws SQLException{
        
        txtNome.setStyle("-fx-background-color: transparent; -fx-border-color: #0598ff; -fx-border-width: 0 0 3 0;");
        txtPreco.setStyle("-fx-background-color: transparent; -fx-border-color: #0598ff; -fx-border-width: 0 0 3 0;");
        txtQuantidade.setStyle("-fx-background-color: transparent; -fx-border-color: #0598ff; -fx-border-width: 0 0 3 0;");
        txtImagem.setStyle("-fx-background-color: transparent; -fx-border-color: #0598ff; -fx-border-width: 0 0 3 0;");
        
        boolean status = validarCampos();
        
        if(status){
            //salvo os dados do produto no banco de dados
            //mostrarAlerta("Formulário Validado");
            
            Produto p = new Produto();
            p.setNome(txtNome.getText());
            p.setCategoria(cbCategoria.getValue());
            p.setPreco(Double.parseDouble(txtPreco.getText()));
            p.setQuantidade(Integer.parseInt(txtQuantidade.getText()));
            p.setImagem(txtImagem.getText());
            p.setDescricao(txtDescricao.getText());
            p.setAtivo(cAtivo.isSelected());
            
            ProdutoDAO dao = new ProdutoDAO();
            dao.cadastrarProduto(p);
            mostrarAlerta("Produto cadastrado com sucesso!");
            
        }else{
            //corrigir informações do formulário
            mostrarAlerta("Todos os campos são obrigatórios");
        }
    }
    
    public void setProduto(Produto p){
        produtoEdicao = p;
        
        txtNome.setText(produtoEdicao.getNome());
        cbCategoria.setValue(produtoEdicao.getCategoria());
        txtPreco.setText(String.valueOf(produtoEdicao.getPreco()));
        txtQuantidade.setText(String.valueOf(produtoEdicao.getQuantidade()));
        txtImagem.setText(produtoEdicao.getImagem());
        txtDescricao.setText(produtoEdicao.getDescricao());
        cAtivo.setSelected(produtoEdicao.isAtivo());
    }
    
    private void mostrarAlerta(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sistema");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
    
}
