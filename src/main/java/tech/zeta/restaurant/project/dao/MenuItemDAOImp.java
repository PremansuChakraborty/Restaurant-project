package tech.zeta.restaurant.project.dao;

import lombok.extern.slf4j.Slf4j;
import tech.zeta.restaurant.project.models.enums.RestaurantStaffEnum;
import tech.zeta.restaurant.project.models.MenuItem;
import tech.zeta.restaurant.project.util.DBConnection;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
@Slf4j
public class MenuItemDAOImp implements  MenuItemDAO{
    @Override
    public void deleteMenuItem(int id, RestaurantStaffEnum restaurantStaffEnum) {
        if(!restaurantStaffEnum.name().equals("ADMIN")){
            log.error("Only Admin can access..........");
        }
        else{
            try(Connection connection= DBConnection.getInstance().getConnection()){
               String sql="delete from menuitem where id=?";
                PreparedStatement preparedStatement=connection.prepareStatement(sql);
                preparedStatement.setInt(1,id);
                int row=preparedStatement.executeUpdate();
                if(row>0){
                    log.info("Successfully delete MenuItem........");
                }
                else log.error("Something went wrong........");
            }catch (IOException | SQLException e){
                log.error("MenuItemDAOImp connection error.........");
            }
        }
    }

    @Override
    public boolean addMenuItem(MenuItem item, RestaurantStaffEnum restaurantStaffEnum) {
        if(!restaurantStaffEnum.name().equals("ADMIN")){
            log.error("Only Admin can access..........");
        }
        else{
            try(Connection connection= DBConnection.getInstance().getConnection()){
                String sql="insert into menuitem (name,price,category) values(?,?,?);";
                PreparedStatement preparedStatement=connection.prepareStatement(sql);
                preparedStatement.setString(1,item.getName());
                preparedStatement.setDouble(2,item.getPrice());
                preparedStatement.setString(3,item.getCategory());
                int row=preparedStatement.executeUpdate();
                if(row>0){
                    log.info("Successfully add a MenuItem........");
                    return true;
                }
                else log.error("Something went wrong........");
            }catch (IOException | SQLException e){
                log.error("MenuItemDAOImp connection error.........");
            }
        }
        return false;
    }

    @Override
    public List<MenuItem> getMenu() {
        List<MenuItem> demo=new ArrayList<>();
        try(Connection connection= DBConnection.getInstance().getConnection()){
            String sql="select * from menuitem;";
            PreparedStatement preparedStatement=connection.prepareStatement(sql);
            ResultSet resultSet =preparedStatement.executeQuery();
            while (resultSet.next()){
                int id=resultSet.getInt("id");
                String name=resultSet.getString("name");
                double price= resultSet.getDouble("price");
                String category=resultSet.getString("category");
                MenuItem menuItem=new MenuItem(name,price,category);
                menuItem.setId(id);
                demo.add(menuItem);
            }
        }catch (IOException | SQLException e){
            log.error("MenuItemDAOImp connection error.........");
        }
        return demo;
    }

    @Override
    public MenuItem getMenuItemById(int id) {
        try(Connection connection= DBConnection.getInstance().getConnection()){
            String sql="select * from menuitem where id=?;";
            PreparedStatement preparedStatement=connection.prepareStatement(sql);
            preparedStatement.setInt(1,id);
            ResultSet resultSet =preparedStatement.executeQuery();
            if (resultSet.next()){
                int menuItemId=resultSet.getInt("id");
                String name=resultSet.getString("name");
                double price= resultSet.getDouble("price");
                String category=resultSet.getString("category");
                MenuItem menuItem=new MenuItem(name,price,category);
                menuItem.setId(id);
                return menuItem;
            }
        }catch (IOException | SQLException e){
            log.error("MenuItemDAOImp connection error.........");
        }
        return null;
    }
}
