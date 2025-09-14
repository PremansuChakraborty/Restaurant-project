package tech.zeta.restaurant.project.models;

import lombok.Getter;
import lombok.Setter;
import tech.zeta.restaurant.project.models.enums.PaymentStatus;

@Getter @Setter
public class Billing {

    private int id;
    private final int orderId;
    private double total_amount;
    private PaymentStatus payment_status;


    public Billing(int orderId,double total_amount) {
        this.orderId = orderId;
        this.total_amount=total_amount;
    }
}
