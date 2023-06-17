package com.driver.services.impl;

import com.driver.model.Payment;
import com.driver.model.PaymentMode;
import com.driver.model.Reservation;
import com.driver.repository.PaymentRepository;
import com.driver.repository.ReservationRepository;
import com.driver.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    ReservationRepository reservationRepository2;
    @Autowired
    PaymentRepository paymentRepository2;

    @Override
    public Payment pay(Integer reservationId, int amountSent, String mode) throws Exception {

        if(!mode.equalsIgnoreCase("cash") && !mode.equalsIgnoreCase("upi") && !mode.equalsIgnoreCase("card"))
            throw new Exception("Payment mode not detected");

        Reservation reservation = reservationRepository2.findById(reservationId).get();

        int price = reservation.getNumberOfHours() * reservation.getSpot().getPricePerHour();

        if(amountSent < price) throw new Exception("Insufficient Amount");

        Payment payment = new Payment();

        if(mode.equalsIgnoreCase("card")) payment.setPaymentMode(PaymentMode.CARD);
        if(mode.equalsIgnoreCase("cash")) payment.setPaymentMode(PaymentMode.CASH);
        else payment.setPaymentMode(PaymentMode.UPI);

        payment.setPaymentCompleted(true);
        payment.setReservation(reservation);

        reservation.setPayment(payment);

        reservationRepository2.save(reservation);
        return payment;

    }
}
