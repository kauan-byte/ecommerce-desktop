/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package br.edu.tds.ecommerce;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author aluno
 */
public class TelaCadastroProdutosController implements Initializable {
 @FXML
 private TextField txtNome;
 @FXML
 private TextField txtPreco;
 @FXML
 private TextField txtQuantidade;
 @FXML
 private TextField txtImagem;
 @FXML
 private TextArea txtDescricao;
 @FXML
   private ComboBox<String> cbCategoria;

 
 
 
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        cbCategoria.getItems().addAll("Eletronico","Informatica");
    }    
    
}
