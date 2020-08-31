package lk.ijse.dep.pos.dao.custom.impl;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import lk.ijse.dep.pos.dao.CrudUtil;
import lk.ijse.dep.pos.dao.custom.OrderDAO;
import lk.ijse.dep.pos.entity.Order;

public class OrderDAOImpl implements OrderDAO {


  public String getLastOrderId() throws Exception {

    ResultSet rst = CrudUtil.execute("SELECT * FROM `Order` ORDER BY id DESC LIMIT 1");
    if (!rst.next()) {
      return null;
    } else {
      return rst.getString(1);
    }

  }

  @Override
  public List<Order> findAll() throws Exception {

    ResultSet rst = CrudUtil.execute("SELECT * FROM `Order`");
    List<Order> orders = new ArrayList<>();
    while (rst.next()) {
      orders.add(new Order(rst.getString(1),
          rst.getDate(2),
          rst.getString(3)));
    }
    return orders;

  }

  @Override
  public Order find(String key) throws Exception {

    ResultSet rst = CrudUtil.execute("SELECT * FROM `Order` WHERE id=?", key);
    if (rst.next()) {
      return new Order(rst.getString(1),
          rst.getDate(2),
          rst.getString(3));
    }
    return null;

  }

  @Override
  public boolean save(Order order) throws Exception {

    return CrudUtil.execute("INSERT INTO `Order` VALUES (?,?,?)",
        order.getId(), order.getDate(), order.getCustomerId());

  }

  @Override
  public boolean update(Order order) throws Exception {

    return CrudUtil
        .execute("UPDATE Order SET date=?, customerId=? WHERE id=?", order.getDate(), order.getCustomerId(),
            order.getId());

  }

  @Override
  public boolean delete(String key) throws Exception {

    return CrudUtil.execute("DELETE FROM Order WHERE id=?", key);

  }
}
