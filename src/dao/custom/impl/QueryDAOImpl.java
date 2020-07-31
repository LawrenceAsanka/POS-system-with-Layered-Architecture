package dao.custom.impl;

import java.sql.Date;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import dao.CrudUtil;
import dao.custom.QueryDAO;
import entity.CustomEntity;

public class QueryDAOImpl implements QueryDAO {

  @Override
  public CustomEntity getOrderDetail(String orderId) throws Exception {

     /* Connection connection = DBConnection.getInstance().getConnection();
      PreparedStatement pstm = connection
          .prepareStatement("SELECT c.name,o.id,o.date FROM `Order` o INNER JOIN Customer c on o.customerId = c.id WHERE o.id=?");
      pstm.setObject(1,orderId);*/
    ResultSet resultSet = CrudUtil
        .execute("SELECT c.name,o.id,o.date FROM `Order` o INNER JOIN Customer c on o.customerId = c.id WHERE o.id=?",
            orderId);
    if (resultSet.next()) {
      return new CustomEntity(resultSet.getString(1),
          resultSet.getString(2), resultSet.getDate(3));
    }
    return null;

  }

  @Override
  public CustomEntity getOrderDetail2(String orderId) throws Exception {

    ResultSet resultSet = CrudUtil
        .execute("SELECT c.id,c.name,o.id FROM Customer c INNER JOIN `Order` o on c.id = o.customerId WHERE o.id=?",
            orderId);
    if (resultSet.next()) {
      return new CustomEntity(resultSet.getString(1),
          resultSet.getString(2), resultSet.getString(3));
    }
    return null;

  }

  @Override
  public CustomEntity getOrderDetail3(String orderId) throws Exception {

    ResultSet resultSet = CrudUtil.execute(
        "SELECT o.id,o.date,c.id,c.name,(SUM(od.qty*od.unitPrice)) as total  FROM `Order` o INNER JOIN OrderDetail od on o.id = od.orderId INNER JOIN Customer c "
            + "on o.customerId = c.id WHERE o.id=? GROUP BY o.id", orderId);
    if (resultSet.next()) {
      Date date = resultSet.getDate(2);
      return new CustomEntity(resultSet.getString(1),
          date, resultSet.getString(3),
          resultSet.getString(4), resultSet.getBigDecimal(5));
    }
    return null;

  }

  @Override
  public List<CustomEntity> getOrderDetail4(String orderId) throws Exception {

    List<CustomEntity> arr = new ArrayList<>();
    ResultSet resultSet = CrudUtil.execute(
        "SELECT o.id,o.date,c.id,c.name,(SUM(od.qty*od.unitPrice)) as total  FROM `Order` o INNER JOIN OrderDetail od on o.id = od.orderId INNER JOIN Customer c "
            + "on o.customerId = c.id WHERE o.id=? GROUP BY o.id", orderId);
    if (resultSet.next()) {
      Date date = resultSet.getDate(2);
      arr.add(new CustomEntity(resultSet.getString(1),
          date, resultSet.getString(3),
          resultSet.getString(4), resultSet.getBigDecimal(5)));
    }
    return arr;

  }
}
