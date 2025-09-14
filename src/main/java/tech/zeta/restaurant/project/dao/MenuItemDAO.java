package tech.zeta.restaurant.project.dao;

import tech.zeta.restaurant.project.models.enums.RestaurantStaffEnum;
import tech.zeta.restaurant.project.models.MenuItem;

import java.util.List;

public interface MenuItemDAO {
    void deleteMenuItem(int id, RestaurantStaffEnum restaurantStaffEnum);
    boolean addMenuItem(MenuItem item, RestaurantStaffEnum restaurantStaffEnum);
    List<MenuItem> getMenu();
    MenuItem getMenuItemById(int id);
}
