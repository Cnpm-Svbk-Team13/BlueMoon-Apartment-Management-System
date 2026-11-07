package com.bluemoon.bfms.dto;

import com.bluemoon.bfms.model.ApartmentType;
import com.bluemoon.bfms.model.ApartmentStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public class ApartmentForm {

    @NotBlank(message = "Please enter the room number.")
    @Size(max = 20, message = "Room number must contain at most 20 characters.")
    private String roomNumber;

    @NotNull(message = "Please enter floor number.")
    @Min(value = 1, message = "Floor number must be between 1 and 30!")
    @Max(value = 30, message = "Floor number must be between 1 and 30!")
    private Integer floorNumber;

    @NotNull(message = "Please enter the usable area.")
    @DecimalMin(value = "0.01", message = "Usable area must be greater than zero.")
    @Digits(
            integer = 10,
            fraction = 2,
            message = "Maximum capacity of 10 digits for the integer part and 2 decimal places."
    )
    private BigDecimal areaSqm;

    @NotNull(message = "Please select apartment type!")
    private ApartmentType apartmentType;
    private ApartmentStatus status = ApartmentStatus.VACANT;
    private String description;

    public ApartmentStatus getStatus() {
        return status;
    }

    public void setStatus(ApartmentStatus status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRoomNumber(){
        return this.roomNumber;
    }

    public void setRoomNumber(String roomNumber){
        this.roomNumber = roomNumber;
    }

    public Integer getFloorNumber(){
        return this.floorNumber;
    }

    public void setFloorNumber(Integer floorNumber){
        this.floorNumber = floorNumber;
    }

    public BigDecimal getAreaSqm(){
        return this.areaSqm;
    }

    public void setAreaSqm(BigDecimal areaSqm){
        this.areaSqm = areaSqm;
    }

    public ApartmentType getApartmentType(){
        return this.apartmentType;
    }

    public void setApartmentType(ApartmentType apartmentType){
        this.apartmentType = apartmentType;
    }
}
