package br.edu.tds.ecommerce;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class DashboardController {

    @FXML
    private BorderPane painelPrincipal;

    @FXML
    private void abrirUsuarios() throws IOException {

        System.out.println("Abrir Usuários");
    }

    @FXML
    private void abrirProdutos() throws IOException {

        System.out.println("Abrir Produtos");
    }

    @FXML
    private void abrirFornecedores() {

        System.out.println("Abrir fornecedores");
    }

    @FXML
    private void abrirVendas() {

        System.out.println("Abrir vendas");
    }

    @FXML
    private void abrirFinanceiro() {

        System.out.println("Abrir financeiro");
    }

    @FXML
    private void abrirRelatorios() {

        System.out.println("Abrir relatórios");
    }

    @FXML
    private void logout() throws IOException {

        System.out.println("logout");
    }
}
