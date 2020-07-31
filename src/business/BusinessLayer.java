package business;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dao.DAOFactory;
import dao.DAOType;
import dao.custom.CustomerDAO;
import dao.custom.ItemDAO;
import dao.custom.OrderDAO;
import dao.custom.OrderDetailDAO;
import db.DBConnection;
import entity.Customer;
import entity.Item;
import entity.Order;
import entity.OrderDetail;
import util.CustomerTM;
import util.ItemTM;
import util.OrderDetailTM;
import util.OrderTM;

public class BusinessLayer {

  public static String getNewCustomerId() {
    try {
      CustomerDAO customerDAO = DAOFactory.getInstance().getDAO(DAOType.CUSTOMER);
      String lastCustomerId = customerDAO.getLastCustomerId();
      if (lastCustomerId == null) {
        return "C001";
      } else {
        int maxId = Integer.parseInt(lastCustomerId.replace("C", ""));
        maxId = maxId + 1;
        String id = "";
        if (maxId < 10) {
          id = "C00" + maxId;
        } else if (maxId < 100) {
          id = "C0" + maxId;
        } else {
          id = "C" + maxId;
        }
        return id;
      }
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public static String getNewItemCode() {
    try {
      ItemDAO itemDAO = DAOFactory.getInstance().getDAO(DAOType.ITEM);
      String lastItemCode = itemDAO.getLastItemCode();
      if (lastItemCode == null) {
        return "I001";
      } else {
        int maxId = Integer.parseInt(lastItemCode.replace("I", ""));
        maxId = maxId + 1;
        String id = "";
        if (maxId < 10) {
          id = "I00" + maxId;
        } else if (maxId < 100) {
          id = "I0" + maxId;
        } else {
          id = "I" + maxId;
        }
        return id;
      }
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public static String getNewOrderId() {
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

  public static List<CustomerTM> getAllCustomers() {
    try {
      CustomerDAO customerDAO = DAOFactory.getInstance().getDAO(DAOType.CUSTOMER);
      List<Customer> allCustomers = customerDAO.findAll();
      List<CustomerTM> customers = new ArrayList<>();
      for (Customer customer : allCustomers) {
        customers.add(new CustomerTM(customer.getId(), customer.getName(), customer.getAddress()));
      }
      return customers;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public static boolean saveCustomer(String id, String name, String address) {
    try {
      CustomerDAO customerDAO = DAOFactory.getInstance().getDAO(DAOType.CUSTOMER);
      return customerDAO.save(new Customer(id, name, address));
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  public static boolean deleteCustomer(String customerId) {
    try {
      CustomerDAO customerDAO = DAOFactory.getInstance().getDAO(DAOType.CUSTOMER);
      return customerDAO.delete(customerId);
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  public static boolean updateCustomer(String name, String address, String customerId) {
    try {
      CustomerDAO customerDAO = DAOFactory.getInstance().getDAO(DAOType.CUSTOMER);
      return customerDAO.update(new Customer(customerId, name, address));
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  public static List<ItemTM> getAllItems() {
    try {
      ItemDAO itemDAO = DAOFactory.getInstance().getDAO(DAOType.ITEM);
      List<Item> allItems = itemDAO.findAll();
      List<ItemTM> items = new ArrayList<>();
      for (Item item : allItems) {
        items.add(new ItemTM(item.getCode(), item.getDescription(), item.getQtyOnHand(),
            item.getUnitPrice().doubleValue()));
      }
      return items;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public static boolean saveItem(String code, String description, int qtyOnHand, double unitPrice) {
    try {
      ItemDAO itemDAO = DAOFactory.getInstance().getDAO(DAOType.ITEM);
      return itemDAO.save(new Item(code, description, BigDecimal.valueOf(unitPrice), qtyOnHand));
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  public static boolean deleteItem(String itemCode) {
    try {
      ItemDAO itemDAO = DAOFactory.getInstance().getDAO(DAOType.ITEM);
      return itemDAO.delete(itemCode);
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  public static boolean updateItem(String description, int qtyOnHand, double unitPrice, String itemCode) {
    try {
      ItemDAO itemDAO = DAOFactory.getInstance().getDAO(DAOType.ITEM);
      return itemDAO.update(new Item(itemCode, description,
          BigDecimal.valueOf(unitPrice), qtyOnHand));
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }


  public static boolean placeOrder(OrderTM order, List<OrderDetailTM> orderDetails) {
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
