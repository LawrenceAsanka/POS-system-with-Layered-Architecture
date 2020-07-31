package dao.custom;

import java.util.List;

import dao.SuperDAO;
import entity.CustomEntity;

public interface QueryDAO extends SuperDAO {

  CustomEntity getOrderDetail(String orderId) throws Exception;
  CustomEntity getOrderDetail2(String orderId) throws Exception;
  CustomEntity getOrderDetail3(String orderId) throws Exception;
  List<CustomEntity> getOrderDetail4(String orderId) throws Exception;

}
