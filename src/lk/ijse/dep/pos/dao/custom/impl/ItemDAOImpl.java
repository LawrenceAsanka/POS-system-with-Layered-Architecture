package lk.ijse.dep.pos.dao.custom.impl;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import lk.ijse.dep.pos.dao.CrudUtil;
import lk.ijse.dep.pos.dao.custom.ItemDAO;
import lk.ijse.dep.pos.entity.Item;

public class ItemDAOImpl implements ItemDAO {


  public String getLastItemCode() throws Exception{

            /*Connection connection = DBConnection.getInstance().getConnection();
            Statement stm = connection.createStatement();*/
      ResultSet rst = CrudUtil.execute("SELECT * FROM Item ORDER BY code DESC LIMIT 1");
      if (!rst.next()) {
        return null;
      } else {
        return rst.getString(1);
      }

  }

  @Override
  public List<Item> findAll() throws Exception{

       /*     Connection connection = DBConnection.getInstance().getConnection();
            Statement stm = connection.createStatement();*/
      ResultSet rst = CrudUtil.execute("SELECT * FROM Item");
      List<Item> items = new ArrayList<>();
      while (rst.next()) {
        items.add(new Item(rst.getString(1),
            rst.getString(2),
            rst.getBigDecimal(3),
            rst.getInt(4)));
      }
      return items;

  }

  @Override
  public Item find(String key) throws Exception{

            /*Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement pstm = connection.prepareStatement("SELECT * FROM Item WHERE code=?");
            pstm.setObject(1, key);*/
      ResultSet rst = CrudUtil.execute("SELECT * FROM Item WHERE code=?", key);
      if (rst.next()) {
        return new Item(rst.getString(1),
            rst.getString(2),
            rst.getBigDecimal(3),
            rst.getInt(4));
      }
      return null;

  }

  @Override
  public boolean save(Item item) throws Exception{

          /*  Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement pstm = connection.prepareStatement("INSERT INTO Item VALUES (?,?,?,?)");
            pstm.setObject(1, item.getCode());
            pstm.setObject(2, item.getDescription());
            pstm.setObject(3, item.getUnitPrice());
            pstm.setObject(4, item.getQtyOnHand());*/
      return CrudUtil
          .execute("INSERT INTO Item VALUES (?,?,?,?)", item.getCode(), item.getDescription(), item.getUnitPrice(),
              item.getQtyOnHand());

  }

  @Override
  public boolean update(Item item) throws Exception{

            /*Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement pstm = connection.prepareStatement("UPDATE Item SET description=?, unitPrice=?, qtyOnHand=? WHERE code=?");
            pstm.setObject(4, item.getCode());
            pstm.setObject(1, item.getDescription());
            pstm.setObject(2, item.getUnitPrice());
            pstm.setObject(3, item.getQtyOnHand());*/
      return CrudUtil
          .execute("UPDATE Item SET description=?, unitPrice=?, qtyOnHand=? WHERE code=?", item.getDescription(),
              item.getUnitPrice(), item.getQtyOnHand(), item.getCode());

  }

  @Override
  public boolean delete(String key) throws Exception{

           /* Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement pstm = connection.prepareStatement("DELETE FROM Item WHERE code=?");
            pstm.setObject(1, key);*/
      return CrudUtil.execute("DELETE FROM Item WHERE code=?", key);

  }
}
