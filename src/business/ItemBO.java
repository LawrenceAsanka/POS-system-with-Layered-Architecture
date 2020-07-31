package business;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import dao.DAOFactory;
import dao.DAOType;
import dao.custom.ItemDAO;
import entity.Item;
import util.ItemTM;

public class ItemBO {

  public  String getNewItemCode() {
    try {
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
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public  List<ItemTM> getAllItems() {
    try {
      ItemDAO itemDAO = DAOFactory.getInstance().getDAO(DAOType.ITEM);
      List<Item> allItems = itemDAO.findAll();
      List<ItemTM> items = new ArrayList<>();
      for (Item item : allItems) {
        items.add(new ItemTM(item.getCode(), item.getDescription(), item.getQtyOnHand(),
            item.getUnitPrice().doubleValue()));
      }
      return items;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public  boolean saveItem(String code, String description, int qtyOnHand, double unitPrice) {
    try {
      ItemDAO itemDAO = DAOFactory.getInstance().getDAO(DAOType.ITEM);
      return itemDAO.save(new Item(code, description, BigDecimal.valueOf(unitPrice), qtyOnHand));
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  public  boolean deleteItem(String itemCode) {
    try {
      ItemDAO itemDAO = DAOFactory.getInstance().getDAO(DAOType.ITEM);
      return itemDAO.delete(itemCode);
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  public  boolean updateItem(String description, int qtyOnHand, double unitPrice, String itemCode) {
    try {
      ItemDAO itemDAO = DAOFactory.getInstance().getDAO(DAOType.ITEM);
      return itemDAO.update(new Item(itemCode, description,
          BigDecimal.valueOf(unitPrice), qtyOnHand));
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }
}
