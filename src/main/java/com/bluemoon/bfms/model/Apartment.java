package com.bluemoon.bfms.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.DecimalMin;
import java.time.OffsetDateTime;

@Entity 
@Table(name = "apartments")
public class Apartment{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "room_number", nullable = false, length = 20)
    private String roomNumber;

    @NotNull 
    @Min(1)
    @Max(30)
    @Column(name = "floor_number", nullable = false)
    private Integer floorNumber;

    @NotNull
    @DecimalMin("0.01")
    @Column(name = "area_sqm", nullable = false, precision = 12, scale = 2)
    private BigDecimal areaSqm;

    @Enumerated(EnumType.STRING)
    @Column(name = "apartment_type", nullable = false, length = 20)
    private ApartmentType apartmentType;
    
    @Enumerated (EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private ApartmentStatus apartmentStatus = ApartmentStatus.VACANT;
    
    @Column(name = "description", columnDefinition = "text")
    private String description;    

    @Column(name = "created_at", insertable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private OffsetDateTime updatedAt;

    protected Apartment(){
    }

    public Apartment(String roomNumber, Integer floorNumber, BigDecimal areaSqm, ApartmentType apartmentType) {
        this.roomNumber = roomNumber;
        this.floorNumber = floorNumber;
        this.areaSqm = areaSqm;
        this.apartmentType = apartmentType;
    }

    public Long getId() {
        return id;
    }

    public void updateDetails(String roomNumber, Integer floorNumber, BigDecimal areaSqm,
            ApartmentType apartmentType, ApartmentStatus status, String description) {
        this.roomNumber = roomNumber;
        this.floorNumber = floorNumber;
        this.areaSqm = areaSqm;
        this.apartmentType = apartmentType;
        this.apartmentStatus = status;
        this.description = description;
    }

    public String getRoomNumber(){
        return roomNumber;
    }

    public Integer getFloorNumber(){
        return floorNumber;
    }
    
    public BigDecimal getAreaSqm(){
        return areaSqm;
    }
    
    public ApartmentType getApartmentType(){
        return apartmentType;
    }

    public ApartmentStatus getStatus(){
        return apartmentStatus;
    }

    public String getDescription(){
        return description;
    }

    public OffsetDateTime getCreatedAt(){
        return createdAt;
    }

    public OffsetDateTime getUpdatedAt(){
        return updatedAt;
    }
}
