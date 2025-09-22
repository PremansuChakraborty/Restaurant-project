package tech.zeta;

import tech.zeta.restaurant.project.service.AdminIntegratedServices;
import tech.zeta.restaurant.project.service.CustomerIntegratedServices;

import java.util.Scanner;

public class RestaurantManagement {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        System.out.print("1. Customer login  2. Admin login: ");
        int choice=sc.nextInt();
        switch (choice){
            case 1: new CustomerIntegratedServices().controlsFlowOfCustomer(sc);
            case 2: new AdminIntegratedServices(sc);
        }
    }
}
