package com.bmstu_bureau_1440.orders.model;

import java.math.BigDecimal;
import java.time.Instant;

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
    private Instant timeWindowStart;

    @Column(updatable = false)
    @EqualsAndHashCode.Exclude
    private Instant timeWindowEnd;

    @NonNull
    @Enumerated(EnumType.STRING)
    @Column(updatable = false)
    private SensorType sensorType;

    public TaskingOrder(String aoi, BigDecimal price, Instant timeWindowStart, Instant timeWindowEnd,
            SensorType sensorType) {
        super(aoi, price);
        this.timeWindowStart = timeWindowStart;
        this.timeWindowEnd = timeWindowEnd;
        this.sensorType = sensorType;
    }

}
