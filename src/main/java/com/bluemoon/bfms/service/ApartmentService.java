package com.bluemoon.bfms.service;

import com.bluemoon.bfms.model.Apartment;
import com.bluemoon.bfms.repository.ApartmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
public class ApartmentService {
    private final ApartmentRepository apartmentRepository;

    public ApartmentService(ApartmentRepository apartmentRepository){
        this.apartmentRepository = apartmentRepository;
    }

    @Transactional(readOnly = true)
    public Optional<Apartment> findByRoomNumber(String roomNumber){
        return apartmentRepository.findByRoomNumber(roomNumber);
    } 

}
