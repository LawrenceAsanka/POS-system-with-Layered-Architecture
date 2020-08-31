package lk.ijse.dep.pos.business.custom.impl;

import java.util.ArrayList;
import java.util.List;

import lk.ijse.dep.pos.business.CustomerBO;
import lk.ijse.dep.pos.dao.DAOFactory;
import lk.ijse.dep.pos.dao.DAOType;
import lk.ijse.dep.pos.dao.custom.CustomerDAO;
import lk.ijse.dep.pos.entity.Customer;
import lk.ijse.dep.pos.util.CustomerTM;

public class CustomerBOImpl implements CustomerBO {

  public  String getNewCustomerId() throws Exception{

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

  }

  public  List<CustomerTM> getAllCustomers() throws Exception {

      CustomerDAO customerDAO = DAOFactory.getInstance().getDAO(DAOType.CUSTOMER);
      List<Customer> allCustomers = customerDAO.findAll();
      List<CustomerTM> customers = new ArrayList<>();
      for (Customer customer : allCustomers) {
        customers.add(new CustomerTM(customer.getId(), customer.getName(), customer.getAddress()));
      }
      return customers;

  }

  public  boolean saveCustomer(String id, String name, String address) throws Exception{

      CustomerDAO customerDAO = DAOFactory.getInstance().getDAO(DAOType.CUSTOMER);
      return customerDAO.save(new Customer(id, name, address));

  }

  public  boolean deleteCustomer(String customerId) throws Exception{

      CustomerDAO customerDAO = DAOFactory.getInstance().getDAO(DAOType.CUSTOMER);
      return customerDAO.delete(customerId);

  }

  public  boolean updateCustomer(String name, String address, String customerId) throws Exception{

      CustomerDAO customerDAO = DAOFactory.getInstance().getDAO(DAOType.CUSTOMER);
      return customerDAO.update(new Customer(customerId, name, address));

  }

}
