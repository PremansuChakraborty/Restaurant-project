package tech.zeta.restaurant.project.models;

import lombok.Getter;
import lombok.Setter;
import tech.zeta.restaurant.project.models.enums.BookingEnum;

import java.time.LocalDateTime;

@Getter @Setter
public class Booking {

    private int id;
    private final int customerID;
    private final int tableID;
    private LocalDateTime bookingTime;
    private BookingEnum status;

    public Booking( int customerID, int tableID, LocalDateTime dateTime) {
        this.customerID = customerID;
        this.tableID = tableID;
        this.bookingTime=dateTime;
        this.status=BookingEnum.BOOKED;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "id=" + id +
                ", customerID=" + customerID +
                ", tableID=" + tableID +
                ", bookingTime=" + bookingTime +
                ", status=" + status +
                '}';
    }
}
