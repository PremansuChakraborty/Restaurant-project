package tech.zeta.restaurant.project.dao;

import tech.zeta.restaurant.project.models.Customer;

import java.util.List;

public interface CustomerDAO {
    List<Customer> getAllCustomer();
    boolean addCustomer(Customer customer);
    Customer getCustomerByEmail(String Email);

    boolean validateCustomer(String email, String password);
}
