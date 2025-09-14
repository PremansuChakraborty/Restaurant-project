package tech.zeta.restaurant.project.service;

import java.util.Scanner;

public class AdminIntegratedServices {
    AdminServices adminServices;
    public AdminIntegratedServices(Scanner sc){
        adminServices=new AdminServices(sc);
        if(adminServices.login()){
            System.out.print("1. addMenu  2. Generate Report : ");
            int choice=sc.nextInt();
            switch (choice){
                case 1: adminServices.addMenuItem();
                case 2: adminServices.generateBillReport();
            }
        }
    }
}
