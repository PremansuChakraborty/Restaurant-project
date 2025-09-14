package tech.zeta.restaurant.project.models;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MenuItem {
    private int id;
    private final String name;
    private final double price;
    private final String category;

    public MenuItem(String name, double price, String category) {
        this.name = name;
        this.price = price;
        this.category = category;
    }

    @Override public String toString(){
        return "id: "+id+" name "+name+" price "+price+" category "+ category;
    }
}
