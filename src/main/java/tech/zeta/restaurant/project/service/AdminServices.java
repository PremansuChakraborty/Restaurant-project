package tech.zeta.restaurant.project.service;


import tech.zeta.restaurant.project.dao.*;
import tech.zeta.restaurant.project.models.RestaurantStaff;
import tech.zeta.restaurant.project.models.enums.RestaurantStaffEnum;
import tech.zeta.restaurant.project.models.Billing;
import tech.zeta.restaurant.project.models.MenuItem;

import java.util.List;
import java.util.Scanner;

public class AdminServices {

    private RestaurantStaff admin;
    private final String adminUsername;
    private final String adminPassword;

    private final Scanner sc;
    final MenuItemDAO menuItemDAO;
    private final BillingDAO billingDAO;

    public AdminServices(Scanner sc) {
        this.sc = sc;
        this.menuItemDAO = new MenuItemDAOImp();
        this.billingDAO = new BillingDAOImp();
        RestaurantStaffDAO restaurantStaffDAO = new RestaurantStaffDAOImp();
        int adminId = restaurantStaffDAO.findAvailableRestaurantStaffByRole(RestaurantStaffEnum.ADMIN);
        this.admin = restaurantStaffDAO.getRestaurantStaffByID(adminId);

        if (this.admin == null) {
            throw new RuntimeException("No admin found in database with role ADMIN!");
        }

        this.adminUsername = admin.getUserName();
        this.adminPassword = admin.getPassword();
    }

    public boolean login() {
        System.out.print("Enter Admin username: ");
        String username = sc.next();
        System.out.print("Enter Admin password: ");
        String password = sc.next();

        if (username.equals(adminUsername) && password.equals(adminPassword)) {
            System.out.println("Login successful!");
            return true;
        } else {
            System.out.println("Invalid credentials!");
            return false;
        }
    }
    public void addMenuItem() {
        List<MenuItem> menuList = menuItemDAO.getMenu();
        System.out.println("Current Menu:");
        for (MenuItem item : menuList) {
            System.out.println(item);
        }

        System.out.print("Enter category: ");
        String category = sc.nextLine();
        sc.nextLine();

        System.out.print("Enter new name: ");
        String name = sc.nextLine();

        System.out.print("Enter new price: ");
        double price = sc.nextDouble();
        sc.nextLine();

        MenuItem itemToUpdate = new MenuItem(name,price,category);
        boolean success = menuItemDAO.addMenuItem(itemToUpdate,RestaurantStaffEnum.ADMIN);

        if (success) System.out.println("Menu updated successfully!");
        else System.out.println("Failed to update menu.");
    }

    // --- Generate Bill Report ---
    public void generateBillReport() {
        List<Billing> bills = billingDAO.getAllBills();
        System.out.println("---- Bill Report ----");
        for (Billing bill : bills) {
            System.out.printf("Bill ID: %d | Order ID: %d | Amount: %.2f | Status: %s",
                    bill.getId(), bill.getOrderId(), bill.getTotal_amount(), bill.getPayment_status());
        }
    }
}
