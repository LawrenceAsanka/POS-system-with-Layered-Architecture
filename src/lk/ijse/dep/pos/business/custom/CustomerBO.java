package lk.ijse.dep.pos.business;

import java.util.List;

import lk.ijse.dep.pos.util.CustomerTM;

public interface CustomerBO extends SuperBO {

  public  String getNewCustomerId() throws Exception;

  public List<CustomerTM> getAllCustomers() throws Exception;

  public  boolean saveCustomer(String id, String name, String address) throws Exception;

  public  boolean deleteCustomer(String customerId) throws Exception;

  public  boolean updateCustomer(String name, String address, String customerId) throws Exception;
}
