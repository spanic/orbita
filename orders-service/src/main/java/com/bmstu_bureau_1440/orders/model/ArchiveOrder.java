package com.bmstu_bureau_1440.orders.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Entity
@DiscriminatorValue(OrderTypes.ARCHIVE)

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = true)
public class ArchiveOrder extends Order {

    @NonNull
    @Column(updatable = false)
    private LocalDate captureDate;

    @NonNull
    @Column(updatable = false)
    @Enumerated(EnumType.STRING)
    private SensorType sensorType;

    public ArchiveOrder(String aoi, BigDecimal price, LocalDate captureDate, SensorType sensorType) {
        super(aoi, price);
        this.captureDate = captureDate;
        this.sensorType = sensorType;
    }

}
