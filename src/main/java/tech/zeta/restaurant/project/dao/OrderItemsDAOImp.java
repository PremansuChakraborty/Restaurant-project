package tech.zeta.restaurant.project.dao;

import lombok.extern.slf4j.Slf4j;
import tech.zeta.restaurant.project.models.OrderItems;
import tech.zeta.restaurant.project.models.enums.OrderItemEnum;
import tech.zeta.restaurant.project.util.DBConnection;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
@Slf4j
public class OrderItemsDAOImp implements OrderItemsDAO{
    @Override
    public int addOrderItem(OrderItems orderItem) {
        try(Connection connection= DBConnection.getInstance().getConnection()){
            String sql="insert into orderitem (order_id, menu_item_id, quantity, status) values(?,?,?,?) RETURNING id;";
            PreparedStatement preparedStatement=connection.prepareStatement(sql);
            preparedStatement.setInt(1,orderItem.getOrderId());
            preparedStatement.setInt(2,orderItem.getMenuItem());
            preparedStatement.setInt(3, orderItem.getQuantity());
            preparedStatement.setString(4,orderItem.getStatus().name());
            ResultSet resultSet=preparedStatement.executeQuery();
            if(resultSet.next()){
                log.info("Successfully add a OrderItem........");
                return resultSet.getInt("id");
            }
            else log.error("Something went wrong........");
        }catch (IOException | SQLException e){
            log.error("OrderItemsDAOImp connection error.........");
        }
        return 0;
    }

    @Override
    public OrderItems getOrderItemById(int id) {
        try(Connection connection= DBConnection.getInstance().getConnection()){
            String sql="select * from orderitem where id=?;";
            PreparedStatement preparedStatement=connection.prepareStatement(sql);
            preparedStatement.setInt(1,id);
            ResultSet resultSet =preparedStatement.executeQuery();
            if (resultSet.next()){
                int orderItemId=resultSet.getInt("id");
                int orderId=resultSet.getInt("order_id");
                int menu_item_id=resultSet.getInt("menu_item_id");
                int quantity=resultSet.getInt("quantity");
                String status=resultSet.getString("status");
                OrderItems orderItem=new OrderItems(orderId,menu_item_id,quantity);
                orderItem.setId(orderItemId);
                orderItem.setStatus(OrderItemEnum.valueOf(status));
                return orderItem;
            }
        }catch (IOException | SQLException e){
            log.error("OrderItemsDAOImp connection error.........");
        }
        return null;
    }

    @Override
    public List<OrderItems> getOrderItemsByOrder(int order_id) {
        List<OrderItems> list=new ArrayList<>();
        try(Connection connection= DBConnection.getInstance().getConnection()){
            String sql="select * from orderitem where order_id=?;";
            PreparedStatement preparedStatement=connection.prepareStatement(sql);
            preparedStatement.setInt(1,order_id);
            ResultSet resultSet =preparedStatement.executeQuery();
            while (resultSet.next()){
                int orderItemId=resultSet.getInt("id");
                int orderId=resultSet.getInt("order_id");
                int menu_item_id=resultSet.getInt("menu_item_id");
                int quantity=resultSet.getInt("quantity");
                String status=resultSet.getString("status");
                OrderItems orderItem=new OrderItems(orderId,menu_item_id,quantity);
                orderItem.setId(orderItemId);
                orderItem.setStatus(OrderItemEnum.valueOf(status));
                list.add(orderItem);
            }
            return list;
        }catch (IOException | SQLException e){
            log.error("OrderItemsDAOImp connection error.........");
        }
        return list;
    }

    @Override
    public boolean updateStatus(int id, OrderItemEnum status) {
        try(Connection connection= DBConnection.getInstance().getConnection()){
            String sql="update orderitem set status=? where id=?;";
            PreparedStatement preparedStatement=connection.prepareStatement(sql);
            preparedStatement.setString(1,status.name());
            preparedStatement.setInt(2,id);
            int row =preparedStatement.executeUpdate();
            if(row>0){
                log.info("Successfully update a OrderItem........");
                return true;
            }
            else log.error("Something went wrong........");
        }catch (IOException | SQLException e){
            log.error("OrderItemsDAOImp connection error.........");
        }
        return false;
    }
}
