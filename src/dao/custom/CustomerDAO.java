package dao.custom;

import java.util.List;

import dao.CrudDAO;
import dao.SuperDAO;
import entity.Customer;

public interface CustomerDAO extends CrudDAO<Customer,String > {
  String getLastCustomerId() throws Exception;
}
