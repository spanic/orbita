package com.bmstu_bureau_1440.orders.model;

import java.math.BigDecimal;

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
@DiscriminatorValue(OrderTypes.MONITORING)

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = true)
public class MonitoringOrder extends Order {

    @NonNull
    @Column(updatable = false)
    @Enumerated(EnumType.STRING)
    private Cadence cadence;

    @NonNull
    @Column(updatable = false)
    private Integer durationDays;

    public MonitoringOrder(String aoi, BigDecimal price, Cadence cadence, Integer durationDays) {
        super(aoi, price);
        this.cadence = cadence;
        this.durationDays = durationDays;
    }

}
