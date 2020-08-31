package lk.ijse.dep.pos.business.custom.impl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import lk.ijse.dep.pos.business.custom.OrderBO;
import lk.ijse.dep.pos.dao.DAOFactory;
import lk.ijse.dep.pos.dao.DAOType;
import lk.ijse.dep.pos.dao.custom.ItemDAO;
import lk.ijse.dep.pos.dao.custom.OrderDAO;
import lk.ijse.dep.pos.dao.custom.OrderDetailDAO;
import lk.ijse.dep.pos.dao.custom.QueryDAO;
import lk.ijse.dep.pos.db.DBConnection;
import lk.ijse.dep.pos.entity.CustomEntity;
import lk.ijse.dep.pos.entity.Item;
import lk.ijse.dep.pos.entity.Order;
import lk.ijse.dep.pos.entity.OrderDetail;
import lk.ijse.dep.pos.util.OrderDetailTM;
import lk.ijse.dep.pos.util.OrderTM;

public class OrderBOImpl implements OrderBO {

  OrderDAO orderDAO = DAOFactory.getInstance().getDAO(DAOType.ORDER);
  ItemDAO itemDAO = DAOFactory.getInstance().getDAO(DAOType.ITEM);
  OrderDetailDAO orderDetailDAO = DAOFactory.getInstance().getDAO(DAOType.ORDER_DETAIL);
  QueryDAO queryDAO = DAOFactory.getInstance().getDAO(DAOType.QUERY);

  public  String getNewOrderId() throws Exception{

      String lastOrderId = orderDAO.getLastOrderId();
      if (lastOrderId == null) {
        return "OD001";
      } else {
        int maxId = Integer.parseInt(lastOrderId.replace("OD", ""));
        maxId = maxId + 1;
        String id = "";
        if (maxId < 10) {
          id = "OD00" + maxId;
        } else if (maxId < 100) {
          id = "OD0" + maxId;
        } else {
          id = "OD" + maxId;
        }
        return id;
      }

  }


  public  boolean placeOrder(OrderTM order, List<OrderDetailTM> orderDetails) throws Exception{
    Connection connection = DBConnection.getInstance().getConnection();
    try {
      connection.setAutoCommit(false);
      boolean result = orderDAO.save(new Order(order.getOrderId(),
         order.getOrderDate(),
          order.getCustomerId()));
      if (!result) {
        connection.rollback();
        return false;
      }
      for (OrderDetailTM orderDetail : orderDetails) {
        result = orderDetailDAO.save(new OrderDetail(
            order.getOrderId(), orderDetail.getCode(),
            orderDetail.getQty(), BigDecimal.valueOf(orderDetail.getUnitPrice())
        ));
        if (!result) {
          connection.rollback();
          return false;
        }


        Item item = itemDAO.find(orderDetail.getCode());
        item.setQtyOnHand(item.getQtyOnHand() - orderDetail.getQty());
        result = itemDAO.update(item);
        if (!result) {
          connection.rollback();
          return false;
        }
      }
      connection.commit();
      return true;
    } catch (Throwable throwables) {
      throwables.printStackTrace();
      try {
        connection.rollback();
      } catch (SQLException e) {
        e.printStackTrace();
      }
      return false;
    } finally {
      try {
        connection.setAutoCommit(true);
      } catch (SQLException throwables) {
        throwables.printStackTrace();
      }
    }
  }

  @Override
  public List<OrderTM> getAllOrders() throws Exception {
    List<CustomEntity> odl = queryDAO.getOrderDetail();
    List<OrderTM> orderDetailsList = new ArrayList<>();
    for (CustomEntity orderDetails : odl) {
      orderDetailsList.add(new OrderTM(orderDetails.getOrderId(),orderDetails.getOrderDate(),orderDetails.getCustomerId(),
              orderDetails.getCustomerName(),orderDetails.getTotal()));
    }
    return orderDetailsList;
  }
}
