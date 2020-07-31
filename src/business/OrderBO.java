package business;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

import dao.DAOFactory;
import dao.DAOType;
import dao.custom.ItemDAO;
import dao.custom.OrderDAO;
import dao.custom.OrderDetailDAO;
import db.DBConnection;
import entity.Item;
import entity.Order;
import entity.OrderDetail;
import util.OrderDetailTM;
import util.OrderTM;

public class OrderBO {

  public  String getNewOrderId() {
    try {
      OrderDAO orderDAO = DAOFactory.getInstance().getDAO(DAOType.ORDER);
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
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }


  public  boolean placeOrder(OrderTM order, List<OrderDetailTM> orderDetails) {
    Connection connection = DBConnection.getInstance().getConnection();
    try {
      connection.setAutoCommit(false);
      OrderDAO orderDAO = DAOFactory.getInstance().getDAO(DAOType.ORDER);
      boolean result = orderDAO.save(new Order(order.getOrderId(),
          Date.valueOf(order.getOrderDate()),
          order.getCustomerId()));
      if (!result) {
        connection.rollback();
        return false;
      }
      for (OrderDetailTM orderDetail : orderDetails) {
        OrderDetailDAO orderDetailDAO = DAOFactory.getInstance().getDAO(DAOType.ORDER_DETAIL);
        result = orderDetailDAO.save(new OrderDetail(
            order.getOrderId(), orderDetail.getCode(),
            orderDetail.getQty(), BigDecimal.valueOf(orderDetail.getUnitPrice())
        ));
        if (!result) {
          connection.rollback();
          return false;
        }

        ItemDAO itemDAO = DAOFactory.getInstance().getDAO(DAOType.ITEM);
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
}
