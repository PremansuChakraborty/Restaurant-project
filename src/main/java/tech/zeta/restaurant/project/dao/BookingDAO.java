package tech.zeta.restaurant.project.dao;

import tech.zeta.restaurant.project.models.enums.BookingEnum;
import tech.zeta.restaurant.project.models.Booking;

import java.util.List;

public interface BookingDAO {
    boolean createBooking(Booking booking);
    boolean updateBooking(int id, BookingEnum status);
    Booking getBookingByID(int id);
    List<Booking> getBookingsByCustomer(int custId);
}
