package com.bluemoon.bfms.repository;

import com.bluemoon.bfms.model.Apartment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ApartmentRepository extends JpaRepository<Apartment, Long>{
    Optional<Apartment> findByRoomNumber(String roomNumber);
}
