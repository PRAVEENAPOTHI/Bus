package com.BusTicketBooking.Bus.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.BusTicketBooking.Bus.model.Bookings;
public interface BookingsRepository extends JpaRepository<Bookings, Integer> {
    List<Bookings> findByUserId(int userId);

    }
