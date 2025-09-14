package tech.zeta.restaurant.project.dao;

import lombok.extern.slf4j.Slf4j;
import tech.zeta.restaurant.project.models.OrderDetails;
import tech.zeta.restaurant.project.models.enums.OrderEnum;
import tech.zeta.restaurant.project.util.DBConnection;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
@Slf4j
public class OrderDetailsDAOImp implements OrderDetailsDAO{
    @Override
    public int addOrder(OrderDetails order) {
        try(Connection connection= DBConnection.getInstance().getConnection()){
            String sql="insert into orders (customer_id, table_id, waiter_id, status) values(?,?,?,?) RETURNING id;";
            PreparedStatement preparedStatement=connection.prepareStatement(sql);
            preparedStatement.setInt(1,order.getCustId());
            preparedStatement.setInt(2,order.getTableId());
            preparedStatement.setInt(3,order.getWaiter_id());
            preparedStatement.setString(4,order.getStatus().name());
            ResultSet resultSet=preparedStatement.executeQuery();
            if(resultSet.next()){
                log.info("Successfully add a Order........");
                return resultSet.getInt("id");
            }
            else log.error("Something went wrong........");
        }catch (IOException | SQLException e){
            log.error("OrderDetailsDAOImp connection error.........");
        }
        return 0;
    }

    @Override
    public OrderDetails getOrderById(int orderId) {
        try(Connection connection= DBConnection.getInstance().getConnection()){
            String sql="select * from orders where id=?;";
            PreparedStatement preparedStatement=connection.prepareStatement(sql);
            preparedStatement.setInt(1,orderId);
            ResultSet resultSet =preparedStatement.executeQuery();
            if (resultSet.next()){
                int id=resultSet.getInt("id");
                int custId=resultSet.getInt("customer_id");
                int tableId=resultSet.getInt("table_id");
                int waiterId=resultSet.getInt("waiter_id");
                String status=resultSet.getString("status");
                OrderDetails orderDetails=new OrderDetails(custId,tableId,waiterId);
                orderDetails.setId(id);
                orderDetails.setStatus(OrderEnum.valueOf(status));
                return orderDetails;
            }
        }catch (IOException | SQLException e){
            log.error("OrderDetailsDAOImp connection error.........");
        }
        return null;
    }

    @Override
    public List<OrderDetails> getAllOrders() {
        List<OrderDetails> demo=new ArrayList<>();
        try(Connection connection= DBConnection.getInstance().getConnection()){
            String sql="select * from orders;";
            PreparedStatement preparedStatement=connection.prepareStatement(sql);
            ResultSet resultSet =preparedStatement.executeQuery();
            while (resultSet.next()){
                int id=resultSet.getInt("id");
                int cusId=resultSet.getInt("customer_id");
                int tableId=resultSet.getInt("table_id");
                int waiterId=resultSet.getInt("waiter_id");
                String status=resultSet.getString("status");
                OrderDetails orderDetails=new OrderDetails(cusId,tableId,waiterId);
                orderDetails.setId(id);
                orderDetails.setStatus(OrderEnum.valueOf(status));
                demo.add(orderDetails);
            }
        }catch (IOException | SQLException e){
            log.error("OrderDetailsDAOImp connection error.........");
        }
        return demo;
    }

    @Override
    public List<OrderDetails> getOrdersByCustomer(int custId) {
        List<OrderDetails> demo=new ArrayList<>();
        try(Connection connection= DBConnection.getInstance().getConnection()){
            String sql="select * from orders where customer_id=?;";
            PreparedStatement preparedStatement=connection.prepareStatement(sql);
            preparedStatement.setInt(1,custId);
            ResultSet resultSet =preparedStatement.executeQuery();
            while (resultSet.next()){
                int id=resultSet.getInt("id");
                int cusId=resultSet.getInt("customer_id");
                int tableId=resultSet.getInt("table_id");
                int waiterId=resultSet.getInt("waiter_id");
                String status=resultSet.getString("status");
                OrderDetails orderDetails=new OrderDetails(cusId,tableId,waiterId);
                orderDetails.setId(id);
                orderDetails.setStatus(OrderEnum.valueOf(status));
                demo.add(orderDetails);
            }
        }catch (IOException | SQLException e){
            log.error("OrderDetailsDAOImp connection error.........");
        }
        return demo;
    }

    @Override
    public boolean updateOrderStatus(int orderId, OrderEnum status) {
        try(Connection connection= DBConnection.getInstance().getConnection()){
            String sql="update orders set status=? where id=?;";
            PreparedStatement preparedStatement=connection.prepareStatement(sql);
            preparedStatement.setString(1,status.name());
            preparedStatement.setInt(2,orderId);
            int row =preparedStatement.executeUpdate();
            if(row>0){
                log.info("Successfully update a Order........");
                return true;
            }
            else log.error("Something went wrong........");
        }catch (IOException | SQLException e){
            log.error("OrderDetailsDAOImp connection error.........");
        }
        return false;
    }
}
