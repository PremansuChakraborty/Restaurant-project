package tech.zeta.restaurant.project.service;

import tech.zeta.restaurant.project.dao.BookingDAOImp;
import tech.zeta.restaurant.project.dao.BookingDAO;
import tech.zeta.restaurant.project.dao.TableInfoDAO;
import tech.zeta.restaurant.project.dao.TableInfoDAOImp;
import tech.zeta.restaurant.project.models.Booking;
import tech.zeta.restaurant.project.models.enums.BookingEnum;

import java.util.*;

public class BookingService {
   private BookingDAO bookingDAO;
   private TableInfoDAO tableInfo;
    public BookingService() {
        bookingDAO = new BookingDAOImp();
        tableInfo=new TableInfoDAOImp();
    }
    public boolean createBooking(Booking booking) {
        return bookingDAO.createBooking(booking);
    }
    public boolean cancelBooking(int bookingId) {
        return bookingDAO.updateBooking(bookingId, BookingEnum.CANCELLED);
    }
    public boolean completeBooking(int bookingId) {
        return bookingDAO.updateBooking(bookingId, BookingEnum.COMPLETED);
    }
    public Booking getBookingById(int bookingId) {
        return bookingDAO.getBookingByID(bookingId);
    }
    public int bestTableAccordingToCapacity(int capacity) {return tableInfo.bestTableAccordingToCapacity(capacity);}
    public List<Booking> getBookingsByCustomer(int customerId) {
        return bookingDAO.getBookingsByCustomer(customerId);
    }
}
