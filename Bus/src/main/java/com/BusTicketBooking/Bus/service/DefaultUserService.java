package com.BusTicketBooking.Bus.service;

import org.springframework.security.core.userdetails.UserDetails;

import com.BusTicketBooking.Bus.DTO.BookingsDTO;
import com.BusTicketBooking.Bus.DTO.UserRegisteredDTO;
import com.BusTicketBooking.Bus.model.Bookings;
import com.BusTicketBooking.Bus.model.User;
public interface DefaultUserService {
    User save(UserRegisteredDTO userRegisteredDTO);

    Bookings updateBookings(BookingsDTO bookingDTO,UserDetails user);

    void sendEmail(BookingsDTO bookingDTO, User users, String nameGenerator);


    org.springframework.security.core.userdetails.User loadUserByUsername(String username);
}
