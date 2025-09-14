package tech.zeta.restaurant.project.service;

import tech.zeta.restaurant.project.models.Booking;

import java.time.*;
import java.util.List;
import java.util.Scanner;

public class CheckInServices {
    BookingService bookingService=new BookingService();
    Scanner sc;
    CheckInServices(Scanner sc){
        this.sc=sc;
    }
    int checkForBooking(int custID){
        LocalDateTime time=LocalDateTime.now();
        List<Booking> bookingList=bookingService.getBookingsByCustomer(custID);
        int bookingId=0;
        for (Booking booking : bookingList) {
            LocalDateTime bookingTime = booking.getBookingTime();
            long minutesDiff = Duration.between(bookingTime, time).toMinutes();
            if (minutesDiff > 30) {
                bookingService.cancelBooking(booking.getId());
            } else {
                bookingId = booking.getId();
            }
        }
        return bookingId;
    }
}
