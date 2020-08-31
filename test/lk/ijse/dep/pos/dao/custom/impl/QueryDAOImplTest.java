package lk.ijse.dep.pos.dao.custom.impl;


import java.util.List;

import lk.ijse.dep.pos.dao.DAOFactory;
import lk.ijse.dep.pos.dao.DAOType;
import lk.ijse.dep.pos.dao.custom.QueryDAO;
import lk.ijse.dep.pos.entity.CustomEntity;

class QueryDAOImplTest {

  public static void main(String[] args) throws Exception{
    QueryDAO queryDAO = DAOFactory.getInstance().getDAO(DAOType.QUERY);
 /*   CustomEntity ce = queryDAO.getOrderDetail("OD001");
    System.out.println(ce.getCustomerName());
    System.out.println(ce.getOrderId());
    System.out.println(ce.getOrderDate());*/

    /*CustomEntity cee = queryDAO.getOrderDetail2("OD001");
    System.out.println(cee.getCustomerId());
    System.out.println(cee.getCustomerName());
    System.out.println(cee.getOrderId());*/

    //CustomEntity cee = queryDAO.getOrderDetail4("OD001");
    List<CustomEntity> list = queryDAO.getOrderDetail4("OD003");
    System.out.println(list);
   /* System.out.println(cee.getOrderDate());
    System.out.println(cee.getCustomerId());
    System.out.println(cee.getCustomerName());
    System.out.println(cee.getTotal());*/



  }


}