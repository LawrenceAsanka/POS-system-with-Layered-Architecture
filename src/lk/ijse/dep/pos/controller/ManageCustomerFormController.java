/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.dep.pos.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.dep.pos.business.BOFactory;
import lk.ijse.dep.pos.business.BOType;
import lk.ijse.dep.pos.business.CustomerBO;
import lk.ijse.dep.pos.db.DBConnection;
import lk.ijse.dep.pos.util.CustomerTM;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author lawrence-asanka
 */
public class ManageCustomerFormController implements Initializable {

    private final CustomerBO customerBO = BOFactory.getInstance().getBO(BOType.CUSTOMER);
    @FXML
    private Button btnSave;
    @FXML
    private Button btnDelete;
    @FXML
    private AnchorPane root;
    @FXML
    private TextField txtCustomerId;
    @FXML
    private TextField txtCustomerName;
    @FXML
    private TextField txtCustomerAddress;
    @FXML
    private TableView<CustomerTM> tblCustomers;
    private boolean result;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tblCustomers.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("id"));
        tblCustomers.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("name"));
        tblCustomers.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("address"));

        txtCustomerId.setDisable(true);
        txtCustomerName.setDisable(true);
        txtCustomerAddress.setDisable(true);
        btnDelete.setDisable(true);
        btnSave.setDisable(true);

        loadAllCustomers();

        tblCustomers.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<CustomerTM>() {
            @Override
            public void changed(ObservableValue<? extends CustomerTM> observable, CustomerTM oldValue, CustomerTM newValue) {
                CustomerTM selectedItem = tblCustomers.getSelectionModel().getSelectedItem();

                if (selectedItem == null) {
                    btnSave.setText("Save");
                    btnDelete.setDisable(true);
                    txtCustomerId.clear();
                    txtCustomerName.clear();
                    txtCustomerAddress.clear();
                    return;
                }

                btnSave.setText("Update");
                btnSave.setDisable(false);
                btnDelete.setDisable(false);
                txtCustomerName.setDisable(false);
                txtCustomerAddress.setDisable(false);
                txtCustomerId.setText(selectedItem.getId());
                txtCustomerName.setText(selectedItem.getName());
                txtCustomerAddress.setText(selectedItem.getAddress());
            }
        });
    }

    private void loadAllCustomers() {

        ObservableList<CustomerTM> customers = tblCustomers.getItems();
        customers.clear();
        List<CustomerTM> allCustomers = null;
        try {
            allCustomers = customerBO.getAllCustomers();
        } catch (Exception e) {
            e.printStackTrace();
        }
        ObservableList<CustomerTM> customerObservableList = FXCollections.observableArrayList(allCustomers);
        tblCustomers.setItems(customerObservableList);
    }

    @FXML
    private void navigateToHome(MouseEvent event) throws IOException {
        URL resource = this.getClass().getResource("/lk/ijse/dep/pos/view/MainForm.fxml");
        Parent root = FXMLLoader.load(resource);
        Scene scene = new Scene(root);
        Stage primaryStage = (Stage) (this.root.getScene().getWindow());
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
    }

    //Save customer
    @FXML
    private void btnSave_OnAction(ActionEvent event) {
        String name = txtCustomerName.getText();
        String address = txtCustomerAddress.getText();

        // Validation
        if (name.trim().length() == 0 || address.trim().length() == 0) {
            new Alert(Alert.AlertType.ERROR, "Customer Name, Address can't be empty", ButtonType.OK).show();
            return;
        }

        if (btnSave.getText().equals("Save")) {

            try {
                result = customerBO.saveCustomer(txtCustomerId.getText(), txtCustomerName.getText(), txtCustomerAddress.getText());
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (!result) {
                new Alert(Alert.AlertType.ERROR, "Failed to add the customer", ButtonType.OK).show();
            }

            btnAddNew_OnAction(event);
            //update customer
        } else {
            CustomerTM selectedItem = tblCustomers.getSelectionModel().getSelectedItem();
            try {
                result = customerBO.updateCustomer(txtCustomerName.getText(), txtCustomerAddress.getText(), selectedItem.getId());
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (!result) {
                new Alert(Alert.AlertType.ERROR, "Failed to update the customer", ButtonType.OK).show();
            }

            tblCustomers.refresh();
            btnAddNew_OnAction(event);
        }
        loadAllCustomers();
    }

    //delete customer
    @FXML
    private void btnDelete_OnAction(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "Are you sure whether you want to delete this customer?",
                ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> buttonType = alert.showAndWait();
        if (buttonType.get() == ButtonType.YES) {
            CustomerTM selectedItem = tblCustomers.getSelectionModel().getSelectedItem();

            try {
                result = customerBO.deleteCustomer(selectedItem.getId());
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (!result) {
                new Alert(Alert.AlertType.ERROR, "Failed to delete the customer", ButtonType.OK).show();
            } else {
                tblCustomers.getItems().remove(selectedItem);
                tblCustomers.getSelectionModel().clearSelection();
            }

        }
        btnAddNew_OnAction(event);
    }

    @FXML
    private void btnAddNew_OnAction(ActionEvent actionEvent) {
        txtCustomerId.clear();
        txtCustomerName.clear();
        txtCustomerAddress.clear();
        tblCustomers.getSelectionModel().clearSelection();
        txtCustomerName.setDisable(false);
        txtCustomerAddress.setDisable(false);
        txtCustomerName.requestFocus();
        btnSave.setDisable(false);

        // Generate a new id
        int maxId = 0;
        try {
            Statement stm = DBConnection.getInstance().getConnection().createStatement();
            ResultSet rst = stm.executeQuery("SELECT id FROM Customer ORDER BY id DESC LIMIT 1");
            if (rst.next()) {
                maxId = Integer.parseInt(rst.getString(1).replace("C", ""));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        maxId = maxId + 1;
        String id = "";
        if (maxId < 10) {
            id = "C00" + maxId;
        } else if (maxId < 100) {
            id = "C0" + maxId;
        } else {
            id = "C" + maxId;
        }
        txtCustomerId.setText(id);

    }

}
