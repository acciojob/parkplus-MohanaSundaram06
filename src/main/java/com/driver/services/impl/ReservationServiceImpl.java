package com.driver.services.impl;

import com.driver.model.*;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.ReservationRepository;
import com.driver.repository.SpotRepository;
import com.driver.repository.UserRepository;
import com.driver.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReservationServiceImpl implements ReservationService {
    @Autowired
    UserRepository userRepository3;
    @Autowired
    SpotRepository spotRepository3;
    @Autowired
    ReservationRepository reservationRepository3;
    @Autowired
    ParkingLotRepository parkingLotRepository3;
    @Override
    public Reservation reserveSpot(Integer userId, Integer parkingLotId, Integer timeInHours, Integer numberOfWheels) throws Exception {

        Optional<User> userOptional = userRepository3.findById(userId);
        if(!userOptional.isPresent()) throw new Exception("User not Found");

        Optional<ParkingLot> parkingLotOptional = parkingLotRepository3.findById(parkingLotId);
        if ((!parkingLotOptional.isPresent())) throw new Exception("ParkingLot not found");

        User user = userOptional.get();
        ParkingLot parkingLot = parkingLotOptional.get();

        Spot spot = null;
        int price = 0;

        for(Spot theSpot : parkingLot.getSpotList()) {
            int wheels = 0;
            if (theSpot.getOccupied()) continue;

            if (theSpot.getSpotType().equals(SpotType.TWO_WHEELER)) wheels = 2;
            else if (theSpot.getSpotType().equals(SpotType.FOUR_WHEELER)) wheels = 4;
            else wheels = Integer.MAX_VALUE;

            if (numberOfWheels <= wheels && theSpot.getPricePerHour() <= price) {
                price = theSpot.getPricePerHour();
                spot = theSpot;
            }
        }
        if (spot == null) throw new Exception("All the spots are occupied");

        spot.setOccupied(true);

        Reservation reservation = new Reservation();
        reservation.setSpot(spot);
        reservation.setUser(user);
        reservation.setNumberOfHours(timeInHours);

        reservation = reservationRepository3.save(reservation);

        user.getReservationList().add(reservation);
        spot.getReservationList().add(reservation);

        userRepository3.save(user);
        spotRepository3.save(spot);

        return reservation;
    }
}

















