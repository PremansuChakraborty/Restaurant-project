package tech.zeta.restaurant.project.dao;

import tech.zeta.restaurant.project.models.RestaurantStaff;
import tech.zeta.restaurant.project.models.enums.RestaurantStaffEnum;
import tech.zeta.restaurant.project.models.enums.RestaurantStaffStatus;

import java.util.List;

public interface RestaurantStaffDAO {
    List<RestaurantStaff> getAllRestaurantStaff();
    boolean addRestaurantStaff(RestaurantStaff restaurantStaff);
    RestaurantStaff getRestaurantStaffByID(int id);
    boolean validateRestaurantStaff(int id, String password);
    int findAvailableRestaurantStaffByRole(RestaurantStaffEnum restaurantStaffEnum);
    boolean updateRestaurantStaffStatus(int id, RestaurantStaffStatus status);
}
