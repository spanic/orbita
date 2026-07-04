package com.bmstu_bureau_1440.orders.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
@DiscriminatorValue(OrderTypes.TASKING)

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = true)
public class TaskingOrder extends Order {

    @NonNull
    @Column(updatable = false)
    @EqualsAndHashCode.Exclude
    private LocalDateTime timeWindowStart;

    @Column(updatable = false)
    @EqualsAndHashCode.Exclude
    private LocalDateTime timeWindowEnd;

    @NonNull
    @Enumerated(EnumType.STRING)
    @Column(updatable = false)
    private SensorType sensorType;

    public TaskingOrder(String aoi, BigDecimal price, LocalDateTime timeWindowStart, LocalDateTime timeWindowEnd,
            SensorType sensorType) {
        super(aoi, price);
        this.timeWindowStart = timeWindowStart;
        this.timeWindowEnd = timeWindowEnd;
        this.sensorType = sensorType;
    }

}
