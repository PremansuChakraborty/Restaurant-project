package tech.zeta.restaurant.project.models;

import lombok.Getter;
import lombok.Setter;
import tech.zeta.restaurant.project.models.enums.OrderEnum;

import java.util.LinkedList;
import java.util.Queue;

@Getter @Setter
public class OrderDetails {
    private int id;
    private final int custId;
    private final int tableId;
    private final int waiter_id;
    private OrderEnum status;
    private Queue<Integer> orderItemsList;

    public OrderDetails(int custId, int tableId, int waiter_id) {
        this.custId = custId;
        this.tableId = tableId;
        this.waiter_id = waiter_id;
        this.status=OrderEnum.PENDING;
        this.orderItemsList=new LinkedList<>();
    }
}
