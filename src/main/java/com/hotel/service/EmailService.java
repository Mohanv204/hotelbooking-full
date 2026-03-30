package com.hotel.service;

import com.hotel.entity.Booking;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendWelcomeEmail(String toEmail, String name) {
        try {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setTo(toEmail);
            msg.setSubject("Welcome to HotelBook! 🏨");
            msg.setText("Dear " + name + ",\n\nWelcome to HotelBook! Your account has been created successfully.\n\nHappy Travelling!\nHotelBook Team");
            mailSender.send(msg);
        } catch (Exception e) {
            log.warn("Email not sent to {} - {}", toEmail, e.getMessage());
        }
    }

    public void sendBookingConfirmation(Booking b) {
        try {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setTo(b.getUser().getEmail());
            msg.setSubject("Booking Confirmed! 🎉 - " + b.getHotel().getName());
            msg.setText("Dear " + b.getUser().getName() + ",\n\nYour booking is CONFIRMED!\n\n"
                    + "Booking ID : #" + b.getId() + "\n"
                    + "Hotel      : " + b.getHotel().getName() + "\n"
                    + "Location   : " + b.getHotel().getLocation() + "\n"
                    + "Room Type  : " + b.getRoom().getRoomType() + "\n"
                    + "Check-In   : " + b.getCheckInDate() + "\n"
                    + "Check-Out  : " + b.getCheckOutDate() + "\n"
                    + "Guests     : " + b.getGuests() + "\n"
                    + "Total      : ₹" + b.getTotalPrice() + "\n\n"
                    + "Have a wonderful stay!\nHotelBook Team");
            mailSender.send(msg);
        } catch (Exception e) {
            log.warn("Booking email not sent - {}", e.getMessage());
        }
    }

    public void sendCancellationEmail(Booking b) {
        try {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setTo(b.getUser().getEmail());
            msg.setSubject("Booking Cancelled - " + b.getHotel().getName());
            msg.setText("Dear " + b.getUser().getName() + ",\n\nYour booking #" + b.getId()
                    + " has been CANCELLED.\nRefund will be processed in 5-7 business days.\n\nHotelBook Team");
            mailSender.send(msg);
        } catch (Exception e) {
            log.warn("Cancellation email not sent - {}", e.getMessage());
        }
    }
}
