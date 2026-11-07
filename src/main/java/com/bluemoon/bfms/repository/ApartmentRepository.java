package com.bluemoon.bfms.repository;

import com.bluemoon.bfms.model.Apartment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface ApartmentRepository extends JpaRepository<Apartment, Long>{
    Optional<Apartment> findByRoomNumber(String roomNumber);
    boolean existsByRoomNumber(String roomNumber);
    List<Apartment> findByFloorNumber(Integer floorNumber);
    List<Apartment> findByFloorNumberOrderByRoomNumber(Integer floorNumber);
}
