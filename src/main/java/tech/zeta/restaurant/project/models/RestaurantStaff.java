package tech.zeta.restaurant.project.models;

import lombok.Getter;
import lombok.Setter;
import tech.zeta.restaurant.project.models.enums.RestaurantStaffEnum;
import tech.zeta.restaurant.project.models.enums.RestaurantStaffStatus;

@Getter @Setter
public class RestaurantStaff {
    private int id;
    private final String userName;
    private final String password;
    private RestaurantStaffStatus status;

    private final RestaurantStaffEnum role;

    public RestaurantStaff(String userName, String password, RestaurantStaffEnum role) {
        this.userName = userName;
        this.password = password;
        this.role = role;
    }

    @Override
    public String toString() {
        return "RestaurantStaff{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                '}';
    }
}
