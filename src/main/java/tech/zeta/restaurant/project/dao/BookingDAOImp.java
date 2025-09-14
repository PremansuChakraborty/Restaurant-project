package tech.zeta.restaurant.project.dao;

import lombok.extern.slf4j.Slf4j;
import tech.zeta.restaurant.project.models.Booking;
import tech.zeta.restaurant.project.models.enums.BookingEnum;
import tech.zeta.restaurant.project.models.enums.TableInfoEnum;
import tech.zeta.restaurant.project.util.DBConnection;

import java.io.IOException;
import java.sql.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class BookingDAOImp implements BookingDAO{
    private static Connection connection;
    @Override
    public boolean createBooking(Booking booking) {
        if(booking==null){
            log.error("Booking object is null.....");
            return false;
        }
        else if(!new TableInfoDAOImp().getTableById(booking.getTableID()).getStatus().name().equals("ACTIVE")){
            log.error("Table is already booked.....");
            return false;
        }
        else {
            try {
                connection = DBConnection.getInstance().getConnection();
                new TableInfoDAOImp().updateStatus(booking.getTableID(), TableInfoEnum.OUT_OF_SERVICE);
                String sql = "INSERT INTO booking (customer_id,table_id,booking_time) " +
                        "VALUES (?,?,?);";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1, booking.getCustomerID());
                preparedStatement.setInt(2, booking.getTableID());
                preparedStatement.setTimestamp(3, Timestamp.valueOf(booking.getBookingTime()));
                int row = preparedStatement.executeUpdate();

                if (row != 0) {
                    log.info("A new booking is created");
                    return true;
                }
            } catch (IOException | SQLException e) {
                log.error("BookingDAOImp sql query fail to execute: {}", e.getMessage());
            }
            return false;
        }
    }

    @Override
    public boolean updateBooking(int id, BookingEnum status) {
        if(id==0 || status.name().isEmpty()){
            log.error("Booking  id or status not found.....");
            return false;
        }
        else {
            try {
                connection = DBConnection.getInstance().getConnection();
                String sql = "update booking set status=? where id=?;";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, status.name());
                preparedStatement.setInt(2, id);
                int row = preparedStatement.executeUpdate();

                if (row != 0) {
                    log.info("Booking status updated");
                    return true;
                }
            } catch (IOException | SQLException e) {
                log.error("BookingDAOImp sql query fail to execute: {}", e.getMessage());
            }
            return false;
        }
    }

    @Override
    public Booking getBookingByID(int id) {
        if(id==0){
            log.error("Booking  id  is not found.....");
            return null;
        }
        else {
            try {
                connection = DBConnection.getInstance().getConnection();
                String sql = "select * from booking where id=?;";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1, id);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()){
                    int bookingID=resultSet.getInt(1);
                    int cusId=resultSet.getInt(2);
                    int tableId=resultSet.getInt(3);
                    LocalDateTime bookingTime=resultSet.getTimestamp("booking_time").toLocalDateTime();
                    BookingEnum status= BookingEnum.valueOf(resultSet.getString("status"));
                   Booking booking=new Booking(cusId,tableId,bookingTime);
                   booking.setStatus(status);
                   booking.setId(id);
                   return booking;
                }
            } catch (IOException | SQLException e) {
                log.error("BookingDAOImp sql query fail to execute: {}", e.getMessage());
            }
            return null;
        }
    }

    @Override
    public List<Booking> getBookingsByCustomer(int custId) {
        List<Booking> bookingList=new ArrayList<>();
        if(custId==0){
            log.error("customer id is not found.....");
            return null;
        }
        else {
            try {
                connection = DBConnection.getInstance().getConnection();
                String sql = "select * from booking where customer_id=?;";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1, custId);
                ResultSet resultSet = preparedStatement.executeQuery();
                while(resultSet.next()){
                    int bookingID=resultSet.getInt(1);
                    int cusId=resultSet.getInt(2);
                    int tableId=resultSet.getInt(3);
                    LocalDateTime bookingTime=resultSet.getTimestamp("booking_time").toLocalDateTime();
                    BookingEnum status= BookingEnum.valueOf(resultSet.getString("status"));
                    Booking booking=new Booking(cusId,tableId,bookingTime);
                    long minutesDiff = Duration.between(bookingTime, LocalDateTime.now() ).toMinutes();
                    log.info("minutes Diff: {}",minutesDiff);
                    if (minutesDiff > 30 && status.name().equals("ACTIVE")) {
                        this.updateBooking(bookingID, BookingEnum.CANCELLED);
                        status=BookingEnum.CANCELLED;
                    }
                    booking.setStatus(status);
                    booking.setId(bookingID);
                    bookingList.add(booking);
                }
            } catch (IOException | SQLException e) {
                log.error("BookingDAOImp sql query fail to execute: {}", e.getMessage());
            }
            return bookingList;
        }
    }
}
