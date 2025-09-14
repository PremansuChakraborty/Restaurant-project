package tech.zeta.restaurant.project.service;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import tech.zeta.restaurant.project.dao.CustomerDAO;
import tech.zeta.restaurant.project.dao.CustomerDAOImp;
import tech.zeta.restaurant.project.models.Customer;
import tech.zeta.restaurant.project.models.Booking;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

@Getter @Setter @Slf4j
public class CustomerIntegratedServices {
    private Customer customer;
    private Booking booking;
    private CustomerDAO customerDAO=new CustomerDAOImp();
    BookingService bookingService=new BookingService();
    OrderService orderService;
    Scanner sc;

    public void controlsFlowOfCustomer(Scanner sc) {
        this.sc = sc;
        System.out.print("1. Register /  2. Login....    Enter 1 or 2: ");
        int choice = sc.nextInt();
        switch (choice) {
            case 1: {
                System.out.print("Enter your name : ");
                String name = sc.next().trim();
                System.out.print("Enter your phone : ");
                String phone = sc.next().trim();
                System.out.print("Enter your email : ");
                String email = sc.next().trim();
                System.out.print("Enter your password : ");
                String password = sc.next().trim();
                Customer temp= new Customer(name, phone, email, password);
                if(customerDAO.addCustomer(temp)){
                    log.info("Customer Registration Successful........");
                }
                else log.info("Customer Registration Failed........");
//                System.out.println("Registration successful");
            };
            case 2:{
                System.out.print("Validate your email for login : ");
                String email = sc.next().trim();
                System.out.print("Validate your password for login: ");
                String password = sc.next().trim();
                if(customerDAO.validateCustomer(email,password)){
                    customer=customerDAO.getCustomerByEmail(email);
                    log.info("Customer Login Successful........");
                }
                else log.info("Customer Login Failed........");
                this.moreServices(sc);
                break;
            }
            default: controlsFlowOfCustomer(sc);
        }
    }

    public void moreServices(Scanner sc){
        System.out.print("Choose 1. New Booking  2. Check bookings 3. check In : ");
        int choice=sc.nextInt();
        switch (choice){
            case 1:{
                System.out.print("Enter capacity: ");
                int capacity=sc.nextInt();

                int tableId=bookingService.bestTableAccordingToCapacity(capacity);

                System.out.println("Enter date (yyyy-MM-dd): ");
                String date = sc.next();
                System.out.println("Enter time (HH:mm) follow 24-hour time format: ");
                String time = sc.next();
                LocalDateTime bookingTime = LocalDateTime.parse(date + " " + time,
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

                Booking booking=new Booking(customer.getId(),tableId,bookingTime);
                bookingService.createBooking(booking);
                break;
            }
            case 2:{
                List<Booking> list=bookingService.getBookingsByCustomer(customer.getId());
                for(Booking booking: list) System.out.println(booking);
                break;
            }
            case 3:{
               CheckInServices checkInServices=new CheckInServices(sc);
               int bookingId=checkInServices.checkForBooking(customer.getId());
               if(bookingId==0) log.info("No booking available for you..........");
               else{
                  booking=bookingService.getBookingById(bookingId);
                  orderService=new OrderService(sc,booking.getTableID(),booking.getCustomerID());
                  orderService.takeOrder();
               }
            }
        }
    }
}
