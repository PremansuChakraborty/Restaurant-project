package tech.zeta.restaurant.project.dao;

import tech.zeta.restaurant.project.models.Billing;
import tech.zeta.restaurant.project.models.enums.PaymentStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface BillingDAO {

        int createBill(Billing bill);
        Billing getBillById(int billId);
        Billing getBillByOrderId(int orderId);
        List<Billing> getAllBills();
        List<Billing> getBillsByStatus(PaymentStatus status);
        boolean updatePaymentStatus(int billId, PaymentStatus status);
        List<Billing> getTotalSalesForDay(LocalDateTime date);


}
