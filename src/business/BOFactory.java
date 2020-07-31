package business;

import business.custom.impl.CustomerBOImpl;
import business.custom.impl.ItemBOImpl;
import business.custom.impl.OrderBOImpl;
import dao.custom.impl.CustomerDAOImpl;
import dao.custom.impl.ItemDAOImpl;
import dao.custom.impl.OrderDAOImpl;
import dao.custom.impl.OrderDetailDAOImpl;
import dao.custom.impl.QueryDAOImpl;

public class BOFactory {

  private static BOFactory boFactory;

  private BOFactory() {
  }

  public static BOFactory getInstance() {
    return (boFactory == null) ? boFactory = new BOFactory() : boFactory;
  }

  public <T extends SuperBO> T getBO(BOType boType) throws Exception {
    switch (boType) {
      case CUSTOMER:
        return (T) new CustomerBOImpl();
      case ITEM:
        return (T) new ItemBOImpl();
      case ORDER:
        return (T) new OrderBOImpl();

      default:
        return null;
    }
  }
}
