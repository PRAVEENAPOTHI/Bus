package com.BusTicketBooking.Bus.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.BusTicketBooking.Bus.model.User;

@Repository
public interface UserRepository extends JpaRepository < User, Integer>{

    User findByEmail(String emailId);
}