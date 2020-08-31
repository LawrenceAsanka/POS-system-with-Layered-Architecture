package lk.ijse.dep.pos.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import lk.ijse.dep.pos.business.BOFactory;
import lk.ijse.dep.pos.business.BOType;
import lk.ijse.dep.pos.business.custom.OrderBO;
import lk.ijse.dep.pos.util.OrderTM;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SearchOrdersFormController {
    public TextField txtSearch;
    public TableView<OrderTM> tblOrders;
    List<OrderTM> orderArrayList = new ArrayList<>();
private OrderBO orderBO = BOFactory.getInstance().getBO(BOType.ORDER);

    public void initialize() {

        tblOrders.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("orderId"));
        tblOrders.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("orderDate"));
        tblOrders.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("customerId"));
        tblOrders.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("customerName"));
        tblOrders.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("orderTotal"));

        loadAllOrders();

        txtSearch.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                tblOrders.getItems().clear();
                for (OrderTM orders : orderArrayList) {
                    if(orders.getCustomerId().contains(newValue) ||
                    orders.getOrderId().contains(newValue) ||
                    orders.getCustomerName().contains(newValue) ||
                    orders.getOrderDate().toString().contains(newValue) ||
                    orders.getOrderTotal().toString().contains(newValue)){
                        tblOrders.getItems().add(orders);
                    }

                }
            }
        });

    }

    private void loadAllOrders() {
        tblOrders.getItems().clear();
        List<OrderTM> allOrders = null;
        try {
            allOrders = orderBO.getAllOrders();
        } catch (Exception e) {
            e.printStackTrace();
        }

        ObservableList<OrderTM> orderObservableList = FXCollections.observableArrayList(allOrders);
        tblOrders.setItems(orderObservableList);
        for (OrderTM orders : allOrders) {
            orderArrayList.add(new OrderTM(orders.getOrderId(),orders.getOrderDate(),orders.getCustomerId(),orders.getCustomerName(),
                    orders.getOrderTotal()));
        }
    }

    @FXML
    private void navigateToHome(MouseEvent event) throws IOException {
        URL resource = this.getClass().getResource("/lk/ijse/dep/pos/view/MainForm.fxml");
        Parent root = FXMLLoader.load(resource);
        Scene scene = new Scene(root);
        Stage primaryStage = (Stage) (this.txtSearch.getScene().getWindow());
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
    }

    public void tblOrders_OnMouseClicked(MouseEvent mouseEvent) throws IOException {
        if (tblOrders.getSelectionModel().getSelectedItem() == null) {
            return;
        }
        if (mouseEvent.getClickCount() == 2) {
            FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("/lk/ijse/dep/pos/view/PlaceOrderForm.fxml"));
            Parent root = fxmlLoader.load();
            PlaceOrderFormController controller = fxmlLoader.getController();
            controller.initializeWithSearchOrderForm(tblOrders.getSelectionModel().getSelectedItem().getOrderId());
            Scene orderScene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(orderScene);
            stage.centerOnScreen();
            stage.show();
        }
    }

}
