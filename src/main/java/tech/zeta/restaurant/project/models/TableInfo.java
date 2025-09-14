package tech.zeta.restaurant.project.models;

import lombok.Getter;
import lombok.Setter;
import tech.zeta.restaurant.project.models.enums.TableInfoEnum;

@Getter @Setter
public class TableInfo {
    private int id;
    private int capacity;
    private TableInfoEnum status;


    public TableInfo(int capacity, TableInfoEnum status) {
        this.capacity = capacity;
        this.status = status;
    }

    @Override
    public String toString() {
        return "TableInfo{" +
                "id=" + id +
                ", capacity=" + capacity +
                ", status=" + status +
                '}';
    }
}
