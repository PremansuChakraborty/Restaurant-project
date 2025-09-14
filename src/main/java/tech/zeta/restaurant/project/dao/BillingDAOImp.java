package tech.zeta.restaurant.project.dao;

import lombok.extern.slf4j.Slf4j;
import tech.zeta.restaurant.project.models.Billing;
import tech.zeta.restaurant.project.models.enums.PaymentStatus;
import tech.zeta.restaurant.project.util.DBConnection;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Slf4j
public class BillingDAOImp implements BillingDAO{
    @Override
    public int createBill(Billing bill) {
        String sql = "INSERT INTO bill (order_id, total_amount) VALUES (?, ?) RETURNING id;";

        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, bill.getOrderId());
            preparedStatement.setDouble(2, bill.getTotal_amount());

            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    int generatedId = rs.getInt("id");
                    log.info("Successfully created a bill with ID: {}", generatedId);
                    return generatedId;
                }
            }

        } catch (IOException | SQLException e) {
            log.error("BillingDAOImp connection error: {}", e.getMessage());
        }
        return -1; // return -1 if failed
    }


    @Override
    public Billing getBillById(int billId) {
        try(Connection connection= DBConnection.getInstance().getConnection()){
            String sql="select * from bill where id=?;";
            PreparedStatement preparedStatement=connection.prepareStatement(sql);
            preparedStatement.setInt(1,billId);
            ResultSet resultSet =preparedStatement.executeQuery();
            if (resultSet.next()){
                int id=resultSet.getInt("id");
                int orderId=resultSet.getInt("order_id");
                double amount=resultSet.getInt("total_amount");
                String status=resultSet.getString("payment_status");
                Billing bill=new Billing(orderId,amount);
                bill.setPayment_status(PaymentStatus.valueOf(status));
                bill.setId(id);
                return bill;
            }
        }catch (IOException | SQLException e){
            log.error("BillingDAOImp connection error.........");
        }
        return null;
    }

    @Override
    public Billing getBillByOrderId(int order_Id) {
        try(Connection connection= DBConnection.getInstance().getConnection()){
            String sql="select * from bill where order_id=?;";
            PreparedStatement preparedStatement=connection.prepareStatement(sql);
            preparedStatement.setInt(1,order_Id);
            ResultSet resultSet =preparedStatement.executeQuery();
            if (resultSet.next()){
                int id=resultSet.getInt("id");
                int orderId=resultSet.getInt("order_id");
                double amount=resultSet.getInt("total_amount");
                String status=resultSet.getString("payment_status");
                Billing bill=new Billing(orderId,amount);
                bill.setPayment_status(PaymentStatus.valueOf(status));
                bill.setId(id);
                return bill;
            }
        }catch (IOException | SQLException e){
            log.error("BillingDAOImp connection error.........");
        }
        return null;
    }

    @Override
    public List<Billing> getAllBills() {
        List<Billing> list=new ArrayList<>();
        try(Connection connection= DBConnection.getInstance().getConnection()){
            String sql="select * from bill;";
            PreparedStatement preparedStatement=connection.prepareStatement(sql);
            ResultSet resultSet =preparedStatement.executeQuery();
            while(resultSet.next()){
                int id=resultSet.getInt("id");
                int orderId=resultSet.getInt("order_id");
                double amount=resultSet.getInt("total_amount");
                String status=resultSet.getString("payment_status");
                Billing bill=new Billing(orderId,amount);
                bill.setPayment_status(PaymentStatus.valueOf(status));
                bill.setId(id);
                list.add(bill);
            }
            return list;
        }catch (IOException | SQLException e){
            log.error("BillingDAOImp connection error.........");
        }
        return list;
    }

    @Override
    public List<Billing> getBillsByStatus(PaymentStatus payment_status) {
        List<Billing> list=new ArrayList<>();
        try(Connection connection= DBConnection.getInstance().getConnection()){
            String sql="select * from bill payment_status=?;";
            PreparedStatement preparedStatement=connection.prepareStatement(sql);
            preparedStatement.setString(1,payment_status.name());
            ResultSet resultSet =preparedStatement.executeQuery();
            while(resultSet.next()){
                int id=resultSet.getInt("id");
                int orderId=resultSet.getInt("order_id");
                double amount=resultSet.getInt("total_amount");
                String status=resultSet.getString("payment_status");
                Billing bill=new Billing(orderId,amount);
                bill.setPayment_status(PaymentStatus.valueOf(status));
                bill.setId(id);
                list.add(bill);
            }
            return list;
        }catch (IOException | SQLException e){
            log.error("BillingDAOImp connection error.........");
        }
        return list;
    }

    @Override
    public boolean updatePaymentStatus(int billId, PaymentStatus status) {
        try(Connection connection= DBConnection.getInstance().getConnection()){
            String sql="update bill set payment_status=? where id=?";
            PreparedStatement preparedStatement=connection.prepareStatement(sql);
            preparedStatement.setString(1,status.name());
            preparedStatement.setInt(2,billId);
            int row=preparedStatement.executeUpdate();
            if(row>0){
                log.info("Successfully create a bill........");
                return true;
            }
            else log.error("Something went wrong........");
        }catch (IOException | SQLException e){
            log.error("BillingDAOImp connection error.........");
        }
        return false;
    }

    @Override
    public List<Billing> getTotalSalesForDay(LocalDateTime date) {
        List<Billing> list=new ArrayList<>();
        try(Connection connection= DBConnection.getInstance().getConnection()){
            String sql="select * from bill date=?;";
            PreparedStatement preparedStatement=connection.prepareStatement(sql);
            preparedStatement.setTimestamp(1, Timestamp.valueOf(date));
            ResultSet resultSet =preparedStatement.executeQuery();
            while(resultSet.next()){
                int id=resultSet.getInt("id");
                int orderId=resultSet.getInt("order_id");
                double amount=resultSet.getInt("total_amount");
                String status=resultSet.getString("payment_status");
                Billing bill=new Billing(orderId,amount);
                bill.setPayment_status(PaymentStatus.valueOf(status));
                bill.setId(id);
                list.add(bill);
            }
            return list;
        }catch (IOException | SQLException e){
            log.error("BillingDAOImp connection error.........");
        }
        return list;
    }
}
