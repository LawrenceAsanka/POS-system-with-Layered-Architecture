package lk.ijse.dep.pos.dao.custom.impl;

import lk.ijse.dep.pos.dao.CrudUtil;
import lk.ijse.dep.pos.dao.custom.QueryDAO;
import lk.ijse.dep.pos.entity.CustomEntity;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class QueryDAOImpl implements QueryDAO {


    @Override
    public List<CustomEntity> getOrderDetail() throws Exception {
        List<CustomEntity> orderDetails = new ArrayList<>();
        ResultSet resultSet = CrudUtil.execute("SELECT o.id,o.date,c.id,c.name,(SUM(od.qty*od.unitPrice)) as total  FROM `Order` o\n" +
                "INNER JOIN OrderDetail od on o.id = od.orderId\n" +
                "INNER JOIN Customer c on o.customerId = c.id\n" +
                "GROUP BY o.id");

        while (resultSet.next()) {
            orderDetails.add(new CustomEntity(resultSet.getString(1), resultSet.getDate(2), resultSet.getString(3),
                    resultSet.getString(4), resultSet.getBigDecimal(5)));
        }
        return orderDetails;
    }

}
