package tech.zeta.restaurant.project.dao;

import tech.zeta.restaurant.project.models.OrderDetails;
import tech.zeta.restaurant.project.models.enums.OrderEnum;

import java.util.*;

public interface OrderDetailsDAO {
    int addOrder(OrderDetails order);
    OrderDetails getOrderById(int orderId);
    List<OrderDetails> getAllOrders();
    List<OrderDetails> getOrdersByCustomer(int id);
    boolean updateOrderStatus(int orderId, OrderEnum status);
}
