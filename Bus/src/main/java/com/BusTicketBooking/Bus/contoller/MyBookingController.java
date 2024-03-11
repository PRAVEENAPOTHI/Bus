package com.BusTicketBooking.Bus.contoller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.BusTicketBooking.Bus.service.DefaultUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import com.BusTicketBooking.Bus.DTO.BookingsDTO;
import com.BusTicketBooking.Bus.helper.ObjectCreationHelper;
import com.BusTicketBooking.Bus.model.Bookings;
import com.BusTicketBooking.Bus.model.BusData;
import com.BusTicketBooking.Bus.model.User;
import com.BusTicketBooking.Bus.repository.BookingsRepository;
import com.BusTicketBooking.Bus.repository.UserRepository;

public class MyBookingController {
    private DefaultUserService userService;

    public MyBookingController(DefaultUserService userService) {
        super();
        this.userService = userService;
    }
    @Autowired
    BookingsRepository bookingsRepository;

    @Autowired
    UserRepository userRepository;

    @ModelAttribute("bookings")
    public BookingsDTO bookingDto() {
        return new BookingsDTO();
    }

    @GetMapping
    public String login(Model model) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        UserDetails users = (UserDetails) securityContext.getAuthentication().getPrincipal();
        User user =userRepository.findByEmail(users.getUsername());
        List<BookingsDTO> bks = new ArrayList<BookingsDTO>();
        List<Bookings> bs = bookingsRepository.findByUserId(user.getId());
        for (Bookings bookings : bs) {
            BookingsDTO bkks = ObjectCreationHelper.createBookingsDTO(bookings);
            bks.add(bkks);
        }
        model.addAttribute("userDetails", user.getName());
        Collections.reverse(bks);
        model.addAttribute("bookings",bks);
        return "myBookings";
    }

    @GetMapping("/generate/{id}")
    public String bookPage(@PathVariable int id,Model model) {
        Optional<Bookings> busdata = bookingsRepository.findById(id);
        Optional<User> users =userRepository.findById(busdata.get().getUserId());
        String user = users.get().getName();
        BookingsDTO bks = ObjectCreationHelper.createBookingsDTO(busdata.get());
        userService.sendEmail(bks, users.get(),busdata.get().getFileName());
        model.addAttribute("userDetails", user);
        List<Bookings> bs = bookingsRepository.findByUserId(users.get().getId());
        Collections.reverse(bs);
        model.addAttribute("bookings",bs);
        return "redirect:/myBooking?success";
    }

    @GetMapping("/cancel/{id}")
    public String cancelBooking(@PathVariable int id,Model model) {
        Optional<Bookings> busdata = bookingsRepository.findById(id);
        if(busdata.get().isTripStatus()==false) {
            setData(busdata.get(),model);
            return "redirect:/myBooking?alreadyCancel";
        }else {
            setData(busdata.get(),model);
            busdata.get().setTripStatus(false);
            bookingsRepository.save(busdata.get());

            return "redirect:/myBooking?successCancel";

        }
    }

    private void setData(Bookings busData, Model model) {
        Optional<User> users =userRepository.findById(busData.getUserId());
        List<Bookings> bs = bookingsRepository.findByUserId(users.get().getId());
        Collections.reverse(bs);
        model.addAttribute("bookings",bs);
    }

}
