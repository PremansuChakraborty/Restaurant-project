package tech.zeta.restaurant.project.dao;

import lombok.extern.slf4j.Slf4j;
import tech.zeta.restaurant.project.models.RestaurantStaff;
import tech.zeta.restaurant.project.models.enums.RestaurantStaffEnum;
import tech.zeta.restaurant.project.models.enums.RestaurantStaffStatus;
import tech.zeta.restaurant.project.util.DBConnection;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class RestaurantStaffDAOImp implements RestaurantStaffDAO {

    @Override
    public List<RestaurantStaff> getAllRestaurantStaff() {
        List<RestaurantStaff> staffList = new ArrayList<>();
        String sql = "SELECT * FROM restaurant_staff;";

        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                String roleString = resultSet.getString("role");
                String status = resultSet.getString("status");

                RestaurantStaffEnum role = RestaurantStaffEnum.valueOf(roleString);
                RestaurantStaff staff = new RestaurantStaff(username, password, role);
                staff.setId(id);
                staff.setStatus(RestaurantStaffStatus.valueOf(status));

                staffList.add(staff);
            }

        } catch (IOException | SQLException e) {
            log.error("getAllRestaurantStaff failed: {}", e.getMessage(), e);
        }
        return staffList;
    }

    @Override
    public boolean addRestaurantStaff(RestaurantStaff restaurantStaff) {
        if (restaurantStaff == null || restaurantStaff.getUserName().isEmpty()
                || restaurantStaff.getRole() == null
                || restaurantStaff.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Restaurant staff object has missing properties");
        }

        String sql = "INSERT INTO restaurant_staff (username, password, role, status) VALUES (?, ?, ?, ?);";

        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, restaurantStaff.getUserName());
            preparedStatement.setString(2, restaurantStaff.getPassword());
            preparedStatement.setString(3, restaurantStaff.getRole().name());
            preparedStatement.setString(4, restaurantStaff.getStatus().name());

            int row = preparedStatement.executeUpdate();

            if (row > 0) {
                log.info("New restaurant staff is added: {}", restaurantStaff.getUserName());
                return true;
            }

        } catch (IOException | SQLException e) {
            log.error("addRestaurantStaff failed: {}", e.getMessage(), e);
        }
        return false;
    }

    @Override
    public RestaurantStaff getRestaurantStaffByID(int id) {
        if (id == 0) return null;

        String sql = "SELECT * FROM restaurant_staff WHERE id = ?;";

        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String username = resultSet.getString("username");
                    String password = resultSet.getString("password");
                    String roleString = resultSet.getString("role");
                    String status = resultSet.getString("status");

                    RestaurantStaffEnum role = RestaurantStaffEnum.valueOf(roleString);
                    RestaurantStaff staff = new RestaurantStaff(username, password, role);
                    staff.setId(id);
                    staff.setStatus(RestaurantStaffStatus.valueOf(status));

                    return staff;
                }
            }

        } catch (IOException | SQLException e) {
            log.error("getRestaurantStaffByID failed for id {}: {}", id, e.getMessage(), e);
        }
        return null;
    }

    @Override
    public boolean validateRestaurantStaff(int id, String password) {
        if (id == 0 || password == null || password.isEmpty()) return false;
        RestaurantStaff staff = this.getRestaurantStaffByID(id);
        return staff != null && staff.getPassword().equals(password);
    }

    @Override
    public int findAvailableRestaurantStaffByRole(RestaurantStaffEnum role) {
        String sql = "SELECT id FROM restaurant_staff WHERE role = ? AND status = ? LIMIT 1;";

        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, role.name());
            preparedStatement.setString(2, RestaurantStaffStatus.ACTIVE.name());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    log.debug("Found available staff with ID: {}", id);
                    return id;
                } else {
                    log.warn("No available staff found for role: {}", role);
                    return 0;
                }
            }

        } catch (IOException | SQLException e) {
            log.error("findAvailableRestaurantStaffByRole failed for role {}: {}", role, e.getMessage(), e);
            return 0;
        }
    }

    @Override
    public boolean updateRestaurantStaffStatus(int id, RestaurantStaffStatus status) {
        String sql = "UPDATE restaurant_staff SET status = ? WHERE id = ?;";

        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, status.name());
            preparedStatement.setInt(2, id);

            int row = preparedStatement.executeUpdate();
            if (row > 0) {
                log.info("Successfully updated status of staff ID {} to {}", id, status);
                return true;
            } else {
                log.warn("No restaurant_staff found with ID: {}", id);
            }

        } catch (IOException | SQLException e) {
            log.error("updateRestaurantStaffStatus failed for id {}: {}", id, e.getMessage(), e);
        }
        return false;
    }
}
