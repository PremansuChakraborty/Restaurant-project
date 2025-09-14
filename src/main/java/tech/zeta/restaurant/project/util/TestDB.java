package tech.zeta.restaurant.project.util;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
@Slf4j
public class TestDB {
    public static void main(String[] args) {
        try {
            Connection connection = DBConnection.getInstance().getConnection();
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
                    System.out.println(id + " | " + name + " | " + phone + " | " + email + " | " + password);
                }
            }
        }catch (SQLException exception){
            log.error("Some Error occurred: ");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
