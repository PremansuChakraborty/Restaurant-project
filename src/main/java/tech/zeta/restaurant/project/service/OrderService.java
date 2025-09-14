package tech.zeta.restaurant.project.service;

import lombok.extern.slf4j.Slf4j;
import tech.zeta.restaurant.project.dao.*;
import tech.zeta.restaurant.project.models.*;
import tech.zeta.restaurant.project.models.enums.*;


import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
@Slf4j
public class OrderService {
    private int waiterID;
    private RestaurantStaff waiter;

    private final RestaurantStaffDAO restaurantStaffDAO;
    private final MenuItemDAO menuItemDAO;
    private final OrderDetailsDAO orderDetailsDAO;
    private final OrderItemsDAO orderItemsDAO;
    private final TableInfoDAO tableInfoDAO;

    private final Scanner sc;
    private final int tableID;
    private final int custId;

    private int orderID;
    private List<Integer> orderItemsList;
    double totalBill=0;

    public OrderService(Scanner sc, int tableID, int custId) {
        this.restaurantStaffDAO = new RestaurantStaffDAOImp();
        this.menuItemDAO = new MenuItemDAOImp();
        this.orderDetailsDAO = new OrderDetailsDAOImp();
        this.orderItemsDAO = new OrderItemsDAOImp();
        this.tableInfoDAO = new TableInfoDAOImp();

        this.waiterID = restaurantStaffDAO.findAvailableRestaurantStaffByRole(RestaurantStaffEnum.WAITER);
        this.waiter = restaurantStaffDAO.getRestaurantStaffByID(waiterID);

        this.sc = sc;
        this.tableID = tableID;
        this.custId = custId;
    }

    public void takeOrder() {
        restaurantStaffDAO.updateRestaurantStaffStatus(waiterID, RestaurantStaffStatus.BUSY);

        System.out.printf("Hello customer! I am %s at your service.%nReady for the order.%nHere is the menu:%n", waiter.getUserName());

        List<MenuItem> list = menuItemDAO.getMenu();
        for (MenuItem item : list) {
            System.out.println(item);
        }

        // create order
        orderID = orderDetailsDAO.addOrder(new OrderDetails(custId, tableID, waiterID));
        orderItemsList = new ArrayList<>();

        while (true) {
            System.out.print("Enter item id or 0 to End your order: ");
            int itemId = sc.nextInt();
            if (itemId == 0) {
                completeOrder(); // cleanup before exit
                return;
            }

            System.out.print("Enter quantity: ");
            int quantity = sc.nextInt();

            int orderItemId = orderItemsDAO.addOrderItem(new OrderItems(orderID, itemId, quantity));
            orderItemsList.add(orderItemId);

            startPreparingOrder();
        }
    }

    private void startPreparingOrder() {
        System.out.println("Our Kitchen staff is preparing your order...");

        for (int orderItemId : orderItemsList) {
            OrderItems orderItem = orderItemsDAO.getOrderItemById(orderItemId);
            if (orderItem == null) {
                System.err.printf("OrderItem with id %d not found!%n", orderItemId);
                continue;
            }

            MenuItem menuItem = menuItemDAO.getMenuItemById(orderItem.getMenuItem());
            if (menuItem == null) {
                System.err.printf("MenuItem with id %d not found!%n", orderItem.getMenuItem());
                continue;
            }

            System.out.printf("Preparing %s ... %d portion(s)...%n",
                    menuItem.getName(), orderItem.getQuantity());
            try {
                Thread.sleep(2000L * orderItem.getQuantity()); // simulate cooking time
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.info("Order preparation interrupted!");
                return;
            }
            boolean updated = orderItemsDAO.updateStatus(orderItemId, OrderItemEnum.READY);
            if (updated) {
                System.out.printf("%s is ready!%n", menuItem.getName());
                servingOrder(orderItemId);
            } else {
                System.err.printf("Failed to update status for orderItem %d%n", orderItemId);
            }
        }
    }


    private void servingOrder(int orderItemId) {
        orderItemsDAO.updateStatus(orderItemId, OrderItemEnum.SERVED);
        OrderItems orderItems = orderItemsDAO.getOrderItemById(orderItemId);
        MenuItem menuItem = menuItemDAO.getMenuItemById(orderItems.getMenuItem());
        totalBill+=menuItem.getPrice()*orderItems.getQuantity();
        System.out.printf("%s is served at your table (%d) by %s.%n",
                menuItem.getName(), tableID, waiter.getUserName());
    }

    private void completeOrder() {
        Billing billing=new Billing(orderID,totalBill);
        BillingDAO billingDAO=new BillingDAOImp();
        int billId=billingDAO.createBill(billing);
        System.out.println("Total bill is: "+totalBill);
        System.out.println("Pay the bill.....");
        sc.next();
        System.out.println("Thank u visit again.......");
        billingDAO.updatePaymentStatus(billId,PaymentStatus.PAID);
        restaurantStaffDAO.updateRestaurantStaffStatus(waiterID, RestaurantStaffStatus.ACTIVE);
        orderDetailsDAO.updateOrderStatus(orderID, OrderEnum.SERVED);
        tableInfoDAO.updateStatus(tableID, TableInfoEnum.ACTIVE);
    }
}
