package lk.ijse.dep.pos.dao.custom.impl;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import lk.ijse.dep.pos.dao.CrudUtil;
import lk.ijse.dep.pos.dao.custom.CustomerDAO;
import lk.ijse.dep.pos.entity.Customer;

public class CustomerDAOImpl implements CustomerDAO {


  public String getLastCustomerId() throws Exception {
           /* Connection connection = DBConnection.getInstance().getConnection();
            Statement stm = connection.createStatement();*/
    ResultSet rst = CrudUtil.execute("SELECT * FROM Customer ORDER BY id DESC LIMIT 1");
    if (!rst.next()) {
      return null;
    } else {
      return rst.getString(1);
    }

  }

  @Override
  public List<Customer> findAll() throws Exception {
            /*Connection connection = DBConnection.getInstance().getConnection();
            Statement stm = connection.createStatement();*/
    ResultSet rst = CrudUtil.execute("SELECT * FROM Customer");
    List<Customer> customers = new ArrayList<>();
    while (rst.next()) {
      customers.add(new Customer(rst.getString(1),
          rst.getString(2),
          rst.getString(3)));
    }
    return customers;
  }

  @Override
  public Customer find(String key) throws Exception {

           /* Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement pstm = connection.prepareStatement("SELECT * FROM Customer WHERE id=?");
            pstm.setObject(1, key);*/
    ResultSet rst = CrudUtil.execute("SELECT * FROM Customer WHERE id=?", key);
    if (rst.next()) {
      return new Customer(rst.getString(1),
          rst.getString(2),
          rst.getString(3));
    }
    return null;

  }

  @Override
  public boolean save(Customer customer) throws Exception {

            /*Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement pstm = connection.prepareStatement("INSERT INTO Customer VALUES (?,?,?)");
            pstm.setObject(1, customer.getId());
            pstm.setObject(2, customer.getName());
            pstm.setObject(3, customer.getAddress());*/
    return CrudUtil
        .execute("INSERT INTO Customer VALUES (?,?,?)", customer.getId(), customer.getName(), customer.getAddress());

  }

  @Override
  public boolean update(Customer customer) throws Exception {

            /*Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement pstm = connection.prepareStatement("UPDATE Customer SET name=?, address=? WHERE id=?");
            pstm.setObject(3, customer.getId());
            pstm.setObject(1, customer.getName());
            pstm.setObject(2, customer.getAddress());*/
    return CrudUtil
        .execute("UPDATE Customer SET name=?, address=? WHERE id=?", customer.getName(), customer.getAddress(),
            customer.getId());

  }

  @Override
  public boolean delete(String key) throws Exception {

           /* Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement pstm = connection.prepareStatement("DELETE FROM Customer WHERE id=?");
            pstm.setObject(1, key);*/
    return CrudUtil.execute("DELETE FROM Customer WHERE id=?", key);

  }
}
