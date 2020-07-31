package business;

import java.util.List;

import util.OrderDetailTM;
import util.OrderTM;

public interface OrderBO extends SuperBO{

  public  String getNewOrderId() throws Exception;
  public  boolean placeOrder(OrderTM order, List<OrderDetailTM> orderDetails) throws Exception;
}
