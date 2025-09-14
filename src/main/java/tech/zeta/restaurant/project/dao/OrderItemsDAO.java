package tech.zeta.restaurant.project.dao;

import tech.zeta.restaurant.project.models.OrderItems;
import tech.zeta.restaurant.project.models.enums.OrderItemEnum;

import java.util.List;

public interface OrderItemsDAO {
    int addOrderItem(OrderItems orderItem);
    OrderItems getOrderItemById(int id);
    List<OrderItems> getOrderItemsByOrder(int orderId);
    boolean updateStatus(int id, OrderItemEnum status);
}
