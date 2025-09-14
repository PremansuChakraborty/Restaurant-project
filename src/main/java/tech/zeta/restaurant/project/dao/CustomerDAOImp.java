package tech.zeta.restaurant.project.dao;

import lombok.extern.slf4j.Slf4j;
import tech.zeta.restaurant.project.models.Customer;
import tech.zeta.restaurant.project.util.DBConnection;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
@Slf4j
public class CustomerDAOImp implements CustomerDAO {
    private static Connection connection;

    @Override
    public List<Customer> getAllCustomer() {
        List<Customer> demoList = new ArrayList<>();
        try {
            connection = DBConnection.getInstance().getConnection();
            String sql = "select * from customer;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet != null) {
                while (resultSet.next()) {
                    int id = resultSet.getInt(1);
                    String name = resultSet.getString(2);
                    String phone = resultSet.getString(3);
                    String email = resultSet.getString(4);
                    String password = resultSet.getString(5);
//                    System.out.println(id + " | " + name + " | " + phone + " | " + email + " | " + password);
                    Customer customer=new Customer(name, phone, email, password);
                    customer.setId(id);
                    demoList.add(customer);
                }
            } else {
                throw new RuntimeException("CustomerDAOImp issue in resultSet");
            }
        } catch (IOException  | SQLException e) {
            log.error("CustomerDAOImp sql query fail to execute: {}", e.getMessage());
        }
        return demoList;
    }

    @Override
    public boolean addCustomer(Customer customer) throws IllegalArgumentException {
        if (customer == null || customer.getName().isEmpty() ||
        customer.getPhone().isEmpty() || customer.getEmail().isEmpty()
                || customer.getPassword().isEmpty()){
            throw new IllegalArgumentException("Customer object have some missing properties");
        }
        else {
            try {
                connection = DBConnection.getInstance().getConnection();
                String sql = "INSERT INTO CUSTOMER (name,phone,email,password) " +
                        "VALUES (?,?,?,?);";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, customer.getName());
                preparedStatement.setString(2, customer.getPhone());
                preparedStatement.setString(3, customer.getEmail());
                preparedStatement.setString(4, customer.getPassword());
                int row = preparedStatement.executeUpdate();

                if (row != 0) {
                    log.info("New customer is added");
                    return true;
                }
            } catch (IOException | SQLException e) {
                log.error("CustomerDAOImp sql query fail to execute: {}", e.getMessage());
            }
            return false;
        }
    }

    @Override
    public Customer getCustomerByEmail(String email) {
        if (email.isEmpty()) return null;
        else {
            try {
                connection = DBConnection.getInstance().getConnection();
                String sql = "SELECT * FROM customer WHERE email = ?;";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, email);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    String phone = resultSet.getString("phone");
                    String password = resultSet.getString("password");
                    Customer customer=new Customer(name, phone, email, password);
                    customer.setId(id);
                    return customer;
                }
            } catch (IOException |SQLException e) {
                log.error("CustomerDAOImp sql query fail to execute: {}", e.getMessage());
            }
            return null;
        }
    }

    @Override
    public boolean validateCustomer(String email, String password) {
        if(email.isEmpty() || password.isEmpty()) return false;
        Customer findCustomer=this.getCustomerByEmail(email);
        return findCustomer!=null && findCustomer.getPassword().equals(password);
    }
}

