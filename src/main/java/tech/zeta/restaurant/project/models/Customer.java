package tech.zeta.restaurant.project.models;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Customer {
    private int id;
     private String name;

    private String phone;
    private String email; //unique email
    private String password;

    public Customer( String name, String phone, String email, String password) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.password = password;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
