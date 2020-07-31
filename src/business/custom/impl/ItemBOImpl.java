package business.custom.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import business.ItemBO;
import dao.DAOFactory;
import dao.DAOType;
import dao.custom.ItemDAO;
import entity.Item;
import util.ItemTM;

public class ItemBOImpl implements ItemBO {

  public  String getNewItemCode() throws Exception{

      ItemDAO itemDAO = DAOFactory.getInstance().getDAO(DAOType.ITEM);
      String lastItemCode = itemDAO.getLastItemCode();
      if (lastItemCode == null) {
        return "I001";
      } else {
        int maxId = Integer.parseInt(lastItemCode.replace("I", ""));
        maxId = maxId + 1;
        String id = "";
        if (maxId < 10) {
          id = "I00" + maxId;
        } else if (maxId < 100) {
          id = "I0" + maxId;
        } else {
          id = "I" + maxId;
        }
        return id;
      }

  }

  public  List<ItemTM> getAllItems() throws Exception{

      ItemDAO itemDAO = DAOFactory.getInstance().getDAO(DAOType.ITEM);
      List<Item> allItems = itemDAO.findAll();
      List<ItemTM> items = new ArrayList<>();
      for (Item item : allItems) {
        items.add(new ItemTM(item.getCode(), item.getDescription(), item.getQtyOnHand(),
            item.getUnitPrice().doubleValue()));
      }
      return items;

  }

  public  boolean saveItem(String code, String description, int qtyOnHand, double unitPrice) throws Exception{

      ItemDAO itemDAO = DAOFactory.getInstance().getDAO(DAOType.ITEM);
      return itemDAO.save(new Item(code, description, BigDecimal.valueOf(unitPrice), qtyOnHand));

  }

  public  boolean deleteItem(String itemCode) throws Exception{

      ItemDAO itemDAO = DAOFactory.getInstance().getDAO(DAOType.ITEM);
      return itemDAO.delete(itemCode);

  }

  public  boolean updateItem(String description, int qtyOnHand, double unitPrice, String itemCode) throws Exception{

      ItemDAO itemDAO = DAOFactory.getInstance().getDAO(DAOType.ITEM);
      return itemDAO.update(new Item(itemCode, description,
          BigDecimal.valueOf(unitPrice), qtyOnHand));

  }
}
