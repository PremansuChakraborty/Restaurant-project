package tech.zeta.restaurant.project.models;

import lombok.Getter;
import lombok.Setter;
import tech.zeta.restaurant.project.models.enums.OrderItemEnum;

@Getter @Setter
public class OrderItems {
//    CREATE TABLE OrderItem (
//    id SERIAL PRIMARY KEY,
//    order_id INT REFERENCES Orders(id),
//    menu_item_id INT REFERENCES MenuItem(id),
//    quantity INT NOT NULL,
//    status VARCHAR(20) DEFAULT 'ORDERED'
//        CHECK (status IN ('ORDERED', 'PREPARING', 'READY', 'SERVED', 'CANCELLED'))
//);
    private int id;
    private final int orderId;
    private final int MenuItem;
    private int quantity;
    private OrderItemEnum status;

    public OrderItems(int orderId, int menuItem, int quantity) {
        this.orderId = orderId;
        MenuItem = menuItem;
        this.quantity = quantity;
        this.status = OrderItemEnum.ORDERED;
    }
}
