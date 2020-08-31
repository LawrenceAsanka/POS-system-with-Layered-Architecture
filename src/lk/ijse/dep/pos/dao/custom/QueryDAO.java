package lk.ijse.dep.pos.dao.custom;

import java.util.List;

import lk.ijse.dep.pos.dao.SuperDAO;
import lk.ijse.dep.pos.entity.CustomEntity;

public interface QueryDAO extends SuperDAO {

  List<CustomEntity> getOrderDetail() throws Exception;

}
