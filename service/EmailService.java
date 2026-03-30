package com.hotelbooking.app.hotelbooking.service;

import org.springframework.stereotype.Service;

@Service
public class EmailService {

    public void sendBookingConfirmation(String toEmail, Long bookingId, String hotelName) {
        System.out.println("========== EMAIL SENT ==========");
        System.out.println("To      : " + toEmail);
        System.out.println("Subject : Booking Confirmed!");
        System.out.println("Body    : Your booking #" + bookingId +
                           " at " + hotelName + " is confirmed.");
        System.out.println("================================");
    }

    public void sendBookingCancellation(String toEmail, Long bookingId) {
        System.out.println("========== EMAIL SENT ==========");
        System.out.println("To      : " + toEmail);
        System.out.println("Subject : Booking Cancelled");
        System.out.println("Body    : Your booking #" + bookingId +
                           " has been cancelled.");
        System.out.println("================================");
    }
}
